-- financialdb.eg_transfer_register definition

-- Drop table

-- DROP TABLE financialdb.eg_transfer_register;

CREATE TABLE financialdb.eg_transfer_register (
	id int8 NOT NULL, -- ID
	asset_number varchar(100) NOT NULL, -- 资产编号
	org_id varchar(100) NULL, -- 签约主体
	account_date timestamp NULL, -- 入账时间
	contract_code varchar(100) NULL, -- 原合同号
	property_address varchar(200) NOT NULL, -- 房产地址
	debt_asset_value numeric(20, 2) NULL, -- 抵债资产入账价值
	process_status varchar(64) DEFAULT 1 NULL, -- 处理状态
	process_instance_id int8 NULL, -- 流程实例id
	del_flag bpchar(1) DEFAULT 0 NULL, -- 是否删除（0-否，1-是）
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	CONSTRAINT eg_transfer_regist_pk PRIMARY KEY (id),
	CONSTRAINT eg_transfer_register_unique UNIQUE (asset_number)
);
COMMENT ON TABLE financialdb.eg_transfer_register IS '转入登记';

-- Column comments

COMMENT ON COLUMN financialdb.eg_transfer_register.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_transfer_register.asset_number IS '资产编号';
COMMENT ON COLUMN financialdb.eg_transfer_register.org_id IS '签约主体';
COMMENT ON COLUMN financialdb.eg_transfer_register.account_date IS '入账时间';
COMMENT ON COLUMN financialdb.eg_transfer_register.contract_code IS '原合同号';
COMMENT ON COLUMN financialdb.eg_transfer_register.property_address IS '房产地址';
COMMENT ON COLUMN financialdb.eg_transfer_register.debt_asset_value IS '抵债资产入账价值';
COMMENT ON COLUMN financialdb.eg_transfer_register.process_status IS '处理状态';
COMMENT ON COLUMN financialdb.eg_transfer_register.process_instance_id IS '流程实例id';
COMMENT ON COLUMN financialdb.eg_transfer_register.del_flag IS '是否删除（0-否，1-是）';
COMMENT ON COLUMN financialdb.eg_transfer_register.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_transfer_register.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_transfer_register.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_transfer_register.update_time IS '更新时间';


-- financialdb.eg_sell_register definition

-- Drop table

-- DROP TABLE financialdb.eg_sell_register;

CREATE TABLE financialdb.eg_sell_register (
	id int8 NOT NULL, -- ID
	asset_number varchar(100) NOT NULL, -- 资产编号
	transfer_out_date timestamp NULL, -- 转出时间
	buy_or_sell_person varchar(100) NULL, -- 买售人
	sell_price numeric(20, 2) NULL, -- 售价
	process_status varchar(64) DEFAULT 1 NULL, -- 处理状态
	process_instance_id int8 NULL, -- 流程实例id
	del_flag bpchar(1) DEFAULT 0 NULL, -- 是否删除（0-否，1-是）
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	CONSTRAINT eg_sell_regist_pk PRIMARY KEY (id)
);
COMMENT ON TABLE financialdb.eg_sell_register IS '出售登记';

-- Column comments

COMMENT ON COLUMN financialdb.eg_sell_register.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_sell_register.asset_number IS '资产编号';
COMMENT ON COLUMN financialdb.eg_sell_register.transfer_out_date IS '转出时间';
COMMENT ON COLUMN financialdb.eg_sell_register.buy_or_sell_person IS '买售人';
COMMENT ON COLUMN financialdb.eg_sell_register.sell_price IS '售价';
COMMENT ON COLUMN financialdb.eg_sell_register.process_status IS '处理状态';
COMMENT ON COLUMN financialdb.eg_sell_register.process_instance_id IS '流程实例id';
COMMENT ON COLUMN financialdb.eg_sell_register.del_flag IS '是否删除（0-否，1-是）';
COMMENT ON COLUMN financialdb.eg_sell_register.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_sell_register.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_sell_register.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_sell_register.update_time IS '更新时间';
