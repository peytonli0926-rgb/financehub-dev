package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author : robjiang
 * @Date : Create in 2025-12-26
 * @Description : 减值三阶段上传记录DTO对象
 * @Modified :
 */
@Data
public class ImpairmentThirdStageRecordExcel implements Serializable{
    private static final long serialVersionUID = 1L;

    @Excel(name = "合同编号", type = Excel.Type.IMPORT)
    private String contractCode;

    @Excel(name = "上传账期", comment = "yyyy-MM")
    private String uploadPeriods;

}
