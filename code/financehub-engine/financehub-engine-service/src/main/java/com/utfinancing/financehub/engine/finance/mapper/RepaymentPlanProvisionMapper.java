package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.RepaymentPlanProvisionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 偿还计划测算表-计提用 Mapper 接口
 * </p>
 *
 * @author robjiang
 * @since 2024-01-30
 */
public interface RepaymentPlanProvisionMapper extends BaseMapper<RepaymentPlanProvisionEntity> {

    /**
     * 计提数据备份
     */
    public void repaymentPlanDataBackup();

    /**
     * 偿还计划数据恢复
     */
    public void repaymentPlanDataRecovery();

    /**
     * 更新上次计提数据
     */
    public void updateProvisionData();


    /**
     * 将
     */
    public void updateProvisionDataByOrgIds(@Param("orgIds") List<String> orgIds);

    @Update("TRUNCATE TABLE eg_repayment_plan_provision")
    public void truncateTable();
}
