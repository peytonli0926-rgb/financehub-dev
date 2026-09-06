--出表ABS新增字段
ALTER TABLE financialdb.eg_out_table_contract_detail ADD period_code int4 NULL;
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.period_code IS '会计期间';
ALTER TABLE financialdb.eg_out_table_abs ADD period_code int4 NULL;
COMMENT ON COLUMN financialdb.eg_out_table_abs.period_code IS '会计期间';

ALTER TABLE financialdb.eg_contract ADD contract_code_m varchar(50) NULL;
COMMENT ON COLUMN financialdb.eg_contract.contract_code_m IS '主合同编号';
