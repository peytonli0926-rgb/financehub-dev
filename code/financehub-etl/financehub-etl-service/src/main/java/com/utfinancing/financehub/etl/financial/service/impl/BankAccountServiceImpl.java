package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.financial.model.dto.BankAccountQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.BankAccountDTO;
import com.utfinancing.financehub.etl.financial.model.vo.BankAccountVO;
import com.utfinancing.financehub.etl.financial.entity.BankAccountEntity;
import com.utfinancing.financehub.etl.financial.mapper.BankAccountMapper;
import com.utfinancing.financehub.etl.financial.service.IBankAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
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
        IPage<BankAccountEntity> entityIPage = bankAccountMapper.selectPage(new Page<BankAccountEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, BankAccountVO.class);
    }

}

