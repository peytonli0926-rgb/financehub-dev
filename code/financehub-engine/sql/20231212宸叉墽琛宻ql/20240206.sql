--已执行，20240226
--凭证表新增字段
ALTER TABLE financialdb.eg_voucher ADD employee_name varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_voucher.employee_name IS '员工姓名';

ALTER TABLE financialdb.eg_voucher ADD expense_type varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_voucher.expense_type IS '费用类型';

ALTER TABLE financialdb.eg_voucher ADD financial_institution varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_voucher.financial_institution IS '金融机构';


ALTER TABLE financialdb.eg_voucher ADD bank_no varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_voucher.bank_no IS '银行账号';

ALTER TABLE financialdb.eg_voucher ADD cost_centre varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_voucher.cost_centre IS '成本中心';

--接口表
ALTER TABLE financialdb.eg_interface_data ADD employee_name varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_interface_data.employee_name IS '员工姓名';

ALTER TABLE financialdb.eg_interface_data ADD expense_type varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_interface_data.expense_type IS '费用类型';

ALTER TABLE financialdb.eg_interface_data ADD financial_institution varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_interface_data.financial_institution IS '金融机构';


ALTER TABLE financialdb.eg_interface_data ADD bank_no varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_interface_data.bank_no IS '银行账号';

ALTER TABLE financialdb.eg_interface_data ADD cost_centre varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_interface_data.cost_centre IS '成本中心';