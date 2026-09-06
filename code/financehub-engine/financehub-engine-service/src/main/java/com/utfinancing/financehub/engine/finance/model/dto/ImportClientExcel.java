package com.utfinancing.financehub.engine.finance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utfinancing.financehub.common.core.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author : hzhao
 * @Date : Create in 2023-10-09
 * @Description : 导入对象
 * @Modified :
 */
@Data
public class ImportClientExcel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Excel(name = "客户编号", type = Excel.Type.IMPORT)
    private String clientCode;

    @Excel(name = "客户名称", type = Excel.Type.IMPORT)
    private String clientName;

    @Excel(name = "行业标签", type = Excel.Type.IMPORT)
    private String industry;

    @Excel(name = "省", type = Excel.Type.IMPORT)
    private String provinceCity;

    @Excel(name = "客户性质标签", type = Excel.Type.IMPORT)
    private String clientNatrue;


}
