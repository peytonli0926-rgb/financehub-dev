-- 清理历史补录逻辑为演示合同重复生成的印花税事件。
-- 使用物理删除；仅删除指定重复订单号，起租时正确生成的印花税事件不受影响。
START TRANSACTION;

DELETE e
FROM eg_voucher_entry e
JOIN eg_voucher v ON v.id=e.voucher_id
WHERE v.order_id='HXZL-DEMO-NORMAL-001-START-AUTO-JTYS';

DELETE b
FROM eg_contract_balance b
JOIN eg_voucher v ON v.id=b.voucher_id
WHERE v.order_id='HXZL-DEMO-NORMAL-001-START-AUTO-JTYS';

DELETE b
FROM eg_contract_balance_temp b
JOIN eg_voucher v ON v.id=b.voucher_id
WHERE v.order_id='HXZL-DEMO-NORMAL-001-START-AUTO-JTYS';

DELETE FROM eg_voucher
WHERE order_id='HXZL-DEMO-NORMAL-001-START-AUTO-JTYS';

DELETE FROM eg_interface_data
WHERE order_id='HXZL-DEMO-NORMAL-001-START-AUTO-JTYS';

DELETE FROM eg_raw_transaction_data
WHERE order_id='HXZL-DEMO-NORMAL-001-START-AUTO-JTYS';

COMMIT;
