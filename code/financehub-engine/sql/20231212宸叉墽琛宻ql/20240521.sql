-- financialdb.eg_non_confirm_collection_file_upload_record definition

-- Drop table

-- DROP TABLE financialdb.eg_non_confirm_collection_file_upload_record;

CREATE TABLE financialdb.eg_non_confirm_collection_file_upload_record (
	id int8 NOT NULL, -- ID
	upload_time timestamp NULL, -- 上传事件
	uploader varchar(20) NULL, -- 上传人
	file_name varchar(200) NULL, -- 上传文件名
	total_records int4 NULL, -- 上传记录总数
	error_file_url varchar(2000) NULL, -- 错误文件URL
	create_by varchar(64) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(64) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	del_flag bpchar(1) NULL DEFAULT 0 -- 是否删除（0:否，1：是）
);

-- Column comments

COMMENT ON COLUMN financialdb.eg_non_confirm_collection_file_upload_record.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_file_upload_record.upload_time IS '上传事件';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_file_upload_record.uploader IS '上传人';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_file_upload_record.file_name IS '上传文件名';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_file_upload_record.total_records IS '上传记录总数';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_file_upload_record.error_file_url IS '错误文件URL';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_file_upload_record.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_file_upload_record.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_file_upload_record.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_file_upload_record.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_file_upload_record.del_flag IS '是否删除（0:否，1：是）';



ALTER TABLE financialdb.eg_fund_payment_data ADD segmented_bill_type varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_fund_payment_data.segmented_bill_type IS '细分票据类型(自开票据/票据背书)';