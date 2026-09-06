package com.utfinancing.financehub.engine.finance.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-22
 * @Description :   ConvertTransfer查询from对象
 * @Modified :
 */
@ApiModel("ConvertTransfer查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class ConvertTransferQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "批次")
    private List<String> batchList;

    @ApiModelProperty(value = "记账开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date accountStartDate;
    @ApiModelProperty(value = "记账结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date accountEndDate;

    @ApiModelProperty(value = "转让方")
    private List<String> transferPartyList;

    @ApiModelProperty(value = "受让方")
    private List<String> transfereePartyList;

    @ApiModelProperty(value = "IDList")
    private List<Long> idList;

    @ApiModelProperty(value = "处理状态集合")
    private List<String> processStatusList;

}
