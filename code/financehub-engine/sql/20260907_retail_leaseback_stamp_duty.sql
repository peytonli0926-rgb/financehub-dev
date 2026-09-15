-- 零售融资租赁/乘用车回租：计提税金-印花税
-- 起租成功后由会计引擎内部自动形成接口事件，无外部接口、无需审批。
START TRANSACTION;

-- 清理由未完成的批量配置产生的其他生命周期场景，只保留本场景。
DELETE c FROM eg_scene_voucher_condition c
JOIN eg_scene_voucher_entry e ON e.id=c.scene_voucher_entry_id
JOIN eg_scene_voucher v ON v.id=e.scene_voucher_id
WHERE v.scene_id BETWEEN 202609071100001 AND 202609071100012;
DELETE e FROM eg_scene_voucher_entry e JOIN eg_scene_voucher v ON v.id=e.scene_voucher_id
WHERE v.scene_id BETWEEN 202609071100001 AND 202609071100012;
DELETE FROM eg_scene_voucher WHERE scene_id BETWEEN 202609071100001 AND 202609071100012;
DELETE FROM eg_scene_fields WHERE scene_id BETWEEN 202609071100001 AND 202609071100012;
DELETE FROM eg_business_scene WHERE scene_id BETWEEN 202609071100001 AND 202609071100012;
DELETE FROM eg_scene WHERE id BETWEEN 202609071100001 AND 202609071100012;
DELETE FROM eg_field_mapping WHERE id BETWEEN 202609071000001 AND 202609071000012;
DELETE FROM eg_account WHERE id BETWEEN 202609071600001 AND 202609071600010;

UPDATE eg_business SET scene_count=2,update_by='retail-stamp-duty-config',update_time=NOW()
WHERE business_code='CYC_RETAIL_LEASEBACK' AND del_flag='0';

INSERT INTO eg_scene
 (id,scene_code,scene_name,scene_period,enable_flag,create_by,create_time,update_by,update_time,del_flag,version)
VALUES
 (202609071100008,'CYC_TAX_ACCRUAL','计提税金-印花税','order','1','retail-stamp-duty-config',NOW(),'retail-stamp-duty-config',NOW(),'0',1);

INSERT INTO eg_business_scene
 (id,business_id,scene_id,scene_code,scene_name,serial,create_by,create_time,update_by,update_time,del_flag)
VALUES
 (202609071110008,202609070100003,202609071100008,'CYC_TAX_ACCRUAL','计提税金-印花税',2,
  'retail-stamp-duty-config',NOW(),'retail-stamp-duty-config',NOW(),'0');

INSERT INTO eg_scene_fields
 (id,scene_id,scene_code,scene_name,field_name,field_code,data_type,create_by,create_time,update_by,update_time,del_flag)
VALUES
 (202609071208001,202609071100008,'CYC_TAX_ACCRUAL','计提税金-印花税','系统来源','systemCode','String','retail-stamp-duty-config',NOW(),'retail-stamp-duty-config',NOW(),'0'),
 (202609071208002,202609071100008,'CYC_TAX_ACCRUAL','计提税金-印花税','内部事件流水号','orderId','String','retail-stamp-duty-config',NOW(),'retail-stamp-duty-config',NOW(),'0'),
 (202609071208003,202609071100008,'CYC_TAX_ACCRUAL','计提税金-印花税','事件名称','eventCode','String','retail-stamp-duty-config',NOW(),'retail-stamp-duty-config',NOW(),'0'),
 (202609071208004,202609071100008,'CYC_TAX_ACCRUAL','计提税金-印花税','业务日期','businessDate','String','retail-stamp-duty-config',NOW(),'retail-stamp-duty-config',NOW(),'0'),
 (202609071208005,202609071100008,'CYC_TAX_ACCRUAL','计提税金-印花税','合同编号','contractCode','String','retail-stamp-duty-config',NOW(),'retail-stamp-duty-config',NOW(),'0'),
 (202609071208006,202609071100008,'CYC_TAX_ACCRUAL','计提税金-印花税','客户编码','clientCode','String','retail-stamp-duty-config',NOW(),'retail-stamp-duty-config',NOW(),'0'),
 (202609071208007,202609071100008,'CYC_TAX_ACCRUAL','计提税金-印花税','客户名称','clientName','String','retail-stamp-duty-config',NOW(),'retail-stamp-duty-config',NOW(),'0'),
 (202609071208008,202609071100008,'CYC_TAX_ACCRUAL','计提税金-印花税','签约主体编码','orgId','String','retail-stamp-duty-config',NOW(),'retail-stamp-duty-config',NOW(),'0'),
 (202609071208009,202609071100008,'CYC_TAX_ACCRUAL','计提税金-印花税','签约主体名称','orgName','String','retail-stamp-duty-config',NOW(),'retail-stamp-duty-config',NOW(),'0'),
 (202609071208010,202609071100008,'CYC_TAX_ACCRUAL','计提税金-印花税','币种','currency','String','retail-stamp-duty-config',NOW(),'retail-stamp-duty-config',NOW(),'0'),
 (202609071208011,202609071100008,'CYC_TAX_ACCRUAL','计提税金-印花税','租金总额（印花税计税基础）','stampDutyTaxBase','Number','retail-stamp-duty-config',NOW(),'retail-stamp-duty-config',NOW(),'0'),
 (202609071208012,202609071100008,'CYC_TAX_ACCRUAL','计提税金-印花税','印花税金额','stampDutyAmount','Number','retail-stamp-duty-config',NOW(),'retail-stamp-duty-config',NOW(),'0'),
 (202609071208013,202609071100008,'CYC_TAX_ACCRUAL','计提税金-印花税','触发起租单号','triggerOrderId','String','retail-stamp-duty-config',NOW(),'retail-stamp-duty-config',NOW(),'0');

-- 税率表按业务类型维护。0.005% = 万分之零点五；接口事件不传税率。
-- 原字段两位小数无法准确保存0.005%，统一扩展为六位小数。
ALTER TABLE eg_tax_rate MODIFY COLUMN tax_rate DECIMAL(20,6) NULL;
DELETE FROM eg_tax_rate WHERE business_code='CYC_RETAIL_LEASEBACK' AND fund_type='stamp_duty';
INSERT INTO eg_tax_rate
 (id,business_code,fund_type,enable_flag,enable_date,tax_rate,create_by,create_time,update_by,update_time,del_flag,lease_type,lease_sub_type)
VALUES
 (202609071700001,'CYC_RETAIL_LEASEBACK','stamp_duty','1','2026-09-03',0.005,
  'retail-stamp-duty-config',NOW(),'retail-stamp-duty-config',NOW(),'0','FINANCE_LEASE','SALE_AND_LEASEBACK');

DELETE FROM eg_account WHERE business_code='CYC_RETAIL_LEASEBACK' AND fund_type IN ('stamp_duty_expense','stamp_duty_payable');
INSERT INTO eg_account
 (id,business_code,business_name,fund_type,account_code,account_name,account_category,create_by,create_time,update_by,update_time,
  del_flag,debit_credit_type,client_flag,contract_flag,assist_flags,settlement_type,check_flag)
VALUES
 (202609071600008,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','stamp_duty_expense','640503','税金及附加_印花税','损益','retail-stamp-duty-config',NOW(),'retail-stamp-duty-config',NOW(),'0','DR','0','1','1','intra','0'),
 (202609071600009,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','stamp_duty_payable','222113','应交税费_应交税金-印花税','负债','retail-stamp-duty-config',NOW(),'retail-stamp-duty-config',NOW(),'0','CR','0','1','1','intra','0');

INSERT INTO eg_scene_voucher
 (id,scene_id,source,voucher_type,company,business_date,currency,voucher_summary,create_by,create_time,update_by,update_time,
  del_flag,script_condition,scene_voucher_name,sub_scene_type)
VALUES
 (202609071300008,202609071100008,'systemCode','02','orgId','businessDate','currency',
  "'起租自动计提印花税-' + contractCode",'retail-stamp-duty-config',NOW(),'retail-stamp-duty-config',NOW(),'0','stampDutyAmount > 0','零售回租计提印花税模板','AUTO_NO_APPROVAL');

INSERT INTO eg_scene_voucher_entry
 (id,scene_voucher_id,fund_type,relate_bank_flag,voucher_summary,create_by,create_time,update_by,update_time,del_flag,cash_attribute_flag,assist_flags)
VALUES
 (202609071408001,202609071300008,'stamp_duty_expense','0','''计提印花税费用''','retail-stamp-duty-config',NOW(),'retail-stamp-duty-config',NOW(),'0','0','1'),
 (202609071408002,202609071300008,'stamp_duty_payable','0','''计提应交印花税''','retail-stamp-duty-config',NOW(),'retail-stamp-duty-config',NOW(),'0','0','1');

INSERT INTO eg_scene_voucher_condition
 (id,scene_voucher_entry_id,serial,script_condition,dondition_description,script_amount,amount_description,debit_credit_type,
  create_by,create_time,update_by,update_time,del_flag)
VALUES
 (202609071508001,202609071408001,1,'stampDutyAmount > 0','起租自动计提印花税','stampDutyAmount','印花税金额','DR','retail-stamp-duty-config',NOW(),'retail-stamp-duty-config',NOW(),'0'),
 (202609071508002,202609071408002,1,'stampDutyAmount > 0','起租自动计提印花税','stampDutyAmount','印花税金额','CR','retail-stamp-duty-config',NOW(),'retail-stamp-duty-config',NOW(),'0');

COMMIT;

SELECT s.scene_code,s.scene_name,v.scene_voucher_name,e.fund_type,c.debit_credit_type,c.script_amount
FROM eg_scene s JOIN eg_scene_voucher v ON v.scene_id=s.id AND v.del_flag='0'
JOIN eg_scene_voucher_entry e ON e.scene_voucher_id=v.id AND e.del_flag='0'
JOIN eg_scene_voucher_condition c ON c.scene_voucher_entry_id=e.id AND c.del_flag='0'
WHERE s.scene_code='CYC_TAX_ACCRUAL' ORDER BY c.debit_credit_type;
