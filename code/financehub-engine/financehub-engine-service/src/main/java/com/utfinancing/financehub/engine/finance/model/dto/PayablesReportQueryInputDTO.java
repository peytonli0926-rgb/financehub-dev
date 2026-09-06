package com.utfinancing.financehub.engine.finance.model.dto;

import com.utfinancing.financehub.common.core.dto.BaseQueryDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class PayablesReportQueryInputDTO extends BaseQueryDTO implements Serializable {
    private String periodCodeStart;
    private String periodCodeEnd;
    private String periodCode;
    private String isException;
    private List<String> orgIdList = new ArrayList<>();
    private Integer limit;
    private Integer offset;
}
