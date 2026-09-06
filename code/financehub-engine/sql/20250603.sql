ALTER TABLE financialdb.eg_non_confirm_amount_for_business ADD report_type varchar(50) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_amount_for_business.report_type IS '报表类型';
ALTER TABLE financialdb.eg_non_confirm_amount_for_business ADD operation_department_history_comments varchar(1000) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_amount_for_business.operation_department_history_comments IS '运营部历史备注';
