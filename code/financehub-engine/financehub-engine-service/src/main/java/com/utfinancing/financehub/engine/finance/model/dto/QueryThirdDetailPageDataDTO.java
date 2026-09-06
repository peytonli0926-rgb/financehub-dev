package com.utfinancing.financehub.engine.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("明细页面（第三层）查询表单")
public class QueryThirdDetailPageDataDTO extends BaseQueryDTO implements Serializable {

    @ApiModelProperty(value = "汇总数据ID")
    private Long sumId;

    @ApiModelProperty(value = "业务系统网银编号/批次号")
    private String businessEbankNumber;

    @ApiModelProperty(value = "业务系统批扣流水号")
    private String ebankSerialNumber;

    //到账银行账号
    @ApiModelProperty(value = "到账银行账号")
    private String collectionAccountsBankNo;

    @ApiModelProperty(value = "最后变化开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lastChangeDateStart;

    @ApiModelProperty(value = "最后变化结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lastChangeDateEnd;

    @ApiModelProperty(value = "网银到账开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date businessDateStart;

    @ApiModelProperty(value = "网银到账结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date businessDateEnd;

    @ApiModelProperty(value = "付款客户")
    private String clientName;

    @ApiModelProperty(value = "剩余未确认金额是否为0; 1:非零，0：零")
    private String isZeroForNonConfirmAmount;

//    @ApiModelProperty(value = "异常类型")
//    private String exceptionType;
//
//    @ApiModelProperty(value = "列表数据")
//    private List<QueryThirdDetailPageListDataDTO> listData = new ArrayList<>();

}
