package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.financial.model.dto.KingdeeAccountQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.KingdeeAccountDTO;
import com.utfinancing.financehub.etl.financial.model.vo.KingdeeAccountVO;
import com.utfinancing.financehub.etl.financial.entity.KingdeeAccountEntity;
import com.utfinancing.financehub.etl.financial.mapper.KingdeeAccountMapper;
import com.utfinancing.financehub.etl.financial.service.IKingdeeAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description :  KingdeeAccount服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class KingdeeAccountServiceImpl extends ServiceImpl<KingdeeAccountMapper, KingdeeAccountEntity> implements IKingdeeAccountService {

    private final KingdeeAccountMapper kingdeeAccountMapper;

    @Override
    public Long saveKingdeeAccount(KingdeeAccountDTO dto) {
        KingdeeAccountEntity entity = BeanUtil.copyProperties(dto, KingdeeAccountEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateKingdeeAccount(Long id, KingdeeAccountDTO dto) {
        KingdeeAccountEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public KingdeeAccountDTO getKingdeeAccountDTOById(Long id) {
        KingdeeAccountEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, KingdeeAccountDTO.class);
    }

    @Override
    public IPage<KingdeeAccountVO> selectPage(KingdeeAccountQueryDTO queryDTO) {
        LambdaQueryWrapper<KingdeeAccountEntity> queryWrapper = Wrappers.<KingdeeAccountEntity>lambdaQuery();
        //这里注入查询条件
        IPage<KingdeeAccountEntity> entityIPage = kingdeeAccountMapper.selectPage(new Page<KingdeeAccountEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, KingdeeAccountVO.class);
    }

}

