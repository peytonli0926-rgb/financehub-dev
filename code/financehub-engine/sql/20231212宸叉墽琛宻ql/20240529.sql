ALTER TABLE financialdb.eg_voucher ADD is_summary bpchar(1) NULL DEFAULT '0'::bpchar;
COMMENT ON COLUMN financialdb.eg_voucher.is_summary IS '传送金蝶是否汇总(0:汇总,1:不汇总)';

ALTER TABLE financialdb.eg_voucher_entry ADD convert_debit_amount numeric(24, 2) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_entry.convert_debit_amount IS '折本币借方发生额';

ALTER TABLE financialdb.eg_voucher_entry ADD convert_credit_amount numeric(24, 2) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_entry.convert_credit_amount IS '折本币贷方发生额';