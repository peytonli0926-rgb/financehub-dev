package com.utfinancing.financehub.etl.financial.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.etl.financial.entity.VoucherToEasRecordEntity;
import com.utfinancing.financehub.etl.financial.mapper.VoucherToEasRecordMapper;
import com.utfinancing.financehub.etl.financial.service.IVoucherToEasRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author : robjiang
 * @Date : Create in 2024-02-29
 * @Description :  VoucherToEasRecord服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class VoucherToEasRecordServiceImpl extends ServiceImpl<VoucherToEasRecordMapper, VoucherToEasRecordEntity>
        implements IVoucherToEasRecordService {

    private final VoucherToEasRecordMapper voucherToEasRecordMapper;


}

