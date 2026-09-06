package com.utfinancing.financehub.engine.finance.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-04-02
 * @Description :   ParityTransfer查询from对象
 * @Modified :
 */
@ApiModel("ParityTransfer查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class ParityTransferQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "批次")
    private String batch;

    @ApiModelProperty(value = "转让方")
    private String transferParty;

    @ApiModelProperty(value = "业务日期")
    private LocalDateTime businessDate;

    @ApiModelProperty(value = "记账日期")
    private LocalDateTime accountDate;

    @ApiModelProperty(value = "记账开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd",locale = "GMT+8")
    private Date startAccountDate;

    @ApiModelProperty(value = "记账结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd",locale = "GMT+8")
    private Date endAccountDate;

    @ApiModelProperty(value = "受让方")
    private String transfereeParty;

    @ApiModelProperty(value = "1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝")
    private String processStatus;

    @ApiModelProperty(value = "批次数组")
    private List<String> batchList;

    @ApiModelProperty(value = "转让方数组")
    private List<String> transferPartyList;

    @ApiModelProperty(value = "受让方数组")
    private List<String> transfereePartyList;

    @ApiModelProperty(value = "id")
    private Long id;
}
