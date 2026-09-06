package com.utfinancing.financehub.engine.enums;

import org.apache.commons.lang3.StringUtils;

public enum OrgIdEnum {
    C0001_01("01-C0001", "VL05Z0001","01-02-000102"),
    C0001_02("02-C0001", "UF30001_01","01-02-000521"),
    A_30001("30001", "UF30001_01","01-03-000006"),
    A_80001("80001", "00000000","01-04-000057"),
    ;
    private final String code;
    private final String contractCode;

    private final String clientCode;

    OrgIdEnum(String code, String contractCode,String clientCode) {
        this.code = code;
        this.contractCode = contractCode;
        this.clientCode = clientCode;
    }


    public String getCode() {
        return code;
    }

    public String getContractCode() {
        return contractCode;
    }

    public String getClientCode() {
        return clientCode;
    }


    public static OrgIdEnum getDescByCode(final String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (OrgIdEnum enums : OrgIdEnum.values()) {
            if (code.equals(enums.code)) {
                return enums;
            }
        }
        return null;
    }
}
