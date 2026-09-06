package com.utfinancing.financehub.engine.scene.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 场景凭证配置实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-08-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_scene_voucher")
public class SceneVoucherEntity extends Model<SceneVoucherEntity> {

    private static final long serialVersionUID = 1L;

    //ID
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //场景ID
    private Long sceneId;

    //规则执行条件
    private String scriptCondition;

    //来源;refInterface
    private String source;

    //凭证类型;refDict
    private String voucherType;

    //公司
    private String company;

    //业务日期
    private String businessDate;

    //凭证日期
    private String voucherDate;

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

    //凭证名称
    private String sceneVoucherName;

    //细分场景
    private String subSceneType;

}
