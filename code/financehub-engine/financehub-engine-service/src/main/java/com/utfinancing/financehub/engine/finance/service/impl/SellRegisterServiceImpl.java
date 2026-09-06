package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.poi.ExcelUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.approve.model.dto.ApproveDTO;
import com.utfinancing.financehub.engine.approve.service.IApproveService;
import com.utfinancing.financehub.engine.enums.BatchTypeEnum;
import com.utfinancing.financehub.engine.enums.ProcessStatusEnum;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.RentRegisterVO;
import com.utfinancing.financehub.engine.finance.model.vo.SellRegisterVO;
import com.utfinancing.financehub.engine.finance.entity.SellRegisterEntity;
import com.utfinancing.financehub.engine.finance.mapper.SellRegisterMapper;
import com.utfinancing.financehub.engine.finance.model.vo.TransferRegisterVO;
import com.utfinancing.financehub.engine.finance.service.IRentRegisterService;
import com.utfinancing.financehub.engine.finance.service.ISellRegisterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.finance.service.ITransferRegisterService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
 * @Description :  SellRegister服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class SellRegisterServiceImpl extends ServiceImpl<SellRegisterMapper, SellRegisterEntity> implements ISellRegisterService {

    private final SellRegisterMapper sellRegisterMapper;
    private final ITransferRegisterService iTransferRegisterService;
    private final IRentRegisterService iRentRegisterService;

    @Value("${approve.url.sellRegister-url:null}")
    private String approveUrl;

    @Resource
    private IApproveService iApproveService;

    @Override
    public Long saveSellRegister(SellRegisterDTO dto) {
        SellRegisterEntity entity = BeanUtil.copyProperties(dto, SellRegisterEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateSellRegister(Long id, SellRegisterDTO dto) {
        SellRegisterEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public SellRegisterDTO getSellRegisterDTOById(Long id) {
        SellRegisterEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, SellRegisterDTO.class);
    }

    @Override
    public IPage<SellRegisterVO> selectPage(SellRegisterQueryDTO queryDTO) {
        LambdaQueryWrapper<SellRegisterEntity> queryWrapper = Wrappers.<SellRegisterEntity>lambdaQuery();
        // 这里注入查询条件
        setQueryCondition(queryDTO, queryWrapper);
        IPage<SellRegisterEntity> entityIPage = sellRegisterMapper.selectPage(new Page<SellRegisterEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        IPage<SellRegisterVO> list = ListBeanUtil.copyPage(entityIPage, SellRegisterVO.class);
        setData(list.getRecords());
        return list;
    }

    private void setQueryCondition(SellRegisterQueryDTO queryDTO, LambdaQueryWrapper<SellRegisterEntity> queryWrapper) {
        if (ObjectUtil.isNotEmpty(queryDTO.getAssetNumber())) {
            queryWrapper.like(SellRegisterEntity::getAssetNumber, queryDTO.getAssetNumber());
        }
        if (ObjectUtil.isNotEmpty(queryDTO.getProcessStatusList())) {
            queryWrapper.in(SellRegisterEntity::getProcessStatus, queryDTO.getProcessStatusList());
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getIdList())) {
            queryWrapper.in(SellRegisterEntity::getId, queryDTO.getIdList());
        }
        if (ObjectUtil.isNotEmpty(queryDTO.getId())) {
            queryWrapper.eq(SellRegisterEntity::getId, queryDTO.getId());
        }
        queryWrapper.orderByDesc(SellRegisterEntity::getId);
    }

    @Override
    public List<SellRegisterVO> selectList(SellRegisterQueryDTO queryDTO) {
        LambdaQueryWrapper<SellRegisterEntity> queryWrapper = Wrappers.<SellRegisterEntity>lambdaQuery();
        // 这里注入查询条件
        setQueryCondition(queryDTO, queryWrapper);
        List<SellRegisterEntity> sellRegisterEntityList = sellRegisterMapper.selectList(queryWrapper);
        List<SellRegisterVO> list = ListBeanUtil.copyList(sellRegisterEntityList, SellRegisterVO.class);
        setData(list);
        return list;
    }

    private void setData(List<SellRegisterVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<String> assetNumberList = list.stream().map(SellRegisterVO::getAssetNumber).distinct().collect(Collectors.toList());
        List<TransferRegisterVO> transferRegisterVOList = iTransferRegisterService.getByAssetNumberList(assetNumberList);
        list.stream().forEach(a -> {
            TransferRegisterVO transferRegisterVO = transferRegisterVOList.stream().filter(b -> ObjectUtil.equals(a.getAssetNumber(), b.getAssetNumber())).findFirst().orElse(null);
            if (ObjectUtil.isNotEmpty(transferRegisterVO)) {
                a.setContractCode(transferRegisterVO.getContractCode());
                a.setPropertyAddress(transferRegisterVO.getPropertyAddress());
            }
        });

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
            ExcelUtil<SellRegisterExcelDTO> util = new ExcelUtil<SellRegisterExcelDTO>(SellRegisterExcelDTO.class);
            List<SellRegisterExcelDTO> list = util.importExcel(file.getInputStream());
            if (CollectionUtils.isEmpty(list)) {
                throw new ServiceException("没有数据需要上传");
            }
            // 校验数据
            checkDate(list);
            List<SellRegisterEntity> entityList = ListBeanUtil.copyList(list, SellRegisterEntity.class);
            // 保存数据
            saveBatch(entityList);

        } catch (Exception e) {
            throw new ServiceException("上传文件失败，失败原因:" + e.getMessage());
        }
        return Boolean.TRUE;
    }

    /**
     * 批量提交
     *
     * @param ids
     * @return
     */
    @Override
    public Boolean submit(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据提交");
        }
        List<SellRegisterEntity> entityList = this.listByIds(ids);
        List<ApproveDTO> approveDTOList = Lists.newArrayList();
        entityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("只有处理状态为已录入或者已拒绝的才可以提交");
            }
            v.setProcessStatus(ProcessStatusEnum.SUBMITTED.getCode());
            ApproveDTO approveDTO = new ApproveDTO();
            approveDTO.setDocumentId(v.getId());
            approveDTO.setDocumentType(BatchTypeEnum.DZZCCSDJ.getCode());
            approveDTO.setUrl(approveUrl + v.getId());
            approveDTOList.add(approveDTO);
        });
        // 发送审核
        Map<Long, Long> processInstantIdMap = iApproveService.submit(approveDTOList);
        entityList.stream().forEach(v -> {
            if (null != processInstantIdMap && processInstantIdMap.containsKey(v.getId())) {
                v.setProcessInstanceId(processInstantIdMap.get(v.getId()));
            }
        });
        return this.updateBatchById(entityList);
    }

    /**
     * 批量撤回
     *
     * @param ids
     * @return
     */
    @Override
    public Boolean withdraw(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请至少勾选一条数据撤回");
        }
        List<SellRegisterEntity> entityList = this.listByIds(ids);
        entityList.stream().forEach(v -> {
            if (!ProcessStatusEnum.SUBMITTED.getCode().equals(v.getProcessStatus())) {
                throw new ServiceException("只有处理状态为已提交的才可以撤回");
            }
            v.setProcessStatus(ProcessStatusEnum.ENTERED.getCode());
        });
        iApproveService.withdraw(entityList.stream().map(SellRegisterEntity::getProcessInstanceId).collect(Collectors.toList()));
        return this.updateBatchById(entityList);
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
            throw new ServiceException("请至少勾选一条数据删除");
        }
        List<SellRegisterEntity> transferRegisterEntityList = this.listByIds(ids);
        transferRegisterEntityList.stream().forEach(v -> {
            if (!(ProcessStatusEnum.ENTERED.getCode().equals(v.getProcessStatus()) || ProcessStatusEnum.REJECTED.getCode().equals(v.getProcessStatus()))) {
                throw new ServiceException("只有处理状态为已录入或者已拒绝的才可以删除");
            }
        });
        return removeBatchByIds(ids);
    }

    /**
     * 根据资产编号查询出售登记
     *
     * @param assetNumber
     * @return
     */
    @Override
    public SellRegisterVO getByAssetNumber(String assetNumber) {
        SellRegisterEntity sellRegisterEntity = getOne(new LambdaQueryWrapper<SellRegisterEntity>().eq(SellRegisterEntity::getAssetNumber, assetNumber));
        return BeanUtil.copyProperties(sellRegisterEntity, SellRegisterVO.class);
    }

    /**
     * 校验导入数据
     *
     * @param list
     */
    private void checkDate(List<SellRegisterExcelDTO> list) {
        Set<String> unionSet = new HashSet<>();
        list.stream().forEach(a -> {
            if (ObjectUtil.isEmpty(a.getAssetNumber())) {
                throw new ServiceException("资产编号不能为空");
            }
            if (ObjectUtil.isEmpty(a.getTransferOutDate())) {
                throw new ServiceException("转出时间不能为空");
            }
            if (ObjectUtil.isEmpty(a.getSellPrice())) {
                throw new ServiceException("售价不能为空");
            }
            if (ObjectUtil.isEmpty(a.getBuyOrSellPerson())) {
                throw new ServiceException("买售人不能为空");
            }
            // 校验资产编号重复
            if (!unionSet.add(a.getAssetNumber())) {
                throw new ServiceException("文件存在重复的资产编号[" + a.getAssetNumber() + "],请检查");
            }

            // 1.若上传资产编号在转入登记表不存在，报错：资产编号不存在
            TransferRegisterVO transferRegisterVO = iTransferRegisterService.getByAssetNumber(a.getAssetNumber());
            if (ObjectUtil.isEmpty(transferRegisterVO)) {
                throw new ServiceException("资产编号[" + a.getAssetNumber() + "]不存在");
            }
            SellRegisterVO sellRegisterVO = getByAssetNumber(a.getAssetNumber());
            RentRegisterVO rentRegisterVO = iRentRegisterService.getByAssetNumber(a.getAssetNumber(), null);
            // 2.若上传资产编号已做过转出处理（存在于转出-出售表/转出-出租表），报错：该资产编号已转出
            if (ObjectUtil.isNotEmpty(sellRegisterVO) || ObjectUtil.isNotEmpty(rentRegisterVO)) {
                throw new ServiceException("资产编号[" + a.getAssetNumber() + "]已转出");
            }

        });
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
        SellRegisterEntity entity = this.getById(approveDTO.getDocumentId());
        if (ObjectUtil.isEmpty(entity)) {
            throw new ServiceException("出售登记数据不存在");
        }
        // 通过、驳回，直接修改状态
        entity.setProcessStatus(approveDTO.getDocumentStatus());
        this.updateById(entity);
    }
}

