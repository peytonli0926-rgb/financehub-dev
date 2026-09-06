package com.utfinancing.financehub.engine.dw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.dw.entity.DwCmGerkhxxDEntity;
import com.utfinancing.financehub.engine.dw.mapper.DwCmGerkhxxDMapper;
import com.utfinancing.financehub.engine.dw.service.IDwCmGerkhxxDService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @Author : lixin
 * @Date : Create in 2023-12-14
 * @Description :  DwCmGerkhxxD服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class DwCmGerkhxxDServiceImpl extends ServiceImpl<DwCmGerkhxxDMapper, DwCmGerkhxxDEntity> implements IDwCmGerkhxxDService {

    private final DwCmGerkhxxDMapper dwCmGerkhxxDMapper;


}

