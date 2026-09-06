package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.BusinessClaimRepaymentRecordEntity;
import com.utfinancing.financehub.engine.finance.entity.NonConfirmCollectionSumEntity;
import com.utfinancing.financehub.engine.finance.entity.VoucherEntryEntity;
import com.utfinancing.financehub.engine.finance.mapper.NonConfirmCollectionSumMapper;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.NonConfirmCollectionSecondDetailVO;
import com.utfinancing.financehub.engine.finance.entity.NonConfirmCollectionSecondDetailEntity;
import com.utfinancing.financehub.engine.finance.mapper.NonConfirmCollectionSecondDetailMapper;
import com.utfinancing.financehub.engine.finance.model.vo.VoucherVO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.hthx.common.enums.FinanceEngineEnum;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author : robjiang
 * @Date : Create in 2024-03-22
 * @Description :  NonConfirmCollectionSecondDetail服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class NonConfirmCollectionSecondDetailServiceImpl extends ServiceImpl<NonConfirmCollectionSecondDetailMapper,
        NonConfirmCollectionSecondDetailEntity> implements INonConfirmCollectionSecondDetailService {

    private final NonConfirmCollectionSecondDetailMapper nonConfirmCollectionSecondDetailMapper;

    private final IBusinessClaimRepaymentRecordService businessClaimRepaymentRecordService;

    private final IManualService manualService;

    private final IVoucherService voucherService;

    private final IVoucherEntryService voucherEntryService;

    private final NonConfirmCollectionSumMapper nonConfirmCollectionSumMapper;

    private final IApproveService approveService;

    @Override
    public Long saveNonConfirmCollectionSecondDetail(NonConfirmCollectionSecondDetailDTO dto) {
        NonConfirmCollectionSecondDetailEntity entity = BeanUtil.copyProperties(dto, NonConfirmCollectionSecondDetailEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateNonConfirmCollectionSecondDetail(Long id, NonConfirmCollectionSecondDetailDTO dto) {
        NonConfirmCollectionSecondDetailEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public NonConfirmCollectionSecondDetailDTO getNonConfirmCollectionSecondDetailDTOById(Long id) {
        NonConfirmCollectionSecondDetailEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, NonConfirmCollectionSecondDetailDTO.class);
    }

    /**
     * 根据条件查询未确认收款明细记录
     */
    public List<NonConfirmCollectionSecondDetailEntity>  getNonConfirmCollectionDetailByCon(
            Long sumId, Date incomeDateOld) {
        LambdaQueryWrapper<NonConfirmCollectionSecondDetailEntity> wrapper = new LambdaQueryWrapper();
        wrapper.eq(NonConfirmCollectionSecondDetailEntity::getSumId, sumId);
        if (incomeDateOld != null) {
            String incomeDateOldYM = DateUtils.parseDateToStr( "yyyy-MM", incomeDateOld);
            wrapper.apply("to_char(business_happen_date, 'yyyy-MM') = {0}", incomeDateOldYM);
        }
        wrapper.eq(NonConfirmCollectionSecondDetailEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        return nonConfirmCollectionSecondDetailMapper.selectList(wrapper);
    }

    @Override
    public IPage<NonConfirmCollectionSecondDetailVO> selectPage(NonConfirmCollectionSecondDetailQueryDTO queryDTO) {
        LambdaQueryWrapper<NonConfirmCollectionSecondDetailEntity> queryWrapper = Wrappers.<NonConfirmCollectionSecondDetailEntity>lambdaQuery();
        //这里注入查询条件
        IPage<NonConfirmCollectionSecondDetailEntity> entityIPage = nonConfirmCollectionSecondDetailMapper.
                selectPage(new Page<NonConfirmCollectionSecondDetailEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, NonConfirmCollectionSecondDetailVO.class);
    }

    /**
     * 审核通过后处理
     */
    @Override
    public void auditPass(CommonApproveDTO approveDTO) {
        if (approveDTO.getDocumentId() == null) {
            return;
        }

        // 明细记录查询
        NonConfirmCollectionSecondDetailEntity detailEntity = nonConfirmCollectionSecondDetailMapper.selectById(approveDTO.getDocumentId());
        if (detailEntity == null) {
            // 批量认领时，是通过uploadfileid进行审批的
            LambdaQueryWrapper<NonConfirmCollectionSecondDetailEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(NonConfirmCollectionSecondDetailEntity::getUploadFileId, approveDTO.getDocumentId());
            List<NonConfirmCollectionSecondDetailEntity> entityList = nonConfirmCollectionSecondDetailMapper.selectList(wrapper);
            if (entityList == null || entityList.isEmpty()) {
                throw new ServiceException("未确认收款明细记录不存在，id：".concat(String.valueOf(approveDTO.getDocumentId())));
            } else {
                detailEntity = entityList.get(0);
            }
        }

        // 更新操作记录的数据
        this.updateDetailDataForApproved(approveDTO, detailEntity);
        // 更新认领记录的状态
        businessClaimRepaymentRecordService.processStatusUpdate(detailEntity.getId(), ProcessStatusEnum.REVIEWED.getCode());

        // 查询是否存在关联的approveId-存在则同步更新detail 记录
        LambdaQueryWrapper<NonConfirmCollectionSecondDetailEntity> wrapper = new LambdaQueryWrapper();
        wrapper.eq(NonConfirmCollectionSecondDetailEntity::getApproveId, detailEntity.getApproveId());
        wrapper.eq(NonConfirmCollectionSecondDetailEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        List<NonConfirmCollectionSecondDetailEntity> nonConfirmCollectionSecondDetailEntityList = nonConfirmCollectionSecondDetailMapper.selectList(wrapper);
        if (nonConfirmCollectionSecondDetailEntityList != null && !nonConfirmCollectionSecondDetailEntityList.isEmpty()) {
            for (NonConfirmCollectionSecondDetailEntity entity : nonConfirmCollectionSecondDetailEntityList) {
                if (entity.getId().longValue() != detailEntity.getId().longValue()) {
                    this.updateDetailDataForApproved(approveDTO, entity);
                    businessClaimRepaymentRecordService.processStatusUpdate(entity.getId(), ProcessStatusEnum.REVIEWED.getCode());
                }
            }
        }
    }

    /**
     * 更新明细数据及凭证状态
     */
    private void updateDetailDataForApproved(CommonApproveDTO approveDTO, NonConfirmCollectionSecondDetailEntity detailEntity) {

        // 未确认认领记录
        NonConfirmCollectionSumEntity nonConfirmCollectionSumEntity = nonConfirmCollectionSumMapper.selectById(detailEntity.getSumId());

        // 已经认领金额汇总
        List<BusinessClaimRepaymentRecordEntity> businessClaimRepaymentRecordEntityList = businessClaimRepaymentRecordService.
                selectClaimRecordByDeductBatchNo(nonConfirmCollectionSumEntity.getEbankSerialNumber(), ProcessStatusEnum.getValidCode(), null, null);
        BigDecimal readyClaimAmount = businessClaimRepaymentRecordService.getReadyClaimAmount(
                businessClaimRepaymentRecordEntityList);

        // 当前认领金额
        List<BusinessClaimRepaymentRecordEntity> curClaimRecordEntityList = businessClaimRepaymentRecordService.
                selectClaimRecordBySecondDetailId(String.valueOf(detailEntity.getId()), null);
        BigDecimal curClaimAmount = businessClaimRepaymentRecordService.getReadyClaimAmount(curClaimRecordEntityList);

        // 认领记录状态更新
        detailEntity.setProcessStatus(ProcessStatusEnum.REVIEWED.getCode());
        detailEntity.setAuditor(approveDTO.getApproverNum());
        detailEntity.setAuditorName(approveDTO.getApproverName());
        // 计算剩余未确认收款的金额- 修改入账日期，不能减当前认领金额
        if (SceneEnum.SGPZMGRZRQ.getCode().equals(approveDTO.getDocumentType())) {
            detailEntity.setRemainNonConfirmAmount(nonConfirmCollectionSumEntity.getBankAmount().subtract(readyClaimAmount));
            detailEntity.setConfirmAmount(readyClaimAmount);
        } else {
            detailEntity.setRemainNonConfirmAmount(nonConfirmCollectionSumEntity.getBankAmount().subtract(readyClaimAmount).subtract(curClaimAmount));
            // 计算截止当前的已确认认领的金额
            detailEntity.setConfirmAmount(readyClaimAmount.add(curClaimAmount));
        }
        detailEntity.setApproveTime(LocalDateTime.now());
        detailEntity.setUpdateTime(LocalDateTime.now());
        detailEntity.setUpdateBy(approveDTO.getApproverNum());
        this.updateById(detailEntity);

        // 手工凭证状态更新
        if (StringUtils.isNotEmpty(detailEntity.getManualVoucherIds())) {
            List<String> manualVoucherIds = Arrays.asList(detailEntity.getManualVoucherIds().split(","));
            manualService.updateStatusByids(manualVoucherIds, ProcessStatusEnum.REVIEWED.getCode(),
                    approveDTO.getApproverNum(), approveDTO.getApproverName());
            List<Long> manualVoucherIdList = manualVoucherIds.stream().map(Long::valueOf).collect(Collectors.toList());

            // 更新正式凭证的状态
            List<VoucherVO> voucherVOList = voucherService.getByBatchIdList(manualVoucherIdList, BatchTypeEnum.SGPZ.getCode());
            List<String> voucherIds = voucherVOList.stream().map(e->String.valueOf(e.getId())).collect(Collectors.toList());
            voucherService.updateStatusByids(voucherIds, ProcessStatusEnum.REVIEWED.getCode(),
                    approveDTO.getApproverNum(), approveDTO.getApproverName());

//            // 复核通过的手工凭证需要生成到voucher表
//            if (manualVoucherIds != null && !manualVoucherIds.isEmpty()) {
//                for (String manualId : manualVoucherIds) {
//                    manualService.generateVoucher(Long.parseLong(manualId), approveDTO.getApproverName(), approveDTO.getApproverNum(),Boolean.FALSE,ManualServiceImpl.NON_CONFIRM_COLLECTION_SECOND_DETAIL);
//                }
//            }
        }

        // 核销回款凭证的状态更新
        if (StringUtils.isNotEmpty(detailEntity.getWriteOffVoucherId())) {
            List<String> voucherIds = Arrays.asList(detailEntity.getWriteOffVoucherId().split(","));
            voucherService.updateStatusByids(voucherIds, ProcessStatusEnum.REVIEWED.getCode(),
                    approveDTO.getApproverNum(), approveDTO.getApproverName());
        }

        // 凭证状态更新
        if (StringUtils.isNotEmpty(detailEntity.getVoucherIds())) {
            List<String> voucherIds = Arrays.asList(detailEntity.getVoucherIds().split(","));
            voucherService.updateStatusByids(voucherIds, ProcessStatusEnum.REVIEWED.getCode(), approveDTO.getApproverNum(), approveDTO.getApproverName());
        }
    }



    /**
     * 审核通过后处理-手工调整余额
     */
    @Override
    public void auditPassForSGTZYE(CommonApproveDTO approveDTO) {
        if (approveDTO.getDocumentId() == null) {
            return;
        }
        LambdaQueryWrapper<NonConfirmCollectionSecondDetailEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NonConfirmCollectionSecondDetailEntity::getUploadFileId, approveDTO.getDocumentId());
        List<NonConfirmCollectionSecondDetailEntity> entityList = nonConfirmCollectionSecondDetailMapper.selectList(wrapper);
        if (entityList == null || entityList.isEmpty()) {
            throw new ServiceException("未确认收款明细记录不存在，uploadFileId：".concat(String.valueOf(approveDTO.getDocumentId())));
        }

        entityList.stream().forEach(e -> {
            // 修改审核状态
            e.setProcessStatus(ProcessStatusEnum.REVIEWED.getCode());
            e.setAuditor(approveDTO.getApproverName());
            e.setAuditorName(approveDTO.getApproverNum());
            e.setApproveTime(LocalDateTime.now());

            // 更新认领记录
            businessClaimRepaymentRecordService.processStatusUpdate(e.getId(), ProcessStatusEnum.REVIEWED.getCode());
        });

        this.saveOrUpdateBatch(entityList);
    }


    /**
     * 审核失败后处理
     */
    @Override
    public void auditFailed(CommonApproveDTO approveDTO) {
        if (approveDTO.getDocumentId() == null) {
            return;
        }

        NonConfirmCollectionSecondDetailEntity detailEntity = nonConfirmCollectionSecondDetailMapper.
                selectById(approveDTO.getDocumentId());
        if (detailEntity == null) {
            // 批量认领时，是通过uploadfileid进行审批的
            LambdaQueryWrapper<NonConfirmCollectionSecondDetailEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(NonConfirmCollectionSecondDetailEntity::getUploadFileId, approveDTO.getDocumentId());
            List<NonConfirmCollectionSecondDetailEntity> entityList = nonConfirmCollectionSecondDetailMapper.selectList(wrapper);
            if (entityList == null || entityList.isEmpty()) {
                throw new ServiceException("未确认收款明细记录不存在，id：".concat(String.valueOf(approveDTO.getDocumentId())));
            } else {
                detailEntity = entityList.get(0);
            }
        }

        // 更新明细记录
        this.updateDetailDataForReject(approveDTO, detailEntity);

        // 更新认领记录的状态
        businessClaimRepaymentRecordService.processStatusUpdate(detailEntity.getId(), ProcessStatusEnum.REJECTED.getCode());

        // 查询是否存在关联的approveId-存在则同步更新detail 记录
        LambdaQueryWrapper<NonConfirmCollectionSecondDetailEntity> wrapper = new LambdaQueryWrapper();
        wrapper.eq(NonConfirmCollectionSecondDetailEntity::getApproveId, detailEntity.getApproveId());
        wrapper.eq(NonConfirmCollectionSecondDetailEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        List<NonConfirmCollectionSecondDetailEntity> nonConfirmCollectionSecondDetailEntityList = nonConfirmCollectionSecondDetailMapper.selectList(wrapper);
        if (nonConfirmCollectionSecondDetailEntityList != null && !nonConfirmCollectionSecondDetailEntityList.isEmpty()) {
            for (NonConfirmCollectionSecondDetailEntity entity : nonConfirmCollectionSecondDetailEntityList) {
                if (entity.getId().longValue() != detailEntity.getId().longValue()) {
                    this.updateDetailDataForReject(approveDTO, detailEntity);
                    businessClaimRepaymentRecordService.processStatusUpdate(entity.getId(), ProcessStatusEnum.REJECTED.getCode());
                }
            }
        }
    }

    private void updateDetailDataForReject(CommonApproveDTO approveDTO, NonConfirmCollectionSecondDetailEntity detailEntity) {

        // 认领记录状态更新
        LambdaUpdateWrapper<NonConfirmCollectionSecondDetailEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(NonConfirmCollectionSecondDetailEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        updateWrapper.eq(NonConfirmCollectionSecondDetailEntity::getId, detailEntity.getId());
        updateWrapper.set(NonConfirmCollectionSecondDetailEntity::getProcessStatus, ProcessStatusEnum.REJECTED.getCode());
        updateWrapper.set(NonConfirmCollectionSecondDetailEntity::getAuditor, approveDTO.getApproverNum());
        updateWrapper.set(NonConfirmCollectionSecondDetailEntity::getAuditorName, approveDTO.getApproverName());
        this.update(updateWrapper);

        if (StringUtils.isNotEmpty(detailEntity.getManualVoucherIds())) {
            List<String> manualVoucherIds = Arrays.asList(detailEntity.getManualVoucherIds().split(","));
            manualService.updateStatusByids(manualVoucherIds, ProcessStatusEnum.REJECTED.getCode(),
                    approveDTO.getApproverNum(), approveDTO.getApproverName());
            List<Long> manualVoucherIdList = manualVoucherIds.stream().map(Long::valueOf).collect(Collectors.toList());

            // 更新正式凭证的状态
            List<VoucherVO> voucherVOList = voucherService.getByBatchIdList(manualVoucherIdList, BatchTypeEnum.SGPZ.getCode());
            List<String> voucherIds = voucherVOList.stream().map(e->String.valueOf(e.getId())).collect(Collectors.toList());
            voucherService.updateStatusByids(voucherIds, ProcessStatusEnum.REJECTED.getCode(),
                    approveDTO.getApproverNum(), approveDTO.getApproverName());

            // 核销回款凭证的状态更新
            if (StringUtils.isNotEmpty(detailEntity.getWriteOffVoucherId())) {
                List<String> writeOffVoucherIds = Arrays.asList(detailEntity.getWriteOffVoucherId().split(","));
                voucherService.updateStatusByids(writeOffVoucherIds, ProcessStatusEnum.REJECTED.getCode(),
                        approveDTO.getApproverNum(), approveDTO.getApproverName());
            }
        }

        // 凭证状态更新
        if (StringUtils.isNotEmpty(detailEntity.getVoucherIds())) {
            List<String> voucherIds = Arrays.asList(detailEntity.getVoucherIds().split(","));
            voucherService.updateStatusByids(voucherIds, ProcessStatusEnum.REJECTED.getCode(),
                    approveDTO.getApproverName(), approveDTO.getApproverNum());
        }
    }


    /**
     * 审核拒绝后处理
     */
    public void auditFailedForSGTZYE(CommonApproveDTO approveDTO) {
        if (approveDTO.getDocumentId() == null) {
            return;
        }
        LambdaQueryWrapper<NonConfirmCollectionSecondDetailEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NonConfirmCollectionSecondDetailEntity::getUploadFileId, approveDTO.getDocumentId());
        List<NonConfirmCollectionSecondDetailEntity> entityList = nonConfirmCollectionSecondDetailMapper.selectList(wrapper);
        if (entityList == null || entityList.isEmpty()) {
            throw new ServiceException("未确认收款明细记录不存在，uploadFileId：".concat(String.valueOf(approveDTO.getDocumentId())));
        }

        entityList.stream().forEach(e -> {
            // 修改审核状态
            e.setProcessStatus(ProcessStatusEnum.REJECTED.getCode());
            e.setAuditor(approveDTO.getApproverName());
            e.setAuditorName(approveDTO.getApproverNum());
            e.setApproveTime(LocalDateTime.now());

            // 更新认领记录
            businessClaimRepaymentRecordService.processStatusUpdate(e.getId(), ProcessStatusEnum.REJECTED.getCode());
        });
        this.saveOrUpdateBatch(entityList);
    }

    /**
     * 查询手工处理数据
     */
    public List<ManualProcessListDTO> queryManualProcess(QueryManualProcessDTO params) {
        if (StringUtils.isEmpty(params.getSumId()) && StringUtils.isEmpty(params.getDetailId())) {
            return new ArrayList<>();
        }

        // 手工处理页面数据查询
        List<ManualProcessListDTO> manualProcessListDTOList = nonConfirmCollectionSecondDetailMapper.
                selectManualProcessData(params);
        if (manualProcessListDTOList == null || manualProcessListDTOList.isEmpty()) {
            return new ArrayList<>();
        }

        manualProcessListDTOList.stream().forEach(e -> {
            e.setOperationType(ClaimOperationTypeEnum.valueOf(e.getOperationType()).getDesc());
            if (StringUtils.isEmpty(e.getWriteOffVoucherId())) {
                e.setWriteOffVoucherId(e.getVoucherIds());
            }
        });
        return manualProcessListDTOList;
    }

    /**
     * @description: 未确认收款列表-查看详情
     **/
    public List<QueryDetailListDataDTO> queryDetailPageData(QueryDetailDataDTO params) {
        List<QueryDetailListDataDTO> listData = new ArrayList<>();
        if (params.getSumId() == null) {
            return new ArrayList<>();
        }
        List<QueryDetailListDataSubDTO> queryDetailListDataSubDTOList = nonConfirmCollectionSecondDetailMapper.selectDetailData(params);
        if (queryDetailListDataSubDTOList == null || queryDetailListDataSubDTOList.isEmpty()) {
            return new ArrayList<>();
        }
        // 业务系统自动认领的金额
        BigDecimal autoClaimAmount = queryDetailListDataSubDTOList.stream().
                filter(e->e.getBatchNo() != null && new BigDecimal(1).compareTo(e.getBatchNo()) == 0).
                map(QueryDetailListDataSubDTO::getConfirmedAmount).
                reduce(BigDecimal.ZERO, BigDecimal::add);

        // 待添加的对象
        QueryDetailListDataDTO queryDetailListDataDTO = null;
        QueryDetailListDataSubDTO lastQueryDetailListDataSubDTO = null;   // 循环的上一次对象
        List<EbankConfirmRecordDTO> ebankConfirmRecordList = new ArrayList<>();
        for (int i = 0; i < queryDetailListDataSubDTOList.size(); i++) {
            QueryDetailListDataSubDTO dto = queryDetailListDataSubDTOList.get(i);
            if (i == 0 || dto.getBatchNo().compareTo(lastQueryDetailListDataSubDTO.getBatchNo()) != 0) {
                if (i != 0) {
                    listData.add(queryDetailListDataDTO);
                }
                queryDetailListDataDTO = this.createQueryDetailListDataDTO(dto, autoClaimAmount);
            }
            // 认领主体
            if (StringUtils.isEmpty(queryDetailListDataDTO.getOrgId())) {
                queryDetailListDataDTO.setOrgId(dto.getOrgId());
                queryDetailListDataDTO.setOrgName(dto.getOrgName());
            } else if (!queryDetailListDataDTO.getOrgId().contains(dto.getOrgId())) {
                queryDetailListDataDTO.setOrgId(queryDetailListDataDTO.getOrgId().concat(",").concat(dto.getOrgId()));
                queryDetailListDataDTO.setOrgName(queryDetailListDataDTO.getOrgName().concat(",").concat(dto.getOrgName()));
            }
            // 业务系统
            if (StringUtils.isEmpty(queryDetailListDataDTO.getSystemCode())) {
                queryDetailListDataDTO.setSystemCode(dto.getSystemCode());
//                queryDetailListDataDTO.setSystemName(SystemEnum.getDescByCode(dto.getSystemCode()));
            } else if (!queryDetailListDataDTO.getSystemCode().contains(dto.getSystemCode())) {
                queryDetailListDataDTO.setSystemCode(queryDetailListDataDTO.getSystemCode().
                        concat(FinanceEngineEnum.Symbol.COMMA.getValue()).concat(dto.getSystemCode()));
//                queryDetailListDataDTO.setSystemName(queryDetailListDataDTO.getSystemName().
//                        concat(",").concat(SystemEnum.getDescByCode(dto.getSystemCode())));
            }
            // 设置网银确认记录
            if (dto.getBatchNo() != null) {
                EbankConfirmRecordDTO ebankConfirmRecordDTO = this.createEbankConfirmRecordDTO(dto);
                if (ebankConfirmRecordDTO != null) {
                    ebankConfirmRecordList.add(ebankConfirmRecordDTO);
                }
            }
            // 设置上一次循环对象
            lastQueryDetailListDataSubDTO = dto;
        }
        if (queryDetailListDataDTO != null) {
            // 业务系统
            if (StringUtils.isNotEmpty(queryDetailListDataDTO.getSystemCode())) {
                List<String> systemCodeList = Arrays.asList(queryDetailListDataDTO.getSystemCode().split(FinanceEngineEnum.Symbol.COMMA.getValue()));
                StringJoiner joiner = new StringJoiner(FinanceEngineEnum.Symbol.COMMA.getValue());
                for (String systemCode : systemCodeList) {
                    joiner.add(SystemEnum.getDescByCode(systemCode));
                }
                queryDetailListDataDTO.setSystemCode(joiner.toString());
            }
            listData.add(queryDetailListDataDTO);
        }
        // 设置网银确认记录
        for (QueryDetailListDataDTO dto : listData) {
            if (ebankConfirmRecordList != null && !ebankConfirmRecordList.isEmpty()) {
                for (int i = ebankConfirmRecordList.size() - 1; i >= 0; i--) {
                    if (ebankConfirmRecordList.get(i).getApproveTime().compareTo(dto.getApproveTime()) <= 0) {
                        dto.getEbankConfirmRecord().add(ebankConfirmRecordList.get(i));
                    }
                }
            }
        }
        return listData;
    }

    /**
     * 创建网银确认记录对象
     */
    private EbankConfirmRecordDTO createEbankConfirmRecordDTO(QueryDetailListDataSubDTO dto) {
        if (dto.getBatchNo() == null) {
            return null;
        }

        EbankConfirmRecordDTO result = new EbankConfirmRecordDTO();
        if (new BigDecimal(1).compareTo(dto.getBatchNo()) == 0) {
            result.setEbankConfirmDate(DateUtils.parseDateToStr( "yyyy-MM-dd", DateUtils.toDate(dto.getBusinessDate())));
        } else {
            if (dto.getBusinessHappenDate() != null) {
                result.setEbankConfirmDate(DateUtils.parseDateToStr("yyyy-MM-dd", DateUtils.toDate(dto.getBusinessHappenDate())));
            }
        }
        result.setContractCode(dto.getContractCode());
        result.setEbankConfirmComments(dto.getSceneName());
        result.setConfirmAmount(dto.getClaimAmount());
        result.setBatchNo(dto.getBatchNo().intValue());
        result.setApproveTime(dto.getApproveTime());
        return result;
    }

    /**
     * 创建QueryDetailListDataDTO对象
     */
    private QueryDetailListDataDTO createQueryDetailListDataDTO(QueryDetailListDataSubDTO dto, BigDecimal autoClaimAmount) {
        QueryDetailListDataDTO result = new QueryDetailListDataDTO();
        result.setCollectionAccountsBank(dto.getCollectionAccountsBank());
        result.setEbankNumber(dto.getEbankNumber());
        result.setEbankSerialNumber(dto.getEbankSerialNumber());
        /**
         * @description: 新增显示字段-业务系统的网银编号
         **/
        result.setBusinessEbankNumber(dto.getBusinessEbankNumber());
        result.setCollectionAccountsBankNo(dto.getCollectionAccountsBankNo());
        result.setBusinessHappenDate(dto.getBusinessHappenDate());
        result.setBusinessDate(dto.getBusinessDate());
        result.setCurrencyType(dto.getCurrencyType());
        result.setBankAmount(dto.getBankAmount());
        result.setApproveTime(dto.getApproveTime());
        if (dto.getBatchNo() != null) {
            result.setBatchNo(dto.getBatchNo().intValue());
        }
        if (dto.getBatchNo() != null && new BigDecimal(1).compareTo(dto.getBatchNo()) == 0) {
            result.setRemainNonConfirmAmount(dto.getBankAmount().subtract(autoClaimAmount));
            result.setConfirmedAmount(autoClaimAmount);
        } else {
            result.setRemainNonConfirmAmount(dto.getRemainNonConfirmAmount());
            result.setConfirmedAmount(dto.getBankAmount().subtract(dto.getRemainNonConfirmAmount()));
        }
        return result;
    }

    /**
     * @description: 未确认收款列表-查看详情-查看明细-第三层详情页面数据查询
     **/
    public List<QueryThirdDetailPageListDataDTO> queryThirdDetailPageData(QueryThirdDetailPageDataDTO params) {
        // 优先按照恒运业务进行查询
        List<QueryThirdDetailPageListDataDTO> queryThirdDetailPageListDataDTOList =
                nonConfirmCollectionSecondDetailMapper.selectThirdDetailForHy(params);
        // 查询为空时，按照其它业务进行查询第三层明细数据
        if (queryThirdDetailPageListDataDTOList == null || queryThirdDetailPageListDataDTOList.isEmpty()) {
            queryThirdDetailPageListDataDTOList = nonConfirmCollectionSecondDetailMapper.selectThirdDetailForOther(params);
            if (queryThirdDetailPageListDataDTOList == null || queryThirdDetailPageListDataDTOList.isEmpty()) {
                return new ArrayList<>();
            }
        }

        for (QueryThirdDetailPageListDataDTO dto : queryThirdDetailPageListDataDTOList) {
            // 设置网银确认记录
            List<BusinessClaimRepaymentRecordEntity> bcrrList = businessClaimRepaymentRecordService.
                    selectClaimRecordByDeductBatchNo(dto.getEbankSerialNumber(), ProcessStatusEnum.getValidCode(), null, null);
            // 是否存在资金入账凭证T+1未入账
            boolean isExist = false;
            if (bcrrList != null && !bcrrList.isEmpty()) {
                // 网银确认记录列表
                for (BusinessClaimRepaymentRecordEntity entity : bcrrList) {
                    EbankConfirmRecordDTO ebankConfirmRecordDTO = new EbankConfirmRecordDTO();
                    ebankConfirmRecordDTO.setConfirmAmount(entity.getClaimAmount());
                    ebankConfirmRecordDTO.setEbankConfirmDate(DateUtils.parseDateToStr("yyyy-MM-dd", DateUtils.toDate(entity.getBusinessDate())));
                    if(StringUtils.isEmpty(entity.getContractCode())){
                        ebankConfirmRecordDTO.setContractCode(FinanceEngineEnum.Symbol.NULL.getValue());
                    }else{
                        ebankConfirmRecordDTO.setContractCode(entity.getContractCode());
                    }
                    ebankConfirmRecordDTO.setEbankConfirmComments(entity.getSceneName());
                    // 网银确认记录
                    dto.getEbankConfirmRecord().add(ebankConfirmRecordDTO);
                    if (DateUtils.toDate(entity.getBusinessDate()).compareTo(DateUtils.
                            addDays(dto.getBusinessDate(), 1)) >= 0) {
                        isExist = true;
                    }
                }
            }

            // 异常类型判定
            // 网银到账金额-已确认金额<0
            dto.setExceptionType("");
            if (dto.getBankAmount().compareTo(dto.getConfirmedAmount()) < 0) {
                dto.setExceptionType(dto.getExceptionType().concat("网银到账金额-已确认金额<0;"));
            }

            // 到账主体与认领主体不一致
            if (StringUtils.isNotEmpty(dto.getCollectionAccountsBank()) &&
                    StringUtils.isNotEmpty(dto.getOrgName()) &&
                    !StringUtils.equals(dto.getCollectionAccountsBank(), dto.getOrgName())) {
                dto.setExceptionType(dto.getExceptionType().concat("到账主体与认领主体不一致;"));
            }

            // 是否存在资金入账凭证T+1未入账
            if (isExist) {
                dto.setExceptionType(dto.getExceptionType().concat("存在资金入账凭证T+1未入账;"));
            }

            // 同一客户累积未认领余额超过50万
            SelectClaimByClientCodeDTO claimByClientCodeDTO = businessClaimRepaymentRecordService.
                    selectClaimByClientCode(dto.getClientCode());
            if (claimByClientCodeDTO != null) {
                if (claimByClientCodeDTO.getBankAmount().subtract(claimByClientCodeDTO.getClaimAmount()).
                        compareTo(new BigDecimal(500000)) >= 0) {
                    dto.setExceptionType(dto.getExceptionType().concat("同一客户累积未认领余额超过50万;"));
                }
            }
            // 去重复项
            dto.setOrgId(StringUtils.distinct(dto.getOrgId()));
            dto.setOrgName(StringUtils.distinct(dto.getOrgName()));
            dto.setEbankSerialNumber(StringUtils.distinct(dto.getEbankSerialNumber()));
            // 系统名称
            //dto.setSystemCode(StringUtils.distinct(dto.getSystemCode()));
            // 业务系统
            if (StringUtils.isNotEmpty(dto.getSystemCode())) {
                List<String> systemCodeList = Arrays.asList(StringUtils.distinct(dto.getSystemCode()).split(FinanceEngineEnum.Symbol.COMMA.getValue()));
                StringJoiner joiner = new StringJoiner(FinanceEngineEnum.Symbol.COMMA.getValue());
                for (String systemCode : systemCodeList) {
                    joiner.add(SystemEnum.getDescByCode(systemCode));
                }
                dto.setSystemCode(joiner.toString());
            }
            // 币种
            if (StringUtils.isNotEmpty(dto.getCurrencyType())) {
                dto.setCurrencyType(CurrencyTypeEnum.getDescByCode(dto.getCurrencyType()));
            }
            dto.setRemark(StringUtils.distinct(dto.getRemark()));
            dto.setSceneCode(StringUtils.distinct(dto.getSceneCode()));
            dto.setSceneName(StringUtils.distinct(dto.getSceneName()));
        }
        return queryThirdDetailPageListDataDTOList;
    }

    /**
     * 第三层详情页面数据查询
     */
    public IPage<QueryThirdDetailPageListDataDTO> queryThirdDetailDataByPage(QueryThirdDetailPageDataDTO params) {
        IPage<QueryThirdDetailPageListDataDTO> result = new Page<QueryThirdDetailPageListDataDTO>(
                params.getPageNum(),params.getPageSize());

        Integer totalCount = nonConfirmCollectionSecondDetailMapper.selectThirdDetailDataCount(params);
        result.setTotal(totalCount);

        // 分页查询原始数据
        List<QueryThirdDetailPageListDataDTO> queryThirdDetailPageListDataDTOList =
                nonConfirmCollectionSecondDetailMapper.selectThirdDetailData(params);
        // 查询为空时，按照其它业务进行查询第三层明细数据
        if (queryThirdDetailPageListDataDTOList == null || queryThirdDetailPageListDataDTOList.isEmpty()) {
            return result.setRecords(new ArrayList<>());
        }

        for (QueryThirdDetailPageListDataDTO dto : queryThirdDetailPageListDataDTOList) {
            // 设置网银确认记录
            List<BusinessClaimRepaymentRecordEntity> bcrrList = businessClaimRepaymentRecordService.
                    selectClaimRecordByDeductBatchNo(dto.getEbankSerialNumber(), ProcessStatusEnum.getValidCode(), null, null);
            // 是否存在资金入账凭证T+1未入账
            boolean isExist = false;
            if (bcrrList != null && !bcrrList.isEmpty()) {
                for (BusinessClaimRepaymentRecordEntity entity : bcrrList) {
                    EbankConfirmRecordDTO ebankConfirmRecordDTO = new EbankConfirmRecordDTO();
                    ebankConfirmRecordDTO.setConfirmAmount(entity.getClaimAmount());
                    ebankConfirmRecordDTO.setEbankConfirmDate(DateUtils.parseDateToStr("yyyy-MM-dd", DateUtils.toDate(entity.getBusinessDate())));
                    ebankConfirmRecordDTO.setContractCode(entity.getContractCode());
                    ebankConfirmRecordDTO.setEbankConfirmComments(entity.getSceneName());
                    dto.getEbankConfirmRecord().add(ebankConfirmRecordDTO);

                    if (DateUtils.toDate(entity.getBusinessDate()).compareTo(DateUtils.
                            addDays(dto.getBusinessDate(), 1)) >= 0) {
                        isExist = true;
                    }
                }
            }

            // 异常类型判定
            // 网银到账金额-已确认金额<0
            dto.setExceptionType("");
            if (dto.getBankAmount().compareTo(dto.getConfirmedAmount()) < 0) {
                dto.setExceptionType(dto.getExceptionType().concat("网银到账金额-已确认金额<0;"));
            }

            // 到账主体与认领主体不一致
            if (StringUtils.isNotEmpty(dto.getCollectionAccountsBank()) &&
                    StringUtils.isNotEmpty(dto.getOrgName()) &&
                    !StringUtils.equals(dto.getCollectionAccountsBank(), dto.getOrgName())) {
                dto.setExceptionType(dto.getExceptionType().concat("到账主体与认领主体不一致;"));
            }

            // 是否存在资金入账凭证T+1未入账
            if (isExist) {
                dto.setExceptionType(dto.getExceptionType().concat("存在资金入账凭证T+1未入账;"));
            }

            // 同一客户累积未认领余额超过50万
            SelectClaimByClientCodeDTO claimByClientCodeDTO = businessClaimRepaymentRecordService.
                    selectClaimByClientCode(dto.getClientCode());
            if (claimByClientCodeDTO != null) {
                if (claimByClientCodeDTO.getBankAmount().subtract(claimByClientCodeDTO.getClaimAmount()).
                        compareTo(new BigDecimal(500000)) >= 0) {
                    dto.setExceptionType(dto.getExceptionType().concat("同一客户累积未认领余额超过50万;"));
                }
            }

            // 去重复项
            dto.setOrgId(StringUtils.distinct(dto.getOrgId()));
            dto.setOrgName(StringUtils.distinct(dto.getOrgName()));
            dto.setEbankSerialNumber(StringUtils.distinct(dto.getEbankSerialNumber()));
            dto.setSystemCode(StringUtils.distinct(dto.getSystemCode()));
            dto.setRemark(StringUtils.distinct(dto.getRemark()));
            dto.setSceneCode(StringUtils.distinct(dto.getSceneCode()));
            dto.setSceneName(StringUtils.distinct(dto.getSceneName()));
        }
        result.setRecords(queryThirdDetailPageListDataDTOList);
        return result;
    }

    /**
     * 撤回
     */
    public R<String> nonConfirmCollectionDetailRecall(NonConfirmCollectionDetailRecallInputDTO params) {
        List<NonConfirmCollectionSecondDetailEntity> detailEntityList = nonConfirmCollectionSecondDetailMapper.
                selectBatchIds(params.getDetailIds());
        if (detailEntityList == null || detailEntityList.isEmpty()) {
            return R.fail("记录已被删除,请重新加载列表数据!");
        }

        String validationResult = this.recallValidation(detailEntityList, params.getDetailIds());
        if (StringUtils.isNotEmpty(validationResult)) {
            return R.fail(validationResult);
        }

        // 认领记录状态更新
        List<Long> dbIds = detailEntityList.stream().map(NonConfirmCollectionSecondDetailEntity::getId).
                collect(Collectors.toList());
        LambdaUpdateWrapper<NonConfirmCollectionSecondDetailEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(NonConfirmCollectionSecondDetailEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        updateWrapper.in(NonConfirmCollectionSecondDetailEntity::getId, dbIds);
        updateWrapper.set(NonConfirmCollectionSecondDetailEntity::getProcessStatus, ProcessStatusEnum.ENTERED.getCode());
        updateWrapper.set(NonConfirmCollectionSecondDetailEntity::getUpdateBy, SecurityUtils.getUserId());
        updateWrapper.set(NonConfirmCollectionSecondDetailEntity::getUpdateTime, LocalDateTime.now());
        this.update(updateWrapper);

        // 手工凭证状态更新
        List<NonConfirmCollectionSecondDetailEntity> manualVoucherIdList = detailEntityList.stream().filter(
                e->StringUtils.isNotEmpty(e.getManualVoucherIds())).collect(Collectors.toList());
        if (manualVoucherIdList != null && !manualVoucherIdList.isEmpty()) {
            List<String> manualVoucherIds = manualVoucherIdList.stream().map(NonConfirmCollectionSecondDetailEntity::getManualVoucherIds).
                    distinct().collect(Collectors.toList());
            List<String> ids = manualVoucherIds.stream().map(e->e.split(",")).flatMap(Arrays::stream).
                    distinct().collect(Collectors.toList());
            manualService.updateStatusByids(ids, ProcessStatusEnum.ENTERED.getCode(), StringUtil.EMPTY, StringUtil.EMPTY);
        }

        // 核销回款凭证的状态更新
        List<NonConfirmCollectionSecondDetailEntity> wirteOffVoucherIdList = detailEntityList.stream().filter(
                e->StringUtils.isNotEmpty(e.getWriteOffVoucherId())).collect(Collectors.toList());
        if (wirteOffVoucherIdList != null && !wirteOffVoucherIdList.isEmpty()) {
            List<String> wirteOffVoucherIds = wirteOffVoucherIdList.stream().
                    map(NonConfirmCollectionSecondDetailEntity::getWriteOffVoucherId).
                    distinct().collect(Collectors.toList());

            List<String> ids = wirteOffVoucherIds.stream().map(e->e.split(",")).flatMap(Arrays::stream).
                    distinct().collect(Collectors.toList());
            if (wirteOffVoucherIds != null && !wirteOffVoucherIds.isEmpty()) {
                voucherService.updateStatusByids(ids, ProcessStatusEnum.ENTERED.getCode(),
                        StringUtil.EMPTY, StringUtil.EMPTY);
            }
        }

        // 凭证状态更新
        List<NonConfirmCollectionSecondDetailEntity> voucherIdList = detailEntityList.stream().
                filter(e->StringUtils.isNotEmpty(e.getVoucherIds())).collect(Collectors.toList());
        if (voucherIdList != null && !voucherIdList.isEmpty()) {
            List<String> voucherIds = voucherIdList.stream().
                    map(NonConfirmCollectionSecondDetailEntity::getVoucherIds).
                    distinct().collect(Collectors.toList());

            List<String> ids = voucherIds.stream().map(e->e.split(",")).flatMap(Arrays::stream).
                    distinct().collect(Collectors.toList());
            if (voucherIds != null && !voucherIds.isEmpty()) {
                voucherService.updateStatusByids(ids, ProcessStatusEnum.ENTERED.getCode(),
                        StringUtil.EMPTY, StringUtil.EMPTY);
            }
        }

        // 更新认领记录的状态
        businessClaimRepaymentRecordService.processStatusBatchUpdate(dbIds, ProcessStatusEnum.ENTERED.getCode());

        // 更新审核记录的状态
        List<Long> approveIds = detailEntityList.stream().map(NonConfirmCollectionSecondDetailEntity::getApproveId).
                collect(Collectors.toList());
        approveService.withdraw(approveIds);
        return R.ok();
    }

    /**
     * 撤回数据校验
     */
    private String recallValidation(List<NonConfirmCollectionSecondDetailEntity> detailEntityList, List<String> detailIds) {
        // 记录数对应
        if (detailEntityList.size() != detailIds.size()) {
            List<Long> dbIds = detailEntityList.stream().map(NonConfirmCollectionSecondDetailEntity::getId).
                    collect(Collectors.toList());
            for (int i = 0; i < detailIds.size(); i++) {
                if (!dbIds.contains(String.valueOf(detailIds.get(i)))) {
                    return "明细ID:".concat(String.valueOf(detailIds.get(i))).concat("的记录不存在!");
                }
            }
        }

        // 状态校验
        for (int i = 0; i < detailEntityList.size(); i++) {
            NonConfirmCollectionSecondDetailEntity entity = detailEntityList.get(i);
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(entity.getProcessStatus())) {
                return "明细ID:".concat(String.valueOf(entity.getId())).concat("处理状态不为已提交状态!");
            }
        }
        return StringUtil.EMPTY;
    }


    /**
     * 删除
     */
    public R<String> nonConfirmCollectionDetailDelete(NonConfirmCollectionDetailDeleteInputDTO params) {
        List<NonConfirmCollectionSecondDetailEntity> detailEntityList = nonConfirmCollectionSecondDetailMapper.
                selectBatchIds(params.getDetailIds());
        if (detailEntityList == null || detailEntityList.isEmpty()) {
            return R.fail("记录已被删除,请重新加载列表数据!");
        }

        String validationResult = this.deleteValidation(detailEntityList, params.getDetailIds());
        if (StringUtils.isNotEmpty(validationResult)) {
            return R.fail(validationResult);
        }

        // 认领记录状态更新
        List<Long> dbIds = detailEntityList.stream().map(NonConfirmCollectionSecondDetailEntity::getId).
                collect(Collectors.toList());
        this.removeBatchByIds(dbIds);

        // 手工凭证状态更新
        List<NonConfirmCollectionSecondDetailEntity> manualVoucherIdList = detailEntityList.stream().
                filter(e->StringUtils.isNotEmpty(e.getManualVoucherIds())).collect(Collectors.toList());
        if (manualVoucherIdList != null && !manualVoucherIdList.isEmpty()) {
            List<String> manualVoucherIds = manualVoucherIdList.stream().
                    map(NonConfirmCollectionSecondDetailEntity::getManualVoucherIds).
                    distinct().collect(Collectors.toList());
            List<Long> newManualVoucherIds = new ArrayList<>();
            if (manualVoucherIds != null && !manualVoucherIds.isEmpty()) {
                manualVoucherIds.stream().forEach(e -> {
                    newManualVoucherIds.addAll(Arrays.asList(e.split(",")).stream().map(Long::parseLong).collect(Collectors.toList()));
                });
                manualService.deleteByIds(newManualVoucherIds);
            }
        }

        // 核销回款凭证的状态更新
        List<NonConfirmCollectionSecondDetailEntity> voucherIdList = detailEntityList.stream().
                filter(e-> StringUtils.isNotEmpty(e.getWriteOffVoucherId())).collect(Collectors.toList());
        if (voucherIdList != null && !voucherIdList.isEmpty()) {
            List<String> voucherIds = voucherIdList.stream().
                    map(e->e.getWriteOffVoucherId()).
                    distinct().collect(Collectors.toList());
            if (voucherIds != null && !voucherIds.isEmpty()) {
                List<String> ids = voucherIds.stream().map(e->e.split(",")).flatMap(Arrays::stream).
                        distinct().collect(Collectors.toList());

                LambdaQueryWrapper<VoucherEntryEntity> wrapper = new LambdaQueryWrapper<>();
                wrapper.in(VoucherEntryEntity::getVoucherId, ids);
                voucherEntryService.remove(wrapper);
                voucherService.removeBatchByIds(voucherIds);
            }
        }

        // 凭证状态更新
        voucherIdList = detailEntityList.stream().
                filter(e->StringUtils.isNotEmpty(e.getVoucherIds())).collect(Collectors.toList());
        if (voucherIdList != null && !voucherIdList.isEmpty()) {
            List<String> voucherIds = voucherIdList.stream().
                    map(NonConfirmCollectionSecondDetailEntity::getVoucherIds).
                    distinct().collect(Collectors.toList());

            if (voucherIds != null && !voucherIds.isEmpty()) {
                List<String> ids = voucherIds.stream().map(e->e.split(",")).flatMap(Arrays::stream).
                        distinct().collect(Collectors.toList());

                LambdaQueryWrapper<VoucherEntryEntity> wrapper = new LambdaQueryWrapper<>();
                wrapper.in(VoucherEntryEntity::getVoucherId, ids);
                voucherEntryService.remove(wrapper);
                voucherService.removeBatchByIds(voucherIds);
            }
        }

        // 更新认领记录的状态
        LambdaUpdateWrapper<BusinessClaimRepaymentRecordEntity> wrapper = new LambdaUpdateWrapper();
        wrapper.in(BusinessClaimRepaymentRecordEntity::getNonConfirmSecondDetailId,dbIds);
        wrapper.set(BusinessClaimRepaymentRecordEntity::getDelFlag, YesOrNoEnum.YES.getCode());
        businessClaimRepaymentRecordService.update(wrapper);
        return R.ok();
    }

    /**
     * 删除数据校验
     */
    private String deleteValidation(List<NonConfirmCollectionSecondDetailEntity> detailEntityList, List<String> detailIds) {

        // 状态校验
        for (int i = 0; i < detailEntityList.size(); i++) {
            NonConfirmCollectionSecondDetailEntity entity = detailEntityList.get(i);
            if (!ProcessStatusEnum.ENTERED.getCode().equals(entity.getProcessStatus())
                && !ProcessStatusEnum.SUBMITTED.getCode().equals(entity.getProcessStatus())
                    && !ProcessStatusEnum.REJECTED.getCode().equals(entity.getProcessStatus())) {
                return "明细ID:".concat(String.valueOf(entity.getId())).concat("处理状态不为已录入/已提交/已拒绝状态,不能删除!");
            }
        }
        return StringUtil.EMPTY;
    }

    /**
     * 未确认收款下载表数据查询
     */
    public List<SelectDownloadDataDTO> queryDownloadData(QueryThirdDetailPageDataDTO params) {
        List<SelectDownloadDataDTO> selectDownloadDataDTOList = nonConfirmCollectionSumMapper.
                selectDownloadData(params);
        if (selectDownloadDataDTOList == null || selectDownloadDataDTOList.isEmpty()) {
            return new ArrayList<>();
        }

        // 按照客户分组，计算客户的未认领金额
        List<SelectDownloadDataDTO> downloadDataDTOList = selectDownloadDataDTOList.stream().
                filter(e->StringUtils.isNotEmpty(e.getClientCode())).collect(Collectors.toList());

        Map<String, BigDecimal> clientRemainAmountMap = new HashMap<>();
        if (downloadDataDTOList != null && !downloadDataDTOList.isEmpty()) {
            Map<String, List<SelectDownloadDataDTO>> ebankSerialNumberGroup = downloadDataDTOList.stream().
                    collect(Collectors.groupingBy(e->e.getEbankSerialNumber()));
            for (String key : ebankSerialNumberGroup.keySet()) {
                List<SelectDownloadDataDTO> list = ebankSerialNumberGroup.get(key);
                BigDecimal remainAmount = BigDecimal.ZERO;
                if (clientRemainAmountMap.containsKey(list.get(0).getClientCode())) {
                    remainAmount = clientRemainAmountMap.get(list.get(0).getClientCode());
                    remainAmount = remainAmount.add(list.get(0).getRemainAmount());
                    clientRemainAmountMap.put(list.get(0).getClientCode(), remainAmount);
                } else {
                    remainAmount = list.get(0).getRemainAmount();
                    clientRemainAmountMap.put(list.get(0).getClientCode(), remainAmount);
                }
            }
        }

        for (SelectDownloadDataDTO dto : selectDownloadDataDTOList) {
            if (StringUtils.isNotEmpty(dto.getSystemCode())) {
                dto.setSystemCode(SystemEnum.getDescByCode(dto.getSystemCode()));
            }

            if (StringUtils.isNotEmpty(dto.getCurrencyType())) {
                dto.setCurrencyType(CurrencyTypeEnum.getDescByCode(dto.getCurrencyType()));
            }

            BigDecimal claimedAmount = Optional.of(selectDownloadDataDTOList.stream().
                            filter(e-> StringUtils.equals(e.getEbankSerialNumber(), dto.getEbankSerialNumber()) &&
                                    DateUtils.truncatedCompareTo(e.getEbankConfirmDate(), dto.getEbankConfirmDate(), Calendar.DATE) <= 0)).
                    orElse(null).map(SelectDownloadDataDTO::getClaimAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (claimedAmount == null) {
                dto.setClaimedAmount(BigDecimal.ZERO);
            } else {
                dto.setClaimedAmount(claimedAmount);
            }

            StringBuffer confirmContent = new StringBuffer();
            if (StringUtils.isNotEmpty(dto.getContractCode())) {
                confirmContent.append(dto.getContractCode()).append(" ");
            }
            if (StringUtils.isNotEmpty(dto.getOperationType())) {
                confirmContent.append(ClaimOperationTypeEnum.getDescByCode(dto.getOperationType()));
            }
            dto.setEbankConfirmedContent(confirmContent.toString());

            // 异常类型判定
            // 网银到账金额-已确认金额<0
            dto.setExceptionType("");
            if (dto.getBankAmount().compareTo(dto.getClaimedAmount()) < 0) {
                dto.setExceptionType(dto.getExceptionType().concat("网银到账金额-已确认金额<0;"));
            }

            // 到账主体与认领主体不一致
            if (StringUtils.isNotEmpty(dto.getCollectionAccountsBank()) &&
                    StringUtils.isNotEmpty(dto.getOrgName()) &&
                    !StringUtils.equals(dto.getCollectionAccountsBank(), dto.getOrgName())) {
                dto.setExceptionType(dto.getExceptionType().concat("到账主体与认领主体不一致;"));
            }

            // 是否存在资金入账凭证T+1未入账
//            if (isExist) {
//                dto.setExceptionType(dto.getExceptionType().concat("存在资金入账凭证T+1未入账;"));
//            }

            // 同一客户累积未认领余额超过50万
            if (clientRemainAmountMap.get(dto.getClientCode()) != null &&
                    clientRemainAmountMap.get(dto.getClientCode()).compareTo(new BigDecimal(500000)) >= 0) {
                dto.setExceptionType(dto.getExceptionType().concat("同一客户累积未认领余额超过50万;"));
            }
        }
        return selectDownloadDataDTOList;
    }
}

