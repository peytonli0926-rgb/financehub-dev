-- 零售回租凭证模板复核修正：统一起租结转科目及关键科目方向。
START TRANSACTION;

-- 起租资产结转统一使用 lease_asset_cost；业务类型决定15410201等科目。

UPDATE eg_account
SET account_name='未实现融资收益_利息（动产项目）_回租',debit_credit_type='CR',
    update_by='retail-template-audit',update_time=NOW()
WHERE business_code='CYC_RETAIL_LEASEBACK' AND fund_type='unearned_lease_interest' AND del_flag='0';

UPDATE eg_account
SET account_name='其他应收款_待收增值税进项税',debit_credit_type='DR',
    update_by='retail-template-audit',update_time=NOW()
WHERE business_code='CYC_RETAIL_LEASEBACK' AND fund_type='input_vat_receivable' AND del_flag='0';

UPDATE eg_account
SET account_name='应付账款_应付车辆分润费',debit_credit_type='CR',
    update_by='retail-template-audit',update_time=NOW()
WHERE business_code='CYC_RETAIL_LEASEBACK' AND fund_type='vehicle_profit_sharing_payable' AND del_flag='0';

COMMIT;

SELECT s.scene_code,v.scene_voucher_name,e.fund_type,c.debit_credit_type,c.script_amount
FROM eg_scene s
JOIN eg_scene_voucher v ON v.scene_id=s.id AND v.del_flag='0'
JOIN eg_scene_voucher_entry e ON e.scene_voucher_id=v.id AND e.del_flag='0'
JOIN eg_scene_voucher_condition c ON c.scene_voucher_entry_id=e.id AND c.del_flag='0'
WHERE s.scene_code IN ('HTQZ','CYC_PROFIT_SHARING_CONFIRM')
ORDER BY s.scene_code,v.id,c.serial,c.id;
