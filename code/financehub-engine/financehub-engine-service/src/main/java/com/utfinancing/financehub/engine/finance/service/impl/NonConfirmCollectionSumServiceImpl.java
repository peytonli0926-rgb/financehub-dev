package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.constant.HttpStatus;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.core.utils.bean.BeanUtils;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.engine.api.HthxFundCoreService;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.constants.Constants;
import com.utfinancing.financehub.engine.constants.RedisConstant;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.mapper.HthxOfflineOnlineBankDataMapper;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.HthxOfflineOnlineBankDataVO;
import com.utfinancing.financehub.engine.finance.model.vo.ManualVoucherVO;
import com.utfinancing.financehub.engine.finance.model.vo.NonConfirmCollectionSumVO;
import com.utfinancing.financehub.engine.finance.mapper.NonConfirmCollectionSumMapper;
import com.utfinancing.financehub.engine.finance.model.vo.OrgCompanyVO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.hthx.common.enums.FinanceEngineEnum;
import com.utfinancing.financehub.engine.hthx.common.enums.ResultEnum;
import com.utfinancing.financehub.engine.hthx.utils.HthxDateUtils;
import com.utfinancing.financehub.engine.hthx.utils.HthxUUIDUtils;
import com.utfinancing.financehub.engine.hthx.utils.PromptMessageUtil;
import com.utfinancing.financehub.engine.model.dto.HthxFundEbankQueryDTO;
import com.utfinancing.financehub.engine.model.dto.HthxFundEbankTransactionDataDTO;
import com.utfinancing.financehub.engine.model.dto.HthxOnlineBankCrossCheckDataDTO;
import com.utfinancing.financehub.engine.model.dto.HthxOnlineBankCrossCheckDetailDTO;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import com.utfinancing.financehub.engine.rule.model.dto.ExecuteCommonDTO;
import com.utfinancing.financehub.engine.rule.service.IRuleService;
import com.utfinancing.financehub.engine.scene.entity.AccountEntity;
import com.utfinancing.financehub.engine.scene.service.IAccountService;
import com.utfinancing.financehub.engine.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.utfinancing.financehub.engine.hthx.common.base.HthxBaseConfirmData;
import com.utfinancing.financehub.engine.hthx.common.base.HthxSoftTipValidationResult;
import com.utfinancing.financehub.engine.hthx.common.cache.HthxConfirmCacheService;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Author : robjiang
 * @Date : Create in 2024-03-22
 * @Description :  NonConfirmCollectionSum服务实现类
 * @Modified :
 */
@Service
@Transactional
@Slf4j
public class NonConfirmCollectionSumServiceImpl extends ServiceImpl<NonConfirmCollectionSumMapper,
        NonConfirmCollectionSumEntity> implements INonConfirmCollectionSumService {

    @Resource
    private NonConfirmCollectionSumMapper nonConfirmCollectionSumMapper;

    @Resource
    private IHyFullOnlineBankBatchNoMappingService hyFullOnlineBankBatchNoMappingService;
    @Lazy
    @Resource
    private IBusinessClaimRepaymentRecordService businessClaimRepaymentRecordService;
    @Lazy
    @Resource
    private IFundBusinessSystemEbankMappingService fundBusinessSystemEbankMappingService;

    @Resource
    private INonConfirmCollectionSecondDetailService nonConfirmCollectionSecondDetailService;

    @Resource
    private INonConfirmCollectionVoucherRecordService nonConfirmCollectionVoucherRecordService;

    @Resource
    private IManualVoucherService manualVoucherService;

    @Resource
    private IContractService contractService;

    @Resource
    private IManualService manualService;

    @Resource
    private IApproveService approveService;

    @Resource
    private IRuleService ruleService;

    @Resource
    private IVoucherService voucherService;

    @Resource
    private IVoucherEntryService voucherEntryService;

    @Resource
    private IAccountService iAccountService;

    @Resource
    private IOrgCompanyService orgCompanyService;

    @Resource
    private IClientService clientService;

    @Resource
    private ResourceLoader resourceLoader ;

    @Resource
    private INonConfirmCollectionFileUploadRecordService nonConfirmCollectionFileUploadRecordService;

    @Value("${file.storage.basicpath.windows:null}")
    private String basicFilePathForWindows;

    @Value("${file.storage.basicpath.linux:null}")
    private String basicFilePathForLinux;

    @Value("${approve.url.nonConfirmAccount-url:null}")
    private String approveUrl;

    @Autowired
    HthxConfirmCacheService hthxConfirmCacheService;

    @Resource
    private HthxFundCoreService hthxFundCoreService;


    @Resource
    HthxOfflineOnlineBankDataMapper hthxOfflineOnlineBankDataMapper;

    @Autowired
    IHthxOfflineOnlineBankService hthxOfflineOnlineBankService;

    @Resource
    @Lazy
    private IBankAccountService bankAccountService;

    @Resource
    private RemoteDictService remoteDictService;

    @Override
    public Long saveNonConfirmCollectionSum(NonConfirmCollectionSumDTO dto) {
        NonConfirmCollectionSumEntity entity = BeanUtil.copyProperties(dto, NonConfirmCollectionSumEntity.class);
        this.save(entity);
        return entity.getId();
    }

    /**
     * 取得文件存储路径
     */
    private String getFileStoragePath() {
        String operationSystemName = System.getProperties().getProperty("os.name");
        if (operationSystemName.toLowerCase().indexOf(Constants.OPERATION_SYSTEM_NAME_WINDOWS) > -1) {
            return basicFilePathForWindows.concat("non_confirm_collection\\");
        } else if (operationSystemName.toLowerCase().indexOf(Constants.OPERATION_SYSTEM_NAME_LINUX) > -1
                || operationSystemName.toLowerCase().indexOf(Constants.OPERATION_SYSTEM_NAME_UNIX) > -1) {
            return basicFilePathForLinux.concat("non_confirm_collection/");
        }
        return StringUtil.EMPTY;
    }

    @Override
    public Long updateNonConfirmCollectionSum(Long id, NonConfirmCollectionSumDTO dto) {
        NonConfirmCollectionSumEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public NonConfirmCollectionSumDTO getNonConfirmCollectionSumDTOById(Long id) {
        NonConfirmCollectionSumEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, NonConfirmCollectionSumDTO.class);
    }

    /**
     * 根据批扣流水号列表查询未确认收款汇总记录
     */
    public Map<String, NonConfirmCollectionSumEntity> queryNonConfirmCollectionSumMap(String... ebankSerialNumberList) {
        LambdaQueryWrapper<NonConfirmCollectionSumEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(NonConfirmCollectionSumEntity::getEbankSerialNumber, ebankSerialNumberList);
        return this.list(wrapper).stream().collect(Collectors.toMap(NonConfirmCollectionSumEntity::getEbankSerialNumber, (e) -> e, (a, b) -> b));
    }

    /**
     * 合并资金网银信息列表
     */
    private void mergeZjWyList(HthxOnlineBankCrossCheckDataDTO existing, HthxOnlineBankCrossCheckDataDTO replacement) {
        List<HthxFundEbankTransactionDataDTO> existingList = existing.getZjWyList();
        List<HthxFundEbankTransactionDataDTO> replacementList = replacement.getZjWyList();
        if (replacementList != null && !replacementList.isEmpty()) {
            if (existingList == null) {
                // 创建新列表避免引用问题
                existing.setZjWyList(new ArrayList<>(replacementList));
            } else {
                // 合并两个列表
                existingList.addAll(replacementList);
            }
        }
    }

    /**
     * 根据条件查询未确认收款记录
     */
    public List<NonConfirmCollectionSumEntity>  getNonConfirmCollectionSumDTOByCon(NonConfirmCollectionSumEntity entity) {
        LambdaQueryWrapper<NonConfirmCollectionSumEntity> wrapper = new LambdaQueryWrapper();
        if (StringUtils.isNotEmpty(entity.getEbankSerialNumber())) {
            wrapper.like(NonConfirmCollectionSumEntity::getEbankSerialNumber, entity.getEbankSerialNumber());
        }
        if (StringUtils.isNotEmpty(entity.getBusinessEbankNumber())) {
            wrapper.like(NonConfirmCollectionSumEntity::getBusinessEbankNumber, entity.getBusinessEbankNumber());
        }
        wrapper.eq(NonConfirmCollectionSumEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        return nonConfirmCollectionSumMapper.selectList(wrapper);
    }

    @Override
    public IPage<NonConfirmCollectionSumVO> selectPage(NonConfirmCollectionSumQueryDTO queryDTO) {
        LambdaQueryWrapper<NonConfirmCollectionSumEntity> queryWrapper = Wrappers.<NonConfirmCollectionSumEntity>lambdaQuery();
        //这里注入查询条件
        IPage<NonConfirmCollectionSumEntity> entityIPage = nonConfirmCollectionSumMapper.selectPage(
                new Page<NonConfirmCollectionSumEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, NonConfirmCollectionSumVO.class);
    }

    /**
     * 分页查询汇总表
     */
    public IPage<SelectNonConfirmCollectionSumByPageDTO> selectPageByCon(NonConfirmCollectionSumQueryDTO queryDTO) {
        IPage<SelectNonConfirmCollectionSumByPageDTO> page = new Page(queryDTO.getPageNum(), queryDTO.getPageSize());
        Integer count = nonConfirmCollectionSumMapper.selectNonConfirmCollectionSumCount(queryDTO);
        List<SelectNonConfirmCollectionSumByPageDTO> queryResult = nonConfirmCollectionSumMapper.
                selectNonConfirmCollectionSumByPage(queryDTO);

        if (queryResult != null && !queryResult.isEmpty()) {
            queryResult.stream().forEach(e-> {
                if (e.getClaimAmount() == null) {
                    e.setClaimAmount(new BigDecimal(0));
                }
                e.setRemainNonConfirmAmount(e.getAccountsReceivable().subtract(e.getClaimAmount()));
            });
        }
        page.setTotal(count);
        page.setRecords(queryResult);
        return page;
    }

    /**
     * 查询汇总表信息
     */
    public List<SelectNonConfirmCollectionSumByPageDTO> selectByCon(NonConfirmCollectionSumQueryDTO queryDTO) {
        queryDTO.setPageNum(1);
        queryDTO.setPageSize(Integer.MAX_VALUE);
        List<SelectNonConfirmCollectionSumByPageDTO> result = nonConfirmCollectionSumMapper.selectNonConfirmCollectionSumByPage(queryDTO);
        if (result != null && !result.isEmpty()) {
            result.stream().forEach(e-> {
                if (e.getClaimAmount() == null) {
                    e.setClaimAmount(new BigDecimal(0));
                }
                e.setRemainNonConfirmAmount(e.getAccountsReceivable().subtract(e.getClaimAmount()));
                e.setOrgId(StringUtils.distinct(e.getOrgId()));
                e.setOrgName(StringUtils.distinct(e.getOrgName()));
            });
        }
        return result;
    }

    /**
     * 认领数据查询
     */
    public R<ClaimQueryResultDTO> claimQuery(ClaimQueryDTO queryDTO) {
        ClaimQueryResultDTO result = new ClaimQueryResultDTO();
        result.setId(queryDTO.getId());

        // 未确认数据查询
        NonConfirmCollectionSumEntity nonConfirmCollectionSumEntity = nonConfirmCollectionSumMapper.selectById(
                queryDTO.getId());
        if (nonConfirmCollectionSumEntity == null) {
            return R.fail("认领数据不存在，可能已经被删除!");
        }

        if (StringUtils.isEmpty(nonConfirmCollectionSumEntity.getEbankSerialNumber())) {
            return R.fail("业务系统网银编号/批次号为空, 映射数据不存在!");
        }

        // 认领记录查询
        List<BusinessClaimRepaymentRecordEntity> bcrrEntityList = businessClaimRepaymentRecordService.
                selectClaimRecordByDeductBatchNo(nonConfirmCollectionSumEntity.getEbankSerialNumber(),
                        ProcessStatusEnum.getValidCode(), null, null);

        // 恒运系统的情况
        if (!StringUtils.equals(nonConfirmCollectionSumEntity.getEbankNumber(), nonConfirmCollectionSumEntity.getEbankSerialNumber())) {
            HyFullOnlineBankBatchNoMappingEntity hfobbnmEntity = new HyFullOnlineBankBatchNoMappingEntity();
            hfobbnmEntity.setDeductBatchNo(nonConfirmCollectionSumEntity.getEbankSerialNumber());
            List<HyFullOnlineBankBatchNoMappingEntity> hfobbnmList = hyFullOnlineBankBatchNoMappingService.
                    selectMappingDataByCon(hfobbnmEntity);
            if (hfobbnmList == null || hfobbnmList.isEmpty()) {
                return R.fail("映射数据不存在，可能已经被删除!");
            }

            for (int i = 0; i < hfobbnmList.size(); i++) {
                ClaimQueryResultDetailDTO detailDTO = new ClaimQueryResultDetailDTO();
                detailDTO.setOrgId(StringUtil.EMPTY);
                detailDTO.setOrgName(StringUtil.EMPTY);
                detailDTO.setCollectionAccountsBank(nonConfirmCollectionSumEntity.getCollectionAccountsBank());
                detailDTO.setCollectionAccountsBankCode(nonConfirmCollectionSumEntity.getCollectionAccountsBankCode());
                detailDTO.setEbankSerialNumber(hfobbnmList.get(i).getDeductBatchNo());
                detailDTO.setBankAmount(hfobbnmList.get(i).getCollectAmount());

                // 计算已认领金额
                BigDecimal claimAmount = new BigDecimal(0);
                if (bcrrEntityList != null && !bcrrEntityList.isEmpty()) {
                    for (int k = 0; k < bcrrEntityList.size(); k++) {
                        if (StringUtils.equals(hfobbnmList.get(i).getDeductBatchNo(),
                                bcrrEntityList.get(k).getEbankSerialNumber())) {
                            claimAmount = claimAmount.add(bcrrEntityList.get(k).getClaimAmount());
                        }
                    }
                }
                detailDTO.setClaimAmount(claimAmount);
                detailDTO.setRemainNonConfirmAmount(hfobbnmList.get(i).getCollectAmount().subtract(claimAmount));
                result.getClaimQueryResultDetailDTO().add(detailDTO);
            }

        } else {
            ClaimQueryResultDetailDTO detailDTO = new ClaimQueryResultDetailDTO();
            detailDTO.setOrgId(StringUtil.EMPTY);
            detailDTO.setOrgName(StringUtil.EMPTY);
            detailDTO.setCollectionAccountsBank(nonConfirmCollectionSumEntity.getCollectionAccountsBank());
            detailDTO.setCollectionAccountsBankCode(nonConfirmCollectionSumEntity.getCollectionAccountsBankCode());
            detailDTO.setEbankSerialNumber(nonConfirmCollectionSumEntity.getEbankSerialNumber());
            detailDTO.setBankAmount(nonConfirmCollectionSumEntity.getBankAmount());

            // 计算已认领金额
            BigDecimal claimAmount = new BigDecimal(0);
            if (bcrrEntityList != null && !bcrrEntityList.isEmpty()) {
                for (int k = 0; k < bcrrEntityList.size(); k++) {
                    claimAmount = claimAmount.add(bcrrEntityList.get(k).getClaimAmount());
                }
            }
            detailDTO.setClaimAmount(claimAmount);
            detailDTO.setRemainNonConfirmAmount(nonConfirmCollectionSumEntity.getBankAmount().subtract(claimAmount));
            result.getClaimQueryResultDetailDTO().add(detailDTO);
        }
        return R.ok(result);
    }

    /**
     * 认领确认
     */
    public R claimConfirm(ClaimConfirmDTO claimConfirmDTO, String confirmKey) {

        // 未确认认领记录
        NonConfirmCollectionSumEntity nonConfirmCollectionSumEntity = nonConfirmCollectionSumMapper.
                selectById(claimConfirmDTO.getId());

        String validationResult = this.claimConfirmVlidation(claimConfirmDTO, nonConfirmCollectionSumEntity);
        if (StringUtils.isNotEmpty(validationResult)) {
            return R.fail(validationResult);
        }

        // 认领记录
        List<ClaimQueryResultDetailDTO> claimQueryResultDetailList = claimConfirmDTO.getClaimQueryResultDetailList().
                stream().filter(e->e.getClaimAmount() != null && e.getClaimAmount().compareTo(new BigDecimal(0)) != 0).
                collect(Collectors.toList());

        /********From:add for 新增校验财务合同状态为6类资产转让状态时进行软提示 by zhangli.chen on 20250415 ***/
        HthxSoftTipValidationResult validation = softTipsValidate(claimConfirmDTO);
        if (validation.isHasWarn() && StringUtils.isEmpty(confirmKey)) {
            // 生成软提示数据
            confirmKey = HthxUUIDUtils.generateUUID();
            HthxBaseConfirmData confirmData = new HthxBaseConfirmData(confirmKey, claimConfirmDTO.generateDataHash());
            hthxConfirmCacheService.setConfirmData(confirmKey, confirmData);
            return R.warn(confirmData,validation.getWarnMessage());
        }
        if (StringUtils.isNotEmpty(confirmKey)) {
            // 从缓存中获取上次提交数据
            HthxBaseConfirmData lastCacheData = hthxConfirmCacheService.getConfirmData(confirmKey);
            // 数据一致性校验
            if (!(claimConfirmDTO.generateDataHash()==lastCacheData.getDataHash())) {
                return R.fail(ResultEnum.COMMON_SUBMIT_DATA_INVALID.getMessage());
            }
        }
        /********End:add for 新增校验财务合同状态为6类资产转让状态时进行软提示 by zhangli.chen on 20250415 ***/

        // 取得最大批次号
        int maxBatchNo = businessClaimRepaymentRecordService.getMaxBatchNo(nonConfirmCollectionSumEntity.getEbankSerialNumber());
        maxBatchNo = maxBatchNo + 1;

        // 记账日期
        Instant instant = claimConfirmDTO.getAccountDate().toInstant();
        LocalDateTime businessDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        // 明细记录保存
        Long detailId = IdWorker.getId();
        NonConfirmCollectionSecondDetailEntity nonConfirmCollectionSecondDetail = new NonConfirmCollectionSecondDetailEntity();

        // 凭证行记录按照做账主体进行拆分
        Map<String, List<ClaimConfirmVoucherDTO>> voucherSplit = claimConfirmDTO.getClaimConfirmVoucherList().stream().
                collect(Collectors.groupingBy(e->e.getCreateConfirmOrgId()));
        String manualIds = StringUtil.EMPTY;
        String writeOffVoucherIds = StringUtil.EMPTY;
        for (String createConfirmOrgId : voucherSplit.keySet()) {
            List<ClaimConfirmVoucherDTO> claimConfirmVoucherDTOList = voucherSplit.get(createConfirmOrgId);

            ContractDTO contractDTO = this.queryContractDTO(claimConfirmVoucherDTOList);

            // 创建手工凭证记录并保存
            Long manualId = this.saveManualVoucher(claimConfirmVoucherDTOList, createConfirmOrgId,
                    nonConfirmCollectionSumEntity.getCurrencyType(), businessDate,
                    ClaimOperationTypeEnum.MANUAL_CLAIM.getCode());
            if (StringUtils.isEmpty(manualIds)) {
                manualIds = String.valueOf(manualId);
            } else {
                manualIds = manualIds.concat(",").concat(String.valueOf(manualId));
            }

            // 若合同为核销状态的合同（财务合同状态为“正常核销”、“亏损结清”、“亏损结清、服务费核销”任意一种），
            // 除在本模块录入的手工认领凭证外，还需自动生成核销回款凭证
            if (contractDTO != null) {
                if (FinancialContractStatusEnum.LOSS_SETTLEMENT_SERVICE_FEE_CANCELLATION.getDesc().
                        equals(contractDTO.getFinancialContractStatus())
                        || FinancialContractStatusEnum.ONE.getDesc().equals(contractDTO.getFinancialContractStatus())
                        || FinancialContractStatusEnum.TWO.getDesc().equals(contractDTO.getFinancialContractStatus())) {
                    // 生成核销回款凭证
                    String tempWriteOffVoucherId = this.generateWriteOffVoucher(detailId, contractDTO, claimConfirmDTO.getAccountDate(),
                            claimConfirmVoucherDTOList, nonConfirmCollectionSumEntity);
                    if (StringUtils.isEmpty(writeOffVoucherIds)) {
                        writeOffVoucherIds = tempWriteOffVoucherId;
                    } else {
                        writeOffVoucherIds = writeOffVoucherIds.concat(",").concat(tempWriteOffVoucherId);
                    }
                }
            }
        }

        // 提交审核
        Long approveId = this.submitVoucher(detailId, BatchTypeEnum.SGPZRL.getCode());

        nonConfirmCollectionSecondDetail.setId(detailId);
        nonConfirmCollectionSecondDetail.setSumId(claimConfirmDTO.getId());
        nonConfirmCollectionSecondDetail.setSystemCode(SystemEnum.CWZT.getCode());
        nonConfirmCollectionSecondDetail.setSystemName(SystemEnum.CWZT.getDesc());
        nonConfirmCollectionSecondDetail.setBusinessDate(nonConfirmCollectionSumEntity.getBusinessDate());
        nonConfirmCollectionSecondDetail.setBusinessHappenDate(businessDate);
        nonConfirmCollectionSecondDetail.setCurrencyType(nonConfirmCollectionSumEntity.getCurrencyType());
        nonConfirmCollectionSecondDetail.setBankAmount(nonConfirmCollectionSumEntity.getBankAmount());
        nonConfirmCollectionSecondDetail.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
        nonConfirmCollectionSecondDetail.setManualVoucherIds(manualIds);
        nonConfirmCollectionSecondDetail.setApproveId(approveId);
        nonConfirmCollectionSecondDetail.setOperationType(ClaimOperationTypeEnum.MANUAL_CLAIM.getCode());
        nonConfirmCollectionSecondDetail.setWriteOffVoucherId(writeOffVoucherIds);
        BigDecimal curClaimAmount = claimQueryResultDetailList.stream().map(ClaimQueryResultDetailDTO::getClaimAmount).
                reduce(BigDecimal.ZERO, BigDecimal::add);
        nonConfirmCollectionSecondDetail.setCurClaimAmount(curClaimAmount);
        nonConfirmCollectionSecondDetail.setIsRelateClientAuxiliaryAccount(claimConfirmDTO.getIsRelateClientAuxiliaryAccount());
        nonConfirmCollectionSecondDetailService.save(nonConfirmCollectionSecondDetail);

        // 保存认领记录
        List<BusinessClaimRepaymentRecordEntity> claimRecordList = new ArrayList<>();

        // 到账主体和认领主体是否一致判断
        boolean isSameForAccountBankAndConfirmOrg = false;

        // 校验到账主体和认领主体是否一致
        for (ClaimQueryResultDetailDTO dto : claimConfirmDTO.getClaimQueryResultDetailList()) {
            if (!StringUtils.equals(dto.getCollectionAccountsBankCode(), dto.getOrgId())) {
                isSameForAccountBankAndConfirmOrg = true;
                break;
            }
        }

        // 取得合同信息
        String contractCode = claimConfirmDTO.getClaimConfirmVoucherList().stream().
                filter(e -> StringUtils.isNotEmpty(e.getContractCode())).findFirst().orElse(null).getContractCode();
        for (ClaimQueryResultDetailDTO dto : claimQueryResultDetailList) {
            BusinessClaimRepaymentRecordEntity entity = new BusinessClaimRepaymentRecordEntity();
            entity.setId(IdWorker.getId());
            entity.setSystemCode(SystemEnum.CWZT.getCode());
            entity.setClaimAmount(dto.getClaimAmount());
            entity.setClientCode(nonConfirmCollectionSumEntity.getClientCode());
            entity.setContractCode(contractCode);
            entity.setCurrencyType(nonConfirmCollectionSumEntity.getCurrencyType());
            entity.setBusinessDate(businessDate);
            entity.setEbankSerialNumber(dto.getEbankSerialNumber());
            entity.setOrgId(dto.getOrgId());
            entity.setOrgName(dto.getOrgName());
            entity.setBatchNo(new BigDecimal(maxBatchNo));
            entity.setNonConfirmSecondDetailId(detailId);
            entity.setRemark(dto.getRemark());
            entity.setSceneCode(SceneEnum.SGPZRL.getCode());
            entity.setSceneName(SceneEnum.SGPZRL.getDesc());
            entity.setOperationType(ClaimOperationTypeEnum.MANUAL_CLAIM.getCode());
            if (isSameForAccountBankAndConfirmOrg) {
                entity.setIsCrossOrg(YesOrNoEnum.YES.getCode());
            } else {
                entity.setIsCrossOrg(YesOrNoEnum.NO.getCode());
            }
            claimRecordList.add(entity);
        }
        businessClaimRepaymentRecordService.saveBatch(claimRecordList);

        // 保存填入的凭证信息
        this.saveVoucherRecordList(claimConfirmDTO.getClaimConfirmVoucherList(), detailId);
        return R.ok();
    }

    /**
     * 创建文件
     */
    private File createFile(String fileName) throws IOException {
        String directoryPath = this.getFileStoragePath();
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        org.springframework.core.io.Resource resource = resourceLoader.
                getResource("classpath:FileTemplate/BatchClaimTemplate.xlsx");
        File targetFile = new File(directory.getAbsolutePath().concat(File.separator).concat(fileName));
        if (targetFile.exists()) {
            targetFile.delete();
        }

        FileUtils.copyInputStreamToFile(resource.getInputStream(), targetFile);
//        File newFile = new File(directory.getAbsolutePath().concat(File.separator).concat("BatchClaimTemplate.xlsx"));
//        newFile.renameTo(targetFile);
        return targetFile;
    }

    /**
     * 批量认领
     */
    public R<String> batchClaimConfirm(MultipartFile file) throws Exception {

        ExcelUtil<BatchClaimConfirmDTO> util = new ExcelUtil<BatchClaimConfirmDTO>(BatchClaimConfirmDTO.class);
        InputStream inputStream = file.getInputStream();
        try {
            List<BatchClaimConfirmDTO> dataList = util.importExcel(inputStream);
            if (dataList == null || dataList.isEmpty()) {
                return R.fail("导入数据不能为空!");
            }

            boolean isErrors = this.batchClaimConfirmValidation(dataList);
            if (isErrors) {
                // 创建错误信息的文件，并存储记录
                this.createErrorFile(file, dataList);
                return R.ok(Constants.FILE_DOWNLOAD_ERRS_FLAG);
            }

            // 按照认领主体进行汇总
            Map<String, List<BatchClaimConfirmDTO>> dataListMap = dataList.stream().collect(Collectors.groupingBy(e->e.getOrgId()));
            List<String> batchIdList = new ArrayList<>();
            for (String orgId : dataListMap.keySet()) {
                Long batchId = IdWorker.getId();
                // 生成审核记录
                Long approveId = this.submitVoucher(batchId, BatchTypeEnum.SGPZRL.getCode());
                List<BatchClaimConfirmDTO> newDataList = dataListMap.get(orgId);
                for (BatchClaimConfirmDTO dto : newDataList) {

                    Long detailId = IdWorker.getId();
                    // 生成凭证记录
                    String voucherId = this.generateBatchClaimVoucher(detailId, dto, batchId);

                    // 生成认领记录
                    BusinessClaimRepaymentRecordEntity entity = new BusinessClaimRepaymentRecordEntity();
                    entity.setId(IdWorker.getId());
                    entity.setSystemCode(SystemEnum.CWZT.getCode());
                    entity.setClaimAmount(dto.getClaimAmount());
                    if (dto.getClientEntity() != null) {
                        entity.setClientCode(dto.getClientEntity().getClientCode());
                    }
                    entity.setContractCode(dto.getContractCode());
                    entity.setCurrencyType(dto.getNonConfirmCollectionSumEntity().getCurrencyType());
                    entity.setBusinessDate(DateUtils.parseLocalDateTime(DateUtils.dateTime(dto.getAccountDate())));
                    entity.setEbankSerialNumber(dto.getEbankSerialNumber());
                    entity.setOrgId(dto.getOrgId());
                    entity.setOrgName(dto.getOrgName());

                    int maxBatchNo = businessClaimRepaymentRecordService.getMaxBatchNo(dto.getEbankSerialNumber());
                    maxBatchNo = maxBatchNo + 1;
                    entity.setBatchNo(new BigDecimal(maxBatchNo));
                    entity.setNonConfirmSecondDetailId(detailId);
//            entity.setRemark(dto.getRemark());
                    entity.setSceneCode(SceneEnum.SGPZRL.getCode());
                    entity.setSceneName(SceneEnum.SGPZRL.getDesc());
                    entity.setOperationType(ClaimOperationTypeEnum.MANUAL_CLAIM.getCode());
                    if (!StringUtils.equals(dto.getCollectionAccountsBank(), dto.getOrgName())) {
                        entity.setIsCrossOrg(YesOrNoEnum.YES.getCode());
                    } else {
                        entity.setIsCrossOrg(YesOrNoEnum.NO.getCode());
                    }
                    businessClaimRepaymentRecordService.save(entity);

                    // 生成操作记录
                    NonConfirmCollectionSecondDetailEntity nonConfirmCollectionSecondDetail = new NonConfirmCollectionSecondDetailEntity();
                    nonConfirmCollectionSecondDetail.setId(detailId);
                    nonConfirmCollectionSecondDetail.setSumId(dto.getNonConfirmCollectionSumEntity().getId());
                    nonConfirmCollectionSecondDetail.setSystemCode(SystemEnum.CWZT.getCode());
                    nonConfirmCollectionSecondDetail.setSystemName(SystemEnum.CWZT.getDesc());
                    nonConfirmCollectionSecondDetail.setBusinessDate(dto.getNonConfirmCollectionSumEntity().getBusinessDate());
                    nonConfirmCollectionSecondDetail.setBusinessHappenDate(DateUtils.parseLocalDateTime(DateUtils.dateTime(dto.getAccountDate())));
                    nonConfirmCollectionSecondDetail.setCurrencyType(dto.getNonConfirmCollectionSumEntity().getCurrencyType());
                    nonConfirmCollectionSecondDetail.setBankAmount(dto.getNonConfirmCollectionSumEntity().getBankAmount());
                    nonConfirmCollectionSecondDetail.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
                    nonConfirmCollectionSecondDetail.setApproveId(approveId);
                    nonConfirmCollectionSecondDetail.setOperationType(ClaimOperationTypeEnum.MANUAL_CLAIM.getCode());
                    nonConfirmCollectionSecondDetail.setCurClaimAmount(dto.getClaimAmount());
                    if (YesOrNoEnum.YES.getDesc().equals(dto.getIsRelateClientAuxiliaryAccount())) {
                        nonConfirmCollectionSecondDetail.setIsRelateClientAuxiliaryAccount(YesOrNoEnum.YES.getCode());
                    } else if (YesOrNoEnum.NO.getDesc().equals(dto.getIsRelateClientAuxiliaryAccount())) {
                        nonConfirmCollectionSecondDetail.setIsRelateClientAuxiliaryAccount(YesOrNoEnum.NO.getCode());
                    }
                    nonConfirmCollectionSecondDetail.setVoucherIds(voucherId);
                    nonConfirmCollectionSecondDetail.setUploadFileId(batchId);
                    nonConfirmCollectionSecondDetailService.save(nonConfirmCollectionSecondDetail);
                }
                batchIdList.add(String.valueOf(batchId));
            }
            String batchIds = batchIdList.stream().collect(Collectors.joining(","));
            this.saveNonConfirmCollectionFileUploadRecord(file.getOriginalFilename(), StringUtils.EMPTY, dataList.size(), batchIds);
            return R.ok();
        }  catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    /**
     * 保存文件上传记录
     */
    private void saveNonConfirmCollectionFileUploadRecord(String fileName, String errFilePath, Integer totalCount, String batchIds) {
        NonConfirmCollectionFileUploadRecordEntity entity = new NonConfirmCollectionFileUploadRecordEntity();
        String staffCode = UserUtils.getStaffCode();
        Long id = IdWorker.getId();
        entity.setId(id);
        entity.setUploader(staffCode);
        entity.setUploadTime(LocalDateTime.now());
        entity.setFileName(fileName);
        entity.setErrorFileUrl(errFilePath);
        entity.setTotalRecords(totalCount);
        if (StringUtils.isEmpty(batchIds)) {
            entity.setOrgBatchIds(String.valueOf(id));
        } else {
            entity.setOrgBatchIds(batchIds);
        }
        nonConfirmCollectionFileUploadRecordService.save(entity);
    }

    /**
     * 校验不通过则生成错误文件
     */
    private void createErrorFile(MultipartFile file, List<BatchClaimConfirmDTO> dataList) throws Exception {
        // 错误文件download到服务器上生成URL
        String fileName = file.getOriginalFilename().replace(".xlsx", "");
        String curTime = DateUtils.parseDateToStr(DateUtils.YYYYMMDDHHMMSS, DateUtils.getNowDate());
        fileName = fileName.replace(".xls", "").concat(curTime).concat(".xlsx");
        File targetFile = this.createFile(fileName);
        ExcelWriter writer = cn.hutool.poi.excel.ExcelUtil.getWriter(targetFile);
        writer.setSheet(0);
        writer.passRows(1);
        writer.setColumnWidth(0, 36);
        writer.setColumnWidth(1, 36);
        writer.setColumnWidth(2, 12);
        writer.setColumnWidth(3, 30);
        writer.setColumnWidth(4, 13);
        writer.setColumnWidth(5, 15);
        writer.setColumnWidth(6, 10);
        writer.setColumnWidth(7, 30);
        writer.setColumnWidth(8, 20);
        writer.setColumnWidth(9, 20);
        writer.setColumnWidth(10, 20);
        writer.setColumnWidth(11, 36);
        writer.setColumnWidth(12, 20);
        writer.setColumnWidth(13, 30);
        writer.setColumnWidth(14, 30);
        writer.setColumnWidth(15, 50);
        List<List<String>> rows = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            List<String> columnList = new ArrayList<>();
            BatchClaimConfirmDTO dto = dataList.get(i);
            if (StringUtils.isNotEmpty(dto.getCollectionAccountsBank())) {
                columnList.add(dto.getCollectionAccountsBank());
            } else {
                columnList.add(StringUtils.EMPTY);
            }

            if (StringUtils.isNotEmpty(dto.getOrgName())) {
                columnList.add(dto.getOrgName());
            } else {
                columnList.add(StringUtils.EMPTY);
            }

            if (dto.getAccountDate() != null) {
                columnList.add(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, dto.getAccountDate()));
            } else {
                columnList.add(StringUtils.EMPTY);
            }

            if (StringUtils.isNotEmpty(dto.getEbankSerialNumber())) {
                columnList.add(dto.getEbankSerialNumber());
            } else {
                columnList.add(StringUtils.EMPTY);
            }

            if (dto.getBankAmount() != null) {
                columnList.add(dto.getBankAmount().toString());
            } else {
                columnList.add(StringUtils.EMPTY);
            }

            if (dto.getRemainNonConfirmAmount() != null) {
                columnList.add(dto.getRemainNonConfirmAmount().toString());
            } else {
                columnList.add(StringUtils.EMPTY);
            }

            if (dto.getClaimAmount() != null) {
                columnList.add(dto.getClaimAmount().toString());
            } else {
                columnList.add(StringUtils.EMPTY);
            }

            if (StringUtils.isNotEmpty(dto.getAmountType())) {
                columnList.add(dto.getAmountType());
            } else {
                columnList.add(StringUtils.EMPTY);
            }

            if (StringUtils.isNotEmpty(dto.getConfiscateBusinessType())) {
                columnList.add(dto.getConfiscateBusinessType());
            } else {
                columnList.add(StringUtils.EMPTY);
            }

            if (StringUtils.isNotEmpty(dto.getBriefFeeType())) {
                columnList.add(dto.getBriefFeeType());
            } else {
                columnList.add(StringUtils.EMPTY);
            }

            if (StringUtils.isNotEmpty(dto.getContractCode())) {
                columnList.add(dto.getContractCode());
            } else {
                columnList.add(StringUtils.EMPTY);
            }

            if (StringUtils.isNotEmpty(dto.getClientCode())) {
                columnList.add(dto.getClientCode());
            } else {
                columnList.add(StringUtils.EMPTY);
            }

            if (StringUtils.isNotEmpty(dto.getIsRelateClientAuxiliaryAccount())) {
                columnList.add(dto.getIsRelateClientAuxiliaryAccount());
            } else {
                columnList.add(StringUtils.EMPTY);
            }

            if (StringUtils.isNotEmpty(dto.getEbankNum())) {
                columnList.add(dto.getEbankNum());
            } else {
                columnList.add(StringUtils.EMPTY);
            }

            if (StringUtils.isNotEmpty(dto.getBillContractCode())) {
                columnList.add(dto.getBillContractCode());
            } else {
                columnList.add(StringUtils.EMPTY);
            }

            if (StringUtils.isNotEmpty(dto.getErrs())) {
                columnList.add(dto.getErrs());
            } else {
                columnList.add(StringUtils.EMPTY);
            }
            rows.add(columnList);
        }
        writer.write(rows, true);
        writer.close();

        this.saveNonConfirmCollectionFileUploadRecord(file.getOriginalFilename(), targetFile.getAbsolutePath(),
                dataList.size(), null);
    }

    private boolean batchClaimConfirmValidation(List<BatchClaimConfirmDTO> dataList) {

        // 签约主体
        List<OrgCompanyVO> companyVOList = orgCompanyService.selectByCondition(new OrgCompanyQueryDTO());
        Map<String, String> companyMap = companyVOList.stream().collect(
                Collectors.toMap(e->e.getOrgName(), e->e.getOrgId(), (a, b) -> b));

        // 批量认领上传加校验：单个批扣流水号上传多行认领记录时，需汇总改批扣流水号的汇总认领金额是否大于剩余未确认金额，
        // 若大于则报错，无法上传，返回错误信息
        Map<String, List<BatchClaimConfirmDTO>> dataListMap = dataList.stream().
                collect(Collectors.groupingBy(e->e.getEbankSerialNumber()));
        Map<String, BigDecimal> batchClaimAmountMap = new HashMap<>();
        for (String key : dataListMap.keySet()) {
            BigDecimal batchClaimAmount = dataListMap.get(key).stream().filter(e->e.getClaimAmount() != null).
                    map(e->e.getClaimAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
            batchClaimAmountMap.put(key, batchClaimAmount);
        }

        boolean isErrors = false;

        for (int i = 0; i < dataList.size(); i++) {
            BatchClaimConfirmDTO dto  = dataList.get(i);
            StringBuffer errs = new StringBuffer();

            if (StringUtils.isEmpty(dto.getEbankSerialNumber())) {
                errs.append("业务系统批扣流水号不能为空! | ");
                isErrors = true;
                dto.setErrs(errs.toString());
                continue;
            }

            NonConfirmCollectionSumEntity entity = new NonConfirmCollectionSumEntity();
            entity.setEbankSerialNumber(dto.getEbankSerialNumber());
            List<NonConfirmCollectionSumEntity> nonConfirmCollectionSumEntityList = this.getNonConfirmCollectionSumDTOByCon(entity);
            if (nonConfirmCollectionSumEntityList == null || nonConfirmCollectionSumEntityList.isEmpty()) {
                errs.append("业务系统批扣流水号:" + dto.getEbankSerialNumber() + "未找到和资金系统的映射记录 | ");
                isErrors = true;
            } else if (nonConfirmCollectionSumEntityList.size() > 1) {
                errs.append("业务系统批扣流水号:" + dto.getEbankSerialNumber() + "找到多条和资金系统的映射记录 | ");
                isErrors = true;
            } else {
                dto.setNonConfirmCollectionSumEntity(nonConfirmCollectionSumEntityList.get(0));
                dto.setCollectionAccountsBank(nonConfirmCollectionSumEntityList.get(0).getCollectionAccountsBank());
                dto.setBankAmount(dto.getNonConfirmCollectionSumEntity().getBankAmount());
            }

            // 认领主体不能为空
            if (StringUtils.isEmpty(dto.getOrgName())) {
                errs.append("对应认领主体不能为空(合同为空)! | ");
                isErrors = true;
            } else {
                String orgId = companyMap.get(dto.getOrgName());
                if (StringUtils.isEmpty(orgId)) {
                    errs.append("未查询到认领主体名称! | ");
                    isErrors = true;
                } else {
                    dto.setOrgId(orgId);
                }
            }

            // 认领金额不能为空
            if (dto.getClaimAmount() == null) {
                errs.append("认领金额不能为空! | ");
                isErrors = true;
            } else {
                if (dto.getNonConfirmCollectionSumEntity() != null) {
                    List<BusinessClaimRepaymentRecordEntity> claimList = businessClaimRepaymentRecordService.
                            selectClaimRecordByDeductBatchNo(dto.getEbankSerialNumber(), ProcessStatusEnum.getValidCode(), null, null);
                    BigDecimal claimedAmount = businessClaimRepaymentRecordService.getReadyClaimAmount(claimList);
                    BigDecimal remainClaimAmount = dto.getNonConfirmCollectionSumEntity().getBankAmount().subtract(claimedAmount);
                    if (dto.getClaimAmount().compareTo(remainClaimAmount) > 0) {
                        errs.append("认领金额大于剩余未确认金额! | ");
                        isErrors = true;
                    }

                    BigDecimal batchClaimAmount = batchClaimAmountMap.get(dto.getEbankSerialNumber());
                    if (batchClaimAmount != null && batchClaimAmount.compareTo(remainClaimAmount) > 0) {
                        errs.append("该批扣流水号该批次认领的总金额大于剩余未确认金额! | ");
                        isErrors = true;
                    }

                    remainClaimAmount = remainClaimAmount.subtract(dto.getClaimAmount());
                    if (remainClaimAmount.compareTo(dto.getNonConfirmCollectionSumEntity().getBankAmount()) > 0) {
                        errs.append("剩余未认领金额(之前的剩余未认领金额-该次认领金额)大于到账金额! | ");
                        isErrors = true;
                    }

                    dto.setRemainNonConfirmAmount(remainClaimAmount);
                }
            }

            // 金额类型
            if (StringUtils.isEmpty(dto.getAmountType())) {
                errs.append("金额类型不能为空! | ");
                isErrors = true;
            } else {
                if ("罚没".equals(dto.getAmountType()) && StringUtils.isEmpty(dto.getConfiscateBusinessType())) {
                    errs.append("罚没业务类型不能为空! | ");
                    isErrors = true;
                }
                if ("诉讼费".equals(dto.getAmountType()) && StringUtils.isEmpty(dto.getBriefFeeType())) {
                    errs.append("诉讼费类型不能为空! | ");
                    isErrors = true;
                }
            }

            // 合同
//            if (StringUtils.isEmpty(dto.getContractCode())) {
//                errs.append("签约主体:" + dto.getEbankSerialNumber() + "行，合同不能为空! | ");
//            }

            if (StringUtils.isNotEmpty(dto.getContractCode()) && StringUtils.isNotEmpty(dto.getOrgId())) {
                ContractDTO contractDTO = contractService.getContractDTOByCode(dto.getContractCode(), dto.getOrgId());
                if (contractDTO == null) {
                    errs.append("未查询到合同信息! | ");
                    isErrors = true;
                } else {
                    dto.setContractDTO(contractDTO);
                    dto.setClientCode(contractDTO.getClientCode());
                }
            }

            // 客户
            if (StringUtils.isNotEmpty(dto.getClientCode())) {
                ClientEntity clientEntity = clientService.selectClientByCode(dto.getClientCode());
                if (clientEntity == null) {
                    errs.append("未查询到客户信息! | ");
                    isErrors = true;
                } else {
                    dto.setClientEntity(clientEntity);
                }
            }

            // 是否涉及其他客户及辅助帐
            if (StringUtils.isEmpty(dto.getIsRelateClientAuxiliaryAccount())) {
                errs.append("是否涉及其他客户及辅助帐不能为空! | ");
                isErrors = true;
            } else {
                if (!YesOrNoEnum.YES.getDesc().equals(dto.getIsRelateClientAuxiliaryAccount())
                        && !YesOrNoEnum.NO.getDesc().equals(dto.getIsRelateClientAuxiliaryAccount())) {
                    errs.append("是否涉及其他客户及辅助帐值错误! | ");
                    isErrors = true;
                }
            }

            dto.setErrs(errs.toString());

        }
        return isErrors;
    }

    /**
     * 保存凭证的记录
     */
    private void saveVoucherRecordList(List<ClaimConfirmVoucherDTO> claimConfirmVoucherDTOList, Long detailId) {
        List<NonConfirmCollectionVoucherRecordEntity> voucherRecordEntityList = new ArrayList<>();
        for (ClaimConfirmVoucherDTO dto : claimConfirmVoucherDTOList) {
            NonConfirmCollectionVoucherRecordEntity entity = new NonConfirmCollectionVoucherRecordEntity();
            entity.setId(IdWorker.getId());
            entity.setAmount(dto.getAmount());
            entity.setAccountNumber(dto.getAccountNumber());
            entity.setClientCode(dto.getClientCode());
            entity.setDetailId(detailId);
            entity.setOrgId(dto.getCreateConfirmOrgId());
            entity.setCrOrDt(dto.getCrOrDt());
            entity.setVoucherComments(dto.getVoucherComments());
            entity.setContractCode(dto.getContractCode());
            entity.setOrgName(dto.getCreateConfirmOrgName());
            entity.setAccountName(dto.getAccountName());
            voucherRecordEntityList.add(entity);
        }
        nonConfirmCollectionVoucherRecordService.saveBatch(voucherRecordEntityList);
    }

    /**
     * 批量认领-凭证生成
     */
    private String generateBatchClaimVoucher(Long detailId, BatchClaimConfirmDTO dto, Long batchId) {
        ExecuteCommonDTO commonDTO = new ExecuteCommonDTO();
        commonDTO.setSystemCode(SystemEnum.CWZT.getCode());
        commonDTO.setSystemName(SystemEnum.CWZT.getDesc());
        commonDTO.setBusinessCode(BusinessEnum.ZLYW.getCode());
        commonDTO.setBusinessName(BusinessEnum.ZLYW.getDesc());
        commonDTO.setOrderId(String.valueOf(detailId));
        commonDTO.setOrgId(dto.getOrgId());
        commonDTO.setContractCode(dto.getContractCode());
        if (dto.getContractDTO() != null) {
            commonDTO.setContractName(dto.getContractDTO().getContractName());
        }
        commonDTO.setCurrencyType(dto.getNonConfirmCollectionSumEntity().getCurrencyType());
        if (dto.getClientEntity() != null) {
            commonDTO.setClientCode(dto.getClientEntity().getClientCode());
            commonDTO.setClientName(dto.getClientEntity().getClientName());
        }
        commonDTO.setSceneCode(SceneEnum.ZLSK.getCode());
        commonDTO.setSceneName(SceneEnum.ZLSK.getDesc());
        commonDTO.setBusinessDate(dto.getAccountDate());
        commonDTO.setBatchId(batchId);
        commonDTO.setBatchType(BatchTypeEnum.SGPZRL.getCode());
        Map<String, Object> commonMap = BeanUtil.beanToMap(commonDTO);

        // 批扣流水号
        commonMap.put("ebankBatchNo", dto.getEbankSerialNumber());
        // 批次号
        commonMap.put("ebankSerialNumber", dto.getNonConfirmCollectionSumEntity().getBusinessEbankNumber());
        // 是否批量
        commonMap.put("isBulkOperation", YesOrNoEnum.YES.getDesc());
        // 银行到账主体
        commonMap.put("bankOrgId", dto.getNonConfirmCollectionSumEntity().getCollectionAccountsBankCode());
        // 金额类型
        commonMap.put("fundType", dto.getAmountType());
        // 罚没业务类型
        commonMap.put("confiscatedType", dto.getConfiscateBusinessType());
        // 诉讼费类型
        commonMap.put("litigationExpensesType", dto.getBriefFeeType());
        // 认领金额
        commonMap.put("claimAmount", dto.getClaimAmount());
        // 是否涉及其他客户及辅助账
        commonMap.put("isInvolvingOtherDimension", dto.getIsRelateClientAuxiliaryAccount());
        // 银行账号
        commonMap.put("ebankNum", dto.getEbankNum());
        // 借款合同编号
        commonMap.put("billContractCode", dto.getBillContractCode());

        commonMap.put("createDate", DateUtils.dateTimeNow());
        commonMap.put("updateDate", DateUtils.dateTimeNow());
        commonMap.put(RuleConstant.IS_SUBMIT, YesOrNoEnum.YES.getCode());

        List<VoucherDTO> voucherDTOList = ruleService.executeRule(commonMap);

        if (CollectionUtils.isNotEmpty(voucherDTOList)) {

            String vouchIds = voucherDTOList.stream().map(VoucherDTO::getId).map(String::valueOf).distinct().collect(
                    Collectors.joining(","));
            return vouchIds;
        } else {
            return StringUtil.EMPTY;
        }
    }

    /**
     * 生成核销回款凭证
     */
    private String generateWriteOffVoucher(Long detailId, ContractDTO contractDTO, Date accountDate,
                                            List<ClaimConfirmVoucherDTO> claimConfirmVoucherDTOList,
                                           NonConfirmCollectionSumEntity nonConfirmCollectionSumEntity) {
        ExecuteCommonDTO commonDTO = new ExecuteCommonDTO();
        commonDTO.setSystemCode(SystemEnum.CWZT.getCode());
        commonDTO.setSystemName(SystemEnum.CWZT.getDesc());
        commonDTO.setBusinessCode(BusinessEnum.ZLYW.getCode());
        commonDTO.setBusinessName(BusinessEnum.ZLYW.getDesc());
        commonDTO.setOrderId(String.valueOf(detailId));
        commonDTO.setOrgId(contractDTO.getOrgId());
        commonDTO.setContractCode(contractDTO.getContractCode());
        commonDTO.setContractName(contractDTO.getContractName());
        commonDTO.setCurrencyType(nonConfirmCollectionSumEntity.getCurrencyType());
        commonDTO.setClientCode(nonConfirmCollectionSumEntity.getClientCode());
        commonDTO.setClientName(nonConfirmCollectionSumEntity.getClientName());
        commonDTO.setSceneCode(SceneEnum.ZLSK.getCode());
        commonDTO.setSceneName(SceneEnum.ZLSK.getDesc());
        commonDTO.setBusinessDate(accountDate);
        commonDTO.setBatchId(detailId);
        commonDTO.setBatchType(BatchTypeEnum.SGPZRL.getCode());
        Map<String, Object> commonMap = BeanUtil.beanToMap(commonDTO);

        // 分录各科目金额汇总
        Map<String, List<ClaimConfirmVoucherDTO>> claimConfirmVoucherDTOMap = claimConfirmVoucherDTOList.
                stream().collect(Collectors.groupingBy(ClaimConfirmVoucherDTO::getAccountNumber));

        // 未确认收款
        List<ClaimConfirmVoucherDTO> receiveUnconfirmedList = claimConfirmVoucherDTOMap.get(Constants.RECEIVABLE_UNCONFIRM_RECEIPT);
        if (receiveUnconfirmedList != null && !receiveUnconfirmedList.isEmpty()) {
            commonMap.put("receiveUnconfirmed", receiveUnconfirmedList.stream().map(
                    ClaimConfirmVoucherDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        } else {
            commonMap.put("receiveUnconfirmed", new BigDecimal(0));
        }

        // 回收本金
        List<ClaimConfirmVoucherDTO> recyclePrincipalAmountList = claimConfirmVoucherDTOMap.get(Constants.RECYCLE_PRINCIPAL_AMOUNT);
        if (recyclePrincipalAmountList != null && !recyclePrincipalAmountList.isEmpty()) {
            commonMap.put("recyclePrincipalAmount", recyclePrincipalAmountList.stream().map(
                    ClaimConfirmVoucherDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        } else {
            commonMap.put("recyclePrincipalAmount", new BigDecimal(0));
        }

        // 收取首付款
        List<ClaimConfirmVoucherDTO> receiveFirstAmountList = claimConfirmVoucherDTOMap.get(Constants.RECEIVE_FIRST_AMOUNT);
        if (receiveFirstAmountList != null && !receiveFirstAmountList.isEmpty()) {
            commonMap.put("receiveFirstAmount", receiveFirstAmountList.stream().map(
                    ClaimConfirmVoucherDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        } else {
            commonMap.put("receiveFirstAmount", new BigDecimal(0));
        }

        // 收取留购价
        List<ClaimConfirmVoucherDTO> receiveRetainedPriceList = claimConfirmVoucherDTOMap.get(Constants.RECEIVE_RETAINED_PRICE);
        if (receiveRetainedPriceList != null && !receiveRetainedPriceList.isEmpty()) {
            commonMap.put("receiveRetainedPrice", receiveRetainedPriceList.stream().map(
                    ClaimConfirmVoucherDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        } else {
            commonMap.put("receiveRetainedPrice", new BigDecimal(0));
        }

        // 收取手续费
        List<ClaimConfirmVoucherDTO> receiveProcedureAmountList = claimConfirmVoucherDTOMap.get(Constants.RECEIVE_PROCEDURE_AMOUNT);
        if (receiveProcedureAmountList != null && !receiveProcedureAmountList.isEmpty()) {
            commonMap.put("receiveProcedureAmount", receiveProcedureAmountList.stream().map(
                    ClaimConfirmVoucherDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        } else {
            commonMap.put("receiveProcedureAmount", new BigDecimal(0));
        }

        // 收取厂商返利
        List<ClaimConfirmVoucherDTO> receiveFirmRebateList = claimConfirmVoucherDTOMap.get(Constants.RECEIVE_FIRM_REBATE);
        if (receiveFirmRebateList != null && !receiveFirmRebateList.isEmpty()) {
            commonMap.put("receiveFirmRebate", receiveFirmRebateList.stream().map(
                    ClaimConfirmVoucherDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        } else {
            commonMap.put("receiveFirmRebate", new BigDecimal(0));
        }

        // 应收保险费
        List<ClaimConfirmVoucherDTO> receivableInsuranceAmountList = claimConfirmVoucherDTOMap.get(Constants.RECEIVABLE_INSURANCE_AMOUNT);
        if (receivableInsuranceAmountList != null && !receivableInsuranceAmountList.isEmpty()) {
            commonMap.put("receivableInsuranceAmount", receivableInsuranceAmountList.stream().map(
                    ClaimConfirmVoucherDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        } else {
            commonMap.put("receivableInsuranceAmount", new BigDecimal(0));
        }

        // 收取其他收入
        List<ClaimConfirmVoucherDTO> receiveOtherRevenuesList = claimConfirmVoucherDTOMap.get(Constants.RECEIVE_OTHER_REVENUES_LIST);
        if (receiveOtherRevenuesList != null && !receiveOtherRevenuesList.isEmpty()) {
            commonMap.put("receiveOtherRevenues", receiveOtherRevenuesList.stream().map(
                    ClaimConfirmVoucherDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        } else {
            commonMap.put("receiveOtherRevenues", new BigDecimal(0));
        }

        // 收取服务费
        List<ClaimConfirmVoucherDTO> receiveServiceAmountList = claimConfirmVoucherDTOMap.get(Constants.RECEIVE_SERVICE_AMOUNT);
        if (receiveServiceAmountList != null && !receiveServiceAmountList.isEmpty()) {
            commonMap.put("receiveServiceAmount", receiveServiceAmountList.stream().map(
                    ClaimConfirmVoucherDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        } else {
            commonMap.put("receiveServiceAmount", new BigDecimal(0));
        }

        // 收取履约保证金
        List<ClaimConfirmVoucherDTO> receiveMarginAmountList = claimConfirmVoucherDTOMap.get(Constants.RECEIVE_MARGIN_AMOUNT);
        if (receiveMarginAmountList != null && !receiveMarginAmountList.isEmpty()) {
            commonMap.put("receiveMarginAmount", receiveMarginAmountList.stream().map(
                    ClaimConfirmVoucherDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        } else {
            commonMap.put("receiveMarginAmount", new BigDecimal(0));
        }

        // 收取供应商保证金
        List<ClaimConfirmVoucherDTO> receiveVendorMarginAmountList = claimConfirmVoucherDTOMap.get(Constants.RECEIVE_VOUCHER_MARGIN_AMOUNT);
        if (receiveVendorMarginAmountList != null && !receiveVendorMarginAmountList.isEmpty()) {
            commonMap.put("receiveVendorMarginAmount", receiveVendorMarginAmountList.stream().map(
                    ClaimConfirmVoucherDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        } else {
            commonMap.put("receiveVendorMarginAmount", new BigDecimal(0));
        }

        // 保险费差额
        List<ClaimConfirmVoucherDTO> receiveInsuranceDifferAmountList = claimConfirmVoucherDTOMap.get(Constants.RECEIVE_INSURANCE_DIFFER_AMOUNT);
        if (receiveInsuranceDifferAmountList != null && !receiveInsuranceDifferAmountList.isEmpty()) {
            commonMap.put("receiveInsuranceDifferAmount", receiveInsuranceDifferAmountList.stream().map(
                    ClaimConfirmVoucherDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        } else {
            commonMap.put("receiveInsuranceDifferAmount", new BigDecimal(0));
        }

        // 回收罚息
        List<ClaimConfirmVoucherDTO> recycleDefaultInterestAmountList = claimConfirmVoucherDTOMap.
                get(Constants.RECYCLE_DEFAULT_INTEREST_AMOUNT);
        if (recycleDefaultInterestAmountList != null && !recycleDefaultInterestAmountList.isEmpty()) {
            commonMap.put("recycleDefaultInterestAmount", recycleDefaultInterestAmountList.stream().map(
                    ClaimConfirmVoucherDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        } else {
            commonMap.put("recycleDefaultInterestAmount", new BigDecimal(0));
        }

        // 收取合同解约及更改手续费
        List<ClaimConfirmVoucherDTO> receiveTerminateProcedureAmountList = claimConfirmVoucherDTOMap.
                get(Constants.RECEIVE_TERMINATE_PROCEDURE_AMOUNT);
        if (receiveTerminateProcedureAmountList != null && !receiveTerminateProcedureAmountList.isEmpty()) {
            commonMap.put("receiveTerminateProcedureAmount", receiveTerminateProcedureAmountList.stream().map(
                    ClaimConfirmVoucherDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        } else {
            commonMap.put("receiveTerminateProcedureAmount", new BigDecimal(0));
        }

        // 收取违约金
        List<ClaimConfirmVoucherDTO> receivePenalList = claimConfirmVoucherDTOMap.get(Constants.RECEIVE_PENAL);
        if (receivePenalList != null && !receivePenalList.isEmpty()) {
            commonMap.put("receivePenal", receivePenalList.stream().map(
                    ClaimConfirmVoucherDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        } else {
            commonMap.put("receivePenal", new BigDecimal(0));
        }
        commonMap.put("createDate", DateUtils.dateTimeNow());
        commonMap.put("updateDate", DateUtils.dateTimeNow());
        commonMap.put(RuleConstant.IS_SUBMIT, YesOrNoEnum.YES.getCode());

        List<VoucherDTO> voucherDTOList = ruleService.executeRule(commonMap);

        if (CollectionUtils.isNotEmpty(voucherDTOList)) {

            String vouchIds = voucherDTOList.stream().map(VoucherDTO::getId).map(String::valueOf).distinct().collect(
                    Collectors.joining(","));
            return vouchIds;
        } else {
            return StringUtil.EMPTY;
        }
    }

    /**
     * 提交到审核列表
     */
    private Long submitVoucher(Long detailId, String documentType) {
        // 提交至审核页面
        List<ApproveDTO> approveDTOList = new ArrayList<>();

        ApproveDTO approveDTO = new ApproveDTO();
        approveDTO.setUrl(approveUrl.concat(String.valueOf(detailId)));
        approveDTO.setSubmitDate(LocalDateTime.now());
        approveDTO.setSubmitterName(UserUtils.getStaffName());
        approveDTO.setSubmitterNum(UserUtils.getStaffCode());
        approveDTO.setDocumentId(detailId);
        approveDTO.setDocumentType(documentType);
        approveDTOList.add(approveDTO);

        Map<Long,Long> approveSubmitResultMap = approveService.submit(approveDTOList);
        return approveSubmitResultMap.get(detailId);
    }

    private Long saveManualVoucher(List<ClaimConfirmVoucherDTO> claimConfirmVoucherDTOList, String orgId,
                                   String currencyType, LocalDateTime businessDate, String operationType) {
        // 手工记录
        Long manualId = IdWorker.getId();
        ManualEntity manualEntity = new ManualEntity();
        manualEntity.setId(manualId);
        manualEntity.setPeriodCode(Integer.parseInt(DateUtils.dateTimeNow("yyyyMM")));
        manualEntity.setBusinessDate(businessDate);
        manualEntity.setVoucherDate(businessDate);
        manualEntity.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
        manualEntity.setVoucherType("转账");
        Long voucherNumber = manualService.generateVoucherNum(manualEntity.getVoucherType(), LocalDateTime.now());
        manualEntity.setVoucherNum(voucherNumber);
        if (ClaimOperationTypeEnum.MANUAL_CLAIM.getCode().equals(operationType)) {
            manualEntity.setSceneCode(SceneEnum.SGPZRL.getCode());
            manualEntity.setSceneName(SceneEnum.SGPZRL.getDesc());
            manualEntity.setIsWriteOff(YesOrNoEnum.NO.getCode());
            manualEntity.setSourceFrom(BatchTypeEnum.SGPZRL.getCode());
        } else if (ClaimOperationTypeEnum.MANUAL_CANCEL_AMOUNT.getCode().equals(operationType)) {
            manualEntity.setSceneCode(SceneEnum.SGPZCX.getCode());
            manualEntity.setSceneName(SceneEnum.SGPZCX.getDesc());
            manualEntity.setIsWriteOff(YesOrNoEnum.YES.getCode());
            manualEntity.setSourceFrom(BatchTypeEnum.SGPZCX.getCode());
        } else if (ClaimOperationTypeEnum.MODIFY_EBANK_NO.getCode().equals(operationType)) {
            manualEntity.setSceneCode(SceneEnum.SGPZTZ.getCode());
            manualEntity.setSceneName(SceneEnum.SGPZTZ.getDesc());
            manualEntity.setIsWriteOff(YesOrNoEnum.NO.getCode());
            manualEntity.setSourceFrom(BatchTypeEnum.SGPZTZ.getCode());
        } else if (ClaimOperationTypeEnum.MODIFY_INCOME_DATE.getCode().equals(operationType)) {
            manualEntity.setSceneCode(SceneEnum.SGPZMGRZRQ.getCode());
            manualEntity.setSceneName(SceneEnum.SGPZMGRZRQ.getDesc());
            manualEntity.setIsWriteOff(YesOrNoEnum.NO.getCode());
            manualEntity.setSourceFrom(BatchTypeEnum.SGPZMGRZRQ.getCode());
        }
        manualEntity.setRate(new BigDecimal(1));
        manualEntity.setBusinessCode(BusinessEnum.ZLYW.getCode());
        manualEntity.setBusinessName(BusinessEnum.ZLYW.getDesc());
        manualEntity.setOrgId(orgId);
        manualEntity.setCurrencyCode(currencyType);
        manualService.save(manualEntity);

        // 分录信息
        List<ManualVoucherEntity> manualVoucherList = new ArrayList<>();
        for (ClaimConfirmVoucherDTO dto : claimConfirmVoucherDTOList) {
            if (dto.getAmount() == null || new BigDecimal(0).compareTo(dto.getAmount()) == 0) {
                throw new ServiceException("分录表中金额不能为空!");
            }

            ManualVoucherEntity entity = new ManualVoucherEntity();
            entity.setId(IdWorker.getId());
            entity.setContractCode(dto.getContractCode());
            entity.setClientCode(dto.getClientCode());
            entity.setClientName(dto.getClientName());
            entity.setOrgId(dto.getCreateConfirmOrgId());
            entity.setPeriodCode(Integer.parseInt(DateUtils.dateTimeNow("yyyyMM")));
            entity.setBusinessDate(businessDate);
            entity.setVoucherDate(businessDate);
            entity.setVoucherType("转账");
            entity.setBankNo(dto.getBankNo());
            entity.setLoansContractCode(dto.getLoansContractCode());
            entity.setVoucherSummary(dto.getVoucherComments());
            if (ClaimOperationTypeEnum.MANUAL_CLAIM.getCode().equals(operationType)) {
                entity.setSceneCode(SceneEnum.SGPZRL.getCode());
            } else if (ClaimOperationTypeEnum.MANUAL_CANCEL_AMOUNT.getCode().equals(operationType)) {
                entity.setSceneCode(SceneEnum.SGPZCX.getCode());
            } else if (ClaimOperationTypeEnum.MODIFY_EBANK_NO.getCode().equals(operationType)) {
                entity.setSceneCode(SceneEnum.SGPZTZ.getCode());
            } else if (ClaimOperationTypeEnum.MODIFY_INCOME_DATE.getCode().equals(operationType)) {
                entity.setSceneCode(SceneEnum.SGPZMGRZRQ.getCode());
            }

            entity.setAccountCode(dto.getAccountNumber());
            entity.setAccountName(dto.getAccountName());
            entity.setCurrencyCode(currencyType);
            entity.setRate("1");
            if (DRCREnum.DR.getCode().equals(dto.getCrOrDt())) {
                entity.setDebitAmount(dto.getAmount());
            } else if (DRCREnum.CR.getCode().equals(dto.getCrOrDt())) {
                entity.setCreditAmount(dto.getAmount());
            }
            entity.setMaterialContractCode(dto.getContractCode());
            entity.setPreparerName(SecurityUtils.getUsername());
            entity.setManualId(manualId);
            manualVoucherList.add(entity);
        }
        manualVoucherService.saveBatch(manualVoucherList);
        // 创建正式凭证
        manualService.generateVoucher(manualId, StringUtil.EMPTY, StringUtil.EMPTY,
                Boolean.FALSE,ManualServiceImpl.NON_CONFIRM_COLLECTION_SECOND_DETAIL);
        return manualId;
    }


    private String claimConfirmVlidation(ClaimConfirmDTO claimConfirmDTO, NonConfirmCollectionSumEntity nonConfirmCollectionSumEntity) {
        if (claimConfirmDTO.getClaimConfirmVoucherList() == null
                || claimConfirmDTO.getClaimConfirmVoucherList().isEmpty()) {
            return "认领凭证明细不能为空";
        }

        if (claimConfirmDTO.getClaimQueryResultDetailList() == null
                || claimConfirmDTO.getClaimQueryResultDetailList().isEmpty()) {
            return "认领数据明细不能为空";
        }

        // modify for 将下列校验代码移动至强校验汇总处 by zhangli.chen on 20250425
        // 过滤掉没有认领的数据
        List<ClaimQueryResultDetailDTO> claimQueryResultDetailList = claimConfirmDTO.getClaimQueryResultDetailList().
                stream().filter(e->e.getClaimAmount() != null && e.getClaimAmount().compareTo(new BigDecimal(0)) != 0).
                collect(Collectors.toList());
        if (claimQueryResultDetailList == null || claimQueryResultDetailList.isEmpty()) {
            return "没有认领记录!";
        }

        // 合同编码唯一，一次只能填写一个合同编码
        List<String> contractCodeList = Optional.ofNullable(claimConfirmDTO.getClaimConfirmVoucherList().stream().
                filter(e->StringUtils.isNotEmpty(e.getContractCode()))).orElse(null).
                map(ClaimConfirmVoucherDTO::getContractCode).distinct().collect(Collectors.toList());
        if (contractCodeList != null && contractCodeList.size() != 1) {
            return "合同编码不是唯一值(一个凭证只能填写唯一合同号)!";
        }

        // 未确认收款科目必须存在校验
        List<ClaimConfirmVoucherDTO> nonConfirmAmountAccountList = claimConfirmDTO.getClaimConfirmVoucherList().stream().filter(
                e-> Constants.RECEIVABLE_UNCONFIRM_RECEIPT.equals(e.getAccountNumber())).collect(Collectors.toList());
        if (nonConfirmAmountAccountList == null || nonConfirmAmountAccountList.isEmpty()) {
            return "未确认收款科目不存在!";
        }

        // 当前认领金额
        BigDecimal claimAmountSum = claimConfirmDTO.getClaimQueryResultDetailList().stream().filter(
                e->e.getClaimAmount() != null).map(ClaimQueryResultDetailDTO::getClaimAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        // 已经认领金额汇总
        List<BusinessClaimRepaymentRecordEntity> businessClaimRepaymentRecordEntityList = businessClaimRepaymentRecordService.
                selectClaimRecordByDeductBatchNo(nonConfirmCollectionSumEntity.getEbankSerialNumber(),
                        ProcessStatusEnum.getValidCode(), null, null);
        BigDecimal readyClaimAmount = businessClaimRepaymentRecordService.getReadyClaimAmount(
                businessClaimRepaymentRecordEntityList);
        // 校验：已经认领金额+当前认领金额<=到账金额
        if (claimAmountSum.add(readyClaimAmount).compareTo(nonConfirmCollectionSumEntity.getBankAmount()) > 0) {
            return "(已经认领金额+当前认领金额)大于到账金额!";
        }

        // 到账金额-已经认领金额-当前认领金额=剩余未确认金额（应该小于到账金额）
        BigDecimal remainNonClaimAmount = nonConfirmCollectionSumEntity.getBankAmount().subtract(readyClaimAmount).subtract(claimAmountSum);
        if (remainNonClaimAmount.compareTo(nonConfirmCollectionSumEntity.getBankAmount()) > 0) {
            return "剩余未确认金额大于到账金额!";
        }

        // 未确认收款科目贷方的列表
        List<ClaimConfirmVoucherDTO> nonConfirmAmountCRAccountList = nonConfirmAmountAccountList.stream().filter(
                e-> DRCREnum.CR.getCode().equals(e.getCrOrDt())).collect(Collectors.toList());

        // 贷方列表为空，则直接判断未确认收款科目的金额与认领金额是否一致
        if (nonConfirmAmountCRAccountList == null || nonConfirmAmountCRAccountList.isEmpty()) {

            // 校验未确认收款科目金额是否和弹窗合计认领金额一致，不一致则校验不通过，无法提交
            BigDecimal nonConfirmAmountSum = claimConfirmDTO.getClaimConfirmVoucherList().stream().filter(
                            e -> Constants.RECEIVABLE_UNCONFIRM_RECEIPT.equals(e.getAccountNumber())).
                    map(ClaimConfirmVoucherDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

            if (nonConfirmAmountSum.compareTo(claimAmountSum) != 0) {
                return "未确认收款科目总金额和认领总金额不一致!";
            }
        } else {

            // 贷方是否有未确认收款科目，如果有，则用借方未确认收款科目-贷方未确认收款科目=认领金额
            BigDecimal crAmount = nonConfirmAmountCRAccountList.stream().map(ClaimConfirmVoucherDTO::getAmount).
                    reduce(BigDecimal.ZERO, BigDecimal::add);

            // 未确认收款借方金额
            BigDecimal drAmount = nonConfirmAmountAccountList.stream().filter(e-> DRCREnum.DR.getCode().equals(e.getCrOrDt())).
                    map(ClaimConfirmVoucherDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (drAmount.subtract(crAmount).compareTo(claimAmountSum) != 0) {
                return "未确认收款科目总金额(借方金额-贷方金额)和认领总金额不一致!";
            }
        }

        // 校验科目信息中借贷双方金额合计是否一致，不一致则校验不通过，无法提交
        BigDecimal crSum = claimConfirmDTO.getClaimConfirmVoucherList().stream().filter(
                        e-> DRCREnum.CR.getCode().equals(e.getCrOrDt())).
                map(ClaimConfirmVoucherDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal dtSum = claimConfirmDTO.getClaimConfirmVoucherList().stream().filter(
                        e-> DRCREnum.DR.getCode().equals(e.getCrOrDt())).
                map(ClaimConfirmVoucherDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (crSum.compareTo(dtSum) != 0) {
            return "科目信息中借贷双方金额合计不一致!";
        }

        // 校验所选择科目必须是在ZLYW下的科目
        for (ClaimConfirmVoucherDTO dto : claimConfirmDTO.getClaimConfirmVoucherList()) {
            List<AccountEntity> accountEntityList = iAccountService.lambdaQuery().eq(AccountEntity::getAccountCode,
                    dto.getAccountNumber()).eq(AccountEntity::getBusinessCode, BusinessEnum.ZLYW.getCode()).list();
            if (accountEntityList == null || accountEntityList.isEmpty()) {
                return "租赁业务下不存在科目编码:".concat(dto.getAccountNumber()).concat("!");
            }
        }

        // 到账主体和认领主体是否一致判断
        boolean isSameForAccountBankAndConfirmOrg = false;

        // modify for 只针对本次认领金额不为0的认领记录进行到账主体和认领主体是否一致的判断 by zhangli.chenf on 20250425
        // 校验到账主体和认领主体是否一致
        for (ClaimQueryResultDetailDTO dto : claimConfirmDTO.getClaimQueryResultDetailList()) {
            // 只针对本次认领金额不为0的认领记录进行到账主体和认领主体是否一致的判断
            if(dto.getClaimAmount()!=null && BigDecimal.ZERO.compareTo(dto.getClaimAmount()) != 0){
                if (!StringUtils.equals(dto.getCollectionAccountsBankCode(), dto.getOrgId())) {
                    isSameForAccountBankAndConfirmOrg = true;
                    break;
                }
            }
        }

        // 查询合同信息
        ContractDTO contractDTO = this.queryContractDTO(claimConfirmDTO.getClaimConfirmVoucherList());

        // 取得科目列表
        List<String> accountNumberList = claimConfirmDTO.getClaimConfirmVoucherList().stream().
                map(ClaimConfirmVoucherDTO::getAccountNumber).collect(Collectors.toList());

        // 到账主体和认领主体不一致
        if (contractDTO != null && isSameForAccountBankAndConfirmOrg) {
            if (FinancialContractStatusEnum.ASSET_DISPOSAL_INNER_TRANSFER.getDesc().equals(
                    contractDTO.getFinancialContractStatus())) {

                // 校验若到账主体和认领主体不一致，当合同财务状态为“资产处置结束（内部转让）”时，是否已录入关联往来科目信息（2241.17
                // 其他应付款_代收转让款项+1221.15 其他应收款款_应收转让后收款），若未录入，则校验不通过，无法提交
                if (!accountNumberList.contains(Constants.OTHER_PAYABLES_PROXY_MAKE_OVER_ACCOUNT)
                        || !accountNumberList.contains(Constants.OHTER_RECEIVABLE_TRANSFER_ACCOUNT)) {
                    return "未关联往来科目信息（2241.17 其他应付款_代收转让款项+1221.15 其他应收款款_应收转让后收款）";
                }
            } else {

                // 校验若到账主体和认领主体不一致，当合同财务状态不为“资产处置结束（内部转让）”时，是否已录入关联往来科目信息
                // （2241.02 其他应付款_关联公司往来+1221.01 其他应收款款_关联公司往来），若未录入，则校验不通过，无法提交
                if (!accountNumberList.contains(Constants.OTHER_PAYABLES_CONNECT_COMPANY_ACCOUNT)
                        || !accountNumberList.contains(Constants.OTHER_RECEIVABLE_CONNECT_COMPANY_ACCOUNT)) {
                    return "未关联往来科目信息（2241.02 其他应付款_关联公司往来+1221.01 其他应收款款_关联公司往来）";
                }
            }
        }
        return "";
    }

    /**
     * 合同查询
     */
    private ContractDTO queryContractDTO(List<ClaimConfirmVoucherDTO> claimConfirmVoucherList) {
        ContractDTO contractDTO = null;
        for (ClaimConfirmVoucherDTO dto : claimConfirmVoucherList) {
            if (StringUtils.isNotEmpty(dto.getCreateConfirmOrgId()) && StringUtils.isNotEmpty(dto.getContractCode())) {
                contractDTO = contractService.getContractDTOByCode(dto.getContractCode(), dto.getCreateConfirmOrgId());
                if (contractDTO != null) {
                    break;
                }
            }
        }
        return contractDTO;
    }

    /**
     * 冲销确认
     */
    public R<String> writeOffConfirm(WriteOffConfirmDTO writeOffConfirmDTO) {

        // 未确认认领记录
        NonConfirmCollectionSumEntity nonConfirmCollectionSumEntity = nonConfirmCollectionSumMapper.
                selectById(writeOffConfirmDTO.getId());

        String validationResult = this.writeOffConfirmVlidation(writeOffConfirmDTO, nonConfirmCollectionSumEntity);
        if (StringUtils.isNotEmpty(validationResult)) {
            return R.fail(validationResult);
        }

        // 明细记录保存
        Long detailId = IdWorker.getId();

        // 取得最大批次号
        int maxBatchNo = businessClaimRepaymentRecordService.getMaxBatchNo(nonConfirmCollectionSumEntity.getEbankSerialNumber());
        maxBatchNo = maxBatchNo +  1;

        // 记账日期
        Instant instant = writeOffConfirmDTO.getAccountDate().toInstant();
        LocalDateTime businessDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        // 过滤掉没有认领的数据
        List<WriteOffDetailDTO> writeOffDetailList = writeOffConfirmDTO.getWriteOffDetailList().
                stream().filter(e->e.getWriteOffAmount() != null && e.getWriteOffAmount().compareTo(new BigDecimal(0)) != 0).
                collect(Collectors.toList());
        if (writeOffDetailList == null || writeOffDetailList.isEmpty()) {
            return R.fail("没有有效冲销记录!");
        }

        // 凭证行记录按照做账主体进行拆分
        Map<String, List<ClaimConfirmVoucherDTO>> voucherSplit = writeOffConfirmDTO.getWriteOffConfirmVoucherList().stream().
                collect(Collectors.groupingBy(e->e.getCreateConfirmOrgId()));
        String manualIds = StringUtil.EMPTY;
        String writeOffVoucherIds = StringUtil.EMPTY;
        for (String createConfirmOrgId : voucherSplit.keySet()) {
            List<ClaimConfirmVoucherDTO> claimConfirmVoucherDTOList = voucherSplit.get(createConfirmOrgId);

            // 取得合同信息
            ContractDTO contractDTO = this.queryContractDTO(claimConfirmVoucherDTOList);

            // 创建手工凭证记录并保存
            Long manualId = this.saveManualVoucher(claimConfirmVoucherDTOList, createConfirmOrgId, nonConfirmCollectionSumEntity.getCurrencyType(),
                    businessDate, ClaimOperationTypeEnum.MANUAL_CANCEL_AMOUNT.getCode());
            if (StringUtils.isEmpty(manualIds)) {
                manualIds = String.valueOf(manualId);
            } else {
                manualIds = manualIds.concat(",").concat(String.valueOf(manualId));
            }

            // 若合同为核销状态的合同（财务合同状态为“正常核销”、“亏损结清”、“亏损结清、服务费核销”任意一种），
            // 除在本模块录入的手工认领凭证外，还需自动生成核销回款凭证
            if (contractDTO != null) {
                if (FinancialContractStatusEnum.LOSS_SETTLEMENT_SERVICE_FEE_CANCELLATION.getDesc().
                        equals(contractDTO.getFinancialContractStatus())
                        || FinancialContractStatusEnum.ONE.getDesc().equals(contractDTO.getFinancialContractStatus())
                        || FinancialContractStatusEnum.TWO.getDesc().equals(contractDTO.getFinancialContractStatus())) {
                    // 生成核销回款凭证
                    String tempWriteOffVoucherIds = this.generateWriteOffVoucher(detailId, contractDTO,
                            writeOffConfirmDTO.getAccountDate(), writeOffConfirmDTO.getWriteOffConfirmVoucherList(), nonConfirmCollectionSumEntity);
                    if (StringUtils.isEmpty(writeOffVoucherIds)) {
                        writeOffVoucherIds = tempWriteOffVoucherIds;
                    } else {
                        writeOffVoucherIds = writeOffVoucherIds.concat(",").concat(tempWriteOffVoucherIds);
                    }
                }
            }
        }

        NonConfirmCollectionSecondDetailEntity nonConfirmCollectionSecondDetail = new NonConfirmCollectionSecondDetailEntity();

        // 提交审核
        Long approveId = this.submitVoucher(detailId, BatchTypeEnum.SGPZCX.getCode());

        nonConfirmCollectionSecondDetail.setId(detailId);
        nonConfirmCollectionSecondDetail.setSumId(writeOffConfirmDTO.getId());
        nonConfirmCollectionSecondDetail.setSystemCode(SystemEnum.CWZT.getCode());
        nonConfirmCollectionSecondDetail.setSystemName(SystemEnum.CWZT.getDesc());
        nonConfirmCollectionSecondDetail.setBusinessDate(nonConfirmCollectionSumEntity.getBusinessDate());
        nonConfirmCollectionSecondDetail.setBusinessHappenDate(businessDate);
        nonConfirmCollectionSecondDetail.setCurrencyType(nonConfirmCollectionSumEntity.getCurrencyType());
        nonConfirmCollectionSecondDetail.setBankAmount(nonConfirmCollectionSumEntity.getBankAmount());
        nonConfirmCollectionSecondDetail.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
        nonConfirmCollectionSecondDetail.setManualVoucherIds(manualIds);
        nonConfirmCollectionSecondDetail.setApproveId(approveId);
        nonConfirmCollectionSecondDetail.setOperationType(ClaimOperationTypeEnum.MANUAL_CANCEL_AMOUNT.getCode());
        nonConfirmCollectionSecondDetail.setWriteOffVoucherId(writeOffVoucherIds);
        BigDecimal curClaimAmount = writeOffDetailList.stream().map(WriteOffDetailDTO::getWriteOffAmount).
                reduce(BigDecimal.ZERO, BigDecimal::add);
        nonConfirmCollectionSecondDetail.setCurClaimAmount(curClaimAmount);
        nonConfirmCollectionSecondDetail.setIsRelateClientAuxiliaryAccount(writeOffConfirmDTO.getIsRelateClientAuxiliaryAccount());
        nonConfirmCollectionSecondDetailService.save(nonConfirmCollectionSecondDetail);


        // 到账主体和认领主体是否一致判断
        boolean isSameForAccountBankAndConfirmOrg = false;

        // 校验到账主体和认领主体是否一致
        for (WriteOffDetailDTO dto : writeOffConfirmDTO.getWriteOffDetailList()) {
            if (!StringUtils.equals(dto.getCollectionAccountsBankCode(), dto.getOrgId())) {
                isSameForAccountBankAndConfirmOrg = true;
                break;
            }
        }

        // 保存冲销记录
        List<BusinessClaimRepaymentRecordEntity> claimRecordList = new ArrayList<>();
        String contractCode = writeOffConfirmDTO.getWriteOffConfirmVoucherList().stream().
                filter(e->StringUtils.isNotEmpty(e.getContractCode())).findFirst().orElse(null).getContractCode();
        for (WriteOffDetailDTO dto : writeOffDetailList) {
            BusinessClaimRepaymentRecordEntity entity = new BusinessClaimRepaymentRecordEntity();
            entity.setId(IdWorker.getId());
            entity.setSystemCode(SystemEnum.CWZT.getCode());
            entity.setClaimAmount(dto.getWriteOffAmount());
            entity.setClientCode(nonConfirmCollectionSumEntity.getClientCode());
            entity.setContractCode(contractCode);
            entity.setCurrencyType(nonConfirmCollectionSumEntity.getCurrencyType());
            entity.setBusinessDate(businessDate);
            entity.setEbankSerialNumber(dto.getEbankSerialNumber());
            entity.setOrgId(dto.getOrgId());
            entity.setOrgName(dto.getOrgName());
            entity.setBatchNo(new BigDecimal(maxBatchNo));
            entity.setSceneCode(SceneEnum.SGPZCX.getCode());
            entity.setSceneName(SceneEnum.SGPZCX.getDesc());
            entity.setOperationType(ClaimOperationTypeEnum.MANUAL_CANCEL_AMOUNT.getCode());
            entity.setNonConfirmSecondDetailId(detailId);
            entity.setRemark(dto.getRemark());
            if (isSameForAccountBankAndConfirmOrg) {
                entity.setIsCrossOrg(YesOrNoEnum.YES.getCode());
            } else {
                entity.setIsCrossOrg(YesOrNoEnum.NO.getCode());
            }
            claimRecordList.add(entity);
        }
        businessClaimRepaymentRecordService.saveBatch(claimRecordList);

        // 保存填入的凭证信息
        this.saveVoucherRecordList(writeOffConfirmDTO.getWriteOffConfirmVoucherList(), detailId);
        return R.ok();
    }


    /**
     * 冲销确认 校验
     */
    private String writeOffConfirmVlidation(WriteOffConfirmDTO writeOffConfirmDTO, NonConfirmCollectionSumEntity nonConfirmCollectionSumEntity) {
        if (writeOffConfirmDTO.getWriteOffConfirmVoucherList() == null
                || writeOffConfirmDTO.getWriteOffConfirmVoucherList().isEmpty()) {
            return "冲销凭证明细不能为空";
        }

        if (writeOffConfirmDTO.getWriteOffDetailList() == null
                || writeOffConfirmDTO.getWriteOffDetailList().isEmpty()) {
            return "冲销数据明细不能为空";
        }

        // 合同编码唯一，一次只能填写一个合同编码
        List<String> contractCodeList = Optional.ofNullable(writeOffConfirmDTO.getWriteOffConfirmVoucherList().stream().
                filter(e->StringUtils.isNotEmpty(e.getContractCode()))).orElse(null)
                .map(ClaimConfirmVoucherDTO::getContractCode).distinct().collect(Collectors.toList());
        if (contractCodeList != null && contractCodeList.size() != 1) {
            return "合同编码不是唯一值(一个凭证只能填写唯一合同号)!";
        }

        // 未确认收款科目必须存在校验
        List<ClaimConfirmVoucherDTO> nonConfirmAmountAccountList = writeOffConfirmDTO.getWriteOffConfirmVoucherList().stream().filter(
                e-> Constants.RECEIVABLE_UNCONFIRM_RECEIPT.equals(e.getAccountNumber())).collect(Collectors.toList());
        if (nonConfirmAmountAccountList == null || nonConfirmAmountAccountList.isEmpty()) {
            return "未确认收款科目不存在!";
        }

        // 当前认领金额
        BigDecimal claimAmountSum = writeOffConfirmDTO.getWriteOffDetailList().stream().filter(
                e->e.getWriteOffAmount() != null).map(WriteOffDetailDTO::getWriteOffAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        // 已经认领金额汇总
        List<BusinessClaimRepaymentRecordEntity> businessClaimRepaymentRecordEntityList = businessClaimRepaymentRecordService.
                selectClaimRecordByDeductBatchNo(nonConfirmCollectionSumEntity.getEbankSerialNumber(), null, null, null);
        BigDecimal readyClaimAmount = businessClaimRepaymentRecordService.getReadyClaimAmount(
                businessClaimRepaymentRecordEntityList);
        // 校验：已经认领金额+当前认领金额<=到账金额
        if (claimAmountSum.add(readyClaimAmount).compareTo(nonConfirmCollectionSumEntity.getBankAmount()) > 0) {
            return "(已经认领金额+当前认领金额)大于到账金额!";
        }

        // 到账金额-已经认领金额-当前认领金额=剩余未确认金额（应该小于到账金额）
        BigDecimal remainNonClaimAmount = nonConfirmCollectionSumEntity.getBankAmount().subtract(readyClaimAmount).subtract(claimAmountSum);
        if (remainNonClaimAmount.compareTo(nonConfirmCollectionSumEntity.getBankAmount()) > 0) {
            return "剩余未确认金额大于到账金额!";
        }

        // 贷方是否有未确认收款科目，如果有，则用借方未确认收款科目-贷方未确认收款科目=认领金额, 如无，则直接判断未确认收款金额和认领金额是否一致
        List<ClaimConfirmVoucherDTO> nonConfirmAmountCRAccountList = nonConfirmAmountAccountList.stream().filter(
                e-> DRCREnum.CR.getCode().equals(e.getCrOrDt())).collect(Collectors.toList());
        if (nonConfirmAmountCRAccountList == null || nonConfirmAmountCRAccountList.isEmpty()) {

            // 校验未确认收款科目金额是否和弹窗合计认领金额一致，不一致则校验不通过，无法提交
            BigDecimal nonConfirmAmountSum = writeOffConfirmDTO.getWriteOffConfirmVoucherList().stream().filter(
                            e-> Constants.RECEIVABLE_UNCONFIRM_RECEIPT.equals(e.getAccountNumber())).
                    map(ClaimConfirmVoucherDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

            if (nonConfirmAmountSum.compareTo(claimAmountSum) != 0) {
                return "未确认收款科目总金额和冲销总金额不一致!";
            }
        } else {

            // 贷方是否有未确认收款科目，如果有，则用借方未确认收款科目-贷方未确认收款科目=认领金额
            BigDecimal crAmount = nonConfirmAmountCRAccountList.stream().map(ClaimConfirmVoucherDTO::getAmount).
                    reduce(BigDecimal::add).get();

            // 未确认收款借方金额
            BigDecimal drAmount = nonConfirmAmountAccountList.stream().filter(e-> DRCREnum.DR.getCode().equals(e.getCrOrDt())).
                    map(ClaimConfirmVoucherDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (drAmount.subtract(crAmount).compareTo(claimAmountSum) != 0) {
                return "未确认收款科目总金额(借方金额-贷方金额)和冲销总金额不一致!";
            }
        }


        // 校验科目信息中借贷双方金额合计是否一致，不一致则校验不通过，无法提交
        BigDecimal crSum = writeOffConfirmDTO.getWriteOffConfirmVoucherList().stream().filter(
                        e-> DRCREnum.CR.getCode().equals(e.getCrOrDt())).
                map(ClaimConfirmVoucherDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal dtSum = writeOffConfirmDTO.getWriteOffConfirmVoucherList().stream().filter(
                        e-> DRCREnum.DR.getCode().equals(e.getCrOrDt())).
                map(ClaimConfirmVoucherDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (crSum.compareTo(dtSum) != 0) {
            return "科目信息中借贷双方金额合计不一致!";
        }

        // 校验所选择科目必须是在ZLYW下的科目
        for (ClaimConfirmVoucherDTO dto : writeOffConfirmDTO.getWriteOffConfirmVoucherList()) {
            List<AccountEntity> accountEntityList = iAccountService.lambdaQuery().eq(AccountEntity::getAccountCode,
                    dto.getAccountNumber()).eq(AccountEntity::getBusinessCode, BusinessEnum.ZLYW.getCode()).list();
            if (accountEntityList == null || accountEntityList.isEmpty()) {
                return "租赁业务下不存在科目编码:".concat(dto.getAccountNumber()).concat("!");
            }
        }

        // 到账主体和认领主体是否一致判断
        boolean isSameForAccountBankAndConfirmOrg = false;

        // 校验到账主体和认领主体是否一致
        for (WriteOffDetailDTO dto : writeOffConfirmDTO.getWriteOffDetailList()) {
            if (!StringUtils.equals(dto.getCollectionAccountsBankCode(), dto.getOrgId())) {
                isSameForAccountBankAndConfirmOrg = true;
                break;
            }
        }

        // 查询合同信息
        ContractDTO contractDTO = this.queryContractDTO(writeOffConfirmDTO.getWriteOffConfirmVoucherList());

        // 取得科目列表
        List<String> accountNumberList = writeOffConfirmDTO.getWriteOffConfirmVoucherList().stream().
                map(ClaimConfirmVoucherDTO::getAccountNumber).collect(Collectors.toList());

        for (int i = 0; i < writeOffConfirmDTO.getWriteOffDetailList().size(); i++) {
            WriteOffDetailDTO writeOffDetailDTO = writeOffConfirmDTO.getWriteOffDetailList().get(i);

            // 校验冲销金额是否超过对应业务系统批扣流水号下网银到账总金额，超过则校验不通过，无法提交；
            if (writeOffDetailDTO.getBankAmount().compareTo(writeOffDetailDTO.getWriteOffAmount()) < 0) {
                return "批扣号:".concat(writeOffDetailDTO.getEbankSerialNumber()).concat("冲销金额大于到账金额,请修改!");
            }
        }

        // 到账主体和认领主体不一致
        if (contractDTO != null && isSameForAccountBankAndConfirmOrg) {
            if (FinancialContractStatusEnum.ASSET_DISPOSAL_INNER_TRANSFER.getDesc().equals(
                    contractDTO.getFinancialContractStatus())) {

                // 校验若到账主体和认领主体不一致，当合同财务状态为“资产处置结束（内部转让）”时，是否已录入关联往来科目信息（2241.17
                // 其他应付款_代收转让款项+1221.15 其他应收款款_应收转让后收款），若未录入，则校验不通过，无法提交
                if (!accountNumberList.contains(Constants.OTHER_PAYABLES_PROXY_MAKE_OVER_ACCOUNT)
                        || !accountNumberList.contains(Constants.OHTER_RECEIVABLE_TRANSFER_ACCOUNT)) {
                    return "未关联往来科目信息（2241.17 其他应付款_代收转让款项+1221.15 其他应收款款_应收转让后收款）";
                }
            } else {

                // 校验若到账主体和认领主体不一致，当合同财务状态不为“资产处置结束（内部转让）”时，是否已录入关联往来科目信息
                // （2241.02 其他应付款_关联公司往来+1221.01 其他应收款款_关联公司往来），若未录入，则校验不通过，无法提交
                if (!accountNumberList.contains(Constants.OTHER_PAYABLES_CONNECT_COMPANY_ACCOUNT)
                        || !accountNumberList.contains(Constants.OTHER_RECEIVABLE_CONNECT_COMPANY_ACCOUNT)) {
                    return "未关联往来科目信息（2241.02 其他应付款_关联公司往来+1221.01 其他应收款款_关联公司往来）";
                }
            }
        }
        return "";
    }


    /**
     * 修改网银编号查询
     */
    public R<ModifyEbankNoQueryDTO> modifyEbankNoQuery(ModifyEbankNoQueryDTO params) {

        SelectClaimRecordForModifyEbankDTO scrfmeDto = new SelectClaimRecordForModifyEbankDTO();
        scrfmeDto.setSumId(Long.valueOf(params.getSumId()));
        List<BusinessClaimRepaymentRecordDTO> businessClaimRepaymentRecordDTOList = nonConfirmCollectionSumMapper.
                selectClaimRecordForModifyEbank(scrfmeDto);
        params.setClaimRecordOldList(businessClaimRepaymentRecordDTOList);
        if (businessClaimRepaymentRecordDTOList == null || businessClaimRepaymentRecordDTOList.isEmpty()) {
            return R.ok(params);
        }

        // 新认领记录行
        Map<String, List<BusinessClaimRepaymentRecordDTO>> bcrrMap = businessClaimRepaymentRecordDTOList.stream().collect(
                Collectors.groupingBy(BusinessClaimRepaymentRecordDTO::getEbankSerialNumber));
        for (String key : bcrrMap.keySet()) {
            List<BusinessClaimRepaymentRecordDTO> dtoList = bcrrMap.get(key);
            BusinessClaimRepaymentRecordDTO dto = new BusinessClaimRepaymentRecordDTO();
            dto.setEbankSerialNumber(key);
            if (dtoList != null && !dtoList.isEmpty()) {
                dto.setContractCode(Optional.ofNullable(dtoList.stream().filter(e->StringUtils.isNotEmpty(e.getContractCode()))).
                        orElse(null).map(BusinessClaimRepaymentRecordDTO::getContractCode).distinct().collect(Collectors.joining(",")));
                dto.setContractCodeList(Optional.ofNullable(dtoList.stream().filter(e->StringUtils.isNotEmpty(e.getContractCode()))).
                        orElse(null).map(BusinessClaimRepaymentRecordDTO::getContractCode).distinct().collect(Collectors.toList()));
                dto.setOrgId(Optional.ofNullable(dtoList.stream().filter(e->StringUtils.isNotEmpty(e.getOrgId()))).
                        orElse(null).map(BusinessClaimRepaymentRecordDTO::getOrgId).distinct().collect(Collectors.joining(",")));
                dto.setOrgName(Optional.ofNullable(dtoList.stream().filter(e->StringUtils.isNotEmpty(e.getOrgName()))).
                        orElse(null).map(BusinessClaimRepaymentRecordDTO::getOrgName).distinct().collect(Collectors.joining(",")));
                dto.setClaimAmount(dtoList.stream().map(BusinessClaimRepaymentRecordDTO::getClaimAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                dto.setAdjustAmount(new BigDecimal(0));
            }
            params.getClaimRecordNewList().add(dto);
        }
        return R.ok(params);
    }

    /**
     * 修改网银编号确认
     */
    public R<String> modifyEbankNoConfirm(ModifyEbankNoConfirmDTO params) {
        String errsMsg = modifyEbankNoConfirmVlidation(params);
        if (StringUtils.isNotEmpty(errsMsg)) {
            return R.fail(errsMsg);
        }

        // 过滤没有调整的批扣号
        List<ModifyEbankNoConfirmRecordDTO> claimRecordNewList = params.getClaimRecordNewList().stream().filter(e->
                StringUtils.isNotEmpty(e.getNewEbankSerialNumber()) && e.getAdjustAmount() != null
                        && new BigDecimal(0).compareTo(e.getAdjustAmount()) != 0
        ).collect(Collectors.toList());

        // 未确认认领记录
        NonConfirmCollectionSumEntity nonConfirmCollectionSumEntity = nonConfirmCollectionSumMapper.
                selectById(params.getSumId());

        // 取得最大批次号
        int maxBatchNo = businessClaimRepaymentRecordService.getMaxBatchNo(nonConfirmCollectionSumEntity.getEbankSerialNumber());
        maxBatchNo = maxBatchNo +  1;

        // 记账日期
        Instant instant = params.getAccountDate().toInstant();
        LocalDateTime businessDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        // 明细记录id
        Long detailId = IdWorker.getId();
        // 提交审核
        Long approveId = this.submitVoucher(detailId, BatchTypeEnum.SGPZTZ.getCode());

        // 保存修改记录
        List<BusinessClaimRepaymentRecordEntity> claimRecordList = new ArrayList<>();
        Map<Long, Long> sumDetailMap = new HashMap<>();
        sumDetailMap.put(nonConfirmCollectionSumEntity.getId(), detailId);
        for (ModifyEbankNoConfirmRecordDTO dto : claimRecordNewList) {

            // 取得合同信息
            List<BusinessClaimRepaymentRecordEntity> recordEntityList = businessClaimRepaymentRecordService.
                    selectClaimRecordByDeductBatchNo(dto.getEbankSerialNumber(), ProcessStatusEnum.getValidCode(),
                            dto.getContractCode(), ClaimOperationTypeEnum.getValidCode());
            if (recordEntityList == null || recordEntityList.isEmpty()) {
                throw new ServiceException("原批扣流水号：" + dto.getEbankSerialNumber() + "未找到相应的认领记录!");
            }

            // 校验认领记录金额大于调整金额
            BigDecimal claimAmountSum = recordEntityList.stream().map(BusinessClaimRepaymentRecordEntity::getClaimAmount).
                    reduce(BigDecimal.ZERO, BigDecimal::add);
            if (claimAmountSum.compareTo(dto.getAdjustAmount()) < 0) {
                throw new ServiceException("原批扣流水号：" + dto.getEbankSerialNumber() + "调整金额大于已认领金额的总和!");
            }

            // 存储待调整的认领记录
            List<BusinessClaimRepaymentRecordEntity> recordEntityListForAdjustment = new ArrayList<>();
            // 取得与调整金额一致的记录->不存在的情况则取比调整金额大的记录->最后取多条记录进行调整
            recordEntityListForAdjustment = recordEntityList.stream().filter(e->e.getClaimAmount().compareTo(
                    dto.getAdjustAmount()) == 0).collect(Collectors.toList());
            if (recordEntityListForAdjustment == null || recordEntityListForAdjustment.isEmpty()) {
                recordEntityListForAdjustment = recordEntityList.stream().filter(e->e.getClaimAmount().compareTo(
                        dto.getAdjustAmount()) > 0).collect(Collectors.toList());

                // 取多条记录进行调整
                if (recordEntityListForAdjustment == null || recordEntityListForAdjustment.isEmpty()) {
                    claimAmountSum = new BigDecimal(0);
                    for (BusinessClaimRepaymentRecordEntity recordEntity : recordEntityList) {
                        if (claimAmountSum.compareTo(dto.getAdjustAmount()) >= 0) {
                            break;
                        } else {
                            claimAmountSum = claimAmountSum.add(recordEntity.getClaimAmount());
                            recordEntityListForAdjustment.add(recordEntity);
                        }
                    }
                }
            }

//            ContractDTO contractDTO = null;
            // 待调整金额
//            BigDecimal adjustmentAmount = dto.getAdjustAmount();
            // 开始进行调整
//            for (int i = 0; i < recordEntityListForAdjustment.size(); i++) {
//                BusinessClaimRepaymentRecordEntity recordEntity = recordEntityListForAdjustment.get(i);
//
//                if (adjustmentAmount.compareTo(new BigDecimal(0)) == 0) {
//                    break;
//                }
//
//                // 本次循环调整金额
//                BigDecimal curAdjustmentAmount = new BigDecimal(0);
//                if (adjustmentAmount.compareTo(recordEntity.getClaimAmount()) <= 0) {
//                    curAdjustmentAmount = adjustmentAmount;
//                    adjustmentAmount = new BigDecimal(0);
//                } else if (adjustmentAmount.compareTo(recordEntity.getClaimAmount()) > 0) {
//                    curAdjustmentAmount = recordEntity.getClaimAmount();
//                    adjustmentAmount = adjustmentAmount.subtract(recordEntity.getClaimAmount());
//                }
//
//                // 查询手工凭证
//                NonConfirmCollectionSecondDetailEntity secondDetailEntity = nonConfirmCollectionSecondDetailService.
//                        getById(recordEntity.getNonConfirmSecondDetailId());

                // 冲销原批扣流水号的手工凭证
//                Long writeOffVoucherId = this.writeOffManualVoucherForAdjustment(secondDetailEntity.getManualVoucherIds(),
//                        businessDate, dto.getEbankSerialNumber(), curAdjustmentAmount);
//                manualIdList.add(String.valueOf(writeOffVoucherId));

                // 生成手工凭证
//                String voucherIds = this.genManualVoucherForAdjustment(recordEntity, dto.getAdjustAmount(),
//                        params.getAccountDate(), nonConfirmCollectionSumEntity, detailId);
//                manualIdList.add(voucherIds);
//            }

            // 调出
            BusinessClaimRepaymentRecordEntity entityOut = new BusinessClaimRepaymentRecordEntity();
            entityOut.setId(IdWorker.getId());
            entityOut.setSystemCode(SystemEnum.CWZT.getCode());
            entityOut.setClaimAmount(dto.getAdjustAmount().multiply(new BigDecimal(-1)));
            entityOut.setClientCode(nonConfirmCollectionSumEntity.getClientCode());
            entityOut.setContractCode(dto.getContractCode());
            entityOut.setCurrencyType(nonConfirmCollectionSumEntity.getCurrencyType());
            entityOut.setBusinessDate(businessDate);
            entityOut.setEbankSerialNumber(dto.getEbankSerialNumber());
            entityOut.setNewEbankSerialNumber(dto.getNewEbankSerialNumber());
            entityOut.setOrgId(recordEntityListForAdjustment.get(0).getOrgId());
            entityOut.setOrgName(recordEntityListForAdjustment.get(0).getOrgName());
            entityOut.setSceneCode(SceneEnum.SGPZTZ.getCode());
            entityOut.setSceneName(SceneEnum.SGPZTZ.getDesc());
            entityOut.setBatchNo(new BigDecimal(maxBatchNo));
            entityOut.setOperationType(ClaimOperationTypeEnum.MODIFY_EBANK_NO.getCode());
            entityOut.setNonConfirmSecondDetailId(detailId);
            claimRecordList.add(entityOut);

            // 调入的批扣流水号对账记录查询
            NonConfirmCollectionSumEntity entityParam = new NonConfirmCollectionSumEntity();
            entityParam.setEbankSerialNumber(dto.getNewEbankSerialNumber());
            List<NonConfirmCollectionSumEntity> nonConfirmCollectionSumEntityList = this.getNonConfirmCollectionSumDTOByCon(entityParam);
            if (nonConfirmCollectionSumEntityList.size() > 1) {
                throw new ServiceException(String.format("批扣流水号:%s存在多条汇总记录!", dto.getNewEbankSerialNumber()));
            }
            NonConfirmCollectionSumEntity newNonConfirmCollectionSumEntity = nonConfirmCollectionSumEntityList.get(0);
            Long newDetailId = IdWorker.getId();   // 调入批扣流水号的操作记录id
            if (!sumDetailMap.keySet().contains(newNonConfirmCollectionSumEntity.getId())) {
                // 创建操作记录-为新的批扣流水号
                NonConfirmCollectionSecondDetailEntity nonConfirmCollectionSecondDetail = new NonConfirmCollectionSecondDetailEntity();
                nonConfirmCollectionSecondDetail.setId(newDetailId);
                nonConfirmCollectionSecondDetail.setSumId(newNonConfirmCollectionSumEntity.getId());
                nonConfirmCollectionSecondDetail.setSystemCode(SystemEnum.CWZT.getCode());
                nonConfirmCollectionSecondDetail.setSystemName(SystemEnum.CWZT.getDesc());
                nonConfirmCollectionSecondDetail.setBusinessDate(newNonConfirmCollectionSumEntity.getBusinessDate());
                nonConfirmCollectionSecondDetail.setBusinessHappenDate(businessDate);
                nonConfirmCollectionSecondDetail.setCurrencyType(newNonConfirmCollectionSumEntity.getCurrencyType());
                nonConfirmCollectionSecondDetail.setBankAmount(newNonConfirmCollectionSumEntity.getBankAmount());
                nonConfirmCollectionSecondDetail.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
                nonConfirmCollectionSecondDetail.setApproveId(approveId);
                nonConfirmCollectionSecondDetail.setOperationType(ClaimOperationTypeEnum.MODIFY_EBANK_NO.getCode());
                nonConfirmCollectionSecondDetailService.save(nonConfirmCollectionSecondDetail);

                sumDetailMap.put(newNonConfirmCollectionSumEntity.getId(), newDetailId);
            }  else {
                newDetailId = sumDetailMap.get(newNonConfirmCollectionSumEntity.getId());
            }

            // 调入
            BusinessClaimRepaymentRecordEntity entityIn = new BusinessClaimRepaymentRecordEntity();
            entityIn.setId(IdWorker.getId());
            entityIn.setSystemCode(SystemEnum.CWZT.getCode());
            entityIn.setClaimAmount(dto.getAdjustAmount());
            entityIn.setClientCode(nonConfirmCollectionSumEntity.getClientCode());
            entityIn.setContractCode(dto.getContractCode());
            entityIn.setCurrencyType(nonConfirmCollectionSumEntity.getCurrencyType());
            entityIn.setBusinessDate(businessDate);
            entityIn.setEbankSerialNumber(dto.getNewEbankSerialNumber());
            entityIn.setNewEbankSerialNumber(dto.getEbankSerialNumber());
            entityIn.setOrgId(recordEntityListForAdjustment.get(0).getOrgId());
            entityIn.setOrgName(recordEntityListForAdjustment.get(0).getOrgName());
            entityIn.setBatchNo(new BigDecimal(maxBatchNo));
            entityIn.setOperationType(ClaimOperationTypeEnum.MODIFY_EBANK_NO.getCode());
            entityIn.setNonConfirmSecondDetailId(newDetailId);
            entityIn.setSceneCode(SceneEnum.SGPZTZ.getCode());
            entityIn.setSceneName(SceneEnum.SGPZTZ.getDesc());
            claimRecordList.add(entityIn);
        }
        businessClaimRepaymentRecordService.saveBatch(claimRecordList);

        // 调整记录保存并提交
        NonConfirmCollectionSecondDetailEntity nonConfirmCollectionSecondDetail = new NonConfirmCollectionSecondDetailEntity();
        nonConfirmCollectionSecondDetail.setId(detailId);
        nonConfirmCollectionSecondDetail.setSumId(params.getSumId());
        nonConfirmCollectionSecondDetail.setSystemCode(SystemEnum.CWZT.getCode());
        nonConfirmCollectionSecondDetail.setSystemName(SystemEnum.CWZT.getDesc());
        nonConfirmCollectionSecondDetail.setBusinessDate(nonConfirmCollectionSumEntity.getBusinessDate());
        nonConfirmCollectionSecondDetail.setBusinessHappenDate(businessDate);
        nonConfirmCollectionSecondDetail.setCurrencyType(nonConfirmCollectionSumEntity.getCurrencyType());
        nonConfirmCollectionSecondDetail.setBankAmount(nonConfirmCollectionSumEntity.getBankAmount());
        nonConfirmCollectionSecondDetail.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
//        nonConfirmCollectionSecondDetail.setManualVoucherIds(manualIdList.stream().collect(Collectors.joining(",")));
        nonConfirmCollectionSecondDetail.setApproveId(approveId);
        nonConfirmCollectionSecondDetail.setOperationType(ClaimOperationTypeEnum.MODIFY_EBANK_NO.getCode());
        nonConfirmCollectionSecondDetailService.save(nonConfirmCollectionSecondDetail);
        return R.ok();
    }


    /**
     * 红冲手工凭证-修改网银编号
     */
    private Long writeOffManualVoucherForAdjustment(String manualId, LocalDateTime businessDate,
                                                    String oldEbankSerialNumber, BigDecimal adjustmentAmount) {
        ManualEntity manualEntity = manualService.getById(manualId);
        if (manualEntity == null) {
            throw new ServiceException("原批扣流水号：" + oldEbankSerialNumber + "未找到手工凭证!");
        }

        // 分录
        List<ManualVoucherVO> manualVoucherList = manualService.getManualVoucherById(manualEntity.getId());
        // 红冲之前的旧凭证
        ManualEntity manualOldEntity = BeanUtil.copyProperties(manualEntity, ManualEntity.class);
        Long oldManualId = IdWorker.getId();
        manualOldEntity.setId(oldManualId);
        manualOldEntity.setCreateBy(SecurityUtils.getUsername());
        manualOldEntity.setCreateTime(LocalDateTime.now());
        manualOldEntity.setUpdateBy(SecurityUtils.getUsername());
        manualOldEntity.setUpdateTime(LocalDateTime.now());
        manualOldEntity.setPeriodCode(Integer.parseInt(DateUtils.dateTimeNow("yyyyMM")));
        manualOldEntity.setBusinessDate(businessDate);
        manualOldEntity.setVoucherDate(businessDate);
        manualOldEntity.setVoucherSummary("修改网银编号-红冲之前的凭证，手工凭证id：".concat(String.valueOf(manualEntity.getId())));
        manualOldEntity.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
        Long voucherNumber = manualService.generateVoucherNum(manualEntity.getVoucherType(), LocalDateTime.now());
        manualOldEntity.setVoucherNum(voucherNumber);
        manualOldEntity.setSceneCode(SceneEnum.SGPZTZ.getCode());
        manualOldEntity.setSceneName(SceneEnum.SGPZTZ.getDesc());
        manualOldEntity.setIsWriteOff(YesOrNoEnum.YES.getCode());
        manualService.save(manualOldEntity);

        // 红冲分录
        List<ManualVoucherEntity> oldManualVoucherEntityList = new ArrayList<>();
        BigDecimal debitAmount = adjustmentAmount;
        BigDecimal creditAmount = adjustmentAmount;
        if (manualVoucherList != null && !manualVoucherList.isEmpty()) {
            for (ManualVoucherVO manualVoucherVO : manualVoucherList) {
                ManualVoucherEntity newManualVoucherEntity = BeanUtil.copyProperties(manualVoucherVO, ManualVoucherEntity.class);
                newManualVoucherEntity.setId(IdWorker.getId());
                newManualVoucherEntity.setCreateBy(SecurityUtils.getUsername());
                newManualVoucherEntity.setCreateTime(LocalDateTime.now());
                newManualVoucherEntity.setUpdateBy(SecurityUtils.getUsername());
                newManualVoucherEntity.setUpdateTime(LocalDateTime.now());
                newManualVoucherEntity.setPeriodCode(Integer.parseInt(DateUtils.dateTimeNow("yyyyMM")));
                newManualVoucherEntity.setBusinessDate(businessDate);
                newManualVoucherEntity.setVoucherDate(businessDate);
                newManualVoucherEntity.setSceneCode(SceneEnum.SGPZTZ.getCode());
                if (manualVoucherVO.getDebitAmount() != null && manualVoucherVO.getDebitAmount().compareTo(new BigDecimal(0)) != 0) {
                    if (debitAmount.compareTo(new BigDecimal(0)) == 0) {
                        continue;
                    }
                    if (manualVoucherVO.getDebitAmount().compareTo(debitAmount) >= 0) {
                        newManualVoucherEntity.setDebitAmount(debitAmount.multiply(new BigDecimal(-1)));
                        debitAmount = new BigDecimal(0);
                    } else if (manualVoucherVO.getDebitAmount().compareTo(debitAmount) < 0) {
                        newManualVoucherEntity.setDebitAmount(manualVoucherVO.getDebitAmount().multiply(new BigDecimal(-1)));
                        debitAmount = debitAmount.subtract(manualVoucherVO.getDebitAmount());
                    }
                }

                if (manualVoucherVO.getCreditAmount() != null && manualVoucherVO.getCreditAmount().compareTo(new BigDecimal(0)) != 0) {
                    if (creditAmount.compareTo(new BigDecimal(0)) == 0) {
                        continue;
                    }
                    if (manualVoucherVO.getCreditAmount().compareTo(creditAmount) >= 0) {
                        newManualVoucherEntity.setCreditAmount(creditAmount.multiply(new BigDecimal(-1)));
                        creditAmount = new BigDecimal(0);
                    } else if (manualVoucherVO.getCreditAmount().compareTo(creditAmount) < 0) {
                        newManualVoucherEntity.setCreditAmount(manualVoucherVO.getCreditAmount().multiply(new BigDecimal(-1)));
                        creditAmount = creditAmount.subtract(manualVoucherVO.getCreditAmount());
                    }
                }
                newManualVoucherEntity.setVoucherSummary("修改网银编号-红冲之前的凭证，手工凭证分录id：".concat(String.valueOf(manualVoucherVO.getId())));
                newManualVoucherEntity.setManualId(oldManualId);
                oldManualVoucherEntityList.add(newManualVoucherEntity);
            }
        }
        manualVoucherService.saveBatch(oldManualVoucherEntityList);
        return oldManualId;
    }

    /**
     * 生成手工凭证-修改网银编号
     */
    private String genManualVoucherForAdjustment(BusinessClaimRepaymentRecordEntity recordEntity, BigDecimal curAdjustmentAmount,
                                 Date businessDate, NonConfirmCollectionSumEntity nonConfirmCollectionSumEntity, Long detailId) {

        // 取得凭证信息
        List<NonConfirmCollectionVoucherRecordEntity> nonConfirmCollectionVoucherRecordEntityList =
                nonConfirmCollectionVoucherRecordService.selectByDetailId(String.valueOf(recordEntity.getNonConfirmSecondDetailId()));

        List<ClaimConfirmVoucherDTO> claimConfirmVoucherDTOList = new ArrayList<>();
        // 借方分录
        ClaimConfirmVoucherDTO crRecord = new ClaimConfirmVoucherDTO();
        crRecord.setCreateConfirmOrgId(nonConfirmCollectionVoucherRecordEntityList.get(0).getOrgId());
        crRecord.setCreateConfirmOrgName(nonConfirmCollectionVoucherRecordEntityList.get(0).getOrgName());
        crRecord.setCrOrDt(DRCREnum.DR.getCode());
        crRecord.setAccountNumber(Constants.RECEIVABLE_UNCONFIRM_RECEIPT);
        crRecord.setAccountName(Constants.RECEIVABLE_UNCONFIRM_RECEIPT_NAME);
        crRecord.setVoucherComments("修改网银编号");
        crRecord.setAmount(curAdjustmentAmount);
        crRecord.setContractCode(nonConfirmCollectionVoucherRecordEntityList.get(0).getContractCode());
        crRecord.setClientCode(nonConfirmCollectionVoucherRecordEntityList.get(0).getClientCode());
        claimConfirmVoucherDTOList.add(crRecord);
        // 贷方分录
        ClaimConfirmVoucherDTO dtRecord = new ClaimConfirmVoucherDTO();
        dtRecord.setCreateConfirmOrgId(nonConfirmCollectionVoucherRecordEntityList.get(0).getOrgId());
        dtRecord.setCreateConfirmOrgName(nonConfirmCollectionVoucherRecordEntityList.get(0).getOrgName());
        dtRecord.setCrOrDt(DRCREnum.CR.getCode());
        dtRecord.setAccountNumber(Constants.RECEIVABLE_UNCONFIRM_RECEIPT);
        dtRecord.setAccountName(Constants.RECEIVABLE_UNCONFIRM_RECEIPT_NAME);
        dtRecord.setVoucherComments("修改网银编号");
        dtRecord.setAmount(curAdjustmentAmount);
        dtRecord.setContractCode(nonConfirmCollectionVoucherRecordEntityList.get(0).getContractCode());
        dtRecord.setClientCode(nonConfirmCollectionVoucherRecordEntityList.get(0).getClientCode());
        claimConfirmVoucherDTOList.add(dtRecord);
        Long manualId = this.saveManualVoucher(claimConfirmVoucherDTOList, nonConfirmCollectionVoucherRecordEntityList.get(0).getOrgId(),
                nonConfirmCollectionSumEntity.getCurrencyType(), DateUtils.parseLocalDateTime(DateUtils.dateTime(businessDate)),
                ClaimOperationTypeEnum.MODIFY_EBANK_NO.getCode());

        this.saveVoucherRecordList(claimConfirmVoucherDTOList, detailId);
        return String.valueOf(manualId);
    }

    /**
     * 修改网银编号确认校验
     */
    private String modifyEbankNoConfirmVlidation(ModifyEbankNoConfirmDTO params) {
        if (params.getClaimRecordNewList() == null || params.getClaimRecordNewList().isEmpty()) {
            return "调整记录不能为空!";
        }

        // 过滤没有调整的批扣号
        List<ModifyEbankNoConfirmRecordDTO> claimRecordNewList = params.getClaimRecordNewList().stream().filter(e->
                StringUtils.isNotEmpty(e.getNewEbankSerialNumber()) && e.getAdjustAmount() != null
                        && new BigDecimal(0).compareTo(e.getAdjustAmount()) != 0
        ).collect(Collectors.toList());
        if (claimRecordNewList == null || claimRecordNewList.isEmpty()) {
            return "调整记录不能为空!";
        }

        // 调整金额必须小于已认领金额
        Map<String, NonConfirmCollectionSumEntity> newNonConfirmCollectionSumMap = new HashMap<>();
        for (int i = 0; i < claimRecordNewList.size(); i++) {
            ModifyEbankNoConfirmRecordDTO dto = claimRecordNewList.get(0);

            // 调整金额不能大于已经认领的金额
            if (dto.getClaimAmount().compareTo(dto.getAdjustAmount()) < 0) {
                return String.format("批扣流水号：%s对应的调整金额：%d不能大于认领金额：%d", dto.getEbankSerialNumber(),
                        dto.getAdjustAmount(), dto.getClaimAmount());
            }

            // 新业务系统的批扣流水号 是原业务系统批扣流水号之一
//            if (!ebankSerialNumberList.contains(dto.getNewEbankSerialNumber())) {
//                return String.format("新业务系统批扣流水号:%s应该是原业务系统的批扣流水号中的一个", dto.getNewEbankSerialNumber());
//            }

            // 新业务系统的批扣流水号 存在性验证
            LambdaQueryWrapper<NonConfirmCollectionSumEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(NonConfirmCollectionSumEntity::getEbankSerialNumber, dto.getNewEbankSerialNumber());
            wrapper.eq(NonConfirmCollectionSumEntity::getDelFlag, YesOrNoEnum.NO.getCode());
            List<NonConfirmCollectionSumEntity> sumEntityList = nonConfirmCollectionSumMapper.selectList(wrapper);
            if (sumEntityList == null || sumEntityList.isEmpty()) {
                return String.format("新业务系统批扣流水号:%s不存在!", dto.getNewEbankSerialNumber());
            } else {
                newNonConfirmCollectionSumMap.put(dto.getNewEbankSerialNumber(), sumEntityList.get(0));
            }
        }

        // 调入的新批扣流水号认领金额后， 应该小于到账金额校验
        Map<String, List<ModifyEbankNoConfirmRecordDTO>> newEbankSerialNumberGroupMap = claimRecordNewList.stream().
                collect(Collectors.groupingBy(e->e.getNewEbankSerialNumber()));
        for (String newEbankSerialNumber : newEbankSerialNumberGroupMap.keySet()) {
            // 当前认领金额
            BigDecimal claimAmountSum = newEbankSerialNumberGroupMap.get(newEbankSerialNumber).
                    stream().map(ModifyEbankNoConfirmRecordDTO::getAdjustAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

            NonConfirmCollectionSumEntity entity = newNonConfirmCollectionSumMap.get(newEbankSerialNumber);

            // 已经认领金额计算
            BigDecimal claimedAmountSum = BigDecimal.ZERO;
            List<BusinessClaimRepaymentRecordEntity> recordEntityList = businessClaimRepaymentRecordService.
                    selectClaimRecordByDeductBatchNo(newEbankSerialNumber, ProcessStatusEnum.getValidCode(),
                            null, null);
            if (recordEntityList != null && !recordEntityList.isEmpty()) {
                claimedAmountSum = recordEntityList.stream().map(BusinessClaimRepaymentRecordEntity::getClaimAmount).
                        reduce(BigDecimal.ZERO, BigDecimal::add);
            }

            // 0<=认领金额+已经认领金额 <= 到账金额
            BigDecimal claimedAmount = claimAmountSum.add(claimedAmountSum);
            if (claimedAmount.compareTo(BigDecimal.ZERO) < 0) {
                return "新批扣流水号:" + newEbankSerialNumber + "当前调入金额+已认领金额("+claimedAmountSum.toString()+")不能小于0";
            }
            if (claimedAmount.compareTo(entity.getBankAmount()) > 0) {
                return "新批扣流水号:" + newEbankSerialNumber + "当前调入金额+已认领金额("+claimedAmountSum.toString()+
                        ")不能大于到账金额("+entity.getBankAmount()+")";
            }
        }
        return StringUtil.EMPTY;
    }

    public void batchModifyIncomeDate(List<ModifyIncomeDateTemplateDownloadExcel> list) {
        if (list == null || list.isEmpty()) {
            return ;
        }

        for (ModifyIncomeDateTemplateDownloadExcel excel : list) {
            String validationResult = this.batchModifyIncomeDateVlidation(excel);
            if (StringUtils.isNotEmpty(validationResult)) {
                throw new ServiceException(validationResult);
            }

            // 查询待修改的数据
            SelectIncomeDateInfoInputDTO inputDTO = new SelectIncomeDateInfoInputDTO();
            if (StringUtils.isNotEmpty(excel.getBusinessEbankNo())) {
                inputDTO.setBusinessEbankNumber(excel.getBusinessEbankNo());
            }
            if (StringUtils.isNotEmpty(excel.getEbankSerialNumber())) {
                inputDTO.setEbankSerialNumber(excel.getEbankSerialNumber());
            }
            inputDTO.setIncomeDateString(DateUtil.format(excel.getIncomeDateOld(), "yyyy-MM"));
            List<SelectIncomeDateInfoOutputDTO> claimRecordList = businessClaimRepaymentRecordService.selectIncomeDateInfo(inputDTO);
            if (claimRecordList == null || claimRecordList.isEmpty()) {
                if (StringUtils.isNotEmpty(excel.getEbankSerialNumber())) {
                    throw new ServiceException(String.format("批扣流水号:%s未查询到相关认领记录，请确认!",
                            excel.getEbankSerialNumber()));
                }
                if (StringUtils.isNotEmpty(excel.getBusinessEbankNo())) {
                    throw new ServiceException(String.format("网银编号:%s未查询到相关认领记录，请确认!",
                            excel.getBusinessEbankNo()));
                }
            }

            // 网银确认日期
            Instant instant = excel.getIncomeDateAdjust().toInstant();
            for (SelectIncomeDateInfoOutputDTO dto : claimRecordList) {
                if (dto.getSumId() == null || dto.getSumId() == 0L) {
                    if (StringUtils.isNotEmpty(excel.getEbankSerialNumber())) {
                        throw new ServiceException(String.format("批扣流水号:%s未查询到资金映射关系，请确认!",
                                excel.getEbankSerialNumber()));
                    }
                    if (StringUtils.isNotEmpty(excel.getBusinessEbankNo())) {
                        throw new ServiceException(String.format("网银编号:%s未查询到资金映射关系，请确认!",
                                excel.getBusinessEbankNo()));
                    }
                }

                // 复制业务系统自动认领的凭证
                String systemVoucherIds = StringUtil.EMPTY;
                if (StringUtils.isNotEmpty(dto.getBusinessVoucherIds())) {
                    systemVoucherIds = this.createWriteOffVoucher(dto.getBusinessVoucherIds(),
                            DateUtil.toLocalDateTime(dto.getBusinessDate()),
                            LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
                }

                // 复制手工凭证
                String manualVoucherIds = StringUtil.EMPTY;
                if (StringUtils.isNotEmpty(dto.getManualVoucherIds())) {
                    manualVoucherIds = this.createManualVoucher(Long.parseLong(dto.getManualVoucherIds()),
                            DateUtil.toLocalDateTime(dto.getBusinessDate()),
                            LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
                }

                // 复制核销回款凭证
                String writeOffVoucherIds = StringUtil.EMPTY;
                if (StringUtils.isNotEmpty(dto.getWriteOffVoucherId())) {
                    writeOffVoucherIds = this.createWriteOffVoucher(dto.getWriteOffVoucherId(),
                            DateUtil.toLocalDateTime(dto.getBusinessDate()),
                            LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
                }

                // 复制批量认领凭证
                String batchClaimVoucherIds = StringUtil.EMPTY;
                if (StringUtils.isNotEmpty(dto.getVoucherIds())) {
                    batchClaimVoucherIds = this.createWriteOffVoucher(dto.getVoucherIds(),
                            DateUtil.toLocalDateTime(dto.getBusinessDate()),
                            LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
                }

                // 创建审核记录
                Long detailId = IdWorker.getId();
                Long approveId = this.submitVoucher(detailId, BatchTypeEnum.SGPZMGRZRQ.getCode());

                // 创建操作记录
                NonConfirmCollectionSecondDetailEntity secondDetailCopyEntity = new NonConfirmCollectionSecondDetailEntity();
                if (dto.getDetailId() == null) {
                    secondDetailCopyEntity.setId(detailId);
                    secondDetailCopyEntity.setSumId(dto.getSumId());
                    secondDetailCopyEntity.setSystemCode(SystemEnum.CWZT.getCode());
                    secondDetailCopyEntity.setSystemName(SystemEnum.CWZT.getDesc());
                    secondDetailCopyEntity.setBusinessHappenDate(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
                    secondDetailCopyEntity.setBusinessDate(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
                    secondDetailCopyEntity.setCurrencyType(dto.getCurrencyType());
                    secondDetailCopyEntity.setBankAmount(dto.getBankAmount());
                    secondDetailCopyEntity.setRemainNonConfirmAmount(NumberUtil.sub(dto.getBankAmount(),
                            dto.getClaimAmountTotal()));
                    secondDetailCopyEntity.setConfirmAmount(dto.getClaimAmountTotal());
                    secondDetailCopyEntity.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
                    secondDetailCopyEntity.setApproveId(approveId);
                    secondDetailCopyEntity.setManualVoucherIds(manualVoucherIds);
                    secondDetailCopyEntity.setCreateBy(SecurityUtils.getUsername());
                    secondDetailCopyEntity.setCreateTime(LocalDateTime.now());
                    secondDetailCopyEntity.setUpdateBy(SecurityUtils.getUsername());
                    secondDetailCopyEntity.setUpdateTime(LocalDateTime.now());
                    secondDetailCopyEntity.setWriteOffVoucherId(writeOffVoucherIds);
                    secondDetailCopyEntity.setOperationType(ClaimOperationTypeEnum.MODIFY_INCOME_DATE.getCode());
                    secondDetailCopyEntity.setCurClaimAmount(dto.getClaimAmount());
                    if (StringUtils.isNotEmpty(batchClaimVoucherIds)) {
                        secondDetailCopyEntity.setVoucherIds(batchClaimVoucherIds);
                    } else {
                        secondDetailCopyEntity.setVoucherIds(systemVoucherIds);
                    }
                } else {
                    secondDetailCopyEntity.setId(detailId);
                    secondDetailCopyEntity.setSumId(dto.getSumId());
                    secondDetailCopyEntity.setSystemCode(SystemEnum.CWZT.getCode());
                    secondDetailCopyEntity.setSystemName(SystemEnum.CWZT.getDesc());
                    secondDetailCopyEntity.setBusinessHappenDate(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
                    secondDetailCopyEntity.setBusinessDate(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
                    secondDetailCopyEntity.setCurrencyType(dto.getCurrencyType());
                    secondDetailCopyEntity.setBankAmount(dto.getBankAmount());
                    secondDetailCopyEntity.setRemainNonConfirmAmount(dto.getRemainNonConfirmAmount());
                    secondDetailCopyEntity.setConfirmAmount(dto.getConfirmAmount());
                    secondDetailCopyEntity.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
                    secondDetailCopyEntity.setApproveId(approveId);
                    secondDetailCopyEntity.setManualVoucherIds(manualVoucherIds);
                    secondDetailCopyEntity.setCreateBy(SecurityUtils.getUsername());
                    secondDetailCopyEntity.setCreateTime(LocalDateTime.now());
                    secondDetailCopyEntity.setUpdateBy(SecurityUtils.getUsername());
                    secondDetailCopyEntity.setUpdateTime(LocalDateTime.now());
                    secondDetailCopyEntity.setWriteOffVoucherId(writeOffVoucherIds);
                    secondDetailCopyEntity.setOperationType(ClaimOperationTypeEnum.MODIFY_INCOME_DATE.getCode());
                    secondDetailCopyEntity.setCurClaimAmount(dto.getCurClaimAmount());
                    if (StringUtils.isNotEmpty(batchClaimVoucherIds)) {
                        secondDetailCopyEntity.setVoucherIds(batchClaimVoucherIds);
                    } else {
                        secondDetailCopyEntity.setVoucherIds(systemVoucherIds);
                    }
                }
                nonConfirmCollectionSecondDetailService.save(secondDetailCopyEntity);

                // 创建认领记录
                this.createBusinessClaimRepaymentRecord(dto, systemVoucherIds, detailId, instant);
            }

        }
    }

    /**
     * 创建核销回款凭证
     */
    private String createWriteOffVoucher(String voucherIds, LocalDateTime oldBusinessDate, LocalDateTime newBusinessDate) {
        List<String> result = new ArrayList<>();
        List<VoucherEntity> voucherEntityList = voucherService.getBaseMapper().selectBatchIds(
                Arrays.asList(voucherIds.split(",")));
        if (voucherEntityList == null || voucherEntityList.isEmpty()) {
            throw new ServiceException("凭证信息不存在；voucherIds=" + voucherIds);
        }

        List<VoucherEntity> newVoucherEntityList = new ArrayList<>();
        List<VoucherEntryEntity> newVoucherEntryEntityList = new ArrayList<>();
        for (VoucherEntity voucherEntity : voucherEntityList) {

            // 红冲凭证
            VoucherEntity oldVoucherEntity = BeanUtil.copyProperties(voucherEntity, VoucherEntity.class);
            Long oldVoucherId = IdWorker.getId();
            result.add(String.valueOf(oldVoucherId));

            oldVoucherEntity.setId(oldVoucherId);
            oldVoucherEntity.setCreateBy(SecurityUtils.getUsername());
            oldVoucherEntity.setCreateTime(LocalDateTime.now());
            oldVoucherEntity.setUpdateBy(SecurityUtils.getUsername());
            oldVoucherEntity.setUpdateTime(LocalDateTime.now());
            oldVoucherEntity.setSceneCode(SceneEnum.SGPZMGRZRQ.getCode());
            oldVoucherEntity.setSceneName(SceneEnum.SGPZMGRZRQ.getDesc());
            oldVoucherEntity.setBusinessDate(oldBusinessDate);
            oldVoucherEntity.setVoucherDate(oldBusinessDate);
            Long voucherNumber = manualService.generateVoucherNum(voucherEntity.getVoucherType(), LocalDateTime.now());
            oldVoucherEntity.setVoucherNum(voucherNumber);
            oldVoucherEntity.setVoucherWay(VoucherWayEnum.MANUAL.getCode());
            oldVoucherEntity.setCreateUserNo(String.valueOf(SecurityUtils.getUserId()));
            oldVoucherEntity.setCreateUserName(SecurityUtils.getUsername());
            oldVoucherEntity.setVoucherStatus(ProcessStatusEnum.SUBMITTED.getCode());
            oldVoucherEntity.setPeriodCode(Integer.parseInt(DateUtils.dateTimeNow("yyyyMM")));
            oldVoucherEntity.setVoucherSummary("修改入账日期-红冲之前的凭证，凭证id：".concat(String.valueOf(voucherEntity.getId())));
            newVoucherEntityList.add(oldVoucherEntity);

            // 分录查询
            List<VoucherEntryEntity> voucherEntryEntityList = voucherEntryService.selectByVoucherId(voucherEntity.getId());
            if (voucherEntryEntityList != null && !voucherEntryEntityList.isEmpty()) {
                for (VoucherEntryEntity voucherEntry : voucherEntryEntityList) {
                    VoucherEntryEntity newVoucherEntryEntity = BeanUtil.copyProperties(voucherEntry, VoucherEntryEntity.class);
                    newVoucherEntryEntity.setId(IdWorker.getId());
                    newVoucherEntryEntity.setCreateBy(SecurityUtils.getUsername());
                    newVoucherEntryEntity.setCreateTime(LocalDateTime.now());
                    newVoucherEntryEntity.setUpdateBy(SecurityUtils.getUsername());
                    newVoucherEntryEntity.setUpdateTime(LocalDateTime.now());
                    newVoucherEntryEntity.setVoucherId(oldVoucherId);
                    newVoucherEntryEntity.setAccountPeriod(Integer.parseInt(DateUtils.dateTimeNow("yyyyMM")));
                    if (voucherEntry.getCreditAmount() != null) {
                        newVoucherEntryEntity.setCreditAmount(voucherEntry.getCreditAmount().multiply(new BigDecimal(-1)));
                    }
                    if (voucherEntry.getDebitAmount() != null) {
                        newVoucherEntryEntity.setDebitAmount(voucherEntry.getDebitAmount().multiply(new BigDecimal(-1)));
                    }
                    newVoucherEntryEntityList.add(newVoucherEntryEntity);
                }
            }

            // 新凭证
            VoucherEntity newVoucherEntity = BeanUtil.copyProperties(voucherEntity, VoucherEntity.class);
            Long voucherId = IdWorker.getId();
            result.add(String.valueOf(voucherId));

            newVoucherEntity.setId(voucherId);
            newVoucherEntity.setCreateBy(SecurityUtils.getUsername());
            newVoucherEntity.setCreateTime(LocalDateTime.now());
            newVoucherEntity.setUpdateBy(SecurityUtils.getUsername());
            newVoucherEntity.setUpdateTime(LocalDateTime.now());
            newVoucherEntity.setSceneCode(SceneEnum.SGPZMGRZRQ.getCode());
            newVoucherEntity.setSceneName(SceneEnum.SGPZMGRZRQ.getDesc());
            newVoucherEntity.setBusinessDate(newBusinessDate);
            newVoucherEntity.setVoucherDate(newBusinessDate);
            voucherNumber = manualService.generateVoucherNum(voucherEntity.getVoucherType(), LocalDateTime.now());
            newVoucherEntity.setVoucherNum(voucherNumber);
            newVoucherEntity.setVoucherWay(VoucherWayEnum.MANUAL.getCode());
            newVoucherEntity.setCreateUserNo(String.valueOf(SecurityUtils.getUserId()));
            newVoucherEntity.setCreateUserName(SecurityUtils.getUsername());
            newVoucherEntity.setVoucherStatus(ProcessStatusEnum.SUBMITTED.getCode());
            newVoucherEntity.setPeriodCode(Integer.parseInt(DateUtils.dateTimeNow("yyyyMM")));
            newVoucherEntityList.add(newVoucherEntity);

            // 分录查询
//            List<VoucherEntryEntity> voucherEntryEntityList = voucherEntryService.selectByVoucherId(voucherEntity.getId());
            if (voucherEntryEntityList != null && !voucherEntryEntityList.isEmpty()) {
                for (VoucherEntryEntity voucherEntry : voucherEntryEntityList) {
                    VoucherEntryEntity newVoucherEntryEntity = BeanUtil.copyProperties(voucherEntry, VoucherEntryEntity.class);
                    newVoucherEntryEntity.setId(IdWorker.getId());
                    newVoucherEntryEntity.setCreateBy(SecurityUtils.getUsername());
                    newVoucherEntryEntity.setCreateTime(LocalDateTime.now());
                    newVoucherEntryEntity.setUpdateBy(SecurityUtils.getUsername());
                    newVoucherEntryEntity.setUpdateTime(LocalDateTime.now());
                    newVoucherEntryEntity.setVoucherId(voucherId);
                    newVoucherEntryEntity.setAccountPeriod(Integer.parseInt(DateUtils.dateTimeNow("yyyyMM")));
                    newVoucherEntryEntityList.add(newVoucherEntryEntity);
                }
            }
        }
        voucherService.saveBatch(newVoucherEntityList);
        voucherEntryService.saveBatch(newVoucherEntryEntityList);
        return result.stream().collect(Collectors.joining(","));
    }

    /**
     * 创建手工凭证
     */
    private String createManualVoucher(Long manualId, LocalDateTime oldBusinessDate, LocalDateTime newBusinessDate) {
        ManualEntity manualEntity = manualService.getBaseMapper().selectById(manualId);
        if (manualEntity == null) {
            throw new ServiceException("凭证信息不存在；ManualId=" + manualId);
        }

        // 分录
        List<ManualVoucherVO> manualVoucherOldVOList = manualService.getManualVoucherById(manualEntity.getId());

        // 冲销之前的手工凭证
        Long oldManualId = this.writeOffManualVoucher(manualEntity, oldBusinessDate, manualVoucherOldVOList);

        // 创建新凭证
        ManualEntity manualCopyEntity = BeanUtil.copyProperties(manualEntity, ManualEntity.class);
        Long newManualId = IdWorker.getId();
        manualCopyEntity.setId(newManualId);
        manualCopyEntity.setCreateBy(SecurityUtils.getUsername());
        manualCopyEntity.setCreateTime(LocalDateTime.now());
        manualCopyEntity.setUpdateBy(SecurityUtils.getUsername());
        manualCopyEntity.setUpdateTime(LocalDateTime.now());
        manualCopyEntity.setPeriodCode(Integer.parseInt(DateUtils.dateTimeNow("yyyyMM")));
        manualCopyEntity.setBusinessDate(newBusinessDate);
        manualCopyEntity.setVoucherDate(newBusinessDate);
        manualCopyEntity.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
        Long voucherNumber = manualService.generateVoucherNum(manualEntity.getVoucherType(), LocalDateTime.now());
        manualCopyEntity.setVoucherNum(voucherNumber);
        manualCopyEntity.setSceneCode(SceneEnum.SGPZMGRZRQ.getCode());
        manualCopyEntity.setSceneName(SceneEnum.SGPZMGRZRQ.getDesc());
        manualCopyEntity.setIsWriteOff(YesOrNoEnum.NO.getCode());
        manualService.save(manualCopyEntity);

//        List<ManualVoucherVO> manualVoucherVOList = manualService.getManualVoucherById(manualId);
        List<ManualVoucherEntity> newManualVoucherEntityList = new ArrayList<>();
        if (manualVoucherOldVOList != null && !manualVoucherOldVOList.isEmpty()) {
            for (ManualVoucherVO manualVoucherVO : manualVoucherOldVOList) {
                ManualVoucherEntity newManualVoucherEntity = BeanUtil.copyProperties(manualVoucherVO, ManualVoucherEntity.class);
                newManualVoucherEntity.setId(IdWorker.getId());
                newManualVoucherEntity.setCreateBy(SecurityUtils.getUsername());
                newManualVoucherEntity.setCreateTime(LocalDateTime.now());
                newManualVoucherEntity.setUpdateBy(SecurityUtils.getUsername());
                newManualVoucherEntity.setUpdateTime(LocalDateTime.now());
                newManualVoucherEntity.setPeriodCode(Integer.parseInt(DateUtils.dateTimeNow("yyyyMM")));
                newManualVoucherEntity.setBusinessDate(newBusinessDate);
                newManualVoucherEntity.setVoucherDate(newBusinessDate);
                newManualVoucherEntity.setSceneCode(SceneEnum.SGPZMGRZRQ.getCode());
                newManualVoucherEntity.setManualId(newManualId);
                newManualVoucherEntityList.add(newManualVoucherEntity);
            }
        }
        manualVoucherService.saveBatch(newManualVoucherEntityList);
        manualService.generateVoucher(newManualId, StringUtils.EMPTY, StringUtils.EMPTY,
                Boolean.FALSE,ManualServiceImpl.NON_CONFIRM_COLLECTION_SECOND_DETAIL);
        return String.valueOf(oldManualId).concat(",").concat(String.valueOf(newManualId));
    }

    /**
     * 红冲手工凭证-修改入账日期
     */
    private Long writeOffManualVoucher(ManualEntity manualEntity, LocalDateTime businessDate,
                                       List<ManualVoucherVO> manualVoucherOldVOList) {
        // 红冲之前的旧凭证
        ManualEntity manualOldEntity = BeanUtil.copyProperties(manualEntity, ManualEntity.class);
        Long oldManualId = IdWorker.getId();
        manualOldEntity.setId(oldManualId);
        manualOldEntity.setCreateBy(SecurityUtils.getUsername());
        manualOldEntity.setCreateTime(LocalDateTime.now());
        manualOldEntity.setUpdateBy(SecurityUtils.getUsername());
        manualOldEntity.setUpdateTime(LocalDateTime.now());
        manualOldEntity.setPeriodCode(Integer.parseInt(DateUtils.dateTimeNow("yyyyMM")));
        manualOldEntity.setBusinessDate(businessDate);
        manualOldEntity.setVoucherDate(businessDate);
        manualOldEntity.setVoucherSummary("修改入账日期-红冲之前的凭证，手工凭证id：".concat(String.valueOf(manualEntity.getId())));
        manualOldEntity.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
        Long voucherNumber = manualService.generateVoucherNum(manualEntity.getVoucherType(), LocalDateTime.now());
        manualOldEntity.setVoucherNum(voucherNumber);
        manualOldEntity.setSceneCode(SceneEnum.SGPZMGRZRQ.getCode());
        manualOldEntity.setSceneName(SceneEnum.SGPZMGRZRQ.getDesc());
        manualOldEntity.setIsWriteOff(YesOrNoEnum.YES.getCode());
        manualService.save(manualOldEntity);

        // 红冲分录
        List<ManualVoucherEntity> oldManualVoucherEntityList = new ArrayList<>();
        if (manualVoucherOldVOList != null && !manualVoucherOldVOList.isEmpty()) {
            for (ManualVoucherVO manualVoucherVO : manualVoucherOldVOList) {
                ManualVoucherEntity newManualVoucherEntity = BeanUtil.copyProperties(manualVoucherVO, ManualVoucherEntity.class);
                newManualVoucherEntity.setId(IdWorker.getId());
                newManualVoucherEntity.setCreateBy(SecurityUtils.getUsername());
                newManualVoucherEntity.setCreateTime(LocalDateTime.now());
                newManualVoucherEntity.setUpdateBy(SecurityUtils.getUsername());
                newManualVoucherEntity.setUpdateTime(LocalDateTime.now());
                newManualVoucherEntity.setPeriodCode(Integer.parseInt(DateUtils.dateTimeNow("yyyyMM")));
                newManualVoucherEntity.setBusinessDate(businessDate);
                newManualVoucherEntity.setVoucherDate(businessDate);
                newManualVoucherEntity.setSceneCode(SceneEnum.SGPZMGRZRQ.getCode());
                newManualVoucherEntity.setDebitAmount(manualVoucherVO.getDebitAmount() == null ? null :
                        manualVoucherVO.getDebitAmount().multiply(new BigDecimal(-1)));
                newManualVoucherEntity.setCreditAmount(manualVoucherVO.getCreditAmount() == null ? null :
                        manualVoucherVO.getCreditAmount().multiply(new BigDecimal(-1)));
                newManualVoucherEntity.setVoucherSummary("修改入账日期-红冲之前的凭证，手工凭证分录id：".concat(String.valueOf(manualVoucherVO.getId())));
                newManualVoucherEntity.setManualId(oldManualId);
                oldManualVoucherEntityList.add(newManualVoucherEntity);
            }
        }
        manualVoucherService.saveBatch(oldManualVoucherEntityList);
        manualService.generateVoucher(oldManualId, StringUtils.EMPTY, StringUtils.EMPTY,
                Boolean.FALSE,ManualServiceImpl.NON_CONFIRM_COLLECTION_SECOND_DETAIL);
        return oldManualId;
    }

    /**
     * 创建业务操作记录
     */
    private void createBusinessClaimRepaymentRecord(SelectIncomeDateInfoOutputDTO dto, String voucherIds,
                                                    Long newNonConfirmSecondDetailId, Instant instant) {

        int maxBatchNo = businessClaimRepaymentRecordService.getMaxBatchNo(dto.getEbankSerialNumber());
        maxBatchNo = maxBatchNo +  1;

        List<BusinessClaimRepaymentRecordEntity> newClaimRepaymentRecordEntityList = new ArrayList<>();
        // 认领数据明细
        BusinessClaimRepaymentRecordEntity entityCopy = new BusinessClaimRepaymentRecordEntity();
        entityCopy.setId(IdWorker.getId());
        entityCopy.setSystemCode(SystemEnum.CWZT.getCode());
        entityCopy.setClaimAmount(dto.getClaimAmount());
        entityCopy.setCreateBy(SecurityUtils.getUsername());
        entityCopy.setCreateTime(LocalDateTime.now());
        entityCopy.setUpdateBy(SecurityUtils.getUsername());
        entityCopy.setUpdateTime(LocalDateTime.now());
        entityCopy.setClientCode(dto.getClientCode());
        entityCopy.setContractCode(dto.getContractCode());
        entityCopy.setCurrencyType(dto.getCurrencyType());
//        entityCopy.setOrderId(dto.getOrderId());
        entityCopy.setBusinessDate(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
        entityCopy.setEbankSerialNumber(dto.getEbankSerialNumber());
        entityCopy.setOrgId(dto.getOrgId());
        entityCopy.setOrgName(dto.getOrgName());
        entityCopy.setSceneCode(SceneEnum.SGPZMGRZRQ.getCode());
        entityCopy.setSceneName(SceneEnum.SGPZMGRZRQ.getDesc());
        entityCopy.setBatchNo(new BigDecimal(maxBatchNo));
        entityCopy.setOperationType(ClaimOperationTypeEnum.MODIFY_INCOME_DATE.getCode());
        entityCopy.setNonConfirmSecondDetailId(newNonConfirmSecondDetailId);
        entityCopy.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
        entityCopy.setNewEbankSerialNumber(dto.getNewEbankSerialNumber());
        entityCopy.setIncomeYmOld(DateUtils.parseDateToStr("yyyy-MM-dd", dto.getBusinessDate()));
        entityCopy.setIsCrossOrg(dto.getIsCrossOrg());
        entityCopy.setVoucherIds(voucherIds);
        newClaimRepaymentRecordEntityList.add(entityCopy);

        // 冲回之前的认领记录
        BusinessClaimRepaymentRecordEntity recallEntityCopy = new BusinessClaimRepaymentRecordEntity();
        recallEntityCopy.setId(IdWorker.getId());
        recallEntityCopy.setSystemCode(SystemEnum.CWZT.getCode());
        recallEntityCopy.setClaimAmount(NumberUtil.mul(dto.getClaimAmount(), new BigDecimal(-1)));
        recallEntityCopy.setCreateBy(SecurityUtils.getUsername());
        recallEntityCopy.setCreateTime(LocalDateTime.now());
        recallEntityCopy.setUpdateBy(SecurityUtils.getUsername());
        recallEntityCopy.setUpdateTime(LocalDateTime.now());
        recallEntityCopy.setClientCode(dto.getClientCode());
        recallEntityCopy.setContractCode(dto.getContractCode());
        recallEntityCopy.setCurrencyType(dto.getCurrencyType());
        recallEntityCopy.setOrderId(dto.getOrderId());
        recallEntityCopy.setBusinessDate(DateUtils.parseLocalDateTime(DateUtils.dateTime(dto.getBusinessDate())));
        recallEntityCopy.setEbankSerialNumber(dto.getEbankSerialNumber());
        recallEntityCopy.setOrgId(dto.getOrgId());
        recallEntityCopy.setOrgName(dto.getOrgName());
        recallEntityCopy.setSceneCode(SceneEnum.SGPZMGRZRQ.getCode());
        recallEntityCopy.setSceneName(SceneEnum.SGPZMGRZRQ.getDesc());
        recallEntityCopy.setBatchNo(new BigDecimal(maxBatchNo));
        recallEntityCopy.setOperationType(ClaimOperationTypeEnum.MODIFY_INCOME_DATE.getCode());
        recallEntityCopy.setNonConfirmSecondDetailId(newNonConfirmSecondDetailId);
        recallEntityCopy.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
        recallEntityCopy.setRemark("冲回之前的认领记录:id:"+dto.getId());
        recallEntityCopy.setNewEbankSerialNumber(dto.getNewEbankSerialNumber());
        recallEntityCopy.setIncomeYmOld(DateUtil.format(dto.getBusinessDate(), "yyyy-MM-dd"));
        recallEntityCopy.setIsCrossOrg(dto.getIsCrossOrg());
        recallEntityCopy.setVoucherIds(voucherIds);
        newClaimRepaymentRecordEntityList.add(recallEntityCopy);
        businessClaimRepaymentRecordService.saveBatch(newClaimRepaymentRecordEntityList);
    }


    private String batchModifyIncomeDateVlidation(ModifyIncomeDateTemplateDownloadExcel excel) {
        if (StringUtils.isEmpty(excel.getBusinessEbankNo()) && StringUtils.isEmpty(excel.getEbankSerialNumber())) {
            return "网银编号和批扣流水号至少填一项!";
        }

        if (excel.getIncomeDateOld() == null) {
            return "原入账年月不能为空!";
        }

        if (excel.getIncomeDateAdjust() == null) {
            return "调整入账日期不能为空!";
        }
        return StringUtil.EMPTY;
    }

    /**
     * @description: 未确认收款-汇总表-认领-软提示校验
     * @author: zhangli.chen
     **/
    HthxSoftTipValidationResult softTipsValidate(ClaimConfirmDTO claimConfirmDTO) {
        HthxSoftTipValidationResult result = new HthxSoftTipValidationResult();
        // 通过录入凭证行-查询合同信息
        List<ContractDTO> contractDTOList = this.queryContractListByVoucherEntry(claimConfirmDTO.getClaimConfirmVoucherList());
        // 若财务合同状态为6类资产转让状态
        if (contractDTOList != null && contractDTOList.size() > 0){
            for (ContractDTO contractDTO : contractDTOList) {
                if (StringUtils.isNotEmpty(FinanceEngineEnum.ContractAssetTransferStatus.getKey(contractDTO.getFinancialContractStatus()))) {
                    // 通过录入凭证行-取得科目列表
                    List<String> accountNumberList = claimConfirmDTO.getClaimConfirmVoucherList().stream().
                            map(ClaimConfirmVoucherDTO::getAccountNumber).collect(Collectors.toList());
                    // 其他应付款_代收款项科目
                    if (!accountNumberList.contains(Constants.OTHER_PAYABLE_PROXY_RECEIVE_ACCOUNT)) {
                        result.setHasWarn(true);
                        result.setWarnMessage(ResultEnum.NC_FINANCIAL_TRANSFER_STATUS.getMessage());
                        break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * @description: 根据分录录入的主体+合同信息，查询合同列表
     * @author: zhangli.chen
     **/
    private List<ContractDTO> queryContractListByVoucherEntry(List<ClaimConfirmVoucherDTO> claimConfirmVoucherList) {
        List<ContractDTO> contractList = new ArrayList<>();
        for (ClaimConfirmVoucherDTO dto : claimConfirmVoucherList) {
            if (StringUtils.isNotEmpty(dto.getCreateConfirmOrgId()) && StringUtils.isNotEmpty(dto.getContractCode())) {
                ContractDTO contractDTO = contractService.getContractDTOByCode(dto.getContractCode(), dto.getCreateConfirmOrgId());
                if (contractDTO != null) {
                    contractList.add(contractDTO);
                }
            }
        }
        return contractList;
    }


    /**
     * @description:未确认收款-汇总表-手工调整余额-确认上传
     * @author: zhangli.chen
     **/
    public R<String> handsAdjustBalance(List<HandsAdjustBalanceTemplateDownloadExcel> importData) {
        log.info("====>>NonConfirmCollectionSumServiceImpl.handsAdjustBalance==>>00==>>");
        if (importData == null || importData.isEmpty()) {
            return R.fail(ResultEnum.COMMON_THE_UPLOAD_FILE_EMPTY.getMessage());
        }
        List<HthxOfflineOnlineBankTemplateExcel> onlineBankTemplateExcelList = new ArrayList<>();
        // 第一步：非空校验
        for (int i = 0; i < importData.size(); i++) {
            HandsAdjustBalanceTemplateDownloadExcel data = importData.get(i);
            if (StringUtils.isEmpty(data.getBusinessEbankNumber()) && StringUtils.isEmpty(data.getEbankSerialNumber())) {
                return R.fail(String.format("第%s行的数据：业务系统网银编号和业务系统网银编号-小网银不能同时为空!", i+1));
            }
            if (data.getClaimAmount() == null) {
                return R.fail(String.format("第%s行的数据：借方发生额不能为空!", i+1));
            }
            NonConfirmCollectionSumEntity sumEntity = BeanUtil.copyProperties(data, NonConfirmCollectionSumEntity.class);
            List<NonConfirmCollectionSumEntity> sumEntities = this.getNonConfirmCollectionSumDTOByCon(sumEntity);
            if (sumEntities == null || sumEntities.isEmpty()) {
                HthxOfflineOnlineBankTemplateExcel dataExcel = BeanUtil.copyProperties(data,HthxOfflineOnlineBankTemplateExcel.class);
                onlineBankTemplateExcelList.add(dataExcel);
            }
        }
        // 第二步：请求资金系统获取网银请求结果
        List<HthxOfflineOnlineBankDataVO> hthxOfflineOnlineBankDataVOList = new ArrayList<>();
        log.info("====>>NonConfirmCollectionSumServiceImpl.handsAdjustBalance==>>01==>>onlineBankTemplateExcelList:{}"
                ,onlineBankTemplateExcelList);
        if(CollectionUtils.isNotEmpty(onlineBankTemplateExcelList)){
            // 从资金系统获取网银信息
           hthxOfflineOnlineBankDataVOList = getOnlineBankInfoFromFundSystem(onlineBankTemplateExcelList);
            log.info("====>>NonConfirmCollectionSumServiceImpl.handsAdjustBalance==>>02==>>hthxOfflineOnlineBankDataVOList:{}"
                    ,hthxOfflineOnlineBankDataVOList);
            // 若资金系统存在，且网银币种为人民币，则后台插入该笔网银，否则则进行提示
            String reminderInfo  =  hthxOfflineOnlineBankDataVOList.stream()
                    .filter(dataVO -> (FinanceEngineEnum.offlineOnlineBankState.NOT_EXIST.getKey().equals(dataVO.getState())
                            || FinanceEngineEnum.offlineOnlineBankState.EXIST_CURRENCY_IS_NOT_RMB.getKey().equals(dataVO.getState())))
                    .map(HthxOfflineOnlineBankDataVO::getEbankNumber)
                    .collect(Collectors.joining(","));
            log.info("====>>NonConfirmCollectionSumServiceImpl.handsAdjustBalance==>>03==>>reminderInfo:{}",reminderInfo);
            if (StringUtils.isNotEmpty(reminderInfo)) {
                return R.fail(PromptMessageUtil.promptMessageFormat(ResultEnum.NC_OFFLINE_ONLINE_BANK_CURRENCY_IS_NOT_RMB, reminderInfo));
            }
        }
        // 第三步：提交审核
        Long uploadFileId = IdWorker.getId();
        Long approveId = this.submitVoucher(uploadFileId, BatchTypeEnum.SGTZYE.getCode());
        // 未确认收款明细
        List<NonConfirmCollectionSecondDetailEntity> nonConfirmCollectionSecondDetailList = new ArrayList<>();
        // 认领明细
        List<BusinessClaimRepaymentRecordEntity> claimRecordList = new ArrayList<>();
        // 线下网银
        List<HthxOfflineOnlineBankDataEntity> newOfflineOnlineBankList = new ArrayList<>();
        // 汇总表
        List<NonConfirmCollectionSumEntity> newSumEntityList = new ArrayList<>();
        // 勾稽表
        List<FundBusinessSystemEbankMappingEntity> newEbankMappingList = new ArrayList<>();
        // 公司主体信息
        R<List<SysDictData>> companyList = remoteDictService.listDictData(DictTypeEnum.COMPANY.getCode());
        Map<String, String> companyMap = companyList.getData().stream().collect(Collectors.toMap(
                e -> e.getDictValue(), e -> e.getDictLabel(), (a, b) -> b));
        for (int i = 0; i < importData.size(); i++) {
            HandsAdjustBalanceTemplateDownloadExcel data = importData.get(i);
            NonConfirmCollectionSumEntity sumEntity = BeanUtil.copyProperties(data, NonConfirmCollectionSumEntity.class);
            log.info("====>>NonConfirmCollectionSumServiceImpl.handsAdjustBalance==>>04==>>data:{},sumEntity:{}"
                    ,data,sumEntity);
            List<NonConfirmCollectionSumEntity> sumEntities = this.getNonConfirmCollectionSumDTOByCon(sumEntity);
            log.info("====>>NonConfirmCollectionSumServiceImpl.handsAdjustBalance==>>05==>>sumEntities:{}",sumEntities);
            if (sumEntities == null || sumEntities.isEmpty()) {
                if (CollectionUtil.isNotEmpty(hthxOfflineOnlineBankDataVOList)) {
                    log.info("====>>NonConfirmCollectionSumServiceImpl.handsAdjustBalance==>>06==>>");
                    // 若资金系统存在，且网银币种为人民币，则后台插入该笔网银
                    HthxOfflineOnlineBankDataVO dataVO = null;
                    Optional<HthxOfflineOnlineBankDataVO> preImportOptional =
                            hthxOfflineOnlineBankDataVOList.stream().filter(
                                    bankDataVO -> ((StringUtils.isNotEmpty(sumEntity.getEbankSerialNumber())
                                            && StringUtils.equals(bankDataVO.getEbankNumber(),sumEntity.getEbankSerialNumber()))
                                            || (StringUtils.isNotEmpty(sumEntity.getBusinessEbankNumber())
                                            && StringUtils.equals(bankDataVO.getEbankNumber(),sumEntity.getBusinessEbankNumber())))
                                    && FinanceEngineEnum.offlineOnlineBankState.EXIST_CURRENCY_IS_RMB.getKey()
                                    .equals(bankDataVO.getState())).findFirst();
                    if (preImportOptional.isPresent()) {
                        dataVO = preImportOptional.get();
                    }
                    log.info("====>>NonConfirmCollectionSumServiceImpl.handsAdjustBalance==>>07==>>dataVO:{}",dataVO);
                    if (dataVO != null) {
                        // 1.判别勾稽关系
                        handleCrossCheckRelation(dataVO.getGjInfo(),newEbankMappingList,BatchTypeEnum.SGTZYE);
                        // 2.判别资金网银
                        handleFundSystemBankRelation(dataVO.getZjWyList(),newOfflineOnlineBankList,BatchTypeEnum.SGTZYE,SceneEnum.HANDS_ADJUST_BALANCE);
                        // 3.判别汇总表--勾稽关系或者资金网银
                        NonConfirmCollectionSumEntity newSumEntity =  handleSumRelation(dataVO,companyMap,newSumEntityList
                                ,BatchTypeEnum.SGTZYE,SceneEnum.HANDS_ADJUST_BALANCE);
                        // 4.判别未确认收款明细表
                        Long detailId =handleNonConfirmDetailRelation(newSumEntity,nonConfirmCollectionSecondDetailList
                                ,dataVO.getClaimAmount(),BatchTypeEnum.SGTZYE,SceneEnum.HANDS_ADJUST_BALANCE,approveId,uploadFileId);
                        // 5.判别未确认收款认领表
                        handleNonConfirmClaimRelation(newSumEntity,claimRecordList,dataVO,detailId,
                                BatchTypeEnum.SGTZYE,SceneEnum.HANDS_ADJUST_BALANCE);
                    }
                }
            } else {
                log.info("====>>NonConfirmCollectionSumServiceImpl.handsAdjustBalance==>>08==>>sumEntities:{}",sumEntities);
                // 1.判别未确认收款明细表
                Long detailId =handleNonConfirmDetailRelation(sumEntities.get(0),nonConfirmCollectionSecondDetailList
                        ,data.getClaimAmount(),null,SceneEnum.HANDS_ADJUST_BALANCE,approveId,uploadFileId);
                // 2.判别未确认收款认领表
                HthxOfflineOnlineBankDataVO dataVO = BeanUtil.copyProperties(data, HthxOfflineOnlineBankDataVO.class);
                handleNonConfirmClaimRelation(sumEntities.get(0),claimRecordList,dataVO,detailId,
                        null,SceneEnum.HANDS_ADJUST_BALANCE);
            }
        }
        log.info("====>>NonConfirmCollectionSumServiceImpl.handleManualUploadData==>>09==>>" +
                "nonConfirmCollectionSecondDetailList:{}",nonConfirmCollectionSecondDetailList);
        // 未确认收款明细
        if(CollectionUtils.isNotEmpty(nonConfirmCollectionSecondDetailList)){
            nonConfirmCollectionSecondDetailService.saveBatch(nonConfirmCollectionSecondDetailList);
        }
        log.info("====>>NonConfirmCollectionSumServiceImpl.handleManualUploadData==>>10==>>" +
                "claimRecordList:{}",claimRecordList);
        // 认领明细
        if(CollectionUtils.isNotEmpty(claimRecordList)){
            businessClaimRepaymentRecordService.saveBatch(claimRecordList);
        }
        log.info("====>>NonConfirmCollectionSumServiceImpl.handleManualUploadData==>>11==>>" +
                "newOfflineOnlineBankList:{}",newOfflineOnlineBankList);
        // 线下网银
        if(CollectionUtils.isNotEmpty(newOfflineOnlineBankList)){
            hthxOfflineOnlineBankService.saveBatch(newOfflineOnlineBankList);
        }
        log.info("====>>NonConfirmCollectionSumServiceImpl.handleManualUploadData==>>12==>>" +
                "newSumEntityList:{}",newSumEntityList);
        // 汇总表
        if(CollectionUtils.isNotEmpty(newSumEntityList)){
            this.saveBatch(newSumEntityList);
        }
        log.info("====>>NonConfirmCollectionSumServiceImpl.handleManualUploadData==>>13==>>" +
                "newEbankMappingList:{}",newEbankMappingList);
        // 勾稽表
        if(CollectionUtils.isNotEmpty(newEbankMappingList)){
            fundBusinessSystemEbankMappingService.saveBatch(newEbankMappingList);
        }
        return R.ok(ResultEnum.COMMON_FILE_UPLOAD_SUCCESS.getMessage());

    }

    /**
     * @param onlineBankTemplateExcelList
     * @param confirmKey
     * @description:未确认收款-汇总表-上传线下网银-确认上传
     * @author: zhangli.chen
     */
    @Override
    public R offlineOnlineBankManualUpload(List<HthxOfflineOnlineBankTemplateExcel> onlineBankTemplateExcelList, String confirmKey) {
        log.info("====>>NonConfirmCollectionSumServiceImpl.offlineOnlineBankManualUpload==>>00==>>confirmKey:{},onlineBankTemplateExcelList.size():{}",confirmKey,
                (onlineBankTemplateExcelList!=null?onlineBankTemplateExcelList.size():FinanceEngineEnum.Numbers.ZERO.getKey()));
        // 第一步：导入文件是否为空判断
        if (CollectionUtils.isEmpty(onlineBankTemplateExcelList)) {
            return R.fail(ResultEnum.COMMON_THE_UPLOAD_FILE_EMPTY.getMessage());
        }
        // 第二步：导入文件中必填字段非空判断
        int row =0;
        for(HthxOfflineOnlineBankTemplateExcel data: onlineBankTemplateExcelList){
            if (StringUtils.isEmpty(data.getBusinessEbankNumber()) && StringUtils.isEmpty(data.getEbankSerialNumber())) {
                return R.fail(String.format("第%s行的数据：业务系统网银编号和业务系统网银编号-小网银不能同时为空！", row + 1));
            }
            if (data.getClaimAmount() == null) {
                return R.fail(String.format("第%s行的数据：借方发生额不能为空！", row + 1));
            }
            if (StringUtils.isEmpty(data.getClaimDate())) {
                return R.fail(String.format("第%s行的数据：认领日期不能为空！", row + 1));
            }
            log.info("====>>NonConfirmCollectionSumServiceImpl.offlineOnlineBankManualUpload==>>01==>>ClaimDate:{}"
                    ,data.getClaimDate());
            if(StringUtils.isNull(DateUtil.parse(data.getClaimDate()))){
                return R.fail(String.format("第%s行的数据：认领日期格式不正确！", row + 1));
            }
            if (StringUtils.isEmpty(data.getOrgName())) {
                return R.fail(String.format("第%s行的数据：网银归属主体不能为空！", row + 1));
            }
            row++;
        }
        // 第三步：导入文件中必填字段业务逻辑校验
        List<OrgCompanyVO> collectionEntityList = orgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toList());
        log.info("====>>NonConfirmCollectionSumServiceImpl.offlineOnlineBankManualUpload==>>02==>>collectionEntityList:{}"
                ,collectionEntityList);
        for (int i = 0; i < onlineBankTemplateExcelList.size(); i++) {
            HthxOfflineOnlineBankTemplateExcel data = onlineBankTemplateExcelList.get(i);
            String orgId = null;
            for(OrgCompanyVO orgCompanyVO : collectionEntityList ){
                if(StringUtils.isNotEmpty(orgCompanyVO.getOrgName()) && orgCompanyVO.getOrgName().contains(data.getOrgName())){
                    orgId =  orgCompanyVO.getOrgId();
                }
                if(StringUtils.isNotEmpty(orgId)){
                    data.setOrgId(orgId);
                    break;
                }
            }
            if(StringUtils.isEmpty(orgId)){
                log.info("====>>NonConfirmCollectionSumServiceImpl.offlineOnlineBankManualUpload==>>03==>>data:{}",data);
                return R.fail(String.format("第%s行的数据：根据导入的网银归属主体未在中台匹配到对应主体名称！", i + 1));
            }
            NonConfirmCollectionSumEntity sumEntity = BeanUtil.copyProperties(data, NonConfirmCollectionSumEntity.class);
            // 校验表eg_non_confirm_collection_sum中是否存在该网银数据
            List<NonConfirmCollectionSumEntity> sumEntityList = this.getNonConfirmCollectionSumDTOByCon(sumEntity);
            if (CollectionUtils.isNotEmpty(sumEntityList)) {
                return R.fail(String.format("第%s行的数据：财务中台已有该网银编号数据，若需调整该网银发生额请使用【手工调整余额】进行调整!", i+1));
            }
        }
        // 第四步：若首次导入，则校验该网银资金系统是否存在
        if (StringUtils.isEmpty(confirmKey)) {
            // 手工上传后进行网银数据检查
            return checkDataAfterManualUpload(onlineBankTemplateExcelList);
        }else{
            // 第五步：若二次确认导入，则直接插入网银数据
            List<HthxOfflineOnlineBankDataVO> dataVOList = (List<HthxOfflineOnlineBankDataVO>)hthxConfirmCacheService.getData(confirmKey);
            log.info("====>>NonConfirmCollectionSumServiceImpl.offlineOnlineBankManualUpload==>>04==>>confirmKey:{}" +
                    ",dataVOList：{}",confirmKey,dataVOList);
            if (dataVOList==null || CollectionUtils.isEmpty(dataVOList)) {
                return R.fail(PromptMessageUtil.promptMessageFormat(ResultEnum.COMMON_FILE_UPLOAD_HAS_TIMED_OUT, RedisConstant.WARE_TIME_OUT));
            }
            handleManualUploadData(dataVOList);
        }
        return R.ok();
    }


    /**
     * @description: 手工上传后进行网银数据检查
     * @author: zhangli.chen
     * @date 2025/08/12 18:31
     * @param: onlineBankTemplateExcelList
     * @return R
     **/
    private R checkDataAfterManualUpload(List<HthxOfflineOnlineBankTemplateExcel> onlineBankTemplateExcelList){
        // 从资金系统获取网银信息
        List<HthxOfflineOnlineBankDataVO> hthxOfflineOnlineBankDataVOList = getOnlineBankInfoFromFundSystem(onlineBankTemplateExcelList);
        // 若资金系统返回存在且网银币种为人民币，则进行二次确认
        String reminderInfo  =  hthxOfflineOnlineBankDataVOList.stream()
                .filter(dataVO -> FinanceEngineEnum.offlineOnlineBankState.EXIST_CURRENCY_IS_RMB.getKey().equals(dataVO.getState()))
                .map(HthxOfflineOnlineBankDataVO::getEbankNumber)
                .collect(Collectors.joining(","));
        log.info("====>>NonConfirmCollectionSumServiceImpl.checkDataAfterManualUpload==>>00==>>reminderInfo:{}",reminderInfo);
        if (StringUtils.isNotEmpty(reminderInfo)) {
            String confirmKey = HthxUUIDUtils.generateUUID();
            HthxBaseConfirmData confirmData = new HthxBaseConfirmData(confirmKey);
            hthxConfirmCacheService.setData(confirmKey,hthxOfflineOnlineBankDataVOList);
            return R.warn(confirmData, PromptMessageUtil.promptMessageFormat(ResultEnum.NC_OFFLINE_ONLINE_BANK_CURRENCY_IS_RMB, reminderInfo));
        }else{
            log.info("====>>NonConfirmCollectionSumServiceImpl.checkDataAfterManualUpload==>>01==>>");
            handleManualUploadData(hthxOfflineOnlineBankDataVOList);
        }
        return R.ok();
    }

    /**
     * @description: 处理上传的网银数据
     * @author: zhangli.chen
     * @date 2025/08/12 18:36
     * @param: dataVOList
     * @return void
     **/
    private void handleManualUploadData(List<HthxOfflineOnlineBankDataVO> dataVOList){
        log.info("====>>NonConfirmCollectionSumServiceImpl.handleManualUploadData==>>00==>>dataVOList:{}",dataVOList);
        // 第三步：提交审核
        Long uploadFileId = IdWorker.getId();
        Long approveId = this.submitVoucher(uploadFileId, BatchTypeEnum.XXWYSC.getCode());
        // 未确认收款明细
        List<NonConfirmCollectionSecondDetailEntity> nonConfirmCollectionSecondDetailList = new ArrayList<>();
        // 认领明细
        List<BusinessClaimRepaymentRecordEntity> claimRecordList = new ArrayList<>();
        // 线下网银
        List<HthxOfflineOnlineBankDataEntity> newOfflineOnlineBankList = new ArrayList<>();
        // 汇总表
        List<NonConfirmCollectionSumEntity> newSumEntityList = new ArrayList<>();
        // 勾稽表
        List<FundBusinessSystemEbankMappingEntity> newEbankMappingList = new ArrayList<>();
        // 公司主体信息
        R<List<SysDictData>> companyList = remoteDictService.listDictData(DictTypeEnum.COMPANY.getCode());
        Map<String, String> companyMap = companyList.getData().stream().collect(Collectors.toMap(
                e -> e.getDictValue(), e -> e.getDictLabel(), (a, b) -> b));
        for(HthxOfflineOnlineBankDataVO dataVO :dataVOList){
            // 一、若是线下网银
            if(FinanceEngineEnum.offlineOnlineBankState.NOT_EXIST.getKey().equals(dataVO.getState())){
                log.info("====>>NonConfirmCollectionSumServiceImpl.handleManualUploadData==>>01==>>dataVO:{}",dataVO);
                // 1.判别线下网银表
                handleOfflineOnlineBankRelation(dataVO,newOfflineOnlineBankList,BatchTypeEnum.XXWYSC,SceneEnum.HANDS_UPLOAD_BANK);
                // 2.判别汇总表
                NonConfirmCollectionSumEntity newSumEntity =  handleSumRelation(dataVO,companyMap,newSumEntityList
                        ,BatchTypeEnum.XXWYSC,SceneEnum.HANDS_UPLOAD_BANK);
                // 3.判别未确认收款明细表
                Long detailId =handleNonConfirmDetailRelation(newSumEntity,nonConfirmCollectionSecondDetailList
                        ,dataVO.getClaimAmount(),BatchTypeEnum.XXWYSC,SceneEnum.HANDS_UPLOAD_BANK,approveId,uploadFileId);
                // 4.判别未确认收款认领表
                handleNonConfirmClaimRelation(newSumEntity,claimRecordList,dataVO,detailId,
                        BatchTypeEnum.XXWYSC,SceneEnum.HANDS_UPLOAD_BANK);
            }else{
                log.info("====>>NonConfirmCollectionSumServiceImpl.handleManualUploadData==>>02==>>dataVO:{}",dataVO);
                // 二、若是资金系统已存在网银
                // 1.判别勾稽关系
                handleCrossCheckRelation(dataVO.getGjInfo(),newEbankMappingList,BatchTypeEnum.XXWYSC);
                // 2.判别资金网银
                handleFundSystemBankRelation(dataVO.getZjWyList(),newOfflineOnlineBankList,BatchTypeEnum.XXWYSC,SceneEnum.HANDS_UPLOAD_BANK);
                // 3.判别汇总表--勾稽关系或者资金网银
                NonConfirmCollectionSumEntity newSumEntity =  handleSumRelation(dataVO,companyMap,newSumEntityList
                        ,BatchTypeEnum.XXWYSC,SceneEnum.HANDS_UPLOAD_BANK);
                // 4.判别未确认收款明细表
                Long detailId = handleNonConfirmDetailRelation(newSumEntity,nonConfirmCollectionSecondDetailList
                        ,dataVO.getClaimAmount(),BatchTypeEnum.XXWYSC,SceneEnum.HANDS_UPLOAD_BANK,approveId,uploadFileId);
                // 5.判别未确认收款认领表
                handleNonConfirmClaimRelation(newSumEntity,claimRecordList,dataVO,detailId,
                        BatchTypeEnum.XXWYSC,SceneEnum.HANDS_UPLOAD_BANK);
            }
        }
        log.info("====>>NonConfirmCollectionSumServiceImpl.handleManualUploadData==>>03==>>" +
                "nonConfirmCollectionSecondDetailList:{}",nonConfirmCollectionSecondDetailList);
        // 未确认收款明细
        if(CollectionUtils.isNotEmpty(nonConfirmCollectionSecondDetailList)){
            nonConfirmCollectionSecondDetailService.saveBatch(nonConfirmCollectionSecondDetailList);
        }
        log.info("====>>NonConfirmCollectionSumServiceImpl.handleManualUploadData==>>04==>>" +
                "claimRecordList:{}",claimRecordList);
        // 认领明细
        if(CollectionUtils.isNotEmpty(claimRecordList)){
            businessClaimRepaymentRecordService.saveBatch(claimRecordList);
        }
        log.info("====>>NonConfirmCollectionSumServiceImpl.handleManualUploadData==>>05==>>" +
                "newOfflineOnlineBankList:{}",newOfflineOnlineBankList);
        // 线下网银
        if(CollectionUtils.isNotEmpty(newOfflineOnlineBankList)){
            hthxOfflineOnlineBankService.saveBatch(newOfflineOnlineBankList);
        }
        log.info("====>>NonConfirmCollectionSumServiceImpl.handleManualUploadData==>>06==>>" +
                "newSumEntityList:{}",newSumEntityList);
        // 汇总表
        if(CollectionUtils.isNotEmpty(newSumEntityList)){
            this.saveBatch(newSumEntityList);
        }
        log.info("====>>NonConfirmCollectionSumServiceImpl.handleManualUploadData==>>07==>>" +
                "newEbankMappingList:{}",newEbankMappingList);
        // 勾稽表
        if(CollectionUtils.isNotEmpty(newEbankMappingList)){
            fundBusinessSystemEbankMappingService.saveBatch(newEbankMappingList);
        }
    }

    /**
     * @description: 从资金系统获取网银信息
     * @author: zhangli.chen
     **/
    private List<HthxOfflineOnlineBankDataVO> getOnlineBankInfoFromFundSystem(List<HthxOfflineOnlineBankTemplateExcel> onlineBankTemplateExcelList){
        log.info("====>>NonConfirmCollectionSumServiceImpl.getOnlineBankInfoFromFundSystem==>>00==>>onlineBankTemplateExcelList:{}"
                ,onlineBankTemplateExcelList);
        List<HthxOfflineOnlineBankDataVO> hthxOfflineOnlineBankDataVOList = new ArrayList<>();
        // 获取业务系统网银编号-小网银的资金信息
        List<String>  ebankSerialNumberParamsList = new ArrayList<>();
        ebankSerialNumberParamsList = onlineBankTemplateExcelList.stream()
                .map(HthxOfflineOnlineBankTemplateExcel::getEbankSerialNumber)
                .filter(serial -> serial != null && !serial.trim().isEmpty())
                .collect(Collectors.toList());
        log.info("====>>NonConfirmCollectionSumServiceImpl.getOnlineBankInfoFromFundSystem==>>01==>>ebankSerialNumberParamsList:{}"
                ,ebankSerialNumberParamsList);
        Map<String, HthxFundEbankTransactionDataDTO> ebankTransactionDataMap = new HashMap<>();
        HthxFundEbankQueryDTO yWyxxRequest = new HthxFundEbankQueryDTO();
        yWyxxRequest.setList(ebankSerialNumberParamsList);
        R<List<HthxFundEbankTransactionDataDTO>> fundEbankResult = hthxFundCoreService.queryWyxx(yWyxxRequest);
        log.info("====>>NonConfirmCollectionSumServiceImpl.getOnlineBankInfoFromFundSystem==>>02==>>fundEbankResult:{}"
                ,fundEbankResult);
        if (fundEbankResult.getCode() == HttpStatus.SUCCESS) {
            List<HthxFundEbankTransactionDataDTO> HthxFundEbankTransactionDataList =  fundEbankResult.getData();
            if(CollectionUtils.isNotEmpty(HthxFundEbankTransactionDataList)){
                ebankTransactionDataMap = HthxFundEbankTransactionDataList.stream()
                        .filter(dto -> dto != null && dto.getEbankNumber() != null)
                        .collect(Collectors.toMap(
                                HthxFundEbankTransactionDataDTO::getEbankNumber,
                                dto -> dto,
                                (existing, replacement) -> replacement));
            }
        }
        log.info("====>>NonConfirmCollectionSumServiceImpl.getOnlineBankInfoFromFundSystem==>>03==>>ebankTransactionDataMap:{}"
                ,ebankTransactionDataMap);
        // 获取业务系统网银编号资金信息
        List<String> businessEbankNumberParamsList = new ArrayList<>();
        for(HthxOfflineOnlineBankTemplateExcel templateExcel:onlineBankTemplateExcelList){
            if(!ebankTransactionDataMap.containsKey(templateExcel.getEbankSerialNumber())
                    && StringUtils.isNotEmpty(templateExcel.getBusinessEbankNumber())){
                businessEbankNumberParamsList.add(templateExcel.getBusinessEbankNumber());
            }
        }
        Map<String, HthxOnlineBankCrossCheckDataDTO> onlineBankDataMap = new HashMap<>();
        log.info("====>>NonConfirmCollectionSumServiceImpl.getOnlineBankInfoFromFundSystem==>>03==>>businessEbankNumberParamsList:{}"
                ,businessEbankNumberParamsList);
        if(CollectionUtils.isNotEmpty(businessEbankNumberParamsList)){
            HthxFundEbankQueryDTO yGjxxRequest = new HthxFundEbankQueryDTO();
            yGjxxRequest.setList(businessEbankNumberParamsList);
            R<List<HthxOnlineBankCrossCheckDataDTO>> onlineBankResult = hthxFundCoreService.queryGjxx(yGjxxRequest);
            log.info("====>>NonConfirmCollectionSumServiceImpl.getOnlineBankInfoFromFundSystem==>>04==>>onlineBankResult:{}"
                    ,onlineBankResult);
            if (onlineBankResult.getCode() == HttpStatus.SUCCESS) {
                List<HthxOnlineBankCrossCheckDataDTO> hthxOnlineBankCrossCheckDataList =  onlineBankResult.getData();
                if(CollectionUtils.isNotEmpty(hthxOnlineBankCrossCheckDataList)){
                    onlineBankDataMap =  hthxOnlineBankCrossCheckDataList.stream()
                            .filter(dto -> dto != null && dto.getHyWybh() != null)
                            .collect(Collectors.toMap(
                                    HthxOnlineBankCrossCheckDataDTO::getHyWybh,
                                    dto -> dto,
                                    (existing, replacement) -> {
                                        mergeZjWyList(existing, replacement);
                                        return existing;
                                    }
                            ));
                }
            }
        }
        log.info("====>>NonConfirmCollectionSumServiceImpl.getOnlineBankInfoFromFundSystem==>>05==>>onlineBankDataMap:{}"
                ,onlineBankDataMap);
        // 针对每一笔导入数据进行状态判别
        for(HthxOfflineOnlineBankTemplateExcel templateExcel:onlineBankTemplateExcelList){
            HthxOfflineOnlineBankDataVO onlineBankDataVO =  BeanUtil.copyProperties(templateExcel, HthxOfflineOnlineBankDataVO.class);
            if(StringUtils.isNotEmpty(templateExcel.getEbankSerialNumber())){
                onlineBankDataVO.setEbankNumber(templateExcel.getEbankSerialNumber());
            }else{
                onlineBankDataVO.setEbankNumber(templateExcel.getBusinessEbankNumber());
            }
            boolean exists = false;
            if(StringUtils.isNotEmpty(templateExcel.getEbankSerialNumber())){
                if(ebankTransactionDataMap.containsKey(templateExcel.getEbankSerialNumber())){
                    exists = true;
                    HthxFundEbankTransactionDataDTO dataDTO = ebankTransactionDataMap.get(templateExcel.getEbankSerialNumber());
                    if(dataDTO!=null &&
                            (FundCurrencyTypeEnum.CNY1.getType().equals(dataDTO.getCurrencyType())
                                    || FundCurrencyTypeEnum.CNY.getType().equals(dataDTO.getCurrencyType()))){
                        onlineBankDataVO.setState(FinanceEngineEnum.offlineOnlineBankState.EXIST_CURRENCY_IS_RMB.getKey());
                    }else{
                        onlineBankDataVO.setState(FinanceEngineEnum.offlineOnlineBankState.EXIST_CURRENCY_IS_NOT_RMB.getKey());
                    }
                    // 资金网银
                    List<HthxFundEbankTransactionDataDTO> zjBankList = new ArrayList<>();
                    zjBankList.add(dataDTO);
                    onlineBankDataVO.setZjWyList(zjBankList);
                }
            }
            if(!exists){
                if(StringUtils.isNotEmpty(templateExcel.getBusinessEbankNumber())){
                    if(onlineBankDataMap.containsKey(templateExcel.getBusinessEbankNumber())){
                        HthxOnlineBankCrossCheckDataDTO onlineBankDataDTO =
                                onlineBankDataMap.get(templateExcel.getBusinessEbankNumber());
                        if(onlineBankDataDTO!=null && CollectionUtils.isNotEmpty(onlineBankDataDTO.getZjWyList())){
                            for(HthxFundEbankTransactionDataDTO dataDTO:onlineBankDataDTO.getZjWyList()){
                                if(!FundCurrencyTypeEnum.CNY1.getType().equals(dataDTO.getCurrencyType())
                                        && !FundCurrencyTypeEnum.CNY.getType().equals(dataDTO.getCurrencyType())){
                                    exists = true;
                                    onlineBankDataVO.setState(FinanceEngineEnum.offlineOnlineBankState.EXIST_CURRENCY_IS_NOT_RMB.getKey());
                                    break;
                                }
                            }
                            if(!exists){
                                onlineBankDataVO.setState(FinanceEngineEnum.offlineOnlineBankState.EXIST_CURRENCY_IS_RMB.getKey());
                            }
                        }
                        // 设置勾稽关系和资金网银
                        onlineBankDataVO.setGjInfo(onlineBankDataDTO.getGjInfo());
                        onlineBankDataVO.setZjWyList(onlineBankDataDTO.getZjWyList());
                    }
                }
            }
            if(StringUtils.isEmpty(onlineBankDataVO.getState())){
                onlineBankDataVO.setState(FinanceEngineEnum.offlineOnlineBankState.NOT_EXIST.getKey());
            }
            hthxOfflineOnlineBankDataVOList.add(onlineBankDataVO);
        }
        log.info("====>>NonConfirmCollectionSumServiceImpl.getOnlineBankInfoFromFundSystem==>>06==>>hthxOfflineOnlineBankDataVOList:{}",hthxOfflineOnlineBankDataVOList);
        return hthxOfflineOnlineBankDataVOList;
    }


    /**
     * @description: 获取线下网银是否存在
     * @author: zhangli.chen
     **/
    public List<HthxOfflineOnlineBankDataEntity>  getOfflineOnlineBankDataByNumber(HthxOfflineOnlineBankDataVO entity) {
        LambdaQueryWrapper<HthxOfflineOnlineBankDataEntity> wrapper = new LambdaQueryWrapper();
        if (StringUtils.isNotEmpty(entity.getEbankSerialNumber())) {
            wrapper.like(HthxOfflineOnlineBankDataEntity::getEbankNumber, entity.getEbankSerialNumber());
        }
        if (StringUtils.isNotEmpty(entity.getBusinessEbankNumber())) {
            wrapper.like(HthxOfflineOnlineBankDataEntity::getEbankNumber, entity.getBusinessEbankNumber());
        }
        wrapper.eq(HthxOfflineOnlineBankDataEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        return hthxOfflineOnlineBankDataMapper.selectList(wrapper);
    }

    /**
     * @description: 获取线下网银是否存在
     * @author: zhangli.chen
     **/
    public List<HthxOfflineOnlineBankDataEntity>  getOfflineOnlineBankDataByNumber(HthxFundEbankTransactionDataDTO dataDTO) {
        LambdaQueryWrapper<HthxOfflineOnlineBankDataEntity> wrapper = new LambdaQueryWrapper();
        wrapper.like(HthxOfflineOnlineBankDataEntity::getEbankNumber, dataDTO.getEbankNumber());
        wrapper.eq(HthxOfflineOnlineBankDataEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        return hthxOfflineOnlineBankDataMapper.selectList(wrapper);
    }


    /**
     * @description: 获取批扣映射表是否存在
     * @author: zhangli.chen
     **/
    public List<FundBusinessSystemEbankMappingEntity>  getBusinessSystemEbankMappingByNumber(HthxOnlineBankCrossCheckDetailDTO entity) {
        LambdaQueryWrapper<FundBusinessSystemEbankMappingEntity> wrapper = new LambdaQueryWrapper();
        wrapper.like(FundBusinessSystemEbankMappingEntity::getMatchNumber, entity.getGjbh());
        wrapper.eq(FundBusinessSystemEbankMappingEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        return fundBusinessSystemEbankMappingService.list(wrapper);
    }

    /**
     * @description: 判别勾稽关系
     * @author: zhangli.chen
     * @date 2025/08/13 14:36
     * @param: gjInfo
     * @param: newEbankMappingList
     * @return void
     **/
    private void handleCrossCheckRelation(HthxOnlineBankCrossCheckDetailDTO gjInfo,List<FundBusinessSystemEbankMappingEntity> newEbankMappingList, BatchTypeEnum batchTypeEnum){
        log.info("====>>NonConfirmCollectionSumServiceImpl.handleCrossCheckRelation==00==>>gjInfo:{},newEbankMappingList:{},batchTypeEnum:{}",gjInfo,newEbankMappingList,batchTypeEnum);
        if(gjInfo!=null){
            List<FundBusinessSystemEbankMappingEntity> mappingEntityList = getBusinessSystemEbankMappingByNumber(gjInfo);
            log.info("====>>NonConfirmCollectionSumServiceImpl.handleManualUploadData==>>05==>>mappingEntityList:{}",mappingEntityList);
            if(mappingEntityList==null || CollectionUtils.isEmpty(mappingEntityList)){
                FundBusinessSystemEbankMappingEntity newEbankMapping = new FundBusinessSystemEbankMappingEntity();
                newEbankMapping.setEbankNumber(gjInfo.getZjwybh());
                newEbankMapping.setEbankSerialNumber(gjInfo.getPcwybh());
                newEbankMapping.setMatchNumber(gjInfo.getGjbh());
                newEbankMapping.setMatchAmount(gjInfo.getGjje());
                newEbankMapping.setMessageStatus(RawMessageStatusEnum.SUCCESS.getCode());
                newEbankMapping.setCreateBy(batchTypeEnum.getCode());
                newEbankMapping.setUpdateBy(batchTypeEnum.getCode());
                newEbankMappingList.add(newEbankMapping);
            }
        }
    }

    /**
     * @description: 判别资金网银
     * @author: zhangli.chen
     * @date 2025/08/13 14:44
     * @param: zjWyList
     * @param: newOfflineOnlineBankList
     * @param: batchTypeEnum
     * @param: sceneEnum
     * @return void
     **/
    private void handleFundSystemBankRelation(List<HthxFundEbankTransactionDataDTO> zjWyList,
                                                 List<HthxOfflineOnlineBankDataEntity> newOfflineOnlineBankList
            ,BatchTypeEnum batchTypeEnum,SceneEnum sceneEnum){
        log.info("====>>NonConfirmCollectionSumServiceImpl.handleFundSystemBankRelation==00==>>" +
                "zjWyList:{},newOfflineOnlineBankList:{},batchTypeEnum:{},sceneEnum:{}",zjWyList,newOfflineOnlineBankList,batchTypeEnum,sceneEnum);
        // 2.判别资金网银
        if(CollectionUtils.isNotEmpty(zjWyList)){
            log.info("====>>NonConfirmCollectionSumServiceImpl.handleFundSystemBankRelation==>>01==>>dataVO.getZjWyList():{}",zjWyList);
            for(HthxFundEbankTransactionDataDTO dataDTO :zjWyList){
                List<HthxOfflineOnlineBankDataEntity> historyOfflineOnlineBank = getOfflineOnlineBankDataByNumber(dataDTO);
                if(CollectionUtils.isEmpty(historyOfflineOnlineBank)){
                    HthxOfflineOnlineBankDataEntity bankDataEntity = BeanUtil.copyProperties(dataDTO, HthxOfflineOnlineBankDataEntity.class);
                    if (StringUtils.isNotEmpty(bankDataEntity.getCurrencyType())) {
                        bankDataEntity.setCurrencyType(FundCurrencyTypeEnum.getEnumByType(bankDataEntity.getCurrencyType()).getCode());
                    }
                    bankDataEntity.setDelFlag(YesOrNoEnum.NO.getCode());
                    bankDataEntity.setBankAmount(new BigDecimal(0));
                    bankDataEntity.setCreateBy(batchTypeEnum.getCode());
                    bankDataEntity.setComment(sceneEnum.getDesc());
                    newOfflineOnlineBankList.add(bankDataEntity);
                }
            }
        }
    }

    /**
     * @description: 判别线下网银表
     * @author: zhangli.chen
     * @date 2025/08/13 14:58
     * @param: dataVO
     * @param: newOfflineOnlineBankList
     * @param: batchTypeEnum
     * @param: sceneEnum
     * @return void
     **/
    private void handleOfflineOnlineBankRelation(HthxOfflineOnlineBankDataVO dataVO,
                                                 List<HthxOfflineOnlineBankDataEntity> newOfflineOnlineBankList,
                                                 BatchTypeEnum batchTypeEnum,
                                                 SceneEnum sceneEnum){
        log.info("====>>NonConfirmCollectionSumServiceImpl.handleOfflineOnlineBankRelation==00==>>" +
                "dataVO:{},newOfflineOnlineBankList:{},batchTypeEnum:{},sceneEnum:{}",dataVO,newOfflineOnlineBankList,batchTypeEnum,sceneEnum);
        // 1.判别线下网银表
        List<HthxOfflineOnlineBankDataEntity> historyOfflineOnlineBank = getOfflineOnlineBankDataByNumber(dataVO);
        log.info("====>>NonConfirmCollectionSumServiceImpl.handleManualUploadData==>>02==>>historyOfflineOnlineBank:{}",historyOfflineOnlineBank);
        if(CollectionUtils.isEmpty(historyOfflineOnlineBank)){
            HthxOfflineOnlineBankDataEntity bankDataEntity = BeanUtil.copyProperties(dataVO, HthxOfflineOnlineBankDataEntity.class);
            if (StringUtils.isEmpty(bankDataEntity.getCurrencyType())) {
                bankDataEntity.setCurrencyType(FundCurrencyTypeEnum.CNY1.getCode());
            }
            if (StringUtils.isEmpty(bankDataEntity.getCollectionType())) {
                bankDataEntity.setCollectionType(CollectionTypeEnum.VC_SHOUKLX01.getCode());
            }
            if(StringUtils.isEmpty(bankDataEntity.getBusinessDate())){
                if(dataVO.getClaimDate()!=null){
                    bankDataEntity.setBusinessDate(dataVO.getClaimDate());
                }else{
                    bankDataEntity.setBusinessDate(HthxDateUtils.dateToStrShort(new Date()));
                }
            }
            bankDataEntity.setDelFlag(YesOrNoEnum.NO.getCode());
            bankDataEntity.setBankAmount(new BigDecimal(0));
            bankDataEntity.setCreateBy(batchTypeEnum.getCode());
            bankDataEntity.setComment(sceneEnum.getDesc());
            newOfflineOnlineBankList.add(bankDataEntity);
        }
    }

    /**
     * @description: 判别汇总表
     * @author: zhangli.chen
     * @date 2025/08/13 15:07
     * @param: dataVO
     * @param: companyMap
     * @param: newSumEntityList
     * @param: batchTypeEnum
     * @param: sceneEnum
     * @return NonConfirmCollectionSumEntity
     **/
    private NonConfirmCollectionSumEntity handleSumRelation(HthxOfflineOnlineBankDataVO dataVO
            ,Map<String, String> companyMap,List<NonConfirmCollectionSumEntity> newSumEntityList
            ,BatchTypeEnum batchTypeEnum,SceneEnum sceneEnum){
        log.info("====>>NonConfirmCollectionSumServiceImpl.handleSumRelation==00==>>" +
                "dataVO:{},newSumEntityList:{},batchTypeEnum:{},sceneEnum:{}",dataVO,newSumEntityList,batchTypeEnum,sceneEnum);
        // 2.判别汇总表
        NonConfirmCollectionSumEntity sumEntityParams = BeanUtil.copyProperties(dataVO, NonConfirmCollectionSumEntity.class);
        List<NonConfirmCollectionSumEntity> historySumEntity = this.getNonConfirmCollectionSumDTOByCon(sumEntityParams);
        log.info("====>>NonConfirmCollectionSumServiceImpl.handleSumRelation==>>01==>>historySumEntity:{}",historySumEntity);
        NonConfirmCollectionSumEntity newSumEntity = null;
        if (historySumEntity == null || historySumEntity.isEmpty()) {
            newSumEntity =  BeanUtil.copyProperties(dataVO, NonConfirmCollectionSumEntity.class);
            List<HthxFundEbankTransactionDataDTO> zjWyList = dataVO.getZjWyList();
            if(CollectionUtils.isNotEmpty(zjWyList)){
                String collectAccountsBankNo = zjWyList.stream().map(
                        HthxFundEbankTransactionDataDTO::getCollectionAccountsBankNo).distinct().collect(Collectors.joining(","));
                List<BankAccountEntity> bankAccountList = bankAccountService.selectBankAccountEntity(collectAccountsBankNo);
                if (bankAccountList != null && !bankAccountList.isEmpty()) {
                    String collectionAccountsBank = bankAccountList.stream().map(e -> companyMap.get(e.getOrgId())).
                            collect(Collectors.joining(","));
                    newSumEntity.setCollectionAccountsBank(collectionAccountsBank);
                    newSumEntity.setCollectionAccountsBankCode(bankAccountList.stream().map(e -> e.getOrgId()).collect(Collectors.joining(",")));
                }
                newSumEntity.setCollectionAccountsBankNo(collectAccountsBankNo);
            }else{
                newSumEntity.setCollectionAccountsBank(dataVO.getOrgName());
                newSumEntity.setCollectionAccountsBankCode(dataVO.getOrgId());
            }
            newSumEntity.setBankAmount(new BigDecimal(0));
            if(dataVO.getClaimDate()!=null){
                newSumEntity.setBusinessDate(DateUtil.parse(dataVO.getClaimDate()).toLocalDateTime());
            }else{
                newSumEntity.setBusinessDate(DateUtil.toLocalDateTime(new Date()));
            }
            if(StringUtils.isNotEmpty(newSumEntity.getEbankSerialNumber())){
                newSumEntity.setEbankSerialNumber(newSumEntity.getEbankNumber());
            }
            if(StringUtils.isNotEmpty(newSumEntity.getBusinessEbankNumber())){
                newSumEntity.setBusinessEbankNumber(newSumEntity.getEbankNumber());
            }
            newSumEntity.setSystemCode(SystemEnum.CWZT.getCode());
            newSumEntity.setDelFlag(YesOrNoEnum.NO.getCode());
            newSumEntity.setCreateBy(batchTypeEnum.getCode());
            newSumEntity.setUpdateBy(batchTypeEnum.getCode());
            newSumEntity.setId(IdWorker.getId());
            newSumEntity.setCurrencyType(FundCurrencyTypeEnum.CNY1.getCode());
            newSumEntity.setComment(sceneEnum.getDesc());
            newSumEntityList.add(newSumEntity);
        }else{
            newSumEntity = historySumEntity.get(0);
        }
        return newSumEntity;
    }

    /**
     * @description: 判别未确认收款明细表
     * @author: zhangli.chen
     * @date 2025/08/13 15:19
     * @param: newSumEntity
     * @param: nonConfirmCollectionSecondDetailList
     * @param: dataVO
     * @param: batchTypeEnum
     * @param: sceneEnum
     * @return void
     **/
    private Long handleNonConfirmDetailRelation(NonConfirmCollectionSumEntity newSumEntity
            ,List<NonConfirmCollectionSecondDetailEntity> nonConfirmCollectionSecondDetailList
            ,BigDecimal curClaimAmount,BatchTypeEnum batchTypeEnum,SceneEnum sceneEnum,Long approveId,Long uploadFileId){
        log.info("====>>NonConfirmCollectionSumServiceImpl.handleNonConfirmDetailRelation==00==>>" +
                "newSumEntity:{},nonConfirmCollectionSecondDetailList:{},curClaimAmount:{},batchTypeEnum:{},sceneEnum:{},approveId:{},uploadFileId:{}"
                ,newSumEntity,nonConfirmCollectionSecondDetailList,curClaimAmount,batchTypeEnum,sceneEnum,approveId,uploadFileId);
        // 3.判别未确认收款明细表
        NonConfirmCollectionSecondDetailEntity nonConfirmCollectionSecondDetail = new NonConfirmCollectionSecondDetailEntity();
        Long detailId = IdWorker.getId();
        nonConfirmCollectionSecondDetail.setId(detailId);
        nonConfirmCollectionSecondDetail.setSumId(newSumEntity.getId());
        nonConfirmCollectionSecondDetail.setSystemCode(SystemEnum.CWZT.getCode());
        nonConfirmCollectionSecondDetail.setSystemName(SystemEnum.CWZT.getDesc());
        nonConfirmCollectionSecondDetail.setBusinessDate(newSumEntity.getBusinessDate());
        nonConfirmCollectionSecondDetail.setBusinessHappenDate(LocalDateTime.now());
        nonConfirmCollectionSecondDetail.setCurrencyType(newSumEntity.getCurrencyType());
        nonConfirmCollectionSecondDetail.setBankAmount(newSumEntity.getBankAmount());
        nonConfirmCollectionSecondDetail.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
        nonConfirmCollectionSecondDetail.setApproveId(approveId);
        if(sceneEnum!=null){
            nonConfirmCollectionSecondDetail.setOperationType(sceneEnum.getCode());
        }
        // 借方发生额以导入数据为准
        nonConfirmCollectionSecondDetail.setCurClaimAmount(curClaimAmount);
        nonConfirmCollectionSecondDetail.setIsRelateClientAuxiliaryAccount(YesOrNoEnum.NO.getCode());
        nonConfirmCollectionSecondDetail.setVoucherIds(null);
        if(batchTypeEnum!=null){
            nonConfirmCollectionSecondDetail.setCreateBy(batchTypeEnum.getCode());
        }
        nonConfirmCollectionSecondDetail.setUploadFileId(uploadFileId);
        nonConfirmCollectionSecondDetailList.add(nonConfirmCollectionSecondDetail);
        return detailId;
    }

    /**
     * @description:判别未确认收款认领表
     * @author: zhangli.chen
     * @date 2025/08/13 15:30
     * @param: newSumEntity
     * @param: claimRecordList
     * @param: dataVO
     * @param: detailId
     * @param: batchTypeEnum
     * @param: sceneEnum
     * @return void
     **/
    private void handleNonConfirmClaimRelation(NonConfirmCollectionSumEntity newSumEntity,List<BusinessClaimRepaymentRecordEntity> claimRecordList
            ,HthxOfflineOnlineBankDataVO dataVO,Long detailId,BatchTypeEnum batchTypeEnum,SceneEnum sceneEnum){
        log.info("====>>NonConfirmCollectionSumServiceImpl.handleNonConfirmClaimRelation==00==>>" +
                "newSumEntity:{},claimRecordList:{},dataVO:{},detailId:{},batchTypeEnum:{},sceneEnum:{}",
                newSumEntity,claimRecordList,dataVO,detailId,batchTypeEnum,sceneEnum);
        // 5.判别未确认收款认领表
        BusinessClaimRepaymentRecordEntity entity = new BusinessClaimRepaymentRecordEntity();
        entity.setId(IdWorker.getId());
        entity.setOrgId(newSumEntity.getCollectionAccountsBankCode());
        entity.setSystemCode(SystemEnum.CWZT.getCode());
        entity.setClaimAmount(dataVO.getClaimAmount());
        entity.setClientCode(newSumEntity.getClientCode());
        entity.setCurrencyType(newSumEntity.getCurrencyType());
        entity.setBusinessDate(DateUtil.parse(dataVO.getClaimDate()).toLocalDateTime());
        log.info("====>>NonConfirmCollectionSumServiceImpl.handleNonConfirmClaimRelation==01==>>" +
                        "dataVO.getEbankSerialNumber():{}", dataVO.getEbankSerialNumber());
        if (StringUtils.isNotEmpty(dataVO.getEbankSerialNumber())) {
            entity.setEbankSerialNumber(dataVO.getEbankSerialNumber());
        } else {
            List<HyFullOnlineBankBatchNoMappingEntity> hyList = hyFullOnlineBankBatchNoMappingService.
                    selectBusinessOnlineBankNoBatchNoMapping(dataVO.getBusinessEbankNumber());
            log.info("====>>NonConfirmCollectionSumServiceImpl.handleNonConfirmClaimRelation==02==>>" +
                    "hyList:{}", hyList);
            if (hyList == null || hyList.isEmpty() || StringUtils.isEmpty(hyList.get(0).getDeductBatchNo())) {
                entity.setEbankSerialNumber(dataVO.getBusinessEbankNumber());
            } else {
                entity.setEbankSerialNumber(hyList.get(0).getDeductBatchNo());
            }
        }
        int maxBatchNo = 0;
        if(StringUtils.isNotEmpty(newSumEntity.getEbankSerialNumber())) {
            log.info("====>>NonConfirmCollectionSumServiceImpl.handleNonConfirmClaimRelation==03==>>" +
                    "newSumEntity.getEbankSerialNumber():{}", newSumEntity.getEbankSerialNumber());
            maxBatchNo = businessClaimRepaymentRecordService.getMaxBatchNo(newSumEntity.getEbankSerialNumber());
            log.info("====>>NonConfirmCollectionSumServiceImpl.handleNonConfirmClaimRelation==04==>>" +
                    "maxBatchNo:{}", maxBatchNo);
        }
        maxBatchNo = maxBatchNo + 1;
        entity.setBatchNo(new BigDecimal(maxBatchNo));
        entity.setNonConfirmSecondDetailId(detailId);
        log.info("====>>NonConfirmCollectionSumServiceImpl.handleNonConfirmClaimRelation==05==>>" +
                "sceneEnum:{}", sceneEnum);
        if(sceneEnum!=null){
            entity.setRemark(sceneEnum.getDesc());
            entity.setSceneCode(sceneEnum.getCode());
            entity.setSceneName(sceneEnum.getDesc());
            entity.setOperationType(sceneEnum.getCode());
        }
        entity.setIsCrossOrg(YesOrNoEnum.NO.getCode());
        entity.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
        //entity.setProcessStatus(ProcessStatusEnum.REVIEWED.getCode());
        log.info("====>>NonConfirmCollectionSumServiceImpl.handleNonConfirmClaimRelation==06==>>" +
                "batchTypeEnum:{}", batchTypeEnum);
        if(batchTypeEnum!=null){
            entity.setCreateBy(batchTypeEnum.getCode());
        }
        claimRecordList.add(entity);
        log.info("====>>NonConfirmCollectionSumServiceImpl.handleNonConfirmClaimRelation==07==>>" +
                "claimRecordList:{}", claimRecordList);
    }





}

