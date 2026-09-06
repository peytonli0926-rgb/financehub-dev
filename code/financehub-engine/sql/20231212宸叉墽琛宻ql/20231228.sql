--新增 偿还计划-成本类每月数据
create table if not exists eg_repayment_plan_cost
(
	id int8 not null
		constraint eg_repayment_plan_cost_pkey
			primary key,
	org_id varchar(100),
	contract_code varchar(100),
	account_date varchar(64),
	plan_date date,
	cash_flow numeric(20,2),
	create_by varchar(32),
	create_time timestamp(6),
	update_by varchar(32),
	update_time timestamp(6),
	del_flag bpchar(1) default '0'
);

comment on table eg_repayment_plan_cost is '偿还计划-成本类每月数据';

comment on column eg_repayment_plan_cost.id is 'ID';

comment on column eg_repayment_plan_cost.org_id is '签约主体';

comment on column eg_repayment_plan_cost.contract_code is '合同号';

comment on column eg_repayment_plan_cost.account_date is '账期';

comment on column eg_repayment_plan_cost.plan_date is '计划日期';

comment on column eg_repayment_plan_cost.cash_flow is '成本类本月汇总金额';

comment on column eg_repayment_plan_cost.create_by is '创建人';

comment on column eg_repayment_plan_cost.create_time is '创建时间';

comment on column eg_repayment_plan_cost.update_by is '更新人';

comment on column eg_repayment_plan_cost.update_time is '更新时间';

comment on column eg_repayment_plan_cost.del_flag is '删除标识(0:未删除,1:已删除)';

create index if not exists idx_repayment_plan_cost_contract_code
	on eg_repayment_plan_cost (contract_code);

