package com.utfinancing.financehub.engine.claim.service;

import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderQueryDTO;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderSpecialQueryDTO;

/**
 * <ul>
 * <li>Project : financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.claim.service.IClaimOrderJobHandlerService</li>
 * <li>CreateTime : 2023/11/24 14:57</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
public interface IClaimOrderJobHandlerService {

    /**
     * 成本类费用
     * @param queryDTO
     * @return
     */
    Boolean costGenerateVoucher(ClaimOrderSpecialQueryDTO queryDTO);

    /**
     * 诉讼费
     * @param queryDTO
     * @return
     */
    Boolean courtCostGenerateVoucher(ClaimOrderQueryDTO queryDTO);


}
