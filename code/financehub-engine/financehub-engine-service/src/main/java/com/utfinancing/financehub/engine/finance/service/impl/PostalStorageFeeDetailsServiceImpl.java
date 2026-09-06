package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.finance.model.dto.PostalStorageFeeDetailsQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.PostalStorageFeeDetailsDTO;
import com.utfinancing.financehub.engine.finance.model.vo.PostalStorageFeeDetailsVO;
import com.utfinancing.financehub.engine.finance.entity.PostalStorageFeeDetailsEntity;
import com.utfinancing.financehub.engine.finance.mapper.PostalStorageFeeDetailsMapper;
import com.utfinancing.financehub.engine.finance.service.IPostalStorageFeeDetailsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : hzhao
 * @Date : Create in 2023-11-17
 * @Description :  PostalStorageFeeDetails服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class PostalStorageFeeDetailsServiceImpl extends ServiceImpl<PostalStorageFeeDetailsMapper, PostalStorageFeeDetailsEntity> implements IPostalStorageFeeDetailsService {

    private final PostalStorageFeeDetailsMapper postalStorageFeeDetailsMapper;

    @Override
    public Long savePostalStorageFeeDetails(PostalStorageFeeDetailsDTO dto) {
        PostalStorageFeeDetailsEntity entity = BeanUtil.copyProperties(dto, PostalStorageFeeDetailsEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updatePostalStorageFeeDetails(Long id, PostalStorageFeeDetailsDTO dto) {
        PostalStorageFeeDetailsEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public PostalStorageFeeDetailsDTO getPostalStorageFeeDetailsDTOById(Long id) {
        PostalStorageFeeDetailsEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, PostalStorageFeeDetailsDTO.class);
    }

    @Override
    public IPage<PostalStorageFeeDetailsVO> selectPage(PostalStorageFeeDetailsQueryDTO queryDTO) {
        LambdaQueryWrapper<PostalStorageFeeDetailsEntity> queryWrapper = Wrappers.<PostalStorageFeeDetailsEntity>lambdaQuery();
        //这里注入查询条件
        IPage<PostalStorageFeeDetailsEntity> entityIPage = postalStorageFeeDetailsMapper.selectPage(new Page<PostalStorageFeeDetailsEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, PostalStorageFeeDetailsVO.class);
    }

}

