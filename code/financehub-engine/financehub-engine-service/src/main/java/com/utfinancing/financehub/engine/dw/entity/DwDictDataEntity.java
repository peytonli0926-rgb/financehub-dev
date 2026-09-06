package com.utfinancing.financehub.engine.dw.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-12-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dw_dict_data")
public class DwDictDataEntity extends Model<DwDictDataEntity> {

    private static final long serialVersionUID = 1L;

    //主键ID
    @TableId(value = "nu_id", type = IdType.ASSIGN_ID)
    private Integer nuId;

    //名称
    private String vcMingc;

    //值
    private String vcZhi;

    //父节点ID
    private String vcFujdid;

    //是否有子级
    private String vcShifyzj;

    //备注
    private String vcBeiz;

    //有效标识
    private String chYouxbs;

    //创建人
    private String vcChuangjr;

    //创建时间
    private String dtChuangjsj;

    //更新人
    private String vcGengxr;

    //更新时间
    private String dtGengxsj;


}
