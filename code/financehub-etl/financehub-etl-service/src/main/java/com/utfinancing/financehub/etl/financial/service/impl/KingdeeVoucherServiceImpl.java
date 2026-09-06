package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.financial.model.dto.KingdeeVoucherEntryInnerDTO;
import com.utfinancing.financehub.etl.financial.model.dto.KingdeeVoucherQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.KingdeeVoucherDTO;
import com.utfinancing.financehub.etl.financial.model.vo.KingdeeVoucherVO;
import com.utfinancing.financehub.etl.financial.entity.KingdeeVoucherEntity;
import com.utfinancing.financehub.etl.financial.mapper.KingdeeVoucherMapper;
import com.utfinancing.financehub.etl.financial.service.IKingdeeVoucherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : lixin
 * @Date : Create in 2023-11-14
 * @Description :  KingdeeVoucher服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class KingdeeVoucherServiceImpl extends ServiceImpl<KingdeeVoucherMapper, KingdeeVoucherEntity> implements IKingdeeVoucherService {

    private final KingdeeVoucherMapper kingdeeVoucherMapper;

    @Override
    public Long saveKingdeeVoucher(KingdeeVoucherDTO dto) {
        KingdeeVoucherEntity entity = BeanUtil.copyProperties(dto, KingdeeVoucherEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateKingdeeVoucher(Long id, KingdeeVoucherDTO dto) {
        KingdeeVoucherEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public KingdeeVoucherDTO getKingdeeVoucherDTOById(Long id) {
        KingdeeVoucherEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, KingdeeVoucherDTO.class);
    }

    @Override
    public IPage<KingdeeVoucherVO> selectPage(KingdeeVoucherQueryDTO queryDTO) {
        LambdaQueryWrapper<KingdeeVoucherEntity> queryWrapper = Wrappers.<KingdeeVoucherEntity>lambdaQuery();
        //这里注入查询条件
        IPage<KingdeeVoucherEntity> entityIPage = kingdeeVoucherMapper.selectPage(new Page<KingdeeVoucherEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, KingdeeVoucherVO.class);
    }

    @Override
    public List<KingdeeVoucherEntryInnerDTO> selectKingdeeVoucherEntrySumByPeriod(Integer periodCode,List<String> contractCodeList) {
        return kingdeeVoucherMapper.selectKingdeeVoucherEntrySumByPeriod(periodCode,contractCodeList);
    }

}

