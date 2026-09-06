package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.entity.KingdeeGeneralAsstEntity;
import com.utfinancing.financehub.engine.finance.model.dto.BankAccountQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.BankAccountDTO;
import com.utfinancing.financehub.engine.finance.model.vo.BankAccountVO;
import com.utfinancing.financehub.engine.finance.entity.BankAccountEntity;
import com.utfinancing.financehub.engine.finance.mapper.BankAccountMapper;
import com.utfinancing.financehub.engine.finance.service.IBankAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;
/**
 * @Author : bruyang
 * @Date : Create in 2023-12-06
 * @Description :  BankAccount服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class BankAccountServiceImpl extends ServiceImpl<BankAccountMapper, BankAccountEntity> implements IBankAccountService {

    private final BankAccountMapper bankAccountMapper;

    @Override
    public Long saveBankAccount(BankAccountDTO dto) {
        BankAccountEntity entity = BeanUtil.copyProperties(dto, BankAccountEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateBankAccount(Long id, BankAccountDTO dto) {
        BankAccountEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public BankAccountDTO getBankAccountDTOById(Long id) {
        BankAccountEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, BankAccountDTO.class);
    }

    @Override
    public IPage<BankAccountVO> selectPage(BankAccountQueryDTO queryDTO) {
        LambdaQueryWrapper<BankAccountEntity> queryWrapper = Wrappers.<BankAccountEntity>lambdaQuery();
        //这里注入查询条件
        queryWrapper.and(StrUtil.isNotBlank(queryDTO.getSearchKey()), i-> i.like(BankAccountEntity::getBankAccountNumber, queryDTO.getSearchKey())
                        .or()
                        .like(BankAccountEntity::getBankAccountName, queryDTO.getSearchKey()));
        IPage<BankAccountEntity> entityIPage = bankAccountMapper.selectPage(new Page<BankAccountEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, BankAccountVO.class);
    }

    /**
     * 根据银行账号查询签约主体
     */
    public List<BankAccountEntity> selectBankAccountEntity(String bankAccountNumber) {
        if (StringUtils.isEmpty(bankAccountNumber)) {
            return null;
        }
        LambdaQueryWrapper<BankAccountEntity> queryWrapper = Wrappers.<BankAccountEntity>lambdaQuery();
        queryWrapper.eq(BankAccountEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        queryWrapper.in(BankAccountEntity::getBankAccountNumber, Arrays.asList(bankAccountNumber.split(",")));
        return bankAccountMapper.selectList(queryWrapper);
    }

    /**
     * 根据银行账号编码查询
     * @param bankAccountCode
     * @return
     */
    @Override
    public List<BankAccountEntity> selectByBankAccountCode(String bankAccountCode) {
        if (StringUtils.isEmpty(bankAccountCode)) {
            return null;
        }
        LambdaQueryWrapper<BankAccountEntity> queryWrapper = Wrappers.<BankAccountEntity>lambdaQuery();
        queryWrapper.eq(BankAccountEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        queryWrapper.in(BankAccountEntity::getBankAccountCode, bankAccountCode);
        return bankAccountMapper.selectList(queryWrapper);
    }
}

