package com.utfinancing.financehub.engine.enums;

/**
 * 接口表字段枚举
 */
public enum InterfaceFieldEnum {
    RECEIVABLE_UNCONFIRM_LIST("receivableUnconfirmList", "长期应收款_未确认收款列表"),
    RECEIVABLE_UNCONFIRM_RECEIPT("receivableUnconfirm", "长期应收款_未确认收款"),
    RECEIVABLE_UNCONFIRM_RECEIPT_SUM("receivableUnconfirmSum", "长期应收款_未确认收款汇总"),

    RECEIVE_TERMINATE_PROCEDURE_LIST("receiveTerminateProcedureList", "合同解约及更改手续费列表"),
    RECEIVE_TERMINATE_PROCEDURE("receiveTerminateProcedure", "合同解约及更改手续费"),
    RECEIVE_TERMINATE_PROCEDURE_SUM("receiveTerminateProcedureSum", "合同解约及更改手续费汇总"),

    PAYABLE_DEVICE_LIST("payableDeviceList", "应付租赁设备款列表"),
    PAYABLE_DEVICE("payableDevice", "应付租赁设备款"),
    PAYABLE_DEVICE_SUM("payableDeviceSum", "应付租赁设备款汇总"),

    PROVISION_BALANCE("provisionBalance", "入库时计提减值"),
    OUTBOUND_EQUIPMENT_PROVISION_BALANCE("outboundEquipmentProvisionBalance", "出库时回收融资租赁设备减值准备余额"),
    OUTBOUND_RECEIVE_COST_BALANCE("outboundReceiveCostBalance", "出库时回收融资租赁设备成本余额"),

    RECEIVE_COST("receiveCost", "回收融资租赁设备成本"),
    RECEIVABLE_LEASE_BALANCE("receivableLeaseBalance", "应收租金"),
    RESIDUAL_BALANCE("residualBalance", "应收期末残值"),
    RECEIVABLE_OUTTAX_BALANCE("receivableOuttaxBalance", "应收期末残值"),
    UNREALIZED_REVENUE_BALANCE("unrealizedRevenueBalance", "未实现收益"),
    RECEIVABLE_MARGIN_BALANCE("receivableMarginBalance", "承租人保证金"),



    ORG_ID("orgId", "签约主体"),
    EBANK_NUM("ebankNum", "网银编号"),
    AMOUNT_TYPE("amountType", "金额类型"),
    DEDUCTION_MARGIN_CONTRACT("deductionMarginContract", "抵扣保证金对应合同"),
    DEDUCTION_MARGIN_ORG_ID("deductionMarginOrgId", "抵扣保证金对应合同签约主体"),

    RECEIVE_PROCEDURE_AMOUNT("receiveProcedureAmount", "收取手续费累计金额"),

    RENT_SETTLEMENT_WAY("rentSettlementWay", "租赁方式"),

    CONTRACT_CODE("contractCode", "客户编码"),
    ;
    private final String code;
    private final String desc;

    InterfaceFieldEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
