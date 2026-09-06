package com.utfinancing.financehub.engine.finance.service;

import com.utfinancing.financehub.common.core.dto.DropDownDTO;

import java.util.List;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.service.ExcelDropDownService</li>
 * <li>CreateTime : 2024/02/04 12:01</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
public interface ExcelDropDownService {

    List<DropDownDTO> getManualExcelDropDown();
}
