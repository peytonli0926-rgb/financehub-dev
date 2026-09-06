ALTER TABLE financialdb.eg_non_confirm_collection_second_detail ADD process_status varchar(50) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.process_status IS '处理状态';
ALTER TABLE financialdb.eg_non_confirm_collection_sum ADD business_date timestamp NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_sum.business_date IS '网银到账日期';

ALTER TABLE financialdb.eg_non_confirm_collection_second_detail ADD approve_id int8 NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.approve_id IS '审核ID';
ALTER TABLE financialdb.eg_non_confirm_collection_second_detail ADD manual_voucher_id int8 NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.manual_voucher_id IS '手工凭证id';

ALTER TABLE financialdb.eg_business_claim_repayment_record ADD non_confirm_second_detail_id int8 NULL;
COMMENT ON COLUMN financialdb.eg_business_claim_repayment_record.non_confirm_second_detail_id IS '未确认收款明细表Id';

ALTER TABLE financialdb.eg_non_confirm_collection_second_detail ADD create_by varchar(64) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.create_by IS '创建人';
ALTER TABLE financialdb.eg_non_confirm_collection_second_detail ADD create_time timestamp NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.create_time IS '创建时间';
ALTER TABLE financialdb.eg_non_confirm_collection_second_detail ADD update_by varchar(64) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.update_by IS '更新人';
ALTER TABLE financialdb.eg_non_confirm_collection_second_detail ADD update_time timestamp NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.update_time IS '更新时间';
ALTER TABLE financialdb.eg_non_confirm_collection_second_detail ADD del_flag bpchar(1) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.del_flag IS '是否删除（0:否，1：是）';

ALTER TABLE financialdb.eg_non_confirm_collection_second_detail ADD write_off_voucher_id int8 NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.write_off_voucher_id IS '核销回款凭证id';


-- financialdb.eg_impairment_provision definition

-- Drop table

-- DROP TABLE financialdb.eg_impairment_provision;

CREATE TABLE financialdb.eg_impairment_provision (
	id int8 NOT NULL, -- ID
	impairment_type varchar(100) NULL, -- 减值类型
	provision_total numeric(20, 2) NULL, -- 拨备合计
	last_month_balance numeric(20, 2) NULL, -- 上月余额
	this_month_provision numeric(20, 2) NULL, -- 本月计提
	process_status varchar(64) DEFAULT 1 NULL, -- 处理状态
	is_generate_voucher bpchar(1) DEFAULT 0 NULL, -- 是否已生成凭证(0-否，1-是)
	process_instance_id int8 NULL, -- 流程实例id
	voucher_id varchar(2000) NULL, -- 凭证id,多个按照逗号分隔
	error_info varchar(2000) NULL, -- 生成凭证报错信息
	account_date timestamp NULL, -- 财务日期
	del_flag bpchar(1) DEFAULT 0 NULL, -- 是否删除（0-否，1-是）
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	CONSTRAINT eg_impairment_provision_pk PRIMARY KEY (id)
);
COMMENT ON TABLE financialdb.eg_impairment_provision IS '减值计提';

-- Column comments

COMMENT ON COLUMN financialdb.eg_impairment_provision.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_impairment_provision.impairment_type IS '减值类型';
COMMENT ON COLUMN financialdb.eg_impairment_provision.provision_total IS '拨备合计';
COMMENT ON COLUMN financialdb.eg_impairment_provision.last_month_balance IS '上月余额';
COMMENT ON COLUMN financialdb.eg_impairment_provision.this_month_provision IS '本月计提';
COMMENT ON COLUMN financialdb.eg_impairment_provision.process_status IS '处理状态';
COMMENT ON COLUMN financialdb.eg_impairment_provision.is_generate_voucher IS '是否已生成凭证(0-否，1-是)';
COMMENT ON COLUMN financialdb.eg_impairment_provision.process_instance_id IS '流程实例id';
COMMENT ON COLUMN financialdb.eg_impairment_provision.voucher_id IS '凭证id,多个按照逗号分隔';
COMMENT ON COLUMN financialdb.eg_impairment_provision.error_info IS '生成凭证报错信息';
COMMENT ON COLUMN financialdb.eg_impairment_provision.account_date IS '财务日期';
COMMENT ON COLUMN financialdb.eg_impairment_provision.del_flag IS '是否删除（0-否，1-是）';
COMMENT ON COLUMN financialdb.eg_impairment_provision.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_impairment_provision.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_impairment_provision.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_impairment_provision.update_time IS '更新时间';


-- financialdb.eg_impairment_provision_detail definition

-- Drop table

-- DROP TABLE financialdb.eg_impairment_provision_detail;

CREATE TABLE financialdb.eg_impairment_provision_detail (
	id int8 NOT NULL, -- ID
	contract_code varchar(100) NULL, -- 合同编号(核算项目)
	impairment_type varchar(100) NULL, -- 减值类型
	business_type varchar(100) NULL, -- 业务类型
	org_id varchar(100) NULL, -- 签约主体
	five_class varchar(100) NULL, -- 五级分类
	three_step varchar(100) NULL, -- 三阶段
	risk_exposure numeric(20, 2) NULL, -- 风险敞口
	provision_total numeric(20, 2) NULL, -- 拨备合计
	last_month_balance numeric(20, 2) NULL, -- 上月余额
	this_month_provision numeric(20, 2) NULL, -- 本月计提
	voucher_id varchar(2000) NULL, -- 凭证id(多个逗号分隔)
	account_date timestamp NULL, -- 财务日期
	del_flag bpchar(1) DEFAULT 0 NULL, -- 是否删除（0-否，1-是）
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	is_verification varchar(10) NULL, -- 是否核销
	rent_receivable_balance numeric(20, 2) NULL, -- 应收租金余额
	hub_rent_receivable_balance numeric(20, 2) NULL, -- 中台应收租金余额
	client_code varchar(100) NULL, -- 客户编号
	client_name varchar(200) NULL, -- 客户名称
	contract_name varchar(200) NULL, -- 合同名称
	excel_type varchar NULL, -- 导入的excel类型
	error_info varchar(2000) NULL, -- 生成凭证报错信息
	CONSTRAINT eg_impairment_provision_detail_pk PRIMARY KEY (id)
);
COMMENT ON TABLE financialdb.eg_impairment_provision_detail IS '减值计提明细';

-- Column comments

COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.contract_code IS '合同编号(核算项目)';
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.impairment_type IS '减值类型';
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.business_type IS '业务类型';
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.org_id IS '签约主体';
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.five_class IS '五级分类';
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.three_step IS '三阶段';
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.risk_exposure IS '风险敞口';
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.provision_total IS '拨备合计';
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.last_month_balance IS '上月余额';
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.this_month_provision IS '本月计提';
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.voucher_id IS '凭证id(多个逗号分隔)';
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.account_date IS '财务日期';
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.del_flag IS '是否删除（0-否，1-是）';
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.is_verification IS '是否核销';
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.rent_receivable_balance IS '应收租金余额';
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.hub_rent_receivable_balance IS '中台应收租金余额';
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.client_code IS '客户编号';
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.client_name IS '客户名称';
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.contract_name IS '合同名称';
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.excel_type IS '导入的excel类型';
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.error_info IS '生成凭证报错信息';