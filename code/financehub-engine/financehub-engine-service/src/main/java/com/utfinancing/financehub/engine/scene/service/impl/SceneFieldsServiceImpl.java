package com.utfinancing.financehub.engine.scene.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.constant.HttpStatus;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.redis.service.RedisService;
import com.utfinancing.financehub.engine.constants.RedisConstant;
import com.utfinancing.financehub.engine.enums.DataTypeEnum;
import com.utfinancing.financehub.engine.enums.DictTypeEnum;
import com.utfinancing.financehub.engine.enums.EditorFunctionEnum;
import com.utfinancing.financehub.engine.enums.EditorOptionTypeEnum;
import com.utfinancing.financehub.engine.scene.entity.SceneEntity;
import com.utfinancing.financehub.engine.scene.mapper.SceneMapper;
import com.utfinancing.financehub.engine.scene.model.dto.*;
import com.utfinancing.financehub.engine.scene.model.vo.SceneFieldsVO;
import com.utfinancing.financehub.engine.scene.entity.SceneFieldsEntity;
import com.utfinancing.financehub.engine.scene.mapper.SceneFieldsMapper;
import com.utfinancing.financehub.engine.scene.service.ISceneFieldsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.scene.service.ITaxRateService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author : lixin
 * @Date : Create in 2023-08-25
 * @Description :  SceneFields服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class SceneFieldsServiceImpl extends ServiceImpl<SceneFieldsMapper, SceneFieldsEntity> implements ISceneFieldsService {

    private final SceneFieldsMapper sceneFieldsMapper;
    private final SceneMapper sceneMapper;
    private final RemoteDictService remoteDictService;
    private final ITaxRateService taxRateService;
    private final RedisService redisService;

    @Override
    public Long saveSceneFields(SceneFieldsSaveDTO dto) {
        SceneFieldsEntity parent = validateAndFillParent(dto);
        SceneFieldsEntity entity = BeanUtil.copyProperties(dto, SceneFieldsEntity.class);
        LambdaQueryWrapper<SceneFieldsEntity> duplicateWrapper = Wrappers.<SceneFieldsEntity>lambdaQuery()
                .eq(SceneFieldsEntity::getSceneCode, dto.getSceneCode())
                .eq(SceneFieldsEntity::getFieldCode, dto.getFieldCode());
        if (parent == null) {
            duplicateWrapper.isNull(SceneFieldsEntity::getParentId);
        } else {
            duplicateWrapper.eq(SceneFieldsEntity::getParentId, parent.getId());
        }
        long count = this.count(duplicateWrapper);
        if (count > 0) {
            throw new ServiceException(StrUtil.format("字段[{}]已存在", dto.getFieldCode()));
        }
        if (StrUtil.isBlank(entity.getRequiredFlag())) {
            entity.setRequiredFlag("0");
        }
        if (entity.getSortNo() == null) {
            entity.setSortNo(0);
        }
        this.save(entity);
        redisService.deleteObject(String.format(RedisConstant.V_FIELD_SCENE_CODE_KEY, dto.getSceneCode()));
        return entity.getId();
    }

    @Override
    public Long updateSceneFields(Long id, SceneFieldsSaveDTO dto) {
        SceneFieldsEntity entity = this.getById(id);
        if (entity == null) {
            throw new ServiceException("字段不存在");
        }
        validateAndFillParent(dto);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        redisService.deleteObject(String.format(RedisConstant.V_FIELD_SCENE_CODE_KEY, dto.getSceneCode()));
        return id;
    }

    @Override
    public Boolean removeSceneField(Long id) {
        SceneFieldsEntity entity = this.getById(id);
        if (entity == null) {
            return Boolean.TRUE;
        }
        this.remove(Wrappers.<SceneFieldsEntity>lambdaQuery().eq(SceneFieldsEntity::getParentId, id));
        boolean removed = this.removeById(id);
        redisService.deleteObject(String.format(RedisConstant.V_FIELD_SCENE_CODE_KEY, entity.getSceneCode()));
        return removed;
    }

    private SceneFieldsEntity validateAndFillParent(SceneFieldsSaveDTO dto) {
        if (dto.getParentId() == null) {
            return null;
        }
        SceneFieldsEntity parent = this.getById(dto.getParentId());
        if (parent == null || !DataTypeEnum.LIST.getCode().equals(parent.getDataType())) {
            throw new ServiceException("父字段不存在或不是List类型");
        }
        if (parent.getParentId() != null) {
            throw new ServiceException("暂不支持多层List嵌套");
        }
        if (DataTypeEnum.LIST.getCode().equals(dto.getDataType())) {
            throw new ServiceException("List明细字段只能使用字符串或数值类型");
        }
        dto.setSceneId(parent.getSceneId());
        dto.setSceneCode(parent.getSceneCode());
        dto.setSceneName(parent.getSceneName());
        return parent;
    }

    @Override
    public SceneFieldsDTO getSceneFieldsDTOById(Long id) {
        SceneFieldsEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, SceneFieldsDTO.class);
    }

    @Override
    public IPage<SceneFieldsVO> selectPage(SceneFieldsQueryDTO queryDTO) {
        LambdaQueryWrapper<SceneFieldsEntity> queryWrapper = Wrappers.<SceneFieldsEntity>lambdaQuery();
        queryWrapper.eq(StrUtil.isNotBlank(queryDTO.getSceneCode()), SceneFieldsEntity::getSceneCode, queryDTO.getSceneCode());
        queryWrapper.eq(ObjUtil.isNotNull(queryDTO.getSceneId()), SceneFieldsEntity::getSceneId, queryDTO.getSceneId());
        queryWrapper.eq(StrUtil.isNotBlank(queryDTO.getFieldCode()), SceneFieldsEntity::getFieldCode, queryDTO.getFieldCode());
        queryWrapper.like(StrUtil.isNotBlank(queryDTO.getSceneName()), SceneFieldsEntity::getSceneName, queryDTO.getSceneName());
        queryWrapper.like(StrUtil.isNotBlank(queryDTO.getFieldName()), SceneFieldsEntity::getFieldName, queryDTO.getFieldName());
        if (ObjUtil.isNotNull(queryDTO.getParentId())) {
            queryWrapper.eq(SceneFieldsEntity::getParentId, queryDTO.getParentId());
        } else {
            queryWrapper.isNull(SceneFieldsEntity::getParentId);
        }
        queryWrapper.orderByAsc(SceneFieldsEntity::getSortNo).orderByAsc(SceneFieldsEntity::getId);
        IPage<SceneFieldsEntity> entityIPage = sceneFieldsMapper.selectPage(new Page<SceneFieldsEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, SceneFieldsVO.class);
    }

    @Override
    public List<SceneFieldsDTO> listSceneFieldsByCode(String sceneCode) {
        List<SceneFieldsEntity> fieldsEntityList = this.list(Wrappers.<SceneFieldsEntity>lambdaQuery()
                .eq(SceneFieldsEntity::getSceneCode, sceneCode)
                .isNull(SceneFieldsEntity::getParentId));
        return ListBeanUtil.copyList(fieldsEntityList, SceneFieldsDTO.class);
    }

    public List<SceneFieldsDTO> listSceneFieldsBySceneId(Long sceneId) {
        List<SceneFieldsEntity> fieldsEntityList = this.list(Wrappers.<SceneFieldsEntity>lambdaQuery()
                .eq(SceneFieldsEntity::getSceneId, sceneId)
                .isNull(SceneFieldsEntity::getParentId));
        return ListBeanUtil.copyList(fieldsEntityList, SceneFieldsDTO.class);
    }

    @Override
    public Map<String, Object> selectSceneFieldsMapByCode(String sceneCode) {
        List<SceneFieldsDTO> fieldsDTOList = this.listSceneFieldsByCode(sceneCode);
        return fieldsDTOList.stream().collect(Collectors.toMap(e -> e.getSceneName() + "接口表." + e.getFieldName(), e -> e.getFieldCode()));
    }

    @Override
    public List<EditorOptionDTO> getTreeBySceneId(Long sceneId) {
        List<EditorOptionDTO> optionDTOList = new ArrayList<>();

        //接口表字段
        optionDTOList.addAll(this.optionListInterfaceFields(sceneId));
        //起租场景的税额、不含税金额和内部路由变量由引擎计算，
        //在编辑器中单列展示，避免被误认为业务接口入参。
        optionDTOList.addAll(this.optionListLeaseStartCalculatedFields(sceneId));
        //合同表
        optionDTOList.addAll(this.optionListContractFields());
        //客户表
        optionDTOList.addAll(this.optionListClientFields());

        return optionDTOList;
    }

    private List<EditorOptionDTO> optionListLeaseStartCalculatedFields(Long sceneId) {
        SceneEntity scene = sceneMapper.selectById(sceneId);
        if (scene == null || !"HTQZ".equals(scene.getSceneCode())) {
            return ListUtil.empty();
        }
        EditorOptionDTO option = new EditorOptionDTO();
        option.setName("起租计算结果表");
        List<EditorOptionItemDTO> children = new ArrayList<>();
        addCalculatedField(children, option.getName(), "本金结转方式", DataTypeEnum.STRING.getCode());
        String[] amountFields = {
                "起租不含税本金", "起租不含税利息", "起租不含税留购价", "起租利息税额", "起租留购价税额",
                "已收手续费未摊销不含税金额", "未收取手续费不含税金额", "未收手续费未摊销不含税金额",
                "未收取手续费税额", "未收取手续费未摊销税额", "未收取手续费含税金额",
                "未摊销手续费不含税合计", "应收手续费不含税金额", "应收手续费税额",
                "经营租赁资产成本不含税金额", "客户融资不含税总额"
        };
        for (String field : amountFields) {
            addCalculatedField(children, option.getName(), field, DataTypeEnum.NUMBER.getCode());
        }
        option.setChildren(children);
        return ListUtil.toList(option);
    }

    private void addCalculatedField(List<EditorOptionItemDTO> children, String tableName,
                                    String fieldName, String dataType) {
        EditorOptionItemDTO item = new EditorOptionItemDTO();
        item.setType(EditorOptionTypeEnum.FIELD.getCode());
        item.setName(fieldName);
        item.setCode(tableName + "." + fieldName);
        item.setDesc("会计引擎根据起租原始数据计算，不作为业务接口入参");
        item.setDataType(dataType);
        children.add(item);
    }


    @Override
    public List<EditorOptionDTO> listEditorOption(Long sceneId) {
        List<EditorOptionDTO> optionDTOList = new ArrayList<>();
        //函数
        optionDTOList.addAll(optionListFunction());
        //接口表字段
        optionDTOList.addAll(this.optionListInterfaceFields(sceneId));
        //内部计算结果可参与规则编辑，但不属于业务接口入参。
        optionDTOList.addAll(this.optionListLeaseStartCalculatedFields(sceneId));
        //金额类型参数
//        optionDTOList.addAll(this.optionListCashType());
        //合同余额表
        optionDTOList.addAll(this.optionListContractBalance());
        //合同表
        optionDTOList.addAll(this.optionListContractFields());
        //合同月表
        optionDTOList.addAll(this.optionListContractMonthFields());
        //客户表
        optionDTOList.addAll(this.optionListClientFields());
        //税率表
        optionDTOList.addAll(this.optionListTaxRate());
        return optionDTOList;
    }

    @Override
    public List<SceneFieldsDTO> listAllSceneFieldsDistinct() {
        List<SceneFieldsEntity> fieldsEntityList = this.list(new QueryWrapper<SceneFieldsEntity>()
                .select("distinct field_code", "field_name").isNull("parent_id"));
        return ListBeanUtil.copyList(fieldsEntityList, SceneFieldsDTO.class);
    }

    @Override
    public List<SceneFieldsDTO> selectNumberSceneFields(String sceneCode) {
        List<SceneFieldsEntity> fieldsEntityList = this.list(Wrappers.<SceneFieldsEntity>lambdaQuery()
                .eq(SceneFieldsEntity::getSceneCode, sceneCode)
                .isNull(SceneFieldsEntity::getParentId)
                .eq(SceneFieldsEntity::getDataType, DataTypeEnum.NUMBER.getCode()));
        return ListBeanUtil.copyList(fieldsEntityList, SceneFieldsDTO.class);
    }

    @Override
    public List<SceneFieldsDTO> selectListSceneFields(String sceneCode) {
        List<SceneFieldsEntity> fieldsEntityList = this.list(Wrappers.<SceneFieldsEntity>lambdaQuery()
                .eq(SceneFieldsEntity::getSceneCode, sceneCode)
                .isNull(SceneFieldsEntity::getParentId)
                .eq(SceneFieldsEntity::getDataType, DataTypeEnum.LIST.getCode()));
        return ListBeanUtil.copyList(fieldsEntityList, SceneFieldsDTO.class);
    }

    /**
     * 接口表字段
     *
     * @param sceneId
     * @return
     */
    private List<EditorOptionDTO> optionListInterfaceFields(Long sceneId) {
        List<EditorOptionDTO> result = new ArrayList<>();
        SceneEntity sceneEntity = sceneMapper.selectById(sceneId);
        if (ObjUtil.isEmpty(sceneEntity)) {
            return result;
        }
        EditorOptionDTO sceneFieldsTreeDTO = new EditorOptionDTO();
        sceneFieldsTreeDTO.setId(sceneId);
        sceneFieldsTreeDTO.setName(sceneEntity.getSceneName() + "接口表");
        result.add(sceneFieldsTreeDTO);

        List<SceneFieldsDTO> sceneFieldsDTOS = listSceneFieldsBySceneId(sceneId);
        if (CollectionUtils.isNotEmpty(sceneFieldsDTOS)) {
            List<EditorOptionItemDTO> children = sceneFieldsDTOS.stream().map(e -> {
                EditorOptionItemDTO sceneFieldsTreeChildrenDTO = new EditorOptionItemDTO();
                sceneFieldsTreeChildrenDTO.setId(e.getId());
                sceneFieldsTreeChildrenDTO.setType(EditorOptionTypeEnum.FIELD.getCode());
                sceneFieldsTreeChildrenDTO.setName(e.getFieldName());
                sceneFieldsTreeChildrenDTO.setCode(sceneFieldsTreeDTO.getName() + "." + e.getFieldName());
                sceneFieldsTreeChildrenDTO.setDataType(e.getDataType());
                return sceneFieldsTreeChildrenDTO;
            }).collect(Collectors.toList());
            sceneFieldsTreeDTO.setChildren(children);
        }
        return result;
    }

    /**
     * 金额类型选项
     */
    private List<EditorOptionDTO> optionListCashType() {
        R<List<SysDictData>> dictListR = remoteDictService.listDictData(DictTypeEnum.CASH_TYPE.getCode());
        if (dictListR.getCode() == HttpStatus.SUCCESS && CollectionUtil.isNotEmpty(dictListR.getData())) {
            EditorOptionDTO functionOption = new EditorOptionDTO();
            List<EditorOptionItemDTO> functionItemList = new ArrayList<>();
            functionOption.setName("金额类型参数表");
            functionOption.setChildren(functionItemList);
            for (SysDictData dictData : dictListR.getData()) {
                EditorOptionItemDTO itemDTO = new EditorOptionItemDTO();
                itemDTO.setName(dictData.getDictLabel());
                itemDTO.setCode(functionOption.getName() + "." + dictData.getDictLabel());
                itemDTO.setType(EditorOptionTypeEnum.FIELD.getCode());
                itemDTO.setDataType(DataTypeEnum.STRING.getCode());
                functionItemList.add(itemDTO);
            }
            return ListUtil.toList(functionOption);
        }
        return ListUtil.empty();
    }

    /**
     * 合同余额表
     *
     * @return
     */
    private List<EditorOptionDTO> optionListContractBalance() {
        R<List<SysDictData>> dictListR = remoteDictService.listDictData(DictTypeEnum.CASH_TYPE.getCode());
        if (dictListR.getCode() == HttpStatus.SUCCESS && CollectionUtil.isNotEmpty(dictListR.getData())) {
            EditorOptionDTO functionOption = new EditorOptionDTO();
            List<EditorOptionItemDTO> functionItemList = new ArrayList<>();
            functionOption.setName("科目余额表");
            functionOption.setChildren(functionItemList);
            for (SysDictData dictData : dictListR.getData()) {
                EditorOptionItemDTO itemDTO = new EditorOptionItemDTO();
                itemDTO.setName(dictData.getDictLabel() + "余额");
                itemDTO.setCode(functionOption.getName() + "." + dictData.getDictLabel() + "余额");
                itemDTO.setType(EditorOptionTypeEnum.FIELD.getCode());
                itemDTO.setDataType(DataTypeEnum.STRING.getCode());
                functionItemList.add(itemDTO);
            }
            return ListUtil.toList(functionOption);
        }
        return ListUtil.empty();
    }

    /**
     * 合同表
     *
     * @return
     */
    private List<EditorOptionDTO> optionListContractFields() {
        R<List<SysDictData>> dictListR = remoteDictService.listDictData(DictTypeEnum.CONTRACT_FIELDS.getCode());
        if (dictListR.getCode() == HttpStatus.SUCCESS && CollectionUtil.isNotEmpty(dictListR.getData())) {
            EditorOptionDTO functionOption = new EditorOptionDTO();
            List<EditorOptionItemDTO> functionItemList = new ArrayList<>();
            functionOption.setName("合同表");
            functionOption.setChildren(functionItemList);
            for (SysDictData dictData : dictListR.getData()) {
                EditorOptionItemDTO itemDTO = new EditorOptionItemDTO();
                itemDTO.setName(dictData.getDictLabel());
                itemDTO.setCode(functionOption.getName() + "." + dictData.getDictLabel());
                itemDTO.setType(EditorOptionTypeEnum.FIELD.getCode());
                itemDTO.setDataType(dictData.getRemark());
                functionItemList.add(itemDTO);
            }
            return ListUtil.toList(functionOption);
        }
        return ListUtil.empty();
    }    /**
     * 合同表
     *
     * @return
     */
    private List<EditorOptionDTO> optionListContractMonthFields() {
        R<List<SysDictData>> dictListR = remoteDictService.listDictData(DictTypeEnum.CONTRACT_MONTH_FIELDS.getCode());
        if (dictListR.getCode() == HttpStatus.SUCCESS && CollectionUtil.isNotEmpty(dictListR.getData())) {
            EditorOptionDTO functionOption = new EditorOptionDTO();
            List<EditorOptionItemDTO> functionItemList = new ArrayList<>();
            functionOption.setName("合同月表");
            functionOption.setChildren(functionItemList);
            for (SysDictData dictData : dictListR.getData()) {
                EditorOptionItemDTO itemDTO = new EditorOptionItemDTO();
                itemDTO.setName(dictData.getDictLabel());
                itemDTO.setCode(functionOption.getName() + "." + dictData.getDictLabel());
                itemDTO.setType(EditorOptionTypeEnum.FIELD.getCode());
                itemDTO.setDataType(dictData.getRemark());
                functionItemList.add(itemDTO);
            }
            return ListUtil.toList(functionOption);
        }
        return ListUtil.empty();
    }

    /**
     * 客户表
     *
     * @return
     */
    private List<EditorOptionDTO> optionListClientFields() {
        R<List<SysDictData>> dictListR = remoteDictService.listDictData(DictTypeEnum.CLIENT_FIELDS.getCode());
        if (dictListR.getCode() == HttpStatus.SUCCESS && CollectionUtil.isNotEmpty(dictListR.getData())) {
            EditorOptionDTO functionOption = new EditorOptionDTO();
            List<EditorOptionItemDTO> functionItemList = new ArrayList<>();
            functionOption.setName("客户表");
            functionOption.setChildren(functionItemList);
            for (SysDictData dictData : dictListR.getData()) {
                EditorOptionItemDTO itemDTO = new EditorOptionItemDTO();
                itemDTO.setName(dictData.getDictLabel());
                itemDTO.setCode(functionOption.getName() + "." + dictData.getDictLabel());
                itemDTO.setType(EditorOptionTypeEnum.FIELD.getCode());
                itemDTO.setDataType(dictData.getRemark());
                functionItemList.add(itemDTO);
            }
            return ListUtil.toList(functionOption);
        }
        return ListUtil.empty();
    }


    /**
     * 税率表
     */
    private List<EditorOptionDTO> optionListTaxRate() {
        List<TaxRateDTO> taxRateDTOList = taxRateService.queryAllForEditor();
        EditorOptionDTO functionOption = new EditorOptionDTO();
        List<EditorOptionItemDTO> functionItemList = new ArrayList<>();
        functionOption.setName("税率");
        functionOption.setChildren(functionItemList);
        for (TaxRateDTO taxRateDTO : taxRateDTOList) {
            EditorOptionItemDTO itemDTO = new EditorOptionItemDTO();
            String name = taxRateDTO.getBusinessCode() + "." + taxRateDTO.getFundType() + "." + taxRateDTO.getLeaseType();
            if (StrUtil.isNotBlank(taxRateDTO.getLeaseSubType())) {
                name = name + "." + taxRateDTO.getLeaseSubType();
            }
            itemDTO.setName(name);
            itemDTO.setCode(functionOption.getName() + "." + name);
            itemDTO.setType(EditorOptionTypeEnum.FIELD.getCode());
            itemDTO.setDataType(DataTypeEnum.NUMBER.getCode());
            itemDTO.setValue(taxRateDTO.getTaxRate());
            functionItemList.add(itemDTO);
        }
        return ListUtil.toList(functionOption);
    }

    /**
     * 函数选项
     */
    private List<EditorOptionDTO> optionListFunction() {
        EditorOptionDTO functionOption = new EditorOptionDTO();
        List<EditorOptionItemDTO> functionItemList = new ArrayList<>();
        functionOption.setName("函数");
        functionOption.setChildren(functionItemList);
        for (EditorFunctionEnum functionEnum : EditorFunctionEnum.values()) {
            EditorOptionItemDTO itemDTO = new EditorOptionItemDTO();
            itemDTO.setName(functionEnum.getCode());
            itemDTO.setCode(functionEnum.getCode());
            itemDTO.setType(EditorOptionTypeEnum.FUN.getCode());
            itemDTO.setDesc(functionEnum.getDesc());
            functionItemList.add(itemDTO);
        }
        return ListUtil.toList(functionOption);
    }


}

