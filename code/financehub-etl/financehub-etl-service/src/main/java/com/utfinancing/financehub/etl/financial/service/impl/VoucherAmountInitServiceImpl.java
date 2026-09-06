package com.utfinancing.financehub.etl.financial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.etl.financial.entity.OutstandingAmountInitEntity;
import com.utfinancing.financehub.etl.financial.entity.VoucherAmountInitEntity;
import com.utfinancing.financehub.etl.financial.mapper.VoucherAmountInitMapper;
import com.utfinancing.financehub.etl.financial.service.IVoucherAmountInitService;
import com.utfinancing.financehub.etl.kingdee.entity.TGLAssistBalanceEntity;
import com.utfinancing.financehub.etl.kingdee.model.dto.TGLVoucherInitDTO;
import com.utfinancing.financehub.etl.kingdee.service.ITGLAssistBalanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
/**
 * @Author : robjiang
 * @Date : Create in 2024-01-10
 * @Description :  VoucherAmountInit服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class VoucherAmountInitServiceImpl extends ServiceImpl<VoucherAmountInitMapper, VoucherAmountInitEntity>
        implements IVoucherAmountInitService {

    @Resource
    private final VoucherAmountInitMapper voucherAmountInitMapper;

    @Resource
    private ITGLAssistBalanceService iTGLAssistBalanceService;

    @Override
    public R<List<TGLVoucherInitDTO>> voucherAmountInit(TGLVoucherInitDTO dto) {
        log.info("金蝶数据查询(凭证查询)开始时间："+ DateUtils.dateTimeNow());
        List<TGLVoucherInitDTO> voucherInitDtoList = iTGLAssistBalanceService.voucherInitQuery(dto);
        log.info("金蝶数据查询(凭证查询)结束时间："+ DateUtils.dateTimeNow());
        if (voucherInitDtoList != null && !voucherInitDtoList.isEmpty()) {
            List<VoucherAmountInitEntity> voucherAmountInitEntityList = BeanUtil.copyToList(voucherInitDtoList, VoucherAmountInitEntity.class);

            LambdaQueryWrapper<VoucherAmountInitEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(VoucherAmountInitEntity :: getContractCode, dto.getContractCode());
//        wrapper.eq(VoucherAmountInitEntity :: getPeriod, dto.getPeriod());
            voucherAmountInitMapper.delete(wrapper);
            this.saveBatch(voucherAmountInitEntityList);
        }
        return R.ok(voucherInitDtoList);
    }
}

