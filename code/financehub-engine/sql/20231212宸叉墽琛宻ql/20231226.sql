--开票认领新增字段
ALTER TABLE financialdb.eg_invoice_claim ADD back_tax_amount numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_invoice_claim.back_tax_amount IS 'back含税金额';
ALTER TABLE financialdb.eg_invoice_claim ADD back_tax_value numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_invoice_claim.back_tax_value IS 'back含税税额';
ALTER TABLE financialdb.eg_invoice_claim ADD invoice_number varchar(50) NULL;
COMMENT ON COLUMN financialdb.eg_invoice_claim.invoice_number IS '发票号码';
