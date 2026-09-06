package com.utfinancing.financehub.engine.approve.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.admin.api.RemoteOrgService;
import com.utfinancing.financehub.admin.api.model.SysInternalUser;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveQueryDTO;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.model.vo.ApproveVO;
import com.utfinancing.financehub.engine.approve.entity.ApproveEntity;
import com.utfinancing.financehub.engine.approve.mapper.ApproveMapper;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.config.ApproveRabbitmqConfig;
import com.utfinancing.financehub.engine.enums.BatchTypeEnum;
import com.utfinancing.financehub.engine.contractstatusupdate.service.IContractStatusUpdateService;
import com.utfinancing.financehub.engine.enums.ProcessStatusEnum;
import com.utfinancing.financehub.engine.finance.service.ILeaseIncomeService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2024-01-08
 * @Description :  Approve服务实现类
 * @Modified :
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ApproveServiceImpl extends ServiceImpl<ApproveMapper, ApproveEntity> implements IApproveService {

    private final ApproveMapper approveMapper;

    @Resource
    private final RemoteOrgService remoteOrgService;

    @Resource
    private final RabbitTemplate rabbitTemplate;

    @Resource
    @Lazy
    private ILeaseIncomeService leaseIncomeService;

//    @Resource
//    private final IContractStatusUpdateService contractStatusUpdateService;

    @Override
    public Long saveApprove(ApproveDTO dto) {
        ApproveEntity entity = BeanUtil.copyProperties(dto, ApproveEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateApprove(Long id, ApproveDTO dto) {
        ApproveEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ApproveDTO getApproveDTOById(Long id) {
        ApproveEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ApproveDTO.class);
    }

    @Override
    public IPage<ApproveVO> selectPage(ApproveQueryDTO queryDTO) {
        LambdaQueryWrapper<ApproveEntity> queryWrapper = getQueryWrapper(queryDTO);
        IPage<ApproveEntity> entityIPage = approveMapper.selectPage(new Page<ApproveEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, ApproveVO.class);
    }

    @Override
    public IPage<ApproveVO> todoApproveByPage(ApproveQueryDTO queryDTO) {
        /**
         * 根据当前登陆人查询所有下级人员信息
         */
        log.info("调用接口参数remoteOrgService.getReviewSubUserByUserCode：{}",JSONObject.toJSONString(queryDTO));
        R<List<SysInternalUser>> sysInternalUserR = remoteOrgService.getReviewSubUserByUserCode(UserUtils.getStaffCode());
        log.info("调用接口返回值remoteOrgService.getReviewSubUserByUserCode：{}",JSONObject.toJSONString(sysInternalUserR));
        if (null == sysInternalUserR || 200 != sysInternalUserR.getCode()) {
            throw new ServiceException("调用远程接口remoteOrgService.getReviewSubUserByUserCode失败，失败原因："+JSONObject.toJSONString(sysInternalUserR));
        }
        List<String> userCodeList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(sysInternalUserR.getData())) {
            userCodeList.addAll(sysInternalUserR.getData().stream()
                    .map(SysInternalUser::getUserCode)
                    .filter(StringUtils::isNotBlank)
                    .distinct()
                    .collect(Collectors.toList()));
        }
        // Demo 环境由当前登录人兼任审批人；将本人加入审批范围，确保已提交手工单据进入“待我审批”。
        String currentUserCode = UserUtils.getStaffCode();
        if (StringUtils.isNotBlank(currentUserCode) && !userCodeList.contains(currentUserCode)) {
            userCodeList.add(currentUserCode);
        }
        queryDTO.setDocumentStatusList(Lists.newArrayList(ProcessStatusEnum.SUBMITTED.getCode()));
        queryDTO.setSubmitterNumList(userCodeList);
        return selectPage(queryDTO);
    }

    @Override
    public IPage<ApproveVO> myDocumentByPage(ApproveQueryDTO queryDTO) {
        queryDTO.setSubmitterNumList(Lists.newArrayList(UserUtils.getStaffCode()));
        return selectPage(queryDTO);
    }

    @Override
    public IPage<ApproveVO> approvedByPage(ApproveQueryDTO queryDTO) {
        queryDTO.setApproverNumList(Lists.newArrayList(UserUtils.getStaffCode()));
        queryDTO.setDocumentStatusList(Lists.newArrayList(ProcessStatusEnum.REVIEWED.getCode(),ProcessStatusEnum.REJECTED.getCode()));
        return selectPage(queryDTO);
    }

    @Override
    public Boolean pass(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少选择一条数据审批通过");
        }
        List<ApproveEntity> approveEntityList = listByIds(idList);
        approveEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getDocumentStatus())) {
                throw new ServiceException("只有状态为已提交的才可以复核通过");
            }
            v.setApproverDate(LocalDateTime.now());
            v.setApproverNum(UserUtils.getStaffCode());
            v.setApproverName(UserUtils.getStaffName());
            v.setDocumentStatus(ProcessStatusEnum.REVIEWED.getCode());
            this.updateById(v);
            CommonApproveDTO commonApproveDTO = BeanUtil.copyProperties(v,CommonApproveDTO.class);
            dispatchApproval(v, commonApproveDTO);
        });
//        // 审核完成后更新合同状态
//        contractStatusUpdateService.updateContractStatus(approveEntityList);
        return Boolean.TRUE;
    }

    @Override
    public Boolean refuse(List<Long> idList,String remark) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少选择一条数据审批通过");
        }
        List<ApproveEntity> approveEntityList = listByIds(idList);
        approveEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getDocumentStatus())) {
                throw new ServiceException("只有状态为已提交的才可以复核拒绝");
            }
            v.setApproverDate(LocalDateTime.now());
            v.setApproverNum(UserUtils.getStaffCode());
            v.setApproverName(UserUtils.getStaffName());
            v.setDocumentStatus(ProcessStatusEnum.REJECTED.getCode());
            v.setRemark(remark);
            this.updateById(v);
            CommonApproveDTO commonApproveDTO = BeanUtil.copyProperties(v,CommonApproveDTO.class);
            dispatchApproval(v, commonApproveDTO);
        });
        return Boolean.TRUE;
    }

    @Override
    public Boolean returnReviewed(List<Long> idList, String remark) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少选择一条已审批单据");
        }
        List<ApproveEntity> approveEntityList = listByIds(idList);
        approveEntityList.forEach(v -> {
            if (!ProcessStatusEnum.REVIEWED.getCode().equals(v.getDocumentStatus())) {
                throw new ServiceException("只有已复核的单据才可以退回");
            }
            v.setDocumentStatus(ProcessStatusEnum.REJECTED.getCode());
            v.setRemark(remark);
            v.setApproverDate(LocalDateTime.now());
            this.updateById(v);
            CommonApproveDTO commonApproveDTO = BeanUtil.copyProperties(v, CommonApproveDTO.class);
            dispatchApproval(v, commonApproveDTO);
        });
        return Boolean.TRUE;
    }

    /**
     * The local demo has no RabbitMQ consumer. Process income-provision approvals
     * in-process so an unavailable broker cannot block or roll back the approval.
     */
    private void dispatchApproval(ApproveEntity entity, CommonApproveDTO approveDTO) {
        if (BatchTypeEnum.SYJT.getCode().equals(entity.getDocumentType())) {
            if (ProcessStatusEnum.REVIEWED.getCode().equals(approveDTO.getDocumentStatus())) {
                leaseIncomeService.pass(approveDTO);
            } else if (ProcessStatusEnum.REJECTED.getCode().equals(approveDTO.getDocumentStatus())) {
                leaseIncomeService.fail(approveDTO);
            }
            return;
        }
        rabbitTemplate.convertAndSend(
                ApproveRabbitmqConfig.FINHUB_EXCHANGE_DIRECT_APPROVAL_DATA,
                String.format(ApproveRabbitmqConfig.COMMON_FINHUB_ROUTION_KEY, entity.getDocumentType()),
                JSONObject.toJSONString(approveDTO));
    }


    @Override
    public Boolean recall(List<Long> idList,String remark) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少选择一条数据审批通过");
        }
        List<ApproveEntity> approveEntityList = listByIds(idList);
        approveEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getDocumentStatus())) {
                throw new ServiceException("只有状态为已提交的才可以撤回");
            }
            v.setApproverDate(LocalDateTime.now());
            v.setApproverNum(UserUtils.getStaffCode());
            v.setApproverName(UserUtils.getStaffName());
            v.setDocumentStatus(ProcessStatusEnum.RECALL.getCode());
            v.setRemark(remark);
            this.updateById(v);
        });
        return Boolean.TRUE;
    }

    @Override
    public Map<Long,Long> submit(List<ApproveDTO> approveDTOList) {
        if (CollectionUtils.isEmpty(approveDTOList)) {
            return new HashMap<>();
        }
        List<ApproveEntity> approveEntityList = BeanUtil.copyToList(approveDTOList, ApproveEntity.class);
        approveEntityList.stream().forEach(v -> {
            v.setSubmitDate(LocalDateTime.now());
            v.setSubmitterNum(UserUtils.getStaffCode());
            v.setSubmitterName(UserUtils.getStaffName());
            v.setDocumentStatus(ProcessStatusEnum.SUBMITTED.getCode());
        });
        this.saveBatch(approveEntityList);
        return approveEntityList.stream().collect(Collectors.toMap(ApproveEntity::getDocumentId,ApproveEntity::getId, (k1,k2)->k2));
    }

    @Override
    public Boolean withdraw(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少选择一条数据撤回");
        }
        List<ApproveEntity> approveEntityList = listByIds(idList);
        approveEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getDocumentStatus())) {
                throw new ServiceException("只有审批状态为已提交的才可以撤回");
            }
        });
        //撤回成功之后，删除审批表记录
        if (CollectionUtils.isNotEmpty(approveEntityList)) {
            this.removeBatchByIds(idList);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean passAll() {
        //获取所有数据
        List<ApproveEntity> approveEntityList = getAllTodoApprove(new ApproveQueryDTO());
        if (CollectionUtils.isEmpty(approveEntityList)) {
            throw new ServiceException("没有数据需要批准");
        }
        return pass(approveEntityList.stream().map(ApproveEntity::getId).collect(Collectors.toList()));
    }

    @Override
    public Boolean refuseAll() {
        //获取所有数据
        List<ApproveEntity> approveEntityList = getAllTodoApprove(new ApproveQueryDTO());
        if (CollectionUtils.isEmpty(approveEntityList)) {
            throw new ServiceException("没有数据需要拒绝");
        }
        return refuse(approveEntityList.stream().map(ApproveEntity::getId).collect(Collectors.toList()),"");
    }

    public LambdaQueryWrapper<ApproveEntity> getQueryWrapper(ApproveQueryDTO queryDTO){
        LambdaQueryWrapper<ApproveEntity> queryWrapper = Wrappers.<ApproveEntity>lambdaQuery();
        if (StringUtils.isNotEmpty(queryDTO.getSubmitterNum())) {
            queryWrapper.like(ApproveEntity::getSubmitterNum, queryDTO.getSubmitterNum());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getSubmitterNumList())) {
            queryWrapper.in(ApproveEntity::getSubmitterNum, queryDTO.getSubmitterNumList());
        }
        if (StringUtils.isNotEmpty(queryDTO.getApproverNum())) {
            queryWrapper.like(ApproveEntity::getApproverNum, queryDTO.getApproverNum());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getApproverNumList())) {
            queryWrapper.in(ApproveEntity::getApproverNum, queryDTO.getApproverNumList());
        }
        if (StringUtils.isNotEmpty(queryDTO.getSubmitterName())) {
            queryWrapper.like(ApproveEntity::getSubmitterName, queryDTO.getSubmitterName());
        }
        if (StringUtils.isNotEmpty(queryDTO.getApproverName())) {
            queryWrapper.like(ApproveEntity::getApproverName, queryDTO.getApproverName());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getDocumentStatusList())) {
            queryWrapper.in(ApproveEntity::getDocumentStatus, queryDTO.getDocumentStatusList());
        }
        if (StringUtils.isNotEmpty(queryDTO.getDocumentStatus())) {
            queryWrapper.eq(ApproveEntity::getDocumentStatus, queryDTO.getDocumentStatus());
        }
        if (StringUtils.isNotEmpty(queryDTO.getDocumentType())) {
            queryWrapper.eq(ApproveEntity::getDocumentType, queryDTO.getDocumentType());
        }
        queryWrapper.orderByDesc(ApproveEntity::getId);
        return queryWrapper;
    }

    public List<ApproveEntity> getAllTodoApprove(ApproveQueryDTO queryDTO){
        /**
         * 根据当前登陆人查询所有下级人员信息
         */
        log.info("调用接口参数remoteOrgService.getReviewSubUserByUserCode：{}",JSONObject.toJSONString(queryDTO));
        R<List<SysInternalUser>> sysInternalUserR = remoteOrgService.getReviewSubUserByUserCode(UserUtils.getStaffCode());
        log.info("调用接口返回值remoteOrgService.getReviewSubUserByUserCode：{}",JSONObject.toJSONString(sysInternalUserR));
        if (null == sysInternalUserR || 200 != sysInternalUserR.getCode()) {
            throw new ServiceException("调用远程接口remoteOrgService.getReviewSubUserByUserCode失败，失败原因："+JSONObject.toJSONString(sysInternalUserR));
        }
        List<String> userCodeList = sysInternalUserR.getData().stream().map(SysInternalUser::getUserCode).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(userCodeList)) {
           return Lists.newArrayList();
        }
        queryDTO.setDocumentStatusList(Lists.newArrayList(ProcessStatusEnum.SUBMITTED.getCode()));
        queryDTO.setSubmitterNumList(userCodeList);
        LambdaQueryWrapper<ApproveEntity> queryWrapper = getQueryWrapper(queryDTO);
        return this.list(queryWrapper);
    }

}

