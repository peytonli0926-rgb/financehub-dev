package com.utfinancing.financehub.engine.rule.service;

import com.utfinancing.financehub.engine.rule.model.dto.InterfaceDataDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public interface IPeriodCodeService {

    /**
     * 计算凭证日期(记账日期)
     */
    public LocalDate generateVoucherDate(InterfaceDataDTO dto);

}
