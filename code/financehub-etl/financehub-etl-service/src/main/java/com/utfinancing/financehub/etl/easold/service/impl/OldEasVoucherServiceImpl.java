package com.utfinancing.financehub.etl.easold.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.etl.easold.model.dto.EasVoucherDTO;
import com.utfinancing.financehub.etl.easold.entity.TGlVoucherEntity;
import com.utfinancing.financehub.etl.easold.mapper.OldEasVoucherMapper;
import com.utfinancing.financehub.etl.easold.model.dto.QueryEas1VoucherInputDTO;
import com.utfinancing.financehub.etl.easold.service.OldEasVoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description :  TGlVoucher服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
public class OldEasVoucherServiceImpl extends ServiceImpl<OldEasVoucherMapper, TGlVoucherEntity> implements OldEasVoucherService {

    private final OldEasVoucherMapper tGlVoucherMapper;


    @Override
    public List<EasVoucherDTO> selectEas1Voucher(QueryEas1VoucherInputDTO param) {
        return tGlVoucherMapper.queryEas1Voucher(param);
    }
}

