-- HTQZ v2: expose raw business data only and let the accounting engine derive
-- term, XIRR, tax-exclusive amounts, VAT and routing variables.

START TRANSACTION;

-- The rule expressions contain single-quoted string literals. Keep the outer
-- SQL literals double-quoted even when the server default enables ANSI_QUOTES.
SET SESSION sql_mode = REPLACE(@@SESSION.sql_mode, 'ANSI_QUOTES', '');

SET @htqz_scene_id := (SELECT id FROM eg_scene WHERE scene_code='HTQZ' AND del_flag='0' LIMIT 1);
SET @htqz_voucher_id := 2110000000000000200;

-- Remove calculated/internal fields from interface configuration.
UPDATE eg_scene_fields
SET del_flag='1',update_by='htqz-derived-fields-v2',update_time=NOW()
WHERE scene_code='HTQZ' AND del_flag='0' AND field_code IN (
 'finance_term','term_unit','total_terms','irr','unearned_finance_income',
 'start_event_variant','principal_offset_type','accounting_variant',
 'lease_principal_net','lease_interest_net','residual_value_net','lease_interest_vat','residual_value_vat',
 'received_fee_unamortized_net','unreceived_fee_net','unreceived_fee_unamortized_net',
 'unreceived_fee_vat','unreceived_fee_unamortized_vat','unreceived_fee_gross',
 'fee_unamortized_net_total','service_fee_net','service_fee_vat',
 'operating_asset_cost_net','customer_finance_net'
);

-- Only original fee status and the applicable VAT rate are additional inputs.
-- All amounts here are tax-inclusive source values.
DROP TEMPORARY TABLE IF EXISTS tmp_htqz_raw_field;
CREATE TEMPORARY TABLE tmp_htqz_raw_field (
 sort_no INT NOT NULL,
 field_name VARCHAR(100) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 field_code VARCHAR(100) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 data_type VARCHAR(50) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 PRIMARY KEY(field_code)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
INSERT INTO tmp_htqz_raw_field VALUES
 (100,'资产类别','asset_category','String'),
 (101,'适用增值税率','vat_rate','Number'),
 (102,'手续费总额（含税）','service_fee','Number'),
 (103,'累计已收手续费（含税）','received_service_fee','Number'),
 (104,'累计已摊销手续费（含税）','amortized_service_fee','Number');

SET @field_id := 2110000000000032000;
INSERT INTO eg_scene_fields
 (id,scene_id,scene_code,scene_name,field_name,field_code,data_type,create_by,create_time,
  update_by,update_time,del_flag,parent_id,required_flag,sort_no)
SELECT (@field_id:=@field_id+1),@htqz_scene_id,'HTQZ','起租',f.field_name,f.field_code,f.data_type,
       'htqz-derived-fields-v2',NOW(),'htqz-derived-fields-v2',NOW(),'0',NULL,'0',f.sort_no
FROM tmp_htqz_raw_field f
WHERE NOT EXISTS (
 SELECT 1 FROM eg_scene_fields x
 WHERE x.scene_code='HTQZ' AND x.field_code=f.field_code AND x.del_flag='0'
);

-- Rebuild one rule branch per calculated amount. A second branch is retained
-- only when the same account uses a different raw business condition/amount.
DELETE c FROM eg_scene_voucher_condition c
JOIN eg_scene_voucher_entry e ON e.id=c.scene_voucher_entry_id
WHERE e.scene_voucher_id=@htqz_voucher_id;

DROP TEMPORARY TABLE IF EXISTS tmp_htqz_v2_rule;
CREATE TEMPORARY TABLE tmp_htqz_v2_rule (
 fund_type VARCHAR(100) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 serial_no INT NOT NULL,
 script_condition VARCHAR(1000) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 condition_description VARCHAR(1000) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 script_amount VARCHAR(1000) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 amount_description VARCHAR(1000) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 debit_credit_type VARCHAR(10) COLLATE utf8mb4_0900_ai_ci NOT NULL,
 PRIMARY KEY(fund_type,serial_no)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO tmp_htqz_v2_rule VALUES
 ('lease_asset_cost',1,"{起租接口表.本金结转方式}=='LEASE_ASSET'&&{起租接口表.起租不含税本金}!=0",'系统判定结转融资租赁资产','{起租接口表.起租不含税本金}','系统计算不含税本金','CR'),
 ('lease_asset_movable_leaseback',1,"{起租接口表.系统来源}=='RETAIL_FINANCE_LEASE'&&{起租接口表.实际投放金额}!=0",'零售乘用车回租资产结转','{起租接口表.实际投放金额}','实际投放金额','CR'),
 ('lease_interest_receivable',1,'{起租接口表.起租不含税利息}!=0','确认应收租赁利息','{起租接口表.起租不含税利息}','系统计算不含税利息','DR'),
 ('lease_interest_vat_receivable',1,'{起租接口表.起租利息税额}!=0','确认应收租赁利息税额','{起租接口表.起租利息税额}','系统计算利息税额','DR'),
 ('lease_principal_receivable',1,'{起租接口表.起租不含税本金}!=0','确认应收租赁本金','{起租接口表.起租不含税本金}','系统计算不含税本金','DR'),
 ('operating_asset_leased',1,"({起租接口表.系统来源}=='OPERATING_LEASE'||{起租接口表.租赁类型}=='OPERATING_LEASE'||{起租接口表.租赁类型}=='经营性租赁')&&{起租接口表.客户融资不含税总额}!=0",'经营租赁资产转出租','{起租接口表.客户融资不含税总额}','系统计算客户融资不含税总额','DR'),
 ('operating_asset_unleased',1,"({起租接口表.系统来源}=='OPERATING_LEASE'||{起租接口表.租赁类型}=='OPERATING_LEASE'||{起租接口表.租赁类型}=='经营性租赁')&&{起租接口表.客户融资不含税总额}!=0",'经营租赁资产转出租','{起租接口表.客户融资不含税总额}','系统计算客户融资不含税总额','CR'),
 ('other_receivable_other_income',1,"{起租接口表.系统来源}=='FINANCE_LEASE'&&({起租接口表.租赁方式}=='DIRECT_LEASE'||{起租接口表.租赁方式}=='直租')&&{起租接口表.未收取手续费含税金额}!=0",'直租未收手续费原值结转','{起租接口表.未收取手续费含税金额}','系统根据手续费原值计算','CR'),
 ('prepaid_lease_asset',1,"{起租接口表.本金结转方式}=='PREPAID_ASSET'&&{起租接口表.起租不含税本金}!=0",'结转预付融资租赁设备款','{起租接口表.起租不含税本金}','系统计算不含税本金','CR'),
 ('prepaid_pv_dealer',1,"{起租接口表.本金结转方式}=='DEALER_PREPAID'&&{起租接口表.起租不含税本金}!=0",'结转大商预付款','{起租接口表.起租不含税本金}','系统计算不含税本金','CR'),
 ('prepaid_pv_project_company',1,"{起租接口表.本金结转方式}=='PROJECT_COMPANY_PREPAID'&&{起租接口表.起租不含税本金}!=0",'结转项目公司预付款','{起租接口表.起租不含税本金}','系统计算不含税本金','CR'),
 ('prepaid_receivable_other_income',1,"{起租接口表.系统来源}=='FINANCE_LEASE'&&({起租接口表.租赁方式}=='DIRECT_LEASE'||{起租接口表.租赁方式}=='直租')&&{起租接口表.未收手续费未摊销不含税金额}!=0",'直租未收未摊销手续费','{起租接口表.未收手续费未摊销不含税金额}','系统计算未收未摊销手续费','DR'),
 ('prepaid_receivable_other_income_vat',1,"{起租接口表.系统来源}=='FINANCE_LEASE'&&({起租接口表.租赁方式}=='DIRECT_LEASE'||{起租接口表.租赁方式}=='直租')&&{起租接口表.未收取手续费未摊销税额}!=0",'直租未收未摊销手续费税额','{起租接口表.未收取手续费未摊销税额}','系统计算未收未摊销手续费税额','DR'),
 ('prepaid_received_other_income',1,"{起租接口表.系统来源}=='FINANCE_LEASE'&&({起租接口表.租赁方式}=='DIRECT_LEASE'||{起租接口表.租赁方式}=='直租')&&{起租接口表.已收手续费未摊销不含税金额}!=0",'直租已收未摊销手续费','{起租接口表.已收手续费未摊销不含税金额}','系统计算已收未摊销手续费','DR'),
 ('receivable_other_income',1,"{起租接口表.系统来源}=='FINANCE_LEASE'&&({起租接口表.租赁方式}=='DIRECT_LEASE'||{起租接口表.租赁方式}=='直租')&&{起租接口表.未收取手续费不含税金额}!=0",'直租确认未收手续费','{起租接口表.未收取手续费不含税金额}','系统计算未收手续费不含税金额','DR'),
 ('receivable_other_income',2,"{起租接口表.系统来源}=='FINANCE_LEASE'&&({起租接口表.租赁方式}=='SALE_AND_LEASEBACK'||{起租接口表.租赁方式}=='LEASEBACK'||{起租接口表.租赁方式}=='回租')&&{起租接口表.应收手续费不含税金额}!=0",'回租确认应收手续费','{起租接口表.应收手续费不含税金额}','系统计算应收手续费不含税金额','DR'),
 ('receivable_other_income_vat',1,"{起租接口表.系统来源}=='FINANCE_LEASE'&&({起租接口表.租赁方式}=='DIRECT_LEASE'||{起租接口表.租赁方式}=='直租')&&{起租接口表.未收取手续费税额}!=0",'直租确认未收手续费税额','{起租接口表.未收取手续费税额}','系统计算未收手续费税额','DR'),
 ('receivable_other_income_vat',2,"{起租接口表.系统来源}=='FINANCE_LEASE'&&({起租接口表.租赁方式}=='SALE_AND_LEASEBACK'||{起租接口表.租赁方式}=='LEASEBACK'||{起租接口表.租赁方式}=='回租')&&{起租接口表.应收手续费税额}!=0",'回租确认应收手续费税额','{起租接口表.应收手续费税额}','系统计算应收手续费税额','DR'),
 ('residual_value_receivable',1,'{起租接口表.起租不含税留购价}!=0','确认应收留购价','{起租接口表.起租不含税留购价}','系统计算不含税留购价','DR'),
 ('residual_value_vat_receivable',1,'{起租接口表.起租留购价税额}!=0','确认应收留购价税额','{起租接口表.起租留购价税额}','系统计算留购价税额','DR'),
 ('unearned_lease_interest',1,'{起租接口表.起租不含税利息}!=0','确认未实现融资收益-利息','{起租接口表.起租不含税利息}','系统计算不含税利息','CR'),
 ('unearned_lease_interest_vat',1,'{起租接口表.起租利息税额}!=0','确认未实现融资收益-利息税额','{起租接口表.起租利息税额}','系统计算利息税额','CR'),
 ('unearned_other_income',1,"{起租接口表.系统来源}=='FINANCE_LEASE'&&({起租接口表.租赁方式}=='DIRECT_LEASE'||{起租接口表.租赁方式}=='直租')&&{起租接口表.未摊销手续费不含税合计}!=0",'直租确认未摊销手续费收益','{起租接口表.未摊销手续费不含税合计}','系统计算未摊销手续费合计','CR'),
 ('unearned_other_income',2,"{起租接口表.系统来源}=='FINANCE_LEASE'&&({起租接口表.租赁方式}=='SALE_AND_LEASEBACK'||{起租接口表.租赁方式}=='LEASEBACK'||{起租接口表.租赁方式}=='回租')&&{起租接口表.应收手续费不含税金额}!=0",'回租确认未实现手续费收益','{起租接口表.应收手续费不含税金额}','系统计算应收手续费不含税金额','CR'),
 ('unearned_other_income_vat',1,"{起租接口表.系统来源}=='FINANCE_LEASE'&&({起租接口表.租赁方式}=='DIRECT_LEASE'||{起租接口表.租赁方式}=='直租')&&{起租接口表.未收取手续费未摊销税额}!=0",'直租确认未摊销手续费税额','{起租接口表.未收取手续费未摊销税额}','系统计算未摊销手续费税额','CR'),
 ('unearned_other_income_vat',2,"{起租接口表.系统来源}=='FINANCE_LEASE'&&({起租接口表.租赁方式}=='SALE_AND_LEASEBACK'||{起租接口表.租赁方式}=='LEASEBACK'||{起租接口表.租赁方式}=='回租')&&{起租接口表.应收手续费税额}!=0",'回租确认未实现手续费税额','{起租接口表.应收手续费税额}','系统计算应收手续费税额','CR'),
 ('unearned_residual_value',1,'{起租接口表.起租不含税留购价}!=0','确认未实现融资收益-留购价','{起租接口表.起租不含税留购价}','系统计算不含税留购价','CR'),
 ('unearned_residual_value_vat',1,'{起租接口表.起租留购价税额}!=0','确认未实现融资收益-留购价税额','{起租接口表.起租留购价税额}','系统计算留购价税额','CR');

-- Keep engine-derived variables visible in the rule editor, but outside the
-- business interface table so upstream systems are never expected to provide them.
UPDATE tmp_htqz_v2_rule SET script_condition=REPLACE(script_condition,'{起租接口表.本金结转方式}','{起租计算结果表.本金结转方式}'),script_amount=REPLACE(script_amount,'{起租接口表.本金结转方式}','{起租计算结果表.本金结转方式}');
UPDATE tmp_htqz_v2_rule SET script_condition=REPLACE(script_condition,'{起租接口表.起租','{起租计算结果表.起租'),script_amount=REPLACE(script_amount,'{起租接口表.起租','{起租计算结果表.起租');
UPDATE tmp_htqz_v2_rule SET script_condition=REPLACE(script_condition,'{起租接口表.已收手续费','{起租计算结果表.已收手续费'),script_amount=REPLACE(script_amount,'{起租接口表.已收手续费','{起租计算结果表.已收手续费');
UPDATE tmp_htqz_v2_rule SET script_condition=REPLACE(script_condition,'{起租接口表.未收','{起租计算结果表.未收'),script_amount=REPLACE(script_amount,'{起租接口表.未收','{起租计算结果表.未收');
UPDATE tmp_htqz_v2_rule SET script_condition=REPLACE(script_condition,'{起租接口表.未摊销手续费','{起租计算结果表.未摊销手续费'),script_amount=REPLACE(script_amount,'{起租接口表.未摊销手续费','{起租计算结果表.未摊销手续费');
UPDATE tmp_htqz_v2_rule SET script_condition=REPLACE(script_condition,'{起租接口表.应收手续费','{起租计算结果表.应收手续费'),script_amount=REPLACE(script_amount,'{起租接口表.应收手续费','{起租计算结果表.应收手续费');
UPDATE tmp_htqz_v2_rule SET script_condition=REPLACE(script_condition,'{起租接口表.经营租赁资产成本','{起租计算结果表.经营租赁资产成本'),script_amount=REPLACE(script_amount,'{起租接口表.经营租赁资产成本','{起租计算结果表.经营租赁资产成本');
UPDATE tmp_htqz_v2_rule SET script_condition=REPLACE(script_condition,'{起租接口表.客户融资','{起租计算结果表.客户融资'),script_amount=REPLACE(script_amount,'{起租接口表.客户融资','{起租计算结果表.客户融资');

SET @condition_id := 2110000000000023000;
INSERT INTO eg_scene_voucher_condition
 (id,scene_voucher_entry_id,serial,script_condition,dondition_description,script_amount,
  amount_description,debit_credit_type,create_by,create_time,update_by,update_time,del_flag)
SELECT (@condition_id:=@condition_id+1),e.id,r.serial_no,r.script_condition,r.condition_description,
       r.script_amount,r.amount_description,r.debit_credit_type,
       'htqz-derived-fields-v2',NOW(),'htqz-derived-fields-v2',NOW(),'0'
FROM tmp_htqz_v2_rule r
JOIN eg_scene_voucher_entry e ON e.scene_voucher_id=@htqz_voucher_id
 AND e.fund_type=r.fund_type AND e.del_flag='0'
ORDER BY e.id,r.serial_no;

COMMIT;

-- Acceptance checks.
SELECT COUNT(*) AS exposed_calculated_fields
FROM eg_scene_fields
WHERE scene_code='HTQZ' AND del_flag='0' AND field_code IN (
 'finance_term','term_unit','total_terms','irr','unearned_finance_income',
 'start_event_variant','principal_offset_type','accounting_variant',
 'lease_principal_net','lease_interest_net','residual_value_net','lease_interest_vat','residual_value_vat',
 'received_fee_unamortized_net','unreceived_fee_net','unreceived_fee_unamortized_net',
 'unreceived_fee_vat','unreceived_fee_unamortized_vat','unreceived_fee_gross',
 'fee_unamortized_net_total','service_fee_net','service_fee_vat',
 'operating_asset_cost_net','customer_finance_net'
);
SELECT COUNT(*) AS rules_using_external_variants
FROM eg_scene_voucher_condition c
JOIN eg_scene_voucher_entry e ON e.id=c.scene_voucher_entry_id
WHERE e.scene_voucher_id=@htqz_voucher_id AND c.del_flag='0'
 AND (c.script_condition LIKE '%核算配置变体%' OR c.script_condition LIKE '%起租事件变体%');
SELECT COUNT(*) AS condition_count
FROM eg_scene_voucher_condition c
JOIN eg_scene_voucher_entry e ON e.id=c.scene_voucher_entry_id
WHERE e.scene_voucher_id=@htqz_voucher_id AND c.del_flag='0';
