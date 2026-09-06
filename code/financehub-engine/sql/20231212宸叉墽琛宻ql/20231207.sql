--生产已执行 start
-- 新增字段
alter table eg_lease_income_details
	add observed bpchar(1);
comment on column eg_lease_income_details.observed is '是否观察期';
alter table eg_lease_income_details
	add observed_expiration_date timestamp(6);
comment on column eg_lease_income_details.observed_expiration_date is '观察期到期日';
alter table eg_lease_income_details
	add comment varchar(500);
comment on column eg_lease_income_details.comment is '备注';
alter table eg_lease_income_details
	add process_method varchar(100);
comment on column eg_lease_income_details.process_method is '处理方式';
alter table eg_lease_income_details
	add labor_overdue_days int4;
comment on column eg_lease_income_details.labor_overdue_days is '变更日期';
alter table eg_lease_income_details
	add labor_overdue_mark varchar(100);
comment on column eg_lease_income_details.labor_overdue_mark is '手工逾期标识';
--生产已执行 end