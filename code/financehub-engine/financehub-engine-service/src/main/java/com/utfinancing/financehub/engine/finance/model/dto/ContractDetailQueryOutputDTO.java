package com.utfinancing.financehub.engine.finance.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ContractDetailQueryOutputDTO implements Serializable {
    private String orgId;
    private String orgName;
    private Date businessDate;
    private String systemCode;
    private String paymentIdentifier;
    private String sceneCode;
    private String sceneName;
    private String contractCode;
    private String clientCode;
    private String clientName;
    private Long id;
}
