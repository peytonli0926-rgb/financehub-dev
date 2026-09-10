package com.utfinancing.financehub.engine.payment.service;

import cn.hutool.core.util.StrUtil;

/** Keeps technical rule codes out of user-facing document and voucher fields. */
final class RetailLeasebackEventNames {
    private RetailLeasebackEventNames() {}

    static String displayName(String internalCode, String sourceValue) {
        if (internalCode != null) {
            switch (internalCode) {
                case "CR000": return "收到现金折扣";
                case "CR001": return "收取保证金";
                case "CR003": return "购入租赁资产";
                case "CR005": return "收到并核销租金";
                case "CR006": return "款项无法确认";
                case "CR007": return "人工明确款项用途";
                case "CR008": return "收到款项核销";
                case "CR025": return "支付资产管理费";
                case "CR029": return "退回平台合作方提前结清贴息金额";
                case "CR033": return "退回的分润费";
                case "CR036": return "支付银行手续费";
                case "CR040": return "抵押服务费";
                case "CR041": return "支付通联手续费";
                case "CR043": return "前期逾期留购价";
                case "CR044": return "渠道商分成结算";
                case "CR056": return "合作方提前结清";
                case "CR060": return "支付印花税";
                case "RF001": return "普通退款";
                case "RF002": return "保证金退款";
                case "RF003": return "未确认款退款";
                case "SC001": return "厂商贴息确认";
                case "SC002": return "平台贴息确认";
                case "SC003": return "提前结清贴息冲回";
                case "OD001": return "本金转逾期";
                case "OD002": return "利息转逾期";
                case "OD003": return "留购价转逾期";
                case "OD004": return "逾期罚息确认";
                case "TS001": return "租金计划调整";
                case "TS002": return "租金信息变更";
                case "TS003": return "结清";
                case "TS004": return "起租后GPS加装";
                case "TS005": return "留购价反向";
                case "TS006": return "费用减免租金";
                case "TS007": return "费用减免留购价";
                case "TS008": return "天津车辆处置结清";
                case "TS009": return "提前留购";
                case "TS010": return "尾款调整租金";
                case "TS011": return "车辆处置";
                case "TS012": return "车辆买断";
                case "AA001": return "项目承租人发生变更";
                case "AA002": return "因辅助核算项目挂错，调整相关科目";
                case "OT001": return "内部资金调拨";
                case "OT002": return "头寸调拨";
                case "OT003":
                case "OT004": return "季度结息";
                case "OT005": return "多支付分润费挂账";
                case "OT006": return "金额记错，调整相关科目";
                case "OT007": return "调整违约金分成";
                case "OT008": return "促销核准后，补差合作方分润费";
                default: break;
            }
        }
        return StrUtil.blankToDefault(sourceValue, internalCode);
    }
}
