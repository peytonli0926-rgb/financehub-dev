package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.easold.model.dto.EasVoucherDTO;
import com.utfinancing.financehub.etl.financial.entity.VoucherEntryEntity;
import com.utfinancing.financehub.etl.financial.model.dto.VoucherEntryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.VoucherQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.VoucherDTO;
import com.utfinancing.financehub.etl.financial.model.vo.VoucherVO;
import com.utfinancing.financehub.etl.financial.entity.VoucherEntity;
import com.utfinancing.financehub.etl.financial.mapper.VoucherMapper;
import com.utfinancing.financehub.etl.financial.service.IVoucherEntryService;
import com.utfinancing.financehub.etl.financial.service.IVoucherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.etl.middle.model.dto.MidVoucherEntryDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-16
 * @Description :  Voucher服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class VoucherServiceImpl extends ServiceImpl<VoucherMapper, VoucherEntity> implements IVoucherService {

    private final VoucherMapper voucherMapper;

    private final IVoucherEntryService iVoucherEntryService;

    @Override
    public Long saveVoucher(VoucherDTO dto) {
        VoucherEntity entity = BeanUtil.copyProperties(dto, VoucherEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateVoucher(Long id, VoucherDTO dto) {
        VoucherEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public VoucherDTO getVoucherDTOById(Long id) {
        VoucherEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, VoucherDTO.class);
    }

    @Override
    public IPage<VoucherVO> selectPage(VoucherQueryDTO queryDTO) {
        LambdaQueryWrapper<VoucherEntity> queryWrapper = Wrappers.<VoucherEntity>lambdaQuery();
        //这里注入查询条件
        IPage<VoucherEntity> entityIPage = voucherMapper.selectPage(new Page<VoucherEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, VoucherVO.class);
    }

    @Override
    public String exportFinhubVoucherEntryByDate(String voucherDate) {
        Integer periodCode = NumberUtil.parseInt(LocalDateTimeUtil.format(LocalDateTimeUtil.parse(voucherDate, "yyyy-MM-dd"), "yyyyMM"));
        List<MidVoucherEntryDTO> voucherEntryDTOList = voucherMapper.selectFinhubVoucherEntryByDate(periodCode, voucherDate);
        String fileName = "finhub_voucher_data_" + voucherDate+ "_" +LocalDateTimeUtil.format(LocalDateTimeUtil.now(), "yyyyMMddHHmmss")+".xlsx";
        ExcelWriter writer = ExcelUtil.getWriter("/home/admin/service/financehub-etl/export/"+fileName);
        writer.addHeaderAlias("businessDate", "业务日期");
        writer.addHeaderAlias("voucherDate", "凭证日期");
        writer.addHeaderAlias("voucherNumber", "凭证号");
        writer.addHeaderAlias("systemCode", "来源系统");
        writer.addHeaderAlias("sceneName", "场景");
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
    public String exportFinhubData(String statement,String columns) {
        List<Map<String, Object>> dataList = voucherMapper.selectFinhubData(statement);
        String fileName = "finhub_data_" +LocalDateTimeUtil.format(LocalDateTimeUtil.now(), "yyyyMMddHHmmss")+".xlsx";
        ExcelWriter writer = ExcelUtil.getWriter("/home/admin/service/financehub-etl/export/"+fileName);
        writer.writeHeadRow(StrUtil.split(columns, ","));
//        writer.autoSizeColumnAll();
        writer.write(dataList, false);
        writer.close();
        return fileName;
    }

    @Override
    public List<EasVoucherDTO> selectFinhubVoucherData(int periodCode, String voucherDate) {
        List<EasVoucherDTO> result = voucherMapper.selectFinhubVoucherData(periodCode,voucherDate);
        if (result != null && !result.isEmpty()) {
            List<String> ids = result.stream().map(EasVoucherDTO::getEntryId).distinct().collect(Collectors.toList());
            List<String> allIds = new ArrayList<>();
            for (String id : ids) {
                allIds.addAll(Arrays.asList(id.split(",")));
            }
            this.lambdaUpdate().set(VoucherEntity::getIsSendKingdee, "2").
                    in(VoucherEntity::getId, allIds).update();
        }
        return result;
    }

    @Override
    public List<EasVoucherDTO> selectFinhubInterVoucherData(int periodCode, String voucherDate) {
        List<EasVoucherDTO> result = voucherMapper.selectFinhubInterVoucherData(periodCode,voucherDate);
        if (result != null && !result.isEmpty()) {
            List<String> ids = result.stream().map(EasVoucherDTO::getEntryId).distinct().collect(Collectors.toList());
            List<String> allIds = new ArrayList<>();
            for (String id : ids) {
                allIds.addAll(Arrays.asList(id.split(",")));
            }
            this.lambdaUpdate().set(VoucherEntity::getIsSendKingdee, "2").
                    in(VoucherEntity::getId, allIds).update();
        }
        return result;
    }

    @Override
    public List<EasVoucherDTO> selectNoSummaryFinhubVoucherData(int periodCode, String voucherDate,boolean isEntryFlag) {
        // 针对WYLSFK更新vouchernum
        voucherMapper.updateVoucherNumForWYLSFK(periodCode, voucherDate, isEntryFlag);
        List<EasVoucherDTO> result = voucherMapper.selectNoSummaryFinhubVoucherData(periodCode,voucherDate,isEntryFlag);
//        List<EasVoucherDTO> easVoucherDTOList = voucherMapper.selectNoSummaryFinhubVoucherDataForWYLSFK(
//                periodCode,voucherDate,isEntryFlag);
//        if (easVoucherDTOList != null && !easVoucherDTOList.isEmpty()) {
//            result.addAll(easVoucherDTOList);
//        }
        if (result != null && !result.isEmpty()) {
            List<String> entryIds = result.stream().map(EasVoucherDTO::getEntryId).distinct().collect(Collectors.toList());
            iVoucherEntryService.lambdaUpdate().set(VoucherEntryEntity::getIsSendKingdee, "2").
                    in(VoucherEntryEntity::getId, entryIds).update();
        }
        return result;
    }

    @Override
    public List<EasVoucherDTO> selectNoSummaryFinhubInterVoucherData(int periodCode, String voucherDate,boolean isEntryFlag) {
        List<EasVoucherDTO> result = voucherMapper.selectNoSummaryFinhubInterVoucherData(periodCode,voucherDate,isEntryFlag);
        if (result != null && !result.isEmpty()) {
            List<String> entryIds = result.stream().map(EasVoucherDTO::getEntryId).distinct().collect(Collectors.toList());
            iVoucherEntryService.lambdaUpdate().set(VoucherEntryEntity::getIsSendKingdee, "2").
                    in(VoucherEntryEntity::getId, entryIds).update();
        }
        return result;
    }

}

