package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.BusinessClaimRepaymentRecordEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.dto.SelectClaimByClientCodeDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SelectClaimDataByConstractInputDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SelectIncomeDateInfoInputDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SelectIncomeDateInfoOutputDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 业务系统对还款认领记录 Mapper 接口
 * </p>
 *
 * @author robjiang
 * @since 2024-03-21
 */
public interface BusinessClaimRepaymentRecordMapper extends BaseMapper<BusinessClaimRepaymentRecordEntity> {
    /**
     * 根据客户编码查询已经认领金额和到账金额总和
     */
    public SelectClaimByClientCodeDTO selectClaimByClientCode(@Param("params") SelectClaimByClientCodeDTO params);

    /**
     * 修改入账日期查询
     */
    public List<SelectIncomeDateInfoOutputDTO> selectIncomeDateInfo(@Param("params") SelectIncomeDateInfoInputDTO params);


    /**
     * 根据合同取得认领总金额
     */
    public BusinessClaimRepaymentRecordEntity selectClaimDataByContract(@Param("params") SelectClaimDataByConstractInputDTO params);
}
