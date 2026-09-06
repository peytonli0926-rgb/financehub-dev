package com.utfinancing.financehub.etl.financial.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.etl.financial.entity.KingdeeBankEntity;
import com.utfinancing.financehub.etl.financial.mapper.KingdeeBankMapper;
import com.utfinancing.financehub.etl.financial.service.IKingdeeBankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @Author : lixin
 * @Date : Create in 2024-01-31
 * @Description :  KingdeeBank服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class KingdeeBankServiceImpl extends ServiceImpl<KingdeeBankMapper, KingdeeBankEntity> implements IKingdeeBankService {

    private final KingdeeBankMapper kingdeeBankMapper;


}

