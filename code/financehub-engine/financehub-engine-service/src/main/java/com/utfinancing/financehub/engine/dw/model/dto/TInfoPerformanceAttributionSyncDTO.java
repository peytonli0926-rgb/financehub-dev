package com.utfinancing.financehub.engine.dw.model.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author : lixin
 * @Date : Create in 2023-12-14
 * @Description : 绩效归属信息同步表DTO对象
 * @Modified :
 */
@Data
public class TInfoPerformanceAttributionSyncDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id，自增")
    private Long id;

    @ApiModelProperty(value = "数据来源ID")
    private String dataSourceCode;

    @ApiModelProperty(value = "数据来源")
    private String dataSourceName;

    @ApiModelProperty(value = "项目编号")
    private String projectNo;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "子合同编号")
    private String secContractNo;

    @ApiModelProperty(value = "承租人id")
    private String lesseeCode;

    @ApiModelProperty(value = "承租人名称")
    private String lesseeName;

    @ApiModelProperty(value = "项目经理id")
    private String projectManagerCode;

    @ApiModelProperty(value = "项目经理名称")
    private String projectManagerName;

    @ApiModelProperty(value = "销售额占比")
    private String saleProportion;

    @ApiModelProperty(value = "销售额归属部门code")
    private String saleVolumeBelongDepartCode;

    @ApiModelProperty(value = "销售额归属部门")
    private String saleVolumeBelongDepartName;

    @ApiModelProperty(value = "销售额归属子部门code")
    private String saleVolumeBelongSecDepartCode;

    @ApiModelProperty(value = "销售额归属子部门")
    private String saleVolumeBelongSecDepartName;

    @ApiModelProperty(value = "销售额归属人code")
    private String saleVolumeBelongUserCode;

    @ApiModelProperty(value = "销售额归属人")
    private String saleVolumeBelongUserName;

    @ApiModelProperty(value = "销售额归属直属总监工号")
    private String directDirectorCode;

    @ApiModelProperty(value = "销售额归属直属总监名称")
    private String directDirectorName;

    @ApiModelProperty(value = "销售额归属总经理助理工号")
    private String generalManagerAssistantCode;

    @ApiModelProperty(value = "销售额归属总经理助理名称")
    private String generalManagerAssistantName;

    @ApiModelProperty(value = "销售额归属副总经理工号")
    private String deputyGeneralManagerCode;

    @ApiModelProperty(value = "销售额归属副总经理名称")
    private String deputyGeneralManagerName;

    @ApiModelProperty(value = "销售额归属副总经理(主持工作)工号")
    private String deputyGeneralManagerWorkCode;

    @ApiModelProperty(value = "销售额归属副总经理(主持工作)名称")
    private String deputyGeneralManagerWorkName;

    @ApiModelProperty(value = "销售额归属总经理工号")
    private String generalManagerCode;

    @ApiModelProperty(value = "销售额归属总经理名称")
    private String generalManagerName;

    @ApiModelProperty(value = "资产占比")
    private String assetProportion;

    @ApiModelProperty(value = "资产归属部门code")
    private String assetBelongDepartCode;

    @ApiModelProperty(value = "资产归属部门名称")
    private String assetBelongDepartName;

    @ApiModelProperty(value = "资产归属子部门code")
    private String assetBelongSecDepartCode;

    @ApiModelProperty(value = "资产归属子部门名称")
    private String assetBelongSecDepartName;

    @ApiModelProperty(value = "资产归属人code")
    private String assetBelongUserCode;

    @ApiModelProperty(value = "资产归属人名称")
    private String assetBelongUserName;

    @ApiModelProperty(value = "资产归属直属总监工号")
    private String assetDirectDirectorCode;

    @ApiModelProperty(value = "资产归属直属总监名称")
    private String assetDirectDirectorName;

    @ApiModelProperty(value = "资产归属总经理助理工号")
    private String assetGeneralManagerAssistantCode;

    @ApiModelProperty(value = "资产归属总经理助理名称")
    private String assetGeneralManagerAssistantName;

    @ApiModelProperty(value = "资产归属副总经理工号")
    private String assetDeputyGeneralManagerCode;

    @ApiModelProperty(value = "资产归属副总经理名称")
    private String assetDeputyGeneralManagerName;

    @ApiModelProperty(value = "资产归属副总经理(主持工作)工号")
    private String assetDeputyGeneralManagerWorkCode;

    @ApiModelProperty(value = "资产归属副总经理(主持工作)名称")
    private String assetDeputyGeneralManagerWorkName;

    @ApiModelProperty(value = "资产归属总经理工号")
    private String assetGeneralManagerCode;

    @ApiModelProperty(value = "资产归属总经理名称")
    private String assetGeneralManagerName;

    @ApiModelProperty(value = "收入占比")
    private String incomeProportion;

    @ApiModelProperty(value = "收入归属部门code")
    private String incomeBelongDepartCode;

    @ApiModelProperty(value = "收入归属部门名称")
    private String incomeBelongDepartName;

    @ApiModelProperty(value = "收入归属子部门code")
    private String incomeBelongSecDepartCode;

    @ApiModelProperty(value = "收入归属子部门名称")
    private String incomeBelongSecDepartName;

    @ApiModelProperty(value = "收入归属人code")
    private String incomeBelongUserCode;

    @ApiModelProperty(value = "收入归属人名称")
    private String incomeBelongUserName;

    @ApiModelProperty(value = "收入归属直属总监工号")
    private String incomeDirectDirectorCode;

    @ApiModelProperty(value = "收入归属直属总监名称")
    private String incomeDirectDirectorName;

    @ApiModelProperty(value = "收入归属总经理助理工号")
    private String incomeGeneralManagerAssistantCode;

    @ApiModelProperty(value = "收入归属总经理助理名称")
    private String incomeGeneralManagerAssistantName;

    @ApiModelProperty(value = "收入归属副总经理工号")
    private String incomeDeputyGeneralManagerCode;

    @ApiModelProperty(value = "收入归属副总经理名称")
    private String incomeDeputyGeneralManagerName;

    @ApiModelProperty(value = "收入归属副总经理(主持工作)工号")
    private String incomeDeputyGeneralManagerWorkCode;

    @ApiModelProperty(value = "收入归属副总经理(主持工作)名称")
    private String incomeDeputyGeneralManagerWorkName;

    @ApiModelProperty(value = "收入归属总经理工号")
    private String incomeGeneralManagerCode;

    @ApiModelProperty(value = "收入归属总经理名称")
    private String incomeGeneralManagerName;

    @ApiModelProperty(value = "客户归属部门code")
    private String customerBelongDepartCode;

    @ApiModelProperty(value = "客户归属部门名称")
    private String customerBelongDepartName;

    @ApiModelProperty(value = "客户归属人code")
    private String customerBelongUserCode;

    @ApiModelProperty(value = "客户归属人名称")
    private String customerBelongUserName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

}
