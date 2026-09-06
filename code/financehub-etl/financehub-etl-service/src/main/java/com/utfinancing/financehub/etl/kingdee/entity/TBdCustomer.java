package com.utfinancing.financehub.etl.kingdee.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author : lixin
 * @Date : Create in 14/12/2023
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "T_BD_CUSTOMER", schema = "HXORACLE")
public class TBdCustomer {

    @TableField("FNUMBER")
    private String fnumber;

    @TableField("FNAME_L2")
    private String fnameL2;

}
