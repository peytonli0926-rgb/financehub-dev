-- financialdb.eg_rent_register definition

-- Drop table

-- DROP TABLE financialdb.eg_rent_register;

CREATE TABLE financialdb.eg_rent_register (
	id int8 NOT NULL, -- ID
	contract_code varchar(100) NULL, -- 合同编号
	asset_number varchar(100) NOT NULL, -- 资产编号
	transfer_out_date date NULL, -- 转出时间
	client_code varchar(100) NULL, -- 客户编号
	client_name varchar(200) NULL, -- 客户名称
	lease_date_start timestamp NULL, -- 起租日
	lease_date_end timestamp NULL, -- 到期日
	rent_total numeric(20, 2) NULL, -- 租金总额
	rent_bond numeric(20, 2) NULL, -- 租赁保证金
	process_status varchar(64) DEFAULT 1 NULL, -- 处理状态
	process_instance_id int8 NULL, -- 流程实例id
	del_flag bpchar(1) DEFAULT 0 NULL, -- 是否删除（0-否，1-是）
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	CONSTRAINT eg_rent_register_pk PRIMARY KEY (id)
);
COMMENT ON TABLE financialdb.eg_rent_register IS '出租登记';

-- Column comments

COMMENT ON COLUMN financialdb.eg_rent_register.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_rent_register.contract_code IS '合同编号';
COMMENT ON COLUMN financialdb.eg_rent_register.asset_number IS '资产编号';
COMMENT ON COLUMN financialdb.eg_rent_register.transfer_out_date IS '转出时间';
COMMENT ON COLUMN financialdb.eg_rent_register.client_code IS '客户编号';
COMMENT ON COLUMN financialdb.eg_rent_register.client_name IS '客户名称';
COMMENT ON COLUMN financialdb.eg_rent_register.lease_date_start IS '起租日';
COMMENT ON COLUMN financialdb.eg_rent_register.lease_date_end IS '到期日';
COMMENT ON COLUMN financialdb.eg_rent_register.rent_total IS '租金总额';
COMMENT ON COLUMN financialdb.eg_rent_register.rent_bond IS '租赁保证金';
COMMENT ON COLUMN financialdb.eg_rent_register.process_status IS '处理状态';
COMMENT ON COLUMN financialdb.eg_rent_register.process_instance_id IS '流程实例id';
COMMENT ON COLUMN financialdb.eg_rent_register.del_flag IS '是否删除（0-否，1-是）';
COMMENT ON COLUMN financialdb.eg_rent_register.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_rent_register.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_rent_register.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_rent_register.update_time IS '更新时间';

-- financialdb.eg_rent_register_detail definition

-- Drop table

-- DROP TABLE financialdb.eg_rent_register_detail;

CREATE TABLE financialdb.eg_rent_register_detail (
	id int8 NOT NULL, -- ID
	contract_code varchar(100) NULL, -- 合同编号
	plan_date date NULL, -- 应付日期
	"period" varchar(100) NULL, -- 所属期
	receivable_rent numeric(20, 2) NULL, -- 应收租金
	this_month_receivable_rent numeric(20, 2) NULL, -- 当月应收租金
	this_month_tax numeric(20, 2) NULL, -- 当月计提税金
	this_month_rent_income numeric(20, 2) NULL, -- 当月租金收入
	voucher_id varchar(2000) NULL, -- 凭证id(多个逗号分隔)
	del_flag bpchar(1) DEFAULT 0 NULL, -- 是否删除（0-否，1-是）
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	CONSTRAINT eg_rent_register_detail_pk PRIMARY KEY (id)
);
COMMENT ON TABLE financialdb.eg_rent_register_detail IS '出租登记-租金计划';

-- Column comments

COMMENT ON COLUMN financialdb.eg_rent_register_detail.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_rent_register_detail.contract_code IS '合同编号';
COMMENT ON COLUMN financialdb.eg_rent_register_detail.plan_date IS '应付日期';
COMMENT ON COLUMN financialdb.eg_rent_register_detail."period" IS '所属期';
COMMENT ON COLUMN financialdb.eg_rent_register_detail.receivable_rent IS '应收租金';
COMMENT ON COLUMN financialdb.eg_rent_register_detail.this_month_receivable_rent IS '当月应收租金';
COMMENT ON COLUMN financialdb.eg_rent_register_detail.this_month_tax IS '当月计提税金';
COMMENT ON COLUMN financialdb.eg_rent_register_detail.this_month_rent_income IS '当月租金收入';
COMMENT ON COLUMN financialdb.eg_rent_register_detail.voucher_id IS '凭证id(多个逗号分隔)';
COMMENT ON COLUMN financialdb.eg_rent_register_detail.del_flag IS '是否删除（0-否，1-是）';
COMMENT ON COLUMN financialdb.eg_rent_register_detail.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_rent_register_detail.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_rent_register_detail.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_rent_register_detail.update_time IS '更新时间';