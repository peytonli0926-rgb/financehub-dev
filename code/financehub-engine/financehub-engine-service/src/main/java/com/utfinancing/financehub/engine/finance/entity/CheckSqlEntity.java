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
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 对账对接其他业务系统sql表实体对象
 * </p>
 *
 * @author jnc
 * @since 2024-04-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("eg_check_sql")
public class CheckSqlEntity extends Model<CheckSqlEntity> {

    private static final long serialVersionUID = 1L;

    //id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //运行环境 dev uat prod
    private String profileCode;

    //对接数据库 乘用车 CYC 商用车 SYC 乘用车资产转让 CYC-ASSET-ABS 商用车资产转让 SYC-ASSET-ABS 小微 XW 统一平台 TYPT
    private String dbCode;

    //业务系统查询sql
    private String querySql;

    //sql描述
    private String sqlDescr;

    //删除标志
    @TableLogic
    private String delFlag;

    //创建人
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //修改人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    //修改时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    //sql标志
    private String sqlMark;

    //中台查询sql
    private String financeQuerySql;

    //sql在对账逻辑里的执行顺序
    private Integer sqlOrder;
}
