-- 零售回租展示案例补充事件：支付印花税。
-- 仅增量扩展现有 CYC_PAYMENT 场景，可重复执行。
START TRANSACTION;

DELETE FROM eg_field_mapping WHERE id = 202609099000001;
INSERT INTO eg_field_mapping
    (id, system_code, field_code, field_name, source_value, target_value, default_value,
     create_by, create_time, update_by, update_time, del_flag, target_field_code)
VALUES
    (202609099000001, 'CYCXT', 'eventCode', '支付印花税', '支付印花税,CR060', 'CR060', NULL,
     'retail-lifecycle-demo', NOW(), 'retail-lifecycle-demo', NOW(), '0', 'paymentEventCode');

UPDATE eg_field_mapping
SET source_value = '购入租赁资产,CR003,支付资产管理费,CR025,平台合作方分润,退回平台合作方提前结清贴息金额,CR029,支付银行手续费,CR036,抵押服务费,CR040,支付通联手续费,CR041,渠道商分成结算,CR044,合作方提前结清,CR056,支付印花税,CR060',
    update_time = NOW()
WHERE id = 202609070900001;

DELETE FROM eg_scene_voucher_condition WHERE id IN (202609099000011, 202609099000012);
DELETE FROM eg_scene_voucher_entry WHERE id = 202609099000010;

INSERT INTO eg_scene_voucher_entry
    (id, scene_voucher_id, fund_type, relate_bank_flag, bank_account, cash_attribute,
     voucher_summary, create_by, create_time, update_by, update_time, del_flag,
     cash_attribute_flag, assist_flags)
VALUES
    (202609099000010, 202609070400001, 'stamp_duty_payable', '0', NULL, NULL,
     '支付印花税', 'retail-lifecycle-demo', NOW(), 'retail-lifecycle-demo', NOW(), '0', '0', '1');

INSERT INTO eg_scene_voucher_condition
    (id, scene_voucher_entry_id, serial, script_condition, dondition_description,
     script_amount, amount_description, debit_credit_type,
     create_by, create_time, update_by, update_time, del_flag)
VALUES
    (202609099000011, 202609099000010, 1,
     "eventCode == 'CR060' && paymentAmount > 0", '支付应交印花税',
     'paymentAmount', '支付印花税金额', 'DR',
     'retail-lifecycle-demo', NOW(), 'retail-lifecycle-demo', NOW(), '0'),
    (202609099000012, 202609070500004, 90,
     "eventCode == 'CR060' && paymentAmount > 0", '银行支付印花税',
     'paymentAmount', '支付印花税金额', 'CR',
     'retail-lifecycle-demo', NOW(), 'retail-lifecycle-demo', NOW(), '0');

COMMIT;
