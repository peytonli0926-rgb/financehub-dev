ALTER TABLE financialdb4.eg_contract ADD tax_amount numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb4.eg_contract.tax_amount IS '税金计提金额';
