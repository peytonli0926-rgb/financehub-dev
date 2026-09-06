package com.utfinancing.financehub.etl.financial.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.etl.financial.entity.KingdeeCostcenterEntity;
import com.utfinancing.financehub.etl.financial.mapper.KingdeeCostcenterMapper;
import com.utfinancing.financehub.etl.financial.service.IKingdeeCostcenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @Author : lixin
 * @Date : Create in 2024-01-31
 * @Description :  KingdeeCostcenter服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class KingdeeCostcenterServiceImpl extends ServiceImpl<KingdeeCostcenterMapper, KingdeeCostcenterEntity> implements IKingdeeCostcenterService {

    private final KingdeeCostcenterMapper kingdeeCostcenterMapper;


}

