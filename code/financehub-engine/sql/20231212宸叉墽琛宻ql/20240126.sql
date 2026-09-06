--诉讼费新增流程实例id
ALTER TABLE financialdb.eg_court_cost ADD process_instance_id int8 NULL;
COMMENT ON COLUMN financialdb.eg_court_cost.process_instance_id IS '流程实例id';

ALTER TABLE financialdb.eg_court_cost_details ALTER COLUMN voucher_id TYPE text USING voucher_id::text;
COMMENT ON COLUMN financialdb.eg_court_cost_details.voucher_id IS '凭证id(多个逗号分隔)';