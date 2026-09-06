ALTER TABLE financialdb.eg_non_confirm_collection_second_detail ADD operation_type varchar(50) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.operation_type IS '操作类型';

ALTER TABLE financialdb.eg_business_claim_repayment_record ADD new_ebank_serial_number varchar(60) NULL;
COMMENT ON COLUMN financialdb.eg_business_claim_repayment_record.new_ebank_serial_number IS '新业务系统批扣流水号';
ALTER TABLE financialdb.eg_business_claim_repayment_record ADD income_ym_old varchar(10) NULL;
COMMENT ON COLUMN financialdb.eg_business_claim_repayment_record.income_ym_old IS '原入账月份';
