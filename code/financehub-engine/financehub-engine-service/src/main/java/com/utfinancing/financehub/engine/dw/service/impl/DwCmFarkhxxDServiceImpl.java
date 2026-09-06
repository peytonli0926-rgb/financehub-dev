package com.utfinancing.financehub.engine.dw.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.dw.entity.DwCmFarkhxxDEntity;
import com.utfinancing.financehub.engine.dw.mapper.DwCmFarkhxxDMapper;
import com.utfinancing.financehub.engine.dw.service.IDwCmFarkhxxDService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-12-14
 * @Description :  DwCmFarkhxxD服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class DwCmFarkhxxDServiceImpl extends ServiceImpl<DwCmFarkhxxDMapper, DwCmFarkhxxDEntity> implements IDwCmFarkhxxDService {

    private final DwCmFarkhxxDMapper dwCmFarkhxxDMapper;


    /**
     * 根据客户编码查询
     * @param clientCode
     * @return
     */
    @Override
    public List<DwCmFarkhxxDEntity> selectByClientCode(String clientCode) {
        if(ObjectUtil.isEmpty(clientCode)){
            return null;
        }
        List<DwCmFarkhxxDEntity> list = list(new LambdaQueryWrapper<DwCmFarkhxxDEntity>().eq(DwCmFarkhxxDEntity::getVcKehbh, clientCode));
        return list;
    }
}

