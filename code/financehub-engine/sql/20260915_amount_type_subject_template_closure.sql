-- 华夏金租金额类型/科目/凭证模板闭环修正。
-- 金额类型只表示经济事项；业务品种、资产阶段及动产/不动产由业务条件决定。
-- 本脚本只软停用旧配置，不修改已经生成的历史凭证。

USE financehub_lease;
START TRANSACTION;

-- 原模板「会计科目体系」：动产项目回租=15410201，印花税=640503/222113。
UPDATE eg_account
SET fund_type='lease_asset_cost',account_code='15410201',
    account_name='融资租赁资产_动产项目_回租',
    update_by='amount-closure-v1',update_time=NOW()
WHERE id=202609070700001 AND business_code='CYC_RETAIL_LEASEBACK' AND del_flag='0';
UPDATE eg_account
SET del_flag='1',update_by='amount-closure-v1',update_time=NOW()
WHERE id=202609070700002 AND business_code='CYC_RETAIL_LEASEBACK' AND del_flag='0';
UPDATE eg_account
SET account_code='640503',account_name='税金及附加_印花税',
    update_by='amount-closure-v1',update_time=NOW()
WHERE id=202609071600008 AND business_code='CYC_RETAIL_LEASEBACK' AND fund_type='stamp_duty_expense';
UPDATE eg_account
SET account_code='222113',account_name='应交税费_应交税金-印花税',
    update_by='amount-closure-v1',update_time=NOW()
WHERE id=202609071600009 AND business_code='CYC_RETAIL_LEASEBACK' AND fund_type='stamp_duty_payable';

-- 零售乘用车回租原来误用不动产科目；逾期类使用动产/回租末级科目。
UPDATE eg_account SET account_code='220302',account_name='预收账款_预收租金（动产项目）',
    update_by='amount-closure-v1',update_time=NOW()
WHERE id=202609096000001 AND business_code='CYC_RETAIL_LEASEBACK' AND fund_type='advance_lease_receipts';
UPDATE eg_account SET account_code='15310601',account_name='应收融资租赁款_应收承租人逾期本金（动产项目）_回租',
    update_by='amount-closure-v1',update_time=NOW()
WHERE id=202609096000008 AND business_code='CYC_RETAIL_LEASEBACK' AND fund_type='overdue_lease_principal';
UPDATE eg_account SET account_code='15311001',account_name='应收融资租赁款_应收承租人逾期利息（动产项目）_回租',
    update_by='amount-closure-v1',update_time=NOW()
WHERE id=202609096000009 AND business_code='CYC_RETAIL_LEASEBACK' AND fund_type='overdue_lease_interest';
UPDATE eg_account SET account_code='60410401',account_name='租赁收入_延期收款收入（动产项目）_回租',
    update_by='amount-closure-v1',update_time=NOW()
WHERE id=202609096000011 AND business_code='CYC_RETAIL_LEASEBACK' AND fund_type='penalty_interest_income';
UPDATE eg_account SET account_code='220202',account_name='应付账款_应付融资租赁设备款（动产项目）',
    update_by='amount-closure-v1',update_time=NOW()
WHERE id=202609096000016 AND business_code='CYC_RETAIL_LEASEBACK' AND fund_type='supplier_payable';
UPDATE eg_account SET account_code='15312801',account_name='应收融资租赁款_逾期留购价（动产项目）_回租',
    update_by='amount-closure-v1',update_time=NOW()
WHERE id=202609075700003 AND business_code='CYC_RETAIL_LEASEBACK' AND fund_type='overdue_residual_value';
UPDATE eg_account SET account_code='15313001',account_name='应收融资租赁款_应收承租人逾期留购价部分增值税（动产项目）_回租',
    update_by='amount-closure-v1',update_time=NOW()
WHERE id=202609075700004 AND business_code='CYC_RETAIL_LEASEBACK' AND fund_type='overdue_residual_value_vat';

-- 其他业务中，同一科目不因「促销」另造金额类型；提前终止分成冲减的是收入。
UPDATE eg_account
SET account_code='60410201',account_name='租赁收入_利息收入（动产项目）_回租',
    update_by='amount-closure-v1',update_time=NOW()
WHERE id=202609096000006 AND business_code='CYC_RETAIL_LEASEBACK' AND fund_type='lease_interest_income';
UPDATE eg_account
SET fund_type='early_termination_income',
    account_name='其他业务收入_提前终止合同收入',
    update_by='amount-closure-v1',update_time=NOW()
WHERE id=202609106000005 AND business_code='CYC_RETAIL_LEASEBACK' AND fund_type='early_termination_share_expense';
UPDATE eg_account
SET del_flag='1',update_by='amount-closure-v1',update_time=NOW()
WHERE id=202609106000007 AND business_code='CYC_RETAIL_LEASEBACK' AND fund_type='lease_interest_income_promotion';

-- 一个金额类型在同一凭证里只配一次：不同条件保留为该分录的条件行。
UPDATE eg_scene_voucher_entry SET fund_type='lease_asset_cost',
    voucher_summary="'购入融资租赁资产'",update_by='amount-closure-v1',update_time=NOW()
WHERE id=202609070500001 AND del_flag='0';
UPDATE eg_scene_voucher_condition SET scene_voucher_entry_id=202609070500001,serial=2,
    update_by='amount-closure-v1',update_time=NOW()
WHERE id=202609070600002 AND del_flag='0';
UPDATE eg_scene_voucher_entry SET del_flag='1',update_by='amount-closure-v1',update_time=NOW()
WHERE id=202609070500002 AND del_flag='0';

UPDATE eg_scene_voucher_entry SET fund_type='lease_asset_cost',
    update_by='amount-closure-v1',update_time=NOW()
WHERE id IN (202609075400002,202609094001002,202609094001014) AND del_flag='0';
UPDATE eg_scene_voucher_condition SET scene_voucher_entry_id=202609094001002,serial=2,
    update_by='amount-closure-v1',update_time=NOW()
WHERE id=202609095001011 AND del_flag='0';
UPDATE eg_scene_voucher_condition SET scene_voucher_entry_id=202609094001014,serial=2,
    update_by='amount-closure-v1',update_time=NOW()
WHERE id=202609095001023 AND del_flag='0';
UPDATE eg_scene_voucher_entry SET del_flag='1',update_by='amount-closure-v1',update_time=NOW()
WHERE id IN (202609094001011,202609094001023) AND del_flag='0';

-- 零售起租按原规则模板用「实际投放金额」结转；非零售用系统计算本金。
UPDATE eg_scene_voucher_condition
SET script_condition="{起租接口表.系统来源}!='RETAIL_FINANCE_LEASE'&&{起租计算结果表.本金结转方式}=='LEASE_ASSET'&&{起租计算结果表.起租不含税本金}!=0",
    update_by='amount-closure-v1',update_time=NOW()
WHERE id=2110000000000023001 AND del_flag='0';
UPDATE eg_scene_voucher_condition SET scene_voucher_entry_id=2110000000000011001,serial=2,
    update_by='amount-closure-v1',update_time=NOW()
WHERE id=2110000000000023002 AND del_flag='0';
UPDATE eg_scene_voucher_entry SET del_flag='1',update_by='amount-closure-v1',update_time=NOW()
WHERE id=2110000000000011002 AND del_flag='0';

UPDATE eg_scene_voucher_entry SET fund_type='early_termination_income',
    update_by='amount-closure-v1',update_time=NOW()
WHERE id=202609105000140 AND del_flag='0';
UPDATE eg_scene_voucher_entry SET fund_type='lease_interest_income',
    update_by='amount-closure-v1',update_time=NOW()
WHERE id=202609105000162 AND del_flag='0';

-- 此类字段不是金额类型：保留旧数据供历史凭证追溯，停用字典入口。
UPDATE financehub_lease.sys_dict_data SET status='1',remark='业务品种/阶段不是金额类型；使用lease_asset_cost'
WHERE dict_type='sys_cash_type' AND dict_value IN ('lease_asset_movable_leaseback','lease_asset_construction_leaseback');
UPDATE financialdb4.sys_dict_data SET status='1',remark='业务品种/阶段不是金额类型；使用lease_asset_cost'
WHERE dict_type='sys_cash_type' AND dict_value IN ('lease_asset_movable_leaseback','lease_asset_construction_leaseback');

COMMIT;

-- 必须全部为0：无效科目编码、起租同金额类型重复、停用金额类型仍被有效凭证使用。
SELECT COUNT(*) AS invalid_subject_1541001_640305 FROM eg_account
WHERE del_flag='0' AND account_code IN ('1541001','640305');
SELECT COUNT(*) AS duplicate_htqz_fund_types FROM (
 SELECT e.fund_type FROM eg_scene_voucher_entry e
 JOIN eg_scene_voucher v ON v.id=e.scene_voucher_id AND v.del_flag='0'
 JOIN eg_scene s ON s.id=v.scene_id AND s.scene_code='HTQZ' AND s.del_flag='0'
 WHERE e.del_flag='0' GROUP BY e.fund_type HAVING COUNT(*)>1
) x;
SELECT COUNT(*) AS retired_amount_types_in_active_templates FROM eg_scene_voucher_entry e
JOIN eg_scene_voucher v ON v.id=e.scene_voucher_id AND v.del_flag='0'
WHERE e.del_flag='0' AND e.fund_type IN
 ('lease_asset_movable_leaseback','lease_asset_construction_leaseback',
  'early_termination_share_expense','lease_interest_income_promotion');
