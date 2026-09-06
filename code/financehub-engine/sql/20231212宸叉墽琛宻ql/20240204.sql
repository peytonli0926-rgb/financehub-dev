--已执行20240226
--手工凭证新增字段
ALTER TABLE financialdb.eg_manual_voucher ADD loans_contract_code_name varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_manual_voucher.loans_contract_code_name IS '借款合同名称';
ALTER TABLE financialdb.eg_manual_voucher ADD cost_centre_name varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_manual_voucher.cost_centre_name IS '成本中心名称';
ALTER TABLE financialdb.eg_manual_voucher ADD employee_code varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_manual_voucher.employee_code IS '员工编码';
ALTER TABLE financialdb.eg_manual_voucher ADD expense_type_name varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_manual_voucher.expense_type_name IS '费用类型名称';
ALTER TABLE financialdb.eg_manual_voucher ADD financial_institution_name varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_manual_voucher.financial_institution_name IS '金融机构名称';
ALTER TABLE financialdb.eg_manual_voucher ADD bank_no_name varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_manual_voucher.bank_no_name IS '银行账号名称';
ALTER TABLE financialdb.eg_manual_voucher ADD material_contract_name varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_manual_voucher.material_contract_name IS '物料（合同名称）';
ALTER TABLE financialdb.eg_manual_voucher ADD project_type_name varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_manual_voucher.project_type_name IS '项目类型名称';

-- financialdb.eg_contract_balance_temp definition

-- Drop table

-- DROP TABLE financialdb.eg_contract_balance_temp;

CREATE TABLE financialdb.eg_contract_balance_temp (
	id int8 NOT NULL, -- ID
	voucher_id int8 NULL, -- 凭证ID
	interface_data_id int8 NULL DEFAULT 0, -- 接口表ID
	system_code varchar(20) NULL, -- 来源系统编码
	business_code varchar(100) NULL, -- 业务编码
	business_date timestamp NULL, -- 业务日期
	voucher_date timestamp NULL, -- 凭证日期
	scene_code varchar(100) NULL, -- 场景编码
	contract_code varchar(100) NULL, -- 合同编码
	client_code varchar(100) NULL, -- 客户编码
	client_type varchar(100) NULL, -- 客户类型
	org_id varchar(100) NULL, -- 机构编码
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	del_flag bpchar(1) NULL DEFAULT '0'::bpchar, -- 删除标识(0:未删除,1:已删除)
	receivable_rent_balance numeric(20, 2) NULL DEFAULT 0, -- 应收租金余额
	receivable_rent_amount numeric(20, 2) NULL DEFAULT 0, -- 应收租金发生额
	receivable_downpayment_balance numeric(20, 2) NULL DEFAULT 0, -- 应收首付款余额
	receivable_downpayment_amount numeric(20, 2) NULL DEFAULT 0, -- 应收首付款发生额
	receivable_residual_value_balance numeric(20, 2) NULL DEFAULT 0, -- 应收期末残值余额
	receivable_residual_value_amount numeric(20, 2) NULL DEFAULT 0, -- 应收期末残值发生额
	receivable_commission_balance numeric(20, 2) NULL DEFAULT 0, -- 应收手续费余额
	receivable_commission_amount numeric(20, 2) NULL DEFAULT 0, -- 应收手续费发生额
	receivable_rebate_balance numeric(20, 2) NULL DEFAULT 0, -- 应收返利余额
	receivable_rebate_amount numeric(20, 2) NULL DEFAULT 0, -- 应收返利发生额
	receivable_insurance_balance numeric(20, 2) NULL DEFAULT 0, -- 应收保险费余额
	receivable_insurance_amount numeric(20, 2) NULL DEFAULT 0, -- 应收保险费发生额
	receivable_otherincome_balance numeric(20, 2) NULL DEFAULT 0, -- 应收其他收入余额
	receivable_otherincome_amount numeric(20, 2) NULL DEFAULT 0, -- 应收其他收入发生额
	receivable_outtax_balance numeric(20, 2) NULL DEFAULT 0, -- 应收销项税余额
	receivable_outtax_amount numeric(20, 2) NULL DEFAULT 0, -- 应收销项税发生额
	receivable_outputtax_base_balance numeric(20, 2) NULL DEFAULT 0, -- 应收销项税-本金余额
	receivable_outputtax_base_amount numeric(20, 2) NULL DEFAULT 0, -- 应收销项税-本金发生额
	receivable_unconfirm_receipt_balance numeric(20, 2) NULL DEFAULT 0, -- 未确认收款余额
	receivable_unconfirm_receipt_amount numeric(20, 2) NULL DEFAULT 0, -- 未确认收款发生额
	receivable_service_balance numeric(20, 2) NULL DEFAULT 0, -- 应收服务费余额
	receivable_service_amount numeric(20, 2) NULL DEFAULT 0, -- 应收服务费发生额
	receivable_service_outtax_balance numeric(20, 2) NULL DEFAULT 0, -- 应收服务费-销项税余额
	receivable_service_outtax_amount numeric(20, 2) NULL DEFAULT 0, -- 应收服务费-销项税发生额
	unrealized_revenue_balance numeric(20, 2) NULL DEFAULT 0, -- 未实现收益余额
	unrealized_revenue_amount numeric(20, 2) NULL DEFAULT 0, -- 未实现收益发生额
	lease_revenue_balance numeric(20, 2) NULL DEFAULT 0, -- 融资租赁收益余额
	lease_revenue_amount numeric(20, 2) NULL DEFAULT 0, -- 融资租赁收益发生额
	service_revenue_balance numeric(20, 2) NULL DEFAULT 0, -- 服务收入余额
	service_revenue_amount numeric(20, 2) NULL DEFAULT 0, -- 服务收入发生额
	insurance_differ_balance numeric(20, 2) NULL DEFAULT 0, -- 保险费差额余额
	insurance_differ_amount numeric(20, 2) NULL DEFAULT 0, -- 保险费差额发生额
	dinterest_revenue_balance numeric(20, 2) NULL DEFAULT 0, -- 罚息收入余额
	dinterest_revenue_amount numeric(20, 2) NULL DEFAULT 0, -- 罚息收入发生额
	terminate_balance numeric(20, 2) NULL DEFAULT 0, -- 合同解约及更改手续费余额
	terminate_amount numeric(20, 2) NULL DEFAULT 0, -- 合同解约及更改手续费发生额
	other_revenue_balance numeric(20, 2) NULL DEFAULT 0, -- 其他租赁相关收入余额
	other_revenue_amount numeric(20, 2) NULL DEFAULT 0, -- 其他租赁相关收入发生额
	damages_revenue_balance numeric(20, 2) NULL DEFAULT 0, -- 违约金收入余额
	damages_revenue_amount numeric(20, 2) NULL DEFAULT 0, -- 违约金收入发生额
	margin_interest_balance numeric(20, 2) NULL DEFAULT 0, -- 融资租赁业务保证金利息收入余额
	margin_interest_amount numeric(20, 2) NULL DEFAULT 0, -- 融资租赁业务保证金利息收入发生额
	payable_device_estimate_balance numeric(20, 2) NULL DEFAULT 0, -- 应付租赁设备款-暂估余额
	payable_device_estimate_amount numeric(20, 2) NULL DEFAULT 0, -- 应付租赁设备款-暂估发生额
	payable_device_balance numeric(20, 2) NULL DEFAULT 0, -- 应付租赁设备款余额
	payable_device_amount numeric(20, 2) NULL DEFAULT 0, -- 应付租赁设备款发生额
	payable_other_cost_estimate_balance numeric(20, 2) NULL DEFAULT 0, -- 应付其他租赁成本-暂估余额
	payable_other_cost_estimate_amount numeric(20, 2) NULL DEFAULT 0, -- 应付其他租赁成本-暂估发生额
	payable_other_cost_balance numeric(20, 2) NULL DEFAULT 0, -- 应付其他租赁成本余额
	payable_other_cost_amount numeric(20, 2) NULL DEFAULT 0, -- 应付其他租赁成本发生额
	payable_agency_estimate_balance numeric(20, 2) NULL DEFAULT 0, -- 应付经销商服务费-暂估余额
	payable_agency_estimate_amount numeric(20, 2) NULL DEFAULT 0, -- 应付经销商服务费-暂估发生额
	payable_agency_balance numeric(20, 2) NULL DEFAULT 0, -- 应付经销商服务费余额
	payable_agency_amount numeric(20, 2) NULL DEFAULT 0, -- 应付经销商服务费发生额
	payable_vehicle_estimate_balance numeric(20, 2) NULL DEFAULT 0, -- 应付收车费-暂估余额
	payable_vehicle_estimate_amount numeric(20, 2) NULL DEFAULT 0, -- 应付收车费-暂估发生额
	payable_vehicle_balance numeric(20, 2) NULL DEFAULT 0, -- 应付收车费余额
	payable_vehicle_amount numeric(20, 2) NULL DEFAULT 0, -- 应付收车费发生额
	payable_band_cost_estimate_balance numeric(20, 2) NULL DEFAULT 0, -- 应付手环成本_暂估余额
	payable_band_cost_estimate_amount numeric(20, 2) NULL DEFAULT 0, -- 应付手环成本_暂估发生额
	payable_band_cost_balance numeric(20, 2) NULL DEFAULT 0, -- 应付手环成本余额
	payable_band_cost_amount numeric(20, 2) NULL DEFAULT 0, -- 应付手环成本发生额
	payable_pledge_estimate_balance numeric(20, 2) NULL DEFAULT 0, -- 应付抵押费_暂估余额
	payable_pledge_estimate_amount numeric(20, 2) NULL DEFAULT 0, -- 应付抵押费_暂估发生额
	payable_pledge_balance numeric(20, 2) NULL DEFAULT 0, -- 应付抵押费余额
	payable_pledge_amount numeric(20, 2) NULL DEFAULT 0, -- 应付抵押费发生额
	payable_unpledge_estimate_balance numeric(20, 2) NULL DEFAULT 0, -- 应付解抵押费_暂估余额
	payable_unpledge_estimate_amount numeric(20, 2) NULL DEFAULT 0, -- 应付解抵押费_暂估发生额
	payable_unpledge_balance numeric(20, 2) NULL DEFAULT 0, -- 应付解抵押费余额
	payable_unpledge_amount numeric(20, 2) NULL DEFAULT 0, -- 应付解抵押费发生额
	lessee_margin_balance numeric(20, 2) NULL DEFAULT 0, -- 承租人保证金余额
	lessee_margin_amount numeric(20, 2) NULL DEFAULT 0, -- 承租人保证金发生额
	supplier_margin_balance numeric(20, 2) NULL DEFAULT 0, -- 供应商及代理商保证金余额
	supplier_margin_amount numeric(20, 2) NULL DEFAULT 0, -- 供应商及代理商保证金发生额
	payable_insurance_estimate_balance numeric(20, 2) NULL DEFAULT 0, -- 应付保险费-暂估余额
	payable_insurance_estimate_amount numeric(20, 2) NULL DEFAULT 0, -- 应付保险费-暂估发生额
	payable_insurance_balance numeric(20, 2) NULL DEFAULT 0, -- 应付保险费余额
	payable_insurance_amount numeric(20, 2) NULL DEFAULT 0, -- 应付保险费发生额
	intax_balance numeric(20, 2) NULL DEFAULT 0, -- 进项税额余额
	intax_amount numeric(20, 2) NULL DEFAULT 0, -- 进项税额发生额
	outtax_balance numeric(20, 2) NULL DEFAULT 0, -- 销项税额余额
	outtax_amount numeric(20, 2) NULL DEFAULT 0, -- 销项税额发生额
	litigation_expenses_balance numeric(20, 2) NULL DEFAULT 0, -- 诉讼费余额
	litigation_expenses_amount numeric(20, 2) NULL DEFAULT 0, -- 诉讼费发生额
	depreciation_reserves_balance numeric(20, 2) NULL DEFAULT 0, -- 减值准备余额
	depreciation_reserves_amount numeric(20, 2) NULL DEFAULT 0, -- 减值准备发生额
	depreciation_loss_balance numeric(20, 2) NULL DEFAULT 0, -- 减值损失余额
	depreciation_loss_amount numeric(20, 2) NULL DEFAULT 0, -- 减值损失发生额
	receive_cost_balance numeric(20, 2) NULL DEFAULT 0, -- 回收融资租赁设备成本余额
	receive_cost_amount numeric(20, 2) NULL DEFAULT 0, -- 回收融资租赁设备成本发生额
	off_income_balance numeric(20, 2) NULL DEFAULT 0, -- 表外租赁收入余额
	off_income_amount numeric(20, 2) NULL DEFAULT 0, -- 表外租赁收入发生额
	defer_income_balance numeric(20, 2) NULL DEFAULT 0, -- 递延收益余额
	defer_income_amount numeric(20, 2) NULL DEFAULT 0, -- 递延收益发生额
	payable_account_balance numeric(20, 2) NULL DEFAULT 0, -- 应付未付款余额
	payable_account_amount numeric(20, 2) NULL DEFAULT 0, -- 应付未付款发生额
	payable_margin_balance numeric(20, 2) NULL DEFAULT 0, -- 应付保证金余额
	payable_margin_amount numeric(20, 2) NULL DEFAULT 0, -- 应付保证金发生额
	payable_margin_year_balance numeric(20, 2) NULL DEFAULT 0, -- 应付一年内保证金余额
	payable_margin_year_amount numeric(20, 2) NULL DEFAULT 0, -- 应付一年内保证金发生额
	margin_ointerest_balance numeric(20, 2) NULL DEFAULT 0, -- 保证金利息支出余额
	margin_ointerest_amount numeric(20, 2) NULL DEFAULT 0, -- 保证金利息支出发生额
	receive_sum_balance numeric(20, 2) NULL DEFAULT 0, -- 应收总额余额
	receive_sum_amount numeric(20, 2) NULL DEFAULT 0, -- 应收总额发生额
	receive_sum_outtax_balance numeric(20, 2) NULL DEFAULT 0, -- 应收总额_销项税余额
	receive_sum_outtax_amount numeric(20, 2) NULL DEFAULT 0, -- 应收总额_销项税发生额
	receive_unrealized_revenue_balance numeric(20, 2) NULL DEFAULT 0, -- 应收总额_未实现收益余额
	receive_unrealized_revenue_amount numeric(20, 2) NULL DEFAULT 0, -- 应收总额_未实现收益发生额
	payable_lessee_margin_year_balance numeric(20, 2) NULL DEFAULT 0, -- 应付一年内承租人保证金余额
	payable_lessee_margin_year_amount numeric(20, 2) NULL DEFAULT 0, -- 应付一年内承租人保证金发生额
	payable_supplier_margin_year_balance numeric(20, 2) NULL DEFAULT 0, -- 应付一年内供应商及代理商保证金余额
	payable_supplier_margin_year_amount numeric(20, 2) NULL DEFAULT 0, -- 应付一年内供应商及代理商保证金发生额
	receivable_litigation_expenses_balance numeric(20, 2) NULL DEFAULT 0, -- 应收诉讼费余额
	receivable_litigation_expenses_amount numeric(20, 2) NULL DEFAULT 0, -- 应收诉讼费发生额
	payable_other_estimate_balance numeric(20, 2) NULL DEFAULT 0, -- 应付其他款项-暂估余额
	payable_other_estimate_amount numeric(20, 2) NULL DEFAULT 0, -- 应付其他款项-暂估发生额
	payable_discount_cost_balance numeric(20, 2) NULL DEFAULT 0, -- 应收贴息手续费余额
	payable_discount_cost_amount numeric(20, 2) NULL DEFAULT 0, -- 应收贴息手续费发生额
	payable_procedure_cost_balance numeric(20, 2) NULL DEFAULT 0, -- 应付手续费成本余额
	payable_procedure_cost_amount numeric(20, 2) NULL DEFAULT 0, -- 应付手续费成本发生额
	provisional_receipts_balance numeric(20, 2) NULL DEFAULT 0, -- 暂收款项余额
	provisional_receipts_amount numeric(20, 2) NULL DEFAULT 0, -- 暂收款项发生额
	equipment_depreciation_reserves_balance numeric(20, 2) NULL DEFAULT 0, -- 回收设备减值准备余额
	equipment_depreciation_reserves_amount numeric(20, 2) NULL DEFAULT 0, -- 回收设备减值准备发生额
	equipment_depreciation_loss_balance numeric(20, 2) NULL DEFAULT 0, -- 回收设备减值损失余额
	equipment_depreciation_loss_amount numeric(20, 2) NULL DEFAULT 0, -- 回收设备减值损失发生额
	asset_dispose_gain_balance numeric(20, 2) NULL DEFAULT 0, -- 资产处置收益余额
	asset_dispose_gain_amount numeric(20, 2) NULL DEFAULT 0, -- 资产处置收益发生额
	asset_dispose_loss_balance numeric(20, 2) NULL DEFAULT 0, -- 资产处置损失余额
	asset_dispose_loss_amount numeric(20, 2) NULL DEFAULT 0, -- 资产处置损失发生额
	receivable_other_balance numeric(20, 2) NULL DEFAULT 0, -- 其他应收款余额
	receivable_other_amount numeric(20, 2) NULL DEFAULT 0, -- 其他应收款发生额
	receivable_collection_transfer_balance numeric(20, 2) NULL DEFAULT 0, -- 应收转让后收款余额
	receivable_collection_transfer_amount numeric(20, 2) NULL DEFAULT 0, -- 应收转让后收款发生额
	receivable_related_party_balance numeric(20, 2) NULL DEFAULT 0, -- 其他应收款_关联公司往来余额
	receivable_related_party_amount numeric(20, 2) NULL DEFAULT 0, -- 其他应收款_关联公司往来发生额
	collection_transfer_balance numeric(20, 2) NULL DEFAULT 0, -- 代收转让款项余额
	collection_transfer_amount numeric(20, 2) NULL DEFAULT 0, -- 代收转让款项发生额
	collect_payment_other_balance numeric(20, 2) NULL DEFAULT 0, -- 其他代收款余额
	collect_payment_other_amount numeric(20, 2) NULL DEFAULT 0, -- 其他代收款发生额
	receivable_interest_balance numeric(20, 2) NULL DEFAULT 0, -- 应收利息余额
	receivable_interest_amount numeric(20, 2) NULL DEFAULT 0, -- 应收利息发生额
	receivable_litigation_margin_balance numeric(20, 2) NULL DEFAULT 0, -- 应收诉讼保证金余额
	receivable_litigation_margin_amount numeric(20, 2) NULL DEFAULT 0, -- 应收诉讼保证金发生额
	property_cost_balance numeric(20, 2) NULL DEFAULT 0, -- 房产_成本余额
	property_cost_amount numeric(20, 2) NULL DEFAULT 0, -- 房产_成本发生额
	property_depreciation_reserves_balance numeric(20, 2) NULL DEFAULT 0, -- 房产_减值准备 余额
	property_depreciation_reserves_amount numeric(20, 2) NULL DEFAULT 0, -- 房产_减值准备 发生额
	machine_cost_balance numeric(20, 2) NULL DEFAULT 0, -- 机器设备_成本余额
	machine_cost_amount numeric(20, 2) NULL DEFAULT 0, -- 机器设备_成本发生额
	machine_depreciation_reserves_balance numeric(20, 2) NULL DEFAULT 0, -- 机器设备_减值准备余额
	machine_depreciation_reserves_amount numeric(20, 2) NULL DEFAULT 0, -- 机器设备_减值准备发生额
	other_cost_balance numeric(20, 2) NULL DEFAULT 0, -- 其他_成本余额
	other_cost_amount numeric(20, 2) NULL DEFAULT 0, -- 其他_成本发生额
	other_depreciation_reserves_balance numeric(20, 2) NULL DEFAULT 0, -- 其他_减值准备余额
	other_depreciation_reserves_amount numeric(20, 2) NULL DEFAULT 0, -- 其他_减值准备发生额
	receivable_factoring_principal_balance numeric(20, 2) NULL DEFAULT 0, -- 应收保理本金余额
	receivable_factoring_principal_amount numeric(20, 2) NULL DEFAULT 0, -- 应收保理本金发生额
	receivable_factoring_interest_balance numeric(20, 2) NULL DEFAULT 0, -- 应收保理利息调整余额
	receivable_factoring_interest_amount numeric(20, 2) NULL DEFAULT 0, -- 应收保理利息调整发生额
	payable_discount_cost_outtax_balance numeric(20, 2) NULL DEFAULT 0, -- 应收贴息手续费_销项税余额
	payable_discount_cost_outtax_amount numeric(20, 2) NULL DEFAULT 0, -- 应收贴息手续费_销项税发生额
	receivable_rent_investment_property_balance numeric(20, 2) NULL DEFAULT 0, -- 投资性房地产应收租金 余额
	receivable_rent_investment_property_amount numeric(20, 2) NULL DEFAULT 0, -- 投资性房地产应收租金 发生额
	receivable_outtax_investment_property_balance numeric(20, 2) NULL DEFAULT 0, -- 投资性房地产应收销项税 余额
	receivable_outtax_investment_property_amount numeric(20, 2) NULL DEFAULT 0, -- 投资性房地产应收销项税 发生额
	depreciation_reserves_trade_balance numeric(20, 2) NULL DEFAULT 0, -- 应收贸易款坏账准备余额
	depreciation_reserves_trade_amount numeric(20, 2) NULL DEFAULT 0, -- 应收贸易款坏账准备发生额
	depreciation_reserves_service_balance numeric(20, 2) NULL DEFAULT 0, -- 应收服务费坏账准备余额
	depreciation_reserves_service_amount numeric(20, 2) NULL DEFAULT 0, -- 应收服务费坏账准备发生额
	depreciation_reserves_other_receivable_balance numeric(20, 2) NULL DEFAULT 0, -- 其他应收款项坏账准备余额
	depreciation_reserves_other_receivable_amount numeric(20, 2) NULL DEFAULT 0, -- 其他应收款项坏账准备发生额
	depreciation_reserves_temp_balance numeric(20, 2) NULL DEFAULT 0, -- 暂支及个人往来坏账准备余额
	depreciation_reserves_temp_amount numeric(20, 2) NULL DEFAULT 0, -- 暂支及个人往来坏账准备发生额
	depreciation_reserves_other_margin_balance numeric(20, 2) NULL DEFAULT 0, -- 其他保证金坏账准备余额
	depreciation_reserves_other_margin_amount numeric(20, 2) NULL DEFAULT 0, -- 其他保证金坏账准备发生额
	depreciation_reserves_investment_property_balance numeric(20, 2) NULL DEFAULT 0, -- 投资性房地产应收租金坏账准备余额
	depreciation_reserves_investment_property_amount numeric(20, 2) NULL DEFAULT 0, -- 投资性房地产应收租金坏账准备发生额
	depreciation_reserves_deposit_balance numeric(20, 2) NULL DEFAULT 0, -- 押金坏账准备余额
	depreciation_reserves_deposit_amount numeric(20, 2) NULL DEFAULT 0, -- 押金坏账准备发生额
	depreciation_reserves_litigation_balance numeric(20, 2) NULL DEFAULT 0, -- 应收诉讼保全费坏账准备余额
	depreciation_reserves_litigation_amount numeric(20, 2) NULL DEFAULT 0, -- 应收诉讼保全费坏账准备发生额
	depreciation_reserves_litigation_margin_balance numeric(20, 2) NULL DEFAULT 0, -- 应收诉讼保证金坏账准备余额
	depreciation_reserves_litigation_margin_amount numeric(20, 2) NULL DEFAULT 0, -- 应收诉讼保证金坏账准备发生额
	depreciation_reserves_other_balance numeric(20, 2) NULL DEFAULT 0, -- 其他坏账准备余额
	depreciation_reserves_other_amount numeric(20, 2) NULL DEFAULT 0, -- 其他坏账准备发生额
	depreciation_reserves_individual_balance numeric(20, 2) NULL DEFAULT 0, -- 减值准备_单项余额
	depreciation_reserves_individual_amount numeric(20, 2) NULL DEFAULT 0, -- 减值准备_单项发生额
	depreciation_reserves_bill_balance numeric(20, 2) NULL DEFAULT 0, -- 应收票据坏账准备余额
	depreciation_reserves_bill_amount numeric(20, 2) NULL DEFAULT 0, -- 应收票据坏账准备发生额
	depreciation_reserves_gov_balance numeric(20, 2) NULL DEFAULT 0, -- 长期应收政府与社会资本合作项目减值准备余额
	depreciation_reserves_gov_amount numeric(20, 2) NULL DEFAULT 0, -- 长期应收政府与社会资本合作项目减值准备发生额
	depreciation_reserves_other_long_receibles_balance numeric(20, 2) NULL DEFAULT 0, -- 其他长期应收款减值准备余额
	depreciation_reserves_other_long_receibles_amount numeric(20, 2) NULL DEFAULT 0, -- 其他长期应收款减值准备发生额
	depreciation_reserves_long_related_balance numeric(20, 2) NULL DEFAULT 0, -- 长期应收款关联公司往来减值准备余额
	depreciation_reserves_long_related_amount numeric(20, 2) NULL DEFAULT 0, -- 长期应收款关联公司往来减值准备发生额
	depreciation_reserves_interest_balance numeric(20, 2) NULL DEFAULT 0, -- 借款应收利息减值准备余额
	depreciation_reserves_interest_amount numeric(20, 2) NULL DEFAULT 0, -- 借款应收利息减值准备发生额
	receivable_principal_balance numeric(20, 2) NULL DEFAULT 0, -- 本金余额
	receivable_principal_amount numeric(20, 2) NULL DEFAULT 0, -- 本金发生额
	receivable_interest_adjustment_balance numeric(20, 2) NULL DEFAULT 0, -- 利息调整余额
	receivable_interest_adjustment_amount numeric(20, 2) NULL DEFAULT 0, -- 利息调整发生额
	receivable_principal_nonfinancial_balance numeric(20, 2) NULL DEFAULT 0, -- 贷款_非金融机构_本金余额
	receivable_principal_nonfinancial_amount numeric(20, 2) NULL DEFAULT 0, -- 贷款_非金融机构_本金发生额
	receivable_commission_nonfinancial_balance numeric(20, 2) NULL DEFAULT 0, -- 贷款_非金融机构_应收手续费余额
	receivable_commission_nonfinancial_amount numeric(20, 2) NULL DEFAULT 0, -- 贷款_非金融机构_应收手续费发生额
	receivable_interest_adjustment_nonfinancial_balance numeric(20, 2) NULL DEFAULT 0, -- 贷款_非金融机构_利息调整余额
	receivable_interest_adjustment_nonfinancial_amount numeric(20, 2) NULL DEFAULT 0, -- 贷款_非金融机构_利息调整发生额
	depreciation_reserves_bank_balance numeric(20, 2) NULL DEFAULT 0, -- 银行存款减值准备余额
	depreciation_reserves_bank_amount numeric(20, 2) NULL DEFAULT 0, -- 银行存款减值准备发生额
	depreciation_reserves_buying_back_balance numeric(20, 2) NULL DEFAULT 0, -- 买入返售金融资产减值准备余额
	depreciation_reserves_buying_back_amount numeric(20, 2) NULL DEFAULT 0, -- 买入返售金融资产减值准备发生额
	depreciation_reserves_term_deposit_interest_balance numeric(20, 2) NULL DEFAULT 0, -- 定期存款应收利息减值准备余额
	depreciation_reserves_term_deposit_interest_amount numeric(20, 2) NULL DEFAULT 0, -- 定期存款应收利息减值准备发生额
	depreciation_reserves_borrowings_interest_balance numeric(20, 2) NULL DEFAULT 0, -- 借款应收利息减值准备余额
	depreciation_reserves_borrowings_interest_amount numeric(20, 2) NULL DEFAULT 0, -- 借款应收利息减值准备发生额
	depreciation_reserves_buying_back_interest_balance numeric(20, 2) NULL DEFAULT 0, -- 买入返售金融资产应收利息减值准备余额
	depreciation_reserves_buying_back_interest_amount numeric(20, 2) NULL DEFAULT 0, -- 买入返售金融资产应收利息减值准备发生额
	depreciation_reserves_financial_product_interest_balance numeric(20, 2) NULL DEFAULT 0, -- 银行理财产品应收利息减值准备余额
	depreciation_reserves_financial_product_interest_amount numeric(20, 2) NULL DEFAULT 0, -- 银行理财产品应收利息减值准备发生额
	depreciation_reserves_structured_deposit_interest_balance numeric(20, 2) NULL DEFAULT 0, -- 结构性存款应收利息减值准备余额
	depreciation_reserves_structured_deposit_interest_amount numeric(20, 2) NULL DEFAULT 0, -- 结构性存款应收利息减值准备发生额
	depreciation_reserves_fvpl_interest_other_balance numeric(20, 2) NULL DEFAULT 0, -- FVPL_应收利息减值准备余额
	depreciation_reserves_fvpl_interest_other_amount numeric(20, 2) NULL DEFAULT 0, -- FVPL_应收利息减值准备发生额
	depreciation_reserves_bond_fvoci_interest_balance numeric(20, 2) NULL DEFAULT 0, -- FVOCI_应收利息减值准备余额
	depreciation_reserves_bond_fvoci_interest_amount numeric(20, 2) NULL DEFAULT 0, -- FVOCI_应收利息减值准备发生额
	depreciation_reserves_fvoci_interest_other_balance numeric(20, 2) NULL DEFAULT 0, -- FVOCI_其他应收利息减值准备余额
	depreciation_reserves_fvoci_interest_other_amount numeric(20, 2) NULL DEFAULT 0, -- FVOCI_其他应收利息减值准备发生额
	depreciation_reserves_bond_ac_interest_balance numeric(20, 2) NULL DEFAULT 0, -- 以摊余成本计量的债券应收利息减值准备余额
	depreciation_reserves_bond_ac_interest_amount numeric(20, 2) NULL DEFAULT 0, -- 以摊余成本计量的债券应收利息减值准备发生额
	depreciation_reserves_ac_interest_other_balance numeric(20, 2) NULL DEFAULT 0, -- 其他以摊余成本计量的金融资产应收利息减值准备余额
	depreciation_reserves_ac_interest_other_amount numeric(20, 2) NULL DEFAULT 0, -- 其他以摊余成本计量的金融资产应收利息减值准备发生额
	depreciation_reserves_bond_ac_balance numeric(20, 2) NULL DEFAULT 0, -- 以摊余成本计量的债券减值准备余额
	depreciation_reserves_bond_ac_amount numeric(20, 2) NULL DEFAULT 0, -- 以摊余成本计量的债券减值准备发生额
	depreciation_reserves_turst_ac_balance numeric(20, 2) NULL DEFAULT 0, -- 以摊余成本计量的信托计划减值准备余额
	depreciation_reserves_turst_ac_amount numeric(20, 2) NULL DEFAULT 0, -- 以摊余成本计量的信托计划减值准备发生额
	depreciation_reserves_other_financial_ac_balance numeric(20, 2) NULL DEFAULT 0, -- 以摊余成本计量的其他金融资产减值准备余额
	depreciation_reserves_other_financial_ac_amount numeric(20, 2) NULL DEFAULT 0, -- 以摊余成本计量的其他金融资产减值准备发生额
	depreciation_reserves_other_asset_ac_balance numeric(20, 2) NULL DEFAULT 0, -- 以摊余成本计量的其他资产减值准备余额
	depreciation_reserves_other_asset_ac_amount numeric(20, 2) NULL DEFAULT 0, -- 以摊余成本计量的其他资产减值准备发生额
	depreciation_reserves_bond_fvoci_balance numeric(20, 2) NULL DEFAULT 0, -- FVOCI_债券减值准备余额
	depreciation_reserves_bond_fvoci_amount numeric(20, 2) NULL DEFAULT 0, -- FVOCI_债券减值准备发生额
	depreciation_reserves_other_financial_fvoci_balance numeric(20, 2) NULL DEFAULT 0, -- FVOCI_其他金融资产减值准备余额
	depreciation_reserves_other_financial_fvoci_amount numeric(20, 2) NULL DEFAULT 0, -- FVOCI_其他金融资产减值准备发生额
	depreciation_reserves_subsidiary_balance numeric(20, 2) NULL DEFAULT 0, -- 投资子公司减值准备余额
	depreciation_reserves_subsidiary_amount numeric(20, 2) NULL DEFAULT 0, -- 投资子公司减值准备发生额
	depreciation_reserves_jv_balance numeric(20, 2) NULL DEFAULT 0, -- 投资合营企业减值准备余额
	depreciation_reserves_jv_amount numeric(20, 2) NULL DEFAULT 0, -- 投资合营企业减值准备发生额
	depreciation_reserves_associate_balance numeric(20, 2) NULL DEFAULT 0, -- 投资联营企业减值准备余额
	depreciation_reserves_associate_amount numeric(20, 2) NULL DEFAULT 0, -- 投资联营企业减值准备发生额
	receivable_virtual_balance numeric(20, 2) NULL DEFAULT 0, -- 虚拟收付款余额
	receivable_virtual_amount numeric(20, 2) NULL DEFAULT 0, -- 虚拟收付款发生额
	unrealized_revenue_other_balance numeric(20, 2) NULL DEFAULT 0, -- 未实现其他收益余额
	unrealized_revenue_other_amount numeric(20, 2) NULL DEFAULT 0, -- 未实现其他收益发生额
	payable_factoring_balance numeric(20, 2) NULL DEFAULT 0, -- 应付保理款余额
	payable_factoring_amount numeric(20, 2) NULL DEFAULT 0, -- 应付保理款发生额
	payable_entrust_balance numeric(20, 2) NULL DEFAULT 0, -- 应付委贷款余额
	payable_entrust_amount numeric(20, 2) NULL DEFAULT 0, -- 应付委贷款发生额
	collect_claims_balance numeric(20, 2) NULL DEFAULT 0, -- 代收理赔款余额
	collect_claims_amount numeric(20, 2) NULL DEFAULT 0, -- 代收理赔款发生额
	payable_other_balance numeric(20, 2) NULL DEFAULT 0, -- 应付其他款项余额
	payable_other_amount numeric(20, 2) NULL DEFAULT 0, -- 应付其他款项发生额
	prereceived_rent_balance numeric(20, 2) NULL DEFAULT 0, -- 预收租赁款余额
	prereceived_rent_amount numeric(20, 2) NULL DEFAULT 0, -- 预收租赁款发生额
	payable_related_party_balance numeric(20, 2) NULL DEFAULT 0, -- 其他应付款_关联公司往来 余额
	payable_related_party_amount numeric(20, 2) NULL DEFAULT 0, -- 其他应付款_关联公司往来 发生额
	other_payable_spv_balance numeric(20, 2) NULL DEFAULT 0, -- 其他应付款_资产支持专项计划余额
	other_payable_spv_amount numeric(20, 2) NULL DEFAULT 0, -- 其他应付款_资产支持专项计划发生额
	other_payable_trust_balance numeric(20, 2) NULL DEFAULT 0, -- 其他应付款_信托计划余额
	other_payable_trust_amount numeric(20, 2) NULL DEFAULT 0, -- 其他应付款_信托计划发生额
	other_payable_claim_asset_balance numeric(20, 2) NULL DEFAULT 0, -- 其他应付款_出表保理资产余额
	other_payable_claim_asset_amount numeric(20, 2) NULL DEFAULT 0, -- 其他应付款_出表保理资产发生额
	other_payable_rent_balance numeric(20, 2) NULL DEFAULT 0, -- 其他应付款_租金余额余额
	other_payable_rent_amount numeric(20, 2) NULL DEFAULT 0, -- 其他应付款_租金余额发生额
	other_payable_residual_balance numeric(20, 2) NULL DEFAULT 0, -- 其他应付款_残值余额余额
	other_payable_residual_amount numeric(20, 2) NULL DEFAULT 0, -- 其他应付款_残值余额发生额
	other_payable_other_balance numeric(20, 2) NULL DEFAULT 0, -- 其他应付款_其他余额
	other_payable_other_amount numeric(20, 2) NULL DEFAULT 0, -- 其他应付款_其他发生额
	other_payable_claim_balance numeric(20, 2) NULL DEFAULT 0, -- 其他应付款_保理款余额余额
	other_payable_claim_amount numeric(20, 2) NULL DEFAULT 0, -- 其他应付款_保理款余额发生额
	other_payable_claim_other_balance numeric(20, 2) NULL DEFAULT 0, -- 其他应付款_保理款余额_其他余额
	other_payable_claim_other_amount numeric(20, 2) NULL DEFAULT 0, -- 其他应付款_保理款余额_其他发生额
	other_payable_collect_balance numeric(20, 2) NULL DEFAULT 0, -- 其他应付款_代收出表保理资产款项余额
	other_payable_collect_amount numeric(20, 2) NULL DEFAULT 0, -- 其他应付款_代收出表保理资产款项发生额
	supplier_pool_balance numeric(20, 2) NULL DEFAULT 0, -- 供应商资金池余额
	supplier_pool_amount numeric(20, 2) NULL DEFAULT 0, -- 供应商资金池发生额
	agent_margin_balance numeric(20, 2) NULL DEFAULT 0, -- 代理商保证金余额
	agent_margin_amount numeric(20, 2) NULL DEFAULT 0, -- 代理商保证金发生额
	other_margin_balance numeric(20, 2) NULL DEFAULT 0, -- 其他保证金余额
	other_margin_amount numeric(20, 2) NULL DEFAULT 0, -- 其他保证金发生额
	rent_margin_balance numeric(20, 2) NULL DEFAULT 0, -- 房屋租赁保证金余额
	rent_margin_amount numeric(20, 2) NULL DEFAULT 0, -- 房屋租赁保证金发生额
	other_long_margin_balance numeric(20, 2) NULL DEFAULT 0, -- 其他长期应付保证金余额
	other_long_margin_amount numeric(20, 2) NULL DEFAULT 0, -- 其他长期应付保证金发生额
	other_business_income_balance numeric(20, 2) NULL DEFAULT 0, -- 其他主营业务收入 余额
	other_business_income_amount numeric(20, 2) NULL DEFAULT 0, -- 其他主营业务收入 发生额
	other_income_service_balance numeric(20, 2) NULL DEFAULT 0, -- 其他主营业务收入_手续费收入余额
	other_income_service_amount numeric(20, 2) NULL DEFAULT 0, -- 其他主营业务收入_手续费收入发生额
	interest_other_long_payables_balance numeric(20, 2) NULL DEFAULT 0, -- 其他长期应收款利息收入余额
	interest_other_long_payables_amount numeric(20, 2) NULL DEFAULT 0, -- 其他长期应收款利息收入发生额
	interest_other_financial_ac_balance numeric(20, 2) NULL DEFAULT 0, -- 以摊余成本计量的其他金融资产利息收入余额
	interest_other_financial_ac_amount numeric(20, 2) NULL DEFAULT 0, -- 以摊余成本计量的其他金融资产利息收入发生额
	lease_revenue6_balance numeric(20, 2) NULL DEFAULT 0, -- 租赁收益6%余额
	lease_revenue6_amount numeric(20, 2) NULL DEFAULT 0, -- 租赁收益6%发生额
	lease_revenue3_balance numeric(20, 2) NULL DEFAULT 0, -- 租赁收益3%余额
	lease_revenue3_amount numeric(20, 2) NULL DEFAULT 0, -- 租赁收益3%发生额
	other_income_lease_transfer_balance numeric(20, 2) NULL DEFAULT 0, -- 其他业务收入_融资租赁款转让收益余额
	other_income_lease_transfer_amount numeric(20, 2) NULL DEFAULT 0, -- 其他业务收入_融资租赁款转让收益发生额
	rent_investment_property_balance numeric(20, 2) NULL DEFAULT 0, -- 投资性房地产租金收入余额
	rent_investment_property_amount numeric(20, 2) NULL DEFAULT 0, -- 投资性房地产租金收入发生额
	other_income_factoring_transfer_balance numeric(20, 2) NULL DEFAULT 0, -- 其他业务收入_应收保理款转让收益余额
	other_income_factoring_transfer_amount numeric(20, 2) NULL DEFAULT 0, -- 其他业务收入_应收保理款转让收益发生额
	asset_dispose_gain_foreclosed_balance numeric(20, 2) NULL DEFAULT 0, -- 抵债资产处置收益余额
	asset_dispose_gain_foreclosed_amount numeric(20, 2) NULL DEFAULT 0, -- 抵债资产处置收益发生额
	asset_dispose_loss_foreclosed_balance numeric(20, 2) NULL DEFAULT 0, -- 抵债资产处置损失余额
	asset_dispose_loss_foreclosed_amount numeric(20, 2) NULL DEFAULT 0, -- 抵债资产处置损失发生额
	other_cost_lease_transfer_balance numeric(20, 2) NULL DEFAULT 0, -- 其他业务成本_融资租赁款转让收益余额
	other_cost_lease_transfer_amount numeric(20, 2) NULL DEFAULT 0, -- 其他业务成本_融资租赁款转让收益发生额
	other_cost_factoring_transfer_balance numeric(20, 2) NULL DEFAULT 0, -- 其他业务成本_应收保理款转让收益余额
	other_cost_factoring_transfer_amount numeric(20, 2) NULL DEFAULT 0, -- 其他业务成本_应收保理款转让收益发生额
	assessment_fee_balance numeric(20, 2) NULL DEFAULT 0, -- 评估费余额
	assessment_fee_amount numeric(20, 2) NULL DEFAULT 0, -- 评估费发生额
	attorney_fee_balance numeric(20, 2) NULL DEFAULT 0, -- 律师费余额
	attorney_fee_amount numeric(20, 2) NULL DEFAULT 0, -- 律师费发生额
	intermediary_fee_other_balance numeric(20, 2) NULL DEFAULT 0, -- 其他聘请中介机构费余额
	intermediary_fee_other_amount numeric(20, 2) NULL DEFAULT 0, -- 其他聘请中介机构费发生额
	consultation_fee_balance numeric(20, 2) NULL DEFAULT 0, -- 咨询费余额
	consultation_fee_amount numeric(20, 2) NULL DEFAULT 0, -- 咨询费发生额
	notary_fee_balance numeric(20, 2) NULL DEFAULT 0, -- 公证费余额
	notary_fee_amount numeric(20, 2) NULL DEFAULT 0, -- 公证费发生额
	lease_asset_recovery_fee_balance numeric(20, 2) NULL DEFAULT 0, -- 回收租赁资产杂费余额
	lease_asset_recovery_fee_amount numeric(20, 2) NULL DEFAULT 0, -- 回收租赁资产杂费发生额
	depreciation_loss_trade_balance numeric(20, 2) NULL DEFAULT 0, -- 应收贸易款坏账损失余额
	depreciation_loss_trade_amount numeric(20, 2) NULL DEFAULT 0, -- 应收贸易款坏账损失发生额
	depreciation_loss_service_balance numeric(20, 2) NULL DEFAULT 0, -- 应收服务费坏账损失余额
	depreciation_loss_service_amount numeric(20, 2) NULL DEFAULT 0, -- 应收服务费坏账损失发生额
	depreciation_loss_other_receivables_balance numeric(20, 2) NULL DEFAULT 0, -- 其他应收款项坏账损失余额
	depreciation_loss_other_receivables_amount numeric(20, 2) NULL DEFAULT 0, -- 其他应收款项坏账损失发生额
	depreciation_loss_temp_balance numeric(20, 2) NULL DEFAULT 0, -- 暂支及个人往来坏账损失余额
	depreciation_loss_temp_amount numeric(20, 2) NULL DEFAULT 0, -- 暂支及个人往来坏账损失发生额
	depreciation_loss_other_margin_balance numeric(20, 2) NULL DEFAULT 0, -- 其他保证金坏账损失余额
	depreciation_loss_other_margin_amount numeric(20, 2) NULL DEFAULT 0, -- 其他保证金坏账损失发生额
	depreciation_loss_rent_investment_property_balance numeric(20, 2) NULL DEFAULT 0, -- 投资性房地产应收租金减值损失余额
	depreciation_loss_rent_investment_property_amount numeric(20, 2) NULL DEFAULT 0, -- 投资性房地产应收租金减值损失发生额
	depreciation_loss_deposit_balance numeric(20, 2) NULL DEFAULT 0, -- 押金减值损失余额
	depreciation_loss_deposit_amount numeric(20, 2) NULL DEFAULT 0, -- 押金减值损失发生额
	depreciation_loss_litigation_balance numeric(20, 2) NULL DEFAULT 0, -- 应收诉讼保全费减值损失余额
	depreciation_loss_litigation_amount numeric(20, 2) NULL DEFAULT 0, -- 应收诉讼保全费减值损失发生额
	depreciation_loss_litigation_margin_balance numeric(20, 2) NULL DEFAULT 0, -- 应收诉讼保证金减值损失余额
	depreciation_loss_litigation_margin_amount numeric(20, 2) NULL DEFAULT 0, -- 应收诉讼保证金减值损失发生额
	depreciation_loss_other_receivable_balance numeric(20, 2) NULL DEFAULT 0, -- 减值损失_其他余额
	depreciation_loss_other_receivable_amount numeric(20, 2) NULL DEFAULT 0, -- 减值损失_其他发生额
	depreciation_loss_reverse_balance numeric(20, 2) NULL DEFAULT 0, -- 应收融资租赁款减值损失_坏账注销转回余额
	depreciation_loss_reverse_amount numeric(20, 2) NULL DEFAULT 0, -- 应收融资租赁款减值损失_坏账注销转回发生额
	depreciation_loss_equity_investment_balance numeric(20, 2) NULL DEFAULT 0, -- 长期股权投资减值损失余额
	depreciation_loss_equity_investment_amount numeric(20, 2) NULL DEFAULT 0, -- 长期股权投资减值损失发生额
	depreciation_loss_interest_balance numeric(20, 2) NULL DEFAULT 0, -- 应收利息减值损失余额
	depreciation_loss_interest_amount numeric(20, 2) NULL DEFAULT 0, -- 应收利息减值损失发生额
	depreciation_loss_bill_balance numeric(20, 2) NULL DEFAULT 0, -- 应收票据减值损失余额
	depreciation_loss_bill_amount numeric(20, 2) NULL DEFAULT 0, -- 应收票据减值损失发生额
	depreciation_loss_collateral_balance numeric(20, 2) NULL DEFAULT 0, -- 抵债资产减值损失余额
	depreciation_loss_collateral_amount numeric(20, 2) NULL DEFAULT 0, -- 抵债资产减值损失发生额
	depreciation_loss_financial_balance numeric(20, 2) NULL DEFAULT 0, -- 金融资产减值损失余额
	depreciation_loss_financial_amount numeric(20, 2) NULL DEFAULT 0, -- 金融资产减值损失发生额
	depreciation_loss_buying_back_balance numeric(20, 2) NULL DEFAULT 0, -- 买入返售金融资产减值损失余额
	depreciation_loss_buying_back_amount numeric(20, 2) NULL DEFAULT 0, -- 买入返售金融资产减值损失发生额
	depreciation_loss_bond_ac_balance numeric(20, 2) NULL DEFAULT 0, -- 以摊余成本计量的债券减值损失余额
	depreciation_loss_bond_ac_amount numeric(20, 2) NULL DEFAULT 0, -- 以摊余成本计量的债券减值损失发生额
	depreciation_loss_financial_fvoci_balance numeric(20, 2) NULL DEFAULT 0, -- FVOCI_金融资产减值损失余额
	depreciation_loss_financial_fvoci_amount numeric(20, 2) NULL DEFAULT 0, -- FVOCI_金融资产减值损失发生额
	depreciation_loss_bank_balance numeric(20, 2) NULL DEFAULT 0, -- 银行存款减值损失余额
	depreciation_loss_bank_amount numeric(20, 2) NULL DEFAULT 0, -- 银行存款减值损失发生额
	depreciation_loss_trust_ac_balance numeric(20, 2) NULL DEFAULT 0, -- 以摊余成本计量的信托计划减值损失余额
	depreciation_loss_trust_ac_amount numeric(20, 2) NULL DEFAULT 0, -- 以摊余成本计量的信托计划减值损失发生额
	depreciation_loss_other_asset_ac_balance numeric(20, 2) NULL DEFAULT 0, -- 以摊余成本计量的其他资产减值损失余额
	depreciation_loss_other_asset_ac_amount numeric(20, 2) NULL DEFAULT 0, -- 以摊余成本计量的其他资产减值损失发生额
	depreciation_loss_other_financial_ac_balance numeric(20, 2) NULL DEFAULT 0, -- 以摊余成本计量的其他金融资产减值损失余额
	depreciation_loss_other_financial_ac_amount numeric(20, 2) NULL DEFAULT 0, -- 以摊余成本计量的其他金融资产减值损失发生额
	depreciation_loss_gov_balance numeric(20, 2) NULL DEFAULT 0, -- 长期应收政府与社会资本合作项目减值损失余额
	depreciation_loss_gov_amount numeric(20, 2) NULL DEFAULT 0, -- 长期应收政府与社会资本合作项目减值损失发生额
	depreciation_loss_other_long_receivables_balance numeric(20, 2) NULL DEFAULT 0, -- 其他长期应收款减值损失余额
	depreciation_loss_other_long_receivables_amount numeric(20, 2) NULL DEFAULT 0, -- 其他长期应收款减值损失发生额
	depreciation_loss_other_long_related_balance numeric(20, 2) NULL DEFAULT 0, -- 长期应收款关联公司往来减值损失余额
	depreciation_loss_other_long_related_amount numeric(20, 2) NULL DEFAULT 0, -- 长期应收款关联公司往来减值损失发生额
	depreciation_loss_borrowings_interest_balance numeric(20, 2) NULL DEFAULT 0, -- 借款应收利息减值损失余额
	depreciation_loss_borrowings_interest_amount numeric(20, 2) NULL DEFAULT 0, -- 借款应收利息减值损失发生额
	depreciation_loss_investment_property_balance numeric(20, 2) NULL DEFAULT 0, -- 投资性房地产减值损失余额
	depreciation_loss_investment_property_amount numeric(20, 2) NULL DEFAULT 0, -- 投资性房地产减值损失发生额
	depreciation_loss_other_balance numeric(20, 2) NULL DEFAULT 0, -- 其他减值损失余额
	depreciation_loss_other_amount numeric(20, 2) NULL DEFAULT 0, -- 其他减值损失发生额
	publication_fee_balance numeric(20, 2) NULL DEFAULT 0, -- 公告费余额
	publication_fee_amount numeric(20, 2) NULL DEFAULT 0, -- 公告费发生额
	receive_sum_debt_restructure_balance numeric(20, 2) NULL DEFAULT 0, -- 债务重组项目应收总额 余额
	receive_sum_debt_restructure_amount numeric(20, 2) NULL DEFAULT 0, -- 债务重组项目应收总额 发生额
	unrealized_revenue_debt_restructure_balance numeric(20, 2) NULL DEFAULT 0, -- 债务重组项目未实现收益余额
	unrealized_revenue_debt_restructure_amount numeric(20, 2) NULL DEFAULT 0, -- 债务重组项目未实现收益发生额
	receivable_outtax_debt_restructure_balance numeric(20, 2) NULL DEFAULT 0, -- 债务重组项目应收销项税余额
	receivable_outtax_debt_restructure_amount numeric(20, 2) NULL DEFAULT 0, -- 债务重组项目应收销项税发生额
	unrealized_revenue_other_debt_restructure_balance numeric(20, 2) NULL DEFAULT 0, -- 债务重组项目未实现其他收益余额
	unrealized_revenue_other_debt_restructure_amount numeric(20, 2) NULL DEFAULT 0, -- 债务重组项目未实现其他收益发生额
	collect_payment_balance numeric(20, 2) NULL DEFAULT 0, -- 代收款项余额
	collect_payment_amount numeric(20, 2) NULL DEFAULT 0, -- 代收款项发生额
	receivable_default_interest_balance numeric(20, 2) NULL DEFAULT 0, -- 应收罚息余额
	receivable_default_interest_amount numeric(20, 2) NULL DEFAULT 0, -- 应收罚息发生额
	receivable_terminate_balance numeric(20, 2) NULL DEFAULT 0, -- 应收变更手续费余额
	receivable_terminate_amount numeric(20, 2) NULL DEFAULT 0, -- 应收变更手续费发生额
	litigation_virtual_balance numeric(20, 2) NULL DEFAULT 0, -- 诉讼费支付_虚拟余额
	litigation_virtual_amount numeric(20, 2) NULL DEFAULT 0, -- 诉讼费支付_虚拟发生额
	period_code int4 NOT NULL, -- 会计期间
	eas_voucher_id varchar(50) NULL, -- 金蝶凭证ID
	bill_contract_code varchar(60) NULL, -- 借款合同编号
	receivable_other_revenue_balance numeric(20, 2) NULL DEFAULT 0, -- 应收其他租赁相关收入余额
	receivable_other_revenue_amount numeric(20, 2) NULL DEFAULT 0, -- 应收其他租赁相关收入发生额
	receivable_damages_revenue_balance numeric(20, 2) NULL DEFAULT 0, -- 应收违约金收入余额
	receivable_damages_revenue_amount numeric(20, 2) NULL DEFAULT 0, -- 应收违约金收入发生额
	continue_involving_assets_balance numeric(20, 2) NULL DEFAULT 0, -- 继续涉入资产余额
	continue_involving_assets_amount numeric(20, 2) NULL DEFAULT 0, -- 继续涉入资产发生额
	payable_margin_entrusted_loans_balance numeric(20, 2) NULL DEFAULT 0, -- 应付委托贷款保证金余额
	payable_margin_entrusted_loans_amount numeric(20, 2) NULL DEFAULT 0, -- 应付委托贷款保证金发生额
	continue_involving_debts_balance numeric(20, 2) NULL DEFAULT 0, -- 继续涉入负债余额
	continue_involving_debts_amount numeric(20, 2) NULL DEFAULT 0, -- 继续涉入负债发生额
	debt_restructuring_income_balance numeric(20, 2) NULL DEFAULT 0, -- 债务重组投资收益余额
	debt_restructuring_income_amount numeric(20, 2) NULL DEFAULT 0, -- 债务重组投资收益发生额
	lease_transfer_income_balance numeric(20, 2) NULL DEFAULT 0, -- 融资租赁款转让收益余额
	lease_transfer_income_amount numeric(20, 2) NULL DEFAULT 0, -- 融资租赁款转让收益发生额
	other_business_cost_balance numeric(20, 2) NULL DEFAULT 0, -- 其他业务成本余额
	other_business_cost_amount numeric(20, 2) NULL DEFAULT 0, -- 其他业务成本发生额
	financial_institution_fee_balance numeric(20, 2) NULL DEFAULT 0, -- 金融机构手续费余额
	financial_institution_fee_amount numeric(20, 2) NULL DEFAULT 0, -- 金融机构手续费发生额
	payable_bill_balance numeric(20, 2) NULL DEFAULT 0, -- 应付票据余额
	payable_bill_amount numeric(20, 2) NULL DEFAULT 0, -- 应付票据发生额
	receivable_bill_balance numeric(20, 2) NULL DEFAULT 0, -- 应收票据余额
	receivable_bill_amount numeric(20, 2) NULL DEFAULT 0, -- 应收票据发生额
	CONSTRAINT eg_contract_balance_temp_partition_pk PRIMARY KEY (id, period_code)
);
CREATE INDEX eg_contract_balance_temp_eas_voucher_id_idx ON ONLY financialdb.eg_contract_balance_temp USING btree (eas_voucher_id);
CREATE INDEX eg_contract_balance_temp_partition_business_code_idx ON ONLY financialdb.eg_contract_balance_temp USING btree (business_code);
CREATE INDEX eg_contract_balance_temp_partition_client_code_idx ON ONLY financialdb.eg_contract_balance_temp USING btree (client_code);
CREATE INDEX eg_contract_balance_temp_partition_org_id_idx ON ONLY financialdb.eg_contract_balance_temp USING btree (org_id);
CREATE INDEX eg_contract_balance_temp_partition_voucher_id_idx ON ONLY financialdb.eg_contract_balance_temp USING btree (voucher_id);
CREATE INDEX idx_contract_balance_partition_index_id_temp ON ONLY financialdb.eg_contract_balance_temp USING btree (contract_code);
COMMENT ON TABLE financialdb.eg_contract_balance_temp IS '合同余额表-临时表';

-- Column comments

COMMENT ON COLUMN financialdb.eg_contract_balance_temp.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.voucher_id IS '凭证ID';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.interface_data_id IS '接口表ID';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.system_code IS '来源系统编码';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.business_code IS '业务编码';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.business_date IS '业务日期';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.voucher_date IS '凭证日期';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.scene_code IS '场景编码';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.contract_code IS '合同编码';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.client_code IS '客户编码';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.client_type IS '客户类型';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.org_id IS '机构编码';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.del_flag IS '删除标识(0:未删除,1:已删除)';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_rent_balance IS '应收租金余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_rent_amount IS '应收租金发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_downpayment_balance IS '应收首付款余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_downpayment_amount IS '应收首付款发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_residual_value_balance IS '应收期末残值余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_residual_value_amount IS '应收期末残值发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_commission_balance IS '应收手续费余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_commission_amount IS '应收手续费发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_rebate_balance IS '应收返利余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_rebate_amount IS '应收返利发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_insurance_balance IS '应收保险费余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_insurance_amount IS '应收保险费发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_otherincome_balance IS '应收其他收入余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_otherincome_amount IS '应收其他收入发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_outtax_balance IS '应收销项税余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_outtax_amount IS '应收销项税发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_outputtax_base_balance IS '应收销项税-本金余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_outputtax_base_amount IS '应收销项税-本金发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_unconfirm_receipt_balance IS '未确认收款余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_unconfirm_receipt_amount IS '未确认收款发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_service_balance IS '应收服务费余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_service_amount IS '应收服务费发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_service_outtax_balance IS '应收服务费-销项税余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_service_outtax_amount IS '应收服务费-销项税发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.unrealized_revenue_balance IS '未实现收益余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.unrealized_revenue_amount IS '未实现收益发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.lease_revenue_balance IS '融资租赁收益余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.lease_revenue_amount IS '融资租赁收益发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.service_revenue_balance IS '服务收入余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.service_revenue_amount IS '服务收入发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.insurance_differ_balance IS '保险费差额余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.insurance_differ_amount IS '保险费差额发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.dinterest_revenue_balance IS '罚息收入余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.dinterest_revenue_amount IS '罚息收入发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.terminate_balance IS '合同解约及更改手续费余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.terminate_amount IS '合同解约及更改手续费发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_revenue_balance IS '其他租赁相关收入余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_revenue_amount IS '其他租赁相关收入发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.damages_revenue_balance IS '违约金收入余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.damages_revenue_amount IS '违约金收入发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.margin_interest_balance IS '融资租赁业务保证金利息收入余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.margin_interest_amount IS '融资租赁业务保证金利息收入发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_device_estimate_balance IS '应付租赁设备款-暂估余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_device_estimate_amount IS '应付租赁设备款-暂估发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_device_balance IS '应付租赁设备款余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_device_amount IS '应付租赁设备款发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_other_cost_estimate_balance IS '应付其他租赁成本-暂估余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_other_cost_estimate_amount IS '应付其他租赁成本-暂估发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_other_cost_balance IS '应付其他租赁成本余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_other_cost_amount IS '应付其他租赁成本发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_agency_estimate_balance IS '应付经销商服务费-暂估余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_agency_estimate_amount IS '应付经销商服务费-暂估发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_agency_balance IS '应付经销商服务费余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_agency_amount IS '应付经销商服务费发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_vehicle_estimate_balance IS '应付收车费-暂估余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_vehicle_estimate_amount IS '应付收车费-暂估发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_vehicle_balance IS '应付收车费余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_vehicle_amount IS '应付收车费发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_band_cost_estimate_balance IS '应付手环成本_暂估余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_band_cost_estimate_amount IS '应付手环成本_暂估发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_band_cost_balance IS '应付手环成本余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_band_cost_amount IS '应付手环成本发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_pledge_estimate_balance IS '应付抵押费_暂估余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_pledge_estimate_amount IS '应付抵押费_暂估发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_pledge_balance IS '应付抵押费余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_pledge_amount IS '应付抵押费发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_unpledge_estimate_balance IS '应付解抵押费_暂估余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_unpledge_estimate_amount IS '应付解抵押费_暂估发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_unpledge_balance IS '应付解抵押费余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_unpledge_amount IS '应付解抵押费发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.lessee_margin_balance IS '承租人保证金余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.lessee_margin_amount IS '承租人保证金发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.supplier_margin_balance IS '供应商及代理商保证金余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.supplier_margin_amount IS '供应商及代理商保证金发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_insurance_estimate_balance IS '应付保险费-暂估余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_insurance_estimate_amount IS '应付保险费-暂估发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_insurance_balance IS '应付保险费余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_insurance_amount IS '应付保险费发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.intax_balance IS '进项税额余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.intax_amount IS '进项税额发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.outtax_balance IS '销项税额余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.outtax_amount IS '销项税额发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.litigation_expenses_balance IS '诉讼费余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.litigation_expenses_amount IS '诉讼费发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_balance IS '减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_amount IS '减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_balance IS '减值损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_amount IS '减值损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receive_cost_balance IS '回收融资租赁设备成本余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receive_cost_amount IS '回收融资租赁设备成本发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.off_income_balance IS '表外租赁收入余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.off_income_amount IS '表外租赁收入发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.defer_income_balance IS '递延收益余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.defer_income_amount IS '递延收益发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_account_balance IS '应付未付款余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_account_amount IS '应付未付款发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_margin_balance IS '应付保证金余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_margin_amount IS '应付保证金发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_margin_year_balance IS '应付一年内保证金余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_margin_year_amount IS '应付一年内保证金发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.margin_ointerest_balance IS '保证金利息支出余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.margin_ointerest_amount IS '保证金利息支出发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receive_sum_balance IS '应收总额余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receive_sum_amount IS '应收总额发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receive_sum_outtax_balance IS '应收总额_销项税余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receive_sum_outtax_amount IS '应收总额_销项税发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receive_unrealized_revenue_balance IS '应收总额_未实现收益余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receive_unrealized_revenue_amount IS '应收总额_未实现收益发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_lessee_margin_year_balance IS '应付一年内承租人保证金余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_lessee_margin_year_amount IS '应付一年内承租人保证金发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_supplier_margin_year_balance IS '应付一年内供应商及代理商保证金余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_supplier_margin_year_amount IS '应付一年内供应商及代理商保证金发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_litigation_expenses_balance IS '应收诉讼费余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_litigation_expenses_amount IS '应收诉讼费发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_other_estimate_balance IS '应付其他款项-暂估余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_other_estimate_amount IS '应付其他款项-暂估发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_discount_cost_balance IS '应收贴息手续费余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_discount_cost_amount IS '应收贴息手续费发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_procedure_cost_balance IS '应付手续费成本余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_procedure_cost_amount IS '应付手续费成本发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.provisional_receipts_balance IS '暂收款项余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.provisional_receipts_amount IS '暂收款项发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.equipment_depreciation_reserves_balance IS '回收设备减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.equipment_depreciation_reserves_amount IS '回收设备减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.equipment_depreciation_loss_balance IS '回收设备减值损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.equipment_depreciation_loss_amount IS '回收设备减值损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.asset_dispose_gain_balance IS '资产处置收益余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.asset_dispose_gain_amount IS '资产处置收益发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.asset_dispose_loss_balance IS '资产处置损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.asset_dispose_loss_amount IS '资产处置损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_other_balance IS '其他应收款余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_other_amount IS '其他应收款发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_collection_transfer_balance IS '应收转让后收款余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_collection_transfer_amount IS '应收转让后收款发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_related_party_balance IS '其他应收款_关联公司往来余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_related_party_amount IS '其他应收款_关联公司往来发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.collection_transfer_balance IS '代收转让款项余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.collection_transfer_amount IS '代收转让款项发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.collect_payment_other_balance IS '其他代收款余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.collect_payment_other_amount IS '其他代收款发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_interest_balance IS '应收利息余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_interest_amount IS '应收利息发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_litigation_margin_balance IS '应收诉讼保证金余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_litigation_margin_amount IS '应收诉讼保证金发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.property_cost_balance IS '房产_成本余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.property_cost_amount IS '房产_成本发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.property_depreciation_reserves_balance IS '房产_减值准备 余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.property_depreciation_reserves_amount IS '房产_减值准备 发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.machine_cost_balance IS '机器设备_成本余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.machine_cost_amount IS '机器设备_成本发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.machine_depreciation_reserves_balance IS '机器设备_减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.machine_depreciation_reserves_amount IS '机器设备_减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_cost_balance IS '其他_成本余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_cost_amount IS '其他_成本发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_depreciation_reserves_balance IS '其他_减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_depreciation_reserves_amount IS '其他_减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_factoring_principal_balance IS '应收保理本金余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_factoring_principal_amount IS '应收保理本金发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_factoring_interest_balance IS '应收保理利息调整余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_factoring_interest_amount IS '应收保理利息调整发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_discount_cost_outtax_balance IS '应收贴息手续费_销项税余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_discount_cost_outtax_amount IS '应收贴息手续费_销项税发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_rent_investment_property_balance IS '投资性房地产应收租金	余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_rent_investment_property_amount IS '投资性房地产应收租金	发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_outtax_investment_property_balance IS '投资性房地产应收销项税	余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_outtax_investment_property_amount IS '投资性房地产应收销项税	发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_trade_balance IS '应收贸易款坏账准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_trade_amount IS '应收贸易款坏账准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_service_balance IS '应收服务费坏账准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_service_amount IS '应收服务费坏账准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_other_receivable_balance IS '其他应收款项坏账准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_other_receivable_amount IS '其他应收款项坏账准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_temp_balance IS '暂支及个人往来坏账准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_temp_amount IS '暂支及个人往来坏账准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_other_margin_balance IS '其他保证金坏账准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_other_margin_amount IS '其他保证金坏账准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_investment_property_balance IS '投资性房地产应收租金坏账准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_investment_property_amount IS '投资性房地产应收租金坏账准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_deposit_balance IS '押金坏账准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_deposit_amount IS '押金坏账准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_litigation_balance IS '应收诉讼保全费坏账准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_litigation_amount IS '应收诉讼保全费坏账准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_litigation_margin_balance IS '应收诉讼保证金坏账准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_litigation_margin_amount IS '应收诉讼保证金坏账准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_other_balance IS '其他坏账准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_other_amount IS '其他坏账准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_individual_balance IS '减值准备_单项余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_individual_amount IS '减值准备_单项发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_bill_balance IS '应收票据坏账准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_bill_amount IS '应收票据坏账准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_gov_balance IS '长期应收政府与社会资本合作项目减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_gov_amount IS '长期应收政府与社会资本合作项目减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_other_long_receibles_balance IS '其他长期应收款减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_other_long_receibles_amount IS '其他长期应收款减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_long_related_balance IS '长期应收款关联公司往来减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_long_related_amount IS '长期应收款关联公司往来减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_interest_balance IS '借款应收利息减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_interest_amount IS '借款应收利息减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_principal_balance IS '本金余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_principal_amount IS '本金发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_interest_adjustment_balance IS '利息调整余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_interest_adjustment_amount IS '利息调整发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_principal_nonfinancial_balance IS '贷款_非金融机构_本金余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_principal_nonfinancial_amount IS '贷款_非金融机构_本金发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_commission_nonfinancial_balance IS '贷款_非金融机构_应收手续费余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_commission_nonfinancial_amount IS '贷款_非金融机构_应收手续费发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_interest_adjustment_nonfinancial_balance IS '贷款_非金融机构_利息调整余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_interest_adjustment_nonfinancial_amount IS '贷款_非金融机构_利息调整发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_bank_balance IS '银行存款减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_bank_amount IS '银行存款减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_buying_back_balance IS '买入返售金融资产减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_buying_back_amount IS '买入返售金融资产减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_term_deposit_interest_balance IS '定期存款应收利息减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_term_deposit_interest_amount IS '定期存款应收利息减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_borrowings_interest_balance IS '借款应收利息减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_borrowings_interest_amount IS '借款应收利息减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_buying_back_interest_balance IS '买入返售金融资产应收利息减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_buying_back_interest_amount IS '买入返售金融资产应收利息减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_financial_product_interest_balance IS '银行理财产品应收利息减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_financial_product_interest_amount IS '银行理财产品应收利息减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_structured_deposit_interest_balance IS '结构性存款应收利息减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_structured_deposit_interest_amount IS '结构性存款应收利息减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_fvpl_interest_other_balance IS 'FVPL_应收利息减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_fvpl_interest_other_amount IS 'FVPL_应收利息减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_bond_fvoci_interest_balance IS 'FVOCI_应收利息减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_bond_fvoci_interest_amount IS 'FVOCI_应收利息减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_fvoci_interest_other_balance IS 'FVOCI_其他应收利息减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_fvoci_interest_other_amount IS 'FVOCI_其他应收利息减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_bond_ac_interest_balance IS '以摊余成本计量的债券应收利息减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_bond_ac_interest_amount IS '以摊余成本计量的债券应收利息减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_ac_interest_other_balance IS '其他以摊余成本计量的金融资产应收利息减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_ac_interest_other_amount IS '其他以摊余成本计量的金融资产应收利息减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_bond_ac_balance IS '以摊余成本计量的债券减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_bond_ac_amount IS '以摊余成本计量的债券减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_turst_ac_balance IS '以摊余成本计量的信托计划减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_turst_ac_amount IS '以摊余成本计量的信托计划减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_other_financial_ac_balance IS '以摊余成本计量的其他金融资产减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_other_financial_ac_amount IS '以摊余成本计量的其他金融资产减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_other_asset_ac_balance IS '以摊余成本计量的其他资产减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_other_asset_ac_amount IS '以摊余成本计量的其他资产减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_bond_fvoci_balance IS 'FVOCI_债券减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_bond_fvoci_amount IS 'FVOCI_债券减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_other_financial_fvoci_balance IS 'FVOCI_其他金融资产减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_other_financial_fvoci_amount IS 'FVOCI_其他金融资产减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_subsidiary_balance IS '投资子公司减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_subsidiary_amount IS '投资子公司减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_jv_balance IS '投资合营企业减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_jv_amount IS '投资合营企业减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_associate_balance IS '投资联营企业减值准备余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_associate_amount IS '投资联营企业减值准备发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_virtual_balance IS '虚拟收付款余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_virtual_amount IS '虚拟收付款发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.unrealized_revenue_other_balance IS '未实现其他收益余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.unrealized_revenue_other_amount IS '未实现其他收益发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_factoring_balance IS '应付保理款余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_factoring_amount IS '应付保理款发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_entrust_balance IS '应付委贷款余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_entrust_amount IS '应付委贷款发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.collect_claims_balance IS '代收理赔款余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.collect_claims_amount IS '代收理赔款发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_other_balance IS '应付其他款项余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_other_amount IS '应付其他款项发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.prereceived_rent_balance IS '预收租赁款余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.prereceived_rent_amount IS '预收租赁款发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_related_party_balance IS '其他应付款_关联公司往来	余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_related_party_amount IS '其他应付款_关联公司往来	发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_payable_spv_balance IS '其他应付款_资产支持专项计划余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_payable_spv_amount IS '其他应付款_资产支持专项计划发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_payable_trust_balance IS '其他应付款_信托计划余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_payable_trust_amount IS '其他应付款_信托计划发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_payable_claim_asset_balance IS '其他应付款_出表保理资产余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_payable_claim_asset_amount IS '其他应付款_出表保理资产发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_payable_rent_balance IS '其他应付款_租金余额余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_payable_rent_amount IS '其他应付款_租金余额发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_payable_residual_balance IS '其他应付款_残值余额余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_payable_residual_amount IS '其他应付款_残值余额发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_payable_other_balance IS '其他应付款_其他余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_payable_other_amount IS '其他应付款_其他发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_payable_claim_balance IS '其他应付款_保理款余额余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_payable_claim_amount IS '其他应付款_保理款余额发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_payable_claim_other_balance IS '其他应付款_保理款余额_其他余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_payable_claim_other_amount IS '其他应付款_保理款余额_其他发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_payable_collect_balance IS '其他应付款_代收出表保理资产款项余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_payable_collect_amount IS '其他应付款_代收出表保理资产款项发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.supplier_pool_balance IS '供应商资金池余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.supplier_pool_amount IS '供应商资金池发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.agent_margin_balance IS '代理商保证金余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.agent_margin_amount IS '代理商保证金发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_margin_balance IS '其他保证金余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_margin_amount IS '其他保证金发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.rent_margin_balance IS '房屋租赁保证金余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.rent_margin_amount IS '房屋租赁保证金发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_long_margin_balance IS '其他长期应付保证金余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_long_margin_amount IS '其他长期应付保证金发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_business_income_balance IS '其他主营业务收入	余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_business_income_amount IS '其他主营业务收入	发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_income_service_balance IS '其他主营业务收入_手续费收入余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_income_service_amount IS '其他主营业务收入_手续费收入发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.interest_other_long_payables_balance IS '其他长期应收款利息收入余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.interest_other_long_payables_amount IS '其他长期应收款利息收入发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.interest_other_financial_ac_balance IS '以摊余成本计量的其他金融资产利息收入余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.interest_other_financial_ac_amount IS '以摊余成本计量的其他金融资产利息收入发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.lease_revenue6_balance IS '租赁收益6%余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.lease_revenue6_amount IS '租赁收益6%发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.lease_revenue3_balance IS '租赁收益3%余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.lease_revenue3_amount IS '租赁收益3%发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_income_lease_transfer_balance IS '其他业务收入_融资租赁款转让收益余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_income_lease_transfer_amount IS '其他业务收入_融资租赁款转让收益发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.rent_investment_property_balance IS '投资性房地产租金收入余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.rent_investment_property_amount IS '投资性房地产租金收入发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_income_factoring_transfer_balance IS '其他业务收入_应收保理款转让收益余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_income_factoring_transfer_amount IS '其他业务收入_应收保理款转让收益发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.asset_dispose_gain_foreclosed_balance IS '抵债资产处置收益余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.asset_dispose_gain_foreclosed_amount IS '抵债资产处置收益发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.asset_dispose_loss_foreclosed_balance IS '抵债资产处置损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.asset_dispose_loss_foreclosed_amount IS '抵债资产处置损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_cost_lease_transfer_balance IS '其他业务成本_融资租赁款转让收益余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_cost_lease_transfer_amount IS '其他业务成本_融资租赁款转让收益发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_cost_factoring_transfer_balance IS '其他业务成本_应收保理款转让收益余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_cost_factoring_transfer_amount IS '其他业务成本_应收保理款转让收益发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.assessment_fee_balance IS '评估费余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.assessment_fee_amount IS '评估费发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.attorney_fee_balance IS '律师费余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.attorney_fee_amount IS '律师费发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.intermediary_fee_other_balance IS '其他聘请中介机构费余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.intermediary_fee_other_amount IS '其他聘请中介机构费发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.consultation_fee_balance IS '咨询费余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.consultation_fee_amount IS '咨询费发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.notary_fee_balance IS '公证费余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.notary_fee_amount IS '公证费发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.lease_asset_recovery_fee_balance IS '回收租赁资产杂费余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.lease_asset_recovery_fee_amount IS '回收租赁资产杂费发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_trade_balance IS '应收贸易款坏账损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_trade_amount IS '应收贸易款坏账损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_service_balance IS '应收服务费坏账损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_service_amount IS '应收服务费坏账损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_other_receivables_balance IS '其他应收款项坏账损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_other_receivables_amount IS '其他应收款项坏账损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_temp_balance IS '暂支及个人往来坏账损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_temp_amount IS '暂支及个人往来坏账损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_other_margin_balance IS '其他保证金坏账损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_other_margin_amount IS '其他保证金坏账损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_rent_investment_property_balance IS '投资性房地产应收租金减值损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_rent_investment_property_amount IS '投资性房地产应收租金减值损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_deposit_balance IS '押金减值损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_deposit_amount IS '押金减值损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_litigation_balance IS '应收诉讼保全费减值损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_litigation_amount IS '应收诉讼保全费减值损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_litigation_margin_balance IS '应收诉讼保证金减值损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_litigation_margin_amount IS '应收诉讼保证金减值损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_other_receivable_balance IS '减值损失_其他余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_other_receivable_amount IS '减值损失_其他发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_reverse_balance IS '应收融资租赁款减值损失_坏账注销转回余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_reverse_amount IS '应收融资租赁款减值损失_坏账注销转回发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_equity_investment_balance IS '长期股权投资减值损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_equity_investment_amount IS '长期股权投资减值损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_interest_balance IS '应收利息减值损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_interest_amount IS '应收利息减值损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_bill_balance IS '应收票据减值损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_bill_amount IS '应收票据减值损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_collateral_balance IS '抵债资产减值损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_collateral_amount IS '抵债资产减值损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_financial_balance IS '金融资产减值损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_financial_amount IS '金融资产减值损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_buying_back_balance IS '买入返售金融资产减值损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_buying_back_amount IS '买入返售金融资产减值损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_bond_ac_balance IS '以摊余成本计量的债券减值损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_bond_ac_amount IS '以摊余成本计量的债券减值损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_financial_fvoci_balance IS 'FVOCI_金融资产减值损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_financial_fvoci_amount IS 'FVOCI_金融资产减值损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_bank_balance IS '银行存款减值损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_bank_amount IS '银行存款减值损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_trust_ac_balance IS '以摊余成本计量的信托计划减值损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_trust_ac_amount IS '以摊余成本计量的信托计划减值损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_other_asset_ac_balance IS '以摊余成本计量的其他资产减值损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_other_asset_ac_amount IS '以摊余成本计量的其他资产减值损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_other_financial_ac_balance IS '以摊余成本计量的其他金融资产减值损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_other_financial_ac_amount IS '以摊余成本计量的其他金融资产减值损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_gov_balance IS '长期应收政府与社会资本合作项目减值损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_gov_amount IS '长期应收政府与社会资本合作项目减值损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_other_long_receivables_balance IS '其他长期应收款减值损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_other_long_receivables_amount IS '其他长期应收款减值损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_other_long_related_balance IS '长期应收款关联公司往来减值损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_other_long_related_amount IS '长期应收款关联公司往来减值损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_borrowings_interest_balance IS '借款应收利息减值损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_borrowings_interest_amount IS '借款应收利息减值损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_investment_property_balance IS '投资性房地产减值损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_investment_property_amount IS '投资性房地产减值损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_other_balance IS '其他减值损失余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_loss_other_amount IS '其他减值损失发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.publication_fee_balance IS '公告费余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.publication_fee_amount IS '公告费发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receive_sum_debt_restructure_balance IS '债务重组项目应收总额	余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receive_sum_debt_restructure_amount IS '债务重组项目应收总额	发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.unrealized_revenue_debt_restructure_balance IS '债务重组项目未实现收益余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.unrealized_revenue_debt_restructure_amount IS '债务重组项目未实现收益发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_outtax_debt_restructure_balance IS '债务重组项目应收销项税余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_outtax_debt_restructure_amount IS '债务重组项目应收销项税发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.unrealized_revenue_other_debt_restructure_balance IS '债务重组项目未实现其他收益余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.unrealized_revenue_other_debt_restructure_amount IS '债务重组项目未实现其他收益发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.collect_payment_balance IS '代收款项余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.collect_payment_amount IS '代收款项发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_default_interest_balance IS '应收罚息余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_default_interest_amount IS '应收罚息发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_terminate_balance IS '应收变更手续费余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_terminate_amount IS '应收变更手续费发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.litigation_virtual_balance IS '诉讼费支付_虚拟余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.litigation_virtual_amount IS '诉讼费支付_虚拟发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.period_code IS '会计期间';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.eas_voucher_id IS '金蝶凭证ID';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.bill_contract_code IS '借款合同编号';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_other_revenue_balance IS '应收其他租赁相关收入余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_other_revenue_amount IS '应收其他租赁相关收入发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_damages_revenue_balance IS '应收违约金收入余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_damages_revenue_amount IS '应收违约金收入发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.continue_involving_assets_balance IS '继续涉入资产余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.continue_involving_assets_amount IS '继续涉入资产发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_margin_entrusted_loans_balance IS '应付委托贷款保证金余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_margin_entrusted_loans_amount IS '应付委托贷款保证金发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.continue_involving_debts_balance IS '继续涉入负债余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.continue_involving_debts_amount IS '继续涉入负债发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.debt_restructuring_income_balance IS '债务重组投资收益余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.debt_restructuring_income_amount IS '债务重组投资收益发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.lease_transfer_income_balance IS '融资租赁款转让收益余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.lease_transfer_income_amount IS '融资租赁款转让收益发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_business_cost_balance IS '其他业务成本余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.other_business_cost_amount IS '其他业务成本发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.financial_institution_fee_balance IS '金融机构手续费余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.financial_institution_fee_amount IS '金融机构手续费发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_bill_balance IS '应付票据余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_bill_amount IS '应付票据发生额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_bill_balance IS '应收票据余额';
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.receivable_bill_amount IS '应收票据发生额';