-- 接口表eg_interface_data 新增网银编号字段
--生产已执行 start
ALTER TABLE financialdb.eg_interface_data ADD ebank_number varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_interface_data.ebank_number IS '网银编号';
--生产已执行 end