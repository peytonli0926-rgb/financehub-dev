package com.utfinancing.financehub.engine.scene.model.dto;

import lombok.Data;

/**
 * @Author : lixin
 * @Date : Create in 17/10/2023
 */
@Data
public class MappingDTO {

    private String fieldCode;
    private String sourceValue;
    private String targetValue;
    private String targetFieldCode;
}
