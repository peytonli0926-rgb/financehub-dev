package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.engine.finance.entity.ContractBalanceLatestEntity;
import com.utfinancing.financehub.engine.finance.mapper.ContractBalanceLatestMapper;
import com.utfinancing.financehub.engine.finance.model.dto.ImpairmentProvisionDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ImpairmentProvisionDetailDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ImpairmentProvisionDetailExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ImpairmentProvisionDetailSummaryVO;
import com.utfinancing.financehub.engine.finance.model.vo.ImpairmentProvisionDetailVO;
import com.utfinancing.financehub.engine.finance.entity.ImpairmentProvisionDetailEntity;
import com.utfinancing.financehub.engine.finance.mapper.ImpairmentProvisionDetailMapper;
import com.utfinancing.financehub.engine.finance.service.IImpairmentProvisionDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.hthx.common.enums.FinanceEngineEnum;
import com.utfinancing.financehub.engine.hthx.utils.HthxExcelExportUtils;
import com.utfinancing.financehub.engine.hthx.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * @Author : wenbin
 * @Date : Create in 2024-03-25
 * @Description :  ImpairmentProvisionDetail服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ImpairmentProvisionDetailServiceImpl extends ServiceImpl<ImpairmentProvisionDetailMapper, ImpairmentProvisionDetailEntity> implements IImpairmentProvisionDetailService {

    private final ImpairmentProvisionDetailMapper impairmentProvisionDetailMapper;
    private final ContractBalanceLatestMapper contractBalanceLatestMapper;

    @Autowired
    @Qualifier("hthxTaskAsyncExecutor")
    private Executor hthxTaskAsyncExecutor;

    @Override
    public Long saveImpairmentProvisionDetail(ImpairmentProvisionDetailDTO dto) {
        ImpairmentProvisionDetailEntity entity = BeanUtil.copyProperties(dto, ImpairmentProvisionDetailEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateImpairmentProvisionDetail(Long id, ImpairmentProvisionDetailDTO dto) {
        ImpairmentProvisionDetailEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ImpairmentProvisionDetailDTO getImpairmentProvisionDetailDTOById(Long id) {
        ImpairmentProvisionDetailEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ImpairmentProvisionDetailDTO.class);
    }

    /**
     * @description:减值计提-查询详情-分页查询+导出
     **/
    @Override
    public IPage<ImpairmentProvisionDetailVO> selectPage(ImpairmentProvisionDetailQueryDTO queryDTO) {
        Page page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        IPage<ImpairmentProvisionDetailVO> entityIPage = impairmentProvisionDetailMapper.selectPageByMapper(page, queryDTO);
        List<ImpairmentProvisionDetailVO> list = entityIPage.getRecords();
        //设置数据
        setImpairmentDate(list);
        return entityIPage;
    }

    /**
     * @description:异步并行查询+预构建内存映射
     **/
    private void setImpairmentDate(List<ImpairmentProvisionDetailVO> list) {
        if (CollectionUtils.isEmpty(list)){
            return;
        }
        // 提取去重参数
        List<String> contractCodeList = list.stream()
                .map(ImpairmentProvisionDetailVO::getContractCode).distinct().collect(Collectors.toList());
        List<String> orgIdList = list.stream()
                .map(ImpairmentProvisionDetailVO::getOrgId).distinct().collect(Collectors.toList());
        // 分批次+异步并发查询
        List<CompletableFuture<List<ContractBalanceLatestEntity>>> futures = StringUtils.splitBatches(contractCodeList, FinanceEngineEnum.Numbers.THOUSAND.getKey()).stream()
                .map(subCodes -> CompletableFuture.supplyAsync(
                        () -> contractBalanceLatestMapper.listReceivableRentBalanceByContractCodeAndOrgId(subCodes, orgIdList), hthxTaskAsyncExecutor)).collect(Collectors.toList());
        // 合并查询结果
        List<ContractBalanceLatestEntity> contractBalanceLatestEntityList = futures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        // 预构建内存映射
        Map<String, Map<String, BigDecimal>> balanceMap = contractBalanceLatestEntityList.stream()
                .collect(Collectors.groupingBy(
                        ContractBalanceLatestEntity::getContractCode,
                        Collectors.groupingBy(
                                ContractBalanceLatestEntity::getOrgId,
                                Collectors.reducing(BigDecimal.ZERO, ContractBalanceLatestEntity::getReceivableRentBalance, BigDecimal::add))));
        // 并行处理列表
        list.parallelStream().forEach(a -> {
            BigDecimal total = Optional.ofNullable(balanceMap.get(a.getContractCode()))
                    .map(orgMap -> orgMap.get(a.getOrgId()))
                    .orElse(BigDecimal.ZERO);
            a.setHubRentReceivableBalance(total);
            if (StringUtils.isEmpty(a.getAccountDate())) {
                a.setPeriodCode(NumberUtil.parseInt(LocalDateTimeUtil.format(LocalDateTime.now(), "yyyyMM")));
            } else {
                a.setPeriodCode(NumberUtil.parseInt(LocalDateTimeUtil.format(a.getAccountDate(), "yyyyMM")));
            }
        });
    }


    /**
     * @description:减值计提-查询详情-导出
     **/
    @Override
    public List<ImpairmentProvisionDetailVO> selectList(ImpairmentProvisionDetailQueryDTO queryDTO) {
        List<ImpairmentProvisionDetailVO> list = impairmentProvisionDetailMapper.selectPageByMapper(queryDTO);
        //设置数据
        setImpairmentDate(list);
        return list;
    }

    /**
     * 查询汇总数据
     * @param queryDTO
     * @return
     */
    @Override
    public ImpairmentProvisionDetailSummaryVO summary(ImpairmentProvisionDetailQueryDTO queryDTO) {
        ImpairmentProvisionDetailSummaryVO summaryVO =impairmentProvisionDetailMapper.selectSummary(queryDTO);
        return summaryVO;
    }


    /**
     * @description:减值计提-首页列表-查看本月减值报告按钮-获取本月数据
     **/
    @Override
    public List<ImpairmentProvisionDetailVO> getThisMonthDataList(String periodCode, String firstDay) {
        List<ImpairmentProvisionDetailVO> list=baseMapper.getThisMonthDataList(periodCode,firstDay);
        return list;
    }

    /**
     * 查询本月转出
     * @return 分减值类型汇总的对应-拨备金额汇总值
     */
    @Override
    public Map<String,BigDecimal> getTransferOut(String periodCode, String firstDay) {
        return impairmentProvisionDetailMapper.getTransferOut(periodCode,firstDay);
    }

    /**
     * 导出excel-明细数据
     *
     * @param response
     * @param queryDTO
     * @return
     */
    @Override
    public void exportProvisionDetailExcel(HttpServletResponse response, ImpairmentProvisionDetailQueryDTO queryDTO) throws Exception {
        List<ImpairmentProvisionDetailVO> list = impairmentProvisionDetailMapper.selectPageByMapper(queryDTO);
        if (CollectionUtils.isEmpty(list)){
            return;
        }
        // 提取去重参数
        List<String> contractCodeList = list.stream()
                .map(ImpairmentProvisionDetailVO::getContractCode).distinct().collect(Collectors.toList());
        List<String> orgIdList = list.stream()
                .map(ImpairmentProvisionDetailVO::getOrgId).distinct().collect(Collectors.toList());
        // 分批次+异步并发查询
        List<CompletableFuture<List<ContractBalanceLatestEntity>>> futures = StringUtils.splitBatches(contractCodeList, FinanceEngineEnum.Numbers.THOUSAND.getKey()).stream()
                .map(subCodes -> CompletableFuture.supplyAsync(
                        () -> contractBalanceLatestMapper.listReceivableRentBalanceByContractCodeAndOrgId(subCodes, orgIdList), hthxTaskAsyncExecutor)).collect(Collectors.toList());
        // 阻塞等待所有异步查询完成
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allFutures.join(); // 阻塞直到完成
        // 合并查询结果
        List<ContractBalanceLatestEntity> contractBalanceLatestEntityList = futures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        // 预构建内存映射
        Map<String, Map<String, BigDecimal>> balanceMap = contractBalanceLatestEntityList.stream()
                .collect(Collectors.groupingBy(
                        ContractBalanceLatestEntity::getContractCode,
                        Collectors.groupingBy(
                                ContractBalanceLatestEntity::getOrgId,
                                Collectors.reducing(BigDecimal.ZERO, ContractBalanceLatestEntity::getReceivableRentBalance, BigDecimal::add))));
        // 并行处理列表
        list.parallelStream().forEach(a -> {
            BigDecimal total = Optional.ofNullable(balanceMap.get(a.getContractCode()))
                    .map(orgMap -> orgMap.get(a.getOrgId()))
                    .orElse(BigDecimal.ZERO);
            a.setHubRentReceivableBalance(total);
            if (StringUtils.isEmpty(a.getAccountDate())) {
                a.setPeriodCode(NumberUtil.parseInt(LocalDateTimeUtil.format(LocalDateTime.now(), "yyyyMM")));
            } else {
                a.setPeriodCode(NumberUtil.parseInt(LocalDateTimeUtil.format(a.getAccountDate(), "yyyyMM")));
            }
        });
//        try {
//            ExcelUtil<ImpairmentProvisionDetailExcelVO> util = new ExcelUtil<ImpairmentProvisionDetailExcelVO>(ImpairmentProvisionDetailExcelVO.class);
//            // 转换为导出对象
//            List<ImpairmentProvisionDetailExcelVO> exportList = BeanUtil.copyToList(list, ImpairmentProvisionDetailExcelVO.class);
//            response.setContentType("application/octet-stream; charset=utf-8");
//            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("减值计提明细.xlsx", "utf8"));
//            util.exportExcel(response, exportList, "减值计提明细");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        try {
            List<ImpairmentProvisionDetailExcelVO> exportList = BeanUtil.copyToList(list, ImpairmentProvisionDetailExcelVO.class);
            // 调用导出工具类
            HthxExcelExportUtils.exportLargeExcel(response,
                    exportList,
                    "减值计提明细",
                    "减值计提明细",
                    ImpairmentProvisionDetailExcelVO.class);
        } catch (IOException e) {
            throw new RuntimeException("减值计提-明细页导出失败",e);
        }

    }

    /**
     * @description:减值计提-查询详情-导出
     **/
    @Override
    public List<ImpairmentProvisionDetailVO> selectDetailList(ImpairmentProvisionDetailQueryDTO queryDTO) {
        List<ImpairmentProvisionDetailVO> list = impairmentProvisionDetailMapper.selectPageByMapper(queryDTO);
        return list;
    }

}

