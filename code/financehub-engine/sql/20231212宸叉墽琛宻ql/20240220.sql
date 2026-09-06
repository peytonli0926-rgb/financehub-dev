--已执行，20240226
--接口表新增字段
ALTER TABLE financialdb.eg_interface_data ADD interface_id int8 NULL;
COMMENT ON COLUMN financialdb.eg_interface_data.interface_id IS '外部接口数据id';


ALTER TABLE financialdb.eg_lease_income_details ALTER COLUMN voucher_id TYPE varchar(2000) USING voucher_id::varchar;
