package com.utfinancing.financehub.engine.integration.model.dto;

import lombok.Data;

/**
 * @Author : lixin
 * @Date : Create in 08/10/2023
 */
@Data
public class OrgPositionOrgReqDTO {

    //部门编号
    private String orgCode;

    //是否需要岗位下的人员信息 0：否 1：是
    private Integer isNeedPositionEmp;

}
