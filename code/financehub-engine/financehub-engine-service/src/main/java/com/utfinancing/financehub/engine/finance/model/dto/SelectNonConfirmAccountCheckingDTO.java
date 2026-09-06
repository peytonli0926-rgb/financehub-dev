package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class SelectNonConfirmAccountCheckingDTO extends BaseQueryDTO implements Serializable {
    private Date startDate;
    private Date endDate;
    private String checkingMonth;
    private String lastCheckingMonth;
    private List<String> collectionAccountsBankCode;
    private List<String> accountAgeClass;
    private List<String> financialPrimaryClassic;
    private List<String> confirmAccountProperty;
    private List<String> systemCode;

    private String fromStartDate;
    private String fromEndDate;


}
