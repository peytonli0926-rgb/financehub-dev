--收益计提详情新增字段
alter table eg_lease_income_details
    add rental_income_on_balance numeric(20,2);
comment on column eg_lease_income_details.rental_income_on_balance is '表内租赁收入';
alter table eg_lease_income_details
    add rental_income_off_balance numeric(20,2);
comment on column eg_lease_income_details.rental_income_off_balance is '表外租赁收入';

--成本类新增费用大类
ALTER TABLE financialdb.eg_cost_channel_fee ADD expense_main_category_type varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_cost_channel_fee.expense_main_category_type IS '费用大类(1:GPS,2:手环设备款,3:经销商服务费、外部渠道费、海通渠道费,4:收车费、抵押费、解抵押费)';
