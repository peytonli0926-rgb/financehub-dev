package com.utfinancing.financehub.engine.rule.entity;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 接口数据表实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-09-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "eg_interface_data", autoResultMap = true)
public class InterfaceDataEntity extends Model<InterfaceDataEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //来源系统编码
    private String systemCode;

    //来源系统名称
    private String systemName;

    //业务编码
    private String businessCode;

    //业务名称
    private String businessName;

    //交易流水号
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

    //业务日期
    private LocalDateTime businessDate;

    //财务日期
    private LocalDateTime financeDate;

    //客户类型
    private String clientType;

    //接口数据
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private JSONObject interfaceData;

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

    //批次ID
    private Long batchId;

    //批次类型
    private String batchType;

    //网银编号
    private String ebankNumber;

    //网银流水号
    private String ebankSerialNumber;

    //银行账号签约主体
    @ApiModelProperty("银行账号签约主体)")
    private String bankOrgId;

    @ApiModelProperty("跨合同抵扣保证金标识(0:应收,1:保证金)")
    private String crossContractFlag;

    //业务付款单号
    private String payableNumber;

    @ApiModelProperty("资金付款单号")
    private String paymentOrder;

    @ApiModelProperty(value = "成本中心")
    private String costCentre;

    @ApiModelProperty(value = "员工姓名")
    private String employeeName;

    @ApiModelProperty(value = "费用类型")
    private String expenseType;

    @ApiModelProperty(value = "金融机构")
    private String financialInstitution;

    @ApiModelProperty(value = "外部接口Id")
    private Long interfaceId;

    @ApiModelProperty(value = "错误原因")
    private String errorInfo;

    @ApiModelProperty(value = "消息状态(0:不需要执行,1:需要执行，2：成功，3：失败)")
    private String status;

    @ApiModelProperty(value = "银行流水批次号")
    private String ebankBatchNo;

    @ApiModelProperty(value = "是否已经抽取到认领记录中")
    private String isExtractData;

    @ApiModelProperty(value = "凭证日期")
    private LocalDateTime voucherDate;
}
