--修改出表ABC详情默认值
ALTER TABLE financialdb.eg_out_table_contract_detail ALTER COLUMN receivable_rent SET DEFAULT 0;
ALTER TABLE financialdb.eg_out_table_contract_detail ALTER COLUMN receivable_residual_value SET DEFAULT 0;
ALTER TABLE financialdb.eg_out_table_contract_detail ALTER COLUMN receivable_outtax SET DEFAULT 0;
ALTER TABLE financialdb.eg_out_table_contract_detail ALTER COLUMN unrealized_revenue SET DEFAULT 0;
ALTER TABLE financialdb.eg_out_table_contract_detail ALTER COLUMN lessee_margin SET DEFAULT 0;

ALTER TABLE financialdb.eg_tail_difference_adjustment_detail ALTER COLUMN contract_code DROP NOT NULL;
