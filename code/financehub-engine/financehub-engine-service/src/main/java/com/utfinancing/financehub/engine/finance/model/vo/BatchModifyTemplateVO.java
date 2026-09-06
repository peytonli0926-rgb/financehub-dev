package com.utfinancing.financehub.engine.finance.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : robjiang
 * @Date : Create in 2025-06-23
 * @Description : 未确认收款-对账表批量修改上传模板表VO对象
 * @Modified :
 */
@Data
public class BatchModifyTemplateVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "对账月份")
    private String accountCheckingMonth;

    @ApiModelProperty(value = "到账主体")
    private String collectionAccountsBank;

    @ApiModelProperty(value = "系统编码")
    private String systemCode;

    @ApiModelProperty(value = "业务系统的网银编号-小网银")
    private String ebankSerialNumber;

    @ApiModelProperty(value = "comments")
    private String remark;

    @ApiModelProperty(value = "财务初分类")
    private String financialPrimaryClassic;

    @ApiModelProperty(value = "运营部确认款项性质")
    private String confirmAccountProperty;

    @ApiModelProperty(value = "非租对账备注")
    private String nonLeaseAccountCheckingComments;

    @ApiModelProperty(value = "运营部历史备注")
    private String operateHistoryComments;

}
