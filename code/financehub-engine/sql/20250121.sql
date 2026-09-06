ALTER TABLE financialdb4.eg_fund_payment_data ADD actual_client_name varchar NULL;
COMMENT ON COLUMN financialdb4.eg_fund_payment_data.actual_client_name IS '实际客户名';
