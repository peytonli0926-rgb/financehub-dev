-- financialdb.eg_voucher_dimension_value definition

-- Drop table

-- DROP TABLE financialdb.eg_voucher_dimension_value;

CREATE TABLE financialdb.eg_voucher_dimension_value (
	id int8 NOT NULL, -- ID
	org_id varchar(100) NULL, -- 组织机编码
	account_code varchar(100) NULL, -- 科目编码
	dim varchar(1000) NULL, -- 维度:用|分隔
	dim_value varchar(1000) NULL, -- 维度值:用|分隔
	source_system varchar(100) NULL -- 来源
);
COMMENT ON TABLE financialdb.eg_voucher_dimension_value IS '凭证维度和值对应表';

-- Column comments

COMMENT ON COLUMN financialdb.eg_voucher_dimension_value.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_voucher_dimension_value.org_id IS '组织机编码';
COMMENT ON COLUMN financialdb.eg_voucher_dimension_value.account_code IS '科目编码';
COMMENT ON COLUMN financialdb.eg_voucher_dimension_value.dim IS '维度:用|分隔';
COMMENT ON COLUMN financialdb.eg_voucher_dimension_value.dim_value IS '维度值:用|分隔';
COMMENT ON COLUMN financialdb.eg_voucher_dimension_value.source_system IS '来源';


-- financialdb.eg_voucher_to_eas_record definition

-- Drop table

-- DROP TABLE financialdb.eg_voucher_to_eas_record;

CREATE TABLE financialdb.eg_voucher_to_eas_record (
	id int8 NOT NULL, -- ID
	company_number varchar(50) NULL, -- 公司编码
	booked_date varchar(10) NULL, -- 记账日期
	biz_date varchar(10) NULL, -- 业务日期
	period_year int4 NULL, -- 会计期间-年
	period_number int4 NULL, -- 会计期间-编码
	voucher_type varchar(100) NULL, -- 凭证字（凭证类型）
	attaches int4 NULL, -- 附件数量
	description varchar(1000) NULL, -- 参考信息
	voucher_number varchar NULL, -- 凭证号
	creator varchar(50) NULL, -- 制单人
	poster varchar(50) NULL, -- 过账人
	auditor varchar(50) NULL, -- 审核人
	entry_seq int4 NULL, -- 分录行号
	voucher_abstract varchar(1000) NULL, -- 摘要
	account_number varchar(50) NULL, -- 科目
	currency_number varchar(50) NULL, -- 币种
	profit_center_number varchar(50) NULL, -- 利润中心编码
	local_rate numeric(10, 2) NULL, -- 汇率
	entry_dc int4 NULL, -- 分录行方向：1 借方 -1贷方
	original_amount numeric(20, 2) NULL, -- 原币金额
	qty numeric(20, 2) NULL, -- 数量
	measurement varchar(20) NULL, -- 计量单位
	price numeric(20, 2) NULL, -- 单价
	debit_amount numeric(20, 2) NULL, -- 借方金额
	credit_amount numeric(20, 2) NULL, -- 贷方金额
	asst_seq int4 NULL, -- 辅助账行号
	biz_number varchar(20) NULL, -- 业务编号
	settlement_number varchar(20) NULL, -- 结算方式
	settlement_type varchar(20) NULL, -- 结算号
	cussent int4 NULL DEFAULT 0, -- 核销/挂账
	asst_act_type1 varchar(100) NULL, -- 核算项目1
	asst_act_number1 varchar(100) NULL, -- 核算对象编码1
	asst_act_name1 varchar(100) NULL, -- 核算对象名称1
	asst_act_type2 varchar(100) NULL, -- 核算项目2
	asst_act_number2 varchar(100) NULL, -- 核算对象编码2
	asst_act_name2 varchar(100) NULL, -- 核算对象名称2
	asst_act_type3 varchar(100) NULL, -- 核算项目3
	asst_act_number3 varchar(100) NULL, -- 核算对象编码3
	asst_act_name3 varchar(100) NULL, -- 核算对象名称3
	asst_act_type4 varchar(100) NULL, -- 核算项目4
	asst_act_number4 varchar(100) NULL, -- 核算对象编码4
	asst_act_name4 varchar(100) NULL, -- 核算对象名称4
	asst_act_type5 varchar(100) NULL, -- 核算项目5
	asst_act_number5 varchar(100) NULL, -- 核算对象编码5
	asst_act_name5 varchar(100) NULL, -- 核算对象名称5
	asst_act_type6 varchar(100) NULL, -- 核算项目6
	asst_act_number6 varchar(100) NULL, -- 核算对象编码6
	asst_act_name6 varchar(100) NULL, -- 核算对象名称6
	asst_act_type7 varchar(100) NULL, -- 核算项目7
	asst_act_number7 varchar(100) NULL, -- 核算对象编码7
	asst_act_name7 varchar(100) NULL, -- 核算对象名称7
	asst_act_type8 varchar(100) NULL, -- 核算项目8
	asst_act_number8 varchar(100) NULL, -- 核算对象编码8
	asst_act_name8 varchar(100) NULL, -- 核算对象名称8
	transaction_date timestamp NULL -- 传送时间
);

-- Column comments

COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.company_number IS '公司编码';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.booked_date IS '记账日期';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.biz_date IS '业务日期';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.period_year IS '会计期间-年';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.period_number IS '会计期间-编码';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.voucher_type IS '凭证字（凭证类型）';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.attaches IS '附件数量';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.description IS '参考信息';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.voucher_number IS '凭证号';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.creator IS '制单人';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.poster IS '过账人';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.auditor IS '审核人';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.entry_seq IS '分录行号';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.voucher_abstract IS '摘要';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.account_number IS '科目';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.currency_number IS '币种';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.profit_center_number IS '利润中心编码';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.local_rate IS '汇率';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.entry_dc IS '分录行方向：1 借方 -1贷方';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.original_amount IS '原币金额';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.qty IS '数量';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.measurement IS '计量单位';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.price IS '单价';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.debit_amount IS '借方金额';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.credit_amount IS '贷方金额';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.asst_seq IS '辅助账行号';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.biz_number IS '业务编号';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.settlement_number IS '结算方式';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.settlement_type IS '结算号';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.cussent IS '核销/挂账';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.asst_act_type1 IS '核算项目1';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.asst_act_number1 IS '核算对象编码1';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.asst_act_name1 IS '核算对象名称1';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.asst_act_type2 IS '核算项目2';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.asst_act_number2 IS '核算对象编码2';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.asst_act_name2 IS '核算对象名称2';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.asst_act_type3 IS '核算项目3';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.asst_act_number3 IS '核算对象编码3';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.asst_act_name3 IS '核算对象名称3';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.asst_act_type4 IS '核算项目4';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.asst_act_number4 IS '核算对象编码4';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.asst_act_name4 IS '核算对象名称4';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.asst_act_type5 IS '核算项目5';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.asst_act_number5 IS '核算对象编码5';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.asst_act_name5 IS '核算对象名称5';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.asst_act_type6 IS '核算项目6';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.asst_act_number6 IS '核算对象编码6';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.asst_act_name6 IS '核算对象名称6';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.asst_act_type7 IS '核算项目7';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.asst_act_number7 IS '核算对象编码7';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.asst_act_name7 IS '核算对象名称7';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.asst_act_type8 IS '核算项目8';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.asst_act_number8 IS '核算对象编码8';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.asst_act_name8 IS '核算对象名称8';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.transaction_date IS '传送时间';



-- financialdb.eg_voucher_to_eas_result definition

-- Drop table

-- DROP TABLE financialdb.eg_voucher_to_eas_result;

CREATE TABLE financialdb.eg_voucher_to_eas_result (
	id int8 NOT NULL, -- ID
	back_result varchar(2000) NULL, -- 返回结果json
	voucher_number varchar NULL, -- 凭证号
	receive_time timestamp NULL -- 收到消息时间
);

-- Column comments

COMMENT ON COLUMN financialdb.eg_voucher_to_eas_result.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_result.back_result IS '返回结果json';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_result.voucher_number IS '凭证号';
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_result.receive_time IS '收到消息时间';

ALTER TABLE financialdb.eg_voucher_to_eas_record ALTER COLUMN booked_date TYPE varchar(50) USING booked_date::varchar;
ALTER TABLE financialdb.eg_voucher_to_eas_record ALTER COLUMN biz_date TYPE varchar(50) USING biz_date::varchar;
ALTER TABLE financialdb.eg_voucher_to_eas_result ALTER COLUMN back_result TYPE text USING back_result::text;
ALTER TABLE financialdb.eg_voucher_to_eas_record ADD fid varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.fid IS '源主键';

