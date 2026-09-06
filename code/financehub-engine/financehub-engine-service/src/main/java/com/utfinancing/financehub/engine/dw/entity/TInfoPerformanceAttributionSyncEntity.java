package com.utfinancing.financehub.engine.dw.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 绩效归属信息同步表实体对象
 * </p>
 *
 * @author lixin
 * @since 2023-12-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_info_performance_attribution_sync")
public class TInfoPerformanceAttributionSyncEntity extends Model<TInfoPerformanceAttributionSyncEntity> {

    private static final long serialVersionUID = 1L;

    //主键id，自增
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    //数据来源ID
    private String dataSourceCode;

    //数据来源
    private String dataSourceName;

    //项目编号
    private String projectNo;

    //合同编号
    private String contractNo;

    //子合同编号
    private String secContractNo;

    //承租人id
    private String lesseeCode;

    //承租人名称
    private String lesseeName;

    //项目经理id
    private String projectManagerCode;

    //项目经理名称
    private String projectManagerName;

    //销售额占比
    private String saleProportion;

    //销售额归属部门code
    private String saleVolumeBelongDepartCode;

    //销售额归属部门
    private String saleVolumeBelongDepartName;

    //销售额归属子部门code
    private String saleVolumeBelongSecDepartCode;

    //销售额归属子部门
    private String saleVolumeBelongSecDepartName;

    //销售额归属人code
    private String saleVolumeBelongUserCode;

    //销售额归属人
    private String saleVolumeBelongUserName;

    //销售额归属直属总监工号
    private String directDirectorCode;

    //销售额归属直属总监名称
    private String directDirectorName;

    //销售额归属总经理助理工号
    private String generalManagerAssistantCode;

    //销售额归属总经理助理名称
    private String generalManagerAssistantName;

    //销售额归属副总经理工号
    private String deputyGeneralManagerCode;

    //销售额归属副总经理名称
    private String deputyGeneralManagerName;

    //销售额归属副总经理(主持工作)工号
    private String deputyGeneralManagerWorkCode;

    //销售额归属副总经理(主持工作)名称
    private String deputyGeneralManagerWorkName;

    //销售额归属总经理工号
    private String generalManagerCode;

    //销售额归属总经理名称
    private String generalManagerName;

    //资产占比
    private String assetProportion;

    //资产归属部门code
    private String assetBelongDepartCode;

    //资产归属部门名称
    private String assetBelongDepartName;

    //资产归属子部门code
    private String assetBelongSecDepartCode;

    //资产归属子部门名称
    private String assetBelongSecDepartName;

    //资产归属人code
    private String assetBelongUserCode;

    //资产归属人名称
    private String assetBelongUserName;

    //资产归属直属总监工号
    private String assetDirectDirectorCode;

    //资产归属直属总监名称
    private String assetDirectDirectorName;

    //资产归属总经理助理工号
    private String assetGeneralManagerAssistantCode;

    //资产归属总经理助理名称
    private String assetGeneralManagerAssistantName;

    //资产归属副总经理工号
    private String assetDeputyGeneralManagerCode;

    //资产归属副总经理名称
    private String assetDeputyGeneralManagerName;

    //资产归属副总经理(主持工作)工号
    private String assetDeputyGeneralManagerWorkCode;

    //资产归属副总经理(主持工作)名称
    private String assetDeputyGeneralManagerWorkName;

    //资产归属总经理工号
    private String assetGeneralManagerCode;

    //资产归属总经理名称
    private String assetGeneralManagerName;

    //收入占比
    private String incomeProportion;

    //收入归属部门code
    private String incomeBelongDepartCode;

    //收入归属部门名称
    private String incomeBelongDepartName;

    //收入归属子部门code
    private String incomeBelongSecDepartCode;

    //收入归属子部门名称
    private String incomeBelongSecDepartName;

    //收入归属人code
    private String incomeBelongUserCode;

    //收入归属人名称
    private String incomeBelongUserName;

    //收入归属直属总监工号
    private String incomeDirectDirectorCode;

    //收入归属直属总监名称
    private String incomeDirectDirectorName;

    //收入归属总经理助理工号
    private String incomeGeneralManagerAssistantCode;

    //收入归属总经理助理名称
    private String incomeGeneralManagerAssistantName;

    //收入归属副总经理工号
    private String incomeDeputyGeneralManagerCode;

    //收入归属副总经理名称
    private String incomeDeputyGeneralManagerName;

    //收入归属副总经理(主持工作)工号
    private String incomeDeputyGeneralManagerWorkCode;

    //收入归属副总经理(主持工作)名称
    private String incomeDeputyGeneralManagerWorkName;

    //收入归属总经理工号
    private String incomeGeneralManagerCode;

    //收入归属总经理名称
    private String incomeGeneralManagerName;

    //客户归属部门code
    private String customerBelongDepartCode;

    //客户归属部门名称
    private String customerBelongDepartName;

    //客户归属人code
    private String customerBelongUserCode;

    //客户归属人名称
    private String customerBelongUserName;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


}
