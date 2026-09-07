-- 零售融资租赁业务系统（CYCXT）/回租/乘用车/付款场景配置
-- MySQL 8.0；脚本可重复执行。银行存款明细科目不进入 eg_account，
-- 而是由 bank_deposit 金额类型在运行时按 eg_bank_account.bank_account_number 取得。

START TRANSACTION;

-- 业务叶子：融资租赁业务 -> 回租 -> 乘用车。
-- 仅使用新旧两套库都具备的公共字段；层级展示由业务名称表达。
INSERT INTO eg_business
    (id, business_code, business_name, scene_count, create_by, create_time, update_by, update_time, del_flag)
VALUES
    (202609070100003, 'CYC_RETAIL_LEASEBACK', '融资租赁业务/回租/乘用车', 1,
     'retail-payment-config', NOW(), 'retail-payment-config', NOW(), '0')
ON DUPLICATE KEY UPDATE
    business_name = VALUES(business_name), scene_count = VALUES(scene_count),
    update_by = VALUES(update_by), update_time = NOW(), del_flag = '0';

-- 零售系统传业务事件名称，值映射负责路由到统一付款场景，并生成模板内部事件码。
-- 第一条映射集中维护“哪些来源事件属于付款”；后八条只维护各事件的模板分录分支。
DELETE FROM eg_field_mapping WHERE id BETWEEN 202609070900001 AND 202609070900009;
INSERT INTO eg_field_mapping
    (id, system_code, field_code, field_name, source_value, target_value, default_value,
     create_by, create_time, update_by, update_time, del_flag, target_field_code)
VALUES
    (202609070900001,'CYCXT','eventCode','零售付款事件',
     '购入租赁资产,CR003,支付资产管理费,CR025,平台合作方分润,退回平台合作方提前结清贴息金额,CR029,支付银行手续费,CR036,抵押服务费,CR040,支付通联手续费,CR041,渠道商分成结算,CR044,合作方提前结清,CR056',
     'CYC_PAYMENT',NULL,'retail-payment-config',NOW(),'retail-payment-config',NOW(),'0','sceneCode'),
    (202609070900002,'CYCXT','eventCode','购入租赁资产','购入租赁资产,CR003','CR003',NULL,'retail-payment-config',NOW(),'retail-payment-config',NOW(),'0','paymentEventCode'),
    (202609070900003,'CYCXT','eventCode','支付资产管理费','支付资产管理费,CR025','CR025',NULL,'retail-payment-config',NOW(),'retail-payment-config',NOW(),'0','paymentEventCode'),
    (202609070900004,'CYCXT','eventCode','平台合作方分润','平台合作方分润,退回平台合作方提前结清贴息金额,CR029','CR029',NULL,'retail-payment-config',NOW(),'retail-payment-config',NOW(),'0','paymentEventCode'),
    (202609070900005,'CYCXT','eventCode','支付银行手续费','支付银行手续费,CR036','CR036',NULL,'retail-payment-config',NOW(),'retail-payment-config',NOW(),'0','paymentEventCode'),
    (202609070900006,'CYCXT','eventCode','抵押服务费','抵押服务费,CR040','CR040',NULL,'retail-payment-config',NOW(),'retail-payment-config',NOW(),'0','paymentEventCode'),
    (202609070900007,'CYCXT','eventCode','支付通联手续费','支付通联手续费,CR041','CR041',NULL,'retail-payment-config',NOW(),'retail-payment-config',NOW(),'0','paymentEventCode'),
    (202609070900008,'CYCXT','eventCode','渠道商分成结算','渠道商分成结算,CR044','CR044',NULL,'retail-payment-config',NOW(),'retail-payment-config',NOW(),'0','paymentEventCode'),
    (202609070900009,'CYCXT','eventCode','合作方提前结清','合作方提前结清,CR056','CR056',NULL,'retail-payment-config',NOW(),'retail-payment-config',NOW(),'0','paymentEventCode');

DROP TEMPORARY TABLE IF EXISTS tmp_retail_payment_scene;
CREATE TEMPORARY TABLE tmp_retail_payment_scene (
    seq_no INT PRIMARY KEY,
    scene_id BIGINT NOT NULL,
    scene_code VARCHAR(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    scene_name VARCHAR(200) NOT NULL
);
INSERT INTO tmp_retail_payment_scene VALUES
    (1, 202609070200001, 'CYC_PAYMENT', '付款');

-- 仅重建本脚本负责的八个场景，避免历史配置和重复分录同时生效。
DELETE c FROM eg_scene_voucher_condition c
JOIN eg_scene_voucher_entry e ON e.id = c.scene_voucher_entry_id
JOIN eg_scene_voucher v ON v.id = e.scene_voucher_id
JOIN eg_scene sc ON sc.id = v.scene_id
WHERE sc.scene_code IN ('CYC_PAYMENT','CR003','CR025','CR029','CR036','CR040','CR041','CR044','CR056');
DELETE e FROM eg_scene_voucher_entry e
JOIN eg_scene_voucher v ON v.id = e.scene_voucher_id
JOIN eg_scene sc ON sc.id = v.scene_id
WHERE sc.scene_code IN ('CYC_PAYMENT','CR003','CR025','CR029','CR036','CR040','CR041','CR044','CR056');
DELETE v FROM eg_scene_voucher v
JOIN eg_scene sc ON sc.id = v.scene_id
WHERE sc.scene_code IN ('CYC_PAYMENT','CR003','CR025','CR029','CR036','CR040','CR041','CR044','CR056');
DELETE FROM eg_scene_fields WHERE scene_code IN ('CYC_PAYMENT','CR003','CR025','CR029','CR036','CR040','CR041','CR044','CR056');
DELETE FROM eg_business_scene WHERE scene_code IN ('CYC_PAYMENT','CR003','CR025','CR029','CR036','CR040','CR041','CR044','CR056');
DELETE FROM eg_scene WHERE scene_code IN ('CYC_PAYMENT','CR003','CR025','CR029','CR036','CR040','CR041','CR044','CR056');

INSERT INTO eg_scene
    (id, scene_code, scene_name, scene_period, enable_flag, create_by, create_time, update_by, update_time, del_flag, version)
SELECT scene_id, scene_code, scene_name, 'order', '1', 'retail-payment-config', NOW(),
       'retail-payment-config', NOW(), '0', 1
FROM tmp_retail_payment_scene;

INSERT INTO eg_business_scene
    (id, business_id, scene_id, scene_code, scene_name, serial, create_by, create_time, update_by, update_time, del_flag)
SELECT 202609070210000 + seq_no, 202609070100003, scene_id, scene_code, scene_name, seq_no,
       'retail-payment-config', NOW(), 'retail-payment-config', NOW(), '0'
FROM tmp_retail_payment_scene;

-- 接口字段。每个事件使用同一份统一付款接口定义，金额组合由事件校验决定。
DROP TEMPORARY TABLE IF EXISTS tmp_retail_payment_field;
CREATE TEMPORARY TABLE tmp_retail_payment_field (
    seq_no INT PRIMARY KEY,
    field_name VARCHAR(100) NOT NULL,
    field_code VARCHAR(100) NOT NULL,
    data_type VARCHAR(50) NOT NULL
);
INSERT INTO tmp_retail_payment_field VALUES
    (1, '系统来源', 'systemCode', 'String'),
    (2, '请求流水号', 'orderId', 'String'),
    (3, '事件编码', 'eventCode', 'String'),
    (4, '业务日期', 'businessDate', 'String'),
    (5, '合同编号', 'contractCode', 'String'),
    (6, '客户编码', 'clientCode', 'String'),
    (7, '客户名称', 'clientName', 'String'),
    (8, '签约主体编码', 'orgId', 'String'),
    (9, '签约主体名称', 'orgName', 'String'),
    (10, '银行账号', 'bankNo', 'String'),
    (11, '币种', 'currency', 'String'),
    (12, '支付方式', 'paymentMethod', 'String'),
    (13, '资产状态', 'assetStage', 'String'),
    (14, '付款总额', 'paymentAmount', 'Number'),
    (15, '不含税费用金额', 'untaxedAmount', 'Number'),
    (16, '不含税利息金额', 'paymentInterestAmount', 'Number'),
    (17, '不含税资产管理费（引擎计算）', 'managementFeeAmount', 'Number'),
    (18, '首付款金额', 'initialPaymentAmount', 'Number'),
    (19, '提前结清应退分润费', 'refundAmount', 'Number'),
    (20, '应收款抵扣金额', 'offsetAmount', 'Number'),
    (21, '银行实付金额', 'bankPaymentAmount', 'Number'),
    (22, '收款方编码', 'payeeCode', 'String'),
    (23, '收款方名称', 'payeeName', 'String'),
    (24, '备注', 'remark', 'String');

INSERT INTO eg_scene_fields
    (id, scene_id, scene_code, scene_name, field_name, field_code, data_type,
     create_by, create_time, update_by, update_time, del_flag)
SELECT 202609070300000 + s.seq_no * 100 + f.seq_no,
       s.scene_id, s.scene_code, s.scene_name, f.field_name, f.field_code, f.data_type,
       'retail-payment-config', NOW(), 'retail-payment-config', NOW(), '0'
FROM tmp_retail_payment_scene s CROSS JOIN tmp_retail_payment_field f;

-- 金额类型字典只补新增项，已存在的公共类型直接复用。
INSERT INTO sys_dict_data
    (dict_sort, dict_label, dict_value, dict_type, status, create_by, create_time, update_by, update_time, remark)
SELECT x.dict_sort, x.dict_label, x.dict_value, 'sys_cash_type', '0',
       'retail-payment-config', NOW(), 'retail-payment-config', NOW(), '零售回租付款场景'
FROM (
    SELECT 201 dict_sort, '融资租赁资产-动产项目-回租' dict_label, 'lease_asset_movable_leaseback' dict_value
    UNION ALL SELECT 202, '融资租赁资产-在建动产项目-回租', 'lease_asset_construction_leaseback'
    UNION ALL SELECT 203, '应付票据-银行承兑汇票', 'bank_acceptance_payable'
    UNION ALL SELECT 204, '渠道商分成应付款', 'channel_commission_payable'
    UNION ALL SELECT 205, '抵押服务费支出', 'mortgage_service_fee_expense'
) x
WHERE NOT EXISTS (
    SELECT 1 FROM sys_dict_data d WHERE d.dict_type = 'sys_cash_type' AND d.dict_value = x.dict_value
);

-- 税率按业务类型配置，付款接口不传税率；tax_general 表示该业务的通用税率。
-- eg_tax_rate.tax_rate 以百分数保存，运行时服务转换为小数参与税额计算。
DELETE FROM eg_tax_rate
WHERE business_code = 'CYC_RETAIL_LEASEBACK'
  AND fund_type IN ('vehicle_management_fee_payable', 'mortgage_service_fee_expense',
                    'payment_channel_fee_expense', 'lease_interest_receivable')
  AND create_by = 'retail-payment-config';

INSERT INTO eg_tax_rate
    (id, business_code, fund_type, enable_flag, enable_date, tax_rate,
     create_by, create_time, update_by, update_time, del_flag, lease_type, lease_sub_type)
SELECT 202609070800001, 'CYC_RETAIL_LEASEBACK', 'tax_general', '1', '2026-09-03', 6.00,
       'retail-payment-config', NOW(), 'retail-payment-config', NOW(), '0',
       'FINANCE_LEASE', 'SALE_AND_LEASEBACK'
WHERE NOT EXISTS (
    SELECT 1 FROM eg_tax_rate t
    WHERE t.business_code = 'CYC_RETAIL_LEASEBACK'
      AND t.fund_type = 'tax_general'
      AND t.enable_flag = '1'
      AND t.del_flag = '0'
      AND t.enable_date <= CURRENT_DATE
);

-- 科目集中配置：只重建付款脚本负责的金额类型，不删除其他场景后来补充的科目。
DELETE FROM eg_account
WHERE business_code = 'CYC_RETAIL_LEASEBACK'
  AND fund_type IN ('lease_asset_movable_leaseback','lease_asset_construction_leaseback',
                    'bank_acceptance_payable','lease_principal_receivable','lease_interest_receivable',
                    'lease_interest_vat_receivable','vehicle_management_fee_payable',
                    'input_vat_pending_certification','vehicle_profit_sharing_payable',
                    'vehicle_profit_sharing_receivable','channel_commission_payable',
                    'bank_service_fee_expense','mortgage_service_fee_expense','payment_channel_fee_expense');
INSERT INTO eg_account
    (id, business_code, business_name, fund_type, account_code, account_name, account_category,
     create_by, create_time, update_by, update_time, del_flag, debit_credit_type,
     client_flag, contract_flag, assist_flags, settlement_type, check_flag)
VALUES
    (202609070700001, 'CYC_RETAIL_LEASEBACK', '融资租赁业务-回租-乘用车', 'lease_asset_movable_leaseback', '1541001', '融资租赁资产-动产项目-回租', '资产', 'retail-payment-config', NOW(), 'retail-payment-config', NOW(), '0', 'DR', '0', '1', '1', 'intra', '0'),
    (202609070700002, 'CYC_RETAIL_LEASEBACK', '融资租赁业务-回租-乘用车', 'lease_asset_construction_leaseback', '15410201', '融资租赁资产-在建动产项目-回租', '资产', 'retail-payment-config', NOW(), 'retail-payment-config', NOW(), '0', 'DR', '0', '1', '1', 'intra', '0'),
    (202609070700003, 'CYC_RETAIL_LEASEBACK', '融资租赁业务-回租-乘用车', 'bank_acceptance_payable', '220101', '应付票据-银行承兑汇票', '负债', 'retail-payment-config', NOW(), 'retail-payment-config', NOW(), '0', 'CR', '0', '1', '1', 'intra', '0'),
    (202609070700004, 'CYC_RETAIL_LEASEBACK', '融资租赁业务-回租-乘用车', 'lease_principal_receivable', '15310301', '应收融资租赁款-应收承租人本金（动产项目）', '资产', 'retail-payment-config', NOW(), 'retail-payment-config', NOW(), '0', 'DR', '1', '1', '0,1', 'intra', '0'),
    (202609070700005, 'CYC_RETAIL_LEASEBACK', '融资租赁业务-回租-乘用车', 'lease_interest_receivable', '15310401', '应收融资租赁款-应收承租人利息（动产项目）', '资产', 'retail-payment-config', NOW(), 'retail-payment-config', NOW(), '0', 'DR', '1', '1', '0,1', 'intra', '0'),
    (202609070700006, 'CYC_RETAIL_LEASEBACK', '融资租赁业务-回租-乘用车', 'lease_interest_vat_receivable', '15311101', '应收融资租赁款-应收承租人利息部分增值税', '资产', 'retail-payment-config', NOW(), 'retail-payment-config', NOW(), '0', 'DR', '1', '1', '0,1', 'intra', '0'),
    (202609070700007, 'CYC_RETAIL_LEASEBACK', '融资租赁业务-回租-乘用车', 'vehicle_management_fee_payable', '220211', '应付账款-应付车辆管理费', '负债', 'retail-payment-config', NOW(), 'retail-payment-config', NOW(), '0', 'CR', '0', '1', '1', 'intra', '0'),
    (202609070700008, 'CYC_RETAIL_LEASEBACK', '融资租赁业务-回租-乘用车', 'input_vat_pending_certification', '222118', '应交税费-待认证进项税额', '负债', 'retail-payment-config', NOW(), 'retail-payment-config', NOW(), '0', 'DR', '0', '1', '1', 'intra', '0'),
    (202609070700009, 'CYC_RETAIL_LEASEBACK', '融资租赁业务-回租-乘用车', 'vehicle_profit_sharing_payable', '220212', '应付账款-应付车辆分润费', '负债', 'retail-payment-config', NOW(), 'retail-payment-config', NOW(), '0', 'CR', '0', '1', '1', 'intra', '0'),
    (202609070700010, 'CYC_RETAIL_LEASEBACK', '融资租赁业务-回租-乘用车', 'vehicle_profit_sharing_receivable', '112503', '应收账款-车辆清分款', '资产', 'retail-payment-config', NOW(), 'retail-payment-config', NOW(), '0', 'DR', '1', '1', '0,1', 'intra', '0'),
    (202609070700011, 'CYC_RETAIL_LEASEBACK', '融资租赁业务-回租-乘用车', 'channel_commission_payable', '224113', '其他应付款-渠道商分成', '负债', 'retail-payment-config', NOW(), 'retail-payment-config', NOW(), '0', 'CR', '0', '1', '1', 'intra', '0'),
    (202609070700012, 'CYC_RETAIL_LEASEBACK', '融资租赁业务-回租-乘用车', 'bank_service_fee_expense', '642101', '手续费及佣金支出-银行手续费', '损益', 'retail-payment-config', NOW(), 'retail-payment-config', NOW(), '0', 'DR', '0', '1', '1', 'intra', '0'),
    (202609070700013, 'CYC_RETAIL_LEASEBACK', '融资租赁业务-回租-乘用车', 'mortgage_service_fee_expense', '64210409', '手续费及佣金支出-抵押服务费', '损益', 'retail-payment-config', NOW(), 'retail-payment-config', NOW(), '0', 'DR', '0', '1', '1', 'intra', '0'),
    (202609070700014, 'CYC_RETAIL_LEASEBACK', '融资租赁业务-回租-乘用车', 'payment_channel_fee_expense', '64210401', '手续费及佣金支出-通联手续费', '损益', 'retail-payment-config', NOW(), 'retail-payment-config', NOW(), '0', 'DR', '0', '1', '1', 'intra', '0');

-- 所有付款事件共用一张凭证模板；eventCode 只在分录条件中分流。
INSERT INTO eg_scene_voucher
    (id, scene_id, source, voucher_type, company, business_date, currency, voucher_summary,
     create_by, create_time, update_by, update_time, del_flag, script_condition,
     scene_voucher_name, sub_scene_type)
SELECT 202609070400000 + seq_no, scene_id, 'systemCode', '02', 'orgId', 'businessDate', 'currency',
       "'零售回租付款-' + orderId + '-' + clientName", 'retail-payment-config', NOW(),
       'retail-payment-config', NOW(), '0', 'true', '零售回租统一付款模板', 'PAYMENT'
FROM tmp_retail_payment_scene;

-- 分录头：一条金额类型可在条件表中有一个事件条件/金额公式。
INSERT INTO eg_scene_voucher_entry
    (id, scene_voucher_id, fund_type, relate_bank_flag, bank_account, cash_attribute,
     voucher_summary, create_by, create_time, update_by, update_time, del_flag,
     cash_attribute_flag, assist_flags)
VALUES
    (202609070500001,202609070400001,'lease_asset_movable_leaseback','0',NULL,NULL,'购入租赁资产','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0','0','1'),
    (202609070500002,202609070400001,'lease_asset_construction_leaseback','0',NULL,NULL,'购入在建租赁资产','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0','0','1'),
    (202609070500003,202609070400001,'bank_acceptance_payable','0',NULL,NULL,'银行承兑汇票支付','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0','0','1'),
    (202609070500004,202609070400001,'bank_deposit','1','bankNo',NULL,'银行收付款','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0','0','2'),
    (202609070500005,202609070400001,'lease_principal_receivable','0',NULL,NULL,'收到承租人首付款','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0','0','0,1'),
    (202609070500021,202609070400001,'vehicle_management_fee_payable','0',NULL,NULL,'支付资产管理费','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0','0','1'),
    (202609070500022,202609070400001,'input_vat_pending_certification','0',NULL,NULL,'待认证进项税','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0','0','1'),
    (202609070500031,202609070400001,'vehicle_profit_sharing_payable','0',NULL,NULL,'平台合作方分润','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0','0','1'),
    (202609070500033,202609070400001,'vehicle_profit_sharing_receivable','0',NULL,NULL,'应收车辆清分款抵扣','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0','0','0,1'),
    (202609070500041,202609070400001,'bank_service_fee_expense','0',NULL,NULL,'支付银行手续费','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0','0','1'),
    (202609070500051,202609070400001,'mortgage_service_fee_expense','0',NULL,NULL,'支付抵押服务费','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0','0','1'),
    (202609070500061,202609070400001,'payment_channel_fee_expense','0',NULL,NULL,'支付通联手续费','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0','0','1'),
    (202609070500071,202609070400001,'channel_commission_payable','0',NULL,NULL,'渠道商分成结算','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0','0','1'),
    (202609070500082,202609070400001,'lease_interest_receivable','0',NULL,NULL,'结清应收租赁利息','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0','0','0,1'),
    (202609070500083,202609070400001,'lease_interest_vat_receivable','0',NULL,NULL,'结清利息部分增值税','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0','0','0,1');

INSERT INTO eg_scene_voucher_condition
    (id, scene_voucher_entry_id, serial, script_condition, dondition_description,
     script_amount, amount_description, debit_credit_type,
     create_by, create_time, update_by, update_time, del_flag)
VALUES
    (202609070600001,202609070500001,1,"eventCode == 'CR003' && assetStage == 'COMPLETED'",'CR003已完工动产项目','paymentAmount','实际支付金额','DR','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0'),
    (202609070600002,202609070500002,1,"eventCode == 'CR003' && assetStage == 'CONSTRUCTION'",'CR003在建动产项目','paymentAmount','实际支付金额','DR','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0'),
    (202609070600003,202609070500003,1,"eventCode == 'CR003' && paymentMethod == 'BANK_ACCEPTANCE'",'CR003银行承兑汇票','paymentAmount','票据支付金额','CR','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0'),
    (202609070600004,202609070500004,1,"eventCode == 'CR003' && paymentMethod == 'BANK_TRANSFER'",'CR003银行转账','paymentAmount','银行支付金额','CR','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0'),
    (202609070600005,202609070500005,1,"eventCode == 'CR003' && initialPaymentAmount > 0",'CR003存在首付款','initialPaymentAmount','首付款','CR','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0'),
    (202609070600006,202609070500004,2,"eventCode == 'CR003' && initialPaymentAmount > 0",'CR003收到首付款','initialPaymentAmount','首付款','DR','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0'),

    (202609070600021,202609070500021,1,"eventCode == 'CR025' && managementFeeAmount > 0",'CR025实际支付资产管理费','managementFeeAmount','付款总额按业务税率反算的不含税资产管理费','DR','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0'),
    (202609070600022,202609070500022,1,"eventCode == 'CR025' && taxAmount > 0",'CR025管理费进项税','taxAmount','税额','DR','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0'),
    (202609070600023,202609070500004,3,"eventCode == 'CR025' && paymentAmount > 0",'CR025银行支付','paymentAmount','实际支付金额','CR','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0'),

    (202609070600031,202609070500031,1,"eventCode == 'CR029' && paymentAmount > 0",'CR029应付分润结算','paymentAmount','分润应付结算额','DR','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0'),
    (202609070600032,202609070500031,2,"eventCode == 'CR029' && refundAmount > 0",'CR029提前结清应退分润','refundAmount','应退分润费','CR','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0'),
    (202609070600033,202609070500033,1,"eventCode == 'CR029' && offsetAmount > 0",'CR029应收款抵扣','offsetAmount','抵扣金额','CR','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0'),
    (202609070600034,202609070500004,4,"eventCode == 'CR029' && bankPaymentAmount > 0",'CR029银行实付','bankPaymentAmount','银行实付金额','CR','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0'),

    (202609070600041,202609070500041,1,"eventCode == 'CR036' && paymentAmount > 0",'CR036银行手续费','paymentAmount','实际支付金额','DR','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0'),
    (202609070600042,202609070500004,5,"eventCode == 'CR036' && paymentAmount > 0",'CR036银行支付','paymentAmount','实际支付金额','CR','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0'),

    (202609070600051,202609070500051,1,"eventCode == 'CR040' && untaxedAmount > 0",'CR040不含税抵押服务费','untaxedAmount','不含税金额','DR','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0'),
    (202609070600052,202609070500022,2,"eventCode == 'CR040' && taxAmount > 0",'CR040抵押服务费进项税','taxAmount','税额','DR','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0'),
    (202609070600053,202609070500004,6,"eventCode == 'CR040' && paymentAmount > 0",'CR040银行支付','paymentAmount','实际支付金额','CR','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0'),

    (202609070600061,202609070500061,1,"eventCode == 'CR041' && untaxedAmount > 0",'CR041不含税通联手续费','untaxedAmount','不含税金额','DR','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0'),
    (202609070600062,202609070500022,3,"eventCode == 'CR041' && taxAmount > 0",'CR041通联手续费进项税','taxAmount','税额','DR','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0'),
    (202609070600063,202609070500004,7,"eventCode == 'CR041' && paymentAmount > 0",'CR041银行支付','paymentAmount','实际支付金额','CR','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0'),

    (202609070600071,202609070500071,1,"eventCode == 'CR044' && paymentAmount > 0",'CR044渠道商分成应付结算','paymentAmount','实际结算金额','DR','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0'),
    (202609070600072,202609070500004,8,"eventCode == 'CR044' && paymentAmount > 0",'CR044银行支付','paymentAmount','实际支付金额','CR','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0'),

    (202609070600081,202609070500004,9,"eventCode == 'CR056' && paymentAmount > 0",'CR056收到结清款','paymentAmount','实际收款金额','DR','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0'),
    (202609070600082,202609070500082,1,"eventCode == 'CR056' && paymentInterestAmount > 0",'CR056不含税利息','paymentInterestAmount','不含税利息','CR','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0'),
    (202609070600083,202609070500083,1,"eventCode == 'CR056' && taxAmount > 0",'CR056利息税额','taxAmount','增值税金额','CR','retail-payment-config',NOW(),'retail-payment-config',NOW(),'0');

-- 模拟“已从金蝶同步”的账号与明细科目。eas_id 为金蝶主键；
-- 1004为存放同业，1002为银行存款。仅使用新旧两套库都具备的公共字段。
DELETE FROM eg_bank_account WHERE id IN (202609070800001,202609070800002,202609070800003,202609070800004);
INSERT INTO eg_bank_account
    (id, eas_id, bank_account_code, bank_account_name, bank_account_number, org_id,
     bank_name, account_name, account_code, currency_code,
     create_by, create_time, update_by, update_time, del_flag)
VALUES
    (202609070800001,'KD-CYC-BA-001','ICBC-CYC-001','工行上海分行付款专户','1000000000000000001','01-C0001','中国工商银行上海分行','银行存款-工商银行','1002.01','CNY','kingdee-sync',NOW(),'kingdee-sync',NOW(),'0'),
    (202609070800002,'KD-CYC-BA-002','CMB-CYC-002','招行上海分行付款专户','1000000000000000002','01-C0001','招商银行上海分行','银行存款-招商银行','1002.12','CNY','kingdee-sync',NOW(),'kingdee-sync',NOW(),'0'),
    (202609070800003,'KD-CYC-BA-003','CIB-CYC-003','兴业银行存放同业专户','1000000000000000003','01-C0001','兴业银行上海分行','存放同业-兴业银行','1004.01','CNY','kingdee-sync',NOW(),'kingdee-sync',NOW(),'0'),
    (202609070800004,'KD-CYC-BA-004','SPDB-CYC-004','浦发银行存放同业专户','1000000000000000004','01-C0001','浦发银行上海分行','存放同业-浦发银行','1004.06','CNY','kingdee-sync',NOW(),'kingdee-sync',NOW(),'0');

-- 余额表采用“资金类型_amount / 资金类型_balance”的动态列模型。
-- 本次新增的四个资金类型同步补列，过程可重复执行。
DROP PROCEDURE IF EXISTS add_retail_payment_balance_column;
DELIMITER $$
CREATE PROCEDURE add_retail_payment_balance_column(IN target_table VARCHAR(64), IN target_column VARCHAR(128))
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables
         WHERE table_schema = DATABASE() AND table_name = target_table
    ) AND NOT EXISTS (
        SELECT 1 FROM information_schema.columns
         WHERE table_schema = DATABASE() AND table_name = target_table AND column_name = target_column
    ) THEN
        SET @retail_payment_ddl = CONCAT(
            'ALTER TABLE `', target_table, '` ADD COLUMN `', target_column,
            '` DECIMAL(20,2) NOT NULL DEFAULT 0 COMMENT ''零售回租付款金额/余额'''
        );
        PREPARE retail_payment_statement FROM @retail_payment_ddl;
        EXECUTE retail_payment_statement;
        DEALLOCATE PREPARE retail_payment_statement;
    END IF;
END$$
DELIMITER ;

CALL add_retail_payment_balance_column('eg_contract_balance', 'lease_asset_movable_leaseback_amount');
CALL add_retail_payment_balance_column('eg_contract_balance', 'lease_asset_movable_leaseback_balance');
CALL add_retail_payment_balance_column('eg_contract_balance', 'lease_asset_construction_leaseback_amount');
CALL add_retail_payment_balance_column('eg_contract_balance', 'lease_asset_construction_leaseback_balance');
CALL add_retail_payment_balance_column('eg_contract_balance', 'mortgage_service_fee_expense_amount');
CALL add_retail_payment_balance_column('eg_contract_balance', 'mortgage_service_fee_expense_balance');
CALL add_retail_payment_balance_column('eg_contract_balance', 'channel_commission_payable_amount');
CALL add_retail_payment_balance_column('eg_contract_balance', 'channel_commission_payable_balance');

CALL add_retail_payment_balance_column('eg_contract_balance_latest', 'lease_asset_movable_leaseback_amount');
CALL add_retail_payment_balance_column('eg_contract_balance_latest', 'lease_asset_movable_leaseback_balance');
CALL add_retail_payment_balance_column('eg_contract_balance_latest', 'lease_asset_construction_leaseback_amount');
CALL add_retail_payment_balance_column('eg_contract_balance_latest', 'lease_asset_construction_leaseback_balance');
CALL add_retail_payment_balance_column('eg_contract_balance_latest', 'mortgage_service_fee_expense_amount');
CALL add_retail_payment_balance_column('eg_contract_balance_latest', 'mortgage_service_fee_expense_balance');
CALL add_retail_payment_balance_column('eg_contract_balance_latest', 'channel_commission_payable_amount');
CALL add_retail_payment_balance_column('eg_contract_balance_latest', 'channel_commission_payable_balance');

CALL add_retail_payment_balance_column('eg_contract_balance_temp', 'lease_asset_movable_leaseback_amount');
CALL add_retail_payment_balance_column('eg_contract_balance_temp', 'lease_asset_movable_leaseback_balance');
CALL add_retail_payment_balance_column('eg_contract_balance_temp', 'lease_asset_construction_leaseback_amount');
CALL add_retail_payment_balance_column('eg_contract_balance_temp', 'lease_asset_construction_leaseback_balance');
CALL add_retail_payment_balance_column('eg_contract_balance_temp', 'mortgage_service_fee_expense_amount');
CALL add_retail_payment_balance_column('eg_contract_balance_temp', 'mortgage_service_fee_expense_balance');
CALL add_retail_payment_balance_column('eg_contract_balance_temp', 'channel_commission_payable_amount');
CALL add_retail_payment_balance_column('eg_contract_balance_temp', 'channel_commission_payable_balance');

DROP PROCEDURE add_retail_payment_balance_column;

COMMIT;

DROP TEMPORARY TABLE IF EXISTS tmp_retail_payment_field;
DROP TEMPORARY TABLE IF EXISTS tmp_retail_payment_scene;

-- 验收查询
SELECT s.scene_code, s.scene_name, COUNT(DISTINCT e.id) entry_count
FROM eg_scene s
JOIN eg_scene_voucher v ON v.scene_id = s.id AND v.del_flag = '0'
JOIN eg_scene_voucher_entry e ON e.scene_voucher_id = v.id AND e.del_flag = '0'
WHERE s.scene_code = 'CYC_PAYMENT'
GROUP BY s.scene_code, s.scene_name ORDER BY s.scene_code;

SELECT COUNT(DISTINCT v.id) voucher_template_count,
       COUNT(DISTINCT e.id) entry_head_count,
       COUNT(DISTINCT e.fund_type) distinct_fund_type_count,
       COUNT(DISTINCT c.id) event_condition_count
FROM eg_scene s
JOIN eg_scene_voucher v ON v.scene_id = s.id AND v.del_flag = '0'
JOIN eg_scene_voucher_entry e ON e.scene_voucher_id = v.id AND e.del_flag = '0'
JOIN eg_scene_voucher_condition c ON c.scene_voucher_entry_id = e.id AND c.del_flag = '0'
WHERE s.scene_code = 'CYC_PAYMENT';

SELECT account_code, account_name, COUNT(*) mapping_count
FROM eg_account WHERE business_code = 'CYC_RETAIL_LEASEBACK' AND del_flag = '0'
GROUP BY account_code, account_name HAVING COUNT(*) > 1;

SELECT bank_account_number, bank_name, account_code, account_name, eas_id
FROM eg_bank_account WHERE id IN (202609070800001,202609070800002,202609070800003,202609070800004)
ORDER BY bank_account_number;

SELECT field_name, source_value, target_field_code, target_value
FROM eg_field_mapping WHERE id BETWEEN 202609070900001 AND 202609070900009
ORDER BY id;
