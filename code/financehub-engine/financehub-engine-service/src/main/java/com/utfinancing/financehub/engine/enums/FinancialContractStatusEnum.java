package com.utfinancing.financehub.engine.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 * <li>Project : FAW-PRIME-financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.enums.FinancialContractStatusEnum</li>
 * <li>CreateTime : 2023/10/13 11:10</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
public enum FinancialContractStatusEnum {
    //正常核销,亏损结清,非亏损结清
    ONE("11", "正常核销"),
    TWO("5", "亏损结清"),
    THREE("23","非亏损结清"),
    ASSET_TABULATION_RELEASE("24","资产出表后赎回"),
    EXECUTING_ASSET_DISPOSAL("25","在执行合同抵债资产处置"),
    PART_DISPOSAL("26","部分处置"),
    AUCTION_PART_DISPOSAL("27","拍卖部分处置（未入库）"),
    INSTORAGE_RELAESE("28","入库后赎回"),
    ASSET_DISPOSAL_END("29","资产处置结束（转让至恒信）"),
    ASSET_TABULATION_EXPIRE("30","资产出表（到期）"),
    ASSET_DISPOSAL_INNER_TRANSFER("1","资产处置结束（内部转让）"),
    LOSS_SETTLEMENT_SERVICE_FEE_CANCELLATION("2", "亏损结清、服务费核销"),
    ;

    private final String code;
    private final String desc;

    FinancialContractStatusEnum(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(final String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (FinancialContractStatusEnum enums : FinancialContractStatusEnum.values()) {
            if (code.equals(enums.code)) {
                return enums.desc;
            }
        }
        return null;
    }

    public static String getCodeByDesc(final String desc) {
        if (StringUtils.isEmpty(desc)) {
            return null;
        }
        for (FinancialContractStatusEnum enums : FinancialContractStatusEnum.values()) {
            if (desc.equals(enums.desc)) {
                return enums.code;
            }
        }
        return null;
    }

    public static List<String> getEndStatus(){
        List<String> result = new ArrayList<>();
        result.add(ONE.desc);
        result.add(TWO.desc);
        result.add(THREE.desc);
        return result;
    }

    public static List<String> getPartStatus(){
        List<String> result = new ArrayList<>();
        result.add(ASSET_TABULATION_RELEASE.desc);
        result.add(EXECUTING_ASSET_DISPOSAL.desc);
        result.add(PART_DISPOSAL.desc);
        result.add(AUCTION_PART_DISPOSAL.desc);
        result.add(INSTORAGE_RELAESE.desc);
        result.add(ASSET_DISPOSAL_END.desc);
        result.add(ASSET_TABULATION_EXPIRE.desc);
        return result;
    }
}
