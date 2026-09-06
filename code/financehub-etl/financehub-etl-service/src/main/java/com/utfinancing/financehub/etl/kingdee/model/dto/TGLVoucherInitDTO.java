package com.utfinancing.financehub.etl.kingdee.model.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
public class TGLVoucherInitDTO {
    private String fid;

    private String orgId;

    private String period;

    private String startPeriod;

    private String endPeriod;

    private String contractCode;

    private String clientCode;


    private String accountNumber;

    private String accountName;

    private String fabStract;

    private BigDecimal dtAmount;

    private BigDecimal crAmount;
}
