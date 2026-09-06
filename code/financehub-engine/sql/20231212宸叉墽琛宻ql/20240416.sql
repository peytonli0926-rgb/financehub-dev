ALTER TABLE financialdb.eg_voucher_to_eas_record ADD assist_abstract varchar(2000) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.assist_abstract IS '辅助账摘要';
ALTER TABLE financialdb.eg_voucher_to_eas_record ADD assist_biz_date varchar(20) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.assist_biz_date IS '业务日期';
ALTER TABLE financialdb.eg_voucher_to_eas_record ADD assist_end_date varchar(20) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.assist_end_date IS '到期日';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.settlement_type IS '结算方式';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.settlement_number IS '结算号';
ALTER TABLE financialdb.eg_voucher_to_eas_record ADD ticket_number varchar(50) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.ticket_number IS '票证号码';
ALTER TABLE financialdb.eg_voucher_to_eas_record ADD invoice_number varchar(50) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.invoice_number IS '发票号码';
ALTER TABLE financialdb.eg_voucher_to_eas_record ADD source_type varchar(50) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.source_type IS '来源类型';



ALTER TABLE financialdb.eg_voucher_entry ADD account_assist_balance_id bigint NULL;
COMMENT ON COLUMN financialdb.eg_voucher_entry.account_assist_balance_id IS '辅助帐科目余额表ID';


ALTER TABLE financialdb.eg_parity_transfer ADD voucher_ids text NULL;
COMMENT ON COLUMN financialdb.eg_parity_transfer.voucher_ids IS '凭证id,多个按照逗号分隔';

ALTER TABLE financialdb.eg_parity_transfer ALTER COLUMN process_status SET DEFAULT 1;
