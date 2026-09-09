-- 零售融资租赁/回租/乘用车：退款、贴息确认、逾期、交易结构变更。
-- 每个场景的同一金额类型仅配置一个字段，由事件值映射和模板条件决定分录。
START TRANSACTION;

-- “退款”从历史付款大类拆为独立中台场景。
UPDATE eg_field_mapping SET source_value='付款,付收车费',update_by='retail-additional-config',update_time=NOW()
 WHERE system_code='CYCXT' AND target_value='ZLFK' AND source_value LIKE '%退款%';

DELETE FROM eg_field_mapping WHERE id BETWEEN 202609090000001 AND 202609090000099;
INSERT INTO eg_field_mapping
 (id,system_code,field_code,field_name,source_value,target_value,default_value,create_by,create_time,update_by,update_time,del_flag,target_field_code)
VALUES
 (202609090000001,'CYCXT','eventCode','退款业务事件','退款,普通退款,保证金退款,退还保证金,未确认款退款,多收款退款,合同撤销退款,RF001,RF002,RF003,RF004','CYC_REFUND',NULL,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','sceneCode'),
 (202609090000002,'CYCXT','eventCode','普通退款','退款,普通退款,合同撤销退款,RF001,RF004','RF001',NULL,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','refundEventCode'),
 (202609090000003,'CYCXT','eventCode','保证金退款','保证金退款,退还保证金,RF002','RF002',NULL,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','refundEventCode'),
 (202609090000004,'CYCXT','eventCode','未确认款退款','未确认款退款,多收款退款,RF003','RF003',NULL,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','refundEventCode'),
 (202609090000010,'CYCXT','eventCode','贴息确认业务事件','厂商贴息确认,平台贴息确认,提前结清贴息冲回,SC001,SC002,SC003','CYC_SUBSIDY_CONFIRM',NULL,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','sceneCode'),
 (202609090000011,'CYCXT','eventCode','厂商贴息确认','厂商贴息确认,SC001','SC001',NULL,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','subsidyEventCode'),
 (202609090000012,'CYCXT','eventCode','平台贴息确认','平台贴息确认,SC002','SC002',NULL,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','subsidyEventCode'),
 (202609090000013,'CYCXT','eventCode','提前结清贴息冲回','提前结清贴息冲回,SC003','SC003',NULL,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','subsidyEventCode'),
 (202609090000020,'CYCXT','eventCode','逾期业务事件','本金转逾期,利息转逾期,留购价转逾期,逾期罚息确认,OD001,OD002,OD003,OD004','CYC_OVERDUE',NULL,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','sceneCode'),
 (202609090000021,'CYCXT','eventCode','本金转逾期','本金转逾期,OD001','OD001',NULL,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','overdueEventCode'),
 (202609090000022,'CYCXT','eventCode','利息转逾期','利息转逾期,OD002','OD002',NULL,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','overdueEventCode'),
 (202609090000023,'CYCXT','eventCode','留购价转逾期','留购价转逾期,OD003','OD003',NULL,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','overdueEventCode'),
 (202609090000024,'CYCXT','eventCode','逾期罚息确认','逾期罚息确认,OD004','OD004',NULL,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','overdueEventCode'),
 (202609090000030,'CYCXT','eventCode','交易结构变更业务事件','租金计划调整,租金信息变更,结清,起租后GPS加装,留购价反向,费用减免租金,费用减免留购价,天津车辆处置结清,提前留购,尾款调整租金,车辆处置,车辆买断,TS001,TS002,TS003,TS004,TS005,TS006,TS007,TS008,TS009,TS010,TS011,TS012','JYJGBG',NULL,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','sceneCode'),
 (202609090000031,'CYCXT','eventCode','租金计划调整','租金计划调整,TS001','TS001',NULL,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','structureEventCode'),
 (202609090000032,'CYCXT','eventCode','租金信息变更','租金信息变更,TS002','TS002',NULL,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','structureEventCode'),
 (202609090000033,'CYCXT','eventCode','结清','结清,TS003','TS003',NULL,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','structureEventCode'),
 (202609090000034,'CYCXT','eventCode','起租后GPS加装','起租后GPS加装,TS004','TS004',NULL,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','structureEventCode'),
 (202609090000035,'CYCXT','eventCode','留购价反向','留购价反向,TS005','TS005',NULL,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','structureEventCode'),
 (202609090000036,'CYCXT','eventCode','费用减免租金','费用减免租金,TS006','TS006',NULL,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','structureEventCode'),
 (202609090000037,'CYCXT','eventCode','费用减免留购价','费用减免留购价,TS007','TS007',NULL,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','structureEventCode'),
 (202609090000038,'CYCXT','eventCode','天津车辆处置结清','天津车辆处置结清,TS008','TS008',NULL,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','structureEventCode'),
 (202609090000039,'CYCXT','eventCode','提前留购','提前留购,TS009','TS009',NULL,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','structureEventCode'),
 (202609090000040,'CYCXT','eventCode','尾款调整租金','尾款调整租金,TS010','TS010',NULL,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','structureEventCode'),
 (202609090000041,'CYCXT','eventCode','车辆处置','车辆处置,TS011','TS011',NULL,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','structureEventCode'),
 (202609090000042,'CYCXT','eventCode','车辆买断','车辆买断,TS012','TS012',NULL,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','structureEventCode');

-- 删除四个场景的旧配置（含历史停用空壳）。
DELETE c FROM eg_scene_voucher_condition c JOIN eg_scene_voucher_entry e ON e.id=c.scene_voucher_entry_id JOIN eg_scene_voucher v ON v.id=e.scene_voucher_id JOIN eg_scene s ON s.id=v.scene_id WHERE s.scene_code IN ('CYC_REFUND','CYC_SUBSIDY_CONFIRM','CYC_OVERDUE','JYJGBG','TK','TXQR','YQ');
DELETE e FROM eg_scene_voucher_entry e JOIN eg_scene_voucher v ON v.id=e.scene_voucher_id JOIN eg_scene s ON s.id=v.scene_id WHERE s.scene_code IN ('CYC_REFUND','CYC_SUBSIDY_CONFIRM','CYC_OVERDUE','JYJGBG','TK','TXQR','YQ');
DELETE v FROM eg_scene_voucher v JOIN eg_scene s ON s.id=v.scene_id WHERE s.scene_code IN ('CYC_REFUND','CYC_SUBSIDY_CONFIRM','CYC_OVERDUE','JYJGBG','TK','TXQR','YQ');
DELETE FROM eg_scene_fields WHERE scene_code IN ('CYC_REFUND','CYC_SUBSIDY_CONFIRM','CYC_OVERDUE','JYJGBG','TK','TXQR','YQ');
DELETE FROM eg_business_scene WHERE scene_code IN ('CYC_REFUND','CYC_SUBSIDY_CONFIRM','CYC_OVERDUE','JYJGBG','TK','TXQR','YQ');
DELETE FROM eg_scene WHERE scene_code IN ('CYC_REFUND','CYC_SUBSIDY_CONFIRM','CYC_OVERDUE','JYJGBG','TK','TXQR','YQ');

INSERT INTO eg_scene (id,scene_code,scene_name,scene_period,enable_flag,create_by,create_time,update_by,update_time,del_flag,version) VALUES
 (202609091000001,'CYC_REFUND','退款','order','1','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0',1),
 (202609091000002,'CYC_SUBSIDY_CONFIRM','贴息确认','order','1','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0',1),
 (202609091000003,'CYC_OVERDUE','逾期','order','1','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0',1),
 (202609091000004,'JYJGBG','交易结构变更','order','1','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0',2);

INSERT INTO eg_business_scene (id,business_id,scene_id,scene_code,scene_name,serial,create_by,create_time,update_by,update_time,del_flag) VALUES
 (202609091100001,202609070100003,202609091000001,'CYC_REFUND','退款',7,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'),
 (202609091100002,202609070100003,202609091000002,'CYC_SUBSIDY_CONFIRM','贴息确认',8,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'),
 (202609091100003,202609070100003,202609091000003,'CYC_OVERDUE','逾期',9,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'),
 (202609091100004,202609070100003,202609091000004,'JYJGBG','交易结构变更',10,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0');
UPDATE eg_business b SET b.scene_count=(SELECT COUNT(*) FROM eg_business_scene bs WHERE bs.business_id=b.id AND bs.del_flag='0'),b.update_by='retail-additional-config',b.update_time=NOW() WHERE b.id=202609070100003;

-- 公共字段和各场景专属字段。scope=ALL 的字段每个场景各生成一份。
INSERT INTO eg_scene_fields
 (id,scene_id,scene_code,scene_name,field_name,field_code,data_type,create_by,create_time,update_by,update_time,del_flag,required_flag,sort_no)
SELECT 202609092000000 + ROW_NUMBER() OVER (ORDER BY s.scene_id,f.sort_no),s.scene_id,s.scene_code,s.scene_name,
       f.field_name,f.field_code,f.data_type,'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0',f.required_flag,f.sort_no
FROM (
 SELECT 202609091000001 scene_id,'CYC_REFUND' scene_code,'退款' scene_name UNION ALL
 SELECT 202609091000002,'CYC_SUBSIDY_CONFIRM','贴息确认' UNION ALL
 SELECT 202609091000003,'CYC_OVERDUE','逾期' UNION ALL
 SELECT 202609091000004,'JYJGBG','交易结构变更'
) s JOIN (
 SELECT 'ALL' scope,'系统来源' field_name,'systemCode' field_code,'String' data_type,'0' required_flag,1 sort_no UNION ALL
 SELECT 'ALL','请求流水号','orderId','String','1',2 UNION ALL SELECT 'ALL','财务事件','eventCode','String','1',3 UNION ALL
 SELECT 'ALL','来源业务事件','sourceEventCode','String','1',4 UNION ALL SELECT 'ALL','业务日期','businessDate','String','1',5 UNION ALL
 SELECT 'ALL','合同编号','contractCode','String','1',6 UNION ALL SELECT 'ALL','客户编码','clientCode','String','0',7 UNION ALL
 SELECT 'ALL','客户名称','clientName','String','0',8 UNION ALL SELECT 'ALL','签约主体编码','orgId','String','0',9 UNION ALL
 SELECT 'ALL','签约主体名称','orgName','String','0',10 UNION ALL SELECT 'ALL','币种','currency','String','0',11 UNION ALL
 SELECT 'ALL','交易对手名称','counterpartyName','String','0',12 UNION ALL SELECT 'ALL','备注','remark','String','0',13 UNION ALL
 SELECT 'CYC_REFUND','付款银行账号','bankNo','String','1',14 UNION ALL SELECT 'CYC_REFUND','银行流水号','transactionSerial','String','0',15 UNION ALL
 SELECT 'CYC_REFUND','退款总金额','refundAmount','Number','1',16 UNION ALL SELECT 'CYC_REFUND','保证金退款金额','depositAmount','Number','0',17 UNION ALL
 SELECT 'CYC_REFUND','预收款退款金额','advanceReceiptAmount','Number','0',18 UNION ALL SELECT 'CYC_REFUND','未确认款退款金额','unidentifiedAmount','Number','0',19 UNION ALL
 SELECT 'CYC_SUBSIDY_CONFIRM','不含税贴息金额','subsidyAmount','Number','0',14 UNION ALL SELECT 'CYC_SUBSIDY_CONFIRM','贴息增值税','subsidyTaxAmount','Number','0',15 UNION ALL
 SELECT 'CYC_OVERDUE','逾期本金','overduePrincipalAmount','Number','0',14 UNION ALL SELECT 'CYC_OVERDUE','逾期不含税利息','overdueInterestAmount','Number','0',15 UNION ALL
 SELECT 'CYC_OVERDUE','逾期利息增值税','overdueInterestTaxAmount','Number','0',16 UNION ALL SELECT 'CYC_OVERDUE','逾期不含税留购价','overdueResidualValueAmount','Number','0',17 UNION ALL
 SELECT 'CYC_OVERDUE','逾期留购价增值税','overdueResidualValueTaxAmount','Number','0',18 UNION ALL SELECT 'CYC_OVERDUE','不含税罚息','penaltyInterestAmount','Number','0',19 UNION ALL
 SELECT 'CYC_OVERDUE','罚息增值税','penaltyInterestTaxAmount','Number','0',20 UNION ALL
 SELECT 'JYJGBG','变更原因','changeReason','String','0',14 UNION ALL SELECT 'JYJGBG','本金调整额','principalAdjustmentAmount','Number','0',15 UNION ALL
 SELECT 'JYJGBG','不含税利息调整额','interestAdjustmentAmount','Number','0',16 UNION ALL SELECT 'JYJGBG','利息税调整额','interestTaxAdjustmentAmount','Number','0',17 UNION ALL
 SELECT 'JYJGBG','不含税留购价调整额','residualValueAdjustmentAmount','Number','0',18 UNION ALL SELECT 'JYJGBG','留购价税调整额','residualValueTaxAdjustmentAmount','Number','0',19 UNION ALL
 SELECT 'JYJGBG','GPS资产调整额','gpsAdjustmentAmount','Number','0',20
) f ON f.scope='ALL' OR f.scope=s.scene_code;

-- 补齐本业务需要的科目类型。科目类型在业务内唯一，模板可稳定取科目。
DELETE FROM eg_account WHERE id BETWEEN 202609096000001 AND 202609096000030;
INSERT INTO eg_account
 (id,business_code,business_name,fund_type,account_code,account_name,account_category,create_by,create_time,update_by,update_time,del_flag,debit_credit_type,client_flag,contract_flag,assist_flags,settlement_type,check_flag)
VALUES
 (202609096000001,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','advance_lease_receipts','220301','预收账款-预收租金','负债','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','CR','1','0','0,1','intra','0'),
 (202609096000002,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','customer_deposit_payable','224103','其他应付款-客户押金','负债','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','CR','1','0','0,1','intra','0'),
 (202609096000003,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','unidentified_receipts','220309','预收账款-其他预收款','负债','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','CR','1','0','0,1','intra','0'),
 (202609096000006,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','lease_interest_income','604101','租赁收入-利息收入','收入','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','CR','1','0','0,1','intra','0'),
 (202609096000007,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','output_vat_payable','2221010208','应交税费-销项税额-其他服务','负债','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','CR','1','0','0,1','intra','0'),
 (202609096000008,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','overdue_lease_principal','153105','应收融资租赁款-逾期本金','资产','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','DR','1','0','0,1','intra','0'),
 (202609096000009,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','overdue_lease_interest','153109','应收融资租赁款-逾期利息','资产','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','DR','1','0','0,1','intra','0'),
 (202609096000010,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','overdue_lease_interest_vat','153114','应收融资租赁款-逾期利息增值税','资产','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','DR','1','0','0,1','intra','0'),
 (202609096000011,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','penalty_interest_income','604103','租赁收入-延期收款收入','收入','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','CR','1','0','0,1','intra','0'),
 (202609096000012,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','unearned_lease_interest','15320201','未实现融资收益-利息','负债','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','CR','1','0','0,1','intra','0'),
 (202609096000013,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','unearned_lease_interest_vat','15320501','未实现融资收益-利息增值税','负债','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','CR','1','0','0,1','intra','0'),
 (202609096000014,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','unearned_residual_value','15320401','未实现融资收益-留购价','负债','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','CR','1','0','0,1','intra','0'),
 (202609096000015,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','unearned_residual_value_vat','15320601','未实现融资收益-留购价增值税','负债','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','CR','1','0','0,1','intra','0'),
 (202609096000016,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','supplier_payable','220201','应付账款-应付融资租赁设备款','负债','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','CR','1','0','0,1','intra','0');

-- 凭证模板：退款1套、贴息确认1套、逾期1套、交易结构变更调增/调减2套。
INSERT INTO eg_scene_voucher (id,scene_id,source,voucher_type,company,business_date,currency,voucher_summary,create_by,create_time,update_by,update_time,del_flag,script_condition,scene_voucher_name,sub_scene_type) VALUES
 (202609093000001,202609091000001,'systemCode','02','orgId','businessDate','currency',"'退款-' + sourceEventCode + '-' + orderId",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','refundAmount > 0','零售回租退款模板','AUTO_NO_APPROVAL'),
 (202609093000002,202609091000002,'systemCode','01','orgId','businessDate','currency',"'贴息确认-' + sourceEventCode + '-' + orderId",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0',"(subsidyEventCode == 'SC001' || subsidyEventCode == 'SC002') && (subsidyAmount > 0 || subsidyTaxAmount > 0)",'零售回租贴息确认模板','AUTO_NO_APPROVAL'),
 (202609093000003,202609091000003,'systemCode','01','orgId','businessDate','currency',"'逾期-' + sourceEventCode + '-' + orderId",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','overduePrincipalAmount > 0 || overdueInterestAmount > 0 || overdueResidualValueAmount > 0 || penaltyInterestAmount > 0','零售回租逾期模板','AUTO_NO_APPROVAL'),
 (202609093000004,202609091000004,'systemCode','01','orgId','businessDate','currency',"'交易结构变更-调增-' + sourceEventCode + '-' + orderId",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','principalAdjustmentAmount > 0 || interestAdjustmentAmount > 0 || interestTaxAdjustmentAmount > 0 || residualValueAdjustmentAmount > 0 || residualValueTaxAdjustmentAmount > 0 || gpsAdjustmentAmount > 0','零售回租交易结构变更-调增模板','AUTO_NO_APPROVAL'),
 (202609093000005,202609091000004,'systemCode','01','orgId','businessDate','currency',"'交易结构变更-调减-' + sourceEventCode + '-' + orderId",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','principalAdjustmentAmount < 0 || interestAdjustmentAmount < 0 || interestTaxAdjustmentAmount < 0 || residualValueAdjustmentAmount < 0 || residualValueTaxAdjustmentAmount < 0 || gpsAdjustmentAmount < 0','零售回租交易结构变更-调减模板','AUTO_NO_APPROVAL'),
 (202609093000006,202609091000002,'systemCode','01','orgId','businessDate','currency',"'贴息冲回-' + sourceEventCode + '-' + orderId",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0',"subsidyEventCode == 'SC003' && (subsidyAmount > 0 || subsidyTaxAmount > 0)",'零售回租提前结清贴息冲回模板','AUTO_NO_APPROVAL');

INSERT INTO eg_scene_voucher_entry (id,scene_voucher_id,fund_type,relate_bank_flag,bank_account,voucher_summary,create_by,create_time,update_by,update_time,del_flag,cash_attribute_flag,assist_flags) VALUES
 (202609094000001,202609093000001,'advance_lease_receipts','0',NULL,"contractCode + '-退还预收款'",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','0','0,1'),
 (202609094000002,202609093000001,'customer_deposit_payable','0',NULL,"contractCode + '-退还保证金'",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','0','0,1'),
 (202609094000003,202609093000001,'unidentified_receipts','0',NULL,"contractCode + '-退还未确认款'",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','0','0,1'),
 (202609094000004,202609093000001,'bank_deposit','1','bankNo',"contractCode + '-退款支付'",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','0','2'),
 (202609094000010,202609093000002,'vehicle_profit_sharing_receivable','0',NULL,"contractCode + '-确认贴息应收款'",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','0','0,1'),
 (202609094000011,202609093000002,'input_vat_receivable','0',NULL,"contractCode + '-确认贴息税额应收款'",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','0','0,1'),
 (202609094000012,202609093000002,'lease_interest_income','0',NULL,"contractCode + '-确认贴息收入'",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','0','0,1'),
 (202609094000013,202609093000002,'output_vat_payable','0',NULL,"contractCode + '-确认贴息销项税'",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','0','0,1'),
 (202609094000014,202609093000006,'vehicle_profit_sharing_receivable','0',NULL,"contractCode + '-冲回贴息应收款'",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','0','0,1'),
 (202609094000015,202609093000006,'input_vat_receivable','0',NULL,"contractCode + '-冲回贴息税额应收款'",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','0','0,1'),
 (202609094000016,202609093000006,'lease_interest_income','0',NULL,"contractCode + '-冲回贴息收入'",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','0','0,1'),
 (202609094000017,202609093000006,'output_vat_payable','0',NULL,"contractCode + '-冲回贴息销项税'",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','0','0,1'),
 (202609094000020,202609093000003,'overdue_lease_principal','0',NULL,"contractCode + '-本金转逾期'",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','0','0,1'),
 (202609094000021,202609093000003,'lease_principal_receivable','0',NULL,"contractCode + '-转出正常本金'",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','0','0,1'),
 (202609094000022,202609093000003,'overdue_lease_interest','0',NULL,"contractCode + '-利息转逾期'",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','0','0,1'),
 (202609094000023,202609093000003,'lease_interest_receivable','0',NULL,"contractCode + '-转出正常利息'",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','0','0,1'),
 (202609094000024,202609093000003,'overdue_lease_interest_vat','0',NULL,"contractCode + '-利息税转逾期'",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','0','0,1'),
 (202609094000025,202609093000003,'lease_interest_vat_receivable','0',NULL,"contractCode + '-转出正常利息税'",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','0','0,1'),
 (202609094000026,202609093000003,'overdue_residual_value','0',NULL,"contractCode + '-留购价转逾期'",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','0','0,1'),
 (202609094000027,202609093000003,'residual_value_receivable','0',NULL,"contractCode + '-转出正常留购价'",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','0','0,1'),
 (202609094000028,202609093000003,'overdue_residual_value_vat','0',NULL,"contractCode + '-留购价税转逾期'",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','0','0,1'),
 (202609094000029,202609093000003,'residual_value_vat_receivable','0',NULL,"contractCode + '-转出正常留购价税'",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','0','0,1'),
 (202609094000030,202609093000003,'overdue_lease_interest','0',NULL,"contractCode + '-确认罚息应收款'",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','0','0,1'),
 (202609094000031,202609093000003,'penalty_interest_income','0',NULL,"contractCode + '-确认罚息收入'",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','0','0,1'),
 (202609094000032,202609093000003,'overdue_lease_interest_vat','0',NULL,"contractCode + '-确认罚息税应收款'",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','0','0,1'),
 (202609094000033,202609093000003,'output_vat_payable','0',NULL,"contractCode + '-确认罚息销项税'",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','0','0,1');

-- 交易结构模板分录成对配置；同一金额字段分别由正负条件选择方向。
INSERT INTO eg_scene_voucher_entry (id,scene_voucher_id,fund_type,relate_bank_flag,bank_account,voucher_summary,create_by,create_time,update_by,update_time,del_flag,cash_attribute_flag,assist_flags)
SELECT 202609094001000 + ROW_NUMBER() OVER (ORDER BY t.template_id,p.seq),t.template_id,p.fund_type,'0',NULL,
       "contractCode + '-交易结构变更'",'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0','0','0,1'
FROM (SELECT 202609093000004 template_id UNION ALL SELECT 202609093000005) t
JOIN (
 SELECT 1 seq,'lease_principal_receivable' fund_type UNION ALL SELECT 2,'lease_asset_movable_leaseback' UNION ALL
 SELECT 3,'lease_interest_receivable' UNION ALL SELECT 4,'unearned_lease_interest' UNION ALL
 SELECT 5,'lease_interest_vat_receivable' UNION ALL SELECT 6,'unearned_lease_interest_vat' UNION ALL
 SELECT 7,'residual_value_receivable' UNION ALL SELECT 8,'unearned_residual_value' UNION ALL
 SELECT 9,'residual_value_vat_receivable' UNION ALL SELECT 10,'unearned_residual_value_vat' UNION ALL
 SELECT 11,'lease_asset_movable_leaseback' UNION ALL SELECT 12,'supplier_payable'
) p;

INSERT INTO eg_scene_voucher_condition (id,scene_voucher_entry_id,serial,script_condition,dondition_description,script_amount,amount_description,debit_credit_type,create_by,create_time,update_by,update_time,del_flag) VALUES
 (202609095000001,202609094000001,1,'advanceReceiptAmount > 0','退还预收款','advanceReceiptAmount','预收款退款金额','DR','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'),
 (202609095000002,202609094000002,2,'depositAmount > 0','退还保证金','depositAmount','保证金退款金额','DR','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'),
 (202609095000003,202609094000003,3,'unidentifiedAmount > 0','退还未确认款','unidentifiedAmount','未确认款退款金额','DR','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'),
 (202609095000004,202609094000004,4,'refundAmount > 0','银行支付退款','refundAmount','退款总金额','CR','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'),
 (202609095000010,202609094000010,1,'subsidyAmount > 0','确认贴息应收款','subsidyAmount','不含税贴息金额','DR','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'),
 (202609095000011,202609094000011,2,'subsidyTaxAmount > 0','确认贴息税额应收款','subsidyTaxAmount','贴息增值税','DR','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'),
 (202609095000012,202609094000012,3,'subsidyAmount > 0','确认贴息收入','subsidyAmount','不含税贴息金额','CR','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'),
 (202609095000013,202609094000013,4,'subsidyTaxAmount > 0','确认贴息销项税','subsidyTaxAmount','贴息增值税','CR','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'),
 (202609095000014,202609094000014,1,'subsidyAmount > 0','冲回贴息应收款','subsidyAmount','不含税贴息金额','CR','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'),
 (202609095000015,202609094000015,2,'subsidyTaxAmount > 0','冲回贴息税额应收款','subsidyTaxAmount','贴息增值税','CR','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'),
 (202609095000016,202609094000016,3,'subsidyAmount > 0','冲回贴息收入','subsidyAmount','不含税贴息金额','DR','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'),
 (202609095000017,202609094000017,4,'subsidyTaxAmount > 0','冲回贴息销项税','subsidyTaxAmount','贴息增值税','DR','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'),
 (202609095000020,202609094000020,1,'overduePrincipalAmount > 0','本金转逾期','overduePrincipalAmount','逾期本金','DR','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'),
 (202609095000021,202609094000021,2,'overduePrincipalAmount > 0','转出正常本金','overduePrincipalAmount','逾期本金','CR','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'),
 (202609095000022,202609094000022,3,'overdueInterestAmount > 0','利息转逾期','overdueInterestAmount','逾期利息','DR','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'),
 (202609095000023,202609094000023,4,'overdueInterestAmount > 0','转出正常利息','overdueInterestAmount','逾期利息','CR','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'),
 (202609095000024,202609094000024,5,'overdueInterestTaxAmount > 0','利息税转逾期','overdueInterestTaxAmount','逾期利息税','DR','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'),
 (202609095000025,202609094000025,6,'overdueInterestTaxAmount > 0','转出正常利息税','overdueInterestTaxAmount','逾期利息税','CR','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'),
 (202609095000026,202609094000026,7,'overdueResidualValueAmount > 0','留购价转逾期','overdueResidualValueAmount','逾期留购价','DR','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'),
 (202609095000027,202609094000027,8,'overdueResidualValueAmount > 0','转出正常留购价','overdueResidualValueAmount','逾期留购价','CR','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'),
 (202609095000028,202609094000028,9,'overdueResidualValueTaxAmount > 0','留购价税转逾期','overdueResidualValueTaxAmount','逾期留购价税','DR','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'),
 (202609095000029,202609094000029,10,'overdueResidualValueTaxAmount > 0','转出正常留购价税','overdueResidualValueTaxAmount','逾期留购价税','CR','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'),
 (202609095000030,202609094000030,11,'penaltyInterestAmount > 0','确认罚息应收款','penaltyInterestAmount','不含税罚息','DR','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'),
 (202609095000031,202609094000031,12,'penaltyInterestAmount > 0','确认罚息收入','penaltyInterestAmount','不含税罚息','CR','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'),
 (202609095000032,202609094000032,13,'penaltyInterestTaxAmount > 0','确认罚息税应收款','penaltyInterestTaxAmount','罚息增值税','DR','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'),
 (202609095000033,202609094000033,14,'penaltyInterestTaxAmount > 0','确认罚息销项税','penaltyInterestTaxAmount','罚息增值税','CR','retail-additional-config',NOW(),'retail-additional-config',NOW(),'0');

-- 交易结构分录ID由模板顺序生成：调增1001-1012，调减1013-1024。
INSERT INTO eg_scene_voucher_condition (id,scene_voucher_entry_id,serial,script_condition,dondition_description,script_amount,amount_description,debit_credit_type,create_by,create_time,update_by,update_time,del_flag)
SELECT 202609095001000 + ROW_NUMBER() OVER (ORDER BY x.entry_id),x.entry_id,x.serial,x.cond,x.descr,x.amount,x.amount_desc,x.direction,
       'retail-additional-config',NOW(),'retail-additional-config',NOW(),'0'
FROM (
 SELECT 202609094001001 entry_id,1 serial,'principalAdjustmentAmount > 0' cond,'调增应收本金' descr,'principalAdjustmentAmount' amount,'本金调整额' amount_desc,'DR' direction UNION ALL
 SELECT 202609094001002,2,'principalAdjustmentAmount > 0','调增租赁资产','principalAdjustmentAmount','本金调整额','CR' UNION ALL
 SELECT 202609094001003,3,'interestAdjustmentAmount > 0','调增应收利息','interestAdjustmentAmount','利息调整额','DR' UNION ALL
 SELECT 202609094001004,4,'interestAdjustmentAmount > 0','调增未实现利息','interestAdjustmentAmount','利息调整额','CR' UNION ALL
 SELECT 202609094001005,5,'interestTaxAdjustmentAmount > 0','调增应收利息税','interestTaxAdjustmentAmount','利息税调整额','DR' UNION ALL
 SELECT 202609094001006,6,'interestTaxAdjustmentAmount > 0','调增未实现利息税','interestTaxAdjustmentAmount','利息税调整额','CR' UNION ALL
 SELECT 202609094001007,7,'residualValueAdjustmentAmount > 0','调增应收留购价','residualValueAdjustmentAmount','留购价调整额','DR' UNION ALL
 SELECT 202609094001008,8,'residualValueAdjustmentAmount > 0','调增未实现留购价','residualValueAdjustmentAmount','留购价调整额','CR' UNION ALL
 SELECT 202609094001009,9,'residualValueTaxAdjustmentAmount > 0','调增应收留购价税','residualValueTaxAdjustmentAmount','留购价税调整额','DR' UNION ALL
 SELECT 202609094001010,10,'residualValueTaxAdjustmentAmount > 0','调增未实现留购价税','residualValueTaxAdjustmentAmount','留购价税调整额','CR' UNION ALL
 SELECT 202609094001011,11,'gpsAdjustmentAmount > 0','调增GPS资产','gpsAdjustmentAmount','GPS资产调整额','DR' UNION ALL
 SELECT 202609094001012,12,'gpsAdjustmentAmount > 0','调增应付设备款','gpsAdjustmentAmount','GPS资产调整额','CR' UNION ALL
 SELECT 202609094001013,1,'principalAdjustmentAmount < 0','调减应收本金','0 - principalAdjustmentAmount','本金调整额','CR' UNION ALL
 SELECT 202609094001014,2,'principalAdjustmentAmount < 0','调减租赁资产','0 - principalAdjustmentAmount','本金调整额','DR' UNION ALL
 SELECT 202609094001015,3,'interestAdjustmentAmount < 0','调减应收利息','0 - interestAdjustmentAmount','利息调整额','CR' UNION ALL
 SELECT 202609094001016,4,'interestAdjustmentAmount < 0','调减未实现利息','0 - interestAdjustmentAmount','利息调整额','DR' UNION ALL
 SELECT 202609094001017,5,'interestTaxAdjustmentAmount < 0','调减应收利息税','0 - interestTaxAdjustmentAmount','利息税调整额','CR' UNION ALL
 SELECT 202609094001018,6,'interestTaxAdjustmentAmount < 0','调减未实现利息税','0 - interestTaxAdjustmentAmount','利息税调整额','DR' UNION ALL
 SELECT 202609094001019,7,'residualValueAdjustmentAmount < 0','调减应收留购价','0 - residualValueAdjustmentAmount','留购价调整额','CR' UNION ALL
 SELECT 202609094001020,8,'residualValueAdjustmentAmount < 0','调减未实现留购价','0 - residualValueAdjustmentAmount','留购价调整额','DR' UNION ALL
 SELECT 202609094001021,9,'residualValueTaxAdjustmentAmount < 0','调减应收留购价税','0 - residualValueTaxAdjustmentAmount','留购价税调整额','CR' UNION ALL
 SELECT 202609094001022,10,'residualValueTaxAdjustmentAmount < 0','调减未实现留购价税','0 - residualValueTaxAdjustmentAmount','留购价税调整额','DR' UNION ALL
 SELECT 202609094001023,11,'gpsAdjustmentAmount < 0','调减GPS资产','0 - gpsAdjustmentAmount','GPS资产调整额','CR' UNION ALL
 SELECT 202609094001024,12,'gpsAdjustmentAmount < 0','调减应付设备款','0 - gpsAdjustmentAmount','GPS资产调整额','DR'
) x;

COMMIT;
