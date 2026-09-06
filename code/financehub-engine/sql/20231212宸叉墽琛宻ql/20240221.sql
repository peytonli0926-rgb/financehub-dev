--已执行，20240226
ALTER TABLE financialdb.eg_lease_income ADD approve_id int8 NULL;
COMMENT ON COLUMN financialdb.eg_lease_income.approve_id IS '审核id';
