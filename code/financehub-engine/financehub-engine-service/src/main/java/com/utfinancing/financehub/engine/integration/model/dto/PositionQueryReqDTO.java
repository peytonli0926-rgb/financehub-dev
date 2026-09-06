package com.utfinancing.financehub.engine.integration.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 08/10/2023
 */
@Data
public class PositionQueryReqDTO {

    private List<PositionQueryDTO> positionList;

}
