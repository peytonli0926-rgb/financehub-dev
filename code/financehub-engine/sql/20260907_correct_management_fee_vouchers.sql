-- 按原始凭证模板修正“计提/支付资产管理费”。脚本可重复执行。
-- 支付总额为含税银行实付额；税率取业务类型配置，不从接口接收。
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

-- 计提资产管理费：借 64210409，贷 220211。
UPDATE eg_account
SET account_code='64210409',
    account_name='手续费及佣金支出-项目手续费-车辆资产管理费',
    client_flag='1',contract_flag='1',assist_flags='0,1',
    update_by='mgmt-fee-correction',update_time=NOW()
WHERE business_code='CYC_RETAIL_LEASEBACK'
  AND fund_type='vehicle_project_service_fee_expense' AND del_flag='0';

UPDATE eg_scene_voucher_entry
SET voucher_summary='计提车辆资产管理费',
    assist_flags='0,1',
    update_by='mgmt-fee-correction',update_time=NOW()
WHERE id=202609073400001;

UPDATE eg_scene_fields
SET required_flag=CASE WHEN field_code IN ('orderId','eventCode','businessDate','contractCode','managementFeeAmount') THEN '1' ELSE '0' END,
    sort_no=CAST(RIGHT(id,3) AS UNSIGNED),
    update_by='mgmt-fee-correction',update_time=NOW()
WHERE scene_code='CYC_ASSET_MANAGEMENT_FEE_ACCRUAL';

INSERT INTO eg_scene_fields
 (id,scene_id,scene_code,scene_name,field_name,field_code,data_type,
  create_by,create_time,update_by,update_time,del_flag,required_flag,sort_no)
VALUES
 (202609073200014,202609073100001,'CYC_ASSET_MANAGEMENT_FEE_ACCRUAL','计提资产管理费','合同主键（系统补齐）','contractId','Number','mgmt-fee-correction',NOW(),'mgmt-fee-correction',NOW(),'0','0',14),
 (202609073200015,202609073100001,'CYC_ASSET_MANAGEMENT_FEE_ACCRUAL','计提资产管理费','合同名称（系统补齐）','contractName','String','mgmt-fee-correction',NOW(),'mgmt-fee-correction',NOW(),'0','0',15)
ON DUPLICATE KEY UPDATE field_name=VALUES(field_name),field_code=VALUES(field_code),data_type=VALUES(data_type),
 update_by=VALUES(update_by),update_time=NOW(),del_flag='0',required_flag=VALUES(required_flag),sort_no=VALUES(sort_no);

-- 支付资产管理费：含税总额按业务税率拆分，不含税金额不再由接口重复提供。
INSERT INTO eg_scene_fields
 (id,scene_id,scene_code,scene_name,field_name,field_code,data_type,
  create_by,create_time,update_by,update_time,del_flag)
VALUES
 (202609070300117,202609070200001,'CYC_PAYMENT','付款',
  '不含税资产管理费（引擎计算）','managementFeeAmount','Number',
  'mgmt-fee-correction',NOW(),'mgmt-fee-correction',NOW(),'0')
ON DUPLICATE KEY UPDATE
 field_name=VALUES(field_name),field_code=VALUES(field_code),data_type=VALUES(data_type),
 update_by=VALUES(update_by),update_time=NOW(),del_flag='0';

UPDATE eg_scene_voucher_condition
SET script_condition="eventCode == 'CR025' && managementFeeAmount > 0",
    dondition_description='CR025实际支付资产管理费',
    script_amount='managementFeeAmount',
    amount_description='付款总额按业务税率反算的不含税资产管理费',
    debit_credit_type='DR',
    update_by='mgmt-fee-correction',update_time=NOW()
WHERE id=202609070600021;

UPDATE eg_scene_voucher_condition
SET script_condition="eventCode == 'CR025' && taxAmount > 0",
    script_amount='taxAmount',debit_credit_type='DR',
    update_by='mgmt-fee-correction',update_time=NOW()
WHERE id=202609070600022;

UPDATE eg_scene_voucher_condition
SET script_condition="eventCode == 'CR025' && paymentAmount > 0",
    script_amount='paymentAmount',debit_credit_type='CR',
    update_by='mgmt-fee-correction',update_time=NOW()
WHERE id=202609070600023;

-- 修正已生成的计提测试凭证，保证页面展示与新模板一致。
UPDATE eg_voucher_entry e
JOIN eg_voucher v ON v.id=e.voucher_id
SET e.account_code='64210409',
    e.account_name='手续费及佣金支出-项目手续费-车辆资产管理费',
    e.client_flag='1',e.client_code='CUST-PC-000001',e.client_name='张华',
    e.contract_flag='1',e.contract_code='HXZL-PC-202609-0001',e.contract_name='C端车辆融资租赁',
    e.assist_flags='0,1',
    e.update_by='mgmt-fee-correction',e.update_time=NOW()
WHERE v.scene_code='CYC_ASSET_MANAGEMENT_FEE_ACCRUAL'
  AND v.order_id='HXZL-PC-202609-0001-MFA-202609'
  AND e.fund_type='vehicle_project_service_fee_expense'
  AND v.del_flag='0' AND e.del_flag='0';

UPDATE eg_voucher_entry e
JOIN eg_voucher v ON v.id=e.voucher_id
SET e.contract_flag='1',e.contract_code='HXZL-PC-202609-0001',e.contract_name='C端车辆融资租赁',
    e.client_flag=CASE WHEN FIND_IN_SET('0',e.assist_flags)>0 THEN '1' ELSE '0' END,
    e.update_by='mgmt-fee-correction',e.update_time=NOW()
WHERE v.order_id='HXZL-PC-202609-0001-MFA-202609'
  AND FIND_IN_SET('1',e.assist_flags)>0 AND v.del_flag='0' AND e.del_flag='0';

UPDATE eg_voucher
SET contract_code='HXZL-PC-202609-0001',contract_name='C端车辆融资租赁',
    client_code='CUST-PC-000001',client_name='张华',
    update_by='mgmt-fee-correction',update_time=NOW()
WHERE order_id='HXZL-PC-202609-0001-MFA-202609' AND del_flag='0';

UPDATE eg_interface_data i
JOIN eg_raw_transaction_data r ON r.id=i.interface_id
SET i.contract_name='C端车辆融资租赁',
    i.interface_data=JSON_SET(i.interface_data,'$.contractId',2095729250015617025,
      '$.contractName','C端车辆融资租赁','$.contract_name','C端车辆融资租赁'),
    i.update_by='mgmt-fee-correction',i.update_time=NOW()
WHERE r.order_id='HXZL-PC-202609-0001-MFA-202609';

UPDATE eg_raw_transaction_data
SET message_content=JSON_SET(message_content,'$.contractId',2095729250015617025,
      '$.contractName','C端车辆融资租赁','$.contract_name','C端车辆融资租赁'),
    update_by='mgmt-fee-correction',update_time=NOW()
WHERE order_id='HXZL-PC-202609-0001-MFA-202609';

COMMIT;

SELECT v.scene_voucher_name,e.fund_type,a.account_code,a.account_name,
       c.debit_credit_type,c.script_amount,c.script_condition
FROM eg_scene s
JOIN eg_scene_voucher v ON v.scene_id=s.id AND v.del_flag='0'
JOIN eg_scene_voucher_entry e ON e.scene_voucher_id=v.id AND e.del_flag='0'
JOIN eg_scene_voucher_condition c ON c.scene_voucher_entry_id=e.id AND c.del_flag='0'
LEFT JOIN eg_account a ON a.business_code='CYC_RETAIL_LEASEBACK'
 AND a.fund_type=e.fund_type AND a.del_flag='0'
WHERE (s.scene_code='CYC_PAYMENT' AND c.script_condition LIKE '%CR025%')
   OR s.scene_code='CYC_ASSET_MANAGEMENT_FEE_ACCRUAL'
ORDER BY s.scene_code,c.id;
