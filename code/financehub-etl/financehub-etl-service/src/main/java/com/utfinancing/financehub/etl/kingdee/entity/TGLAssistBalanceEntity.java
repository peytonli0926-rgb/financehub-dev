package com.utfinancing.financehub.etl.kingdee.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "T_GL_AssistBalance_5F", schema = "HXORACLE")
public class TGLAssistBalanceEntity {
    private String contractCode;

    private String clientCode;

    private String currency;

    private String accountNumber;

    private String accountName;

    private String orgId;

    private BigDecimal endBalanceFor;

    private BigDecimal beginBalanceFor;

    private String period;
}
