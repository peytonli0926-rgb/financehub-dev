-- Tail-difference adjustment configuration for the latest balance model.
-- Runtime databases in the local development environment:
--   financehub_lease: engine configuration and business data
--   financialdb4:     admin dictionaries exposed by RemoteDictService

START TRANSACTION;

UPDATE financehub_lease.eg_scene
SET enable_flag = '1', del_flag = '0', update_by = 'tail-difference-config', update_time = NOW()
WHERE scene_code = 'WCTZ';

-- initData obtains the selectable account list from the admin dictionary service.
INSERT INTO financialdb4.sys_dict_data
    (dict_sort, dict_label, dict_value, dict_type, is_default, status,
     create_by, create_time, update_by, update_time, remark)
SELECT 1, '未实现融资收益_利息（动产项目）_回租', '15320201',
       'mantissa_adjust_account', 'N', '0',
       'tail-difference-config', NOW(), 'tail-difference-config', NOW(),
       '尾差调整：对应最新余额字段 unearned_lease_interest_balance'
WHERE NOT EXISTS (
    SELECT 1 FROM financialdb4.sys_dict_data
    WHERE dict_type = 'mantissa_adjust_account' AND dict_value = '15320201'
);

UPDATE financialdb4.sys_dict_data
SET dict_label = '未实现融资收益_利息（动产项目）_回租', status = '0',
    update_by = 'tail-difference-config', update_time = NOW(),
    remark = '尾差调整：对应最新余额字段 unearned_lease_interest_balance'
WHERE dict_type = 'mantissa_adjust_account' AND dict_value = '15320201';

-- Keep the engine-side dictionary copy aligned for environments without split databases.
INSERT INTO financehub_lease.sys_dict_data
    (dict_sort, dict_label, dict_value, dict_type, is_default, status,
     create_by, create_time, update_by, update_time, remark)
SELECT 1, '未实现融资收益_利息（动产项目）_回租', '15320201',
       'mantissa_adjust_account', 'N', '0',
       'tail-difference-config', NOW(), 'tail-difference-config', NOW(),
       '尾差调整：对应最新余额字段 unearned_lease_interest_balance'
WHERE NOT EXISTS (
    SELECT 1 FROM financehub_lease.sys_dict_data
    WHERE dict_type = 'mantissa_adjust_account' AND dict_value = '15320201'
);

UPDATE financehub_lease.sys_dict_data
SET dict_label = '未实现融资收益_利息（动产项目）_回租', status = '0',
    update_by = 'tail-difference-config', update_time = NOW(),
    remark = '尾差调整：对应最新余额字段 unearned_lease_interest_balance'
WHERE dict_type = 'mantissa_adjust_account' AND dict_value = '15320201';

-- Add accountCode as a rule variable. The service already supplies this property.
INSERT INTO financehub_lease.eg_scene_fields
    (id, scene_id, scene_code, scene_name, field_name, field_code, data_type,
     create_by, create_time, update_by, update_time, del_flag, parent_id, required_flag, sort_no)
VALUES
    (202609110100001, 1715254732115357697, 'WCTZ', '尾差调整', '科目代码', 'accountCode', 'String',
     'tail-difference-config', NOW(), 'tail-difference-config', NOW(), '0', NULL, '0', 16)
ON DUPLICATE KEY UPDATE
    field_name = VALUES(field_name), field_code = VALUES(field_code), data_type = VALUES(data_type),
    update_by = VALUES(update_by), update_time = NOW(), del_flag = '0', sort_no = VALUES(sort_no);

-- One current template handles both positive and negative residual balances.
INSERT INTO financehub_lease.eg_scene_voucher
    (id, scene_id, source, voucher_type, company, business_date, currency, voucher_summary,
     create_by, create_time, update_by, update_time, del_flag, script_condition,
     scene_voucher_name, sub_scene_type)
VALUES
    (202609110200001, 1715254732115357697,
     '{尾差调整接口表.来源系统}', '03', '{尾差调整接口表.签约主体}',
     '{尾差调整接口表.业务发生日期}', '{合同表.币种}',
     "'尾差调整'+'-'+{尾差调整接口表.合同编号}",
     'tail-difference-config', NOW(), 'tail-difference-config', NOW(), '0',
     "{尾差调整接口表.来源系统}=='FINHUB'&&{尾差调整接口表.科目代码}=='15320201'&&{尾差调整接口表.科目余额}!=0&&-1<={尾差调整接口表.科目余额}&&{尾差调整接口表.科目余额}<=1",
     '最新余额-未实现融资收益利息尾差', '尾差调整')
ON DUPLICATE KEY UPDATE
    source = VALUES(source), voucher_type = VALUES(voucher_type), company = VALUES(company),
    business_date = VALUES(business_date), currency = VALUES(currency), voucher_summary = VALUES(voucher_summary),
    update_by = VALUES(update_by), update_time = NOW(), del_flag = '0',
    script_condition = VALUES(script_condition), scene_voucher_name = VALUES(scene_voucher_name),
    sub_scene_type = VALUES(sub_scene_type);

INSERT INTO financehub_lease.eg_scene_voucher_entry
    (id, scene_voucher_id, fund_type, relate_bank_flag, voucher_summary,
     create_by, create_time, update_by, update_time, del_flag, cash_attribute_flag, assist_flags)
VALUES
    (202609110300001, 202609110200001, 'unearned_lease_interest', '0', '结清未实现融资收益尾差',
     'tail-difference-config', NOW(), 'tail-difference-config', NOW(), '0', '0', '0,1'),
    (202609110300002, 202609110200001, 'lease_interest_income', '0', '结转租赁利息收入尾差',
     'tail-difference-config', NOW(), 'tail-difference-config', NOW(), '0', '0', '0,1')
ON DUPLICATE KEY UPDATE
    fund_type = VALUES(fund_type), relate_bank_flag = VALUES(relate_bank_flag),
    voucher_summary = VALUES(voucher_summary), update_by = VALUES(update_by), update_time = NOW(),
    del_flag = '0', cash_attribute_flag = VALUES(cash_attribute_flag), assist_flags = VALUES(assist_flags);

INSERT INTO financehub_lease.eg_scene_voucher_condition
    (id, scene_voucher_entry_id, serial, script_condition, dondition_description,
     script_amount, amount_description, debit_credit_type,
     create_by, create_time, update_by, update_time, del_flag)
VALUES
    (202609110400001, 202609110300001, 1, '{尾差调整接口表.科目余额}>0', '贷方科目正尾差转出',
     '{尾差调整接口表.科目余额}', '正尾差金额', 'DR',
     'tail-difference-config', NOW(), 'tail-difference-config', NOW(), '0'),
    (202609110400002, 202609110300001, 2, '{尾差调整接口表.科目余额}<0', '贷方科目负尾差冲回',
     '0-{尾差调整接口表.科目余额}', '负尾差绝对值', 'CR',
     'tail-difference-config', NOW(), 'tail-difference-config', NOW(), '0'),
    (202609110400003, 202609110300002, 1, '{尾差调整接口表.科目余额}>0', '正尾差转租赁利息收入',
     '{尾差调整接口表.科目余额}', '正尾差金额', 'CR',
     'tail-difference-config', NOW(), 'tail-difference-config', NOW(), '0'),
    (202609110400004, 202609110300002, 2, '{尾差调整接口表.科目余额}<0', '负尾差冲回租赁利息收入',
     '0-{尾差调整接口表.科目余额}', '负尾差绝对值', 'DR',
     'tail-difference-config', NOW(), 'tail-difference-config', NOW(), '0')
ON DUPLICATE KEY UPDATE
    scene_voucher_entry_id = VALUES(scene_voucher_entry_id), serial = VALUES(serial),
    script_condition = VALUES(script_condition), dondition_description = VALUES(dondition_description),
    script_amount = VALUES(script_amount), amount_description = VALUES(amount_description),
    debit_credit_type = VALUES(debit_credit_type), update_by = VALUES(update_by),
    update_time = NOW(), del_flag = '0';

UPDATE financehub_lease.eg_scene
SET version = COALESCE(version, 0) + 1, update_by = 'tail-difference-config', update_time = NOW()
WHERE id = 1715254732115357697;

COMMIT;

SELECT s.scene_code, s.enable_flag, v.scene_voucher_name, v.company,
       e.fund_type, c.script_condition, c.script_amount, c.debit_credit_type
FROM financehub_lease.eg_scene s
JOIN financehub_lease.eg_scene_voucher v ON v.scene_id = s.id AND v.del_flag = '0'
JOIN financehub_lease.eg_scene_voucher_entry e ON e.scene_voucher_id = v.id AND e.del_flag = '0'
JOIN financehub_lease.eg_scene_voucher_condition c ON c.scene_voucher_entry_id = e.id AND c.del_flag = '0'
WHERE v.id = 202609110200001
ORDER BY e.id, c.serial;
