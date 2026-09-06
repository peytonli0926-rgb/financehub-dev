package com.utfinancing.financehub.engine.finance.service;

import com.utfinancing.financehub.engine.finance.model.dto.VoucherSaveDTO;
import com.utfinancing.financehub.engine.rule.model.dto.InterfaceDataDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneRuleDTO;

public interface IVoucherExtService {
    public VoucherSaveDTO generateVoucherFromRule(SceneRuleDTO ruleDTO, InterfaceDataDTO interfaceDataDTO);
}
