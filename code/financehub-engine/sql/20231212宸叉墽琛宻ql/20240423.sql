
CREATE TABLE financialdb.eg_report_period_sync_record (
      id int8 NOT NULL,
      period_code int4 NOT NULL,
      execute_status varchar(50) NULL,
      start_time timestamp(6) NULL,
      end_time timestamp(6) NULL,
      del_flag bpchar(1) NULL DEFAULT '0'::bpchar,
      create_by varchar(32) NULL,
      create_time timestamp(6) NULL,
      update_by varchar(32) NULL,
      update_time timestamp(6) NULL,
      CONSTRAINT eg_report_period_sync_record_pk PRIMARY KEY (id)
);


CREATE TABLE financialdb.eg_report_finance_in_out (
      id int8 NOT NULL,
      execute_date varchar(100) NULL,
      period_code int4 NULL,
      contract_code varchar(100) NULL,
      client_code varchar(100) NULL,
      org_id varchar(100) NULL,
      inbound_date varchar(100) NULL,
      receivable_rent_balance numeric(20, 2) NULL,
      receivable_residual_value_balance numeric(20, 2) NULL,
      receivable_outtax_balance numeric(20, 2) NULL,
      unrealized_revenue_balance numeric(20, 2) NULL,
      lessee_margin_balance numeric(20, 2) NULL,
      financial_exposure numeric(20, 2) NULL,
      recycling_equipment_cost numeric(20, 2) NULL,
      provision_for_impairment numeric(20, 2) NULL,
      substract_balance numeric(20, 2) NULL,
      net_worth numeric(20, 2) NULL,
      provisional_receipts_balance numeric(20, 2) NULL,
      outbound_date varchar(100) NULL,
      outbound_type varchar(20) NULL,
      deal_amount numeric(20, 2) NULL,
      receivable_service_outtax_amount numeric(20, 2) NULL,
      profit_loss numeric(20, 2) NULL,
      del_flag bpchar(1) NULL DEFAULT '0'::bpchar,
      create_by varchar(32) NULL,
      create_time timestamp(6) NULL,
      update_by varchar(32) NULL,
      update_time timestamp(6) NULL,
      CONSTRAINT eg_report_finance_in_out_pk PRIMARY KEY (id)
);
CREATE INDEX eg_report_finance_in_out_client_code_idx ON financialdb.eg_report_finance_in_out USING btree (client_code);
CREATE INDEX eg_report_finance_in_out_contract_code_idx ON financialdb.eg_report_finance_in_out USING btree (contract_code);
CREATE INDEX eg_report_finance_in_out_inbound_date_idx ON financialdb.eg_report_finance_in_out USING btree (inbound_date);
CREATE INDEX eg_report_finance_in_out_outbound_date_idx ON financialdb.eg_report_finance_in_out USING btree (outbound_date);
CREATE INDEX eg_report_finance_in_out_period_code_idx ON financialdb.eg_report_finance_in_out USING btree (period_code);

COMMENT ON TABLE financialdb.eg_report_finance_in_out IS '报表-财务入库出库';
COMMENT ON COLUMN financialdb.eg_report_finance_in_out.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_report_finance_in_out.execute_date IS '执行时间yyyy-MM-dd';
COMMENT ON COLUMN financialdb.eg_report_finance_in_out.period_code IS '执行数据期间yyyyMM';
COMMENT ON COLUMN financialdb.eg_report_finance_in_out.contract_code IS '合同代码';
COMMENT ON COLUMN financialdb.eg_report_finance_in_out.client_code IS '客户代码';
COMMENT ON COLUMN financialdb.eg_report_finance_in_out.org_id IS '签约主体';
COMMENT ON COLUMN financialdb.eg_report_finance_in_out.inbound_date IS '入库时间';
COMMENT ON COLUMN financialdb.eg_report_finance_in_out.receivable_rent_balance IS '应收租金';
COMMENT ON COLUMN financialdb.eg_report_finance_in_out.receivable_residual_value_balance IS '应收期末残值';
COMMENT ON COLUMN financialdb.eg_report_finance_in_out.receivable_outtax_balance IS '应收销项税';
COMMENT ON COLUMN financialdb.eg_report_finance_in_out.unrealized_revenue_balance IS '未实现收益';
COMMENT ON COLUMN financialdb.eg_report_finance_in_out.lessee_margin_balance IS '承租人保证金';
COMMENT ON COLUMN financialdb.eg_report_finance_in_out.financial_exposure IS '财务敞口';
COMMENT ON COLUMN financialdb.eg_report_finance_in_out.recycling_equipment_cost IS '回收设备成本';
COMMENT ON COLUMN financialdb.eg_report_finance_in_out.provision_for_impairment IS '入库时计提减值';
COMMENT ON COLUMN financialdb.eg_report_finance_in_out.subtract_balance IS '回收设备减值';
COMMENT ON COLUMN financialdb.eg_report_finance_in_out.net_worth IS '净值';
COMMENT ON COLUMN financialdb.eg_report_finance_in_out.provisional_receipts_balance IS '暂收款项';
COMMENT ON COLUMN financialdb.eg_report_finance_in_out.outbound_date IS '出库日期';
COMMENT ON COLUMN financialdb.eg_report_finance_in_out.outbound_type IS '出库类型';
COMMENT ON COLUMN financialdb.eg_report_finance_in_out.deal_amount IS '处置金额';
COMMENT ON COLUMN financialdb.eg_report_finance_in_out.receivable_service_outtax_amount IS '应交销项税';
COMMENT ON COLUMN financialdb.eg_report_finance_in_out.profit_loss IS '融资租赁资产处置损益';
COMMENT ON COLUMN financialdb.eg_report_finance_in_out.del_flag IS '删除标记';
COMMENT ON COLUMN financialdb.eg_report_finance_in_out.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_report_finance_in_out.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_report_finance_in_out.update_by IS '更新人';


ALTER TABLE financialdb.eg_non_confirm_collection_second_detail ADD approve_time timestamp NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.approve_time IS '审核时间';

ALTER TABLE financialdb.eg_send_eas2_result ADD primary_key text NULL;
COMMENT ON COLUMN financialdb.eg_send_eas2_result.primary_key IS '系统id,多个逗号分隔';