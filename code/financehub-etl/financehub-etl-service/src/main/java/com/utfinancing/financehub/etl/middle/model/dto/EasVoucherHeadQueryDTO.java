package com.utfinancing.financehub.etl.middle.model.dto;
import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-28
 * @Description :   EasVoucherHead查询from对象
 * @Modified :
 */
@ApiModel("EasVoucherHead查询表单")
@Data
@EqualsAndHashCode(callSuper = true)
public class EasVoucherHeadQueryDTO extends BaseQueryDTO{

    @ApiModelProperty(value = "系统名称")
    private String system;

    @ApiModelProperty(value = "优先级(5，30）")
    private BigDecimal priority;

    @ApiModelProperty(value = "模块名称")
    private String modelname;

    @ApiModelProperty(value = "财务组织")
    private String orgnumber;

    @ApiModelProperty(value = "业务流程编号（单据ID（业务系统唯一码）")
    private String bzprocid;

    @ApiModelProperty(value = "凭证号（另定义存储过程来生成该号码）")
    private String vouchernumber;

    @ApiModelProperty(value = "业务日期")
    private LocalDateTime bzdate;

    @ApiModelProperty(value = "记账日期")
    private LocalDateTime financialdate;

    @ApiModelProperty(value = "期间年")
    private String periodyear;

    @ApiModelProperty(value = "期间月")
    private String periodmonth;

    @ApiModelProperty(value = "凭证类型（凭证字）")
    private String vouchertype;

    @ApiModelProperty(value = "合同号")
    private String contractid;

    @ApiModelProperty(value = "单据状态(0未完整，1已完整)")
    private String billstatus;

    @ApiModelProperty(value = "状态（1新增、2修改、3作废、9删除）")
    private String status;

    @ApiModelProperty(value = "备注")
    private String memo;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "更新人")
    private String modificator;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime modifyDate;

    @ApiModelProperty(value = "EAS标记（0待处理，1成功、2失败）")
    private String easflag;

    @ApiModelProperty(value = "EAS操作信息")
    private String easreason;

    @ApiModelProperty(value = "EAS操作时间")
    private LocalDateTime easoptime;

    @ApiModelProperty(value = "EAS业务编码")
    private String easbzcode;

    @ApiModelProperty(value = "业务标记（0待处理，1成功、2失败）")
    private String bzflag;

    @ApiModelProperty(value = "业务操作信息")
    private String bzreason;

    @ApiModelProperty(value = "业务操作时间")
    private LocalDateTime bzoptime;

    @ApiModelProperty(value = "")
    private String description;

    @ApiModelProperty(value = "EAS审核后凭证编码")
    private String easVouchernumber;

    @ApiModelProperty(value = "互联魔方报销单ID")
    private String bxBillid;

    @ApiModelProperty(value = "互联魔方报销单页面ID")
    private String bxMainid;
}
