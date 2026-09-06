package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 凭证表实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-09-01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("eg_voucher")
public class VoucherEntity extends Model<VoucherEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //接口表ID
    private Long interfaceDataId;

    //来源;refInterface
    private String source;

    //来源系统编码
    private String systemCode;

    //来源系统名称
    private String systemName;

    //业务编码
    private String businessCode;

    //业务名称
    private String businessName;

    //业务交易流水号
    private String orderId;

    //业务场景编码
    private String sceneCode;

    //业务场景名称
    private String sceneName;

    //合同编号
    private String contractCode;

    //合同名称
    private String contractName;

    //客户编号
    private String clientCode;

    //客户名称
    private String clientName;

    //组织机编码
    private String orgId;

    //组织机构名称
    private String orgName;

    //凭证类型;refDict
    private String voucherType;

    //公司
    private String signCompany;

    //业务日期
    private LocalDateTime businessDate;

    //凭证日期
    private LocalDateTime voucherDate;

    //币种
    private String currency;

    //部门
    private String deptName;

    //凭证摘要
    private String voucherSummary;

    //创建人
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //更新人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    //删除标识(0:未删除,1:已删除)
//    @TableLogic
    private String delFlag;

    //凭证号
    private Long voucherNum;

    //凭证生成方式：auto:自动凭证, manual:手工凭证
    private String voucherWay;

    //制单人工号
    private String createUserNo;

    //制单人姓名
    private String createUserName;

    //复核人工号
    private String recheckUserNo;

    //复核人姓名
    private String recheckUserName;

    //凭证状态
    private String voucherStatus;

    //批次ID
    private Long batchId;

    //批次类型
    private String batchType;

    //细分场景
    private String subSceneType;

    //会计期间
    private Integer periodCode;

    //有效标识1:有效 2:凭证行为空 3:借贷金额未平
    private String validFlag;

    @ApiModelProperty(value = "成本中心")
    private String costCentre;

    @ApiModelProperty(value = "员工姓名")
    private String employeeName;

    @ApiModelProperty(value = "费用类型")
    private String expenseType;

    @ApiModelProperty(value = "金融机构")
    private String financialInstitution;

    @ApiModelProperty(value = "银行账号")
    private String bankNo;

    @ApiModelProperty(value = "外部接口Id")
    private Long interfaceId;

    @ApiModelProperty(value = "传送金蝶是否汇总金额，0：汇总，1：不汇总")
    private String isSummary;

    @ApiModelProperty(value = "金蝶凭证ID")
    private String easVoucherId;

    @ApiModelProperty(value = "金蝶凭证号")
    private String easVoucherNumber;

    @ApiModelProperty(value = "是否冲销，0：未冲销，1：已冲销")
    private String isWriteOff;

    @ApiModelProperty(value = "手工凭证id")
    private Long manualId;

    private String easbzcode;

    @ApiModelProperty(value = "是否发送金蝶（0：未发送，1：已发送 2：发送中）该字段只针对不汇总分录数据'")
    private String isSendKingdee;
}
