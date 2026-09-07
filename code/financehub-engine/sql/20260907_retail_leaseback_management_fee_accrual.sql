-- 零售融资租赁/乘用车回租：计提资产管理费
-- 每合同每月一笔；计提金额为不含税金额，税额在付款/发票环节按业务税率处理。
DROP PROCEDURE IF EXISTS ensure_management_scene_field_columns;
DELIMITER $$
CREATE PROCEDURE ensure_management_scene_field_columns()
BEGIN
  IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema=DATABASE() AND table_name='eg_scene_fields' AND column_name='required_flag') THEN
    ALTER TABLE eg_scene_fields ADD COLUMN required_flag CHAR(1) NOT NULL DEFAULT '0';
  END IF;
  IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema=DATABASE() AND table_name='eg_scene_fields' AND column_name='sort_no') THEN
    ALTER TABLE eg_scene_fields ADD COLUMN sort_no INT NOT NULL DEFAULT 0;
  END IF;
END$$
DELIMITER ;
CALL ensure_management_scene_field_columns();
DROP PROCEDURE ensure_management_scene_field_columns;

START TRANSACTION;

DELETE FROM eg_field_mapping WHERE id BETWEEN 202609073000001 AND 202609073000002;
INSERT INTO eg_field_mapping
 (id,system_code,field_code,field_name,source_value,target_value,default_value,
  create_by,create_time,update_by,update_time,del_flag,target_field_code)
VALUES
 (202609073000001,'CYCXT','eventCode','计提资产管理费事件','计提资产管理费,JTZCGLF',
  'CYC_ASSET_MANAGEMENT_FEE_ACCRUAL',NULL,'retail-management-accrual-config',NOW(),'retail-management-accrual-config',NOW(),'0','sceneCode'),
 (202609073000002,'CYCXT','eventCode','计提资产管理费事件','计提资产管理费,JTZCGLF',
  'JTZCGLF',NULL,'retail-management-accrual-config',NOW(),'retail-management-accrual-config',NOW(),'0','managementFeeEventCode');

DELETE c FROM eg_scene_voucher_condition c
JOIN eg_scene_voucher_entry e ON e.id=c.scene_voucher_entry_id
JOIN eg_scene_voucher v ON v.id=e.scene_voucher_id
JOIN eg_scene s ON s.id=v.scene_id
WHERE s.scene_code='CYC_ASSET_MANAGEMENT_FEE_ACCRUAL';
DELETE e FROM eg_scene_voucher_entry e
JOIN eg_scene_voucher v ON v.id=e.scene_voucher_id
JOIN eg_scene s ON s.id=v.scene_id
WHERE s.scene_code='CYC_ASSET_MANAGEMENT_FEE_ACCRUAL';
DELETE v FROM eg_scene_voucher v JOIN eg_scene s ON s.id=v.scene_id
WHERE s.scene_code='CYC_ASSET_MANAGEMENT_FEE_ACCRUAL';
DELETE FROM eg_scene_fields WHERE scene_code='CYC_ASSET_MANAGEMENT_FEE_ACCRUAL';
DELETE FROM eg_business_scene WHERE scene_code='CYC_ASSET_MANAGEMENT_FEE_ACCRUAL';
DELETE FROM eg_scene WHERE scene_code='CYC_ASSET_MANAGEMENT_FEE_ACCRUAL';

INSERT INTO eg_scene
 (id,scene_code,scene_name,scene_period,enable_flag,create_by,create_time,update_by,update_time,del_flag,version)
VALUES
 (202609073100001,'CYC_ASSET_MANAGEMENT_FEE_ACCRUAL','计提资产管理费','month','1',
  'retail-management-accrual-config',NOW(),'retail-management-accrual-config',NOW(),'0',1);

INSERT INTO eg_business_scene
 (id,business_id,scene_id,scene_code,scene_name,serial,create_by,create_time,update_by,update_time,del_flag)
VALUES
 (202609073110001,202609070100003,202609073100001,'CYC_ASSET_MANAGEMENT_FEE_ACCRUAL','计提资产管理费',4,
  'retail-management-accrual-config',NOW(),'retail-management-accrual-config',NOW(),'0');

UPDATE eg_business SET scene_count=4,update_by='retail-management-accrual-config',update_time=NOW()
WHERE business_code='CYC_RETAIL_LEASEBACK' AND del_flag='0';

INSERT INTO eg_scene_fields
 (id,scene_id,scene_code,scene_name,field_name,field_code,data_type,create_by,create_time,update_by,update_time,del_flag)
VALUES
 (202609073200001,202609073100001,'CYC_ASSET_MANAGEMENT_FEE_ACCRUAL','计提资产管理费','系统来源','systemCode','String','retail-management-accrual-config',NOW(),'retail-management-accrual-config',NOW(),'0'),
 (202609073200002,202609073100001,'CYC_ASSET_MANAGEMENT_FEE_ACCRUAL','计提资产管理费','请求流水号','orderId','String','retail-management-accrual-config',NOW(),'retail-management-accrual-config',NOW(),'0'),
 (202609073200003,202609073100001,'CYC_ASSET_MANAGEMENT_FEE_ACCRUAL','计提资产管理费','事件编码','eventCode','String','retail-management-accrual-config',NOW(),'retail-management-accrual-config',NOW(),'0'),
 (202609073200004,202609073100001,'CYC_ASSET_MANAGEMENT_FEE_ACCRUAL','计提资产管理费','业务日期','businessDate','String','retail-management-accrual-config',NOW(),'retail-management-accrual-config',NOW(),'0'),
 (202609073200005,202609073100001,'CYC_ASSET_MANAGEMENT_FEE_ACCRUAL','计提资产管理费','合同编号','contractCode','String','retail-management-accrual-config',NOW(),'retail-management-accrual-config',NOW(),'0'),
 (202609073200006,202609073100001,'CYC_ASSET_MANAGEMENT_FEE_ACCRUAL','计提资产管理费','客户编码','clientCode','String','retail-management-accrual-config',NOW(),'retail-management-accrual-config',NOW(),'0'),
 (202609073200007,202609073100001,'CYC_ASSET_MANAGEMENT_FEE_ACCRUAL','计提资产管理费','客户名称','clientName','String','retail-management-accrual-config',NOW(),'retail-management-accrual-config',NOW(),'0'),
 (202609073200008,202609073100001,'CYC_ASSET_MANAGEMENT_FEE_ACCRUAL','计提资产管理费','签约主体编码','orgId','String','retail-management-accrual-config',NOW(),'retail-management-accrual-config',NOW(),'0'),
 (202609073200009,202609073100001,'CYC_ASSET_MANAGEMENT_FEE_ACCRUAL','计提资产管理费','签约主体名称','orgName','String','retail-management-accrual-config',NOW(),'retail-management-accrual-config',NOW(),'0'),
 (202609073200010,202609073100001,'CYC_ASSET_MANAGEMENT_FEE_ACCRUAL','计提资产管理费','币种','currency','String','retail-management-accrual-config',NOW(),'retail-management-accrual-config',NOW(),'0'),
 (202609073200011,202609073100001,'CYC_ASSET_MANAGEMENT_FEE_ACCRUAL','计提资产管理费','不含税管理费金额','managementFeeAmount','Number','retail-management-accrual-config',NOW(),'retail-management-accrual-config',NOW(),'0'),
 (202609073200012,202609073100001,'CYC_ASSET_MANAGEMENT_FEE_ACCRUAL','计提资产管理费','计提月份','accrualPeriod','String','retail-management-accrual-config',NOW(),'retail-management-accrual-config',NOW(),'0'),
 (202609073200013,202609073100001,'CYC_ASSET_MANAGEMENT_FEE_ACCRUAL','计提资产管理费','备注','remark','String','retail-management-accrual-config',NOW(),'retail-management-accrual-config',NOW(),'0');

UPDATE eg_scene_fields
SET required_flag=CASE WHEN field_code IN ('orderId','eventCode','businessDate','contractCode','managementFeeAmount') THEN '1' ELSE '0' END,
    sort_no=CAST(RIGHT(id,3) AS UNSIGNED)
WHERE scene_code='CYC_ASSET_MANAGEMENT_FEE_ACCRUAL';

INSERT INTO eg_scene_fields
 (id,scene_id,scene_code,scene_name,field_name,field_code,data_type,create_by,create_time,update_by,update_time,del_flag,required_flag,sort_no)
VALUES
 (202609073200014,202609073100001,'CYC_ASSET_MANAGEMENT_FEE_ACCRUAL','计提资产管理费','合同主键（系统补齐）','contractId','Number','retail-management-accrual-config',NOW(),'retail-management-accrual-config',NOW(),'0','0',14),
 (202609073200015,202609073100001,'CYC_ASSET_MANAGEMENT_FEE_ACCRUAL','计提资产管理费','合同名称（系统补齐）','contractName','String','retail-management-accrual-config',NOW(),'retail-management-accrual-config',NOW(),'0','0',15);

-- 应付车辆管理费科目复用付款场景；仅补充本业务尚不存在的费用科目。
DELETE FROM eg_account
WHERE business_code='CYC_RETAIL_LEASEBACK' AND fund_type='vehicle_project_service_fee_expense';
INSERT INTO eg_account
 (id,business_code,business_name,fund_type,account_code,account_name,account_category,
  create_by,create_time,update_by,update_time,del_flag,debit_credit_type,client_flag,contract_flag,assist_flags,settlement_type,check_flag)
VALUES
 (202609073600001,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','vehicle_project_service_fee_expense',
  '64210409','手续费及佣金支出-项目手续费-车辆资产管理费','损益',
  'retail-management-accrual-config',NOW(),'retail-management-accrual-config',NOW(),'0','DR','1','1','0,1','intra','0');

INSERT INTO eg_scene_voucher
 (id,scene_id,source,voucher_type,company,business_date,currency,voucher_summary,
  create_by,create_time,update_by,update_time,del_flag,script_condition,scene_voucher_name,sub_scene_type)
VALUES
 (202609073300001,202609073100001,'systemCode','02','orgId','businessDate','currency',
  "'计提资产管理费-' + accrualPeriod + '-' + contractCode",
  'retail-management-accrual-config',NOW(),'retail-management-accrual-config',NOW(),'0',
  "managementFeeEventCode == 'JTZCGLF' && managementFeeAmount > 0",
  '零售回租计提资产管理费模板','AUTO_NO_APPROVAL');

INSERT INTO eg_scene_voucher_entry
 (id,scene_voucher_id,fund_type,relate_bank_flag,voucher_summary,create_by,create_time,update_by,update_time,del_flag,cash_attribute_flag,assist_flags)
VALUES
 (202609073400001,202609073300001,'vehicle_project_service_fee_expense','0','计提车辆资产管理费','retail-management-accrual-config',NOW(),'retail-management-accrual-config',NOW(),'0','0','0,1'),
 (202609073400002,202609073300001,'vehicle_management_fee_payable','0','计提应付车辆管理费','retail-management-accrual-config',NOW(),'retail-management-accrual-config',NOW(),'0','0','1');

INSERT INTO eg_scene_voucher_condition
 (id,scene_voucher_entry_id,serial,script_condition,dondition_description,script_amount,amount_description,debit_credit_type,
  create_by,create_time,update_by,update_time,del_flag)
VALUES
 (202609073500001,202609073400001,1,"managementFeeEventCode == 'JTZCGLF' && managementFeeAmount > 0",'月末计提资产管理费','managementFeeAmount','不含税资产管理费','DR','retail-management-accrual-config',NOW(),'retail-management-accrual-config',NOW(),'0'),
 (202609073500002,202609073400002,1,"managementFeeEventCode == 'JTZCGLF' && managementFeeAmount > 0",'月末计提应付资产管理费','managementFeeAmount','不含税资产管理费','CR','retail-management-accrual-config',NOW(),'retail-management-accrual-config',NOW(),'0');

COMMIT;

SELECT s.scene_code,s.scene_name,s.scene_period,v.scene_voucher_name,e.fund_type,c.debit_credit_type,c.script_amount
FROM eg_scene s JOIN eg_scene_voucher v ON v.scene_id=s.id AND v.del_flag='0'
JOIN eg_scene_voucher_entry e ON e.scene_voucher_id=v.id AND e.del_flag='0'
JOIN eg_scene_voucher_condition c ON c.scene_voucher_entry_id=e.id AND c.del_flag='0'
WHERE s.scene_code='CYC_ASSET_MANAGEMENT_FEE_ACCRUAL' ORDER BY c.id;
