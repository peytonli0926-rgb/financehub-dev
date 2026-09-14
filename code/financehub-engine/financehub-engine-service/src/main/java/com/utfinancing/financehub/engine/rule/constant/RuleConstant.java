package com.utfinancing.financehub.engine.rule.constant;

import io.swagger.annotations.ApiModelProperty;

public interface RuleConstant {
    String FIELD_ACCOUNTING_BUSINESS_CODE = "accountingBusinessCode";
    String DEFAULT_BUSINESS_CODE = "default"; //默认业务编码

    String FIELD_ID = "id"; //默认业务编码
    String FIELD_BUSINESS_CODE = "businessCode"; //业务编码
    String FIELD_SCENE_CODE = "sceneCode"; //场景编码
    String FIELD_SCENE_NAME = "sceneName"; //场景编码
    String FIELD_CONTRACT_CODE = "contractCode"; //合同编码
    String FIELD_CONTRACT_CODE_M = "contractCodeM";//主合同编号
    String FIELD_BILL_CONTRACT_CODE = "billContractCode";//借款合同编号
    String FIELD_BUSINESS_DATE = "businessDate";//业务日期
    String FIELD_CLIENT_CODE = "clientCode"; //客户编码
    String FIELD_CONTRACT_STATUS = "contractStatus"; //合同状态
    String FIELD_ORG_ID = "orgId"; //签约主体
    String FIELD_SYSTEM_CODE = "systemCode"; //系统编号
    String BANK_NO = "collectionAccountsBankNo"; //资金系统收款银行账号
    String PAY_BANK_NO = "paymentAccountsBankNo"; //资金系统付款银行账号
    String FIELD_ORDER_ID = "orderId"; //订单id
    String BANK_ORG_ID = "bankOrgId"; //银行签约主体

    String BANK_CLIENT_CODE = "bankClientCode"; //银行签约主体对应的客户编码名称
    String EBANK_NUM = "ebankNum"; //银行签约主体
    String CROSS_CONTRACT_FLAG = "crossContractFlag"; //跨合同抵扣保证金标识(0:应收,1:保证金)
    String TRANSACTION_TYPE = "transactionType";//资金系统交易类型
    String IS_SUBMIT = "isSubmit"; // 是否是提交凭证生成
    String FINANCIAL_CONTRACT_STATUS = "financialContractStatus";
    String FIELD_SCENE_CODE_ORIGINAL = "sceneCodeOriginal"; //原始场景编码

    String INTERFACE_CREATE_TIME = "interfaceCreateTime"; //业务系统创建时间

    String CREATE_USER_NO = "createUserNo"; //制单人工号

    String CREATE_USER_NAME = "createUserName"; //制单人姓名

    String FIELD_CLIENT_TYPE = "clientType"; //客户类型

    String FIELD_INTERFACE_ID = "interfaceId"; //外部接口id

    String FIELD_SUB_SCENE_TYPE = "sub_scene_type"; //细分场景

    String ORG_CLIENT_CODE = "orgClientCode"; //主体对应的客户编码名称

    String FIELD_PERIOD = "period"; //会计期间

    String DEDUCTION_MARGIN_ORG_ID = "deductionMarginOrgId"; //签约主体

    String FIELD_CONTRACT_CLIENT_CODE = "contract_clientCode"; //合同客户编码

    String FIELD_EBANK_SECIAL_NUMBER = "ebankSerialNumber"; //银行序列号

    String FIELD_EBANK_BATCH_NO = "ebankBatchNo";//银行批次编码

    String FIELD_CONTRACT_LEASE_TYPE = "contract_leaseType"; //合同租赁类型

    String EAS_VOUCHER_ID = "easVoucherId"; //主体对应的客户编码名称

    String IS_INTERFACE_DATA = "isInterfaceData"; //是否是接口表数据

    String EBANK_SERIAL_NUMBER = "ebankSerialNumber"; //银行流水号

    String FIELD_CLIENT_NAME = "clientName"; //客户名称

    String FIELD_CLIENT_TABLE_NAME = "client_clientName"; //客户表客户名称

    String FIELD_CLIENT_ATTRIBUTE = "clientAttribute"; //客户参数=个人/法人

    String FIELD_PAYABLE_NUMBER = "payableNumber";//付款单号

    String FIELD_PAYMENT_ORDER = "paymentOrder";//资金付款单号
    String FIELD_SETTLEMENT_WAY = "settlementWay";//资金付款单号
    String FIELD_PAYMENT_METHOD = "paymentMethod";//资金付款方式
    String ACTUAL_CLIENT_CODE = "actualClientCode";//资金付款方式
    String ACTUAL_CLIENT_NAME = "actualClientName";//资金付款方式
    String FIELD_RECEIVABLE_SERVICE_AMOUNT = "receivableServiceAmount";
    String FIELD_RECEIVE_SERVICE_AMOUNT = "receiveServiceAmount";
    String FIELD_PAYABLE_DEVICE_AMOUNT = "payableDeviceAmount";

    /**
     * @description: 小微业务系统-保证金池类型
     **/
    String VENDOR_POOL_TYPE = "vendorPoolType";
}
