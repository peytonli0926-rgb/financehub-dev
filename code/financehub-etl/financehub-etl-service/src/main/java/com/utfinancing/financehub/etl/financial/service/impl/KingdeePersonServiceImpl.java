package com.utfinancing.financehub.etl.financial.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.etl.financial.entity.KingdeePersonEntity;
import com.utfinancing.financehub.etl.financial.mapper.KingdeePersonMapper;
import com.utfinancing.financehub.etl.financial.service.IKingdeePersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @Author : lixin
 * @Date : Create in 2024-01-31
 * @Description :  KingdeePerson服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class KingdeePersonServiceImpl extends ServiceImpl<KingdeePersonMapper, KingdeePersonEntity> implements IKingdeePersonService {

    private final KingdeePersonMapper kingdeePersonMapper;


}

