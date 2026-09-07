-- 零售融资租赁/乘用车回租：分润费确认场景
-- 一个接口、一个凭证模板，通过事件编码控制正向确认与退回冲销。
START TRANSACTION;

-- 来源事件到统一场景、模板内部事件的值映射。
DELETE FROM eg_field_mapping WHERE id BETWEEN 202609072000001 AND 202609072000003;
INSERT INTO eg_field_mapping
 (id,system_code,field_code,field_name,source_value,target_value,default_value,
  create_by,create_time,update_by,update_time,del_flag,target_field_code)
VALUES
 (202609072000001,'CYCXT','eventCode','分润费确认事件',
  '起租日确认分润费,FRF_CONFIRM,退回分润费,FRF_REFUND','CYC_PROFIT_SHARING_CONFIRM',NULL,
  'retail-profit-sharing-config',NOW(),'retail-profit-sharing-config',NOW(),'0','sceneCode'),
 (202609072000002,'CYCXT','eventCode','起租日确认分润费',
  '起租日确认分润费,FRF_CONFIRM','FRF_CONFIRM',NULL,
  'retail-profit-sharing-config',NOW(),'retail-profit-sharing-config',NOW(),'0','profitSharingEventCode'),
 (202609072000003,'CYCXT','eventCode','退回分润费',
  '退回分润费,FRF_REFUND','FRF_REFUND',NULL,
  'retail-profit-sharing-config',NOW(),'retail-profit-sharing-config',NOW(),'0','profitSharingEventCode');

-- 只重建本场景，不重复创建已由付款场景维护的分润科目。
DELETE c FROM eg_scene_voucher_condition c
JOIN eg_scene_voucher_entry e ON e.id=c.scene_voucher_entry_id
JOIN eg_scene_voucher v ON v.id=e.scene_voucher_id
JOIN eg_scene s ON s.id=v.scene_id
WHERE s.scene_code='CYC_PROFIT_SHARING_CONFIRM';
DELETE e FROM eg_scene_voucher_entry e
JOIN eg_scene_voucher v ON v.id=e.scene_voucher_id
JOIN eg_scene s ON s.id=v.scene_id
WHERE s.scene_code='CYC_PROFIT_SHARING_CONFIRM';
DELETE v FROM eg_scene_voucher v JOIN eg_scene s ON s.id=v.scene_id
WHERE s.scene_code='CYC_PROFIT_SHARING_CONFIRM';
DELETE FROM eg_scene_fields WHERE scene_code='CYC_PROFIT_SHARING_CONFIRM';
DELETE FROM eg_business_scene WHERE scene_code='CYC_PROFIT_SHARING_CONFIRM';
DELETE FROM eg_scene WHERE scene_code='CYC_PROFIT_SHARING_CONFIRM';

INSERT INTO eg_scene
 (id,scene_code,scene_name,scene_period,enable_flag,create_by,create_time,update_by,update_time,del_flag,version)
VALUES
 (202609072100001,'CYC_PROFIT_SHARING_CONFIRM','分润费确认','order','1',
  'retail-profit-sharing-config',NOW(),'retail-profit-sharing-config',NOW(),'0',1);

INSERT INTO eg_business_scene
 (id,business_id,scene_id,scene_code,scene_name,serial,create_by,create_time,update_by,update_time,del_flag)
VALUES
 (202609072110001,202609070100003,202609072100001,'CYC_PROFIT_SHARING_CONFIRM','分润费确认',3,
  'retail-profit-sharing-config',NOW(),'retail-profit-sharing-config',NOW(),'0');

UPDATE eg_business SET scene_count=3,update_by='retail-profit-sharing-config',update_time=NOW()
WHERE business_code='CYC_RETAIL_LEASEBACK' AND del_flag='0';

INSERT INTO eg_scene_fields
 (id,scene_id,scene_code,scene_name,field_name,field_code,data_type,create_by,create_time,update_by,update_time,del_flag)
VALUES
 (202609072200001,202609072100001,'CYC_PROFIT_SHARING_CONFIRM','分润费确认','系统来源','systemCode','String','retail-profit-sharing-config',NOW(),'retail-profit-sharing-config',NOW(),'0'),
 (202609072200002,202609072100001,'CYC_PROFIT_SHARING_CONFIRM','分润费确认','请求流水号','orderId','String','retail-profit-sharing-config',NOW(),'retail-profit-sharing-config',NOW(),'0'),
 (202609072200003,202609072100001,'CYC_PROFIT_SHARING_CONFIRM','分润费确认','事件编码','eventCode','String','retail-profit-sharing-config',NOW(),'retail-profit-sharing-config',NOW(),'0'),
 (202609072200004,202609072100001,'CYC_PROFIT_SHARING_CONFIRM','分润费确认','业务日期','businessDate','String','retail-profit-sharing-config',NOW(),'retail-profit-sharing-config',NOW(),'0'),
 (202609072200005,202609072100001,'CYC_PROFIT_SHARING_CONFIRM','分润费确认','合同编号','contractCode','String','retail-profit-sharing-config',NOW(),'retail-profit-sharing-config',NOW(),'0'),
 (202609072200006,202609072100001,'CYC_PROFIT_SHARING_CONFIRM','分润费确认','客户编码','clientCode','String','retail-profit-sharing-config',NOW(),'retail-profit-sharing-config',NOW(),'0'),
 (202609072200007,202609072100001,'CYC_PROFIT_SHARING_CONFIRM','分润费确认','客户名称','clientName','String','retail-profit-sharing-config',NOW(),'retail-profit-sharing-config',NOW(),'0'),
 (202609072200008,202609072100001,'CYC_PROFIT_SHARING_CONFIRM','分润费确认','签约主体编码','orgId','String','retail-profit-sharing-config',NOW(),'retail-profit-sharing-config',NOW(),'0'),
 (202609072200009,202609072100001,'CYC_PROFIT_SHARING_CONFIRM','分润费确认','签约主体名称','orgName','String','retail-profit-sharing-config',NOW(),'retail-profit-sharing-config',NOW(),'0'),
 (202609072200010,202609072100001,'CYC_PROFIT_SHARING_CONFIRM','分润费确认','币种','currency','String','retail-profit-sharing-config',NOW(),'retail-profit-sharing-config',NOW(),'0'),
 (202609072200011,202609072100001,'CYC_PROFIT_SHARING_CONFIRM','分润费确认','分润费金额','profitSharingAmount','Number','retail-profit-sharing-config',NOW(),'retail-profit-sharing-config',NOW(),'0'),
 (202609072200012,202609072100001,'CYC_PROFIT_SHARING_CONFIRM','分润费确认','关联起租单号','triggerOrderId','String','retail-profit-sharing-config',NOW(),'retail-profit-sharing-config',NOW(),'0'),
 (202609072200013,202609072100001,'CYC_PROFIT_SHARING_CONFIRM','分润费确认','备注','remark','String','retail-profit-sharing-config',NOW(),'retail-profit-sharing-config',NOW(),'0');

INSERT INTO eg_scene_voucher
 (id,scene_id,source,voucher_type,company,business_date,currency,voucher_summary,
  create_by,create_time,update_by,update_time,del_flag,script_condition,scene_voucher_name,sub_scene_type)
VALUES
 (202609072300001,202609072100001,'systemCode','02','orgId','businessDate','currency',
  "eventCode + '-' + contractCode",'retail-profit-sharing-config',NOW(),'retail-profit-sharing-config',NOW(),'0',
  "profitSharingAmount > 0 && (profitSharingEventCode == 'FRF_CONFIRM' || profitSharingEventCode == 'FRF_REFUND')",
  '零售回租分润费确认模板','AUTO_NO_APPROVAL');

INSERT INTO eg_scene_voucher_entry
 (id,scene_voucher_id,fund_type,relate_bank_flag,voucher_summary,create_by,create_time,update_by,update_time,del_flag,cash_attribute_flag,assist_flags)
VALUES
 (202609072400001,202609072300001,'vehicle_profit_sharing_receivable','0','应收车辆清分款','retail-profit-sharing-config',NOW(),'retail-profit-sharing-config',NOW(),'0','0','0,1'),
 (202609072400002,202609072300001,'vehicle_profit_sharing_payable','0','应付车辆分润费','retail-profit-sharing-config',NOW(),'retail-profit-sharing-config',NOW(),'0','0','1');

INSERT INTO eg_scene_voucher_condition
 (id,scene_voucher_entry_id,serial,script_condition,dondition_description,script_amount,amount_description,debit_credit_type,
  create_by,create_time,update_by,update_time,del_flag)
VALUES
 (202609072500001,202609072400001,1,"profitSharingEventCode == 'FRF_CONFIRM' && profitSharingAmount > 0",'起租日确认应收分润款','profitSharingAmount','分润费金额','DR','retail-profit-sharing-config',NOW(),'retail-profit-sharing-config',NOW(),'0'),
 (202609072500002,202609072400002,1,"profitSharingEventCode == 'FRF_CONFIRM' && profitSharingAmount > 0",'起租日确认应付分润费','profitSharingAmount','分润费金额','CR','retail-profit-sharing-config',NOW(),'retail-profit-sharing-config',NOW(),'0'),
 (202609072500003,202609072400002,2,"profitSharingEventCode == 'FRF_REFUND' && profitSharingAmount > 0",'退回应付分润费','profitSharingAmount','退回金额','DR','retail-profit-sharing-config',NOW(),'retail-profit-sharing-config',NOW(),'0'),
 (202609072500004,202609072400001,2,"profitSharingEventCode == 'FRF_REFUND' && profitSharingAmount > 0",'退回应收车辆清分款','profitSharingAmount','退回金额','CR','retail-profit-sharing-config',NOW(),'retail-profit-sharing-config',NOW(),'0');

COMMIT;

SELECT s.scene_code,s.scene_name,v.scene_voucher_name,e.fund_type,c.debit_credit_type,c.script_condition
FROM eg_scene s JOIN eg_scene_voucher v ON v.scene_id=s.id AND v.del_flag='0'
JOIN eg_scene_voucher_entry e ON e.scene_voucher_id=v.id AND e.del_flag='0'
JOIN eg_scene_voucher_condition c ON c.scene_voucher_entry_id=e.id AND c.del_flag='0'
WHERE s.scene_code='CYC_PROFIT_SHARING_CONFIRM' ORDER BY c.id;
