package com.utfinancing.financehub.engine.finance.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SelectAssistFlagsBySceneDTO implements Serializable {
    private String sceneCode;
    private String assistFlags;
}
