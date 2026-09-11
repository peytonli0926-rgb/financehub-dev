-- Run only after deleting the generated tail-difference document in the application.

DELETE FROM eg_contract_balance_latest
WHERE id IN (202609119100001, 202609119100002)
   OR contract_code IN ('WCTZ-TEST-POS-001', 'WCTZ-TEST-NEG-001');

DELETE FROM eg_contract
WHERE id IN (202609119000001, 202609119000002)
   OR contract_code IN ('WCTZ-TEST-POS-001', 'WCTZ-TEST-NEG-001');

SELECT 'tail-difference fixture cleaned' AS result;
