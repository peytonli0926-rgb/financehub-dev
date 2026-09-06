package com.utfinancing.financehub.etl.kingdee.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.financial.entity.KingdeeContractBalanceEntity;
import com.utfinancing.financehub.etl.financial.entity.KingdeeVoucherEntity;
import com.utfinancing.financehub.etl.financial.entity.KingdeeVoucherEntryEntity;
import com.utfinancing.financehub.etl.financial.model.dto.KingdeeVoucherDTO;
import com.utfinancing.financehub.etl.kingdee.entity.TBdCustomer;
import com.utfinancing.financehub.etl.kingdee.model.dto.*;
import com.utfinancing.financehub.etl.kingdee.entity.TGlVoucherEntity;
import com.utfinancing.financehub.etl.kingdee.mapper.TGlVoucherMapper;
import com.utfinancing.financehub.etl.kingdee.service.ITGlVoucherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description :  TGlVoucher服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class TGlVoucherServiceImpl extends ServiceImpl<TGlVoucherMapper, TGlVoucherEntity> implements ITGlVoucherService {

    private final TGlVoucherMapper tGlVoucherMapper;


    @Override
    public List<ContractSumDTO> selectOneContract() {
        IPage<ContractSumDTO> iPageDto = (IPage<ContractSumDTO>) tGlVoucherMapper.selectOneContract(new Page<ContractSumDTO>(1L, 100L));
        return tGlVoucherMapper.selectOneContract(new Page<ContractSumDTO>(1L, 100L));
    }

    @Override
    public List<KingdeeVoucherEntity> selectVoucherByPeriodAndDate(Integer periodCode, String voucherDate) {
        return tGlVoucherMapper.selectVoucherByPeriodAndDate(periodCode, voucherDate);
    }

    @Override
    public KingdeeVoucherEntity selectVoucherByEasId(String easVoucherId) {
        return tGlVoucherMapper.selectVoucherByEasVoucherId(easVoucherId);
    }

    @Override
    public List<KingdeeVoucherEntity> selectVoucherByPeriod(Integer periodCode) {
        return tGlVoucherMapper.selectVoucherByPeriod(periodCode);
    }

    @Override
    public List<KingdeeVoucherEntryEntity> selectVoucherEntryByPeriodAndDate(String voucherId) {
        return tGlVoucherMapper.selectVoucherEntryByPeriodAndDate(voucherId);
    }

    @Override
    public List<KingdeeVoucherEntryEntity> selectVoucherEntryByPeriodCode(Integer periodCode, String voucherDate, Integer bizStatus,List<String> contractCodeList) {
        return tGlVoucherMapper.selectVoucherEntryByPeriodCode(periodCode, voucherDate, bizStatus,contractCodeList);
    }

    @Override
    public List<KingdeeContractBalanceDTO> selectAccountBalanceList(String clientCode, String contractCode) {
        return tGlVoucherMapper.selectAccountBalanceList(clientCode, contractCode);
    }

    @Override
    public List<KingdeeContractBalanceEntity> selectContractBalance(Integer periodCode,List<String> contractCodeList) {
        return tGlVoucherMapper.selectContractBalance(periodCode,contractCodeList);
    }

    @Override
    public List<KingdeeContractBalanceEntity> selectContractBalance80001(Integer periodCode,List<String> contractCodeList) {
        return tGlVoucherMapper.selectContractBalance80001(periodCode,contractCodeList);
    }

    @Override
    public List<KingdeeAssistBalanceDTO> selectKingdeeAssistBalanceList(Integer periodCode, String orgId) {
        return tGlVoucherMapper.selectKingdeeAssistBalanceList(periodCode, orgId);
    }

    @Override
    public List<TBdCustomer> selectCustomerById(String clientCode) {
        return tGlVoucherMapper.selectCustomerById(clientCode);
    }

    @Override
    public String exportKingdeeData(String statement,String columns) {
        List<Map<String, Object>> dataList = tGlVoucherMapper.selectKingdeeData(statement);
        String fileName = "kingdee_data_" + LocalDateTimeUtil.format(LocalDateTimeUtil.now(), "yyyyMMddHHmmss")+".xlsx";
        ExcelWriter writer = ExcelUtil.getWriter("/home/admin/service/financehub-etl/export/"+fileName);
//        writer.autoSizeColumnAll();
        writer.writeHeadRow(StrUtil.split(columns, ","));
        writer.write(dataList, false);
        writer.close();
        return fileName;
    }
}

