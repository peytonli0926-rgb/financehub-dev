package com.utfinancing.financehub.etl.middle.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.utfinancing.financehub.etl.easold.model.dto.EasVoucherDTO;
import com.utfinancing.financehub.etl.financial.entity.KingdeeMiddleVoucherEntity;
import com.utfinancing.financehub.etl.financial.service.IDataExecutionTaskService;
import com.utfinancing.financehub.etl.financial.service.IVoucherToEasRecordService;
import com.utfinancing.financehub.etl.financial.service.VoucherTransactionService;
import com.utfinancing.financehub.etl.middle.model.dto.MidVoucherEntryDTO;
import com.utfinancing.financehub.etl.middle.entity.EasVoucherHeadEntity;
import com.utfinancing.financehub.etl.middle.mapper.EasVoucherHeadMapper;
import com.utfinancing.financehub.etl.middle.service.IEasVoucherHeadService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-28
 * @Description :  EasVoucherHead服务实现类
 * @Modified :
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class EasVoucherHeadServiceImpl extends ServiceImpl<EasVoucherHeadMapper, EasVoucherHeadEntity> implements IEasVoucherHeadService {

    private final EasVoucherHeadMapper easVoucherHeadMapper;
    @Resource
    private final VoucherTransactionService voucherTransactionService;

    @Override
    public EasVoucherHeadEntity selectMidVoucherHeaderByEasId(String easId) {
        EasVoucherHeadEntity entity = this.getOne(Wrappers.<EasVoucherHeadEntity>lambdaQuery()
                .eq(EasVoucherHeadEntity::getEasbzcode, easId), false);
        return entity;
    }

        @Override
        public List<EasVoucherHeadEntity> selectMidVoucherHeaderByEasIds(List<String> easIds) {
            List<EasVoucherHeadEntity> entityList = this.list(Wrappers.<EasVoucherHeadEntity>lambdaQuery()
                    .in(EasVoucherHeadEntity::getEasbzcode, easIds)
                    .eq(EasVoucherHeadEntity::getEasflag, "1"));
            return entityList;
        }

        @Override
        public List<EasVoucherHeadEntity> selectMidVoucherHeaderByDate(String voucherDate) {
            String periodYear = StrUtil.sub(voucherDate,0, 4);
            String periodMonth = StrUtil.sub(voucherDate, 5,7);
            return easVoucherHeadMapper.selectMidVoucherHeaderByDate(periodYear, periodMonth, voucherDate);
    }

    @Override
    public String exportMidVoucherEntryByDate(String voucherDate) {
        String periodYear = StrUtil.sub(voucherDate,0, 4);
        String periodMonth = StrUtil.sub(voucherDate, 5,7);
        List<MidVoucherEntryDTO> voucherEntryDTOList = easVoucherHeadMapper.selectMidVoucherEntryByDate(NumberUtil.parseInt(periodYear), NumberUtil.parseInt(periodMonth), voucherDate);
        String fileName = "eas_voucher_data_" + voucherDate+ "_" +LocalDateTimeUtil.format(LocalDateTimeUtil.now(), "yyyyMMddHHmmss")+".xlsx";
        ExcelWriter writer = ExcelUtil.getWriter("/home/admin/service/financehub-etl/export/"+fileName);
        writer.addHeaderAlias("businessDate", "业务日期");
        writer.addHeaderAlias("voucherDate", "凭证日期");
        writer.addHeaderAlias("voucherNumber", "凭证编号");
        writer.addHeaderAlias("systemCode", "来源系统");
        writer.addHeaderAlias("sceneName", "场景名称");
        writer.addHeaderAlias("orgId", "签约主体");
        writer.addHeaderAlias("contractCode", "合同编号");
        writer.addHeaderAlias("accountCode", "科目编码");
        writer.addHeaderAlias("debitAmount", "借方金额");
        writer.addHeaderAlias("creditAmount", "贷方金额");
        writer.autoSizeColumnAll();
        writer.write(voucherEntryDTOList, true);
        writer.close();
        return fileName;
    }

    @Override
    public String exportMidVoucherAsstacttByDate(String voucherDate) {
        String periodYear = StrUtil.sub(voucherDate,0, 4);
        String periodMonth = StrUtil.sub(voucherDate, 5,7);
        List<Map<String, Object>> asstactList = easVoucherHeadMapper.selectMidVoucherAsstactByDate(NumberUtil.parseInt(periodYear), NumberUtil.parseInt(periodMonth), voucherDate);
        String fileName = "eas_voucher_asstact_" + voucherDate+ "_" +LocalDateTimeUtil.format(LocalDateTimeUtil.now(), "yyyyMMddHHmmss")+".xlsx";
        ExcelWriter writer = ExcelUtil.getWriter("/home/admin/service/financehub-etl/export/"+fileName);
        writer.autoSizeColumnAll();writer.addHeaderAlias("businessDate", "业务日期");

        writer.write(asstactList, true);
        writer.close();
        return fileName;
    }

    @Override
    public String exportMidVoucherIncloudAsscateByDate(String voucherDate) {
        String periodYear = StrUtil.sub(voucherDate,0, 4);
        String periodMonth = StrUtil.sub(voucherDate, 5,7);
        List<Map<String, Object>> dataList = easVoucherHeadMapper.selectMidVoucherIncloudAsscateByDate(NumberUtil.parseInt(periodYear), NumberUtil.parseInt(periodMonth), voucherDate);
        String fileName = "eas_new_voucher_" + voucherDate+ "_" +LocalDateTimeUtil.format(LocalDateTimeUtil.now(), "yyyyMMddHHmmss")+".xlsx";
        ExcelWriter writer = ExcelUtil.getWriter("/home/admin/service/financehub-etl/export/"+fileName);
        writer.autoSizeColumnAll();
        List<String> headers = ListUtil.toList("BUSINESS_DATE","VOUCHER_DATE","VOUCHER_NUMBER","SYSTEM_CODE","SCENE_NAME","ORG_ID","CONTRACT_CODE","ACCOUNT_CODE","DEBIT_AMOUNT","CREDIT_AMOUNT","ASSTACT_TYPE_NAME","ASSTACT_NUMBER","AMOUNT");
        writer.writeHeadRow(headers);
        writer.write(dataList, false);
        writer.close();
        return fileName;
    }

    @Override
    public String exportMiddleData(String statement,String columns) {
        List<Map<String, Object>> dataList = easVoucherHeadMapper.selectMiddleData(statement);
        String fileName = "middle_data_" +LocalDateTimeUtil.format(LocalDateTimeUtil.now(), "yyyyMMddHHmmss")+".xlsx";
        ExcelWriter writer = ExcelUtil.getWriter("/home/admin/service/financehub-etl/export/"+fileName);
//        writer.autoSizeColumnAll();
        writer.writeHeadRow(StrUtil.split(columns, ","));
        writer.write(dataList, false);
        writer.close();
        return fileName;
    }

    @Override
    public List<EasVoucherDTO> selectMiddleEasVoucher(Integer periodYear,Integer periodMonth,String voucherDate) {
        return easVoucherHeadMapper.selectMiddleEasVoucher(periodYear, periodMonth,voucherDate);
    }

    @Override
    public List<EasVoucherDTO> selectStageMiddleEasVoucher(Integer periodYear, Integer periodMonth, String voucherDate) {
        return easVoucherHeadMapper.selectStageMiddleEasVoucher(periodYear, periodMonth, voucherDate);
    }

    @Override
    public List<EasVoucherDTO> selectStageZJXTMiddleEasVoucher(Integer periodYear, Integer periodMonth, String voucherDate) {
        return easVoucherHeadMapper.selectStageZJXTMiddleEasVoucher(periodYear, periodMonth, voucherDate);
    }

    @Override
    public List<KingdeeMiddleVoucherEntity> selectKingdeeMiddleGaxdVoucher(Integer year, Integer month, String voucherDate) {
        return easVoucherHeadMapper.selectKingdeeMiddleGaxdVoucher(year,month,voucherDate);
    }
}

