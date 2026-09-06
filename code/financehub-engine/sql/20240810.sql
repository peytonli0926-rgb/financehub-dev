ALTER TABLE financialdb4.eg_fund_ebank_transaction_data ADD bill_number varchar(100) NULL;

ALTER TABLE financialdb4.eg_fund_business_system_ebank_wy_amount ADD business_date date NULL;
COMMENT ON COLUMN financialdb4.eg_fund_business_system_ebank_wy_amount.business_date IS '业务日期';
