COMMENT ON COLUMN financialdb.eg_impairment_provision_upload_task.status IS '状态';
ALTER TABLE financialdb.eg_impairment_provision_upload_task ADD task_type varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_impairment_provision_upload_task.task_type IS '任务类型';
ALTER TABLE financialdb.eg_impairment_provision_upload_task ADD data_size int4 NULL;
COMMENT ON COLUMN financialdb.eg_impairment_provision_upload_task.data_size IS '任务处理数据总条数';
ALTER TABLE financialdb.eg_impairment_provision_upload_task ADD data_success_size int4 NULL;
COMMENT ON COLUMN financialdb.eg_impairment_provision_upload_task.data_success_size IS '成功数据条数';
ALTER TABLE financialdb.eg_impairment_provision_upload_task ADD data_failed_size int4 NULL;
COMMENT ON COLUMN financialdb.eg_impairment_provision_upload_task.data_failed_size IS '失败数据条数';
ALTER TABLE financialdb.eg_impairment_provision_upload_task ADD doc_id int8 NULL;
COMMENT ON COLUMN financialdb.eg_impairment_provision_upload_task.doc_id IS '单据id';

ALTER TABLE financialdb.eg_data_execution_task ALTER COLUMN error_message TYPE text USING error_message::text;


ALTER TABLE financialdb.eg_contract ADD is_service_contract bpchar(1) NULL DEFAULT '0'::bpchar;
COMMENT ON COLUMN financialdb.eg_contract.is_service_contractis_service_contract IS '删除标识(0:否,1:是)';

ALTER TABLE financialdb.eg_contract_his ADD old_org_id varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_contract_his.old_org_id IS '原组织编码';

ALTER TABLE financialdb.eg_contract_status_record ADD process_instance_id int8 NULL;
COMMENT ON COLUMN financialdb.eg_contract_status_record.process_instance_id IS '流程实例id';

-- financialdb.eg_repayment_plan_exceldata definition

-- Drop table

-- DROP TABLE financialdb.eg_repayment_plan_exceldata;

CREATE TABLE financialdb.eg_repayment_plan_exceldata (
	system_code varchar(100) NULL,
	contract_code varchar(100) NULL,
	periods int4 NULL,
	plan_date date NULL,
	actual_repayment_rent_amount numeric(20, 2) NULL, -- 实际归还租金
	actual_repayment_principal_amount numeric(20, 2) NULL, -- 实际归还本金
	actual_repayment_interes_amount numeric(20, 2) NULL, -- 实际归还利息
	ta numeric(20, 2) NULL -- TA金额
);

-- Column comments

COMMENT ON COLUMN financialdb.eg_repayment_plan_exceldata.actual_repayment_rent_amount IS '实际归还租金';
COMMENT ON COLUMN financialdb.eg_repayment_plan_exceldata.actual_repayment_principal_amount IS '实际归还本金';
COMMENT ON COLUMN financialdb.eg_repayment_plan_exceldata.actual_repayment_interes_amount IS '实际归还利息';
COMMENT ON COLUMN financialdb.eg_repayment_plan_exceldata.ta IS 'TA金额';


ALTER TABLE financialdb.eg_impairment_provision ALTER COLUMN error_info TYPE text USING error_info::text;

