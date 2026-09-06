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
public class OrgDTO {

    @ApiModelProperty(value = "部门编号")
    private String orgCode;

    @ApiModelProperty(value = "部门代码")
    private String orgAuthCode;

    @ApiModelProperty(value = "部门名称")
    private String orgName;

    @ApiModelProperty(value = "部门类型编号")
    private String orgTypeCode;

    @ApiModelProperty(value = "部门类型名称")
    private String orgTypeName;

    @ApiModelProperty(value = "部门级别编号")
    private String orgGradeCode;

    @ApiModelProperty(value = "部门级别名称")
    private String orgGradeName;

    @ApiModelProperty(value = "部门属性编码")
    private String orgPropertyCode;

    @ApiModelProperty(value = "部门属性名称")
    private String orgPropertyName;

    @ApiModelProperty(value = "负责人工号")
    private String managerStaffCode;

    @ApiModelProperty(value = "负责人姓名")
    private String managerStaffName;

    @ApiModelProperty(value = "分管领导工号")
    private String managerLeaderStaffCode;

    @ApiModelProperty(value = "分管领导姓名")
    private String managerLeaderStaffName;

    @ApiModelProperty(value = "管理单元id")
    private Integer ouid;

    @ApiModelProperty(value = "管理单元名称")
    private String ouidName;

    @ApiModelProperty(value = "上级机构编码")
    private String parentOrgCode;

    @ApiModelProperty(value = "上级机构名称")
    private String parentOrgName;

    @ApiModelProperty(value = "是否禁用")
    private Integer disabled;

    @ApiModelProperty(value = "是否内部")
    private Integer isInnerFlag;

    @ApiModelProperty(value = "是否启用")
    private Integer enabled;

    @ApiModelProperty(value = "是否虚拟部门")
    private Integer isVirtualFlag;

    @ApiModelProperty(value = "下级部门列表")
    private List<OrgDTO> childOrgList;

    @ApiModelProperty(value = "岗位列表")
    private List<PositionDTO> positionList;

    @ApiModelProperty(value = "员工列表")
    private List<StaffDTO> staffList;

}
