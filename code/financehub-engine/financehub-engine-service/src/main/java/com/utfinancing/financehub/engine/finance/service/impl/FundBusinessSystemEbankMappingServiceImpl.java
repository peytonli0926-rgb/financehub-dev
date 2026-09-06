package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.engine.enums.CurrencyTypeEnum;
import com.utfinancing.financehub.engine.enums.DictTypeEnum;
import com.utfinancing.financehub.engine.enums.RawMessageStatusEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.BankAccountEntity;
import com.utfinancing.financehub.engine.finance.entity.FundBusinessSystemEbankMappingEntity;
import com.utfinancing.financehub.engine.finance.entity.FundBusinessSystemEbankWyAmountEntity;
import com.utfinancing.financehub.engine.finance.entity.FundEbankTransactionDataEntity;
import com.utfinancing.financehub.engine.finance.entity.HyFullOnlineBankBatchNoMappingEntity;
import com.utfinancing.financehub.engine.finance.entity.NonConfirmCollectionSumEntity;
import com.utfinancing.financehub.engine.finance.mapper.FundBusinessSystemEbankMappingMapper;
import com.utfinancing.financehub.engine.finance.model.dto.FundBusinessSystemEbankMappingDTO;
import com.utfinancing.financehub.engine.finance.model.dto.FundBusinessSystemEbankMappingQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SelectFundEbankTransactionDataByConDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SelectNonConfirmCollectionDataDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SystemBankMappingReconciliationDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SystemBankMappingReconciliationQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.FundBusinessSystemEbankMappingVO;
import com.utfinancing.financehub.engine.finance.model.vo.SystemBankMappingReconciliationDetailVO;
import com.utfinancing.financehub.engine.finance.model.vo.SystemBankMappingReconciliationNoVO;
import com.utfinancing.financehub.engine.finance.model.vo.SystemBankMappingReconciliationVO;
import com.utfinancing.financehub.engine.finance.service.IBankAccountService;
import com.utfinancing.financehub.engine.finance.service.IBusinessClaimRepaymentRecordService;
import com.utfinancing.financehub.engine.finance.service.IFundBusinessSystemEbankMappingService;
import com.utfinancing.financehub.engine.finance.service.IFundBusinessSystemEbankWyAmountService;
import com.utfinancing.financehub.engine.finance.service.IFundEbankTransactionDataService;
import com.utfinancing.financehub.engine.finance.service.IHyFullOnlineBankBatchNoMappingService;
import com.utfinancing.financehub.engine.finance.service.INonConfirmCollectionSumService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2023-12-21
 * @Description :  FundBusinessSystemEbankMapping服务实现类
 * @Modified :
 */
@Service
@Transactional
@Slf4j
public class FundBusinessSystemEbankMappingServiceImpl extends ServiceImpl<FundBusinessSystemEbankMappingMapper,
        FundBusinessSystemEbankMappingEntity> implements IFundBusinessSystemEbankMappingService {


    @Resource
    private FundBusinessSystemEbankMappingMapper fundBusinessSystemEbankMappingMapper;

    @Resource
    @Lazy
    private IFundEbankTransactionDataService fundEbankTransactionDataService;

    @Resource
    @Lazy
    private IHyFullOnlineBankBatchNoMappingService hyFullOnlineBankBatchNoMappingService;
    @Lazy
    @Resource
    private IBusinessClaimRepaymentRecordService businessClaimRepaymentRecordService;
    @Lazy
    @Resource
    private INonConfirmCollectionSumService nonConfirmCollectionSumService;

    @Resource
    @Lazy
    private IBankAccountService bankAccountService;

    @Resource
    private IFundBusinessSystemEbankWyAmountService fundBusinessSystemEbankWyAmountService;

    @Resource
    private RemoteDictService remoteDictService;

    @Override
    public Long saveFundBusinessSystemEbankMapping(FundBusinessSystemEbankMappingDTO dto) {
        FundBusinessSystemEbankMappingEntity entity = BeanUtil.copyProperties(dto, FundBusinessSystemEbankMappingEntity.class);
        this.save(entity);
        return entity.getId();
    }

    /**
     * 根据条件查询业务系统和资金系统网银编号映射关系
     */
    public List<FundBusinessSystemEbankMappingEntity> selectFundBusinessSystemEbankMappingByCon(
            FundBusinessSystemEbankMappingEntity entity) {
        LambdaQueryWrapper<FundBusinessSystemEbankMappingEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FundBusinessSystemEbankMappingEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        if (StringUtils.isNotEmpty(entity.getEbankNumber())) {
            List<String> eBankNoList = Arrays.asList(entity.getEbankNumber().split(","));
            wrapper.in(FundBusinessSystemEbankMappingEntity::getEbankNumber, eBankNoList);
        }
        if (StringUtils.isNotEmpty(entity.getEbankSerialNumber())) {
            List<String> eBankSerialNoList = Arrays.asList(entity.getEbankSerialNumber().split(","));
            wrapper.in(FundBusinessSystemEbankMappingEntity::getEbankSerialNumber, eBankSerialNoList);
        }
        return fundBusinessSystemEbankMappingMapper.selectList(wrapper);
    }

    /**
     * 根据批扣号查询资金系统网银编号映射关系
     */
    public FundBusinessSystemEbankMappingEntity selectFundBusinessSystemEbankMappingBySerialNumber(
            String ebankSerialNumber) {
        LambdaQueryWrapper<FundBusinessSystemEbankMappingEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FundBusinessSystemEbankMappingEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        wrapper.like(FundBusinessSystemEbankMappingEntity::getEbankSerialNumber, ebankSerialNumber);
        return fundBusinessSystemEbankMappingMapper.selectOne(wrapper);
    }


    @Override
    public Long updateFundBusinessSystemEbankMapping(Long id, FundBusinessSystemEbankMappingDTO dto) {
        FundBusinessSystemEbankMappingEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public FundBusinessSystemEbankMappingDTO getFundBusinessSystemEbankMappingDTOById(Long id) {
        FundBusinessSystemEbankMappingEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, FundBusinessSystemEbankMappingDTO.class);
    }

    @Override
    public IPage<FundBusinessSystemEbankMappingVO> selectPage(FundBusinessSystemEbankMappingQueryDTO queryDTO) {
        LambdaQueryWrapper<FundBusinessSystemEbankMappingEntity> queryWrapper = Wrappers.<FundBusinessSystemEbankMappingEntity>lambdaQuery();
        //这里注入查询条件
        IPage<FundBusinessSystemEbankMappingEntity> entityIPage = fundBusinessSystemEbankMappingMapper.selectPage(new Page<FundBusinessSystemEbankMappingEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, FundBusinessSystemEbankMappingVO.class);
    }

    @Override
    public Boolean saveEbankMapping(List<FundBusinessSystemEbankMappingDTO> ebankMappingDTO) {
        List<FundBusinessSystemEbankMappingEntity> mappingEntityList = BeanUtil.copyToList(ebankMappingDTO, FundBusinessSystemEbankMappingEntity.class);
        mappingEntityList.forEach(v -> {
            v.setMessageStatus(RawMessageStatusEnum.NOT_EXECUTE.getCode());
        });
        boolean result = saveBatch(mappingEntityList);

        // 关联网银收款信息入库到未确认收款汇总表
//        this.selectNonConfirmCollectionFromFundSystem();
        return result;
    }

    /**
     * 关联网银收款信息入库到未确认收款汇总表
     */
    public void selectNonConfirmCollectionFromFundSystem() {
        log.info("未确认收款数据同步开始...");
        List<SelectNonConfirmCollectionDataDTO> selectNonConfirmCollectionDataList =
                fundBusinessSystemEbankMappingMapper.selectNonConfirmCollectionData();
        if (selectNonConfirmCollectionDataList == null || selectNonConfirmCollectionDataList.isEmpty()) {
            return;
        }


        R<List<SysDictData>> companyList = remoteDictService.listDictData(DictTypeEnum.COMPANY.getCode());
        Map<String, String> companyMap = companyList.getData().stream().collect(Collectors.toMap(
                e -> e.getDictValue(), e -> e.getDictLabel(), (a, b) -> b));

        // 业务系统网银编号与批扣号映射查询-恒运系统通过映射关系查找批次号，其余系统直接查找回笼数据
//        String ebankSerialNumber = selectNonConfirmCollectionDataList.stream().filter(e->StringUtils.isNotEmpty(e.getEbankSerialNumber())).
//                map(SelectNonConfirmCollectionDataDTO::getEbankSerialNumber).distinct().collect(Collectors.joining(","));
        List<HyFullOnlineBankBatchNoMappingEntity> allMappingList = hyFullOnlineBankBatchNoMappingService.
                selectBusinessOnlineBankNoBatchNoMapping("");
        Map<String, List<HyFullOnlineBankBatchNoMappingEntity>> allHyMappingList = allMappingList.stream().
                collect(Collectors.groupingBy(HyFullOnlineBankBatchNoMappingEntity::getOnlineBankNo));

        List<NonConfirmCollectionSumEntity> nonConfirmCollectionSumEntityList = new ArrayList<>();
        for (SelectNonConfirmCollectionDataDTO dto : selectNonConfirmCollectionDataList) {
            NonConfirmCollectionSumEntity entity = new NonConfirmCollectionSumEntity();
            Long sumId = IdWorker.getId();
            entity.setId(sumId);
            entity.setEbankNumber(dto.getEbankNumber());  // 资金系统网银编号
            entity.setBusinessEbankNumber(dto.getEbankSerialNumber());  // 业务系统网银编号

            List<HyFullOnlineBankBatchNoMappingEntity> mappingList = new ArrayList<>();
            for (int i = 0; i < dto.getEbankSerialNumber().split(",").length; i++) {
                List<HyFullOnlineBankBatchNoMappingEntity> tempList = allHyMappingList.get(dto.getEbankSerialNumber().split(",")[i]);
                if (tempList != null && !tempList.isEmpty()) {
                    mappingList.addAll(tempList);
                }
            }

            // 恒运系统通过映射关系查找批次号
            if (mappingList != null && !mappingList.isEmpty()) {
                // 存储关联的sum id
                mappingList.stream().forEach(e -> {
                    e.setSumId(sumId);
                });
                hyFullOnlineBankBatchNoMappingService.updateBatchById(mappingList);

                String deductBatchNo = mappingList.stream().map(HyFullOnlineBankBatchNoMappingEntity::getDeductBatchNo).
                        distinct().collect(Collectors.joining(","));
                entity.setEbankSerialNumber(deductBatchNo);

                // 应批扣金额保存
                BigDecimal accountReceivable = mappingList.stream().map(
                        HyFullOnlineBankBatchNoMappingEntity::getCollectAmount).reduce(new BigDecimal(0), BigDecimal::add);
                entity.setAccountsReceivable(accountReceivable);

            } else if (dto.getEbankSerialNumber().split(",").length == 1
                    && StringUtils.equals(dto.getEbankNumber(), dto.getEbankSerialNumber())) {
                entity.setAccountsReceivable(dto.getBankAmount());
                entity.setEbankSerialNumber(dto.getEbankSerialNumber());
            } else {
                continue;
            }

            // 资金系统收款数据查询
            List<FundEbankTransactionDataEntity> fundEbankTransactionDataList = selectNonConfirmCollectionSumByCon(dto);
            if (fundEbankTransactionDataList != null && !fundEbankTransactionDataList.isEmpty()) {

                String collectAccountsBankNo = fundEbankTransactionDataList.stream().map(
                        FundEbankTransactionDataEntity::getCollectionAccountsBankNo).distinct().collect(Collectors.joining(","));
                List<BankAccountEntity> bankAccountList = bankAccountService.selectBankAccountEntity(collectAccountsBankNo);
                if (bankAccountList != null && !bankAccountList.isEmpty()) {
                    String collectionAccountsBank = bankAccountList.stream().map(e -> companyMap.get(e.getOrgId())).
                            collect(Collectors.joining(","));
                    entity.setCollectionAccountsBank(collectionAccountsBank);
                    entity.setCollectionAccountsBankCode(bankAccountList.stream().map(e -> e.getOrgId()).collect(Collectors.joining(",")));
                }

                entity.setCollectionAccountsBankNo(collectAccountsBankNo);
                entity.setClientCode(fundEbankTransactionDataList.get(0).getClientCode());
                entity.setClientName(fundEbankTransactionDataList.get(0).getClientName());
                entity.setBankSummary(fundEbankTransactionDataList.get(0).getBankSummary());
                entity.setComment(fundEbankTransactionDataList.get(0).getComment());
                entity.setClientAccountsBankNo(fundEbankTransactionDataList.get(0).getClientAccountsBankNo());

                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime localTime = LocalDateTime.parse(fundEbankTransactionDataList.get(0).getBusinessDate(), df);
                entity.setBusinessDate(localTime);

//                String currencyType = fundEbankTransactionDataList.stream().filter(e -> StringUtils.isNotEmpty(e.getCurrencyType())).
//                        map(e -> CurrencyTypeEnum.getEnumByType(e.getCurrencyType()).getCode()).distinct().
//                        collect(Collectors.joining(","));
                entity.setCurrencyType(fundEbankTransactionDataList.get(0).getCurrencyType());
            } else {
                List<FundBusinessSystemEbankWyAmountEntity> fundEbankWyAmountList = fundBusinessSystemEbankWyAmountService.
                        selectFundEbankTransactionDataByCon(dto);
                if (fundEbankWyAmountList != null && !fundEbankWyAmountList.isEmpty()) {

                    String collectAccountsBankNo = fundEbankWyAmountList.stream().map(
                            FundBusinessSystemEbankWyAmountEntity::getCollectionAccountsBankNo).distinct().collect(Collectors.joining(","));
                    List<BankAccountEntity> bankAccountList = bankAccountService.selectBankAccountEntity(collectAccountsBankNo);
                    if (bankAccountList != null && !bankAccountList.isEmpty()) {
                        String collectionAccountsBank = bankAccountList.stream().map(e -> companyMap.get(e.getOrgId())).
                                collect(Collectors.joining(","));
                        entity.setCollectionAccountsBank(collectionAccountsBank);
                        entity.setCollectionAccountsBankCode(bankAccountList.stream().map(e -> e.getOrgId()).collect(Collectors.joining(",")));
                    }

                    entity.setCollectionAccountsBankNo(collectAccountsBankNo);

                    LocalDateTime localTime = DateUtil.toLocalDateTime(fundEbankWyAmountList.get(0).getBusinessDate());
                    entity.setBusinessDate(localTime);

                    entity.setCurrencyType(CurrencyTypeEnum.CNY.getDesc());
                    entity.setClientName(fundEbankWyAmountList.get(0).getClientName());
                    entity.setClientCode(fundEbankWyAmountList.get(0).getClientCode());
                    entity.setBankSummary(fundEbankWyAmountList.get(0).getBankSummary());
                    entity.setComment(fundEbankWyAmountList.get(0).getComment());
                    entity.setClientAccountsBankNo(fundEbankWyAmountList.get(0).getClientAccountsBankNo());
                } else {
                    entity.setBusinessDate(LocalDateTime.now());
                }
            }

            entity.setEbankMappingId(dto.getId());
            entity.setBankAmount(dto.getBankAmount());

            nonConfirmCollectionSumEntityList.add(entity);
            log.info("List size:" + nonConfirmCollectionSumEntityList.size());
        }
        nonConfirmCollectionSumService.saveBatch(nonConfirmCollectionSumEntityList);
        log.info("未确认收款数据同步完成...");
    }

    @Override
    public IPage<SystemBankMappingReconciliationVO> systemBankMappingReconciliationFilter(SystemBankMappingReconciliationQueryDTO dto) {
        // pageNum = -1 表示不分页
        int pageNum = dto.getPageNum();
        Page<SystemBankMappingReconciliationVO> page;
        if (pageNum != -1){
            page = Page.of(pageNum, dto.getPageSize());
        }else {
            page = Page.of(pageNum, Long.MAX_VALUE, false);
        }

        return fundBusinessSystemEbankMappingMapper.systemBankMappingReconciliationFilter(page ,dto.getPeriodCode(), dto.abnormal());
    }

    @Override
    public IPage<SystemBankMappingReconciliationDetailVO> systemBankMappingReconciliationDetailByMatchNumber(SystemBankMappingReconciliationDetailQueryDTO dto) {
        String matchNumber = dto.getMatchNumber();
        SystemBankMappingReconciliationVO reconciliationVO = fundBusinessSystemEbankMappingMapper.systemBankMappingReconciliationByMatchNumber(dto.getPeriodCode(), matchNumber);

        Page<SystemBankMappingReconciliationDetailVO> page;
        if (dto.getPageNum() != -1){
            page = Page.of(dto.getPageNum(), dto.getPageSize());
        }else {
            page = Page.of(dto.getPageNum(), Long.MAX_VALUE, false);
        }
        Page<SystemBankMappingReconciliationDetailVO> vos = fundBusinessSystemEbankMappingMapper.systemBankMappingReconciliationDetailByMatchNumber(page,matchNumber);
        for (SystemBankMappingReconciliationDetailVO vo : vos.getRecords()) {
            vo.setBusinessDate(reconciliationVO.getBusinessDate());
        }

        return vos;
    }

    @Override
    public IPage<SystemBankMappingReconciliationNoVO> systemBankMappingReconciliationNoVO(BaseQueryDTO dto) {
        Page<SystemBankMappingReconciliationNoVO> page;
        if (dto.getPageNum() != -1){
            page = Page.of(dto.getPageNum(), dto.getPageSize());
        }else {
            page = Page.of(dto.getPageNum(), Long.MAX_VALUE, false);
        }
        return fundBusinessSystemEbankMappingMapper.systemBankMappingReconciliationNoVO(page);
    }

    /**
     * 查询
     */
    private List<FundEbankTransactionDataEntity> selectNonConfirmCollectionSumByCon(SelectNonConfirmCollectionDataDTO dto) {
        SelectFundEbankTransactionDataByConDTO params = new SelectFundEbankTransactionDataByConDTO();
        if (StringUtils.isNotEmpty(dto.getEbankNumber())) {
            List<String> ebankNumberList = Arrays.asList(dto.getEbankNumber().split(","));
            params.setEbankNumberList(ebankNumberList);
        }
        return fundEbankTransactionDataService.selectFundEbankTransactionDataByCon(params);
    }


}

