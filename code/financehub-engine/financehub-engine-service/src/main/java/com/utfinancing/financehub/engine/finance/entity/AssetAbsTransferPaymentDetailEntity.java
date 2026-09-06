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
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 资产转付详情表实体对象
 * </p>
 *
 * @author bruyang
 * @since 2024-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_asset_abs_transfer_payment_detail")
public class AssetAbsTransferPaymentDetailEntity extends Model<AssetAbsTransferPaymentDetailEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //合同编号
    private String contractCode;

    //客户编码
    private String clientCode;

    //客户名称
    private String clientName;

    //转付主表Id
    private Long assetAbsTransferPaymentId;

    //税率
    private String rate;

    //实付本金
    private String actualPrincipalAmount;

    //实付利息
    private String actualInterestAmount;

    //实付留够价
    private String actualRetentionPurchaseAmount;

    //实付罚息及手续费
    private String actualPenaltyInterestAmount;

    //凭证id,多个按照逗号分隔
    private String voucherIds;

    //是否删除（0：未删除1：删除）默认0
    @TableLogic
    private String delFlag;

    //报错信息
    private String errorInfo;

    //会计期间
    private Integer periodCode;

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

    @ApiModelProperty("签约主体")
    private String orgId;

}
