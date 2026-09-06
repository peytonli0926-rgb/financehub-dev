        ALTER TABLE financialdb.eg_charge_off ADD bad_debt_write_off_balance numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_charge_off.bad_debt_write_off_balance IS '坏账核销余额';