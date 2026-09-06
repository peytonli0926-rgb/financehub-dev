--已执行，20240226
ALTER TABLE financialdb.eg_raw_transaction_data ADD business_date timestamp NULL;
COMMENT ON COLUMN financialdb.eg_raw_transaction_data.business_date IS '业务日期';

