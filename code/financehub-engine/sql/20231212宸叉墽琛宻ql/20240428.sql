--核销新增创建人姓名
ALTER TABLE financialdb.eg_verification ADD create_name varchar(32) NULL;
COMMENT ON COLUMN financialdb.eg_verification.create_name IS '创建人姓名';

ALTER TABLE financialdb.eg_verification_details ADD create_name varchar(32) NULL;
COMMENT ON COLUMN financialdb.eg_verification_details.create_name IS '创建人姓名';

--前面文档的sql已经再uat 执行，prod还未执行



ALTER TABLE financialdb.eg_recycling_equipment_in_detail ADD error_info varchar(4000) NULL;
COMMENT ON COLUMN financialdb.eg_recycling_equipment_in_detail.error_info IS '生成凭证报错信息';


ALTER TABLE financialdb.eg_recycling_equipment_out_detail ADD error_info varchar(4000) NULL;
COMMENT ON COLUMN financialdb.eg_recycling_equipment_out_detail.error_info IS '生成凭证报错信息';


-- financialdb.eg_impairment_provision_upload_task definition

-- Drop table

-- DROP TABLE financialdb.eg_impairment_provision_upload_task;

CREATE TABLE financialdb.eg_impairment_provision_upload_task (
	id int8 NOT NULL, -- ID
	excel_type varchar(20) NULL, -- 导入的excel类型
	start_time timestamp NULL, -- 开始时间
	end_time timestamp NULL, -- 结束时间
	file_name varchar(100) NULL, -- 导入文件名称
	status varchar(20) DEFAULT 0 NULL, -- 状态，0-开始;1,结束
	user_id int8 NULL, -- 上传用户id
	error_info text NULL, -- 错误信息
	del_flag bpchar(1) DEFAULT 0 NULL, -- 是否删除（0-否，1-是）
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	user_name varchar(100) NULL, -- 用户名
	CONSTRAINT eg_impairment_provision_upload_task_pk PRIMARY KEY (id)
);
COMMENT ON TABLE financialdb.eg_impairment_provision_upload_task IS '减值计提上传任务';

-- Column comments

COMMENT ON COLUMN financialdb.eg_impairment_provision_upload_task.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_impairment_provision_upload_task.excel_type IS '导入的excel类型';
COMMENT ON COLUMN financialdb.eg_impairment_provision_upload_task.start_time IS '开始时间';
COMMENT ON COLUMN financialdb.eg_impairment_provision_upload_task.end_time IS '结束时间';
COMMENT ON COLUMN financialdb.eg_impairment_provision_upload_task.file_name IS '导入文件名称';
COMMENT ON COLUMN financialdb.eg_impairment_provision_upload_task.status IS '状态，0-开始;1,结束';
COMMENT ON COLUMN financialdb.eg_impairment_provision_upload_task.user_id IS '上传用户id';
COMMENT ON COLUMN financialdb.eg_impairment_provision_upload_task.error_info IS '错误信息';
COMMENT ON COLUMN financialdb.eg_impairment_provision_upload_task.del_flag IS '是否删除（0-否，1-是）';
COMMENT ON COLUMN financialdb.eg_impairment_provision_upload_task.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_impairment_provision_upload_task.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_impairment_provision_upload_task.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_impairment_provision_upload_task.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_impairment_provision_upload_task.user_name IS '用户名';

ALTER TABLE financialdb.eg_impairment_provision ALTER COLUMN voucher_id TYPE text USING voucher_id::text;


ALTER TABLE financialdb.eg_voucher_to_eas_record ADD primary_key text NULL;
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.primary_key IS '系统id,多个逗号分隔';