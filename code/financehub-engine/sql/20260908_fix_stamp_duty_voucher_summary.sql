-- 修复零售回租计提印花税凭证摘要：
-- 规则引擎按 Aviator 表达式执行摘要，固定文本必须以字符串字面量保存。
START TRANSACTION;

UPDATE eg_scene_voucher_entry
SET voucher_summary = CASE fund_type
    WHEN 'stamp_duty_expense' THEN '''计提印花税费用'''
    WHEN 'stamp_duty_payable' THEN '''计提应交印花税'''
END,
    update_by = 'fix-stamp-duty-summary',
    update_time = NOW()
WHERE scene_voucher_id = 202609071300008
  AND fund_type IN ('stamp_duty_expense', 'stamp_duty_payable')
  AND del_flag = '0';

UPDATE eg_voucher_entry e
JOIN eg_voucher v ON v.id = e.voucher_id
SET e.voucher_summary = CASE e.account_code
    WHEN '640305' THEN '计提印花税费用'
    WHEN '222108' THEN '计提应交印花税'
END,
    e.update_by = 'fix-stamp-duty-summary',
    e.update_time = NOW()
WHERE v.scene_code = 'CYC_TAX_ACCRUAL'
  AND e.account_code IN ('640305', '222108')
  AND (e.voucher_summary IS NULL OR e.voucher_summary = '')
  AND v.del_flag = '0'
  AND e.del_flag = '0';

COMMIT;
