-- Technical event codes are rule-engine data. User-facing document and voucher fields use Chinese names.
DROP TEMPORARY TABLE IF EXISTS tmp_retail_event_name;
CREATE TEMPORARY TABLE tmp_retail_event_name (
  event_code VARCHAR(20) PRIMARY KEY,
  event_name VARCHAR(100) NOT NULL
);

INSERT INTO tmp_retail_event_name (event_code, event_name) VALUES
('CR000','收到现金折扣'),('CR001','收取保证金'),('CR003','购入租赁资产'),('CR005','到并核销租金'),
('CR006','款项无法确认'),('CR007','人工明确款项用途'),('CR008','收到款项核销'),('CR025','支付资产管理费'),
('CR029','退回平台合作方提前结清贴息金额'),('CR033','退回的分润费'),('CR036','支付银行手续费'),
('CR040','抵押服务费'),('CR041','支付通联手续费'),('CR043','前期逾期留购价'),('CR044','渠道商分成结算'),('CR056','合作方提前结清'),
('RF001','普通退款'),('RF002','保证金退款'),('RF003','未确认款退款'),
('SC001','厂商贴息确认'),('SC002','平台贴息确认'),('SC003','提前结清贴息冲回'),
('OD001','本金转逾期'),('OD002','利息转逾期'),('OD003','留购价转逾期'),('OD004','逾期罚息确认'),
('TS001','租金计划调整'),('TS002','租金信息变更'),('TS003','结清'),('TS004','起租后GPS加装'),
('TS005','留购价反向'),('TS006','费用减免租金'),('TS007','费用减免留购价'),('TS008','天津车辆处置结清'),
('TS009','提前留购'),('TS010','尾款调整租金'),('TS011','车辆处置'),('TS012','车辆买断');

-- Voucher headers and entries may have used sourceEventCode in their generated summaries.
UPDATE eg_voucher v
JOIN eg_interface_data i ON i.id = v.interface_data_id
JOIN tmp_retail_event_name n ON n.event_code = JSON_UNQUOTE(JSON_EXTRACT(i.interface_data, '$.eventCode'))
SET v.voucher_summary = REPLACE(v.voucher_summary,
  CONCAT('-', JSON_UNQUOTE(JSON_EXTRACT(i.interface_data, '$.sourceEventCode')), '-'),
  CONCAT('-', n.event_name, '-'))
WHERE v.del_flag = '0' AND i.del_flag = '0';

UPDATE eg_voucher_entry e
JOIN eg_voucher v ON v.id = e.voucher_id AND v.period_code = e.period_code
JOIN eg_interface_data i ON i.id = v.interface_data_id
JOIN tmp_retail_event_name n ON n.event_code = JSON_UNQUOTE(JSON_EXTRACT(i.interface_data, '$.eventCode'))
SET e.voucher_summary = REPLACE(e.voucher_summary,
  CONCAT('-', JSON_UNQUOTE(JSON_EXTRACT(i.interface_data, '$.sourceEventCode')), '-'),
  CONCAT('-', n.event_name, '-'))
WHERE e.del_flag = '0' AND v.del_flag = '0' AND i.del_flag = '0';

UPDATE eg_interface_data i
JOIN tmp_retail_event_name n ON n.event_code = JSON_UNQUOTE(JSON_EXTRACT(i.interface_data, '$.eventCode'))
SET i.interface_data = JSON_SET(i.interface_data,
  '$.sourceEventOriginal', COALESCE(JSON_UNQUOTE(JSON_EXTRACT(i.interface_data, '$.sourceEventOriginal')), JSON_UNQUOTE(JSON_EXTRACT(i.interface_data, '$.sourceEventCode'))),
  '$.sourceEventCode', n.event_name,
  '$.event_name', n.event_name)
WHERE i.del_flag = '0';

UPDATE eg_raw_transaction_data r
JOIN tmp_retail_event_name n ON n.event_code = JSON_UNQUOTE(JSON_EXTRACT(r.message_content, '$.eventCode'))
SET r.message_content = JSON_SET(r.message_content,
  '$.sourceEventOriginal', COALESCE(JSON_UNQUOTE(JSON_EXTRACT(r.message_content, '$.sourceEventOriginal')), JSON_UNQUOTE(JSON_EXTRACT(r.message_content, '$.sourceEventCode'))),
  '$.sourceEventCode', n.event_name,
  '$.event_name', n.event_name)
WHERE r.del_flag = '0';

DROP TEMPORARY TABLE tmp_retail_event_name;
