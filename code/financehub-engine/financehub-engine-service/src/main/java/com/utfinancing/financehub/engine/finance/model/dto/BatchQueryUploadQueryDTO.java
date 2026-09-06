package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import com.utfinancing.financehub.engine.hthx.page.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-07
 * @Description :   RecyclingEquipmentIn查询from对象
 * @Modified :
 */
@ApiModel("BatchQueryUpload查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class BatchQueryUploadQueryDTO extends PageParam {

    @ApiModelProperty(value = "币种")
    private String currencyCode;

//    @ApiModelProperty(value = "签约主体")
//    private String orgId;

    @ApiModelProperty(value = "签约主体id集合")
    private List<String> orgIdList;

    @ApiModelProperty(value = "审批状态集合")
    private List<String> processStatusList;

    @ApiModelProperty(value = "排除范围集合")
    //排除范围 无发生不展示/余额为零且无发生不显示/余额为零不显示/本年无发生额不显示/余额为零且本年无发生不显示
    private List<String> exceptList;

//    @ApiModelProperty(value = "会计期间 开始")
//    private Integer periodCodeStart;
//
//    @ApiModelProperty(value = "会计期间 结束")
//    private Integer periodCodeEnd;

    @ApiModelProperty(value = "查询日期")
    private String queryDate;

    @ApiModelProperty(value = "科目编号集合")
    private List<String> accountCodeList;

    @ApiModelProperty(value = "合同编号集合")
    private List<String> contractCodeList;

    @ApiModelProperty(value = "科目字段集合")
    private List<String> fundTypeList;

    @ApiModelProperty(value = "会计期间")
    private Integer periodCode;

    private Integer limit;
    private Integer offset;

    private String queryType;

    @ApiModelProperty(value = "导出类型 common, detail")
    private String exportType;
}
