-- Isolated fixtures for positive/negative tail-difference integration tests.
-- Run against financehub_lease. Remove generated adjustment records through the
-- application first, then run 20260911_tail_difference_fixture_cleanup.sql.

DELETE FROM eg_contract_balance_latest
WHERE id IN (202609119100001, 202609119100002)
   OR contract_code IN ('WCTZ-TEST-POS-001', 'WCTZ-TEST-NEG-001');

DELETE FROM eg_contract
WHERE id IN (202609119000001, 202609119000002)
   OR contract_code IN ('WCTZ-TEST-POS-001', 'WCTZ-TEST-NEG-001');

INSERT INTO eg_contract
    (id, contract_code, client_code, org_id, lease_date_end, business_code,
     lease_type, contract_status, currency_type, create_by, create_time, del_flag,
     special_flag, tax_rate)
VALUES
    (202609119000001, 'WCTZ-TEST-POS-001', 'WCTZ-TEST-CLIENT', 'HXZL001', '2026-08-31 23:59:59',
     'CYC_RETAIL_LEASEBACK', '回租', '正常结清', 'CNY', 'tail-difference-test', NOW(), '0', NULL, 0.060000),
    (202609119000002, 'WCTZ-TEST-NEG-001', 'WCTZ-TEST-CLIENT', 'HXZL001', '2026-08-31 23:59:59',
     'CYC_RETAIL_LEASEBACK', '回租', '正常结清', 'CNY', 'tail-difference-test', NOW(), '0', NULL, 0.060000);

INSERT INTO eg_contract_balance_latest
    (id, business_code, business_date, contract_code, client_code, org_id,
     create_by, create_time, del_flag, period_code, unearned_lease_interest_balance)
VALUES
    (202609119100001, 'CYC_RETAIL_LEASEBACK', '2026-09-11 00:00:00', 'WCTZ-TEST-POS-001',
     'WCTZ-TEST-CLIENT', 'HXZL001', 'tail-difference-test', NOW(), '0', '202609', 0.01),
    (202609119100002, 'CYC_RETAIL_LEASEBACK', '2026-09-11 00:00:00', 'WCTZ-TEST-NEG-001',
     'WCTZ-TEST-CLIENT', 'HXZL001', 'tail-difference-test', NOW(), '0', '202609', -0.01);

SELECT contract_code, org_id, business_code, unearned_lease_interest_balance
FROM eg_contract_balance_latest
WHERE id IN (202609119100001, 202609119100002)
ORDER BY id;
