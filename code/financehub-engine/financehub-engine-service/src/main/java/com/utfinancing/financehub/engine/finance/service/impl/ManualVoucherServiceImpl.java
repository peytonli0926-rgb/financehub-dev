package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
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
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.mapper.ManualMapper;
import com.utfinancing.financehub.engine.finance.model.dto.ManualVoucherQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ManualVoucherDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ManualVoucherExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ManualVoucherVO;
import com.utfinancing.financehub.engine.finance.mapper.ManualVoucherMapper;
import com.utfinancing.financehub.engine.finance.service.IContractService;
import com.utfinancing.financehub.engine.finance.service.IManualVoucherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.finance.service.IOrgCompanyService;
import com.utfinancing.financehub.engine.finance.service.IVoucherService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import com.utfinancing.financehub.engine.scene.entity.AccountEntity;
import com.utfinancing.financehub.engine.scene.entity.SceneEntity;
import com.utfinancing.financehub.engine.scene.service.IAccountService;
import com.utfinancing.financehub.engine.scene.service.ISceneService;
import com.utfinancing.financehub.engine.verification.model.dto.VerificationExcelDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author : bruyang
 * @Date : Create in 2024-01-03
 * @Description :  ManualVoucher服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ManualVoucherServiceImpl extends ServiceImpl<ManualVoucherMapper, ManualVoucherEntity> implements IManualVoucherService {

    private final ManualVoucherMapper manualVoucherMapper;
    private final ManualMapper manualMapper;

    @Resource
    private ISceneService iSceneService;

    @Override
    public Long saveManualVoucher(ManualVoucherDTO dto) {
        ManualVoucherEntity entity = BeanUtil.copyProperties(dto, ManualVoucherEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateManualVoucher(Long id, ManualVoucherDTO dto) {
        ManualVoucherEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ManualVoucherDTO getManualVoucherDTOById(Long id) {
        ManualVoucherEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ManualVoucherDTO.class);
    }

    @Override
    public IPage<ManualVoucherVO> selectPage(ManualVoucherQueryDTO queryDTO) {
        LambdaQueryWrapper<ManualVoucherEntity> queryWrapper = Wrappers.<ManualVoucherEntity>lambdaQuery();
        //这里注入查询条件
        if (ObjectUtils.isNotNull(queryDTO.getManualId())) {
            queryWrapper.eq(ManualVoucherEntity::getManualId,queryDTO.getManualId());
        }
        if (StringUtils.isNotEmpty(queryDTO.getOrgId())) {
            queryWrapper.eq(ManualVoucherEntity::getOrgId,queryDTO.getOrgId());
        }
        if (ObjectUtils.isNotNull(queryDTO.getStartVoucherDate())) {
            queryWrapper.apply("to_char(voucher_date,'YYYY-MM-DD')>={0}", DateUtil.format(queryDTO.getStartVoucherDate(),"yyyy-MM-dd"));
        }
        if (ObjectUtils.isNotNull(queryDTO.getEndVoucherDate())) {
            queryWrapper.apply("to_char(voucher_date,'YYYY-MM-DD')<={0}", DateUtil.format(queryDTO.getEndVoucherDate(),"yyyy-MM-dd"));
        }
        if (ObjectUtils.isNotNull(queryDTO.getStartBusinessDate())) {
            queryWrapper.apply("to_char(business_date,'YYYY-MM-DD')>={0}", DateUtil.format(queryDTO.getStartBusinessDate(),"yyyy-MM-dd"));
        }
        if (ObjectUtils.isNotNull(queryDTO.getEndBusinessDate())) {
            queryWrapper.apply("to_char(business_date,'YYYY-MM-DD')<={0}", DateUtil.format(queryDTO.getEndBusinessDate(),"yyyy-MM-dd"));
        }
        if (StringUtils.isNotEmpty(queryDTO.getVoucherType())) {
            queryWrapper.eq(ManualVoucherEntity::getVoucherType, queryDTO.getVoucherType());
        }
        if (StringUtils.isNotEmpty(queryDTO.getAccountCode())) {
            queryWrapper.like(ManualVoucherEntity::getAccountCode, queryDTO.getAccountCode());
        }
        if (StringUtils.isNotEmpty(queryDTO.getAccountName())) {
            queryWrapper.like(ManualVoucherEntity::getAccountName, queryDTO.getAccountName());
        }
        if (StringUtils.isNotEmpty(queryDTO.getCurrencyCode())) {
            queryWrapper.like(ManualVoucherEntity::getCurrencyCode, queryDTO.getCurrencyCode());
        }
        if (StringUtils.isNotEmpty(queryDTO.getIsCashFlow())) {
            queryWrapper.eq(ManualVoucherEntity::getIsCashFlow, queryDTO.getIsCashFlow());
        }
        if (StringUtils.isNotEmpty(queryDTO.getContractCode())) {
            queryWrapper.like(ManualVoucherEntity::getContractCode, queryDTO.getContractCode());
        }
        if (StringUtils.isNotEmpty(queryDTO.getContractName())) {
            queryWrapper.like(ManualVoucherEntity::getContractName, queryDTO.getContractName());
        }
        if (StringUtils.isNotEmpty(queryDTO.getClientCode())) {
            queryWrapper.like(ManualVoucherEntity::getClientCode, queryDTO.getClientCode());
        }
        if (StringUtils.isNotEmpty(queryDTO.getClientName())) {
            queryWrapper.like(ManualVoucherEntity::getClientName, queryDTO.getClientName());
        }
        queryWrapper.orderByAsc(ManualVoucherEntity::getCreateTime);
        IPage<ManualVoucherEntity> entityIPage = manualVoucherMapper.selectPage(new Page<ManualVoucherEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        IPage<ManualVoucherVO> manualVoucherVOIPage = ListBeanUtil.copyPage(entityIPage, ManualVoucherVO.class);
        Map<String,String> sceneCodeMap = iSceneService.list().stream().collect(HashMap::new, (h,v)->h.put(v.getSceneCode(),v.getSceneName()),HashMap::putAll);
        //获取主表信息
        ManualEntity entity = manualMapper.selectById(queryDTO.getManualId());
        manualVoucherVOIPage.getRecords().forEach(v -> {
            if (sceneCodeMap.containsKey(v.getSceneCode())) {
                v.setSceneName(sceneCodeMap.get(v.getSceneCode()));
            }
            v.setVoucherNum(entity.getVoucherNum());
            v.setSubSceneType(entity.getSubSceneType());
            v.setRecheckUserNo(entity.getRecheckUserNo());
            v.setRecheckUserName(entity.getRecheckUserName());
            v.setCreateUserName(entity.getCreateUserName());
            v.setProcessStatus(entity.getProcessStatus());
        });
        return manualVoucherVOIPage;
    }


}

