package com.utfinancing.financehub.engine.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 合同状态
 */
public enum ContractStatusTransferEnum {
    STATUS1("正常(起租) ", "正常(起租)"),
    STATUS2("内部结清", "正常(起租)"),
    STATUS3("设备入库", "正常(起租)"),
    STATUS4("部分设备入库", "正常(起租)"),
    STATUS5("部分处置", "正常(起租)"),
    STATUS6("部分财务入库", "正常(起租)");
    private final String sourceStatus;
    private final String targetStatus;

    ContractStatusTransferEnum(String sourceStatus, String targetStatus) {
        this.sourceStatus = sourceStatus;
        this.targetStatus = targetStatus;
    }

    public static List<String> getContractStatusList() {
        List<String> result = new ArrayList<>();
        for (ContractStatusTransferEnum value : ContractStatusTransferEnum.values()) {
            result.add(value.getSourceStatus());
        }
        return result;
    }

    public static String transferContractStatus(String status) {
        for (ContractStatusTransferEnum value : ContractStatusTransferEnum.values()) {
            if (value.getSourceStatus().equals(status)) {
                return value.getTargetStatus();
            }
        }
        return status;
    }


    public String getSourceStatus() {
        return sourceStatus;
    }

    public String getTargetStatus() {
        return targetStatus;
    }
}
