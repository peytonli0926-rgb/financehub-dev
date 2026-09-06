package com.utfinancing.financehub.etl.easold.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.etl.easold.model.dto.EasVoucherDTO;
import com.utfinancing.financehub.etl.easold.model.dto.QueryEas1VoucherInputDTO;
import com.utfinancing.financehub.etl.easold.entity.TGlVoucherEntity;
import com.utfinancing.financehub.etl.financial.model.vo.KingdeeHybVoucherVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description : TGlVoucher服务类接口
 * @Modified :
 */
@DS("slave_kingdee")
public interface OldEasVoucherService extends IService<TGlVoucherEntity> {

    List<EasVoucherDTO> selectEas1Voucher(QueryEas1VoucherInputDTO param);
}
