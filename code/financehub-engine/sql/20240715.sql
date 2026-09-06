ALTER TABLE eg_interface_data ADD ebank_batch_no varchar(500) NULL;
COMMENT ON COLUMN eg_interface_data.ebank_batch_no IS '银行批次流水号';

--dev uat 已执行