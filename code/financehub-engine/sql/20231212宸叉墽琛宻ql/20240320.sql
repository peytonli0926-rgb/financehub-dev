--凭证表新增字段
ALTER TABLE financialdb.eg_voucher_entry ADD edit_flag bpchar NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_voucher_entry.edit_flag IS '是否可以编辑(0:否，1：是)';

-- financialdb.eg_hy_full_online_bank_batch_no_mapping definition

-- Drop table

-- DROP TABLE financialdb.eg_hy_full_online_bank_batch_no_mapping;

CREATE TABLE financialdb.eg_hy_full_online_bank_batch_no_mapping (
	id int8 NOT NULL, -- ID
	online_bank_no varchar(50) NULL, -- 业务系统网银编号
	deduct_batch_no varchar(50) NULL, -- 批扣流水号
	create_by varchar(64) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(64) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	del_flag bpchar(1) NULL DEFAULT 0, -- 是否删除（0:否，1：是）
	deduct_category varchar(1) NULL, -- 扣除种类:1手动汇款、2自动扣款
	business_line varchar(20) NULL, -- 乘用、商用
	bank_receipt_no varchar(50) NULL, -- 银行回单号
	collect_amount_time timestamp NULL, -- 到账时间
	collect_amount numeric(20, 2) NULL, -- 到账金额
	collect_bank varchar(100) NULL, -- 到账银行
	collect_bank_account varchar(50) NULL, -- 到账银行账号
	"comments" varchar(2000) NULL, -- 摘要
	pay_bank_account_name varchar(50) NULL, -- 付款账户
	pay_bank varchar(100) NULL, -- 付款银行
	pay_bank_account varchar(50) NULL, -- 付款银行账户
	can_charge_off_amount numeric(20, 2) NULL, -- 可核销金额
	already_charge_off_amount numeric(20, 2) NULL, -- 已核销金额
	dec_dongjje numeric(20, 2) NULL, -- 已认领溢存款待核销金额。认领到溢存款时增加，溢存款出账时减少
	refund_amount numeric(20, 2) NULL, -- 退款金额
	collect_amount_account_name varchar(50) NULL, -- 到账账户
	refund_flag varchar(10) NULL, -- 退款标志
	online_bank_belong varchar(20) NULL -- 网银归属 01-C0001:恒信 30001:自贸区
);

-- Column comments

COMMENT ON COLUMN financialdb.eg_hy_full_online_bank_batch_no_mapping.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_hy_full_online_bank_batch_no_mapping.online_bank_no IS '业务系统网银编号';
COMMENT ON COLUMN financialdb.eg_hy_full_online_bank_batch_no_mapping.deduct_batch_no IS '批扣流水号';
COMMENT ON COLUMN financialdb.eg_hy_full_online_bank_batch_no_mapping.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_hy_full_online_bank_batch_no_mapping.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_hy_full_online_bank_batch_no_mapping.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_hy_full_online_bank_batch_no_mapping.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_hy_full_online_bank_batch_no_mapping.del_flag IS '是否删除（0:否，1：是）';
COMMENT ON COLUMN financialdb.eg_hy_full_online_bank_batch_no_mapping.deduct_category IS '扣除种类:1手动汇款、2自动扣款';
COMMENT ON COLUMN financialdb.eg_hy_full_online_bank_batch_no_mapping.business_line IS '乘用、商用';
COMMENT ON COLUMN financialdb.eg_hy_full_online_bank_batch_no_mapping.bank_receipt_no IS '银行回单号';
COMMENT ON COLUMN financialdb.eg_hy_full_online_bank_batch_no_mapping.collect_amount_time IS '到账时间';
COMMENT ON COLUMN financialdb.eg_hy_full_online_bank_batch_no_mapping.collect_amount IS '到账金额';
COMMENT ON COLUMN financialdb.eg_hy_full_online_bank_batch_no_mapping.collect_bank IS '到账银行';
COMMENT ON COLUMN financialdb.eg_hy_full_online_bank_batch_no_mapping.collect_bank_account IS '到账银行账号';
COMMENT ON COLUMN financialdb.eg_hy_full_online_bank_batch_no_mapping."comments" IS '摘要';
COMMENT ON COLUMN financialdb.eg_hy_full_online_bank_batch_no_mapping.pay_bank_account_name IS '付款账户';
COMMENT ON COLUMN financialdb.eg_hy_full_online_bank_batch_no_mapping.pay_bank IS '付款银行';
COMMENT ON COLUMN financialdb.eg_hy_full_online_bank_batch_no_mapping.pay_bank_account IS '付款银行账户';
COMMENT ON COLUMN financialdb.eg_hy_full_online_bank_batch_no_mapping.can_charge_off_amount IS '可核销金额';
COMMENT ON COLUMN financialdb.eg_hy_full_online_bank_batch_no_mapping.already_charge_off_amount IS '已核销金额';
COMMENT ON COLUMN financialdb.eg_hy_full_online_bank_batch_no_mapping.dec_dongjje IS '已认领溢存款待核销金额。认领到溢存款时增加，溢存款出账时减少';
COMMENT ON COLUMN financialdb.eg_hy_full_online_bank_batch_no_mapping.refund_amount IS '退款金额';
COMMENT ON COLUMN financialdb.eg_hy_full_online_bank_batch_no_mapping.collect_amount_account_name IS '到账账户';
COMMENT ON COLUMN financialdb.eg_hy_full_online_bank_batch_no_mapping.refund_flag IS '退款标志';
COMMENT ON COLUMN financialdb.eg_hy_full_online_bank_batch_no_mapping.online_bank_belong IS '网银归属 01-C0001:恒信 30001:自贸区';


-- financialdb.eg_pay_vat definition

-- Drop table

-- DROP TABLE financialdb.eg_pay_vat;

CREATE TABLE financialdb.eg_pay_vat (
	id int8 NOT NULL, -- ID
	contract_code varchar(100) NULL, -- 合同编号
	contract_type varchar(20) NULL, -- 合同类型
	client_code varchar(100) NULL, -- 客户编号
	client_name varchar(200) NULL, -- 客户名称
	org_id varchar(100) NULL, -- 开票主体
	contract_code_m varchar(50) NULL, -- 租赁合同编号
	process_status varchar(64) NULL DEFAULT 1, -- 处理状态
	is_generate_voucher bpchar(1) NULL DEFAULT 0, -- 是否已生成凭证(0-否，1-是)
	process_instance_id int8 NULL, -- 流程实例id
	del_flag bpchar(1) NULL DEFAULT 0, -- 是否删除（0-否，1-是）
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL -- 更新时间
);
CREATE INDEX eg_pay_vat_contract_code_idx ON financialdb.eg_pay_vat USING btree (contract_code, org_id);
CREATE UNIQUE INDEX eg_pay_vat_pk ON financialdb.eg_pay_vat USING btree (id);
COMMENT ON TABLE financialdb.eg_pay_vat IS '应交增值税';

-- Column comments

COMMENT ON COLUMN financialdb.eg_pay_vat.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_pay_vat.contract_code IS '合同编号';
COMMENT ON COLUMN financialdb.eg_pay_vat.contract_type IS '合同类型';
COMMENT ON COLUMN financialdb.eg_pay_vat.client_code IS '客户编号';
COMMENT ON COLUMN financialdb.eg_pay_vat.client_name IS '客户名称';
COMMENT ON COLUMN financialdb.eg_pay_vat.org_id IS '开票主体';
COMMENT ON COLUMN financialdb.eg_pay_vat.contract_code_m IS '租赁合同编号';
COMMENT ON COLUMN financialdb.eg_pay_vat.process_status IS '处理状态';
COMMENT ON COLUMN financialdb.eg_pay_vat.is_generate_voucher IS '是否已生成凭证(0-否，1-是)';
COMMENT ON COLUMN financialdb.eg_pay_vat.process_instance_id IS '流程实例id';
COMMENT ON COLUMN financialdb.eg_pay_vat.del_flag IS '是否删除（0-否，1-是）';
COMMENT ON COLUMN financialdb.eg_pay_vat.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_pay_vat.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_pay_vat.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_pay_vat.update_time IS '更新时间';