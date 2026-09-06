package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.constant.HttpStatus;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.redis.service.RedisService;
import com.utfinancing.financehub.engine.constants.RedisConstant;
import com.utfinancing.financehub.engine.enums.AssistFlagEnum;
import com.utfinancing.financehub.engine.enums.ContractBalanceColumnsEnum;
import com.utfinancing.financehub.engine.enums.DictTypeEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.model.dto.ContractBalanceLatestQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ContractBalanceLatestDTO;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ContractBalanceLatestVO;
import com.utfinancing.financehub.engine.finance.entity.ContractBalanceLatestEntity;
import com.utfinancing.financehub.engine.finance.mapper.ContractBalanceLatestMapper;
import com.utfinancing.financehub.engine.finance.service.IContractBalanceLatestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.parser.BooleanNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-30
 * @Description :  ContractBalanceLatest服务实现类
 * @Modified :
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ContractBalanceLatestServiceImpl extends ServiceImpl<ContractBalanceLatestMapper, ContractBalanceLatestEntity> implements IContractBalanceLatestService {

    private final ContractBalanceLatestMapper contractBalanceLatestMapper;
    private final RedisService redisService;
    private final RemoteDictService remoteDictService;

    @Override
    public Long saveContractBalanceLatest(ContractBalanceLatestDTO dto) {
        ContractBalanceLatestEntity entity = BeanUtil.copyProperties(dto, ContractBalanceLatestEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateContractBalanceLatest(Long id, ContractBalanceLatestDTO dto) {
        ContractBalanceLatestEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ContractBalanceLatestDTO getContractBalanceLatestDTOById(Long id) {
        ContractBalanceLatestEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ContractBalanceLatestDTO.class);
    }

    @Override
    public IPage<ContractBalanceLatestVO> selectPage(ContractBalanceLatestQueryDTO queryDTO) {
        LambdaQueryWrapper<ContractBalanceLatestEntity> queryWrapper = Wrappers.<ContractBalanceLatestEntity>lambdaQuery();
        //这里注入查询条件
        IPage<ContractBalanceLatestEntity> entityIPage = contractBalanceLatestMapper.selectPage(new Page<ContractBalanceLatestEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, ContractBalanceLatestVO.class);
    }



    public List<ContractBalanceLatestEntity> selectContractBalanceLatestByCode(List<String> contractCode) {
        LambdaQueryWrapper<ContractBalanceLatestEntity> lambdaQueryWrapper = Wrappers.<ContractBalanceLatestEntity>lambdaQuery();
        lambdaQueryWrapper.eq(ContractBalanceLatestEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        lambdaQueryWrapper.in(ContractBalanceLatestEntity::getContractCode, contractCode);
        return this.getBaseMapper().selectList(lambdaQueryWrapper);
    }

    public Map<String, ContractBalanceLatestEntity> selectContractBalanceLatestMap(List<String> contractCode) {
        List<ContractBalanceLatestEntity> contractBalanceLatestEntityList = this.selectContractBalanceLatestByCode(contractCode);
        if (contractBalanceLatestEntityList == null || contractBalanceLatestEntityList.isEmpty()) {
            return new HashMap<>();
        }
        return contractBalanceLatestEntityList.stream().collect(Collectors.toMap(e->getBusKey(e), (e)->e, (a, b)-> b));
    }

    public String getBusKey(ContractBalanceLatestEntity entity) {
        StringBuffer result = new StringBuffer(entity.getContractCode()).append("|");
        result.append(entity.getBusinessCode()).append("|");
        result.append(entity.getClientCode());
        return result.toString();
    }

    @Override
    public Map<String, Object> getLastBalanceMap(String businessCode, String clientCode, String contractCode,String orgId,String billContractCode) {
        long start = System.currentTimeMillis();
        LambdaQueryWrapper<ContractBalanceLatestEntity> lambdaQueryWrapper = Wrappers.<ContractBalanceLatestEntity>lambdaQuery();
//        lambdaQueryWrapper.eq(ContractBalanceLatestEntity::getBusinessCode, businessCode);
//        lambdaQueryWrapper.eq(ContractBalanceLatestEntity::getDelFlag,"0");
        if (StrUtil.isNotBlank(clientCode) && !"null".equals(clientCode)) {
            lambdaQueryWrapper.eq(ContractBalanceLatestEntity::getClientCode, clientCode);
        } else {
            //lambdaQueryWrapper.isNull(ContractBalanceLatestEntity::getClientCode);
            // 将 null 和空字符串条件用括号包装
            lambdaQueryWrapper.and(wrapper -> wrapper
                    .isNull(ContractBalanceLatestEntity::getClientCode)
                    .or()
                    .eq(ContractBalanceLatestEntity::getClientCode, ""));
        }
        if (StrUtil.isNotBlank(contractCode) && !"null".equals(contractCode)) {
            lambdaQueryWrapper.eq(ContractBalanceLatestEntity::getContractCode, contractCode);
        } else {
            //lambdaQueryWrapper.isNull(ContractBalanceLatestEntity::getContractCode);
            lambdaQueryWrapper.and(wrapper -> wrapper
                    .isNull(ContractBalanceLatestEntity::getContractCode)
                    .or()
                    .eq(ContractBalanceLatestEntity::getContractCode, ""));
        }
        if (StrUtil.isNotBlank(orgId) && !"null".equals(orgId)) {
            lambdaQueryWrapper.eq(ContractBalanceLatestEntity::getOrgId, orgId);
        } else {
            //lambdaQueryWrapper.isNull(ContractBalanceLatestEntity::getOrgId);
            lambdaQueryWrapper.and(wrapper -> wrapper
                    .isNull(ContractBalanceLatestEntity::getOrgId)
                    .or()
                    .eq(ContractBalanceLatestEntity::getOrgId, ""));
        }
        if (StrUtil.isNotBlank(billContractCode) && !"null".equals(billContractCode)) {
            lambdaQueryWrapper.eq(ContractBalanceLatestEntity::getBillContractCode, billContractCode);
        } else {
            //lambdaQueryWrapper.isNull(ContractBalanceLatestEntity::getBillContractCode);
            lambdaQueryWrapper.and(wrapper -> wrapper
                    .isNull(ContractBalanceLatestEntity::getBillContractCode)
                    .or()
                    .eq(ContractBalanceLatestEntity::getBillContractCode, ""));
        }
        lambdaQueryWrapper.orderByDesc(ContractBalanceLatestEntity::getId);
        lambdaQueryWrapper.last("limit 1");
        Map<String, Object> rowMap = this.getMap(lambdaQueryWrapper);
        if (MapUtil.isEmpty(rowMap)) {
            Map<String, Object> emptyMap = new HashMap<>();
            emptyMap.put(ContractBalanceColumnsEnum.BUSINESS_CODE.getCode(), businessCode);
            emptyMap.put(ContractBalanceColumnsEnum.CLIENT_CODE.getCode(), clientCode);
            emptyMap.put(ContractBalanceColumnsEnum.CONTRACT_CODE.getCode(), contractCode);
            //其余余额字段补0
            fillBalanceZero(emptyMap);
            return emptyMap;
        }
        //过滤发生额和其他字段，只保留余额字段
        for (Iterator<Map.Entry<String, Object>> it = rowMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Object> item = it.next();
            if (!StrUtil.containsAny(item.getKey(), "_balance") && !StrUtil.equals(item.getKey(), "id")) {
                it.remove();
            }
        }
        rowMap.put(ContractBalanceColumnsEnum.BUSINESS_CODE.getCode(), businessCode);
        rowMap.put(ContractBalanceColumnsEnum.CLIENT_CODE.getCode(), clientCode);
        rowMap.put(ContractBalanceColumnsEnum.CONTRACT_CODE.getCode(), contractCode);
        long end = System.currentTimeMillis();
        log.info("getLastBalanceMap 查询耗时：{}ms",(end-start));
        return rowMap;
    }

    @Override
    public Boolean insertMap(VoucherDTO voucherDTO, Map<String, Object> insertMap) {
        insertMap.put("id", IdWorker.getId());
        insertMap.put("update_time", LocalDateTime.now());
        insertMap.put("create_time", LocalDateTime.now());
        insertMap.put("del_flag","0");
        List<String> columns = ListUtil.toList();
        List<Object> values = ListUtil.toList();
        for (Map.Entry<String, Object> mapEntry : insertMap.entrySet()) {
            columns.add(mapEntry.getKey());
            values.add(mapEntry.getValue());
        }
        //保存前判断是否已经存在数据，存在则删除再保存
        deleteExistLatest(voucherDTO.getBusinessCode(),voucherDTO.getClientCode(),voucherDTO.getContractCode());
        return contractBalanceLatestMapper.insertContractBalanceLatest(columns, values);
    }

    @Override
    public Boolean insertMap(Map<String, Object> insertMap) {
        insertMap.put("id", IdWorker.getId());
        insertMap.put("update_time", LocalDateTime.now());
        insertMap.put("create_time", LocalDateTime.now());
        insertMap.put("del_flag","0");
        List<String> columns = ListUtil.toList();
        List<Object> values = ListUtil.toList();
        for (Map.Entry<String, Object> mapEntry : insertMap.entrySet()) {
            columns.add(mapEntry.getKey());
            values.add(mapEntry.getValue());
        }
        //保存前判断是否已经存在数据，存在则删除再保存
        return contractBalanceLatestMapper.insertContractBalanceLatest(columns, values);
    }

    @Override
    public Map<String, Object> queryFullLastBalanceMap(String businessCode, String orgId, String contractCode, String clientCode, String billContractCode, List<String> assistFlags) {
        Map<String, Object> fullBalanceMap = new HashMap<>(1000);
        //1.查询默认余额：接口传递什么维度，就查询什么维度
        Map<String, Object> defaultBalanceMap = this.getLastBalanceMap(businessCode, clientCode, contractCode,orgId,billContractCode);
        fullBalanceMap.putAll(defaultBalanceMap);
        //2.查询维度余额：根据场景的维度配置查询余额
        for (String assistFlag: assistFlags){
            Map<String, Object> assistBalanceMap = null;
            if (StrUtil.isBlank(assistFlag)){
                assistFlag = "none";
                assistBalanceMap = this.getLastBalanceMap(businessCode, null, null,orgId,billContractCode);
            }else{
                List<String> entryAssistsFlag = StrUtil.split(assistFlag, "_");
                String queryContractCode = null;
                String queryClientCode = null;
                if (CollectionUtil.contains(entryAssistsFlag, AssistFlagEnum.CLIENT.getCode())){
                    queryClientCode = clientCode;
                }
                if (CollectionUtil.contains(entryAssistsFlag, AssistFlagEnum.CONTRACT.getCode())){
                    queryContractCode = contractCode;
                }
                assistBalanceMap = getLastBalanceMap(businessCode, queryClientCode, queryContractCode,orgId,billContractCode);
            }
            //替换key
            fullBalanceMap.putAll(replaceBalanceMapKey(assistBalanceMap, assistFlag));
        }
        return fullBalanceMap;
    }

    @Override
    public void updateLastBalanceMap(Map<String, Object> rowMap) {
        Long id = (Long) rowMap.get("id");
        rowMap.put("update_time", LocalDateTime.now());
        rowMap.put("create_time", LocalDateTime.now());
        rowMap.remove("id");
        contractBalanceLatestMapper.updateContractBalance(rowMap, id);
    }

    public Map<String, Object> replaceBalanceMapKey(Map<String, Object> balanceMap, String keySuffix){
        Map<String, Object> newMap = new HashMap<>();
        Iterator<Map.Entry<String, Object>> it = balanceMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, Object> entry = it.next();
            if (CharSequenceUtil.endWith(entry.getKey(), "_balance")){
                newMap.put(entry.getKey()+"_"+keySuffix, entry.getValue());
            }
        }
        return newMap;
    }

    public void deleteExistLatest(String businessCode,String clientCode,String contractCode) {
        LambdaQueryWrapper<ContractBalanceLatestEntity> lambdaQueryWrapper = Wrappers.<ContractBalanceLatestEntity>lambdaQuery();
        lambdaQueryWrapper.eq(ContractBalanceLatestEntity::getBusinessCode, businessCode);
        if (StrUtil.isNotBlank(clientCode)) {
            lambdaQueryWrapper.eq(ContractBalanceLatestEntity::getClientCode, clientCode);
        } else {
            lambdaQueryWrapper.isNull(ContractBalanceLatestEntity::getClientCode);
        }
        if (StrUtil.isNotBlank(contractCode)) {
            lambdaQueryWrapper.eq(ContractBalanceLatestEntity::getContractCode, contractCode);
        } else {
            lambdaQueryWrapper.isNull(ContractBalanceLatestEntity::getContractCode);
        }
        List<ContractBalanceLatestEntity> latestEntityList = list(lambdaQueryWrapper);
        if (CollectionUtil.isNotEmpty(latestEntityList)) {
            this.removeBatchByIds(latestEntityList.stream().map(ContractBalanceLatestEntity::getId).collect(Collectors.toList()));
        }
    }


    public void fillBalanceZero(Map<String, Object> emptyMap){
        //查询金额类型数据字典
        R<List<SysDictData>> dictListR =  listDictTypeData(DictTypeEnum.CASH_TYPE.getCode());
        if (dictListR.getCode() == HttpStatus.SUCCESS && CollectionUtil.isNotEmpty(dictListR.getData())) {
            for (SysDictData dictData : dictListR.getData()) {
                emptyMap.put(StrUtil.addSuffixIfNot(dictData.getDictValue(), "_balance"), BigDecimal.ZERO);
            }
        }
    }


    private R<List<SysDictData>> listDictTypeData (String dictType) {
        R<List<SysDictData>> sysDictR = redisService.getCacheObject(String.format(RedisConstant.V_DICT_SYS_CASH_TYPE,dictType));
        if (ObjectUtil.isNull(sysDictR)) {
            sysDictR =  remoteDictService.listDictData(dictType);
            if (ObjectUtil.isNotNull(sysDictR)) {
                redisService.setCacheObject(String.format(RedisConstant.V_DICT_SYS_CASH_TYPE,dictType), sysDictR, RedisConstant.TIME_OUT, TimeUnit.MINUTES);
            }
        }
        return sysDictR;
    }
}

