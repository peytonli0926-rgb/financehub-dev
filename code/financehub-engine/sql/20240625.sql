ALTER TABLE financialdb.eg_fund_ebank_transaction_data ADD collection_type varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_fund_ebank_transaction_data.collection_type IS '收款类型';
ALTER TABLE financialdb.eg_fund_payment_data ADD business_date varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_fund_payment_data.business_date IS '业务日期操作付款日期，格式：yyyy-MM-dd HH:mm:ss';


-- financialdb.eg_batch_task definition

-- Drop table

-- DROP TABLE financialdb.eg_batch_task;

CREATE TABLE financialdb.eg_batch_task (
	id int8 NOT NULL, -- ID
	task_type varchar(100) NULL, -- 任务类型
	business_id int8 NULL, -- 业务id
	business_type varchar(100) NULL, -- 业务类型
	data_size int4 NULL, -- 任务处理数据总条数
	data_success_size int4 NULL, -- 成功数据条数
	data_failed_size int4 NULL, -- 失败数据条数
	start_time timestamp NULL, -- 开始时间
	end_time timestamp NULL, -- 结束时间
	status varchar(20) NULL DEFAULT 0, -- 状态 1=处理中，2=成功，3：失败
	user_id int8 NULL, -- 上传用户id
	error_info text NULL, -- 错误信息
	del_flag bpchar(1) NULL DEFAULT 0, -- 是否删除（0-否，1-是）
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	user_name varchar(100) NULL -- 用户名
);
COMMENT ON TABLE financialdb.eg_batch_task IS '批量任务';

-- Column comments

COMMENT ON COLUMN financialdb.eg_batch_task.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_batch_task.task_type IS '任务类型';
COMMENT ON COLUMN financialdb.eg_batch_task.business_id IS '业务id';
COMMENT ON COLUMN financialdb.eg_batch_task.business_type IS '业务类型';
COMMENT ON COLUMN financialdb.eg_batch_task.data_size IS '任务处理数据总条数';
COMMENT ON COLUMN financialdb.eg_batch_task.data_success_size IS '成功数据条数';
COMMENT ON COLUMN financialdb.eg_batch_task.data_failed_size IS '失败数据条数';
COMMENT ON COLUMN financialdb.eg_batch_task.start_time IS '开始时间';
COMMENT ON COLUMN financialdb.eg_batch_task.end_time IS '结束时间';
COMMENT ON COLUMN financialdb.eg_batch_task.status IS '状态 1=处理中，2=成功，3：失败';
COMMENT ON COLUMN financialdb.eg_batch_task.user_id IS '上传用户id';
COMMENT ON COLUMN financialdb.eg_batch_task.error_info IS '错误信息';
COMMENT ON COLUMN financialdb.eg_batch_task.del_flag IS '是否删除（0-否，1-是）';
COMMENT ON COLUMN financialdb.eg_batch_task.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_batch_task.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_batch_task.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_batch_task.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_batch_task.user_name IS '用户名';
CREATE INDEX eg_batch_task_business_id_idx ON financialdb.eg_batch_task (business_id);
CREATE INDEX eg_batch_task_business_type_idx ON financialdb.eg_batch_task (business_type);
CREATE INDEX eg_batch_task_status_idx ON financialdb.eg_batch_task (status);
CREATE INDEX eg_batch_task_task_type_idx ON financialdb.eg_batch_task (task_type);

CREATE INDEX eg_tail_difference_adjustment_detail_account_balance_idx ON financialdb3.eg_tail_difference_adjustment_detail (account_balance);

--以上sql prod已执行