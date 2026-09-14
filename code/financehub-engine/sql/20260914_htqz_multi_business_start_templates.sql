-- HTQZ multi-system lease-start voucher configuration.
-- Source: accounting rule workbook V1.0 (2025-11-04), sheet "凭证规则模板".
-- Design rule: every voucher owns at most one entry for a fund_type.  Alternative
-- credit subjects are selected by mutually exclusive interface conditions.

START TRANSACTION;

SET @htqz_scene_id := (SELECT id FROM eg_scene WHERE scene_code = 'HTQZ' AND del_flag = '0' LIMIT 1);

-- Interface discriminator and canonical amount fields.
DROP TEMPORARY TABLE IF EXISTS tmp_htqz_fields;
CREATE TEMPORARY TABLE tmp_htqz_fields (
    sort_no INT NOT NULL,
    field_name VARCHAR(100) COLLATE utf8mb4_0900_ai_ci NOT NULL,
    field_code VARCHAR(100) COLLATE utf8mb4_0900_ai_ci NOT NULL,
    data_type VARCHAR(50) COLLATE utf8mb4_0900_ai_ci NOT NULL
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
INSERT INTO tmp_htqz_fields VALUES
 (100, '资产类别', 'asset_category', 'String'),
 (101, '起租事件变体', 'start_event_variant', 'String'),
 (102, '本金结转方式', 'principal_offset_type', 'String'),
 (103, '核算配置变体', 'accounting_variant', 'String'),
 (110, '起租不含税本金', 'lease_principal_net', 'Number'),
 (111, '起租不含税利息', 'lease_interest_net', 'Number'),
 (112, '起租不含税留购价', 'residual_value_net', 'Number'),
 (113, '起租利息税额', 'lease_interest_vat', 'Number'),
 (114, '起租留购价税额', 'residual_value_vat', 'Number'),
 (120, '已收手续费未摊销不含税金额', 'received_fee_unamortized_net', 'Number'),
 (121, '未收取手续费不含税金额', 'unreceived_fee_net', 'Number'),
 (122, '未收手续费未摊销不含税金额', 'unreceived_fee_unamortized_net', 'Number'),
 (123, '未收取手续费税额', 'unreceived_fee_vat', 'Number'),
 (124, '未收取手续费未摊销税额', 'unreceived_fee_unamortized_vat', 'Number'),
 (125, '未收取手续费含税金额', 'unreceived_fee_gross', 'Number'),
 (126, '未摊销手续费不含税合计', 'fee_unamortized_net_total', 'Number'),
 (127, '应收手续费不含税金额', 'service_fee_net', 'Number'),
 (128, '应收手续费税额', 'service_fee_vat', 'Number'),
 (130, '经营租赁资产成本不含税金额', 'operating_asset_cost_net', 'Number'),
 (131, '客户融资不含税总额', 'customer_finance_net', 'Number');

SET @field_id := 2110000000000030000;
INSERT INTO eg_scene_fields
 (id, scene_id, scene_code, scene_name, field_name, field_code, data_type,
  create_by, create_time, update_by, update_time, del_flag, parent_id, required_flag, sort_no)
SELECT (@field_id := @field_id + 1), @htqz_scene_id, 'HTQZ', '起租', f.field_name, f.field_code,
       f.data_type, 'htqz-multi-business-v1', NOW(), 'htqz-multi-business-v1', NOW(), '0', NULL, '0', f.sort_no
FROM tmp_htqz_fields f
WHERE NOT EXISTS (
    SELECT 1 FROM eg_scene_fields x
    WHERE x.scene_code='HTQZ' AND x.field_code=f.field_code AND x.del_flag='0'
);

-- Map every workbook lease-start source event to the real HTQZ scene.
DELETE FROM eg_field_mapping
WHERE update_by='htqz-multi-business-v1' OR create_by='htqz-multi-business-v1';
INSERT INTO eg_field_mapping
 (id,system_code,field_code,field_name,source_value,target_value,default_value,
  create_by,create_time,update_by,update_time,del_flag,target_field_code)
VALUES
 (2110000000000040001,'FINANCE_LEASE','eventCode','融资租赁起租事件',
  'Z005,Z0Z05,Z008,Z009,Z0Z08,Z0Z09,ZZ005,ZZ008,H002,H005,H006,HZ002,HZ006',
  'HTQZ',NULL,'htqz-multi-business-v1',NOW(),'htqz-multi-business-v1',NOW(),'0','sceneCode'),
 (2110000000000040002,'HOUSEHOLD_PV','eventCode','户用光伏起租事件',
  'H008,H072,H063,HY008,HY072,HY063,J003,JY003',
  'HTQZ',NULL,'htqz-multi-business-v1',NOW(),'htqz-multi-business-v1',NOW(),'0','sceneCode'),
 (2110000000000040003,'OPERATING_LEASE','eventCode','经营租赁起租事件',
  'J003,JY003','HTQZ',NULL,'htqz-multi-business-v1',NOW(),'htqz-multi-business-v1',NOW(),'0','sceneCode');

-- Replace only configurations owned by this migration.  The existing retail
-- passenger-car HTQZ template remains untouched.
DELETE c FROM eg_scene_voucher_condition c
JOIN eg_scene_voucher_entry e ON e.id=c.scene_voucher_entry_id
JOIN eg_scene_voucher v ON v.id=e.scene_voucher_id
WHERE v.create_by='htqz-multi-business-v1';
DELETE e FROM eg_scene_voucher_entry e
JOIN eg_scene_voucher v ON v.id=e.scene_voucher_id
WHERE v.create_by='htqz-multi-business-v1';
DELETE FROM eg_scene_voucher WHERE create_by='htqz-multi-business-v1';

INSERT INTO eg_scene_voucher
 (id,scene_id,source,voucher_type,company,business_date,voucher_date,currency,dept_name,
  voucher_summary,create_by,create_time,update_by,update_time,del_flag,script_condition,
  scene_voucher_name,sub_scene_type)
VALUES
 (2110000000000000101,@htqz_scene_id,'{起租接口表.系统来源}','05','{起租接口表.核算主体代码}',
  '{起租接口表.业务发生日期}','{起租接口表.业务发生日期}','{起租接口表.币种}',NULL,
  "'确认承租人长期应收款及未实现融资收益-'+{起租接口表.融资租赁合同号}",
  'htqz-multi-business-v1',NOW(),'htqz-multi-business-v1',NOW(),'0',
  "({起租接口表.核算配置变体}=='ZLYW_DIRECT_REAL_ESTATE'||{起租接口表.核算配置变体}=='ZLYW_DIRECT_MOVABLE')",
  '融资租赁业务系统-直租起租凭证','FINANCE_LEASE_START'),
 (2110000000000000102,@htqz_scene_id,'{起租接口表.系统来源}','05','{起租接口表.核算主体代码}',
  '{起租接口表.业务发生日期}','{起租接口表.业务发生日期}','{起租接口表.币种}',NULL,
  "'确认承租人长期应收款及未实现融资收益-'+{起租接口表.融资租赁合同号}",
  'htqz-multi-business-v1',NOW(),'htqz-multi-business-v1',NOW(),'0',
  "({起租接口表.核算配置变体}=='ZLYW_LEASEBACK_REAL_ESTATE'||{起租接口表.核算配置变体}=='ZLYW_LEASEBACK_MOVABLE')",
  '融资租赁业务系统-回租起租凭证','FINANCE_LEASE_START'),
 (2110000000000000103,@htqz_scene_id,'{起租接口表.系统来源}','05','{起租接口表.核算主体代码}',
  '{起租接口表.业务发生日期}','{起租接口表.业务发生日期}','{起租接口表.币种}',NULL,
  "'确认合作方融资租赁款及未实现融资收益-'+{起租接口表.融资租赁合同号}",
  'htqz-multi-business-v1',NOW(),'htqz-multi-business-v1',NOW(),'0',
  "{起租接口表.核算配置变体}=='ZLYW_HOUSEHOLD_PV'&&{起租接口表.起租事件变体}=='HY008'",
  '户用光伏-项目投放后起租凭证','FINANCE_LEASE_START'),
 (2110000000000000104,@htqz_scene_id,'{起租接口表.系统来源}','05','{起租接口表.核算主体代码}',
  '{起租接口表.业务发生日期}','{起租接口表.业务发生日期}','{起租接口表.币种}',NULL,
  "'确认项目公司融资租赁款及未实现融资收益-'+{起租接口表.融资租赁合同号}",
  'htqz-multi-business-v1',NOW(),'htqz-multi-business-v1',NOW(),'0',
  "{起租接口表.核算配置变体}=='ZLYW_HOUSEHOLD_PV'&&{起租接口表.起租事件变体}=='HY072'",
  '户用光伏-项目公司投放后起租凭证','FINANCE_LEASE_START'),
 (2110000000000000105,@htqz_scene_id,'{起租接口表.系统来源}','05','{起租接口表.核算主体代码}',
  '{起租接口表.业务发生日期}','{起租接口表.业务发生日期}','{起租接口表.币种}',NULL,
  "'确认大商渠道融资租赁款及未实现融资收益-'+{起租接口表.融资租赁合同号}",
  'htqz-multi-business-v1',NOW(),'htqz-multi-business-v1',NOW(),'0',
  "{起租接口表.核算配置变体}=='ZLYW_HOUSEHOLD_PV'&&{起租接口表.起租事件变体}=='HY063'",
  '户用光伏-大商渠道投放后起租凭证','FINANCE_LEASE_START'),
 (2110000000000000106,@htqz_scene_id,'{起租接口表.系统来源}','05','{起租接口表.核算主体代码}',
  '{起租接口表.业务发生日期}','{起租接口表.业务发生日期}','{起租接口表.币种}',NULL,
  "'出租经营租赁资产-'+{起租接口表.融资租赁合同号}",
  'htqz-multi-business-v1',NOW(),'htqz-multi-business-v1',NOW(),'0',
  "{起租接口表.核算配置变体}=='JYZL_HOUSEHOLD_PV'&&{起租接口表.起租事件变体}=='JY003'",
  '户用光伏-经营租赁资产出租凭证','OPERATING_LEASE_START');

DROP TEMPORARY TABLE IF EXISTS tmp_htqz_entry;
CREATE TEMPORARY TABLE tmp_htqz_entry (
 voucher_id BIGINT NOT NULL,
 serial_no INT NOT NULL,
 fund_type VARCHAR(100) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 summary_text VARCHAR(300) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 condition_text VARCHAR(1000) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 amount_text VARCHAR(1000) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 amount_desc VARCHAR(300) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 dc CHAR(2) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 PRIMARY KEY(voucher_id,fund_type)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Canonical financing entries.  One fund_type appears once per voucher.
INSERT INTO tmp_htqz_entry VALUES
 (2110000000000000101,1,'lease_principal_receivable','确认应收租赁本金',"{起租接口表.起租不含税本金}!=0",'{起租接口表.起租不含税本金}','起租不含税本金','DR'),
 (2110000000000000101,2,'lease_interest_receivable','确认应收租赁利息',"{起租接口表.起租不含税利息}!=0",'{起租接口表.起租不含税利息}','起租不含税利息','DR'),
 (2110000000000000101,3,'residual_value_receivable','确认应收留购价',"{起租接口表.起租不含税留购价}!=0",'{起租接口表.起租不含税留购价}','起租不含税留购价','DR'),
 (2110000000000000101,4,'lease_interest_vat_receivable','确认应收租赁利息税额',"{起租接口表.起租利息税额}!=0",'{起租接口表.起租利息税额}','起租利息税额','DR'),
 (2110000000000000101,5,'residual_value_vat_receivable','确认应收留购价税额',"{起租接口表.起租留购价税额}!=0",'{起租接口表.起租留购价税额}','起租留购价税额','DR'),
 (2110000000000000101,6,'lease_asset_cost','结转融资租赁资产',"{起租接口表.本金结转方式}=='LEASE_ASSET'&&{起租接口表.起租不含税本金}!=0",'{起租接口表.起租不含税本金}','融资租赁资产结转金额','CR'),
 (2110000000000000101,7,'prepaid_lease_asset','结转预付融资租赁设备款',"{起租接口表.本金结转方式}=='PREPAID_ASSET'&&{起租接口表.起租不含税本金}!=0",'{起租接口表.起租不含税本金}','预付设备款结转金额','CR'),
 (2110000000000000101,8,'unearned_lease_interest','确认未实现融资收益-利息',"{起租接口表.起租不含税利息}!=0",'{起租接口表.起租不含税利息}','起租不含税利息','CR'),
 (2110000000000000101,9,'unearned_residual_value','确认未实现融资收益-留购价',"{起租接口表.起租不含税留购价}!=0",'{起租接口表.起租不含税留购价}','起租不含税留购价','CR'),
 (2110000000000000101,10,'unearned_lease_interest_vat','确认未实现融资收益-利息税额',"{起租接口表.起租利息税额}!=0",'{起租接口表.起租利息税额}','起租利息税额','CR'),
 (2110000000000000101,11,'unearned_residual_value_vat','确认未实现融资收益-留购价税额',"{起租接口表.起租留购价税额}!=0",'{起租接口表.起租留购价税额}','起租留购价税额','CR'),
 (2110000000000000101,12,'prepaid_received_other_income','结转已收未摊销手续费',"{起租接口表.已收手续费未摊销不含税金额}!=0",'{起租接口表.已收手续费未摊销不含税金额}','已收手续费未摊销不含税金额','DR'),
 (2110000000000000101,13,'receivable_other_income','确认应收其他收益',"{起租接口表.未收取手续费不含税金额}!=0",'{起租接口表.未收取手续费不含税金额}','未收取手续费不含税金额','DR'),
 (2110000000000000101,14,'prepaid_receivable_other_income','结转未收未摊销手续费',"{起租接口表.未收手续费未摊销不含税金额}!=0",'{起租接口表.未收手续费未摊销不含税金额}','未收手续费未摊销不含税金额','DR'),
 (2110000000000000101,15,'receivable_other_income_vat','确认应收其他收益税额',"{起租接口表.未收取手续费税额}!=0",'{起租接口表.未收取手续费税额}','未收取手续费税额','DR'),
 (2110000000000000101,16,'prepaid_receivable_other_income_vat','结转未收未摊销手续费税额',"{起租接口表.未收取手续费未摊销税额}!=0",'{起租接口表.未收取手续费未摊销税额}','未收取手续费未摊销税额','DR'),
 (2110000000000000101,17,'other_receivable_other_income','冲销应收其他收益',"{起租接口表.未收取手续费含税金额}!=0",'{起租接口表.未收取手续费含税金额}','未收取手续费含税金额','CR'),
 (2110000000000000101,18,'unearned_other_income','确认未实现其他收益',"{起租接口表.未摊销手续费不含税合计}!=0",'{起租接口表.未摊销手续费不含税合计}','未摊销手续费不含税合计','CR'),
 (2110000000000000101,19,'unearned_other_income_vat','确认未实现其他收益税额',"{起租接口表.未收取手续费未摊销税额}!=0",'{起租接口表.未收取手续费未摊销税额}','未收取手续费未摊销税额','CR');

-- Leaseback is the same canonical core plus the four service-fee subjects.
DROP TEMPORARY TABLE IF EXISTS tmp_htqz_entry_copy;
CREATE TEMPORARY TABLE tmp_htqz_entry_copy LIKE tmp_htqz_entry;
INSERT INTO tmp_htqz_entry_copy SELECT * FROM tmp_htqz_entry;
INSERT INTO tmp_htqz_entry
SELECT 2110000000000000102,serial_no,fund_type,summary_text,
       CASE WHEN fund_type='lease_asset_cost'
            THEN "{起租接口表.起租不含税本金}!=0"
            ELSE condition_text END,
       amount_text,amount_desc,dc
FROM tmp_htqz_entry_copy
WHERE voucher_id=2110000000000000101 AND serial_no IN (1,2,3,4,5,6,8,9,10,11);
INSERT INTO tmp_htqz_entry VALUES
 (2110000000000000102,12,'receivable_other_income','确认应收其他收益',"{起租接口表.应收手续费不含税金额}!=0",'{起租接口表.应收手续费不含税金额}','应收手续费不含税金额','DR'),
 (2110000000000000102,13,'receivable_other_income_vat','确认应收其他收益税额',"{起租接口表.应收手续费税额}!=0",'{起租接口表.应收手续费税额}','应收手续费税额','DR'),
 (2110000000000000102,14,'unearned_other_income','确认未实现其他收益',"{起租接口表.应收手续费不含税金额}!=0",'{起租接口表.应收手续费不含税金额}','应收手续费不含税金额','CR'),
 (2110000000000000102,15,'unearned_other_income_vat','确认未实现其他收益税额',"{起租接口表.应收手续费税额}!=0",'{起租接口表.应收手续费税额}','应收手续费税额','CR');

-- Household-PV financing: three events share the same amount types; only the
-- principal credit subject differs and is selected by principal_offset_type.
TRUNCATE TABLE tmp_htqz_entry_copy;
INSERT INTO tmp_htqz_entry_copy SELECT * FROM tmp_htqz_entry;
INSERT INTO tmp_htqz_entry
SELECT v.id,e.serial_no,e.fund_type,e.summary_text,
       CASE e.fund_type WHEN 'lease_asset_cost' THEN "{起租接口表.本金结转方式}=='LEASE_ASSET'&&{起租接口表.起租不含税本金}!=0" ELSE e.condition_text END,
       e.amount_text,e.amount_desc,e.dc
FROM eg_scene_voucher v
JOIN tmp_htqz_entry_copy e ON e.voucher_id=2110000000000000102
WHERE v.id IN (2110000000000000103,2110000000000000104,2110000000000000105)
  AND e.fund_type IN ('lease_principal_receivable','lease_interest_receivable','residual_value_receivable',
                      'lease_interest_vat_receivable','residual_value_vat_receivable','lease_asset_cost',
                      'unearned_lease_interest','unearned_residual_value','unearned_lease_interest_vat','unearned_residual_value_vat');
INSERT INTO tmp_htqz_entry VALUES
 (2110000000000000103,20,'prepaid_pv_project_company','结转项目公司预付设备款',"{起租接口表.本金结转方式}=='PROJECT_COMPANY_PREPAID'&&{起租接口表.起租不含税本金}!=0",'{起租接口表.起租不含税本金}','项目公司预付设备款结转金额','CR'),
 (2110000000000000103,21,'prepaid_pv_dealer','结转大商预付设备款',"{起租接口表.本金结转方式}=='DEALER_PREPAID'&&{起租接口表.起租不含税本金}!=0",'{起租接口表.起租不含税本金}','大商预付设备款结转金额','CR'),
 (2110000000000000104,20,'prepaid_pv_project_company','结转项目公司预付设备款',"{起租接口表.本金结转方式}=='PROJECT_COMPANY_PREPAID'&&{起租接口表.起租不含税本金}!=0",'{起租接口表.起租不含税本金}','项目公司预付设备款结转金额','CR'),
 (2110000000000000104,21,'prepaid_pv_dealer','结转大商预付设备款',"{起租接口表.本金结转方式}=='DEALER_PREPAID'&&{起租接口表.起租不含税本金}!=0",'{起租接口表.起租不含税本金}','大商预付设备款结转金额','CR'),
 (2110000000000000105,20,'prepaid_pv_project_company','结转项目公司预付设备款',"{起租接口表.本金结转方式}=='PROJECT_COMPANY_PREPAID'&&{起租接口表.起租不含税本金}!=0",'{起租接口表.起租不含税本金}','项目公司预付设备款结转金额','CR'),
 (2110000000000000105,21,'prepaid_pv_dealer','结转大商预付设备款',"{起租接口表.本金结转方式}=='DEALER_PREPAID'&&{起租接口表.起租不含税本金}!=0",'{起租接口表.起租不含税本金}','大商预付设备款结转金额','CR');

INSERT INTO tmp_htqz_entry VALUES
 (2110000000000000106,1,'operating_asset_leased','转入已出租经营租赁资产',"{起租接口表.客户融资不含税总额}!=0",'{起租接口表.客户融资不含税总额}','客户融资不含税总额','DR'),
 (2110000000000000106,2,'operating_asset_unleased','转出未出租经营租赁资产',"{起租接口表.客户融资不含税总额}!=0",'{起租接口表.客户融资不含税总额}','客户融资不含税总额','CR');

SET @entry_id := 2110000000000010000;
INSERT INTO eg_scene_voucher_entry
 (id,scene_voucher_id,fund_type,relate_bank_flag,bank_account,cash_attribute,voucher_summary,
  create_by,create_time,update_by,update_time,del_flag,cash_attribute_flag,assist_flags)
SELECT (@entry_id := @entry_id + 1),voucher_id,fund_type,'0',NULL,NULL,
       CONCAT("'",summary_text,"'"),'htqz-multi-business-v1',NOW(),
       'htqz-multi-business-v1',NOW(),'0','0','0,1'
FROM tmp_htqz_entry ORDER BY voucher_id,serial_no;

SET @condition_id := 2110000000000020000;
INSERT INTO eg_scene_voucher_condition
 (id,scene_voucher_entry_id,serial,script_condition,dondition_description,script_amount,
  amount_description,debit_credit_type,create_by,create_time,update_by,update_time,del_flag)
SELECT (@condition_id := @condition_id + 1),e.id,t.serial_no,t.condition_text,t.condition_text,
       t.amount_text,t.amount_desc,t.dc,'htqz-multi-business-v1',NOW(),
       'htqz-multi-business-v1',NOW(),'0'
FROM tmp_htqz_entry t
JOIN eg_scene_voucher_entry e ON e.scene_voucher_id=t.voucher_id
 AND e.fund_type=t.fund_type AND e.del_flag='0'
ORDER BY t.voucher_id,t.serial_no;

-- Account variants keep businessCode (and contract balances) unchanged while
-- selecting the correct product/method subject during voucher generation.
DELETE FROM eg_account WHERE create_by='htqz-multi-business-v1';
DROP TEMPORARY TABLE IF EXISTS tmp_htqz_account;
CREATE TEMPORARY TABLE tmp_htqz_account (
 variant VARCHAR(100) COLLATE utf8mb4_0900_ai_ci, fund_type VARCHAR(100) COLLATE utf8mb4_0900_ai_ci,
 account_code VARCHAR(100) COLLATE utf8mb4_0900_ai_ci,
 account_name VARCHAR(200) COLLATE utf8mb4_0900_ai_ci,
 balance_dc CHAR(2) COLLATE utf8mb4_0900_ai_ci, PRIMARY KEY(variant,fund_type)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO tmp_htqz_account VALUES
 ('ZLYW_DIRECT_REAL_ESTATE','lease_principal_receivable','15310102','应收融资租赁款_应收承租人本金（不动产项目）_直租','DR'),
 ('ZLYW_DIRECT_REAL_ESTATE','lease_interest_receivable','15310202','应收融资租赁款_应收承租人利息（不动产项目）_直租','DR'),
 ('ZLYW_DIRECT_REAL_ESTATE','residual_value_receivable','15310702','应收融资租赁款_留购价（不动产项目）_直租','DR'),
 ('ZLYW_DIRECT_REAL_ESTATE','lease_interest_vat_receivable','15311502','应收融资租赁款_应收承租人利息部分增值税（不动产项目）_直租','DR'),
 ('ZLYW_DIRECT_REAL_ESTATE','residual_value_vat_receivable','15311602','应收融资租赁款_应收承租人留购价部分增值税（不动产项目）_直租','DR'),
 ('ZLYW_DIRECT_REAL_ESTATE','lease_asset_cost','15410102','融资租赁资产_不动产项目_直租','DR'),
 ('ZLYW_DIRECT_REAL_ESTATE','prepaid_lease_asset','112201','预付账款_预付融资租赁项目设备购置款（不动产项目）','DR'),
 ('ZLYW_DIRECT_REAL_ESTATE','unearned_lease_interest','15320102','未实现融资收益_利息（不动产项目）_直租','CR'),
 ('ZLYW_DIRECT_REAL_ESTATE','unearned_residual_value','15320302','未实现融资收益_留购价（不动产项目）_直租','CR'),
 ('ZLYW_DIRECT_REAL_ESTATE','unearned_lease_interest_vat','15320902','未实现融资收益_利息部分增值税（不动产项目）_直租','CR'),
 ('ZLYW_DIRECT_REAL_ESTATE','unearned_residual_value_vat','15321002','未实现融资收益_留购价部分增值税（不动产项目）_直租','CR'),
 ('ZLYW_DIRECT_REAL_ESTATE','prepaid_received_other_income','112207','预付账款_预收融资租赁项目其他收益（不动产项目）','DR'),
 ('ZLYW_DIRECT_REAL_ESTATE','receivable_other_income','15311902','应收融资租赁款_应收承租人其他收益（不动产项目）_直租','DR'),
 ('ZLYW_DIRECT_REAL_ESTATE','prepaid_receivable_other_income','112209','预付账款_应收融资租赁项目其他收益（不动产项目）','DR'),
 ('ZLYW_DIRECT_REAL_ESTATE','receivable_other_income_vat','15312102','应收融资租赁款_应收承租人其他收益增值税（不动产项目）_直租','DR'),
 ('ZLYW_DIRECT_REAL_ESTATE','prepaid_receivable_other_income_vat','112213','预付账款_应收融资租赁项目其他收益增值税（不动产项目）','DR'),
 ('ZLYW_DIRECT_REAL_ESTATE','other_receivable_other_income','123110','其他应收款_应收融资租赁项目其他收益（不动产项目）','DR'),
 ('ZLYW_DIRECT_REAL_ESTATE','unearned_other_income','15321202','未实现融资收益_其他收益（不动产项目）_直租','CR'),
 ('ZLYW_DIRECT_REAL_ESTATE','unearned_other_income_vat','15321402','未实现融资收益_其他收益增值税（不动产项目）_直租','CR');

DROP TEMPORARY TABLE IF EXISTS tmp_htqz_account_copy;
CREATE TEMPORARY TABLE tmp_htqz_account_copy LIKE tmp_htqz_account;
INSERT INTO tmp_htqz_account_copy SELECT * FROM tmp_htqz_account;
INSERT INTO tmp_htqz_account
SELECT 'ZLYW_DIRECT_MOVABLE',fund_type,
 CASE account_code
  WHEN '15310102' THEN '15310302' WHEN '15310202' THEN '15310402' WHEN '15310702' THEN '15310802'
  WHEN '15311502' THEN '15311102' WHEN '15311602' THEN '15311202' WHEN '15410102' THEN '15410202'
  WHEN '112201' THEN '112202' WHEN '15320102' THEN '15320202' WHEN '15320302' THEN '15320402'
  WHEN '15320902' THEN '15320502' WHEN '15321002' THEN '15320602' WHEN '112207' THEN '112208'
  WHEN '15311902' THEN '15312002' WHEN '112209' THEN '112210' WHEN '15312102' THEN '15312202'
  WHEN '112213' THEN '112214' WHEN '123110' THEN '123111' WHEN '15321202' THEN '15321302'
  WHEN '15321402' THEN '15321502' END,
 REPLACE(REPLACE(account_name,'不动产项目','动产项目'),'15310102','15310302'),balance_dc
FROM tmp_htqz_account_copy WHERE variant='ZLYW_DIRECT_REAL_ESTATE';

INSERT INTO tmp_htqz_account VALUES
 ('ZLYW_LEASEBACK_REAL_ESTATE','lease_principal_receivable','15310101','应收融资租赁款_应收承租人本金（不动产项目）_回租','DR'),
 ('ZLYW_LEASEBACK_REAL_ESTATE','lease_interest_receivable','15310201','应收融资租赁款_应收承租人利息（不动产项目）_回租','DR'),
 ('ZLYW_LEASEBACK_REAL_ESTATE','residual_value_receivable','15310701','应收融资租赁款_留购价（不动产项目）_回租','DR'),
 ('ZLYW_LEASEBACK_REAL_ESTATE','lease_interest_vat_receivable','15311501','应收融资租赁款_应收承租人利息部分增值税（不动产项目）_回租','DR'),
 ('ZLYW_LEASEBACK_REAL_ESTATE','residual_value_vat_receivable','15311601','应收融资租赁款_应收承租人留购价部分增值税（不动产项目）_回租','DR'),
 ('ZLYW_LEASEBACK_REAL_ESTATE','lease_asset_cost','15410101','融资租赁资产_不动产项目_回租','DR'),
 ('ZLYW_LEASEBACK_REAL_ESTATE','unearned_lease_interest','15320101','未实现融资收益_利息（不动产项目）_回租','CR'),
 ('ZLYW_LEASEBACK_REAL_ESTATE','unearned_residual_value','15320301','未实现融资收益_留购价（不动产项目）_回租','CR'),
 ('ZLYW_LEASEBACK_REAL_ESTATE','unearned_lease_interest_vat','15320901','未实现融资收益_利息部分增值税（不动产项目）_回租','CR'),
 ('ZLYW_LEASEBACK_REAL_ESTATE','unearned_residual_value_vat','15321001','未实现融资收益_留购价部分增值税（不动产项目）_回租','CR'),
 ('ZLYW_LEASEBACK_REAL_ESTATE','receivable_other_income','15311901','应收融资租赁款_应收承租人其他收益（不动产项目）_回租','DR'),
 ('ZLYW_LEASEBACK_REAL_ESTATE','receivable_other_income_vat','15312101','应收融资租赁款_应收承租人其他收益增值税（不动产项目）_回租','DR'),
 ('ZLYW_LEASEBACK_REAL_ESTATE','unearned_other_income','15321201','未实现融资收益_其他收益（不动产项目）_回租','CR'),
 ('ZLYW_LEASEBACK_REAL_ESTATE','unearned_other_income_vat','15321401','未实现融资收益_其他收益增值税（不动产项目）_回租','CR');

TRUNCATE TABLE tmp_htqz_account_copy;
INSERT INTO tmp_htqz_account_copy SELECT * FROM tmp_htqz_account;
INSERT INTO tmp_htqz_account
SELECT 'ZLYW_LEASEBACK_MOVABLE',fund_type,
 CASE account_code
  WHEN '15310101' THEN '15310301' WHEN '15310201' THEN '15310401' WHEN '15310701' THEN '15310801'
  WHEN '15311501' THEN '15311101' WHEN '15311601' THEN '15311201' WHEN '15410101' THEN '15410201'
  WHEN '15320101' THEN '15320201' WHEN '15320301' THEN '15320401' WHEN '15320901' THEN '15320501'
  WHEN '15321001' THEN '15320601' WHEN '15311901' THEN '15312001' WHEN '15312101' THEN '15312201'
  WHEN '15321201' THEN '15321301' WHEN '15321401' THEN '15321501' END,
 REPLACE(account_name,'不动产项目','动产项目'),balance_dc
FROM tmp_htqz_account_copy WHERE variant='ZLYW_LEASEBACK_REAL_ESTATE';

TRUNCATE TABLE tmp_htqz_account_copy;
INSERT INTO tmp_htqz_account_copy SELECT * FROM tmp_htqz_account;
INSERT INTO tmp_htqz_account
SELECT 'ZLYW_HOUSEHOLD_PV',fund_type,account_code,account_name,balance_dc
FROM tmp_htqz_account_copy WHERE variant='ZLYW_DIRECT_MOVABLE'
 AND fund_type IN ('lease_principal_receivable','lease_interest_receivable','residual_value_receivable',
                   'lease_interest_vat_receivable','residual_value_vat_receivable','lease_asset_cost',
                   'unearned_lease_interest','unearned_residual_value','unearned_lease_interest_vat','unearned_residual_value_vat');
INSERT INTO tmp_htqz_account VALUES
 ('ZLYW_HOUSEHOLD_PV','prepaid_pv_project_company','112223','预付账款_预付户用光伏项目公司购置款','DR'),
 ('ZLYW_HOUSEHOLD_PV','prepaid_pv_dealer','112222','预付账款_预付户用光伏大商设备购置款','DR'),
 ('JYZL_HOUSEHOLD_PV','operating_asset_leased','16110202','经营租赁资产_动产项目_已出租资产','DR'),
 ('JYZL_HOUSEHOLD_PV','operating_asset_unleased','16110201','经营租赁资产_动产项目_未出租资产','DR');

SET @account_id := 2110000000000050000;
INSERT INTO eg_account
 (id,business_code,business_name,fund_type,account_code,account_name,account_category,
  create_by,create_time,update_by,update_time,del_flag,debit_credit_type,
  client_flag,contract_flag,assist_flags,settlement_type,check_flag)
SELECT (@account_id := @account_id + 1),variant,'起租科目配置变体',fund_type,account_code,account_name,NULL,
       'htqz-multi-business-v1',NOW(),'htqz-multi-business-v1',NOW(),'0',balance_dc,
       '1','1','0,1',NULL,'1'
FROM tmp_htqz_account ORDER BY variant,fund_type;

-- New cash types are also balance columns. They are added to both dictionary
-- copies because the engine and admin services use separate schemas locally.
DROP TEMPORARY TABLE IF EXISTS tmp_htqz_cash_type;
CREATE TEMPORARY TABLE tmp_htqz_cash_type (
 dict_sort INT,dict_label VARCHAR(200) COLLATE utf8mb4_0900_ai_ci,
 dict_value VARCHAR(100) COLLATE utf8mb4_0900_ai_ci
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
INSERT INTO tmp_htqz_cash_type VALUES
 (301,'预付融资租赁设备款','prepaid_lease_asset'),
 (302,'预付已收其他收益','prepaid_received_other_income'),
 (303,'应收其他收益','receivable_other_income'),
 (304,'预付应收其他收益','prepaid_receivable_other_income'),
 (305,'应收其他收益税额','receivable_other_income_vat'),
 (306,'预付应收其他收益税额','prepaid_receivable_other_income_vat'),
 (307,'其他应收款-其他收益','other_receivable_other_income'),
 (308,'未实现其他收益','unearned_other_income'),
 (309,'未实现其他收益税额','unearned_other_income_vat'),
 (310,'预付户用光伏项目公司购置款','prepaid_pv_project_company'),
 (311,'预付户用光伏大商设备购置款','prepaid_pv_dealer'),
 (312,'已出租经营租赁资产','operating_asset_leased'),
 (313,'未出租经营租赁资产','operating_asset_unleased');

INSERT INTO financehub_lease.sys_dict_data
 (dict_sort,dict_label,dict_value,dict_type,is_default,status,create_by,create_time,update_by,update_time,remark)
SELECT t.dict_sort,t.dict_label,t.dict_value,'sys_cash_type','N','0','htqz-multi-business-v1',NOW(),
       'htqz-multi-business-v1',NOW(),'HTQZ multi-business lease-start'
FROM tmp_htqz_cash_type t WHERE NOT EXISTS (
 SELECT 1 FROM financehub_lease.sys_dict_data d WHERE d.dict_type='sys_cash_type' AND d.dict_value=t.dict_value
);
INSERT INTO financialdb4.sys_dict_data
 (dict_sort,dict_label,dict_value,dict_type,is_default,status,create_by,create_time,update_by,update_time,remark)
SELECT t.dict_sort,t.dict_label,t.dict_value,'sys_cash_type','N','0','htqz-multi-business-v1',NOW(),
       'htqz-multi-business-v1',NOW(),'HTQZ multi-business lease-start'
FROM tmp_htqz_cash_type t WHERE NOT EXISTS (
 SELECT 1 FROM financialdb4.sys_dict_data d WHERE d.dict_type='sys_cash_type' AND d.dict_value=t.dict_value
);

COMMIT;

-- Balance schema support for the new semantic amount types.
DROP PROCEDURE IF EXISTS add_htqz_balance_column;
DELIMITER $$
CREATE PROCEDURE add_htqz_balance_column(IN p_table VARCHAR(64), IN p_column VARCHAR(128))
BEGIN
 IF NOT EXISTS (
   SELECT 1 FROM information_schema.columns
   WHERE table_schema=DATABASE() AND table_name=p_table AND column_name=p_column
 ) THEN
   SET @ddl=CONCAT('ALTER TABLE `',p_table,'` ADD COLUMN `',p_column,'` DECIMAL(20,2) DEFAULT 0');
   PREPARE stmt FROM @ddl;
   EXECUTE stmt;
   DEALLOCATE PREPARE stmt;
 END IF;
END$$
DELIMITER ;

DROP TEMPORARY TABLE IF EXISTS tmp_htqz_balance_type;
CREATE TEMPORARY TABLE tmp_htqz_balance_type (
 fund_type VARCHAR(100) COLLATE utf8mb4_0900_ai_ci PRIMARY KEY
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
INSERT INTO tmp_htqz_balance_type SELECT dict_value FROM tmp_htqz_cash_type;

DROP PROCEDURE IF EXISTS add_all_htqz_balance_columns;
DELIMITER $$
CREATE PROCEDURE add_all_htqz_balance_columns()
BEGIN
 DECLARE done INT DEFAULT 0;
 DECLARE v_type VARCHAR(100);
 DECLARE cur CURSOR FOR SELECT fund_type FROM tmp_htqz_balance_type;
 DECLARE CONTINUE HANDLER FOR NOT FOUND SET done=1;
 OPEN cur;
 read_loop: LOOP
   FETCH cur INTO v_type;
   IF done=1 THEN LEAVE read_loop; END IF;
   CALL add_htqz_balance_column('eg_contract_balance',CONCAT(v_type,'_amount'));
   CALL add_htqz_balance_column('eg_contract_balance',CONCAT(v_type,'_balance'));
   CALL add_htqz_balance_column('eg_contract_balance_latest',CONCAT(v_type,'_amount'));
   CALL add_htqz_balance_column('eg_contract_balance_latest',CONCAT(v_type,'_balance'));
   CALL add_htqz_balance_column('eg_contract_balance_temp',CONCAT(v_type,'_amount'));
   CALL add_htqz_balance_column('eg_contract_balance_temp',CONCAT(v_type,'_balance'));
 END LOOP;
 CLOSE cur;
END$$
DELIMITER ;
CALL add_all_htqz_balance_columns();
DROP PROCEDURE add_all_htqz_balance_columns;
DROP PROCEDURE add_htqz_balance_column;

-- Verification: both result sets must be empty/zero.
SELECT v.id,v.scene_voucher_name,e.fund_type,COUNT(*) duplicate_count
FROM eg_scene_voucher v
JOIN eg_scene_voucher_entry e ON e.scene_voucher_id=v.id AND e.del_flag='0'
WHERE v.create_by='htqz-multi-business-v1' AND v.del_flag='0'
GROUP BY v.id,v.scene_voucher_name,e.fund_type HAVING COUNT(*)>1;
SELECT COUNT(*) missing_account_count
FROM eg_scene_voucher v
JOIN eg_scene_voucher_entry e ON e.scene_voucher_id=v.id AND e.del_flag='0'
LEFT JOIN eg_account a ON a.fund_type=e.fund_type AND a.del_flag='0'
 AND a.business_code IN ('ZLYW_DIRECT_REAL_ESTATE','ZLYW_DIRECT_MOVABLE',
                         'ZLYW_LEASEBACK_REAL_ESTATE','ZLYW_LEASEBACK_MOVABLE',
                         'ZLYW_HOUSEHOLD_PV','JYZL_HOUSEHOLD_PV')
WHERE v.create_by='htqz-multi-business-v1' AND v.del_flag='0' AND a.id IS NULL;
