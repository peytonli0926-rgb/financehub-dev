package com.utfinancing.financehub.engine.finance.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-09-13
 * @Description :   Contract查询from对象
 * @Modified :
 */
@ApiModel("Contract查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "客户编号")
    private String clientCode;

    @ApiModelProperty(value = "客户名称")
    private String clientName;

    @ApiModelProperty(value = "组织机编码")
    private List<String> orgIds;

    @ApiModelProperty(value = "起租日")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date leaseDateStart;

    @ApiModelProperty(value = "到期日")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date leaseDateEnd;

    @ApiModelProperty(value = "业务日期开始")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date businessDateStart;

    @ApiModelProperty(value = "业务日期结束")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date businessDateEnd;

    @ApiModelProperty("合同状态")
    private List<String> contractStatuses;

    @ApiModelProperty("财务合同状态")
    private List<String> financialContractStatuses;

    @ApiModelProperty(value = "合同名称或者合同编码")
    private String searchKey;

    @ApiModelProperty(value = "合同编号")
    private List<String> contractCodeList;

    @ApiModelProperty(value = "系统来源编码集合")
    private List<String> systemCodeList;

    @ApiModelProperty(value = "渠道名称")
    private String dealerName;

    @ApiModelProperty(value = "车架号/VIN")
    private String vin;

    @ApiModelProperty(value = "车辆品牌")
    private String brand;

    @ApiModelProperty(value = "车型")
    private String model;

    @ApiModelProperty(value = "起租日开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date leaseDateStartDate;

    @ApiModelProperty(value = "起租日结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date leaseDateStartEndDate;

    @ApiModelProperty(value = "到期日开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date leaseDateEndStartDate;

    @ApiModelProperty(value = "到期日结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date leaseDateEndEndDate;
}
