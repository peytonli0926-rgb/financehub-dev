ALTER TABLE financialdb.eg_service_fee ADD process_instance_id int8 NULL;
COMMENT ON COLUMN financialdb.eg_service_fee.process_instance_id IS '流程实例id';
