package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
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
import com.utfinancing.financehub.engine.finance.entity.CheckAccountDetailResultHisEntity;
import com.utfinancing.financehub.engine.finance.entity.ClientEntity;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountKingdeeResultQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.CheckAccountKingdeeResultDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OrgCompanyQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.CheckAccountDetailResultVO;
import com.utfinancing.financehub.engine.finance.model.vo.CheckAccountKingdeeResultVO;
import com.utfinancing.financehub.engine.finance.entity.CheckAccountKingdeeResultEntity;
import com.utfinancing.financehub.engine.finance.mapper.CheckAccountKingdeeResultMapper;
import com.utfinancing.financehub.engine.finance.model.vo.OrgCompanyVO;
import com.utfinancing.financehub.engine.finance.service.ICheckAccountKingdeeResultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.finance.service.IOrgCompanyService;
import com.utfinancing.financehub.engine.scene.entity.AccountEntity;
import com.utfinancing.financehub.engine.scene.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-27
 * @Description :  CheckAccountKingdeeResult服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class CheckAccountKingdeeResultServiceImpl extends ServiceImpl<CheckAccountKingdeeResultMapper, CheckAccountKingdeeResultEntity> implements ICheckAccountKingdeeResultService {

    private final CheckAccountKingdeeResultMapper checkAccountKingdeeResultMapper;

    @Resource
    IAccountService iAccountService;

    @Resource
    IOrgCompanyService iOrgCompanyService;

    @Override
    public Long saveCheckAccountKingdeeResult(CheckAccountKingdeeResultDTO dto) {
        CheckAccountKingdeeResultEntity entity = BeanUtil.copyProperties(dto, CheckAccountKingdeeResultEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateCheckAccountKingdeeResult(Long id, CheckAccountKingdeeResultDTO dto) {
        CheckAccountKingdeeResultEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public CheckAccountKingdeeResultDTO getCheckAccountKingdeeResultDTOById(Long id) {
        CheckAccountKingdeeResultEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, CheckAccountKingdeeResultDTO.class);
    }

    @Override
    public IPage<CheckAccountKingdeeResultVO> selectKingdeePage(CheckAccountKingdeeResultQueryDTO queryDTO) {
        LambdaQueryWrapper<CheckAccountKingdeeResultEntity> queryWrapper = Wrappers.<CheckAccountKingdeeResultEntity>lambdaQuery();
        //这里注入查询条件
        Integer periodCode = queryDTO.getPeriodCode();
        String accountCode = queryDTO.getAccountCode();
        String orgId = queryDTO.getOrgId();
        String currencyCode = queryDTO.getCurrencyType();
        String diffFlag = queryDTO.getDiffFlag();
        if(ObjectUtil.isNotEmpty(periodCode)){
            queryWrapper.eq(CheckAccountKingdeeResultEntity::getPeriodCode, periodCode);
        }
        if(StringUtils.isNotEmpty(accountCode)){
            queryWrapper.eq(CheckAccountKingdeeResultEntity::getAccountCode, accountCode);
        }
        if(StringUtils.isNotEmpty(orgId)){
            queryWrapper.eq(CheckAccountKingdeeResultEntity::getOrgId, orgId);
        }
        if(StringUtils.isNotEmpty(currencyCode)){
            queryWrapper.eq(CheckAccountKingdeeResultEntity::getCurrencyType, currencyCode);
        }
        if(StringUtils.isNotEmpty(diffFlag)){
            queryWrapper.eq(CheckAccountKingdeeResultEntity::getDiffFlag, diffFlag);
        }

        IPage<CheckAccountKingdeeResultEntity> entityIPage = checkAccountKingdeeResultMapper.selectPage(new Page<CheckAccountKingdeeResultEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        IPage<CheckAccountKingdeeResultVO> page = ListBeanUtil.copyPage(entityIPage, CheckAccountKingdeeResultVO.class);
        List<CheckAccountKingdeeResultVO> list = page.getRecords();
        if(CollectionUtils.isNotEmpty(list)) {
            Map<String, String> accountMap = getAccountNameAccountCode();
            Map<String,String> orgNameByOrgIdMap = getOrgNameOrgId();
            list.stream().forEach(v -> {
                if (orgNameByOrgIdMap.containsKey(v.getOrgId())) {
                    v.setOrgName(orgNameByOrgIdMap.get(v.getOrgId()));
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

    private Map<String,String> getOrgNameOrgId(){
        Map<String,String> orgNameAndIdMap = Maps.newHashMap();
        List<OrgCompanyVO> orgCompanyVOList =  iOrgCompanyService.selectByCondition(new OrgCompanyQueryDTO());
        if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty(orgCompanyVOList)) {
            orgNameAndIdMap = orgCompanyVOList.stream().collect(Collectors.toMap(OrgCompanyVO::getOrgId,OrgCompanyVO::getOrgName, (k1,k2)->k2));
        }
        return orgNameAndIdMap;
    }

    @Override
    public void queryAndSaveCheckKingdeeDto(Integer periodCode, String checkType, CheckAccountDetailRecordEntity latestRecord) {
        CheckAccountKingdeeResultDTO param = new CheckAccountKingdeeResultDTO();
        param.setRecordId(latestRecord.getId());
        param.setCreateBy(StringUtils.equals(CheckTypeEnum.JOB.getCode(), checkType)?"System": SecurityUtils.getUserId().toString());
        param.setUpdateBy(StringUtils.equals(CheckTypeEnum.JOB.getCode(), checkType)?"System": SecurityUtils.getUserId().toString());
        param.setPeriodCode(periodCode);
        LocalDateTime period = LocalDateTimeUtil.parse(periodCode.toString(), "yyyyMM");
        param.setLastPeriodCode(Integer.valueOf(LocalDateTimeUtil.format(period.minusMonths(1), "yyyyMM")));
        checkAccountKingdeeResultMapper.queryAndSaveCheckKingdee(param);
    }

    @Override
    public void clearTableData() {
        checkAccountKingdeeResultMapper.clearTableData();
    }

}

