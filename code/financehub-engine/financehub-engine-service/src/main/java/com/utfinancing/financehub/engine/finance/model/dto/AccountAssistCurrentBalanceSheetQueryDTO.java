package com.utfinancing.financehub.engine.finance.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2024-01-05
 * @Description :   AccountAssistBalance查询from对象
 * @Modified :
 */
@ApiModel("本期发生额查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class AccountAssistCurrentBalanceSheetQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "会计期间")
    private Integer periodCode;

    @ApiModelProperty(value = "科目编码")
    private String accountCode;

    @ApiModelProperty(value = "签约主体")
    private String orgId;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "查询类型(前端输入)：query 分页查询 export 下载")
    private String queryType;

    @ApiModelProperty(value = "查询类型(前端输入)：assist 核算项目余额表-本期发生额明细 account 科目余额表-本期发生额明细 contract 科目余额表-本期发生额合同明细")
    private String targetType;

    @ApiModelProperty(value = "会计期间开始")
    private Integer periodCodeStart;

    @ApiModelProperty(value = "会计期间结束")
    private Integer periodCodeEnd;

    private List<Integer> periodCodeList;

    private Integer limit;
    private Integer offset;
}
