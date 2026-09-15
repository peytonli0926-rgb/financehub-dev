-- HTQZ: tax rates are accounting configuration, not interface parameters.
-- The migration is idempotent and may be rerun safely.

START TRANSACTION;

UPDATE eg_scene_fields
SET del_flag='1', update_by='htqz-tax-config-v3', update_time=NOW()
WHERE scene_code='HTQZ' AND field_code IN ('vat_rate','tax_rate') AND del_flag='0';

-- Principal is one amount type; asset_category carries the rate difference.

DROP TEMPORARY TABLE IF EXISTS tmp_htqz_tax_rate;
CREATE TEMPORARY TABLE tmp_htqz_tax_rate (
 business_code VARCHAR(50) NOT NULL,
 fund_type VARCHAR(100) NOT NULL,
 lease_type VARCHAR(50) NOT NULL,
 lease_method VARCHAR(50) NOT NULL,
 asset_category VARCHAR(30) NOT NULL DEFAULT '',
 tax_rate DECIMAL(20,6) NOT NULL,
 PRIMARY KEY (business_code,fund_type,lease_type,lease_method,asset_category)
) DEFAULT CHARSET=utf8mb4;

INSERT INTO tmp_htqz_tax_rate
 (business_code,fund_type,lease_type,lease_method,tax_rate) VALUES
 ('ZLYW','tax_general','FINANCE_LEASE','DIRECT_LEASE',13.000000),
 ('ZLYW','tax_general','FINANCE_LEASE','SALE_AND_LEASEBACK',6.000000),
 ('ZLYW','lease_interest_receivable','FINANCE_LEASE','DIRECT_LEASE',13.000000),
 ('ZLYW','lease_interest_receivable','FINANCE_LEASE','SALE_AND_LEASEBACK',6.000000),
 ('ZLYW','residual_value_receivable','FINANCE_LEASE','DIRECT_LEASE',13.000000),
 ('ZLYW','residual_value_receivable','FINANCE_LEASE','SALE_AND_LEASEBACK',6.000000),
 ('ZLYW','receivable_service','FINANCE_LEASE','DIRECT_LEASE',6.000000),
 ('ZLYW','receivable_service','FINANCE_LEASE','SALE_AND_LEASEBACK',6.000000),
 ('ZLYW','lease_rent_receivable','FINANCE_LEASE','DIRECT_LEASE',13.000000),
 ('ZLYW','lease_rent_receivable','FINANCE_LEASE','SALE_AND_LEASEBACK',6.000000),
 ('CYC_RETAIL_LEASEBACK','tax_general','FINANCE_LEASE','SALE_AND_LEASEBACK',6.000000),
 ('CYC_RETAIL_LEASEBACK','lease_interest_receivable','FINANCE_LEASE','SALE_AND_LEASEBACK',6.000000),
 ('CYC_RETAIL_LEASEBACK','residual_value_receivable','FINANCE_LEASE','SALE_AND_LEASEBACK',6.000000),
 ('CYC_RETAIL_LEASEBACK','receivable_service','FINANCE_LEASE','SALE_AND_LEASEBACK',6.000000),
 ('CYC_RETAIL_LEASEBACK','lease_rent_receivable','FINANCE_LEASE','SALE_AND_LEASEBACK',6.000000),
 ('JYZL','tax_general','OPERATING_LEASE','DIRECT_LEASE',13.000000),
 ('JYZL','tax_general','OPERATING_LEASE','SALE_AND_LEASEBACK',13.000000),
 ('JYZL','lease_interest_receivable','OPERATING_LEASE','DIRECT_LEASE',13.000000),
 ('JYZL','lease_interest_receivable','OPERATING_LEASE','SALE_AND_LEASEBACK',13.000000),
 ('JYZL','residual_value_receivable','OPERATING_LEASE','DIRECT_LEASE',13.000000),
 ('JYZL','residual_value_receivable','OPERATING_LEASE','SALE_AND_LEASEBACK',13.000000),
 ('JYZL','receivable_service','OPERATING_LEASE','DIRECT_LEASE',6.000000),
 ('JYZL','receivable_service','OPERATING_LEASE','SALE_AND_LEASEBACK',6.000000),
 ('JYZL','lease_rent_receivable','OPERATING_LEASE','DIRECT_LEASE',13.000000),
 ('JYZL','lease_rent_receivable','OPERATING_LEASE','SALE_AND_LEASEBACK',13.000000);

INSERT INTO tmp_htqz_tax_rate VALUES
 ('ZLYW','lease_principal_receivable','FINANCE_LEASE','DIRECT_LEASE','MOVABLE',13.000000),
 ('ZLYW','lease_principal_receivable','FINANCE_LEASE','DIRECT_LEASE','REAL_ESTATE',9.000000),
 ('ZLYW','lease_principal_receivable','FINANCE_LEASE','SALE_AND_LEASEBACK','MOVABLE',0.000000),
 ('ZLYW','lease_principal_receivable','FINANCE_LEASE','SALE_AND_LEASEBACK','REAL_ESTATE',0.000000),
 ('CYC_RETAIL_LEASEBACK','lease_principal_receivable','FINANCE_LEASE','SALE_AND_LEASEBACK','MOVABLE',0.000000),
 ('CYC_RETAIL_LEASEBACK','lease_principal_receivable','FINANCE_LEASE','SALE_AND_LEASEBACK','REAL_ESTATE',0.000000),
 ('JYZL','lease_principal_receivable','OPERATING_LEASE','DIRECT_LEASE','MOVABLE',13.000000),
 ('JYZL','lease_principal_receivable','OPERATING_LEASE','DIRECT_LEASE','REAL_ESTATE',9.000000),
 ('JYZL','lease_principal_receivable','OPERATING_LEASE','SALE_AND_LEASEBACK','MOVABLE',13.000000),
 ('JYZL','lease_principal_receivable','OPERATING_LEASE','SALE_AND_LEASEBACK','REAL_ESTATE',9.000000);

UPDATE eg_tax_rate r
JOIN tmp_htqz_tax_rate t
 ON t.business_code=r.business_code AND t.fund_type=r.fund_type
 AND t.lease_type=r.lease_type AND t.lease_method=r.lease_sub_type
 AND COALESCE(r.asset_category,'')=t.asset_category
SET r.tax_rate=t.tax_rate, r.enable_flag='1', r.enable_date='2026-09-15', r.del_flag='0',
    r.update_by='htqz-tax-config-v3', r.update_time=NOW();

SET @tax_rate_id := GREATEST(COALESCE((SELECT MAX(id) FROM eg_tax_rate),0),2110000000000040000);
INSERT INTO eg_tax_rate
 (id,business_code,fund_type,enable_flag,enable_date,tax_rate,create_by,create_time,
  update_by,update_time,del_flag,lease_type,lease_sub_type,asset_category)
SELECT (@tax_rate_id:=@tax_rate_id+1),t.business_code,t.fund_type,'1','2026-09-15',t.tax_rate,
       'htqz-tax-config-v3',NOW(),'htqz-tax-config-v3',NOW(),'0',t.lease_type,t.lease_method,
       NULLIF(t.asset_category,'')
FROM tmp_htqz_tax_rate t
WHERE NOT EXISTS (
 SELECT 1 FROM eg_tax_rate r
 WHERE r.business_code=t.business_code AND r.fund_type=t.fund_type
   AND r.lease_type=t.lease_type AND r.lease_sub_type=t.lease_method
   AND COALESCE(r.asset_category,'')=t.asset_category
);

-- Tax lines use the configured-rate function. The first argument is the source
-- system; the function maps it to the accounting business code.
SET @htqz_voucher_id := 2110000000000000200;
UPDATE eg_scene_voucher_condition c
JOIN eg_scene_voucher_entry e ON e.id=c.scene_voucher_entry_id
SET c.script_amount="scale2({起租计算结果表.起租不含税利息}*taxRate({起租接口表.系统来源},'lease_interest_receivable',{起租接口表.租赁类型},{起租接口表.租赁方式}))",
    c.amount_description='按金额类型税率配置计算起租利息税额',
    c.update_by='htqz-tax-config-v3',c.update_time=NOW()
WHERE e.scene_voucher_id=@htqz_voucher_id AND e.fund_type IN
 ('lease_interest_vat_receivable','unearned_lease_interest_vat') AND c.del_flag='0';

UPDATE eg_scene_voucher_condition c
JOIN eg_scene_voucher_entry e ON e.id=c.scene_voucher_entry_id
SET c.script_amount="scale2({起租计算结果表.起租不含税留购价}*taxRate({起租接口表.系统来源},'residual_value_receivable',{起租接口表.租赁类型},{起租接口表.租赁方式}))",
    c.amount_description='按金额类型税率配置计算起租留购价税额',
    c.update_by='htqz-tax-config-v3',c.update_time=NOW()
WHERE e.scene_voucher_id=@htqz_voucher_id AND e.fund_type IN
 ('residual_value_vat_receivable','unearned_residual_value_vat') AND c.del_flag='0';

UPDATE eg_scene_voucher_condition c
JOIN eg_scene_voucher_entry e ON e.id=c.scene_voucher_entry_id
SET c.script_amount=CASE
      WHEN e.fund_type='prepaid_receivable_other_income_vat'
       THEN "scale2({起租计算结果表.未收手续费未摊销不含税金额}*taxRate({起租接口表.系统来源},'receivable_service',{起租接口表.租赁类型},{起租接口表.租赁方式}))"
      WHEN e.fund_type='unearned_other_income_vat' AND c.serial=1
       THEN "scale2({起租计算结果表.未收手续费未摊销不含税金额}*taxRate({起租接口表.系统来源},'receivable_service',{起租接口表.租赁类型},{起租接口表.租赁方式}))"
      WHEN e.fund_type='receivable_other_income_vat' AND c.serial=1
       THEN "scale2({起租计算结果表.未收取手续费不含税金额}*taxRate({起租接口表.系统来源},'receivable_service',{起租接口表.租赁类型},{起租接口表.租赁方式}))"
      ELSE "scale2({起租计算结果表.应收手续费不含税金额}*taxRate({起租接口表.系统来源},'receivable_service',{起租接口表.租赁类型},{起租接口表.租赁方式}))"
    END,
    c.amount_description='按手续费金额类型税率配置计算税额',
    c.update_by='htqz-tax-config-v3',c.update_time=NOW()
WHERE e.scene_voucher_id=@htqz_voucher_id
 AND e.fund_type IN ('prepaid_receivable_other_income_vat','receivable_other_income_vat','unearned_other_income_vat')
 AND c.del_flag='0';

COMMIT;

SELECT COUNT(*) AS exposed_tax_rate_interface_fields
FROM eg_scene_fields
WHERE scene_code='HTQZ' AND field_code IN ('vat_rate','tax_rate') AND del_flag='0';
SELECT COUNT(*) AS configured_htqz_tax_rates
FROM eg_tax_rate r JOIN tmp_htqz_tax_rate t
 ON t.business_code=r.business_code AND t.fund_type=r.fund_type
 AND t.lease_type=r.lease_type AND t.lease_method=r.lease_sub_type
WHERE r.enable_flag='1' AND r.del_flag='0';
SELECT COUNT(*) AS voucher_rules_using_tax_rate_function
FROM eg_scene_voucher_condition c
JOIN eg_scene_voucher_entry e ON e.id=c.scene_voucher_entry_id
WHERE e.scene_voucher_id=@htqz_voucher_id AND c.del_flag='0'
 AND c.script_amount LIKE '%taxRate(%';
