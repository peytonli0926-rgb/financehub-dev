--生产已执行
--合同表新增字段
ALTER TABLE financialdb.eg_contract ADD payable_service numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_contract.payable_service IS '应付经销商服务费';

--接口表
ALTER TABLE financialdb.eg_interface_data ADD ebank_serial_number varchar(60) NULL;
COMMENT ON COLUMN financialdb.eg_interface_data.ebank_serial_number IS '网银流水号';
----生产已执行



