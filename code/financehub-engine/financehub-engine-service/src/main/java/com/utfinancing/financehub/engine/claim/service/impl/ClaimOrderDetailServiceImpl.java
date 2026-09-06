package com.utfinancing.financehub.engine.claim.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderDetailQueryDTO;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderDetailDTO;
import com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderDetailVO;
import com.utfinancing.financehub.engine.claim.entity.ClaimOrderDetailEntity;
import com.utfinancing.financehub.engine.claim.mapper.ClaimOrderDetailMapper;
import com.utfinancing.financehub.engine.claim.service.IClaimOrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : lixin
 * @Date : Create in 2023-10-23
 * @Description :  ClaimOrderDetail服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ClaimOrderDetailServiceImpl extends ServiceImpl<ClaimOrderDetailMapper, ClaimOrderDetailEntity> implements IClaimOrderDetailService {

    private final ClaimOrderDetailMapper claimOrderDetailMapper;

    @Override
    public Long saveClaimOrderDetail(ClaimOrderDetailDTO dto) {
        ClaimOrderDetailEntity entity = BeanUtil.copyProperties(dto, ClaimOrderDetailEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateClaimOrderDetail(Long id, ClaimOrderDetailDTO dto) {
        ClaimOrderDetailEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ClaimOrderDetailDTO getClaimOrderDetailDTOById(Long id) {
        ClaimOrderDetailEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ClaimOrderDetailDTO.class);
    }

    @Override
    public IPage<ClaimOrderDetailVO> selectPage(ClaimOrderDetailQueryDTO queryDTO) {
        LambdaQueryWrapper<ClaimOrderDetailEntity> queryWrapper = Wrappers.<ClaimOrderDetailEntity>lambdaQuery();
        //这里注入查询条件
        IPage<ClaimOrderDetailEntity> entityIPage = claimOrderDetailMapper.selectPage(new Page<ClaimOrderDetailEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, ClaimOrderDetailVO.class);
    }

    @Override
    public List<ClaimOrderDetailVO> selectByCondition(ClaimOrderDetailQueryDTO queryDTO) {
        return claimOrderDetailMapper.selectByCondition(queryDTO);
    }

    /**
     * 取得未下载文件的费用明细
     */
    public List<ClaimOrderDetailEntity> selectByIsDownloadFile() {
        LambdaQueryWrapper<ClaimOrderDetailEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ClaimOrderDetailEntity::getIsDownloadFile, YesOrNoEnum.NO.getCode());
        wrapper.eq(ClaimOrderDetailEntity::getExpenseType, "回收租赁资产杂费");
        wrapper.isNotNull(ClaimOrderDetailEntity::getAttachNeid);
        wrapper.isNotNull(ClaimOrderDetailEntity::getAttachNsid);
        return claimOrderDetailMapper.selectList(wrapper);
    }
}

