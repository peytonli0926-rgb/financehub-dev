package com.utfinancing.financehub.engine.rule.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.model.vo.ContractTransactionVO;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import com.utfinancing.financehub.engine.rule.model.dto.InterfaceDataQueryDTO;
import com.utfinancing.financehub.engine.rule.model.dto.InterfaceDataDTO;
import com.utfinancing.financehub.engine.rule.model.dto.SelectHtqzSceneInputDTO;
import com.utfinancing.financehub.engine.rule.model.vo.InterfaceDataVO;
import com.utfinancing.financehub.engine.rule.entity.InterfaceDataEntity;
import com.utfinancing.financehub.engine.rule.mapper.InterfaceDataMapper;
import com.utfinancing.financehub.engine.rule.service.IInterfaceDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.rule.service.IPeriodCodeService;
import com.utfinancing.financehub.engine.rule.util.RuleUtil;
import com.utfinancing.financehub.engine.scene.entity.SceneVoucherConditionEntity;
import com.utfinancing.financehub.engine.verification.model.dto.VerificationPaybackQueryDTO;
import com.utfinancing.financehub.engine.verification.model.vo.VerificationPaybackDetailsVO;
import com.utfinancing.financehub.engine.verification.model.vo.VerificationPaybackVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Author : lixin
 * @Date : Create in 2023-09-14
 * @Description :  InterfaceData服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class InterfaceDataServiceImpl extends ServiceImpl<InterfaceDataMapper, InterfaceDataEntity> implements IInterfaceDataService {

    private final InterfaceDataMapper interfaceDataMapper;

    private final IPeriodCodeService periodCodeService;

    @Override
    public Long saveInterfaceData(InterfaceDataDTO dto) {
        InterfaceDataEntity entity = BeanUtil.copyProperties(dto, InterfaceDataEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateInterfaceData(Long id, InterfaceDataDTO dto) {
        InterfaceDataEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public InterfaceDataDTO getInterfaceDataDTOById(Long id) {
        InterfaceDataEntity entity = this.getById(id);
        if (entity == null) return null;
        InterfaceDataDTO dto = BeanUtil.copyProperties(entity, InterfaceDataDTO.class);
        dto.setInterfaceData(entity.getInterfaceData());
        return dto;
    }

//    @Override
//    public IPage<InterfaceDataVO> selectPage(InterfaceDataQueryDTO queryDTO) {
//        LambdaQueryWrapper<InterfaceDataEntity> queryWrapper = Wrappers.<InterfaceDataEntity>lambdaQuery();
//        //这里注入查询条件
//        IPage<InterfaceDataEntity> entityIPage = interfaceDataMapper.selectPage(new Page<InterfaceDataEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
//        return ListBeanUtil.copyPage(entityIPage, InterfaceDataVO.class);
//    }


    @Override
    public JSONObject selectPage(InterfaceDataQueryDTO queryDTO) {
        LambdaQueryWrapper<InterfaceDataEntity> queryWrapper = Wrappers.<InterfaceDataEntity>lambdaQuery();

        queryWrapper.eq(StringUtils.isNotBlank(queryDTO.getSystemName()), InterfaceDataEntity::getSystemName, queryDTO.getSystemName());
        queryWrapper.eq(StringUtils.isNotBlank(queryDTO.getSceneName()), InterfaceDataEntity::getSceneName, queryDTO.getSceneName());
        queryWrapper.eq(StringUtils.isNotBlank(queryDTO.getOrgName()), InterfaceDataEntity::getOrgName, queryDTO.getOrgName());
        queryWrapper.like(StringUtils.isNotBlank(queryDTO.getClientCode()), InterfaceDataEntity::getClientCode, queryDTO.getClientCode());
        queryWrapper.like(StringUtils.isNotBlank(queryDTO.getClientName()), InterfaceDataEntity::getClientName, queryDTO.getClientName());

        //这里注入查询条件
        IPage<InterfaceDataEntity> entityIPage = interfaceDataMapper.selectPage(new Page<InterfaceDataEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        List<Map<String, Object>> mapList = new ArrayList<>();
        //从分页对象中取出查询的结果集
        List<InterfaceDataEntity> records = entityIPage.getRecords();
        //将结果集转换为json数组
        JSONArray.from(records);
        //对数据进行处理，处理成jason格式
        JSONArray from1 = new JSONArray();
        for (InterfaceDataEntity record : records) {

            //将遍历的结果转换成一个个jsonObject
            JSONObject recordJson = JSONObject.from(record);
            //从jsonObject中取出键为interfaceData的数据
            JSONObject interfaceData = record.getInterfaceData();

            //遍历
            Iterator<Map.Entry<String, Object>> iterator = interfaceData.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                recordJson.put(entry.getKey(), entry.getValue());
            }
            recordJson.remove("interfaceData");
            from1.add(recordJson);
        }

        JSONObject jsonObject = JSONObject.from(entityIPage);
        jsonObject.put("records", from1);

        return jsonObject;
    }

    @Override
    public InterfaceDataDTO saveInterfaceDataFromMap(Map<String, Object> dataMap) {
        InterfaceDataEntity entity = BeanUtil.copyProperties(dataMap, InterfaceDataEntity.class);
        if (StringUtils.isEmpty(entity.getEbankBatchNo())) {
            entity.setEbankBatchNo(MapUtil.getStr(dataMap, RuleConstant.EBANK_SERIAL_NUMBER));
        }
        entity.setInterfaceData(JSONObject.parseObject(JSONObject.toJSONString(dataMap)));
        entity.setId(IdWorker.getId());
        this.save(entity);
        InterfaceDataDTO dto = BeanUtil.copyProperties(entity, InterfaceDataDTO.class);
        dto.setInterfaceData(entity.getInterfaceData());
        dto.setInterfaceCreateTime(MapUtil.get(dataMap, RuleConstant.INTERFACE_CREATE_TIME, LocalDateTime.class));
        dto.setCreateUserNo(MapUtil.getStr(dataMap, RuleConstant.CREATE_USER_NO));
        dto.setCreateUserName(MapUtil.getStr(dataMap, RuleConstant.CREATE_USER_NAME));
        dto.setBillContractCode(MapUtil.getStr(dataMap, RuleConstant.FIELD_BILL_CONTRACT_CODE));
        dto.setBankClientCode(MapUtil.getStr(dataMap, RuleConstant.BANK_CLIENT_CODE));
        dto.setOrgClientCode(MapUtil.getStr(dataMap, RuleConstant.ORG_CLIENT_CODE));
        dto.setEasVoucherId(MapUtil.getStr(dataMap, RuleConstant.EAS_VOUCHER_ID));
        dto.setEasVoucherId(MapUtil.getStr(dataMap, RuleConstant.IS_INTERFACE_DATA));
        dto.setSettlementWay(MapUtil.getStr(dataMap, RuleConstant.FIELD_SETTLEMENT_WAY));
        dto.setActualClientCode(MapUtil.getStr(dataMap, RuleConstant.ACTUAL_CLIENT_CODE));
        dto.setActualClientName(MapUtil.getStr(dataMap, RuleConstant.ACTUAL_CLIENT_NAME));
        // modify by zhangli.chen for DTO中新增小微业务系统入参vendorPoolType（保证金池类型） on 20250616
        dto.setVendorPoolType(MapUtil.getStr(dataMap, RuleConstant.VENDOR_POOL_TYPE));
        dto.setAccountingBusinessCode(MapUtil.getStr(dataMap, RuleConstant.FIELD_ACCOUNTING_BUSINESS_CODE));
        //凭证日期
        LocalDate voucherDate = periodCodeService.generateVoucherDate(dto);
        if (null != voucherDate) {
            dataMap.put(RuleConstant.FIELD_PERIOD, DateUtil.format(voucherDate.atStartOfDay(), "yyyy.MM"));
        }
        return dto;
    }

    @Override
    public IPage<VerificationPaybackVO> selectPaybackPage(VerificationPaybackQueryDTO queryDTO) {
        Page page = new Page(queryDTO.getPageNum(), queryDTO.getPageSize());
        return this.interfaceDataMapper.selectPaybackPage(page, queryDTO);
    }

    @Override
    public List<InterfaceDataEntity> selectByCondition(InterfaceDataDTO interfaceDataDTO) {
        QueryWrapper<InterfaceDataEntity> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(interfaceDataDTO.getOrgId())) {
            queryWrapper.lambda().eq(InterfaceDataEntity::getOrgId, interfaceDataDTO.getOrgId());
        }
        if (ObjectUtil.isNotNull(interfaceDataDTO.getBusinessDate())) {
            queryWrapper.apply("to_char(business_date, 'yyyy-MM-dd')=to_char({0}, 'yyyy-MM-dd')", interfaceDataDTO.getBusinessDate());
        }
        return this.list(queryWrapper);
    }

    @Override
    public IPage<VerificationPaybackDetailsVO> selectPaybackDetailsPage(VerificationPaybackQueryDTO queryDTO) {
        Page page = new Page(queryDTO.getPageNum(), queryDTO.getPageSize());
        return this.interfaceDataMapper.selectPaybackDetailsPage(page, queryDTO);
    }

    @Override
    public List<VerificationPaybackDetailsVO> selectPayBackAmount(VerificationPaybackQueryDTO queryDTO){
        return interfaceDataMapper.selectPayBackAmount(queryDTO);
    }

    @Override
    public List<VerificationPaybackDetailsVO> listPaybackDetails(VerificationPaybackQueryDTO queryDTO) {
        return interfaceDataMapper.listPaybackDetails(queryDTO);
    }


    /**
     * 查询网银编号不为空的数据-排除已经取过的数据
     */
    public List<InterfaceDataEntity> selectEbankSerialNumberNotNullData() {
        return interfaceDataMapper.selectEbankSerialNumberNotNullData();
    }

    @Override
    public IPage<ContractTransactionVO> contractTransactionByPage(InterfaceDataQueryDTO queryDTO) {
        return interfaceDataMapper.contractTransactionByPage(new Page(queryDTO.getPageNum(),queryDTO.getPageSize()), queryDTO);
    }

    /**
     * 查询过去一年起租的合同信息
     */
    public List<InterfaceDataEntity> queryOnHireContract() {
        SelectHtqzSceneInputDTO params = new SelectHtqzSceneInputDTO();
        Date endDate = DateUtils.truncate(DateUtils.getNowDate(), Calendar.DATE);
        params.setEndDate(endDate);
        Date startDate = DateUtil.offset(endDate, DateField.of(Calendar.YEAR), -1);
        params.setStartDate(startDate);
        List<InterfaceDataEntity> interfaceDataList = interfaceDataMapper.selectHtqzScene(params);
        return interfaceDataList;
    }

    @Override
    public List<VerificationPaybackVO> selectPaybackList(VerificationPaybackQueryDTO queryDTO) {
        return interfaceDataMapper.selectPaybackList(queryDTO);
    }

    @Override
    public List<VerificationPaybackDetailsVO> selectPaybackDetailsList(VerificationPaybackQueryDTO queryDTO) {
        return interfaceDataMapper.selectPaybackDetailsList(queryDTO);
    }
}

