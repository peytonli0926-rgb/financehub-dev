-- 收益计提：冲减当期平台分润费对应的利息收入。
-- incomeAdjust 使用正数传入；凭证按借记利息收入、贷记未实现融资收益生成冲回分录。
START TRANSACTION;

DELETE FROM eg_scene_voucher_condition
WHERE id IN (2096000100000000123,2096000100000000124);

DELETE FROM eg_scene_voucher_entry
WHERE id IN (2096000100000000023,2096000100000000024);

INSERT INTO eg_scene_voucher_entry
(id,scene_voucher_id,fund_type,relate_bank_flag,voucher_summary,
 create_by,create_time,update_by,update_time,del_flag,cash_attribute_flag,assist_flags)
VALUES
(2096000100000000023,2096000100000000001,'lease_interest_income','0',
 '''冲减平台方分润费计提的收入''','local-admin',NOW(),'local-admin',NOW(),'0','0','0,1'),
(2096000100000000024,2096000100000000001,'unearned_lease_interest','0',
 '''冲减平台方分润费计提的收入''','local-admin',NOW(),'local-admin',NOW(),'0','0','0,1');

INSERT INTO eg_scene_voucher_condition
(id,scene_voucher_entry_id,serial,script_condition,dondition_description,script_amount,
 amount_description,debit_credit_type,create_by,create_time,update_by,update_time,del_flag)
VALUES
(2096000100000000123,2096000100000000023,13,
 '{收益计提接口表.收益计提调整额}>0','存在当期分润费收入冲减额',
 '{收益计提接口表.收益计提调整额}','冲减平台方分润费计提的收入','DR',
 'local-admin',NOW(),'local-admin',NOW(),'0'),
(2096000100000000124,2096000100000000024,14,
 '{收益计提接口表.收益计提调整额}>0','存在当期分润费收入冲减额',
 '{收益计提接口表.收益计提调整额}','冲减平台方分润费计提的收入','CR',
 'local-admin',NOW(),'local-admin',NOW(),'0');

COMMIT;

SELECT e.fund_type,c.debit_credit_type,c.script_condition,c.script_amount
FROM eg_scene_voucher_entry e
JOIN eg_scene_voucher_condition c ON c.scene_voucher_entry_id=e.id AND c.del_flag='0'
WHERE e.id IN (2096000100000000023,2096000100000000024)
ORDER BY c.serial;
