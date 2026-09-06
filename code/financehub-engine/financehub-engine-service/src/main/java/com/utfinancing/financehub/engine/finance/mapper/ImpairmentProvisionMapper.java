package com.utfinancing.financehub.engine.finance.mapper;

import com.utfinancing.financehub.engine.finance.entity.ImpairmentProvisionDetailEntity;
import com.utfinancing.financehub.engine.finance.entity.ImpairmentProvisionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utfinancing.financehub.engine.finance.model.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 减值计提 Mapper 接口
 * </p>
 *
 * @author wenbin
 * @since 2024-03-25
 */
public interface ImpairmentProvisionMapper extends BaseMapper<ImpairmentProvisionEntity> {

    /**
     * 获取上月余额
     *
     * @param queryDTO
     * @return
     */
    ImpairmentProvisionDetailEntity getLastMonthBalance(@Param("queryDTO") ImpairmentProvisionDetailEntity queryDTO);

    /**
     * 获取上月数据
     *
     * @param excelType
     * @return
     */
    List<ImpairmentProvisionDetailEntity> getLastMonthByExcelType(@Param("excelType") String excelType);

    /**
     * 获取导出减值清单1的数据
     *
     * @return
     */
    List<ImpairmentProvisionExcelVOExport1> getExportExcel1();

    /**
     * 获取导出减值清单2的数据
     *
     * @return
     */
    List<ImpairmentProvisionExcelVOExport2> getExportExcel2();

    /**
     * 获取导出减值清单3的数据
     *
     * @return
     */
    List<ImpairmentProvisionExcelVOExport3> getExportExcel3();

    /**
     * 获取导出减值清单4的数据
     *
     * @return
     */
    List<ImpairmentProvisionExcelVOExport4> getExportExcel4();

    /**
     * 获取导出减值清单5的数据
     *
     * @return
     */
    List<ImpairmentProvisionExcelVOExport5> getExportExcel5();


    /**
     * 获取上月数据-根据导入文件类型+账期
     *
     * @param excelType
     * @return
     */
    List<ImpairmentProvisionDetailEntity> getLastMonthByPeriodCode(@Param("excelType") String excelType,@Param("periodCode") String periodCode);


}
