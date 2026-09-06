package com.utfinancing.financehub.etl.middle.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.easold.model.dto.EasVoucherDTO;
import com.utfinancing.financehub.etl.easold.model.dto.QueryEas1VoucherInputDTO;
import com.utfinancing.financehub.etl.financial.entity.KingdeeMiddleVoucherEntity;
import com.utfinancing.financehub.etl.middle.model.dto.EasVoucherHeadQueryDTO;
import com.utfinancing.financehub.etl.middle.model.dto.EasVoucherHeadDTO;
import com.utfinancing.financehub.etl.middle.model.dto.MidVoucherEntryDTO;
import com.utfinancing.financehub.etl.middle.model.vo.EasVoucherHeadVO;
import com.utfinancing.financehub.etl.middle.entity.EasVoucherHeadEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-28
 * @Description : EasVoucherHead服务类接口
 * @Modified :
 */
@DS("slave_middle")
public interface IEasVoucherHeadService extends IService<EasVoucherHeadEntity> {


    EasVoucherHeadEntity selectMidVoucherHeaderByEasId(String easId);


    List<EasVoucherHeadEntity> selectMidVoucherHeaderByEasIds(List<String> easIds);


    List<EasVoucherHeadEntity> selectMidVoucherHeaderByDate(String voucherDate);


    String exportMidVoucherEntryByDate(String voucherDate);

    String exportMidVoucherAsstacttByDate(String voucherDate);

    String exportMidVoucherIncloudAsscateByDate(String voucherDate);

    String exportMiddleData(String statement, String columns);

    /**
     * 查询金蝶中间库凭证信息 提交人，审核，过账人固定
     * @param periodYear
     * @param periodMonth
     * @param voucherDate
     * @return
     */
    List<EasVoucherDTO> selectMiddleEasVoucher(Integer periodYear,Integer periodMonth,String voucherDate);

    /**
     * 查询暂存的金蝶中间库凭证信息
     * @param periodYear
     * @param periodMonth
     * @param voucherDate
     * @return
     */
    List<EasVoucherDTO> selectStageMiddleEasVoucher(Integer periodYear,Integer periodMonth,String voucherDate);


    /**
     * 查询暂存的金蝶中间库凭证信息
     * @param periodYear
     * @param periodMonth
     * @param voucherDate
     * @return
     */
    List<EasVoucherDTO> selectStageZJXTMiddleEasVoucher(Integer periodYear,Integer periodMonth,String voucherDate);

    List<KingdeeMiddleVoucherEntity> selectKingdeeMiddleGaxdVoucher(Integer year, Integer month, String voucherDate);


}
