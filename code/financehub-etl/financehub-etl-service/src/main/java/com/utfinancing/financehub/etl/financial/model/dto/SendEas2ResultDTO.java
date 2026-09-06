package com.utfinancing.financehub.etl.financial.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : bruyang
 * @Date : Create in 2024-04-10
 * @Description : 记录发送EAS2数据是否成功表DTO对象
 * @Modified :
 */
@Data
public class SendEas2ResultDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "fid")
    private String fid;

    @ApiModelProperty(value = "是否成功（sucs：成功，errs：失败）")
    private String isSuccess;

    @ApiModelProperty(value = "数据来源：eas1:EAS1,金蝶中间库：KINGDEE_MIDDLE,中台：FINHUB")
    private String systemCode;

}
