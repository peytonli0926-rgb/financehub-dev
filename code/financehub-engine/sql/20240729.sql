ALTER TABLE eg_hy_full_online_bank_batch_no_mapping ADD client_code varchar(64) NULL;
COMMENT ON COLUMN eg_hy_full_online_bank_batch_no_mapping.client_code IS '客户编号';

ALTER TABLE eg_hy_full_online_bank_batch_no_mapping ADD client_name varchar(64) NULL;
COMMENT ON COLUMN eg_hy_full_online_bank_batch_no_mapping.client_name IS '客户名称';


--以上dev,uat prod已执行