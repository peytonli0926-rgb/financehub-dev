package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import com.utfinancing.financehub.engine.enums.CheckDiffEnum;
import com.utfinancing.financehub.engine.enums.CheckTypeEnum;
import com.utfinancing.financehub.engine.finance.entity.CheckAccountDetailRecordEntity;
import com.utfinancing.financehub.engine.finance.entity.CheckAccountMiddleResultEntity;
import com.utfinancing.financehub.engine.finance.entity.ClientEntity;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.CheckAccountDetailResultVO;
import com.utfinancing.financehub.engine.finance.model.vo.CheckAccountMiddleResultHisVO;
import com.utfinancing.financehub.engine.finance.entity.CheckAccountMiddleResultHisEntity;
import com.utfinancing.financehub.engine.finance.mapper.CheckAccountMiddleResultHisMapper;
import com.utfinancing.financehub.engine.finance.model.vo.CheckAccountMiddleResultVO;
import com.utfinancing.financehub.engine.finance.model.vo.OrgCompanyVO;
import com.utfinancing.financehub.engine.finance.service.ICheckAccountMiddleResultHisService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.finance.service.IClientService;
import com.utfinancing.financehub.engine.finance.service.IOrgCompanyService;
import com.utfinancing.financehub.engine.scene.entity.AccountEntity;
import com.utfinancing.financehub.engine.scene.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-27
 * @Description :  CheckAccountMiddleResultHis服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class CheckAccountMiddleResultHisServiceImpl extends ServiceImpl<CheckAccountMiddleResultHisMapper, CheckAccountMiddleResultHisEntity> implements ICheckAccountMiddleResultHisService {

    private final CheckAccountMiddleResultHisMapper checkAccountMiddleResultHisMapper;

    @Resource
    private IAccountService iAccountService;

    @Resource
    private IClientService iClientService;

    @Resource
    private IOrgCompanyService iOrgCompanyService;

    @Override
    public Long saveCheckAccountMiddleResultHis(CheckAccountMiddleResultHisDTO dto) {
        CheckAccountMiddleResultHisEntity entity = BeanUtil.copyProperties(dto, CheckAccountMiddleResultHisEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateCheckAccountMiddleResultHis(Long id, CheckAccountMiddleResultHisDTO dto) {
        CheckAccountMiddleResultHisEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public CheckAccountMiddleResultHisDTO getCheckAccountMiddleResultHisDTOById(Long id) {
        CheckAccountMiddleResultHisEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, CheckAccountMiddleResultHisDTO.class);
    }

    @Override
    public IPage<CheckAccountMiddleResultHisVO> selectMiddleHisPage(CheckAccountMiddleResultHisQueryDTO queryDTO) {
        LambdaQueryWrapper<CheckAccountMiddleResultHisEntity> queryWrapper = Wrappers.<CheckAccountMiddleResultHisEntity>lambdaQuery();
        //这里注入查询条件
        Integer periodCode = queryDTO.getPeriodCode();
        String accountCode = queryDTO.getAccountCode();
        String orgId = queryDTO.getOrgId();
        String currencyCode = queryDTO.getCurrencyType();
        String diffFlag = queryDTO.getDiffFlag();
        if(ObjectUtil.isNotEmpty(periodCode)){
            queryWrapper.eq(CheckAccountMiddleResultHisEntity::getPeriodCode, periodCode);
        }
        if(StringUtils.isNotEmpty(accountCode)){
            queryWrapper.eq(CheckAccountMiddleResultHisEntity::getAccountCode, accountCode);
        }
        if(StringUtils.isNotEmpty(orgId)){
            queryWrapper.eq(CheckAccountMiddleResultHisEntity::getOrgId, orgId);
        }
        if(StringUtils.isNotEmpty(currencyCode)){
            queryWrapper.eq(CheckAccountMiddleResultHisEntity::getCurrencyType, currencyCode);
        }
        if(StringUtils.isNotEmpty(diffFlag)){
            queryWrapper.eq(CheckAccountMiddleResultHisEntity::getDiffFlag, diffFlag);
        }
        IPage<CheckAccountMiddleResultHisEntity> entityIPage = checkAccountMiddleResultHisMapper.selectPage(new Page<CheckAccountMiddleResultHisEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        IPage<CheckAccountMiddleResultHisVO> page = ListBeanUtil.copyPage(entityIPage, CheckAccountMiddleResultHisVO.class);
        List<CheckAccountMiddleResultHisVO> list = page.getRecords();
        if(CollectionUtils.isNotEmpty(list)) {
            Map<String,String> orgNameByOrgIdMap = getOrgNameOrgId();
            Map<String, String> clientMap = getClientNameClientCode(list);
            Map<String, String> accountMap = getAccountNameAccountCode();

            list.stream().forEach(v -> {
                if (orgNameByOrgIdMap.containsKey(v.getOrgId())) {
                    v.setOrgName(orgNameByOrgIdMap.get(v.getOrgId()));
                }
                if(clientMap.containsKey(v.getClientCode())){
                    v.setClientName(clientMap.get(v.getClientCode()));
                }
                if (accountMap.containsKey(v.getAccountCode())){
                    v.setAccountName(accountMap.get(v.getAccountCode()));
                }
                v.setDiffFlagDesc(StringUtils.equals(v.getDiffFlag(), CheckDiffEnum.NO_DIFF.getCode())?CheckDiffEnum.NO_DIFF.getDesc():CheckDiffEnum.DIFF.getDesc());
            });
        }
        page.setRecords(list);
        return page;
    }

    private Map<String, String> getAccountNameAccountCode() {
        Map<String,String> accountMap = new HashMap<>();
        List<AccountEntity> accountEntityList = iAccountService.list();
        if (CollectionUtils.isNotEmpty(accountEntityList)) {
            accountMap = accountEntityList.stream().collect(HashMap::new,(map,item)->map.put(item.getAccountCode(),item.getAccountName()),HashMap::putAll);
        }
        return accountMap;
    }

    private Map<String, String> getClientNameClientCode(List<CheckAccountMiddleResultHisVO> list) {
        List<String> clientCodeList = list.stream().filter(v -> StringUtils.isNotEmpty(v.getClientCode())).map(CheckAccountMiddleResultHisVO::getClientCode).distinct().collect(Collectors.toList());
        Map<String,String> clientMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(clientCodeList)) {
            List<ClientEntity> clientEntityList = iClientService.lambdaQuery().in(ClientEntity::getClientCode,clientCodeList).list();
            if (CollectionUtils.isNotEmpty(clientEntityList)) {
                clientMap = clientEntityList.stream().collect(HashMap::new,(map,item)->map.put(item.getClientCode(),item.getClientName()),HashMap::putAll);
            }
        }
        return clientMap;
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
    public void saveResultToHis() {
        checkAccountMiddleResultHisMapper.saveResultToHis();
    }

    @Override
    public void clearHisTableData(Integer periodCode) {
        checkAccountMiddleResultHisMapper.clearHisTableData(periodCode);
    }

    @Override
    public void queryAndSaveCheckMiddleDto(Integer periodCode, String checkType, CheckAccountDetailRecordEntity latestRecord) {
        CheckAccountMiddleResultHisDTO param = new CheckAccountMiddleResultHisDTO();
        param.setRecordId(latestRecord.getId());
        param.setCreateBy(StringUtils.equals(CheckTypeEnum.JOB.getCode(), checkType)?"System": SecurityUtils.getUserId().toString());
        param.setUpdateBy(StringUtils.equals(CheckTypeEnum.JOB.getCode(), checkType)?"System": SecurityUtils.getUserId().toString());
        param.setPeriodCode(periodCode);
        checkAccountMiddleResultHisMapper.queryAndSaveCheckMiddle(param);
    }

}

