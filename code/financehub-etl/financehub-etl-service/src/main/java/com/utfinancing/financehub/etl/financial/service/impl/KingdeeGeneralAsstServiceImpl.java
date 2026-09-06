package com.utfinancing.financehub.etl.financial.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.etl.financial.entity.KingdeeGeneralAsstEntity;
import com.utfinancing.financehub.etl.financial.mapper.KingdeeGeneralAsstMapper;
import com.utfinancing.financehub.etl.financial.service.IKingdeeGeneralAsstService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @Author : lixin
 * @Date : Create in 2024-01-31
 * @Description :  KingdeeGeneralAsst服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class KingdeeGeneralAsstServiceImpl extends ServiceImpl<KingdeeGeneralAsstMapper, KingdeeGeneralAsstEntity> implements IKingdeeGeneralAsstService {

    private final KingdeeGeneralAsstMapper kingdeeGeneralAsstMapper;


}

