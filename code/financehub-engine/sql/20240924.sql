ALTER TABLE financialdb4.eg_fund_payment_data ADD batch_number varchar(100) NULL;
COMMENT ON COLUMN financialdb4.eg_fund_payment_data.batch_number IS '付款批次号';
