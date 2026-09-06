package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.financial.model.dto.ContractQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.ContractDTO;
import com.utfinancing.financehub.etl.financial.model.vo.ContractVO;
import com.utfinancing.financehub.etl.financial.entity.ContractEntity;
import com.utfinancing.financehub.etl.financial.mapper.ContractMapper;
import com.utfinancing.financehub.etl.financial.service.IContractService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
/**
 * @Author : bruyang
 * @Date : Create in 2024-01-02
 * @Description :  Contract服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ContractServiceImpl extends ServiceImpl<ContractMapper, ContractEntity> implements IContractService {

    private final ContractMapper contractMapper;

    @Override
    public Long saveContract(ContractDTO dto) {
        ContractEntity entity = BeanUtil.copyProperties(dto, ContractEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateContract(Long id, ContractDTO dto) {
        ContractEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ContractDTO getContractDTOById(Long id) {
        ContractEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ContractDTO.class);
    }

    @Override
    public IPage<ContractVO> selectPage(ContractQueryDTO queryDTO) {
        LambdaQueryWrapper<ContractEntity> queryWrapper = Wrappers.<ContractEntity>lambdaQuery();
        //这里注入查询条件
        IPage<ContractEntity> entityIPage = contractMapper.selectPage(new Page<ContractEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, ContractVO.class);
    }


    @Override
    public ContractDTO getContractDTOByCode(String contractCode,String orgId) {
        QueryWrapper<ContractEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ContractEntity::getContractCode, contractCode);
        if (StringUtils.isNotEmpty(orgId)) {
            queryWrapper.lambda().eq(ContractEntity::getOrgId, orgId);
        }
        queryWrapper.lambda().orderByDesc(ContractEntity::getId);
        List<ContractEntity> entityList = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        return BeanUtil.copyProperties(entityList.get(0), ContractDTO.class);
    }

    @Override
    public List<ContractDTO> listContractDTOByCodeList(List<String> contractCodeList) {
        List<ContractEntity> contractEntities = contractMapper.selectList(
                Wrappers.<ContractEntity>lambdaQuery()
                        .in(ContractEntity::getContractCode, contractCodeList));
        if (CollectionUtils.isEmpty(contractEntities)) {
            return new ArrayList<>();
        }
        return BeanUtil.copyToList(contractEntities, ContractDTO.class);
    }
}

