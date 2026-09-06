ALTER TABLE financialdb4.eg_fund_payment_data ADD payment_item varchar(50) NULL;
COMMENT ON COLUMN financialdb4.eg_fund_payment_data.payment_item IS '付款项目';
ALTER TABLE financialdb4.eg_fund_payment_data ADD payment_type varchar(50) NULL;
COMMENT ON COLUMN financialdb4.eg_fund_payment_data.payment_type IS '付款类型';

ALTER TABLE financialdb4.eg_claim_order_detail ADD file_path varchar(500) NULL;
COMMENT ON COLUMN financialdb4.eg_claim_order_detail.file_path IS '文件路径';

ALTER TABLE financialdb4.eg_assets_expense ADD errs_info text NULL;
COMMENT ON COLUMN financialdb4.eg_assets_expense.errs_info IS '错误信息';
