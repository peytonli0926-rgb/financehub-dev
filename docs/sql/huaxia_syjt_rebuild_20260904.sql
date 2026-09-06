CREATE TABLE IF NOT EXISTS bak_syjt_scene_20260904 LIKE eg_scene;
INSERT IGNORE INTO bak_syjt_scene_20260904 SELECT * FROM eg_scene WHERE scene_code = 'SYJT';
CREATE TABLE IF NOT EXISTS bak_syjt_voucher_20260904 LIKE eg_scene_voucher;
INSERT IGNORE INTO bak_syjt_voucher_20260904 SELECT * FROM eg_scene_voucher WHERE scene_id = 1722178556031397889;
CREATE TABLE IF NOT EXISTS bak_syjt_entry_20260904 LIKE eg_scene_voucher_entry;
INSERT IGNORE INTO bak_syjt_entry_20260904 SELECT * FROM eg_scene_voucher_entry
 WHERE scene_voucher_id IN (SELECT id FROM eg_scene_voucher WHERE scene_id = 1722178556031397889);
CREATE TABLE IF NOT EXISTS bak_syjt_condition_20260904 LIKE eg_scene_voucher_condition;
INSERT IGNORE INTO bak_syjt_condition_20260904 SELECT * FROM eg_scene_voucher_condition
 WHERE scene_voucher_entry_id IN (SELECT id FROM eg_scene_voucher_entry
   WHERE scene_voucher_id IN (SELECT id FROM eg_scene_voucher WHERE scene_id = 1722178556031397889));
CREATE TABLE IF NOT EXISTS bak_syjt_fields_20260904 LIKE eg_scene_fields;
INSERT IGNORE INTO bak_syjt_fields_20260904 SELECT * FROM eg_scene_fields WHERE scene_id = 1722178556031397889;

UPDATE eg_scene SET scene_name = '收益计提', scene_period = 'order', enable_flag = '1',
 del_flag = '0', update_time = NOW(), version = version + 1 WHERE id = 1722178556031397889;
UPDATE eg_scene_fields SET del_flag = '0', update_time = NOW() WHERE scene_id = 1722178556031397889;
UPDATE eg_scene_voucher SET del_flag = '1', update_time = NOW() WHERE scene_id = 1722178556031397889;
UPDATE eg_scene_voucher_entry SET del_flag = '1', update_time = NOW()
 WHERE scene_voucher_id IN (SELECT id FROM eg_scene_voucher WHERE scene_id = 1722178556031397889);
UPDATE eg_scene_voucher_condition SET del_flag = '1', update_time = NOW()
 WHERE scene_voucher_entry_id IN (SELECT id FROM eg_scene_voucher_entry
   WHERE scene_voucher_id IN (SELECT id FROM eg_scene_voucher WHERE scene_id = 1722178556031397889));

INSERT INTO eg_scene_voucher
(id, scene_id, source, voucher_type, company, business_date, voucher_date, currency, dept_name,
 voucher_summary, create_by, create_time, update_by, update_time, del_flag, script_condition,
 scene_voucher_name, sub_scene_type)
VALUES
(2096000100000000001, 1722178556031397889, '{收益计提接口表.来源系统}', '03',
 '{收益计提接口表.签约主体}', '{收益计提接口表.业务发生日期}', NULL,
 '{收益计提接口表.币种}', NULL, '''确认''+{收益计提接口表.计提月份}+''融资租赁收益''',
 'local-admin', NOW(), 'local-admin', NOW(), '0',
 '{收益计提接口表.是否计提}==''是''&&({收益计提接口表.收益计提金额}!=0||{收益计提接口表.表外计提金额}!=0||{收益计提接口表.表内转表外金额}!=0||{收益计提接口表.表外转表内金额}!=0)',
 '华夏金租融资租赁收益计提', '11');

INSERT INTO eg_scene_voucher_entry
(id, scene_voucher_id, fund_type, relate_bank_flag, bank_account, cash_attribute, voucher_summary,
 create_by, create_time, update_by, update_time, del_flag, cash_attribute_flag, assist_flags)
VALUES
(2096000100000000011,2096000100000000001,'unearned_lease_interest','0',NULL,NULL,'确认融资租赁表内收益','local-admin',NOW(),'local-admin',NOW(),'0','0','0,1'),
(2096000100000000012,2096000100000000001,'lease_interest_income','0',NULL,NULL,'确认融资租赁表内收益','local-admin',NOW(),'local-admin',NOW(),'0','0','0,1'),
(2096000100000000013,2096000100000000001,'off_balance_lease_interest_receivable','0',NULL,NULL,'确认融资租赁表外收益','local-admin',NOW(),'local-admin',NOW(),'0','0','0,1'),
(2096000100000000014,2096000100000000001,'off_balance_offset','0',NULL,NULL,'确认融资租赁表外收益','local-admin',NOW(),'local-admin',NOW(),'0','0','0,1'),
(2096000100000000015,2096000100000000001,'lease_interest_income','0',NULL,NULL,'表内收益转表外','local-admin',NOW(),'local-admin',NOW(),'0','0','0,1'),
(2096000100000000016,2096000100000000001,'unearned_lease_interest','0',NULL,NULL,'表内收益转表外','local-admin',NOW(),'local-admin',NOW(),'0','0','0,1'),
(2096000100000000017,2096000100000000001,'off_balance_lease_interest_receivable','0',NULL,NULL,'表内收益转表外','local-admin',NOW(),'local-admin',NOW(),'0','0','0,1'),
(2096000100000000018,2096000100000000001,'off_balance_offset','0',NULL,NULL,'表内收益转表外','local-admin',NOW(),'local-admin',NOW(),'0','0','0,1'),
(2096000100000000019,2096000100000000001,'unearned_lease_interest','0',NULL,NULL,'表外收益转表内','local-admin',NOW(),'local-admin',NOW(),'0','0','0,1'),
(2096000100000000020,2096000100000000001,'lease_interest_income','0',NULL,NULL,'表外收益转表内','local-admin',NOW(),'local-admin',NOW(),'0','0','0,1'),
(2096000100000000021,2096000100000000001,'off_balance_offset','0',NULL,NULL,'表外收益转表内','local-admin',NOW(),'local-admin',NOW(),'0','0','0,1'),
(2096000100000000022,2096000100000000001,'off_balance_lease_interest_receivable','0',NULL,NULL,'表外收益转表内','local-admin',NOW(),'local-admin',NOW(),'0','0','0,1');

INSERT INTO eg_scene_voucher_condition
(id, scene_voucher_entry_id, serial, script_condition, dondition_description, script_amount,
 amount_description, debit_credit_type, create_by, create_time, update_by, update_time, del_flag)
VALUES
(2096000100000000111,2096000100000000011,1,'{收益计提接口表.收益计提金额}!=0','表内收益非零','{收益计提接口表.收益计提金额}','本期表内收益','DR','local-admin',NOW(),'local-admin',NOW(),'0'),
(2096000100000000112,2096000100000000012,2,'{收益计提接口表.收益计提金额}!=0','表内收益非零','{收益计提接口表.收益计提金额}','本期表内收益','CR','local-admin',NOW(),'local-admin',NOW(),'0'),
(2096000100000000113,2096000100000000013,3,'{收益计提接口表.表外计提金额}!=0','表外收益非零','{收益计提接口表.表外计提金额}','本期表外收益','DR','local-admin',NOW(),'local-admin',NOW(),'0'),
(2096000100000000114,2096000100000000014,4,'{收益计提接口表.表外计提金额}!=0','表外收益非零','{收益计提接口表.表外计提金额}','本期表外收益','CR','local-admin',NOW(),'local-admin',NOW(),'0'),
(2096000100000000115,2096000100000000015,5,'{收益计提接口表.表内转表外金额}!=0','表内转表外非零','{收益计提接口表.表内转表外金额}','表内转表外','DR','local-admin',NOW(),'local-admin',NOW(),'0'),
(2096000100000000116,2096000100000000016,6,'{收益计提接口表.表内转表外金额}!=0','表内转表外非零','{收益计提接口表.表内转表外金额}','表内转表外','CR','local-admin',NOW(),'local-admin',NOW(),'0'),
(2096000100000000117,2096000100000000017,7,'{收益计提接口表.表内转表外金额}!=0','表内转表外非零','{收益计提接口表.表内转表外金额}','表内转表外','DR','local-admin',NOW(),'local-admin',NOW(),'0'),
(2096000100000000118,2096000100000000018,8,'{收益计提接口表.表内转表外金额}!=0','表内转表外非零','{收益计提接口表.表内转表外金额}','表内转表外','CR','local-admin',NOW(),'local-admin',NOW(),'0'),
(2096000100000000119,2096000100000000019,9,'{收益计提接口表.表外转表内金额}!=0','表外转表内非零','{收益计提接口表.表外转表内金额}','表外转表内','DR','local-admin',NOW(),'local-admin',NOW(),'0'),
(2096000100000000120,2096000100000000020,10,'{收益计提接口表.表外转表内金额}!=0','表外转表内非零','{收益计提接口表.表外转表内金额}','表外转表内','CR','local-admin',NOW(),'local-admin',NOW(),'0'),
(2096000100000000121,2096000100000000021,11,'{收益计提接口表.表外转表内金额}!=0','表外转表内非零','{收益计提接口表.表外转表内金额}','表外转表内','DR','local-admin',NOW(),'local-admin',NOW(),'0'),
(2096000100000000122,2096000100000000022,12,'{收益计提接口表.表外转表内金额}!=0','表外转表内非零','{收益计提接口表.表外转表内金额}','表外转表内','CR','local-admin',NOW(),'local-admin',NOW(),'0');
