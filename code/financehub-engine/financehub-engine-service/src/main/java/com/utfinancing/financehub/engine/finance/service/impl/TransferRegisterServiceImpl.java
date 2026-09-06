package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.common.core.utils.uuid.IdUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.enums.BatchTypeEnum;
import com.utfinancing.financehub.engine.enums.ProcessStatusEnum;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.mapper.RentRegisterMapper;
import com.utfinancing.financehub.engine.finance.mapper.SellRegisterMapper;
import com.utfinancing.financehub.engine.finance.model.dto.TransferRegisterExcelDTO;
import com.utfinancing.financehub.engine.finance.model.dto.TransferRegisterQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.TransferRegisterDTO;
import com.utfinancing.financehub.engine.finance.model.vo.TransferRegisterExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.TransferRegisterVO;
import com.utfinancing.financehub.engine.finance.mapper.TransferRegisterMapper;
import com.utfinancing.financehub.engine.finance.service.IContractService;
import com.utfinancing.financehub.engine.finance.service.IOrgCompanyService;
import com.utfinancing.financehub.engine.finance.service.ITransferRegisterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-03
 * @Description :  TransferRegister服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class TransferRegisterServiceImpl extends ServiceImpl<TransferRegisterMapper, TransferRegisterEntity> implements ITransferRegisterService {

    private final TransferRegisterMapper transferRegisterMapper;
    private final IOrgCompanyService iOrgCompanyService;
    private final IContractService iContractService;
    private final SellRegisterMapper sellRegisterMapper;
    private final RentRegisterMapper rentRegisterMapper;

    @Value("${approve.url.transferRegister-url:null}")
    private String approveUrl;

    @Resource
    private IApproveService iApproveService;

    @Override
    public Long saveTransferRegister(TransferRegisterDTO dto) {
        TransferRegisterEntity entity = BeanUtil.copyProperties(dto, TransferRegisterEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateTransferRegister(Long id, TransferRegisterDTO dto) {
        TransferRegisterEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public TransferRegisterDTO getTransferRegisterDTOById(Long id) {
        TransferRegisterEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, TransferRegisterDTO.class);
    }

    @Override
    public IPage<TransferRegisterVO> selectPage(TransferRegisterQueryDTO queryDTO) {
        LambdaQueryWrapper<TransferRegisterEntity> queryWrapper = Wrappers.<TransferRegisterEntity>lambdaQuery();
        // 这里注入查询条件
        setQueryCondition(queryDTO, queryWrapper);
        IPage<TransferRegisterEntity> entityIPage = transferRegisterMapper.selectPage(new Page<TransferRegisterEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, TransferRegisterVO.class);
    }

    @Override
    public List<TransferRegisterExcelVO> selectList(TransferRegisterQueryDTO queryDTO) {
        LambdaQueryWrapper<TransferRegisterEntity> queryWrapper = Wrappers.<TransferRegisterEntity>lambdaQuery();
        // 这里注入查询条件
        setQueryCondition(queryDTO, queryWrapper);

        List<TransferRegisterEntity> list = transferRegisterMapper.selectList(queryWrapper);
        List<TransferRegisterExcelVO> transferRegisterExcelVOS = ListBeanUtil.copyList(list, TransferRegisterExcelVO.class);
        setData(transferRegisterExcelVOS);
        return transferRegisterExcelVOS;
    }

    private void setData(List<TransferRegisterExcelVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        // 签约主体
        Map<String, String> companyMap = iOrgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toMap(e -> e.getOrgId(), e -> e.getOrgName(), (a, b) -> b));
        list.stream().forEach(a -> {
            a.setOrgName(companyMap.get(a.getOrgId()));
        });

    }

    private void setQueryCondition(TransferRegisterQueryDTO queryDTO, LambdaQueryWrapper<TransferRegisterEntity> queryWrapper) {
        if (ObjectUtil.isNotEmpty(queryDTO.getAssetNumber())) {
            queryWrapper.like(TransferRegisterEntity::getAssetNumber, queryDTO.getAssetNumber());
        }
        if (ObjectUtil.isNotEmpty(queryDTO.getContractCode())) {
            queryWrapper.like(TransferRegisterEntity::getContractCode, queryDTO.getContractCode());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getOrgIds())) {
            queryWrapper.in(TransferRegisterEntity::getOrgId, queryDTO.getOrgIds());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getProcessStatusList())) {
            queryWrapper.in(TransferRegisterEntity::getProcessStatus, queryDTO.getProcessStatusList());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getIdList())) {
            queryWrapper.in(TransferRegisterEntity::getId, queryDTO.getIdList());
        }
        if (ObjectUtil.isNotEmpty(queryDTO.getId())) {
            queryWrapper.in(TransferRegisterEntity::getId, queryDTO.getId());
        }
        queryWrapper.orderByDesc(TransferRegisterEntity::getId);
    }

    /**
     * 上传
     *
     * @param file
     * @return
     */
    @Override
    public Boolean importFile(MultipartFile file) {
        try {
            ExcelUtil<TransferRegisterExcelDTO> util = new ExcelUtil<TransferRegisterExcelDTO>(TransferRegisterExcelDTO.class);
            List<TransferRegisterExcelDTO> list = util.importExcel(file.getInputStream());
            if (CollectionUtils.isEmpty(list)) {
                throw new ServiceException("没有数据需要上传");
            }
            // 校验数据
            checkDate(list);

            // 签约主体
            Map<String, String> companyMap = iOrgCompanyService.selectAllOrgIdAndName().stream().collect(Collectors.toMap(e -> e.getOrgName(), e -> e.getOrgId(), (a, b) -> b));

            for (TransferRegisterExcelDTO a : list) {
                String orgId = companyMap.get(a.getOrgId());
                if (ObjectUtil.isEmpty(orgId)) {
                    throw new ServiceException("根据签约主体名称[" + a.getOrgId() + "]未查询到对应的签约主体");
                }
                a.setOrgId(orgId);
            }
            List<TransferRegisterEntity> transferRegisterEntityList = ListBeanUtil.copyList(list, TransferRegisterEntity.class);
            // 保存数据
            saveBatch(transferRegisterEntityList);

        } catch (Exception e) {
            throw new ServiceException("上传文件失败，失败原因:" + e.getMessage());
        }
        return Boolean.TRUE;
    }

    /**
     * 批量提交
     *
     * @param idList
     * @return
     */
    @Override
    public Boolean submit(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条数据提交");
        }
        List<TransferRegisterEntity> transferRegisterEntityList = this.listByIds(idList);
        List<ApproveDTO> approveDTOList = Lists.newArrayList();
        transferRegisterEntityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("只有处理状态为已录入或者已拒绝的才可以提交");
            }
            v.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setDocumentId(v.getId());
            approveDTO.setDocumentType(BatchTypeEnum.DZZCZRDJ.getCode());
            approveDTO.setUrl(approveUrl + v.getId());
            approveDTOList.add(approveDTO);
        });
        // 发送审核
        Map<Long, Long> processInstantIdMap = iApproveService.submit(approveDTOList);
        transferRegisterEntityList.stream().forEach(v -> {
            if (null != processInstantIdMap && processInstantIdMap.containsKey(v.getId())) {
                v.setProcessInstanceId(processInstantIdMap.get(v.getId()));
            }
        });
        return this.updateBatchById(transferRegisterEntityList);
    }

    /**
     * 批量撤回
     *
     * @param idList
     * @return
     */
    @Override
    public Boolean withdraw(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请至少勾选一条数据撤回");
        }
        List<TransferRegisterEntity> transferRegisterEntityList = this.listByIds(idList);
        transferRegisterEntityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已提交的才可以撤回");
            }
            v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
        });
        iApproveService.withdraw(transferRegisterEntityList.stream().map(TransferRegisterEntity::getProcessInstanceId).collect(Collectors.toList()));
        return this.updateBatchById(transferRegisterEntityList);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @Override
    public Boolean delete(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据撤回");
        }
        List<TransferRegisterEntity> transferRegisterEntityList = this.listByIds(ids);
        transferRegisterEntityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("只有处理状态为已录入或者已拒绝的才可以删除");
            }
            // 校验出售登记和出租登记中是否有 资产编号
            List<SellRegisterEntity> sellRegisterEntityList = sellRegisterMapper.selectList(new LambdaQueryWrapper<SellRegisterEntity>().eq(SellRegisterEntity::getAssetNumber, v.getAssetNumber()));
            List<RentRegisterEntity> rentRegisterEntityList = rentRegisterMapper.selectList(new LambdaQueryWrapper<RentRegisterEntity>().eq(RentRegisterEntity::getAssetNumber, v.getAssetNumber()));
            if (CollectionUtils.isNotEmpty(sellRegisterEntityList)) {
                throw new ServiceException("出售登记中存在资产编号[" + v.getAssetNumber() + "]的数据，不能删除");
            }
            if (CollectionUtils.isNotEmpty(rentRegisterEntityList)) {
                throw new ServiceException("出租登记中存在资产编号[" + v.getAssetNumber() + "]的数据，不能删除");
            }
        });
        return removeBatchByIds(ids);
    }

    /**
     * 校验导入数据
     *
     * @param list
     */
    private void checkDate(List<TransferRegisterExcelDTO> list) {
        Set<String> unionSet = new HashSet<>();
        list.stream().forEach(a -> {
            if (ObjectUtil.isEmpty(a.getAssetNumber())) {
                throw new ServiceException("资产编号不能为空");
            }
            if (ObjectUtil.isEmpty(a.getOrgId())) {
                throw new ServiceException("签约主体不能为空");
            }
            if (ObjectUtil.isEmpty(a.getAccountDate())) {
                throw new ServiceException("入账时间不能为空");
            }
            if (ObjectUtil.isEmpty(a.getContractCode())) {
                throw new ServiceException("原合同号不能为空");
            }
            if (ObjectUtil.isEmpty(a.getPropertyAddress())) {
                throw new ServiceException("房产地址不能为空");
            }
            if (ObjectUtil.isEmpty(a.getDebtAssetValue())) {
                throw new ServiceException("抵债资产入账价值不能为空");
            }

            // 校验合同号
            List<ContractEntity> contractEntityList = iContractService.list(new LambdaQueryWrapper<ContractEntity>().eq(ContractEntity::getContractCode, a.getContractCode()));
            if (CollectionUtils.isEmpty(contractEntityList)) {
                throw new ServiceException("根据合同编号[" + a.getContractCode() + "]未查询到对应的合同");
            }
            // 校验房产地址重复
            if (!unionSet.add(a.getPropertyAddress())) {
                throw new ServiceException("文件存在重复的房产地址[" + a.getPropertyAddress() + "],请检查");
            }
            // 校验房产地址
            TransferRegisterEntity transferRegisterEntity = transferRegisterMapper.selectOne(new LambdaQueryWrapper<TransferRegisterEntity>().eq(TransferRegisterEntity::getPropertyAddress, a.getPropertyAddress()));
            if (ObjectUtil.isNotEmpty(transferRegisterEntity)) {
                throw new ServiceException("房产地址[" + a.getPropertyAddress() + "]已存在，不能导入");
            }

        });
    }

    /**
     * 根据资产编号查询转入登记
     *
     * @param assetNumber
     * @return
     */
    @Override
    public TransferRegisterVO getByAssetNumber(String assetNumber) {
        TransferRegisterEntity transferRegisterEntity = this.getOne(new LambdaQueryWrapper<TransferRegisterEntity>().eq(TransferRegisterEntity::getAssetNumber, assetNumber));
        return BeanUtil.copyProperties(transferRegisterEntity, TransferRegisterVO.class);
    }

    @Override
    public List<TransferRegisterVO> getByAssetNumberList(List<String> assetNumberList) {
        List<TransferRegisterEntity> transferRegisterEntityList = this.list(new LambdaQueryWrapper<TransferRegisterEntity>().in(TransferRegisterEntity::getAssetNumber, assetNumberList));
        return BeanUtil.copyToList(transferRegisterEntityList, TransferRegisterVO.class);
    }

    /**
     * 审批修改单据状态
     *
     * @param approveDTO
     */
    @Override
    public void updateProcessStatus(CommonApproveDTO approveDTO) {
        if (StringUtils.isEmpty(approveDTO.getDocumentStatus())) {
            throw new ServiceException("处理状态不可以为空");
        }
        TransferRegisterEntity entity = this.getById(approveDTO.getDocumentId());
        if (ObjectUtil.isEmpty(entity)) {
            throw new ServiceException("转入登记数据不存在");
        }
        // 通过、驳回，直接修改状态
        entity.setProcessStatus(approveDTO.getDocumentStatus());
        this.updateById(entity);
    }

}

