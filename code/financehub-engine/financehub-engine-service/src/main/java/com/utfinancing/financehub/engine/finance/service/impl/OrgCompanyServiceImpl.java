package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.model.dto.OrgCompanyQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OrgCompanyDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OrgCompanySaveDTO;
import com.utfinancing.financehub.engine.finance.model.vo.OrgCompanyVO;
import com.utfinancing.financehub.engine.finance.entity.OrgCompanyEntity;
import com.utfinancing.financehub.engine.finance.mapper.OrgCompanyMapper;
import com.utfinancing.financehub.engine.finance.service.IOrgCompanyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-12
 * @Description :  OrgCompany服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class OrgCompanyServiceImpl extends ServiceImpl<OrgCompanyMapper, OrgCompanyEntity> implements IOrgCompanyService {

    private final OrgCompanyMapper orgCompanyMapper;

    public static Map<String, String> orgIdMap = new HashMap<>();

    /**
     * 签约主体新旧编码转换
     */
    static {
        orgIdMap.put("01-C0001", "01-C0001-old");
        orgIdMap.put("02-C0001", "02-C0001-old");
        orgIdMap.put("30001", "30001-old");
        orgIdMap.put("80001", "80001-old");
    }

    @Override
    public Long saveOrgCompany(OrgCompanySaveDTO dto) {
        OrgCompanyEntity entity = BeanUtil.copyProperties(dto, OrgCompanyEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateOrgCompany(Long id, OrgCompanySaveDTO dto) {
        OrgCompanyEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public OrgCompanyDTO getOrgCompanyDTOById(Long id) {
        OrgCompanyEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, OrgCompanyDTO.class);
    }

    @Override
    public IPage<OrgCompanyVO> selectPage(OrgCompanyQueryDTO queryDTO) {
        LambdaQueryWrapper<OrgCompanyEntity> queryWrapper = Wrappers.<OrgCompanyEntity>lambdaQuery();
        //这里注入查询条件
        IPage<OrgCompanyEntity> entityIPage = orgCompanyMapper.selectPage(new Page<OrgCompanyEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, OrgCompanyVO.class);
    }

    @Override
    public List<OrgCompanyVO> selectByCondition(OrgCompanyQueryDTO queryDTO) {
        LambdaQueryWrapper<OrgCompanyEntity> queryWrapper = Wrappers.<OrgCompanyEntity>lambdaQuery();
        List<OrgCompanyEntity> orgCompanyEntityList = list(queryWrapper).stream().distinct().collect(Collectors.toList());
        return ListBeanUtil.copyList(orgCompanyEntityList, OrgCompanyVO.class);
    }

    @Override
    public List<OrgCompanyVO> selectAllOrgIdAndName() {
        LambdaQueryWrapper<OrgCompanyEntity> queryWrapper = Wrappers.<OrgCompanyEntity>lambdaQuery();
        queryWrapper.select(OrgCompanyEntity::getOrgId,OrgCompanyEntity::getOrgName);
        List<OrgCompanyEntity> orgCompanyEntityList = list(queryWrapper).stream().distinct().collect(Collectors.toList());
        return ListBeanUtil.copyList(orgCompanyEntityList, OrgCompanyVO.class);
    }

    /**
     * 存在旧签约主体，则返回old签约主体
     */
    public String getOldOrgId(String newOrgId) {
        if (StringUtils.isEmpty(newOrgId)) {
            return StringUtil.EMPTY;
        }

        String oldOrgId = orgIdMap.get(newOrgId);
        if (StringUtils.isEmpty(oldOrgId)) {
            return newOrgId;
        } else {
            return oldOrgId;
        }
    }
}

