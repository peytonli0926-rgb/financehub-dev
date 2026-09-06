
CREATE TABLE financialdb.eg_batch_query_upload_record (
      id int8 NOT NULL,
      upload_by varchar(32) NULL,
      upload_time timestamp(6) NULL,
      contract_code_list text NULL,
      account_code_list text NULL,
      del_flag bpchar(1) NULL DEFAULT '0'::bpchar,
      create_by varchar(32) NULL,
      create_time timestamp(6) NULL,
      update_by varchar(32) NULL,
      update_time timestamp(6) NULL,
      CONSTRAINT eg_batch_query_upload_record_pk PRIMARY KEY (id)
);

--uat,prod已执行
DROP INDEX financialdb.idx_fund_payment_unique_id;
ALTER TABLE financialdb.eg_fund_payment_data DROP CONSTRAINT eg_fund_payment_data_pk;
ALTER TABLE financialdb.eg_fund_payment_data ALTER COLUMN order_id DROP NOT NULL;

ALTER TABLE financialdb.eg_fund_payment_data ADD CONSTRAINT eg_fund_payment_data_pk PRIMARY KEY (id);
CREATE INDEX eg_fund_payment_data_id_idx ON financialdb.eg_fund_payment_data (id);
--uat,prod已执行

ALTER TABLE financialdb.eg_non_confirm_collection_second_detail ADD upload_file_id int8 NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.upload_file_id IS '文件上传id';

ALTER TABLE financialdb.eg_non_confirm_collection_file_upload_record ADD org_batch_ids varchar(200) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_file_upload_record.org_batch_ids IS '上传的数据按照认领主体分组(分组id)';

