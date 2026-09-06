package com.utfinancing.financehub.engine.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 未确认收款-对账表批量修改上传模板表实体对象
 * </p>
 *
 * @author robjiang
 * @since 2025-06-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_batch_modify_template")
public class BatchModifyTemplateEntity extends Model<BatchModifyTemplateEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //对账月份
    private String accountCheckingMonth;

    //到账主体
    private String collectionAccountsBank;

    //系统编码
    private String systemCode;

    //业务系统的网银编号-小网银
    private String ebankSerialNumber;

    //comments
    private String remark;

    //财务初分类
    private String financialPrimaryClassic;

    //运营部确认款项性质
    private String confirmAccountProperty;

    //非租对账备注
    private String nonLeaseAccountCheckingComments;

    //运营部历史备注
    private String operateHistoryComments;


}
