package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.financial.model.dto.AccountQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.AccountDTO;
import com.utfinancing.financehub.etl.financial.model.vo.AccountVO;
import com.utfinancing.financehub.etl.financial.entity.AccountEntity;
import com.utfinancing.financehub.etl.financial.mapper.AccountMapper;
import com.utfinancing.financehub.etl.financial.service.IAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : lixin
 * @Date : Create in 2023-11-16
 * @Description :  Account服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class AccountServiceImpl extends ServiceImpl<AccountMapper, AccountEntity> implements IAccountService {

    private final AccountMapper accountMapper;

    @Override
    public Long saveAccount(AccountDTO dto) {
        AccountEntity entity = BeanUtil.copyProperties(dto, AccountEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateAccount(Long id, AccountDTO dto) {
        AccountEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public AccountDTO getAccountDTOById(Long id) {
        AccountEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, AccountDTO.class);
    }

    @Override
    public IPage<AccountVO> selectPage(AccountQueryDTO queryDTO) {
        LambdaQueryWrapper<AccountEntity> queryWrapper = Wrappers.<AccountEntity>lambdaQuery();
        //这里注入查询条件
        IPage<AccountEntity> entityIPage = accountMapper.selectPage(new Page<AccountEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, AccountVO.class);
    }

}

