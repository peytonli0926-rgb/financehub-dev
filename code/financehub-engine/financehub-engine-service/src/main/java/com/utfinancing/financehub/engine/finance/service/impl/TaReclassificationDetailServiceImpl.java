package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.StringUtils;
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
import com.utfinancing.financehub.engine.finance.entity.TaOtherPayableDetailEntity;
import com.utfinancing.financehub.engine.finance.model.dto.OrgCompanyQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.TaReclassificationDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.TaReclassificationDetailDTO;
import com.utfinancing.financehub.engine.finance.model.vo.OrgCompanyVO;
import com.utfinancing.financehub.engine.finance.model.vo.TaReclassificationDetailVO;
import com.utfinancing.financehub.engine.finance.entity.TaReclassificationDetailEntity;
import com.utfinancing.financehub.engine.finance.mapper.TaReclassificationDetailMapper;
import com.utfinancing.financehub.engine.finance.service.IOrgCompanyService;
import com.utfinancing.financehub.engine.finance.service.ITaReclassificationDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author : wenbin
 * @Date : Create in 2024-05-22
 * @Description :  TaReclassificationDetail服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class TaReclassificationDetailServiceImpl extends ServiceImpl<TaReclassificationDetailMapper, TaReclassificationDetailEntity> implements ITaReclassificationDetailService {

    private final TaReclassificationDetailMapper taReclassificationDetailMapper;

    @Resource
    IOrgCompanyService iOrgCompanyService;

    @Resource
    private RemoteDictService remoteDictService;

    @Override
    public Long saveTaReclassificationDetail(TaReclassificationDetailDTO dto) {
        TaReclassificationDetailEntity entity = BeanUtil.copyProperties(dto, TaReclassificationDetailEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateTaReclassificationDetail(Long id, TaReclassificationDetailDTO dto) {
        TaReclassificationDetailEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public TaReclassificationDetailDTO getTaReclassificationDetailDTOById(Long id) {
        TaReclassificationDetailEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, TaReclassificationDetailDTO.class);
    }

    @Override
    public IPage<TaReclassificationDetailVO> selectPage(TaReclassificationDetailQueryDTO queryDTO) {
        LambdaQueryWrapper<TaReclassificationDetailEntity> queryWrapper = Wrappers.<TaReclassificationDetailEntity>lambdaQuery();

        //这里注入查询条件

        String reclassificationMonth = queryDTO.getReclassificationMonth();
        if(StringUtils.isNotEmpty(reclassificationMonth)){
            queryWrapper.apply("to_char(reclassification_month, 'YYYY-MM') = {0}", reclassificationMonth);
        }

        if(CollectionUtil.isNotEmpty(queryDTO.getOrgIdList())){
            queryWrapper.in(TaReclassificationDetailEntity::getOrgId, queryDTO.getOrgIdList());
        }

        if(CollectionUtil.isNotEmpty(queryDTO.getSystemCodeList())){
            queryWrapper.in(TaReclassificationDetailEntity::getSystemCode, queryDTO.getSystemCodeList());
        }

        if(CollectionUtil.isNotEmpty(queryDTO.getBankOrgIdList())){
            queryWrapper.in(TaReclassificationDetailEntity::getBankOrgId, queryDTO.getBankOrgIdList());
        }

        if(StringUtils.isNotEmpty(queryDTO.getTaReclassificationAmountStr())){
            if(StringUtils.equals(queryDTO.getTaReclassificationAmountStr(), "0")){
                queryWrapper.apply("ta_reclassification_amount = 0");
            }else{
                queryWrapper.apply("ta_reclassification_amount != 0");
            }
        }

        if(StringUtils.isNotEmpty(queryDTO.getExceptionType())){
//            if(StringUtils.equals(queryDTO.getExceptionType(), "0")){
//                queryWrapper.apply("(ta_excess_balance !=0 or receivable_rent_balance !=0) and ta_reclassification_amount = 0");
//            }else{
//                queryWrapper.apply("ta_reclassification_amount != 0");
//            }
            queryWrapper.eq(TaReclassificationDetailEntity::getExceptionType, queryDTO.getExceptionType());
        }

        if(CollectionUtil.isNotEmpty(queryDTO.getTaReclassificationIdList())){
            queryWrapper.in(TaReclassificationDetailEntity::getTaReclassificationId, queryDTO.getTaReclassificationIdList());
        }

        IPage<TaReclassificationDetailEntity> entityIPage = taReclassificationDetailMapper.selectPage(new Page<TaReclassificationDetailEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        IPage<TaReclassificationDetailVO> page = ListBeanUtil.copyPage(entityIPage, TaReclassificationDetailVO.class);
        List<TaReclassificationDetailVO> list = page.getRecords();
        if(CollectionUtil.isNotEmpty(list)){
            Map<String, String> orgMap = getOrgNameOrgId();
            for(TaReclassificationDetailVO vo :list){
                vo.setOrgName(orgMap.getOrDefault(vo.getOrgId(), ""));
                vo.setBankOrgName(orgMap.getOrDefault(vo.getBankOrgId(), ""));

                vo.setReclassificationMonthStr(LocalDateTimeUtil.format(vo.getReclassificationMonth(), "yyyy-MM"));
            }
        }
        return page;
    }

    @Override
    public List<TaReclassificationDetailVO> selectList(TaReclassificationDetailQueryDTO queryDTO) {
        LambdaQueryWrapper<TaReclassificationDetailEntity> queryWrapper = Wrappers.<TaReclassificationDetailEntity>lambdaQuery();

        //这里注入查询条件
        if(ObjectUtil.isNotEmpty(queryDTO.getReclassificationMonth())) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
            String reclassificationMonthFormat = formatter.format(queryDTO.getReclassificationMonth());
            queryWrapper.apply("to_char(reclassification_month, 'YYYY-MM') = {0}", reclassificationMonthFormat);
        }

        if(CollectionUtil.isNotEmpty(queryDTO.getTaReclassificationIdList())){
            queryWrapper.in(TaReclassificationDetailEntity::getTaReclassificationId,queryDTO.getTaReclassificationIdList());
        }

        if(CollectionUtil.isNotEmpty(queryDTO.getOrgIdList())){
            queryWrapper.in(TaReclassificationDetailEntity::getOrgId, queryDTO.getOrgIdList());
        }

        if(CollectionUtil.isNotEmpty(queryDTO.getSystemCodeList())){
            queryWrapper.in(TaReclassificationDetailEntity::getSystemCode, queryDTO.getSystemCodeList());
        }

        if(CollectionUtil.isNotEmpty(queryDTO.getBankOrgIdList())){
            queryWrapper.in(TaReclassificationDetailEntity::getBankOrgId, queryDTO.getBankOrgIdList());
        }

        if(StringUtils.isNotEmpty(queryDTO.getTaReclassificationAmountStr())){
            if(StringUtils.equals(queryDTO.getTaReclassificationAmountStr(), "0")){
                queryWrapper.apply("ta_reclassification_amount = 0");
            }else{
                queryWrapper.apply("ta_reclassification_amount != 0");
            }
        }

        if(StringUtils.isNotEmpty(queryDTO.getExceptionType())){
//            if(StringUtils.equals(queryDTO.getExceptionType(), "0")){
//                queryWrapper.apply("(ta_excess_balance !=0 or receivable_rent_balance !=0) and ta_reclassification_amount = 0");
//            }else{
//                queryWrapper.apply("ta_reclassification_amount != 0");
//            }
            queryWrapper.eq(TaReclassificationDetailEntity::getExceptionType, queryDTO.getExceptionType());
        }

        List<TaReclassificationDetailEntity> list = this.list(queryWrapper);
        List<TaReclassificationDetailVO> voList = BeanUtil.copyToList(list, TaReclassificationDetailVO.class);
        if(CollectionUtil.isNotEmpty(voList)){
            Map<String, String> orgMap = getOrgNameOrgId();
            R<List<SysDictData>> businessR = remoteDictService.listDictData(DictTypeEnum.CONTRACT_BUSINESS_TYPE.getCode());
            Map<String, String> businessMap = businessR.getData().stream().collect(Collectors.toMap(e -> e.getDictValue(),
                    e -> e.getDictLabel()));

            R<List<SysDictData>> exceptR = remoteDictService.listDictData(DictTypeEnum.TA_EXCEPT_TYPE.getCode());
            Map<String, String> exceptMap = exceptR.getData().stream().collect(Collectors.toMap(e -> e.getDictValue(),
                    e -> e.getDictLabel()));

            for(TaReclassificationDetailVO vo :voList){
                vo.setOrgName(orgMap.getOrDefault(vo.getOrgId(), ""));
                vo.setBankOrgName(orgMap.getOrDefault(vo.getBankOrgId(), ""));

                String systemCode = vo.getSystemCode();
                String sysCodeDesc = SystemEnum.getDescByCode(systemCode);
                if(StringUtils.isNotEmpty(sysCodeDesc)){
                    vo.setSystemCodeName(sysCodeDesc);
                }else {
                    vo.setSystemCodeName(systemCode);
                }

                String businessCode = vo.getBusinessCode();
                vo.setBusinessCodeName(businessMap.getOrDefault(businessCode, businessCode));

                String exceptType = vo.getExceptionType();
                vo.setExceptionTypeName(exceptMap.getOrDefault(exceptType, exceptType));
            }
        }
        return voList;
    }


    private Map<String,String> getOrgNameOrgId(){
        Map<String,String> orgNameAndIdMap = Maps.newHashMap();
        List<OrgCompanyVO> orgCompanyVOList =  iOrgCompanyService.selectByCondition(new OrgCompanyQueryDTO());
        if (CollectionUtils.isNotEmpty(orgCompanyVOList)) {
            orgNameAndIdMap = orgCompanyVOList.stream().collect(Collectors.toMap(OrgCompanyVO::getOrgId,OrgCompanyVO::getOrgName, (k1, k2)->k2));
        }
        return orgNameAndIdMap;
    }

    @Override
    public List<TaReclassificationDetailEntity> getByParams(Map<String, String> param) {
        return taReclassificationDetailMapper.getByParams(param);
    }

    @Override
    public void removeByTaReclassificationIdList(List<Long> ids) {
        if (CollUtil.isNotEmpty(ids)){
            remove(new LambdaQueryWrapper<TaReclassificationDetailEntity>().in(TaReclassificationDetailEntity::getTaReclassificationId,ids));
        }
    }

    @Override
    public List<TaReclassificationDetailEntity> getTaReclassificationDetailList() {
        return taReclassificationDetailMapper.getTaReclassificationDetailList();
    }

    @Override
    public List<TaReclassificationDetailEntity> selectTYPTInfo(String queryDate, String queryDateNextDay, String systemCode) {
        return taReclassificationDetailMapper.selectTYPTInfo(queryDate, queryDateNextDay, systemCode);
    }

    @Override
    public List<TaReclassificationDetailEntity> selectDetailDataAndContractInfo(String queryDate) {
        return taReclassificationDetailMapper.selectDetailDataAndContractInfo(queryDate);
    }

}

