--成本类新增流程实例id
ALTER TABLE financialdb.eg_cost_channel_fee ADD process_instance_id int8 NULL;
COMMENT ON COLUMN financialdb.eg_cost_channel_fee.process_instance_id IS '流程实例id';