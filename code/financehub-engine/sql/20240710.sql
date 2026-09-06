---------------------------------------prod 已执行   20240710------------------------------------------------------------------------------
CREATE INDEX eg_non_confirm_collection_sum_ebank_number_idx ON financialdb4.eg_non_confirm_collection_sum (ebank_number);
CREATE INDEX eg_non_confirm_collection_sum_ebank_serial_number_idx ON financialdb4.eg_non_confirm_collection_sum (ebank_serial_number);
CREATE INDEX eg_business_claim_repayment_record_ebank_serial_number_idx ON financialdb4.eg_business_claim_repayment_record (ebank_serial_number);
ALTER TABLE eg_fund_payment_data ADD bill_number varchar(100) NULL;
COMMENT ON COLUMN eg_fund_payment_data.bill_number IS '票据号';
---------------------------------------dev,uat,prod 已执行   20240710------------------------------------------------------------------------------