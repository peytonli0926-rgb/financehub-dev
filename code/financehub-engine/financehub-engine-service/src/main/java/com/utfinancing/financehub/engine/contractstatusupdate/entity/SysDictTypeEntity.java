package com.utfinancing.financehub.engine.contractstatusupdate.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SysDictTypeEntity实体类
 * 对应数据库表：sys_dict_type
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_dict_type")
public class SysDictTypeEntity extends Model<SysDictTypeEntity> {

	private static final long serialVersionUID = 1L;

	//字典主键
	@TableField("dict_id")
	private Long dictId;

	//字典名称
	@TableField("dict_name")
	private String dictName;

	//字典类型
	@TableField("dict_type")
	private String dictType;

	//状态（0正常 1停用）
	private String status;

	//创建者
	@TableField("create_by")
	private String createBy;

	//创建时间
	@TableField("create_time")
	private LocalDateTime createTime;

	//更新者
	@TableField("update_by")
	private String updateBy;

	//更新时间
	@TableField("update_time")
	private LocalDateTime updateTime;

	//备注
	private String remark;

}