package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.easold.model.dto.EasVoucherDTO;
import com.utfinancing.financehub.etl.financial.model.dto.VoucherEntryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.VoucherQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.VoucherDTO;
import com.utfinancing.financehub.etl.financial.model.vo.VoucherVO;
import com.utfinancing.financehub.etl.financial.entity.VoucherEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-16
 * @Description : Voucher服务类接口
 * @Modified :
 */
public interface IVoucherService extends IService<VoucherEntity> {

    Long saveVoucher(VoucherDTO dto);

    Long updateVoucher(Long id, VoucherDTO dto);

    VoucherDTO getVoucherDTOById(Long id);

    IPage<VoucherVO> selectPage(VoucherQueryDTO queryDTO);

    String exportFinhubVoucherEntryByDate(String voucherDate);


    String exportFinhubData(String statement, String columns);

    /**
     * 数据来源为
     * 统一平台
     * 小微系统
     * 商用车
     * 乘用车
     * 恒运保
     * 资金系统（网银收款）
     * 资金系统（网银收款）
     * @param periodCode
     * @param voucherDate
     * @return
     */
    List<EasVoucherDTO> selectFinhubVoucherData(int periodCode, String voucherDate);

    /**
     * 中台内部生成的凭证数据
     * 数据来源等于FINHUB
     * @param periodCode
     * @param voucherDate
     * @return
     */
    List<EasVoucherDTO> selectFinhubInterVoucherData(int periodCode, String voucherDate);

    /**
     * 数据来源为
     * 统一平台
     * 小微系统
     * 商用车
     * 乘用车
     * 恒运保
     * 资金系统（网银收款）
     * 资金系统（网银收款）
     * @param periodCode
     * @param voucherDate
     * @param isEntryFlag
     * @return
     */
    List<EasVoucherDTO> selectNoSummaryFinhubVoucherData(int periodCode, String voucherDate,boolean isEntryFlag);

    /**
     * 中台内部生成的凭证数据
     * 数据来源等于FINHUB
     * @param periodCode
     * @param voucherDate
     * @return
     */
    List<EasVoucherDTO> selectNoSummaryFinhubInterVoucherData(int periodCode, String voucherDate,boolean isEntryFlag);


}
