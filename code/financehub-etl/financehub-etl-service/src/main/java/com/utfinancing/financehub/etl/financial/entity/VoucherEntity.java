package com.utfinancing.financehub.etl.financial.entity;

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
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
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
    @TableLogic
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

    private Integer periodCode;

    //金蝶凭证ID
    private String easVoucherId;

    //金蝶凭证号
    private String easVoucherNumber;

    private String easbzcode;

    private String isSendKingdee;
}
