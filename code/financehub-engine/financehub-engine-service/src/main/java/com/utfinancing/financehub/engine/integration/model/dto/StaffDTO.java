package com.utfinancing.financehub.engine.integration.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 08/10/2023
 */
@NoArgsConstructor
@Data
public class StaffDTO {

    @ApiModelProperty(value = "员工号")
    private String staffCode;

    @ApiModelProperty(value = "姓名")
    private String staffName;

    @ApiModelProperty(value = "一级部门编码")
    private String org1Code;

    @ApiModelProperty(value = "一级部门名称")
    private String org1Name;

    @ApiModelProperty(value = "二级部门编码")
    private String org2Code;

    @ApiModelProperty(value = "二级部门名称")
    private String org2Name;

    @ApiModelProperty(value = "三级部门编码")
    private String org3Code;

    @ApiModelProperty(value = "三级部门名称")
    private String org3Name;

    @ApiModelProperty(value = "岗位编码")
    private String positionCode;

    @ApiModelProperty(value = "岗位名称")
    private String positionName;

    @ApiModelProperty(value = "在职状态")
    private String onJobStatus;

    @ApiModelProperty(value = "上级工号")
    private String reportToStaffCode;

    @ApiModelProperty(value = "上级姓名")
    private String reportToStaffName;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "电话")
    private String mobile;

    @ApiModelProperty(value = "入职日期")
    private String firstHiredDate;

    @ApiModelProperty(value = "离职日期")
    private String leaveDate;

    @ApiModelProperty(value = "管理单元编码")
    private Integer ouid;

    @ApiModelProperty(value = "管理单元名称")
    private String ouidName;

    @ApiModelProperty(value = "区域")
    private String area;

    @ApiModelProperty(value = "办公电话")
    private String officePhone;

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value = "内部标识")
    private Integer isInnerFlag;

    @ApiModelProperty(value = "岗位列表")
    private List<PositionDTO> positionList;


}
