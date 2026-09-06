package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * <p>
 * 减值计提明细实体对象
 * </p>
 *
 * @author wenbin
 * @since 2024-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@TableName("eg_impairment_provision_detail")
public class ImpairmentProvisionDetailEntity extends Model<ImpairmentProvisionDetailEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //合同编号(核算项目)
    private String contractCode;

    //合同名称
    private String contractName;

    //客户编号
    private String clientCode;

    //客户名称
    private String clientName;

    //减值类型
    private String impairmentType;

    //业务类型
    private String businessType;

    //签约主体
    private String orgId;

    //五级分类
    private String fiveClass;

    //三阶段
    private String threeStep;

    //风险敞口
    private BigDecimal riskExposure;

    //拨备合计
    private BigDecimal provisionTotal;

    //上月余额
    private BigDecimal lastMonthBalance;

    //本月计提
    private BigDecimal thisMonthProvision;

    //凭证id(多个逗号分隔)
    private String voucherId;

    //财务日期
    private LocalDateTime accountDate;

    //是否删除（0-否，1-是）
    @TableLogic
    private String delFlag;

    //创建人
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //更新人
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    //更新时间
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    //是否核销
    private String isVerification;

    //应收租金余额
    private BigDecimal rentReceivableBalance;

    //中台应收租金余额
    private BigDecimal hubRentReceivableBalance;

    //导入的excel类型
    private String excelType;

    //生成凭证报错信息
    private String errorInfo;

    // 金融机构
    private String financialInstitution;

    @ApiModelProperty(name = "减值计提id")
    private Long impairmentProvisionId;

    @ApiModelProperty(value = "目标币种本月计提金额")
    private BigDecimal targetAmount;

    @ApiModelProperty(value = "汇率")
    private BigDecimal exchangeRate;

    @ApiModelProperty(name = "原币币种")
    private String originCurrency;

    @ApiModelProperty(name = "目标币种")
    private String targetCurrency;

    @ApiModelProperty(name = "历史记录标识位(Y:历史记录;N:非历史记录)")
    private String historyFlag;

    @ApiModelProperty(name = "标识（1:合同已结束;2:合同已核销;3:境外主体不生成凭证;4:凭证校验失败无法生成凭证）;5:凭证生成成功;6:凭证生成失败;7:资产处置结束（内部转让）")
    private String importFlag;

    @ApiModelProperty(value = "财务合同状态")
    @Excel(name = "财务合同状态")
    private String financialContractStatus;

}
