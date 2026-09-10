-- 零售融资租赁/回租/乘用车：收到发票场景
-- 收票时将“待收增值税进项税”转入“待认证进项税额”：
--   借 222118 应交税费-待认证进项税额
--   贷 123105 其他应收款-待收增值税进项税
-- 标准场景 SDFP 为公共场景，模板条件按业务编码隔离。

START TRANSACTION;

-- 历史初始化数据中的标准场景为逻辑删除状态，当前业务启用时一并恢复。
UPDATE eg_scene
SET scene_name='收到发票',scene_period='order',enable_flag='1',del_flag='0',
    update_by='retail-invoice-config',update_time=NOW()
WHERE id=1717479500793212930 AND scene_code='SDFP';

-- 来源事件兼容“收到发票”和历史“发票认证”两种名称。
DELETE FROM eg_field_mapping WHERE id IN (202609074000001, 202609074000002);
INSERT INTO eg_field_mapping
 (id,system_code,field_code,field_name,source_value,target_value,default_value,
  create_by,create_time,update_by,update_time,del_flag,target_field_code)
VALUES
 (202609074000001,'CYCXT','eventCode','收到发票事件','收到发票,SDFP','SDFP',NULL,
  'retail-invoice-config',NOW(),'retail-invoice-config',NOW(),'0','sceneCode'),
 (202609074000002,'CYCXT','eventCode','收到发票事件','发票认证','SDFP',NULL,
  'retail-invoice-config',NOW(),'retail-invoice-config',NOW(),'0','sceneCode');

-- 复用系统标准场景；仅补本业务与场景的关联。
INSERT INTO eg_business_scene
 (id,business_id,scene_id,scene_code,scene_name,serial,
  create_by,create_time,update_by,update_time,del_flag)
VALUES
 (202609074110001,202609070100003,1717479500793212930,'SDFP','收到发票',5,
  'retail-invoice-config',NOW(),'retail-invoice-config',NOW(),'0')
ON DUPLICATE KEY UPDATE
 scene_id=VALUES(scene_id),scene_code=VALUES(scene_code),scene_name=VALUES(scene_name),serial=VALUES(serial),
 update_by=VALUES(update_by),update_time=NOW(),del_flag='0';

UPDATE eg_business b
SET b.scene_count=(
    SELECT COUNT(*) FROM eg_business_scene bs
    WHERE bs.business_id=b.id AND bs.del_flag='0'
),b.update_by='retail-invoice-config',b.update_time=NOW()
WHERE b.id=202609070100003;

DELETE FROM eg_scene_fields WHERE id BETWEEN 202609074200001 AND 202609074200016;
INSERT INTO eg_scene_fields
 (id,scene_id,scene_code,scene_name,field_name,field_code,data_type,
  create_by,create_time,update_by,update_time,del_flag)
VALUES
 (202609074200001,1717479500793212930,'SDFP','收到发票','系统来源','systemCode','String','retail-invoice-config',NOW(),'retail-invoice-config',NOW(),'0'),
 (202609074200002,1717479500793212930,'SDFP','收到发票','请求流水号','orderId','String','retail-invoice-config',NOW(),'retail-invoice-config',NOW(),'0'),
 (202609074200003,1717479500793212930,'SDFP','收到发票','事件编码','eventCode','String','retail-invoice-config',NOW(),'retail-invoice-config',NOW(),'0'),
 (202609074200004,1717479500793212930,'SDFP','收到发票','业务日期','businessDate','String','retail-invoice-config',NOW(),'retail-invoice-config',NOW(),'0'),
 (202609074200005,1717479500793212930,'SDFP','收到发票','合同编号','contractCode','String','retail-invoice-config',NOW(),'retail-invoice-config',NOW(),'0'),
 (202609074200006,1717479500793212930,'SDFP','收到发票','客户编码','clientCode','String','retail-invoice-config',NOW(),'retail-invoice-config',NOW(),'0'),
 (202609074200007,1717479500793212930,'SDFP','收到发票','客户名称','clientName','String','retail-invoice-config',NOW(),'retail-invoice-config',NOW(),'0'),
 (202609074200008,1717479500793212930,'SDFP','收到发票','签约主体编码','orgId','String','retail-invoice-config',NOW(),'retail-invoice-config',NOW(),'0'),
 (202609074200009,1717479500793212930,'SDFP','收到发票','签约主体名称','orgName','String','retail-invoice-config',NOW(),'retail-invoice-config',NOW(),'0'),
 (202609074200010,1717479500793212930,'SDFP','收到发票','币种','currency','String','retail-invoice-config',NOW(),'retail-invoice-config',NOW(),'0'),
 (202609074200011,1717479500793212930,'SDFP','收到发票','发票号码','invoiceNumber','String','retail-invoice-config',NOW(),'retail-invoice-config',NOW(),'0'),
 (202609074200012,1717479500793212930,'SDFP','收到发票','发票代码','invoiceCode','String','retail-invoice-config',NOW(),'retail-invoice-config',NOW(),'0'),
 (202609074200013,1717479500793212930,'SDFP','收到发票','不含税金额','untaxedAmount','Number','retail-invoice-config',NOW(),'retail-invoice-config',NOW(),'0'),
 (202609074200014,1717479500793212930,'SDFP','收到发票','税额','taxAmount','Number','retail-invoice-config',NOW(),'retail-invoice-config',NOW(),'0'),
 (202609074200015,1717479500793212930,'SDFP','收到发票','价税合计','invoiceAmount','Number','retail-invoice-config',NOW(),'retail-invoice-config',NOW(),'0'),
 (202609074200016,1717479500793212930,'SDFP','收到发票','备注','remark','String','retail-invoice-config',NOW(),'retail-invoice-config',NOW(),'0');

-- 当前环境已扩展必填标识和排序字段。
UPDATE eg_scene_fields
SET required_flag=CASE WHEN field_code IN ('orderId','eventCode','businessDate','contractCode','invoiceNumber','taxAmount') THEN '1' ELSE '0' END,
    sort_no=CAST(RIGHT(id,3) AS UNSIGNED)
WHERE id BETWEEN 202609074200001 AND 202609074200016;

INSERT INTO sys_dict_data
 (dict_sort,dict_label,dict_value,dict_type,status,create_by,create_time,update_by,update_time,remark)
SELECT 43,'待收增值税进项税','input_vat_receivable','sys_cash_type','0',
       'retail-invoice-config',NOW(),'retail-invoice-config',NOW(),'收到发票前暂挂的进项税'
WHERE NOT EXISTS (
 SELECT 1 FROM sys_dict_data WHERE dict_type='sys_cash_type' AND dict_value='input_vat_receivable'
);

DELETE FROM eg_account
WHERE id=202609074600001
   OR (business_code='CYC_RETAIL_LEASEBACK' AND fund_type='input_vat_receivable');
INSERT INTO eg_account
 (id,business_code,business_name,fund_type,account_code,account_name,account_category,
  create_by,create_time,update_by,update_time,del_flag,debit_credit_type,
  client_flag,contract_flag,assist_flags,settlement_type,check_flag)
VALUES
 (202609074600001,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','input_vat_receivable',
  '123105','其他应收款_待收增值税进项税','资产','retail-invoice-config',NOW(),
  'retail-invoice-config',NOW(),'0','DR','0','1','1','intra','0');

DELETE c FROM eg_scene_voucher_condition c
JOIN eg_scene_voucher_entry e ON e.id=c.scene_voucher_entry_id
WHERE e.scene_voucher_id=202609074300001;
DELETE FROM eg_scene_voucher_entry WHERE scene_voucher_id=202609074300001;
DELETE FROM eg_scene_voucher WHERE id=202609074300001;

INSERT INTO eg_scene_voucher
 (id,scene_id,source,voucher_type,company,business_date,currency,voucher_summary,
  create_by,create_time,update_by,update_time,del_flag,script_condition,scene_voucher_name,sub_scene_type)
VALUES
 (202609074300001,1717479500793212930,'systemCode','02','orgId','businessDate','currency',
  "'收到发票-' + invoiceNumber + '-' + contractCode",'retail-invoice-config',NOW(),
  'retail-invoice-config',NOW(),'0',"businessCode == 'CYC_RETAIL_LEASEBACK' && taxAmount > 0",
  '零售回租收到发票模板','AUTO_NO_APPROVAL');

INSERT INTO eg_scene_voucher_entry
 (id,scene_voucher_id,fund_type,relate_bank_flag,voucher_summary,
  create_by,create_time,update_by,update_time,del_flag,cash_attribute_flag,assist_flags)
VALUES
 (202609074400001,202609074300001,'input_vat_pending_certification','0',
  "contractCode + '-' + invoiceNumber + '-' + '收到发票转待认证进项税'",
  'retail-invoice-config',NOW(),'retail-invoice-config',NOW(),'0','0','1'),
 (202609074400002,202609074300001,'input_vat_receivable','0',
  "contractCode + '-' + invoiceNumber + '-' + '结转待收增值税进项税'",
  'retail-invoice-config',NOW(),'retail-invoice-config',NOW(),'0','0','1');

INSERT INTO eg_scene_voucher_condition
 (id,scene_voucher_entry_id,serial,script_condition,dondition_description,
  script_amount,amount_description,debit_credit_type,
  create_by,create_time,update_by,update_time,del_flag)
VALUES
 (202609074500001,202609074400001,1,'taxAmount > 0','收到发票确认待认证进项税',
  'taxAmount','发票税额','DR','retail-invoice-config',NOW(),'retail-invoice-config',NOW(),'0'),
 (202609074500002,202609074400002,1,'taxAmount > 0','收到发票结转待收进项税',
  'taxAmount','发票税额','CR','retail-invoice-config',NOW(),'retail-invoice-config',NOW(),'0');

COMMIT;

SELECT b.business_code,s.scene_code,s.scene_name,v.scene_voucher_name,
       e.fund_type,c.debit_credit_type,c.script_amount,e.voucher_summary
FROM eg_business b
JOIN eg_business_scene bs ON bs.business_id=b.id AND bs.del_flag='0'
JOIN eg_scene s ON s.id=bs.scene_id AND s.del_flag='0'
JOIN eg_scene_voucher v ON v.scene_id=s.id AND v.del_flag='0'
JOIN eg_scene_voucher_entry e ON e.scene_voucher_id=v.id AND e.del_flag='0'
JOIN eg_scene_voucher_condition c ON c.scene_voucher_entry_id=e.id AND c.del_flag='0'
WHERE b.business_code='CYC_RETAIL_LEASEBACK' AND s.scene_code='SDFP'
ORDER BY c.debit_credit_type;
