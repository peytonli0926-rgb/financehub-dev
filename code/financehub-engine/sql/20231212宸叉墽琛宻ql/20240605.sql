--余额表新增字段
ALTER TABLE financialdb.eg_contract_balance ADD payable_other_margin_year_balance numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_contract_balance.payable_other_margin_year_balance IS '应付一年内其他保证金余额';

ALTER TABLE financialdb.eg_contract_balance ADD payable_other_margin_year_amount numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_contract_balance.payable_other_margin_year_amount IS '应付一年内其他保证金发生额';

ALTER TABLE financialdb.eg_contract_balance_latest ADD payable_other_margin_year_balance numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_contract_balance_latest.payable_other_margin_year_balance IS '应付一年内其他保证金余额';

ALTER TABLE financialdb.eg_contract_balance_latest ADD payable_other_margin_year_amount numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_contract_balance_latest.payable_other_margin_year_amount IS '应付一年内其他保证金发生额';

ALTER TABLE financialdb.eg_contract_balance_temp ADD payable_other_margin_year_balance numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_other_margin_year_balance IS '应付一年内其他保证金余额';

ALTER TABLE financialdb.eg_contract_balance_temp ADD payable_other_margin_year_amount numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_other_margin_year_amount IS '应付一年内其他保证金发生额';

ALTER TABLE financialdb.eg_voucher_entry ADD voucher_amount numeric(24, 2) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_entry.voucher_amount IS '凭证金额';
