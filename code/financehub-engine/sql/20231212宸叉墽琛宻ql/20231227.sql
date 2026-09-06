--资金业务银行余额表新增字段
ALTER TABLE financialdb.eg_fund_system_balance ADD transaction_type varchar(50) NULL;
COMMENT ON COLUMN financialdb.eg_fund_system_balance.transaction_type IS '交易类型（收款：collection，付款：payment）';
--接口表新增字段 生产已执行 start
ALTER TABLE financialdb.eg_interface_data ADD payable_number varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_interface_data.payable_number IS '业务付款单号';

update eg_interface_data set payable_number = interface_data::jsonb->>'payableNumber';
--接口表新增字段 生产已执行 end
--接口表新增付款单号字段
ALTER TABLE financialdb.eg_interface_data ADD payment_order varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_interface_data.payment_order IS '资金付款单号';
