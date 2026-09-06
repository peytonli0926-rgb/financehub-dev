ALTER TABLE financialdb.eg_contract ADD manual_lease_flag varchar(2) NULL;
COMMENT ON COLUMN financialdb.eg_contract.manual_lease_flag IS '0:非手工起租 1:手工起租';
ALTER TABLE financialdb.eg_contract ALTER COLUMN manual_lease_flag SET DEFAULT 0;

ALTER TABLE financialdb.eg_lease_income_details ADD manual_change_mark varchar(10) NULL;
COMMENT ON COLUMN financialdb.eg_lease_income_details.manual_change_mark IS '交易结构手工调整标志';

ALTER TABLE financialdb.eg_lease_income_details ALTER COLUMN manual_change_mark SET DEFAULT 0;
