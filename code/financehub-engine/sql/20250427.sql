-- financialdb3.eg_report_balance definition

-- Drop table

 DROP TABLE financialdb3.eg_report_balance;

CREATE TABLE financialdb3.eg_report_balance (
	id int8 NULL, -- 主键
	contract_code varchar(100) NULL, -- 合同编号
	org_id varchar(100) NULL, -- 组织机编码
	report_balance numeric(20, 2) NULL, -- 报表余额
	invoicing_date date NULL -- 开票日期
);
COMMENT ON TABLE financialdb3.eg_report_balance IS '从业务系统取得的报表余额';

-- Column comments

COMMENT ON COLUMN financialdb3.eg_report_balance.id IS '主键';
COMMENT ON COLUMN financialdb3.eg_report_balance.contract_code IS '合同编号';
COMMENT ON COLUMN financialdb3.eg_report_balance.org_id IS '组织机编码';
COMMENT ON COLUMN financialdb3.eg_report_balance.report_balance IS '报表余额';
COMMENT ON COLUMN financialdb3.eg_report_balance.invoicing_date IS '开票日期';

ALTER TABLE financialdb.eg_report_balance ADD amount_type varchar NULL;
COMMENT ON COLUMN financialdb.eg_report_balance.amount_type IS '租赁or服务费';
