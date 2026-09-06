package com.utfinancing.financehub.engine.rule.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SelectHtqzSceneInputDTO implements Serializable {
    private Date endDate;
    private Date startDate;
}
