-- 金额类型为通用「应收租赁本金」；不动产/动产税率差异放在独立资产类别维度。
-- 先执行科目/凭证模板闭环脚本，再执行本脚本并部署对应 Java 版本。

USE financehub_lease;

SELECT IF(COUNT(*)=0,
 'ALTER TABLE financehub_lease.eg_tax_rate ADD COLUMN asset_category VARCHAR(30) NULL COMMENT ''动产/不动产税率维度''',
 'SELECT 1') INTO @asset_category_ddl
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA='financehub_lease' AND TABLE_NAME='eg_tax_rate'
 AND COLUMN_NAME='asset_category';
PREPARE asset_category_stmt FROM @asset_category_ddl;
EXECUTE asset_category_stmt;
DEALLOCATE PREPARE asset_category_stmt;

START TRANSACTION;

UPDATE eg_tax_rate
SET asset_category='MOVABLE',update_by='amount-closure-v1',update_time=NOW()
WHERE fund_type='lease_principal_receivable' AND asset_category IS NULL AND del_flag='0';
UPDATE eg_tax_rate
SET fund_type='lease_principal_receivable',asset_category='REAL_ESTATE',
    update_by='amount-closure-v1',update_time=NOW()
WHERE fund_type='lease_principal_real_estate' AND del_flag='0';

UPDATE financehub_lease.sys_dict_data
SET status='1',remark='动产/不动产是税率资产类别维度，不是金额类型'
WHERE dict_type='sys_cash_type' AND dict_value='lease_principal_real_estate';
UPDATE financialdb4.sys_dict_data
SET status='1',remark='动产/不动产是税率资产类别维度，不是金额类型'
WHERE dict_type='sys_cash_type' AND dict_value='lease_principal_real_estate';

COMMIT;

SELECT COUNT(*) AS old_principal_tax_fund_types FROM eg_tax_rate
WHERE del_flag='0' AND fund_type='lease_principal_real_estate';
SELECT business_code,lease_type,lease_sub_type,asset_category,tax_rate
FROM eg_tax_rate WHERE del_flag='0' AND fund_type='lease_principal_receivable'
ORDER BY business_code,lease_type,lease_sub_type,asset_category;
