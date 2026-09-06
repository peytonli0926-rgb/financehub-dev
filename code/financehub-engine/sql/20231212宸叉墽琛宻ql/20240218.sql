--已执行，20240226
ALTER TABLE financialdb.eg_lease_income_details ADD intable_transfer_outtable_amount numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_lease_income_details.intable_transfer_outtable_amount IS '表内转表外金额';
ALTER TABLE financialdb.eg_lease_income_details ADD outtable_transfer_intable_amount numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_lease_income_details.outtable_transfer_intable_amount IS '表外转表内金额';
