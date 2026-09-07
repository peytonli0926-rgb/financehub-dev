-- 修正零售融资租赁生命周期凭证的记账日期与会计期间。
-- 规则：记账日期取业务日期，会计期间取业务日期yyyyMM。
START TRANSACTION;

UPDATE eg_interface_data
SET voucher_date=DATE(business_date),update_by='retail-period-fix',update_time=NOW()
WHERE contract_code='HXZL-PC-202609-0001'
  AND business_code='CYC_RETAIL_LEASEBACK'
  AND business_date IS NOT NULL;

UPDATE eg_voucher
SET voucher_date=DATE(business_date),
    period_code=CAST(DATE_FORMAT(business_date,'%Y%m') AS UNSIGNED),
    update_by='retail-period-fix',update_time=NOW()
WHERE contract_code='HXZL-PC-202609-0001'
  AND business_code='CYC_RETAIL_LEASEBACK'
  AND business_date IS NOT NULL;

UPDATE eg_voucher_entry e
JOIN eg_voucher v ON v.id=e.voucher_id
SET e.period_code=v.period_code,e.update_by='retail-period-fix',e.update_time=NOW()
WHERE v.contract_code='HXZL-PC-202609-0001'
  AND v.business_code='CYC_RETAIL_LEASEBACK';

UPDATE eg_contract_balance b
JOIN eg_voucher v ON v.id=b.voucher_id
SET b.voucher_date=v.voucher_date,b.period_code=v.period_code,
    b.update_by='retail-period-fix',b.update_time=NOW()
WHERE v.contract_code='HXZL-PC-202609-0001'
  AND v.business_code='CYC_RETAIL_LEASEBACK';

UPDATE eg_contract_balance_latest b
JOIN eg_voucher v ON v.id=b.voucher_id
SET b.voucher_date=v.voucher_date,b.period_code=v.period_code,
    b.update_by='retail-period-fix',b.update_time=NOW()
WHERE v.contract_code='HXZL-PC-202609-0001'
  AND v.business_code='CYC_RETAIL_LEASEBACK';

UPDATE eg_contract_balance_temp b
JOIN eg_voucher v ON v.id=b.voucher_id
SET b.voucher_date=v.voucher_date,b.period_code=v.period_code,
    b.update_by='retail-period-fix',b.update_time=NOW()
WHERE v.contract_code='HXZL-PC-202609-0001'
  AND v.business_code='CYC_RETAIL_LEASEBACK';

COMMIT;

SELECT order_id,business_date,voucher_date,period_code,scene_code
FROM eg_voucher
WHERE contract_code='HXZL-PC-202609-0001'
ORDER BY business_date,order_id;
