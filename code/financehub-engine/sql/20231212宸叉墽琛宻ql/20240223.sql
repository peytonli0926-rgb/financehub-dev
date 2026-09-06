--已执行，20240226
ALTER TABLE financialdb.eg_postal_storage_fee_details ALTER COLUMN voucher_id TYPE varchar(2000) USING voucher_id::varchar;
ALTER TABLE financialdb.eg_postal_storage_fee_details ADD exception_type varchar(2000) NULL;
COMMENT ON COLUMN financialdb.eg_postal_storage_fee_details.exception_type IS '异常信息';

ALTER TABLE financialdb.eg_margin_contract_balance ALTER COLUMN voucher_id TYPE varchar(2000) USING voucher_id::varchar;
ALTER TABLE financialdb.eg_margin_contract_balance ADD exception_type varchar(2000) NULL;
COMMENT ON COLUMN financialdb.eg_margin_contract_balance.exception_type IS '异常信息';

ALTER TABLE financialdb.eg_offline_contract ALTER COLUMN voucher_id TYPE varchar(2000) USING voucher_id::varchar;
ALTER TABLE financialdb.eg_offline_contract ADD exception_type varchar(2000) NULL;
COMMENT ON COLUMN financialdb.eg_offline_contract.exception_type IS '异常信息';

ALTER TABLE financialdb.eg_payable_insurance_details ALTER COLUMN voucher_id TYPE varchar(2000) USING voucher_id::varchar;
ALTER TABLE financialdb.eg_payable_insurance_details ADD exception_type varchar(2000) NULL;
COMMENT ON COLUMN financialdb.eg_payable_insurance_details.exception_type IS '异常信息';

ALTER TABLE financialdb.eg_service_fee_details ALTER COLUMN voucher_id TYPE varchar(2000) USING voucher_id::varchar;

ALTER TABLE financialdb.eg_contract_his ALTER COLUMN voucher_id TYPE varchar(2000) USING voucher_id::varchar;
ALTER TABLE financialdb.eg_contract_his ADD exception_type varchar(2000) NULL;
COMMENT ON COLUMN financialdb.eg_contract_his.exception_type IS '异常信息';
