ALTER TABLE eg_fund_business_system_ebank_amount_mapping ADD message_status varchar(50) NULL;
COMMENT ON COLUMN eg_fund_business_system_ebank_amount_mapping.message_status IS '消息状态（NOT_EXECUTE:未执行,RUNNING:进行中,SUCCESS:成功,FAILED:失败）';

ALTER TABLE eg_fund_business_system_ebank_amount_mapping ADD error_info text NULL;
COMMENT ON COLUMN eg_fund_business_system_ebank_amount_mapping.error_info IS '异常信息';

ALTER TABLE eg_fund_business_system_ebank_wy_amount ADD collection_accounts_bank_no varchar(64) NULL;
COMMENT ON COLUMN eg_fund_business_system_ebank_wy_amount.collection_accounts_bank_no IS '收款账号（虚拟户）';
ALTER TABLE eg_fund_business_system_ebank_wy_amount ADD collection_accounts_bank varchar(64) NULL;
COMMENT ON COLUMN eg_fund_business_system_ebank_wy_amount.collection_accounts_bank IS '收款开户行';
ALTER TABLE eg_fund_business_system_ebank_wy_amount ADD client_code varchar(64) NULL;
COMMENT ON COLUMN eg_fund_business_system_ebank_wy_amount.client_code IS '客户编号';
ALTER TABLE eg_fund_business_system_ebank_wy_amount ADD client_name varchar(64) NULL;
COMMENT ON COLUMN eg_fund_business_system_ebank_wy_amount.client_name IS '客户名称';


ALTER TABLE eg_fund_business_system_ebank_mapping ADD message_status varchar(50) NULL;
COMMENT ON COLUMN eg_fund_business_system_ebank_mapping.message_status IS '消息状态（NOT_EXECUTE:未执行,RUNNING:进行中,SUCCESS:成功,FAILED:失败）';

ALTER TABLE eg_fund_business_system_ebank_mapping ADD error_info text NULL;
COMMENT ON COLUMN eg_fund_business_system_ebank_mapping.error_info IS '异常信息';