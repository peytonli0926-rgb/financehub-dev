-- 零售融资租赁/回租/乘用车：辅助账调整、其他。
-- 依据 20260907 核算模板；同一金额类型仅保留一个接口字段。
START TRANSACTION;

DELETE FROM eg_field_mapping WHERE id BETWEEN 202609101000001 AND 202609101000099;
INSERT INTO eg_field_mapping
 (id,system_code,field_code,field_name,source_value,target_value,default_value,create_by,create_time,update_by,update_time,del_flag,target_field_code)
VALUES
 (202609101000001,'CYCXT','eventCode','辅助账调整场景','项目承租人发生变更,因辅助核算项目挂错，调整相关科目,C020,CR020,C048,CR048,C049,CR049,C050,CR050,CR062','CYC_AUXILIARY_ADJUSTMENT',NULL,'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','sceneCode'),
 (202609101000002,'CYCXT','eventCode','项目承租人发生变更','项目承租人发生变更,C020,CR020','AA001',NULL,'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','auxiliaryEventCode'),
 (202609101000003,'CYCXT','eventCode','辅助核算项目调整','因辅助核算项目挂错，调整相关科目,C048,CR048,C049,CR049,C050,CR050,CR062','AA002',NULL,'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','auxiliaryEventCode'),
 (202609101000010,'CYCXT','eventCode','其他场景','内部资金调拨,头寸调拨,季度结息,多支付分润费挂账,金额记错，调整相关科目,调整违约金分成,促销核准后，补差合作方分润费,C016,CR016,C037,CR037,C038,CR038,C039,CR039,C045,CR045,C046,CR046,C047,CR047,CR057,CR057#','CYC_OTHER',NULL,'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','sceneCode'),
 (202609101000011,'CYCXT','eventCode','内部资金调拨','内部资金调拨,C016,CR016','OT001',NULL,'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','otherEventCode'),
 (202609101000012,'CYCXT','eventCode','头寸调拨','头寸调拨,C037,CR037','OT002',NULL,'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','otherEventCode'),
 (202609101000013,'CYCXT','eventCode','季度结息-存放同业','C038,CR038','OT003',NULL,'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','otherEventCode'),
 (202609101000014,'CYCXT','eventCode','季度结息-银行存款','季度结息,C039,CR039','OT004',NULL,'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','otherEventCode'),
 (202609101000015,'CYCXT','eventCode','多支付分润费挂账','多支付分润费挂账,C045,CR045','OT005',NULL,'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','otherEventCode'),
 (202609101000016,'CYCXT','eventCode','金额记错调整','金额记错，调整相关科目,C046,CR046','OT006',NULL,'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','otherEventCode'),
 (202609101000017,'CYCXT','eventCode','调整违约金分成','调整违约金分成,C047,CR047','OT007',NULL,'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','otherEventCode'),
 (202609101000018,'CYCXT','eventCode','促销补差分润费','促销核准后，补差合作方分润费,CR057,CR057#','OT008',NULL,'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','otherEventCode');

DELETE c FROM eg_scene_voucher_condition c JOIN eg_scene_voucher_entry e ON e.id=c.scene_voucher_entry_id JOIN eg_scene_voucher v ON v.id=e.scene_voucher_id JOIN eg_scene s ON s.id=v.scene_id WHERE s.scene_code IN ('CYC_AUXILIARY_ADJUSTMENT','CYC_OTHER');
DELETE e FROM eg_scene_voucher_entry e JOIN eg_scene_voucher v ON v.id=e.scene_voucher_id JOIN eg_scene s ON s.id=v.scene_id WHERE s.scene_code IN ('CYC_AUXILIARY_ADJUSTMENT','CYC_OTHER');
DELETE v FROM eg_scene_voucher v JOIN eg_scene s ON s.id=v.scene_id WHERE s.scene_code IN ('CYC_AUXILIARY_ADJUSTMENT','CYC_OTHER');
DELETE FROM eg_scene_fields WHERE scene_code IN ('CYC_AUXILIARY_ADJUSTMENT','CYC_OTHER');
DELETE FROM eg_business_scene WHERE scene_code IN ('CYC_AUXILIARY_ADJUSTMENT','CYC_OTHER');
DELETE FROM eg_scene WHERE scene_code IN ('CYC_AUXILIARY_ADJUSTMENT','CYC_OTHER');

INSERT INTO eg_scene (id,scene_code,scene_name,scene_period,enable_flag,create_by,create_time,update_by,update_time,del_flag,version) VALUES
 (202609102000001,'CYC_AUXILIARY_ADJUSTMENT','辅助账调整','order','1','retail-aux-other',NOW(),'retail-aux-other',NOW(),'0',1),
 (202609102000002,'CYC_OTHER','其他','order','1','retail-aux-other',NOW(),'retail-aux-other',NOW(),'0',1);
INSERT INTO eg_business_scene (id,business_id,scene_id,scene_code,scene_name,serial,create_by,create_time,update_by,update_time,del_flag) VALUES
 (202609102100001,202609070100003,202609102000001,'CYC_AUXILIARY_ADJUSTMENT','辅助账调整',12,'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0'),
 (202609102100002,202609070100003,202609102000002,'CYC_OTHER','其他',13,'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0');
UPDATE eg_business b SET b.scene_count=(SELECT COUNT(*) FROM eg_business_scene bs WHERE bs.business_id=b.id AND bs.del_flag='0'),b.update_by='retail-aux-other',b.update_time=NOW() WHERE b.id=202609070100003;

INSERT INTO eg_scene_fields
 (id,scene_id,scene_code,scene_name,field_name,field_code,data_type,create_by,create_time,update_by,update_time,del_flag,required_flag,sort_no)
SELECT 202609103000000 + ROW_NUMBER() OVER (ORDER BY s.scene_id,f.sort_no),s.scene_id,s.scene_code,s.scene_name,
 f.field_name,f.field_code,f.data_type,'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0',f.required_flag,f.sort_no
FROM (
 SELECT 202609102000001 scene_id,'CYC_AUXILIARY_ADJUSTMENT' scene_code,'辅助账调整' scene_name UNION ALL
 SELECT 202609102000002,'CYC_OTHER','其他'
) s JOIN (
 SELECT 'ALL' scope,'系统来源' field_name,'systemCode' field_code,'String' data_type,'0' required_flag,1 sort_no UNION ALL
 SELECT 'ALL','请求流水号','orderId','String','1',2 UNION ALL SELECT 'ALL','财务事件','eventCode','String','1',3 UNION ALL
 SELECT 'ALL','来源业务事件','sourceEventCode','String','1',4 UNION ALL SELECT 'ALL','业务日期','businessDate','String','1',5 UNION ALL
 SELECT 'ALL','合同编号','contractCode','String','1',6 UNION ALL SELECT 'ALL','客户编码','clientCode','String','0',7 UNION ALL
 SELECT 'ALL','客户名称','clientName','String','0',8 UNION ALL SELECT 'ALL','签约主体编码','orgId','String','0',9 UNION ALL
 SELECT 'ALL','签约主体名称','orgName','String','0',10 UNION ALL SELECT 'ALL','币种','currency','String','0',11 UNION ALL
 SELECT 'ALL','备注','remark','String','0',12 UNION ALL
 SELECT 'CYC_AUXILIARY_ADJUSTMENT','辅助账事件类型','auxiliaryEventCode','String','1',13 UNION ALL
 SELECT 'CYC_AUXILIARY_ADJUSTMENT','原承租人编码','originalClientCode','String','0',14 UNION ALL
 SELECT 'CYC_AUXILIARY_ADJUSTMENT','原承租人名称','originalClientName','String','0',15 UNION ALL
 SELECT 'CYC_AUXILIARY_ADJUSTMENT','账面本金余额','principalBalance','Number','0',16 UNION ALL
 SELECT 'CYC_AUXILIARY_ADJUSTMENT','账面利息余额','interestBalance','Number','0',17 UNION ALL
 SELECT 'CYC_AUXILIARY_ADJUSTMENT','账面留购价余额','residualValueBalance','Number','0',18 UNION ALL
 SELECT 'CYC_AUXILIARY_ADJUSTMENT','账面利息税余额','interestTaxBalance','Number','0',19 UNION ALL
 SELECT 'CYC_AUXILIARY_ADJUSTMENT','账面留购价税余额','residualValueTaxBalance','Number','0',20 UNION ALL
 SELECT 'CYC_AUXILIARY_ADJUSTMENT','账面计提利息余额','accruedInterestBalance','Number','0',21 UNION ALL
 SELECT 'CYC_AUXILIARY_ADJUSTMENT','账面计提留购价余额','accruedResidualValueBalance','Number','0',22 UNION ALL
 SELECT 'CYC_AUXILIARY_ADJUSTMENT','账面计提利息税余额','accruedInterestTaxBalance','Number','0',23 UNION ALL
 SELECT 'CYC_AUXILIARY_ADJUSTMENT','账面计提留购价税余额','accruedResidualValueTaxBalance','Number','0',24 UNION ALL
 SELECT 'CYC_AUXILIARY_ADJUSTMENT','其他预收款辅助项调整金额','unidentifiedReceiptAssistAmount','Number','0',25 UNION ALL
 SELECT 'CYC_AUXILIARY_ADJUSTMENT','车辆管理费应付辅助项调整金额','managementFeePayableAssistAmount','Number','0',26 UNION ALL
 SELECT 'CYC_AUXILIARY_ADJUSTMENT','待收进项税辅助项调整金额','inputVatReceivableAssistAmount','Number','0',27 UNION ALL
 SELECT 'CYC_AUXILIARY_ADJUSTMENT','未实现利息辅助项调整金额','unearnedInterestAssistAmount','Number','0',28 UNION ALL
 SELECT 'CYC_OTHER','其他事件类型','otherEventCode','String','1',13 UNION ALL
 SELECT 'CYC_OTHER','转出银行账号','sourceBankAccountNo','String','0',14 UNION ALL
 SELECT 'CYC_OTHER','转入银行账号','targetBankAccountNo','String','0',15 UNION ALL
 SELECT 'CYC_OTHER','调拨金额','transferAmount','Number','0',16 UNION ALL
 SELECT 'CYC_OTHER','季度结息金额','quarterlyInterestAmount','Number','0',17 UNION ALL
 SELECT 'CYC_OTHER','多支付分润费金额','overpaidProfitSharingAmount','Number','0',18 UNION ALL
 SELECT 'CYC_OTHER','渠道商分成含税金额','channelShareAmount','Number','0',19 UNION ALL
 SELECT 'CYC_OTHER','渠道商分成不含税金额','channelShareNetAmount','Number','0',20 UNION ALL
 SELECT 'CYC_OTHER','渠道商分成税额','channelShareTaxAmount','Number','0',21 UNION ALL
 SELECT 'CYC_OTHER','违约金分成调整金额','penaltyShareAdjustmentAmount','Number','0',22 UNION ALL
 SELECT 'CYC_OTHER','促销补差计提利息','promotionInterestAmount','Number','0',23 UNION ALL
 SELECT 'CYC_OTHER','促销补差待收进项税','promotionInputVatAmount','Number','0',24 UNION ALL
 SELECT 'CYC_OTHER','促销补差利息收入','promotionIncomeAmount','Number','0',25 UNION ALL
 SELECT 'CYC_OTHER','促销补差应付分润费','promotionProfitSharingAmount','Number','0',26
) f ON f.scope='ALL' OR f.scope=s.scene_code;

-- 动态余额登记按 fund_type 拼接列名，新科目类型需同步补齐三张余额表。
DROP PROCEDURE IF EXISTS add_retail_aux_other_balance_column;
DELIMITER $$
CREATE PROCEDURE add_retail_aux_other_balance_column(IN p_table VARCHAR(64), IN p_column VARCHAR(128))
BEGIN
 IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema=DATABASE() AND table_name=p_table AND column_name=p_column) THEN
  SET @ddl=CONCAT('ALTER TABLE ',p_table,' ADD COLUMN ',p_column,' DECIMAL(24,2) NULL DEFAULT 0');
  PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;
 END IF;
END$$
DELIMITER ;
CALL add_retail_aux_other_balance_column('eg_contract_balance','interbank_interest_receivable_balance');
CALL add_retail_aux_other_balance_column('eg_contract_balance','interbank_interest_receivable_amount');
CALL add_retail_aux_other_balance_column('eg_contract_balance','placement_interest_receivable_balance');
CALL add_retail_aux_other_balance_column('eg_contract_balance','placement_interest_receivable_amount');
CALL add_retail_aux_other_balance_column('eg_contract_balance','interbank_interest_income_balance');
CALL add_retail_aux_other_balance_column('eg_contract_balance','interbank_interest_income_amount');
CALL add_retail_aux_other_balance_column('eg_contract_balance','deposit_interest_income_balance');
CALL add_retail_aux_other_balance_column('eg_contract_balance','deposit_interest_income_amount');
CALL add_retail_aux_other_balance_column('eg_contract_balance','early_termination_share_expense_balance');
CALL add_retail_aux_other_balance_column('eg_contract_balance','early_termination_share_expense_amount');
CALL add_retail_aux_other_balance_column('eg_contract_balance','output_vat_payable_share_balance');
CALL add_retail_aux_other_balance_column('eg_contract_balance','output_vat_payable_share_amount');
CALL add_retail_aux_other_balance_column('eg_contract_balance','lease_interest_income_promotion_balance');
CALL add_retail_aux_other_balance_column('eg_contract_balance','lease_interest_income_promotion_amount');
CALL add_retail_aux_other_balance_column('eg_contract_balance_latest','interbank_interest_receivable_balance');
CALL add_retail_aux_other_balance_column('eg_contract_balance_latest','interbank_interest_receivable_amount');
CALL add_retail_aux_other_balance_column('eg_contract_balance_latest','placement_interest_receivable_balance');
CALL add_retail_aux_other_balance_column('eg_contract_balance_latest','placement_interest_receivable_amount');
CALL add_retail_aux_other_balance_column('eg_contract_balance_latest','interbank_interest_income_balance');
CALL add_retail_aux_other_balance_column('eg_contract_balance_latest','interbank_interest_income_amount');
CALL add_retail_aux_other_balance_column('eg_contract_balance_latest','deposit_interest_income_balance');
CALL add_retail_aux_other_balance_column('eg_contract_balance_latest','deposit_interest_income_amount');
CALL add_retail_aux_other_balance_column('eg_contract_balance_latest','early_termination_share_expense_balance');
CALL add_retail_aux_other_balance_column('eg_contract_balance_latest','early_termination_share_expense_amount');
CALL add_retail_aux_other_balance_column('eg_contract_balance_latest','output_vat_payable_share_balance');
CALL add_retail_aux_other_balance_column('eg_contract_balance_latest','output_vat_payable_share_amount');
CALL add_retail_aux_other_balance_column('eg_contract_balance_latest','lease_interest_income_promotion_balance');
CALL add_retail_aux_other_balance_column('eg_contract_balance_latest','lease_interest_income_promotion_amount');
CALL add_retail_aux_other_balance_column('eg_contract_balance_temp','interbank_interest_receivable_balance');
CALL add_retail_aux_other_balance_column('eg_contract_balance_temp','interbank_interest_receivable_amount');
CALL add_retail_aux_other_balance_column('eg_contract_balance_temp','placement_interest_receivable_balance');
CALL add_retail_aux_other_balance_column('eg_contract_balance_temp','placement_interest_receivable_amount');
CALL add_retail_aux_other_balance_column('eg_contract_balance_temp','interbank_interest_income_balance');
CALL add_retail_aux_other_balance_column('eg_contract_balance_temp','interbank_interest_income_amount');
CALL add_retail_aux_other_balance_column('eg_contract_balance_temp','deposit_interest_income_balance');
CALL add_retail_aux_other_balance_column('eg_contract_balance_temp','deposit_interest_income_amount');
CALL add_retail_aux_other_balance_column('eg_contract_balance_temp','early_termination_share_expense_balance');
CALL add_retail_aux_other_balance_column('eg_contract_balance_temp','early_termination_share_expense_amount');
CALL add_retail_aux_other_balance_column('eg_contract_balance_temp','output_vat_payable_share_balance');
CALL add_retail_aux_other_balance_column('eg_contract_balance_temp','output_vat_payable_share_amount');
CALL add_retail_aux_other_balance_column('eg_contract_balance_temp','lease_interest_income_promotion_balance');
CALL add_retail_aux_other_balance_column('eg_contract_balance_temp','lease_interest_income_promotion_amount');
DROP PROCEDURE add_retail_aux_other_balance_column;

-- 补齐模板使用的科目类型。
DELETE FROM eg_account WHERE id BETWEEN 202609106000001 AND 202609106000020;
INSERT INTO eg_account
 (id,business_code,business_name,fund_type,account_code,account_name,account_category,create_by,create_time,update_by,update_time,del_flag,debit_credit_type,client_flag,contract_flag,assist_flags,settlement_type,check_flag)
VALUES
 (202609106000001,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','interbank_interest_receivable','11240101','应收利息-存放同业应收利息','资产','retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','DR','0','0','2','intra','0'),
 (202609106000002,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','placement_interest_receivable','11240201','应收利息-拆放同业应收利息','资产','retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','DR','0','0','2','intra','0'),
 (202609106000003,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','interbank_interest_income','60110101','金融机构往来利息收入-存放同业利息收入','收入','retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','CR','0','0','2','intra','0'),
 (202609106000004,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','deposit_interest_income','60110102','金融机构往来利息收入-银行存款利息收入','收入','retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','CR','0','0','2','intra','0'),
 (202609106000005,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','early_termination_share_expense','605104','其他业务支出-提前终止费分成','费用','retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','DR','0','1','1','intra','0'),
 (202609106000006,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','output_vat_payable_share','2221010210','应交税费-销项税额-提前终止服务','负债','retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','CR','0','1','1','intra','0'),
 (202609106000007,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','lease_interest_income_promotion','60410201','租赁收入-利息收入-促销补差','收入','retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','CR','1','1','0,1','intra','0');

-- 测试环境补充同一签约主体的第二个银行账号，以验证内部调拨的双银行动态科目。
INSERT INTO eg_bank_account (id,eas_id,bank_account_code,bank_account_name,bank_account_number,org_id,bank_name,account_name,account_code,currency_code,create_by,create_time,update_by,update_time,del_flag)
SELECT 202609106100001,'KD-CYC-RECEIPT-002','CMB-CYC-RECEIPT-002','招行零售回租收款专户','1000000000000000006','HXZL001','招商银行','银行存款-招商银行','1002.12','CNY','retail-aux-other',NOW(),'retail-aux-other',NOW(),'0'
WHERE NOT EXISTS (SELECT 1 FROM eg_bank_account WHERE bank_account_number='1000000000000000006' AND del_flag='0');

INSERT INTO eg_scene_voucher (id,scene_id,source,voucher_type,company,business_date,currency,voucher_summary,create_by,create_time,update_by,update_time,del_flag,script_condition,scene_voucher_name,sub_scene_type) VALUES
 (202609104000001,202609102000001,'systemCode','01','orgId','businessDate','currency',"'辅助账调整-' + sourceEventCode + '-' + orderId",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0',"auxiliaryEventCode == 'AA001'",'项目承租人变更辅助账调整模板','AUTO_NO_APPROVAL'),
 (202609104000002,202609102000001,'systemCode','01','orgId','businessDate','currency',"'辅助账调整-' + sourceEventCode + '-' + orderId",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0',"auxiliaryEventCode == 'AA002'",'辅助核算项目挂错调整模板','AUTO_NO_APPROVAL'),
 (202609104000003,202609102000002,'systemCode','01','orgId','businessDate','currency',"'其他-' + sourceEventCode + '-' + orderId",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0',"otherEventCode == 'OT001' || otherEventCode == 'OT002'",'内部及头寸资金调拨模板','AUTO_NO_APPROVAL'),
 (202609104000004,202609102000002,'systemCode','02','orgId','businessDate','currency',"'其他-季度结息-' + orderId",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0',"otherEventCode == 'OT003'",'存放同业季度结息模板','AUTO_NO_APPROVAL'),
 (202609104000005,202609102000002,'systemCode','02','orgId','businessDate','currency',"'其他-季度结息-' + orderId",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0',"otherEventCode == 'OT004'",'银行存款季度结息模板','AUTO_NO_APPROVAL'),
 (202609104000006,202609102000002,'systemCode','01','orgId','businessDate','currency',"'其他-' + sourceEventCode + '-' + orderId",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0',"otherEventCode == 'OT005'",'多支付分润费挂账模板','AUTO_NO_APPROVAL'),
 (202609104000007,202609102000002,'systemCode','01','orgId','businessDate','currency',"'其他-' + sourceEventCode + '-' + orderId",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0',"otherEventCode == 'OT006'",'金额记错调整模板','AUTO_NO_APPROVAL'),
 (202609104000008,202609102000002,'systemCode','01','orgId','businessDate','currency',"'其他-' + sourceEventCode + '-' + orderId",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0',"otherEventCode == 'OT007'",'违约金分成调整模板','AUTO_NO_APPROVAL'),
 (202609104000009,202609102000002,'systemCode','01','orgId','businessDate','currency',"'其他-' + sourceEventCode + '-' + orderId",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0',"otherEventCode == 'OT008'",'促销核准补差分润费模板','AUTO_NO_APPROVAL');

-- 辅助账模板分录。承租人变更按原维度红字转出、新维度蓝字转入；模板保留成对行。
INSERT INTO eg_scene_voucher_entry (id,scene_voucher_id,fund_type,relate_bank_flag,bank_account,voucher_summary,create_by,create_time,update_by,update_time,del_flag,cash_attribute_flag,assist_flags)
SELECT 202609105000000 + x.seq,202609104000001,x.fund_type,'0',NULL,"contractCode + '-承租人变更辅助账调整'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','0,1'
FROM (
 SELECT 1 seq,'lease_principal_receivable' fund_type UNION ALL SELECT 2,'lease_principal_receivable' UNION ALL
 SELECT 3,'lease_interest_receivable' UNION ALL SELECT 4,'lease_interest_receivable' UNION ALL
 SELECT 5,'residual_value_receivable' UNION ALL SELECT 6,'residual_value_receivable' UNION ALL
 SELECT 7,'lease_interest_vat_receivable' UNION ALL SELECT 8,'lease_interest_vat_receivable' UNION ALL
 SELECT 9,'residual_value_vat_receivable' UNION ALL SELECT 10,'residual_value_vat_receivable' UNION ALL
 SELECT 11,'unearned_lease_interest' UNION ALL SELECT 12,'unearned_lease_interest' UNION ALL
 SELECT 13,'unearned_residual_value' UNION ALL SELECT 14,'unearned_residual_value' UNION ALL
 SELECT 15,'unearned_lease_interest_vat' UNION ALL SELECT 16,'unearned_lease_interest_vat' UNION ALL
 SELECT 17,'unearned_residual_value_vat' UNION ALL SELECT 18,'unearned_residual_value_vat'
) x;

INSERT INTO eg_scene_voucher_entry (id,scene_voucher_id,fund_type,relate_bank_flag,bank_account,voucher_summary,create_by,create_time,update_by,update_time,del_flag,cash_attribute_flag,assist_flags) VALUES
 (202609105000021,202609104000002,'unidentified_receipts','0',NULL,"contractCode + '-其他预收款辅助项调整'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','0,1'),
 (202609105000022,202609104000002,'unidentified_receipts','0',NULL,"contractCode + '-其他预收款辅助项调整'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','0,1'),
 (202609105000023,202609104000002,'vehicle_management_fee_payable','0',NULL,"contractCode + '-车辆管理费应付辅助项调整'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','0,1'),
 (202609105000024,202609104000002,'vehicle_management_fee_payable','0',NULL,"contractCode + '-车辆管理费应付辅助项调整'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','0,1'),
 (202609105000025,202609104000002,'input_vat_receivable','0',NULL,"contractCode + '-待收进项税辅助项调整'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','0,1'),
 (202609105000026,202609104000002,'input_vat_receivable','0',NULL,"contractCode + '-待收进项税辅助项调整'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','0,1'),
 (202609105000027,202609104000002,'unearned_lease_interest','0',NULL,"contractCode + '-未实现利息辅助项调整'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','0,1'),
 (202609105000028,202609104000002,'unearned_lease_interest','0',NULL,"contractCode + '-未实现利息辅助项调整'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','0,1');

INSERT INTO eg_scene_voucher_entry (id,scene_voucher_id,fund_type,relate_bank_flag,bank_account,voucher_summary,create_by,create_time,update_by,update_time,del_flag,cash_attribute_flag,assist_flags) VALUES
 (202609105000101,202609104000003,'bank_deposit','1','targetBankAccountNo',"sourceEventCode + '-转入账户'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','2'),
 (202609105000102,202609104000003,'bank_deposit','1','sourceBankAccountNo',"sourceEventCode + '-转出账户'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','2'),
 (202609105000110,202609104000004,'bank_deposit','1','targetBankAccountNo',"'收到季度存款利息'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','2'),
 (202609105000111,202609104000004,'interbank_interest_receivable','0',NULL,"'核销存放同业应收利息'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','2'),
 (202609105000112,202609104000004,'interbank_interest_receivable','0',NULL,"'确认存放同业利息收入'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','2'),
 (202609105000113,202609104000004,'interbank_interest_income','0',NULL,"'确认存放同业利息收入'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','2'),
 (202609105000120,202609104000005,'bank_deposit','1','targetBankAccountNo',"'收到季度存款利息'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','2'),
 (202609105000121,202609104000005,'placement_interest_receivable','0',NULL,"'核销银行应收利息'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','2'),
 (202609105000122,202609104000005,'placement_interest_receivable','0',NULL,"'确认银行存款利息收入'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','2'),
 (202609105000123,202609104000005,'deposit_interest_income','0',NULL,"'确认银行存款利息收入'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','2'),
 (202609105000130,202609104000006,'vehicle_profit_sharing_payable','0',NULL,"contractCode + '-多支付分润费转出'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','0,1'),
 (202609105000131,202609104000006,'vehicle_profit_sharing_receivable','0',NULL,"contractCode + '-多支付分润费挂账'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','0,1'),
 (202609105000140,202609104000007,'early_termination_share_expense','0',NULL,"contractCode + '-调整提前终止费分成'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','1'),
 (202609105000141,202609104000007,'output_vat_payable_share','0',NULL,"contractCode + '-调整提前终止费税额'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','1'),
 (202609105000142,202609104000007,'channel_commission_payable','0',NULL,"contractCode + '-调整渠道商分成应付'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','1'),
 (202609105000150,202609104000008,'channel_commission_payable','0',NULL,"contractCode + '-调整违约金分成应付'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','1'),
 (202609105000151,202609104000008,'vehicle_profit_sharing_receivable','0',NULL,"contractCode + '-调整违约金分成应收'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','0,1'),
 (202609105000160,202609104000009,'unearned_lease_interest','0',NULL,"contractCode + '-促销补差计提利息'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','0,1'),
 (202609105000161,202609104000009,'input_vat_receivable','0',NULL,"contractCode + '-促销补差待收进项税'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','0,1'),
 (202609105000162,202609104000009,'lease_interest_income_promotion','0',NULL,"contractCode + '-促销补差利息收入'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','0,1'),
 (202609105000163,202609104000009,'vehicle_profit_sharing_payable','0',NULL,"contractCode + '-促销补差应付分润费'",'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0','0','0,1');

INSERT INTO eg_scene_voucher_condition (id,scene_voucher_entry_id,serial,script_condition,dondition_description,script_amount,amount_description,debit_credit_type,create_by,create_time,update_by,update_time,del_flag)
SELECT 202609107000000 + ROW_NUMBER() OVER (ORDER BY x.entry_id),x.entry_id,x.serial,x.cond,x.descr,x.amount,x.amount_desc,x.direction,'retail-aux-other',NOW(),'retail-aux-other',NOW(),'0'
FROM (
 SELECT 202609105000001 entry_id,1 serial,'principalBalance > 0' cond,'原承租人本金红字转出' descr,'0-principalBalance' amount,'账面本金余额' amount_desc,'DR' direction UNION ALL
 SELECT 202609105000002,2,'principalBalance > 0','新承租人本金转入','principalBalance','账面本金余额','DR' UNION ALL
 SELECT 202609105000003,3,'interestBalance > 0','原承租人利息红字转出','0-interestBalance','账面利息余额','DR' UNION ALL
 SELECT 202609105000004,4,'interestBalance > 0','新承租人利息转入','interestBalance','账面利息余额','DR' UNION ALL
 SELECT 202609105000005,5,'residualValueBalance > 0','原承租人留购价红字转出','0-residualValueBalance','账面留购价余额','DR' UNION ALL
 SELECT 202609105000006,6,'residualValueBalance > 0','新承租人留购价转入','residualValueBalance','账面留购价余额','DR' UNION ALL
 SELECT 202609105000007,7,'interestTaxBalance > 0','原承租人利息税红字转出','0-interestTaxBalance','账面利息税余额','DR' UNION ALL
 SELECT 202609105000008,8,'interestTaxBalance > 0','新承租人利息税转入','interestTaxBalance','账面利息税余额','DR' UNION ALL
 SELECT 202609105000009,9,'residualValueTaxBalance > 0','原承租人留购价税红字转出','0-residualValueTaxBalance','账面留购价税余额','DR' UNION ALL
 SELECT 202609105000010,10,'residualValueTaxBalance > 0','新承租人留购价税转入','residualValueTaxBalance','账面留购价税余额','DR' UNION ALL
 SELECT 202609105000011,11,'accruedInterestBalance > 0','原承租人计提利息红字转出','0-accruedInterestBalance','账面计提利息余额','CR' UNION ALL
 SELECT 202609105000012,12,'accruedInterestBalance > 0','新承租人计提利息转入','accruedInterestBalance','账面计提利息余额','CR' UNION ALL
 SELECT 202609105000013,13,'accruedResidualValueBalance > 0','原承租人计提留购价红字转出','0-accruedResidualValueBalance','账面计提留购价余额','CR' UNION ALL
 SELECT 202609105000014,14,'accruedResidualValueBalance > 0','新承租人计提留购价转入','accruedResidualValueBalance','账面计提留购价余额','CR' UNION ALL
 SELECT 202609105000015,15,'accruedInterestTaxBalance > 0','原承租人计提利息税红字转出','0-accruedInterestTaxBalance','账面计提利息税余额','CR' UNION ALL
 SELECT 202609105000016,16,'accruedInterestTaxBalance > 0','新承租人计提利息税转入','accruedInterestTaxBalance','账面计提利息税余额','CR' UNION ALL
 SELECT 202609105000017,17,'accruedResidualValueTaxBalance > 0','原承租人计提留购价税红字转出','0-accruedResidualValueTaxBalance','账面计提留购价税余额','CR' UNION ALL
 SELECT 202609105000018,18,'accruedResidualValueTaxBalance > 0','新承租人计提留购价税转入','accruedResidualValueTaxBalance','账面计提留购价税余额','CR' UNION ALL
 SELECT 202609105000021,1,'unidentifiedReceiptAssistAmount > 0','原辅助项红字转出','0-unidentifiedReceiptAssistAmount','其他预收款辅助项调整金额','DR' UNION ALL
 SELECT 202609105000022,2,'unidentifiedReceiptAssistAmount > 0','新辅助项转入','unidentifiedReceiptAssistAmount','其他预收款辅助项调整金额','DR' UNION ALL
 SELECT 202609105000023,3,'managementFeePayableAssistAmount > 0','原辅助项红字转出','0-managementFeePayableAssistAmount','车辆管理费应付辅助项调整金额','CR' UNION ALL
 SELECT 202609105000024,4,'managementFeePayableAssistAmount > 0','新辅助项转入','managementFeePayableAssistAmount','车辆管理费应付辅助项调整金额','CR' UNION ALL
 SELECT 202609105000025,5,'inputVatReceivableAssistAmount > 0','原辅助项红字转出','0-inputVatReceivableAssistAmount','待收进项税辅助项调整金额','DR' UNION ALL
 SELECT 202609105000026,6,'inputVatReceivableAssistAmount > 0','新辅助项转入','inputVatReceivableAssistAmount','待收进项税辅助项调整金额','DR' UNION ALL
 SELECT 202609105000027,7,'unearnedInterestAssistAmount > 0','原辅助项红字转出','0-unearnedInterestAssistAmount','未实现利息辅助项调整金额','DR' UNION ALL
 SELECT 202609105000028,8,'unearnedInterestAssistAmount > 0','新辅助项转入','unearnedInterestAssistAmount','未实现利息辅助项调整金额','DR' UNION ALL
 SELECT 202609105000101,1,'transferAmount > 0','转入账户','transferAmount','调拨金额','DR' UNION ALL
 SELECT 202609105000102,2,'transferAmount > 0','转出账户','transferAmount','调拨金额','CR' UNION ALL
 SELECT 202609105000110,1,'quarterlyInterestAmount > 0','银行收到利息','quarterlyInterestAmount','季度结息金额','DR' UNION ALL
 SELECT 202609105000111,2,'quarterlyInterestAmount > 0','核销应收利息','quarterlyInterestAmount','季度结息金额','CR' UNION ALL
 SELECT 202609105000112,3,'quarterlyInterestAmount > 0','确认应收利息','quarterlyInterestAmount','季度结息金额','DR' UNION ALL
 SELECT 202609105000113,4,'quarterlyInterestAmount > 0','确认利息收入','quarterlyInterestAmount','季度结息金额','CR' UNION ALL
 SELECT 202609105000120,1,'quarterlyInterestAmount > 0','银行收到利息','quarterlyInterestAmount','季度结息金额','DR' UNION ALL
 SELECT 202609105000121,2,'quarterlyInterestAmount > 0','核销应收利息','quarterlyInterestAmount','季度结息金额','CR' UNION ALL
 SELECT 202609105000122,3,'quarterlyInterestAmount > 0','确认应收利息','quarterlyInterestAmount','季度结息金额','DR' UNION ALL
 SELECT 202609105000123,4,'quarterlyInterestAmount > 0','确认利息收入','quarterlyInterestAmount','季度结息金额','CR' UNION ALL
 SELECT 202609105000130,1,'overpaidProfitSharingAmount > 0','转出应付分润费','overpaidProfitSharingAmount','多支付分润费金额','CR' UNION ALL
 SELECT 202609105000131,2,'overpaidProfitSharingAmount > 0','挂账应收分润费','overpaidProfitSharingAmount','多支付分润费金额','DR' UNION ALL
 SELECT 202609105000140,1,'channelShareAmount > 0','调整提前终止费分成支出','channelShareNetAmount','渠道商分成不含税金额','DR' UNION ALL
 SELECT 202609105000141,2,'channelShareAmount > 0','调整提前终止费税额','channelShareTaxAmount','渠道商分成税额','DR' UNION ALL
 SELECT 202609105000142,3,'channelShareAmount > 0','确认渠道商分成应付','channelShareAmount','渠道商分成含税金额','CR' UNION ALL
 SELECT 202609105000150,1,'penaltyShareAdjustmentAmount > 0','调整渠道商分成应付','penaltyShareAdjustmentAmount','违约金分成调整金额','DR' UNION ALL
 SELECT 202609105000151,2,'penaltyShareAdjustmentAmount > 0','调整应收车辆清分款','penaltyShareAdjustmentAmount','违约金分成调整金额','CR' UNION ALL
 SELECT 202609105000160,1,'promotionInterestAmount > 0','促销补差计提利息','promotionInterestAmount','促销补差计提利息','DR' UNION ALL
 SELECT 202609105000161,2,'promotionInputVatAmount > 0','促销补差待收进项税','promotionInputVatAmount','促销补差待收进项税','DR' UNION ALL
 SELECT 202609105000162,3,'promotionIncomeAmount > 0','促销补差利息收入','promotionIncomeAmount','促销补差利息收入','CR' UNION ALL
 SELECT 202609105000163,4,'promotionProfitSharingAmount > 0','促销补差应付分润费','promotionProfitSharingAmount','促销补差应付分润费','CR'
) x;

COMMIT;
