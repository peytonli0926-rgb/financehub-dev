-- HXZL-PC-202609-0001 完整生命周期测试数据日期调整
START TRANSACTION;

UPDATE eg_raw_transaction_data
SET business_date = CASE order_id
        WHEN 'HXZL-PC-202609-0001-PAY-01' THEN '2026-09-03 09:30:00'
        WHEN 'HXZL-PC-202609-0001-PAY-02' THEN '2026-09-04 09:00:00'
        WHEN 'HXZL-PC-202609-0001-PAY-03' THEN '2026-09-04 09:30:00'
        WHEN 'HXZL-PC-202609-0001-PAY-04' THEN '2026-09-05 10:00:00'
        WHEN 'HXZL-PC-202609-0001-PAY-05' THEN '2026-10-03 10:00:00'
        WHEN 'HXZL-PC-202609-0001-PAY-06' THEN '2027-03-03 10:00:00'
        WHEN 'HXZL-PC-202609-0001-PAY-07' THEN '2027-06-20 10:00:00'
        WHEN 'HXZL-PC-202609-0001-PAY-08' THEN '2027-06-20 10:30:00'
    END,
    message_content = JSON_SET(
        message_content,
        '$.businessDate', DATE_FORMAT(CASE order_id
            WHEN 'HXZL-PC-202609-0001-PAY-01' THEN '2026-09-03 09:30:00'
            WHEN 'HXZL-PC-202609-0001-PAY-02' THEN '2026-09-04 09:00:00'
            WHEN 'HXZL-PC-202609-0001-PAY-03' THEN '2026-09-04 09:30:00'
            WHEN 'HXZL-PC-202609-0001-PAY-04' THEN '2026-09-05 10:00:00'
            WHEN 'HXZL-PC-202609-0001-PAY-05' THEN '2026-10-03 10:00:00'
            WHEN 'HXZL-PC-202609-0001-PAY-06' THEN '2027-03-03 10:00:00'
            WHEN 'HXZL-PC-202609-0001-PAY-07' THEN '2027-06-20 10:00:00'
            WHEN 'HXZL-PC-202609-0001-PAY-08' THEN '2027-06-20 10:30:00'
        END, '%Y-%m-%d %H:%i:%s'),
        '$.business_date', DATE_FORMAT(CASE order_id
            WHEN 'HXZL-PC-202609-0001-PAY-01' THEN '2026-09-03 09:30:00'
            WHEN 'HXZL-PC-202609-0001-PAY-02' THEN '2026-09-04 09:00:00'
            WHEN 'HXZL-PC-202609-0001-PAY-03' THEN '2026-09-04 09:30:00'
            WHEN 'HXZL-PC-202609-0001-PAY-04' THEN '2026-09-05 10:00:00'
            WHEN 'HXZL-PC-202609-0001-PAY-05' THEN '2026-10-03 10:00:00'
            WHEN 'HXZL-PC-202609-0001-PAY-06' THEN '2027-03-03 10:00:00'
            WHEN 'HXZL-PC-202609-0001-PAY-07' THEN '2027-06-20 10:00:00'
            WHEN 'HXZL-PC-202609-0001-PAY-08' THEN '2027-06-20 10:30:00'
        END, '%Y-%m-%d %H:%i:%s')
    )
WHERE order_id IN (
    'HXZL-PC-202609-0001-PAY-01', 'HXZL-PC-202609-0001-PAY-02',
    'HXZL-PC-202609-0001-PAY-03', 'HXZL-PC-202609-0001-PAY-04',
    'HXZL-PC-202609-0001-PAY-05', 'HXZL-PC-202609-0001-PAY-06',
    'HXZL-PC-202609-0001-PAY-07', 'HXZL-PC-202609-0001-PAY-08'
);

UPDATE eg_interface_data i
JOIN eg_raw_transaction_data r ON r.id = i.interface_id
SET i.business_date = r.business_date,
    i.interface_data = JSON_SET(
        i.interface_data,
        '$.businessDate', DATE_FORMAT(r.business_date, '%Y-%m-%d %H:%i:%s'),
        '$.business_date', DATE_FORMAT(r.business_date, '%Y-%m-%d %H:%i:%s')
    )
WHERE r.order_id LIKE 'HXZL-PC-202609-0001-PAY-%';

UPDATE eg_voucher v
JOIN eg_interface_data i ON i.id = v.interface_data_id
JOIN eg_raw_transaction_data r ON r.id = i.interface_id
SET v.business_date = r.business_date
WHERE r.order_id LIKE 'HXZL-PC-202609-0001-PAY-%';

COMMIT;
