package com.utfinancing.financehub.admin.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2023-11-17
 * @Description : 中台角色下的用户表VO对象
 * @Modified :
 */
@Data
public class SysInternalUserVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "用户编码")
    private String userCode;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "是否删除（0：否，1：是）")
    private String delFlag;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "下级用户信息列表")
    private List<SysInternalUserVO> subUserList;

    @ApiModelProperty(value = "用户对应角色列表")
    private List<SysInternalRoleVO> roleList;

}
