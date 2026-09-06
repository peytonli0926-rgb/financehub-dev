--余额表新增索引
CREATE INDEX eg_contract_balance_temp_contract_code_idx ON financialdb.eg_contract_balance_temp USING btree (contract_code, voucher_date);

ALTER TABLE financialdb.eg_voucher_to_eas_record ADD cashier varchar(50) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.cashier IS '出纳人';
