ALTER TABLE financialdb.eg_contract ADD actual_service_amount numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract.actual_service_amount IS '实收服务费';

ALTER TABLE financialdb.eg_interface_data ADD status varchar(50) null  DEFAULT '0'::bpchar;
COMMENT ON COLUMN financialdb.eg_interface_data.status IS '消息状态(0:不需要执行,1:需要执行，2：成功，3：失败)';

ALTER TABLE financialdb.eg_interface_data ADD error_info varchar(2000) NULL;
COMMENT ON COLUMN financialdb.eg_interface_data.error_info IS '异常信息';

--以上prod 已同步
