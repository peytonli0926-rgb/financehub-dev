package com.utfinancing.financehub.engine.finance.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class SelectEndBalForInputDTO implements Serializable {
    private List<String> contractList = new ArrayList<>();
}
