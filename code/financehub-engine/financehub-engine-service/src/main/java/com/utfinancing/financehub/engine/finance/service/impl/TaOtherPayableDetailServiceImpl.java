package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.enums.DictTypeEnum;
import com.utfinancing.financehub.engine.enums.SystemEnum;
import com.utfinancing.financehub.engine.finance.model.dto.OrgCompanyQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.TaOtherPayableDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.TaOtherPayableDetailDTO;
import com.utfinancing.financehub.engine.finance.model.vo.OrgCompanyVO;
import com.utfinancing.financehub.engine.finance.model.vo.TaOtherPayableDetailVO;
import com.utfinancing.financehub.engine.finance.entity.TaOtherPayableDetailEntity;
import com.utfinancing.financehub.engine.finance.mapper.TaOtherPayableDetailMapper;
import com.utfinancing.financehub.engine.finance.model.vo.TaOtherPayableVO;
import com.utfinancing.financehub.engine.finance.service.IOrgCompanyService;
import com.utfinancing.financehub.engine.finance.service.ITaOtherPayableDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author : wenbin
 * @Date : Create in 2024-05-22
 * @Description :  TaOtherPayableDetail服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class TaOtherPayableDetailServiceImpl extends ServiceImpl<TaOtherPayableDetailMapper, TaOtherPayableDetailEntity> implements ITaOtherPayableDetailService {

    private final TaOtherPayableDetailMapper taOtherPayableDetailMapper;
    private final RemoteDictService remoteDictService;

    @Resource
    private IOrgCompanyService iOrgCompanyService;

    @Override
    public Long saveTaOtherPayableDetail(TaOtherPayableDetailDTO dto) {
        TaOtherPayableDetailEntity entity = BeanUtil.copyProperties(dto, TaOtherPayableDetailEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateTaOtherPayableDetail(Long id, TaOtherPayableDetailDTO dto) {
        TaOtherPayableDetailEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public TaOtherPayableDetailDTO getTaOtherPayableDetailDTOById(Long id) {
        TaOtherPayableDetailEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, TaOtherPayableDetailDTO.class);
    }

    @Override
    public IPage<TaOtherPayableDetailVO> selectPage(TaOtherPayableDetailQueryDTO queryDTO) {
        //这里注入查询条件
        if(CollUtil.isNotEmpty(queryDTO.getTaOtherPayableIdList())){

        }

        IPage<TaOtherPayableDetailEntity> entityIPage = taOtherPayableDetailMapper.selectByMapper(new Page<TaOtherPayableDetailEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryDTO);
        IPage<TaOtherPayableDetailVO> voiPage = ListBeanUtil.copyPage(entityIPage, TaOtherPayableDetailVO.class);
        setData(voiPage.getRecords());
        return voiPage;
    }

    @Override
    public List<TaOtherPayableDetailVO> selectList(TaOtherPayableDetailQueryDTO queryDTO) {
        List<TaOtherPayableDetailEntity> entityList = taOtherPayableDetailMapper.selectByMapper(queryDTO);
        List<TaOtherPayableDetailVO> list = ListBeanUtil.copyList(entityList, TaOtherPayableDetailVO.class);
        setData(list);
        return list;
    }

    private void setData(List<TaOtherPayableDetailVO> list) {
        if(CollUtil.isEmpty(list)){
            return;
        }

        Map<String,String> orgNameByOrgIdMap = getOrgNameOrgId();
        list.stream().forEach(a->{
            // 设置业务系统名称
            a.setSystemName(SystemEnum.getDescByCode(a.getSystemCode()));

            a.setReclassificationMonthStr(LocalDateTimeUtil.format(a.getReclassificationMonth(), "yyyy-MM"));

            if(StringUtils.isNotEmpty(a.getBankOrgId())&&orgNameByOrgIdMap.containsKey(a.getBankOrgId())){
                a.setBankOrgName(orgNameByOrgIdMap.get(a.getBankOrgId()));
            }
        });
    }

    private Map<String,String> getOrgNameOrgId(){
        Map<String,String> orgNameAndIdMap = Maps.newHashMap();
        List<OrgCompanyVO> orgCompanyVOList =  iOrgCompanyService.selectByCondition(new OrgCompanyQueryDTO());
        if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty(orgCompanyVOList)) {
            orgNameAndIdMap = orgCompanyVOList.stream().collect(Collectors.toMap(OrgCompanyVO::getOrgId,OrgCompanyVO::getOrgName, (k1,k2)->k2));
        }
        return orgNameAndIdMap;
    }

    @Override
    public void removeByTaOtherPayableIdList(List<Long> ids) {
        if (CollUtil.isNotEmpty(ids)){
            remove(new LambdaQueryWrapper<TaOtherPayableDetailEntity>().in(TaOtherPayableDetailEntity::getTaOtherPayableId,ids));
        }
    }

    @Override
    public List<TaOtherPayableDetailEntity> selectDetailInfo(Long id) {
        return taOtherPayableDetailMapper.selectDetailInfo(id);
    }

}

