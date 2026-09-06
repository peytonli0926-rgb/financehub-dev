package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 合同状态记录临时表实体对象
 * </p>
 *
 * @author robjiang
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_contract_status_record_temp")
public class ContractStatusRecordTempEntity extends Model<ContractStatusRecordTempEntity> {

    private static final long serialVersionUID = 1L;

    //合同编码
    @TableId(value = "contract_code", type = IdType.ASSIGN_ID)
    private String contractCode;

    //签约主体
    private String orgId;

    //状态
    private String financialContractStatus;

    //更新时间
    private Date financialContractStatusUpdateTime;

    /**
     * @description:转入公司
     **/
    private String transferOrgId;

    /**
     * @description:转入合同号
     **/
    private String transferContractCode;

    /**
     * @description:转入合同系统合同状态
     **/
    private String transferContractStatus;

}
