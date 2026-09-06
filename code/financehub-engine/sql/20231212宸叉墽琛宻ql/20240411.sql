-- financialdb.eg_rent_income_confirm definition

-- Drop table

-- DROP TABLE financialdb.eg_rent_income_confirm;

CREATE TABLE financialdb.eg_rent_income_confirm (
	id int8 NOT NULL, -- ID
	contract_code varchar(100) NULL, -- 合同编号
	client_code varchar(100) NULL, -- 客户编号
	client_name varchar(200) NULL, -- 客户名称
	account_month date NULL, -- 记账月份
	this_month_receivable_rent numeric(20, 2) NULL, -- 当月应收租金
	this_month_tax numeric(20, 2) NULL, -- 当月计提税金
	this_month_rent_income numeric(20, 2) NULL, -- 确认收入金额
	process_status varchar(64) DEFAULT 1 NULL, -- 处理状态
	process_instance_id int8 NULL, -- 流程实例id
	voucher_id varchar(2000) NULL, -- 凭证id(多个逗号分隔)
	account_date timestamp NULL, -- 财务日期
	error_info varchar(2000) NULL, -- 生成凭证报错信息
	del_flag bpchar(1) DEFAULT 0 NULL, -- 是否删除（0-否，1-是）
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	org_id varchar(100) NULL, -- 签约主体
	scene_code varchar(100) NULL, -- 凭证场景（多个逗号分隔）
	voucher_id_dzzcsr varchar(1000) NULL, -- 抵债资产收入凭证id
	voucher_id_dzzcjz varchar(1000) NULL, -- 抵债资产结转凭证id
	is_generate_voucher bpchar(1) DEFAULT 0 NULL, -- 是否已生成凭证(0-否，1-是)
	CONSTRAINT eg_rent_income_confirm_pk PRIMARY KEY (id)
);
COMMENT ON TABLE financialdb.eg_rent_income_confirm IS '租金收入确认';

-- Column comments

COMMENT ON COLUMN financialdb.eg_rent_income_confirm.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_rent_income_confirm.contract_code IS '合同编号';
COMMENT ON COLUMN financialdb.eg_rent_income_confirm.client_code IS '客户编号';
COMMENT ON COLUMN financialdb.eg_rent_income_confirm.client_name IS '客户名称';
COMMENT ON COLUMN financialdb.eg_rent_income_confirm.account_month IS '记账月份';
COMMENT ON COLUMN financialdb.eg_rent_income_confirm.this_month_receivable_rent IS '当月应收租金';
COMMENT ON COLUMN financialdb.eg_rent_income_confirm.this_month_tax IS '当月计提税金';
COMMENT ON COLUMN financialdb.eg_rent_income_confirm.this_month_rent_income IS '确认收入金额';
COMMENT ON COLUMN financialdb.eg_rent_income_confirm.process_status IS '处理状态';
COMMENT ON COLUMN financialdb.eg_rent_income_confirm.process_instance_id IS '流程实例id';
COMMENT ON COLUMN financialdb.eg_rent_income_confirm.voucher_id IS '凭证id(多个逗号分隔)';
COMMENT ON COLUMN financialdb.eg_rent_income_confirm.account_date IS '财务日期';
COMMENT ON COLUMN financialdb.eg_rent_income_confirm.error_info IS '生成凭证报错信息';
COMMENT ON COLUMN financialdb.eg_rent_income_confirm.del_flag IS '是否删除（0-否，1-是）';
COMMENT ON COLUMN financialdb.eg_rent_income_confirm.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_rent_income_confirm.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_rent_income_confirm.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_rent_income_confirm.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_rent_income_confirm.org_id IS '签约主体';
COMMENT ON COLUMN financialdb.eg_rent_income_confirm.scene_code IS '凭证场景（多个逗号分隔）';
COMMENT ON COLUMN financialdb.eg_rent_income_confirm.voucher_id_dzzcsr IS '抵债资产收入凭证id';
COMMENT ON COLUMN financialdb.eg_rent_income_confirm.voucher_id_dzzcjz IS '抵债资产结转凭证id';
COMMENT ON COLUMN financialdb.eg_rent_income_confirm.is_generate_voucher IS '是否已生成凭证(0-否，1-是)';

--结果表增加约束
ALTER TABLE financialdb.eg_send_eas2_result ADD CONSTRAINT eg_send_eas2_result_pk PRIMARY KEY (id);
--修改记录表字段类型
ALTER TABLE financialdb.eg_voucher_to_eas_record ALTER COLUMN fid TYPE text USING fid::text;

ALTER TABLE financialdb.eg_send_eas2_result ADD result_key varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_send_eas2_result.result_key IS '返回值key';

ALTER TABLE financialdb.eg_non_confirm_collection_sum ADD bank_summary varchar(1000) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_sum.bank_summary IS '银行交易摘要';
ALTER TABLE financialdb.eg_non_confirm_collection_sum ADD "comment" varchar(500) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_sum."comment" IS '备注';
ALTER TABLE financialdb.eg_non_confirm_collection_sum ADD client_accounts_bank_no varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_sum.client_accounts_bank_no IS '对方客户银行账号';


-- financialdb.eg_parity_transfer definition

-- Drop table

-- DROP TABLE financialdb.eg_parity_transfer;

CREATE TABLE financialdb.eg_parity_transfer (
	id int8 NOT NULL, -- ID
	batch varchar(100) NOT NULL, -- 批次
	transfer_party varchar(100) NOT NULL, -- 转让方
	business_date timestamp NULL, -- 业务日期
	account_date timestamp NULL, -- 记账日期
	transferee_party varchar(100) NULL, -- 受让方
	reference_date timestamp NULL, -- 基准日
	trade_date timestamp NULL, -- 交易日
	transfer_price numeric(20, 2) NULL, -- 转让价格
	contract_num int4 NULL, -- 合同数量
	is_invoice_flag bpchar NULL DEFAULT 0, -- 转让后是否开发票（0：否，1：是）
	process_instance_id int8 NULL, -- 流程id
	process_status varchar(100) NULL, -- 1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝
	is_generate_voucher bpchar(1) NULL DEFAULT '0'::bpchar, -- 是否已生成凭证（0：未生成1：已生成）默认0
	period_code int4 NULL, -- 会计期间
	approve_error_info text NULL, -- 审批报错信息
	del_flag bpchar(1) NULL DEFAULT '0'::bpchar, -- 是否删除（0：未删除1：删除）默认0
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	CONSTRAINT eg_parity_transfer_pk PRIMARY KEY (id)
);
CREATE INDEX eg_parity_transfer_id_idx ON financialdb.eg_parity_transfer USING btree (id, batch, transfer_party);
COMMENT ON TABLE financialdb.eg_parity_transfer IS '平价转让';

-- Column comments

COMMENT ON COLUMN financialdb.eg_parity_transfer.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_parity_transfer.batch IS '批次';
COMMENT ON COLUMN financialdb.eg_parity_transfer.transfer_party IS '转让方';
COMMENT ON COLUMN financialdb.eg_parity_transfer.business_date IS '业务日期';
COMMENT ON COLUMN financialdb.eg_parity_transfer.account_date IS '记账日期';
COMMENT ON COLUMN financialdb.eg_parity_transfer.transferee_party IS '受让方';
COMMENT ON COLUMN financialdb.eg_parity_transfer.reference_date IS '基准日';
COMMENT ON COLUMN financialdb.eg_parity_transfer.trade_date IS '交易日';
COMMENT ON COLUMN financialdb.eg_parity_transfer.transfer_price IS '转让价格';
COMMENT ON COLUMN financialdb.eg_parity_transfer.contract_num IS '合同数量';
COMMENT ON COLUMN financialdb.eg_parity_transfer.is_invoice_flag IS '转让后是否开发票（0：否，1：是）';
COMMENT ON COLUMN financialdb.eg_parity_transfer.process_instance_id IS '流程id';
COMMENT ON COLUMN financialdb.eg_parity_transfer.process_status IS '1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝';
COMMENT ON COLUMN financialdb.eg_parity_transfer.is_generate_voucher IS '是否已生成凭证（0：未生成1：已生成）默认0';
COMMENT ON COLUMN financialdb.eg_parity_transfer.period_code IS '会计期间';
COMMENT ON COLUMN financialdb.eg_parity_transfer.approve_error_info IS '审批报错信息';
COMMENT ON COLUMN financialdb.eg_parity_transfer.del_flag IS '是否删除（0：未删除1：删除）默认0';
COMMENT ON COLUMN financialdb.eg_parity_transfer.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_parity_transfer.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_parity_transfer.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_parity_transfer.update_time IS '更新时间';


-- financialdb.eg_parity_transfer_detail definition

-- Drop table

-- DROP TABLE financialdb.eg_parity_transfer_detail;

CREATE TABLE financialdb.eg_parity_transfer_detail (
	id int8 NOT NULL, -- ID
	parity_transfer_id int8 NOT NULL, -- 平价转让id
	batch varchar(100) NOT NULL, -- 转让批次
	contract_code varchar(100) NULL, -- 合同编码
	client_code varchar(100) NULL, -- 客户编码
	client_name varchar(100) NULL, -- 客户名称
	org_id varchar(100) NULL, -- 签约主体
	financial_contract_status varchar(100) NULL, -- 财务合同状态
	tax_rate varchar(100) NULL, -- 税率
	receivable_rent numeric(20, 2) NULL DEFAULT 0, -- 应收租金余额
	receivable_residual_value numeric(20, 2) NULL DEFAULT 0, -- 应收期末残值余额
	receivable_outtax numeric(20, 2) NULL DEFAULT 0, -- 应收销项税余额
	unrealized_revenue numeric(20, 2) NULL DEFAULT 0, -- 未实现收益余额
	lessee_margin numeric(20, 2) NULL DEFAULT 0, -- 承租人保证金余额
	payable_agency_estimate numeric(20, 2) NULL DEFAULT 0, -- 应付经销商服务费-暂估余额
	payable_vehicle_estimate numeric(20, 2) NULL DEFAULT 0, -- 应付收车费-暂估余额
	payable_band_cost_estimate numeric(20, 2) NULL DEFAULT 0, -- 应付手环成本_暂估余额
	payable_pledge_estimate numeric(20, 2) NULL DEFAULT 0, -- 应付抵押费_暂估余额
	payable_unpledge_estimate numeric(20, 2) NULL DEFAULT 0, -- 应付解抵押费_暂估余额
	payable_other_cost_estimate numeric(20, 2) NULL DEFAULT 0, -- 应付其他租赁成本-暂估余额
	depreciation_reserves numeric(20, 2) NULL DEFAULT 0, -- 减值准备余额-应收租赁款组合拨备
	appraised_value numeric(20, 2) NULL DEFAULT 0, -- 评估价
	voucher_ids text NULL, -- 凭证id,多个按照逗号分隔
	del_flag bpchar(1) NULL DEFAULT '0'::bpchar, -- 是否删除（0：未删除1：删除）默认0
	error_info text NULL, -- 报错信息
	period_code int4 NULL, -- 会计期间
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	CONSTRAINT eg_parity_transfer_detail_pk PRIMARY KEY (id)
);
CREATE INDEX eg_parity_transfer_detail_parity_transfer_id_idx ON financialdb.eg_parity_transfer_detail USING btree (parity_transfer_id, batch, contract_code);
COMMENT ON TABLE financialdb.eg_parity_transfer_detail IS '平价转让详情';

-- Column comments

COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.parity_transfer_id IS '平价转让id';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.batch IS '转让批次';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.contract_code IS '合同编码';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.client_code IS '客户编码';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.client_name IS '客户名称';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.org_id IS '签约主体';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.financial_contract_status IS '财务合同状态';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.tax_rate IS '税率';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.receivable_rent IS '应收租金余额';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.receivable_residual_value IS '应收期末残值余额';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.receivable_outtax IS '应收销项税余额';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.unrealized_revenue IS '未实现收益余额';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.lessee_margin IS '承租人保证金余额';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.payable_agency_estimate IS '应付经销商服务费-暂估余额';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.payable_vehicle_estimate IS '应付收车费-暂估余额';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.payable_band_cost_estimate IS '应付手环成本_暂估余额';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.payable_pledge_estimate IS '应付抵押费_暂估余额';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.payable_unpledge_estimate IS '应付解抵押费_暂估余额';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.payable_other_cost_estimate IS '应付其他租赁成本-暂估余额';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.depreciation_reserves IS '减值准备余额-应收租赁款组合拨备';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.appraised_value IS '评估价';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.voucher_ids IS '凭证id,多个按照逗号分隔';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.del_flag IS '是否删除（0：未删除1：删除）默认0';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.error_info IS '报错信息';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.period_code IS '会计期间';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_parity_transfer_detail.update_time IS '更新时间';


-- financialdb.eg_out_table_abs definition

-- Drop table

-- DROP TABLE financialdb.eg_out_table_abs;

CREATE TABLE financialdb.eg_out_table_abs (
	id int8 NOT NULL, -- ID
	loan_contract_code varchar(100) NULL, -- 借款合同编号
	business_date timestamp NULL, -- 业务日期
	account_date timestamp NULL, -- 记账日期
	periods varchar(100) NULL, -- 期数
	administrator varchar(100) NULL, -- 管理人
	close_date timestamp NULL, -- 封包日
	release_date timestamp NULL, -- 发行日
	transfer_price numeric(20, 2) NULL, -- 转让价格
	contract_num int4 NULL, -- 合同数量
	calculation_period varchar(100) NULL, -- 计算周期
	transfer_period varchar(100) NULL, -- 转付周期
	cash_period varchar(100) NULL, -- 兑付周期
	process_instance_id int8 NULL, -- 流程id
	process_status varchar(100) NULL, -- 1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝
	is_generate_voucher bpchar(1) NULL DEFAULT '0'::bpchar, -- 是否已生成凭证（0：未生成1：已生成）默认0
	approve_error_info text NULL, -- 审批报错信息
	del_flag bpchar(1) NULL DEFAULT '0'::bpchar, -- 是否删除（0：未删除1：删除）默认0
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	period_code int4 NULL, -- 会计期间
	CONSTRAINT eg_out_table_abs_pk PRIMARY KEY (id)
);
CREATE INDEX eg_out_table_abs_loan_contract_code_idx ON financialdb.eg_out_table_abs USING btree (loan_contract_code, business_date, account_date, periods);
COMMENT ON TABLE financialdb.eg_out_table_abs IS '出表ABS';

-- Column comments

COMMENT ON COLUMN financialdb.eg_out_table_abs.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_out_table_abs.loan_contract_code IS '借款合同编号';
COMMENT ON COLUMN financialdb.eg_out_table_abs.business_date IS '业务日期';
COMMENT ON COLUMN financialdb.eg_out_table_abs.account_date IS '记账日期';
COMMENT ON COLUMN financialdb.eg_out_table_abs.periods IS '期数';
COMMENT ON COLUMN financialdb.eg_out_table_abs.administrator IS '管理人';
COMMENT ON COLUMN financialdb.eg_out_table_abs.close_date IS '封包日';
COMMENT ON COLUMN financialdb.eg_out_table_abs.release_date IS '发行日';
COMMENT ON COLUMN financialdb.eg_out_table_abs.transfer_price IS '转让价格';
COMMENT ON COLUMN financialdb.eg_out_table_abs.contract_num IS '合同数量';
COMMENT ON COLUMN financialdb.eg_out_table_abs.calculation_period IS '计算周期';
COMMENT ON COLUMN financialdb.eg_out_table_abs.transfer_period IS '转付周期';
COMMENT ON COLUMN financialdb.eg_out_table_abs.cash_period IS '兑付周期';
COMMENT ON COLUMN financialdb.eg_out_table_abs.process_instance_id IS '流程id';
COMMENT ON COLUMN financialdb.eg_out_table_abs.process_status IS '1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝';
COMMENT ON COLUMN financialdb.eg_out_table_abs.is_generate_voucher IS '是否已生成凭证（0：未生成1：已生成）默认0';
COMMENT ON COLUMN financialdb.eg_out_table_abs.approve_error_info IS '审批报错信息';
COMMENT ON COLUMN financialdb.eg_out_table_abs.del_flag IS '是否删除（0：未删除1：删除）默认0';
COMMENT ON COLUMN financialdb.eg_out_table_abs.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_out_table_abs.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_out_table_abs.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_out_table_abs.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_out_table_abs.period_code IS '会计期间';

-- financialdb.eg_out_table_contract_detail definition

-- Drop table

-- DROP TABLE financialdb.eg_out_table_contract_detail;

CREATE TABLE financialdb.eg_out_table_contract_detail (
	id int8 NOT NULL, -- ID
	out_table_abs_id int8 NOT NULL, -- 出表absId
	loan_contract_code varchar(100) NULL, -- 借款合同编码
	contract_code varchar(100) NULL, -- 合同编号
	client_code varchar(100) NULL, -- 客户编码
	client_name varchar(100) NULL, -- 客户名称
	org_id varchar(100) NULL, -- 签约主体
	financial_contract_status varchar(100) NULL, -- 财务合同状态
	receivable_rent numeric(20, 2) NULL DEFAULT 0, -- 封包日应收租金
	receivable_residual_value numeric(20, 2) NULL DEFAULT 0, -- 封包日应收残值
	receivable_outtax numeric(20, 2) NULL DEFAULT 0, -- 封包日应收销项税
	unrealized_revenue numeric(20, 2) NULL DEFAULT 0, -- 封包日为实现收益
	lessee_margin numeric(20, 2) NULL DEFAULT 0, -- 封包日承租人保证金
	voucher_id text NULL, -- 凭证id,多个按照逗号分隔
	error_info varchar(2000) NULL, -- 生成凭证报错信息
	del_flag bpchar(1) NULL DEFAULT '0'::bpchar, -- 是否删除（0：未删除1：删除）默认0
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	transfer_price numeric(20, 2) NULL, -- 合同转让价格
	period_code int4 NULL, -- 会计期间
	CONSTRAINT eg_out_table_contract_detail_pk PRIMARY KEY (id)
);
CREATE INDEX eg_out_table_contract_detail_id_idx ON financialdb.eg_out_table_contract_detail USING btree (id, out_table_abs_id, loan_contract_code, contract_code, client_code, client_name, org_id, financial_contract_status);
COMMENT ON TABLE financialdb.eg_out_table_contract_detail IS '出表ABS合同详情';

-- Column comments

COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.out_table_abs_id IS '出表absId';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.loan_contract_code IS '借款合同编码';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.contract_code IS '合同编号';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.client_code IS '客户编码';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.client_name IS '客户名称';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.org_id IS '签约主体';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.financial_contract_status IS '财务合同状态';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.receivable_rent IS '封包日应收租金';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.receivable_residual_value IS '封包日应收残值';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.receivable_outtax IS '封包日应收销项税';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.unrealized_revenue IS '封包日为实现收益';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.lessee_margin IS '封包日承租人保证金';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.voucher_id IS '凭证id,多个按照逗号分隔';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.error_info IS '生成凭证报错信息';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.del_flag IS '是否删除（0：未删除1：删除）默认0';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.transfer_price IS '合同转让价格';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.period_code IS '会计期间';


-- financialdb.eg_asset_abs_redeem definition

-- Drop table

-- DROP TABLE financialdb.eg_asset_abs_redeem;

CREATE TABLE financialdb.eg_asset_abs_redeem (
	id int8 NOT NULL, -- ID
	business_date timestamp NULL, -- 业务日期
	account_date timestamp NULL, -- 记账日期
	loan_contract_code varchar(100) NULL, -- 借款合同编号
	org_id varchar(100) NULL, -- 签约主体
	periods varchar(64) NULL, -- 出表期数
	start_date timestamp NULL, -- 赎回开始日期
	actual_date timestamp NULL, -- 实际赎回日
	process_instance_id int8 NULL, -- 流程id
	process_status varchar(100) NULL, -- 1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝
	is_generate_voucher bpchar(1) NULL DEFAULT '0'::bpchar, -- 是否已生成凭证（0：未生成1：已生成）默认0
	period_code int4 NULL, -- 会计期间
	approve_error_info text NULL, -- 审批报错信息
	del_flag bpchar(1) NULL DEFAULT '0'::bpchar, -- 是否删除（0：未删除1：删除）默认0
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	CONSTRAINT eg_asset_abs_redeem_pk PRIMARY KEY (id)
);
CREATE INDEX eg_asset_abs_redeem_id_idx ON financialdb.eg_asset_abs_redeem USING btree (id, business_date, account_date, loan_contract_code, org_id, periods);
COMMENT ON TABLE financialdb.eg_asset_abs_redeem IS '资产转让ABS-赎回';

-- Column comments

COMMENT ON COLUMN financialdb.eg_asset_abs_redeem.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem.business_date IS '业务日期';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem.account_date IS '记账日期';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem.loan_contract_code IS '借款合同编号';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem.org_id IS '签约主体';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem.periods IS '出表期数';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem.start_date IS '赎回开始日期';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem.actual_date IS '实际赎回日';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem.process_instance_id IS '流程id';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem.process_status IS '1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem.is_generate_voucher IS '是否已生成凭证（0：未生成1：已生成）默认0';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem.period_code IS '会计期间';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem.approve_error_info IS '审批报错信息';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem.del_flag IS '是否删除（0：未删除1：删除）默认0';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem.update_time IS '更新时间';


-- financialdb.eg_asset_abs_redeem_detail definition

-- Drop table

-- DROP TABLE financialdb.eg_asset_abs_redeem_detail;

CREATE TABLE financialdb.eg_asset_abs_redeem_detail (
	id int8 NOT NULL, -- ID
	contract_code varchar(100) NULL, -- 合同编号
	client_code varchar(100) NULL, -- 客户编码
	client_name varchar(100) NULL, -- 客户名称
	asset_abs_redeem_id int8 NULL, -- 赎回主表Id
	financial_contract_status varchar(100) NULL, -- 财务合同状态
	redeem_price numeric(20, 2) NULL, -- 赎回价格
	rate varchar(100) NULL, -- 税率
	voucher_ids text NULL, -- 凭证id,多个按照逗号分隔
	del_flag bpchar(1) NULL DEFAULT '0'::bpchar, -- 是否删除（0：未删除1：删除）默认0
	error_info text NULL, -- 报错信息
	period_code int4 NULL, -- 会计期间
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	CONSTRAINT eg_asset_abs_redeem_detail_pk PRIMARY KEY (id)
);
CREATE INDEX eg_asset_abs_redeem_detail_id_idx ON financialdb.eg_asset_abs_redeem_detail USING btree (id, contract_code, client_code);
COMMENT ON TABLE financialdb.eg_asset_abs_redeem_detail IS '资产转让ABS-赎回详情表';

-- Column comments

COMMENT ON COLUMN financialdb.eg_asset_abs_redeem_detail.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem_detail.contract_code IS '合同编号';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem_detail.client_code IS '客户编码';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem_detail.client_name IS '客户名称';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem_detail.asset_abs_redeem_id IS '赎回主表Id';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem_detail.financial_contract_status IS '财务合同状态';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem_detail.redeem_price IS '赎回价格';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem_detail.rate IS '税率';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem_detail.voucher_ids IS '凭证id,多个按照逗号分隔';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem_detail.del_flag IS '是否删除（0：未删除1：删除）默认0';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem_detail.error_info IS '报错信息';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem_detail.period_code IS '会计期间';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem_detail.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem_detail.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem_detail.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_asset_abs_redeem_detail.update_time IS '更新时间';


-- financialdb.eg_asset_abs_transfer_payment definition

-- Drop table

-- DROP TABLE financialdb.eg_asset_abs_transfer_payment;

CREATE TABLE financialdb.eg_asset_abs_transfer_payment (
	id int8 NOT NULL, -- ID
	business_date timestamp NULL, -- 业务日期
	account_date timestamp NULL, -- 记账日期
	loan_contract_code varchar(100) NULL, -- 借款合同编号
	periods varchar(64) NULL, -- 出表期数
	process_instance_id int8 NULL, -- 流程id
	process_status varchar(100) NULL, -- 1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝
	is_generate_voucher bpchar(1) NULL DEFAULT '0'::bpchar, -- 是否已生成凭证（0：未生成1：已生成）默认0
	period_code int4 NULL, -- 会计期间
	approve_error_info text NULL, -- 审批报错信息
	del_flag bpchar(1) NULL DEFAULT '0'::bpchar, -- 是否删除（0：未删除1：删除）默认0
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	CONSTRAINT eg_asset_abs_transfer_payment_pk PRIMARY KEY (id)
);
CREATE INDEX eg_asset_abs_transfer_payment_id_idx ON financialdb.eg_asset_abs_transfer_payment USING btree (id, business_date, account_date, loan_contract_code, periods);
COMMENT ON TABLE financialdb.eg_asset_abs_transfer_payment IS '资产转付';

-- Column comments

COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment.business_date IS '业务日期';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment.account_date IS '记账日期';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment.loan_contract_code IS '借款合同编号';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment.periods IS '出表期数';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment.process_instance_id IS '流程id';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment.process_status IS '1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment.is_generate_voucher IS '是否已生成凭证（0：未生成1：已生成）默认0';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment.period_code IS '会计期间';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment.approve_error_info IS '审批报错信息';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment.del_flag IS '是否删除（0：未删除1：删除）默认0';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment.update_time IS '更新时间';



-- financialdb.eg_asset_abs_transfer_payment_detail definition

-- Drop table

-- DROP TABLE financialdb.eg_asset_abs_transfer_payment_detail;

CREATE TABLE financialdb.eg_asset_abs_transfer_payment_detail (
	id int8 NOT NULL, -- ID
	contract_code varchar(100) NULL, -- 合同编号
	client_code varchar(100) NULL, -- 客户编码
	client_name varchar(100) NULL, -- 客户名称
	asset_abs_transfer_payment_id int8 NOT NULL, -- 转付主表Id
	rate varchar(100) NULL, -- 税率
	actual_principal_amount numeric(20, 2) NULL, -- 实付本金
	actual_interest_amount numeric(20, 2) NULL, -- 实付利息
	actual_retention_purchase_amount numeric(20, 2) NULL, -- 实付留够价
	actual_penalty_interest_amount numeric(20, 2) NULL, -- 实付罚息及手续费
	voucher_ids text NULL, -- 凭证id,多个按照逗号分隔
	del_flag bpchar(1) NULL DEFAULT '0'::bpchar, -- 是否删除（0：未删除1：删除）默认0
	error_info text NULL, -- 报错信息
	period_code int4 NULL, -- 会计期间
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	org_id varchar(100) NULL, -- 签约主体
	CONSTRAINT eg_asset_abs_transfer_payment_detail_pk PRIMARY KEY (id)
);
CREATE INDEX eg_asset_abs_transfer_payment_detail_id_idx ON financialdb.eg_asset_abs_transfer_payment_detail USING btree (id, contract_code, client_code);
COMMENT ON TABLE financialdb.eg_asset_abs_transfer_payment_detail IS '资产转付详情表';

-- Column comments

COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment_detail.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment_detail.contract_code IS '合同编号';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment_detail.client_code IS '客户编码';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment_detail.client_name IS '客户名称';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment_detail.asset_abs_transfer_payment_id IS '转付主表Id';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment_detail.rate IS '税率';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment_detail.actual_principal_amount IS '实付本金';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment_detail.actual_interest_amount IS '实付利息';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment_detail.actual_retention_purchase_amount IS '实付留够价';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment_detail.actual_penalty_interest_amount IS '实付罚息及手续费';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment_detail.voucher_ids IS '凭证id,多个按照逗号分隔';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment_detail.del_flag IS '是否删除（0：未删除1：删除）默认0';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment_detail.error_info IS '报错信息';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment_detail.period_code IS '会计期间';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment_detail.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment_detail.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment_detail.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment_detail.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_asset_abs_transfer_payment_detail.org_id IS '签约主体';
