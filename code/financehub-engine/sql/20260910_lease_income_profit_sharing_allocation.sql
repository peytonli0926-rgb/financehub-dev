-- 收益计提明细增加独立的分润费分摊字段，避免复用“逾期调整额”。
SET @profit_share_column_exists = (
  SELECT COUNT(*) FROM information_schema.columns
  WHERE table_schema = DATABASE()
    AND table_name = 'eg_lease_income_details'
    AND column_name = 'profit_sharing_allocation_amount'
);
SET @profit_share_ddl = IF(
  @profit_share_column_exists = 0,
  'ALTER TABLE eg_lease_income_details ADD COLUMN profit_sharing_allocation_amount DECIMAL(20,2) NOT NULL DEFAULT 0 COMMENT ''当月分润费分摊额（不含税），冲减合同应收息中包含的分润收入'' AFTER overdue_adjustment_amount',
  'SELECT 1'
);
PREPARE profit_share_statement FROM @profit_share_ddl;
EXECUTE profit_share_statement;
DEALLOCATE PREPARE profit_share_statement;

-- 华夏金租零售融资租赁演示合同统一归属零售融资租赁业务系统。
UPDATE eg_contract
SET system_code = 'RETAIL_FINANCE_LEASE', update_time = NOW()
WHERE business_code = 'CYC_RETAIL_LEASEBACK'
  AND contract_code LIKE 'HXZL-%'
  AND del_flag = '0';

UPDATE eg_repayment_plan
SET system_code = 'RETAIL_FINANCE_LEASE', update_time = NOW()
WHERE contract_code LIKE 'HXZL-%' AND del_flag = '0';

UPDATE eg_lease_income_details
SET system_code = 'RETAIL_FINANCE_LEASE', update_time = NOW()
WHERE contract_code LIKE 'HXZL-%' AND del_flag = '0';
