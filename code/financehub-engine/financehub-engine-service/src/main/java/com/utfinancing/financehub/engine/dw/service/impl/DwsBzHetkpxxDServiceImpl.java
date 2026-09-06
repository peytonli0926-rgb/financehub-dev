package com.utfinancing.financehub.engine.dw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.dw.entity.DwsBzHetkpxxDEntity;
import com.utfinancing.financehub.engine.dw.mapper.DwsBzHetkpxxDMapper;
import com.utfinancing.financehub.engine.dw.service.IDwsBzHetkpxxDService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @Author : lixin
 * @Date : Create in 2023-12-14
 * @Description :  DwsBzHetkpxxD服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class DwsBzHetkpxxDServiceImpl extends ServiceImpl<DwsBzHetkpxxDMapper, DwsBzHetkpxxDEntity> implements IDwsBzHetkpxxDService {

    private final DwsBzHetkpxxDMapper dwsBzHetkpxxDMapper;


}

