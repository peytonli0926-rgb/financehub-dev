ALTER TABLE financialdb.eg_non_confirm_collection_voucher_record ADD bank_no varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_voucher_record.bank_no IS '银行账号';
ALTER TABLE financialdb.eg_non_confirm_collection_voucher_record ADD loans_contract_code varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_voucher_record.loans_contract_code IS '借款合同编号';
