package com.utfinancing.financehub.etl.financial.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.etl.financial.entity.VoucherToEasResultEntity;
import com.utfinancing.financehub.etl.financial.mapper.VoucherToEasResultMapper;
import com.utfinancing.financehub.etl.financial.service.IVoucherToEasResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : robjiang
 * @Date : Create in 2024-02-29
 * @Description :  VoucherToEasResult服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class VoucherToEasResultServiceImpl extends ServiceImpl<VoucherToEasResultMapper, VoucherToEasResultEntity>
        implements IVoucherToEasResultService {

    private final VoucherToEasResultMapper voucherToEasResultMapper;


}

