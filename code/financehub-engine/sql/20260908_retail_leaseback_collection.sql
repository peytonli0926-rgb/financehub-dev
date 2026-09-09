-- 零售融资租赁/回租/乘用车：统一“收款”场景。
-- 来源事件经值映射得到 collectionEventCode；共享金额字段只配置一次。
START TRANSACTION;

DELETE FROM eg_field_mapping WHERE id BETWEEN 202609075000001 AND 202609075000020;
INSERT INTO eg_field_mapping
 (id,system_code,field_code,field_name,source_value,target_value,default_value,create_by,create_time,update_by,update_time,del_flag,target_field_code)
VALUES
 (202609075000001,'CYCXT','eventCode','收款业务事件','收到现金折扣,C000,CR000,收取保证金,收到保证金,C001,CR001,到并核销租金,收到租金,C005,CR005,款项无法确认,款项无法确认款,C006,CR006,人工明确款项用途,明确不明款项用途,C007,CR007,收到款项核销,款项核销,C008,CR008,退回的分润费,收到退回分润费,C033,CR033,前期逾期留购价,收到前期逾期留购价,C043,CR043','CYC_COLLECTION',NULL,'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','sceneCode'),
 (202609075000002,'CYCXT','eventCode','收到现金折扣','收到现金折扣,C000,CR000','CR000',NULL,'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','collectionEventCode'),
 (202609075000003,'CYCXT','eventCode','收取保证金','收取保证金,收到保证金,C001,CR001','CR001',NULL,'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','collectionEventCode'),
 (202609075000004,'CYCXT','eventCode','到并核销租金','到并核销租金,收到租金,C005,CR005','CR005',NULL,'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','collectionEventCode'),
 (202609075000005,'CYCXT','eventCode','款项无法确认','款项无法确认,款项无法确认款,C006,CR006','CR006',NULL,'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','collectionEventCode'),
 (202609075000006,'CYCXT','eventCode','人工明确款项用途','人工明确款项用途,明确不明款项用途,C007,CR007','CR007',NULL,'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','collectionEventCode'),
 (202609075000007,'CYCXT','eventCode','收到款项核销','收到款项核销,款项核销,C008,CR008','CR008',NULL,'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','collectionEventCode'),
 (202609075000008,'CYCXT','eventCode','退回的分润费','退回的分润费,收到退回分润费,C033,CR033','CR033',NULL,'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','collectionEventCode'),
 (202609075000009,'CYCXT','eventCode','前期逾期留购价','前期逾期留购价,收到前期逾期留购价,C043,CR043','CR043',NULL,'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','collectionEventCode');

DELETE c FROM eg_scene_voucher_condition c JOIN eg_scene_voucher_entry e ON e.id=c.scene_voucher_entry_id JOIN eg_scene_voucher v ON v.id=e.scene_voucher_id JOIN eg_scene s ON s.id=v.scene_id WHERE s.scene_code='CYC_COLLECTION';
DELETE e FROM eg_scene_voucher_entry e JOIN eg_scene_voucher v ON v.id=e.scene_voucher_id JOIN eg_scene s ON s.id=v.scene_id WHERE s.scene_code='CYC_COLLECTION';
DELETE v FROM eg_scene_voucher v JOIN eg_scene s ON s.id=v.scene_id WHERE s.scene_code='CYC_COLLECTION';
DELETE FROM eg_scene_fields WHERE scene_code='CYC_COLLECTION';
DELETE FROM eg_business_scene WHERE scene_code='CYC_COLLECTION';
DELETE FROM eg_scene WHERE scene_code='CYC_COLLECTION';

INSERT INTO eg_scene (id,scene_code,scene_name,scene_period,enable_flag,create_by,create_time,update_by,update_time,del_flag,version)
VALUES (202609075100001,'CYC_COLLECTION','收款','order','1','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0',2);
INSERT INTO eg_business_scene (id,business_id,scene_id,scene_code,scene_name,serial,create_by,create_time,update_by,update_time,del_flag)
VALUES (202609075110001,202609070100003,202609075100001,'CYC_COLLECTION','收款',6,'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0');
UPDATE eg_business b SET b.scene_count=(SELECT COUNT(*) FROM eg_business_scene bs WHERE bs.business_id=b.id AND bs.del_flag='0'),b.update_by='retail-collection-config',b.update_time=NOW() WHERE b.id=202609070100003;

INSERT INTO eg_scene_fields
 (id,scene_id,scene_code,scene_name,field_name,field_code,data_type,create_by,create_time,update_by,update_time,del_flag,required_flag,sort_no)
VALUES
 (202609075200001,202609075100001,'CYC_COLLECTION','收款','系统来源','systemCode','String','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0',1),
 (202609075200002,202609075100001,'CYC_COLLECTION','收款','请求流水号','orderId','String','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','1',2),
 (202609075200003,202609075100001,'CYC_COLLECTION','收款','财务收款事件','eventCode','String','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','1',3),
 (202609075200004,202609075100001,'CYC_COLLECTION','收款','来源业务事件','sourceEventCode','String','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','1',4),
 (202609075200005,202609075100001,'CYC_COLLECTION','收款','业务日期','businessDate','String','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','1',5),
 (202609075200006,202609075100001,'CYC_COLLECTION','收款','合同编号','contractCode','String','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','1',6),
 (202609075200007,202609075100001,'CYC_COLLECTION','收款','客户编码','clientCode','String','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0',7),
 (202609075200008,202609075100001,'CYC_COLLECTION','收款','客户名称','clientName','String','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0',8),
 (202609075200009,202609075100001,'CYC_COLLECTION','收款','签约主体编码','orgId','String','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0',9),
 (202609075200010,202609075100001,'CYC_COLLECTION','收款','签约主体名称','orgName','String','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0',10),
 (202609075200011,202609075100001,'CYC_COLLECTION','收款','到账银行账号','bankNo','String','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0',11),
 (202609075200012,202609075100001,'CYC_COLLECTION','收款','币种','currency','String','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0',12),
 (202609075200013,202609075100001,'CYC_COLLECTION','收款','实际收款金额','receivedAmount','Number','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0',13),
 (202609075200014,202609075100001,'CYC_COLLECTION','收款','核销金额','applicationAmount','Number','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0',14),
 (202609075200015,202609075100001,'CYC_COLLECTION','收款','现金折扣金额','cashDiscountAmount','Number','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0',15),
 (202609075200016,202609075100001,'CYC_COLLECTION','收款','保证金金额','depositAmount','Number','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0',16),
 (202609075200017,202609075100001,'CYC_COLLECTION','收款','未确认收款金额','unidentifiedAmount','Number','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0',17),
 (202609075200018,202609075100001,'CYC_COLLECTION','收款','本金金额','principalAmount','Number','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0',18),
 (202609075200019,202609075100001,'CYC_COLLECTION','收款','不含税利息','interestAmount','Number','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0',19),
 (202609075200020,202609075100001,'CYC_COLLECTION','收款','利息增值税','interestTaxAmount','Number','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0',20),
 (202609075200021,202609075100001,'CYC_COLLECTION','收款','不含税留购价','residualValueAmount','Number','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0',21),
 (202609075200022,202609075100001,'CYC_COLLECTION','收款','留购价增值税','residualValueTaxAmount','Number','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0',22),
 (202609075200023,202609075100001,'CYC_COLLECTION','收款','逾期本金','overduePrincipalAmount','Number','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0',23),
 (202609075200024,202609075100001,'CYC_COLLECTION','收款','逾期不含税利息','overdueInterestAmount','Number','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0',24),
 (202609075200025,202609075100001,'CYC_COLLECTION','收款','逾期利息增值税','overdueInterestTaxAmount','Number','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0',25),
 (202609075200026,202609075100001,'CYC_COLLECTION','收款','逾期不含税留购价','overdueResidualValueAmount','Number','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0',26),
 (202609075200027,202609075100001,'CYC_COLLECTION','收款','逾期留购价增值税','overdueResidualValueTaxAmount','Number','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0',27),
 (202609075200028,202609075100001,'CYC_COLLECTION','收款','退回分润费','profitSharingRefundAmount','Number','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0',28),
 (202609075200029,202609075100001,'CYC_COLLECTION','收款','付款方名称','payerName','String','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0',29),
 (202609075200030,202609075100001,'CYC_COLLECTION','收款','银行流水号','transactionSerial','String','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0',30),
 (202609075200031,202609075100001,'CYC_COLLECTION','收款','备注','remark','String','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0',31);

DELETE FROM eg_bank_account WHERE id=202609075800001;
INSERT INTO eg_bank_account (id,eas_id,bank_account_code,bank_account_name,bank_account_number,org_id,bank_name,account_name,account_code,currency_code,create_by,create_time,update_by,update_time,del_flag)
VALUES (202609075800001,'KD-CYC-RECEIPT-001','ICBC-CYC-RECEIPT-001','工行零售回租收款专户','1000000000000000005','HXZL001','中国工商银行','银行存款-工商银行','1002.01','CNY','kingdee-sync',NOW(),'kingdee-sync',NOW(),'0');

-- 将通用租赁科目明确映射到本业务类型，避免规则执行时跨业务取科目。
DELETE FROM eg_account WHERE id BETWEEN 202609075700001 AND 202609075700004;
INSERT INTO eg_account
 (id,business_code,business_name,fund_type,account_code,account_name,account_category,create_by,create_time,update_by,update_time,del_flag,debit_credit_type,client_flag,contract_flag,assist_flags,settlement_type,check_flag)
VALUES
 (202609075700001,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','residual_value_receivable','15310801','应收融资租赁款-留购价','资产','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','DR','1','0','0,1','intra','0'),
 (202609075700002,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','residual_value_vat_receivable','15311201','应收融资租赁款-留购价增值税','资产','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','DR','1','0','0,1','intra','0'),
 (202609075700003,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','overdue_residual_value','153127','应收融资租赁款-逾期留购价','资产','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','DR','1','0','0,1','intra','0'),
 (202609075700004,'CYC_RETAIL_LEASEBACK','融资租赁业务-回租-乘用车','overdue_residual_value_vat','153129','应收融资租赁款-逾期留购价增值税','资产','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','DR','1','0','0,1','intra','0');

-- 银行到账类事件：统一使用收款凭证。
INSERT INTO eg_scene_voucher (id,scene_id,source,voucher_type,company,business_date,currency,voucher_summary,create_by,create_time,update_by,update_time,del_flag,script_condition,scene_voucher_name,sub_scene_type)
VALUES (202609075300001,202609075100001,'systemCode','02','orgId','businessDate','currency',"'收款-' + sourceEventCode + '-' + orderId",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0',"collectionEventCode != 'CR007' && collectionEventCode != 'CR008' && receivedAmount > 0",'零售回租收款-银行到账模板','AUTO_NO_APPROVAL');

-- 转账核销类事件：前期未确认收款转入具体用途。
INSERT INTO eg_scene_voucher (id,scene_id,source,voucher_type,company,business_date,currency,voucher_summary,create_by,create_time,update_by,update_time,del_flag,script_condition,scene_voucher_name,sub_scene_type)
VALUES (202609075300002,202609075100001,'systemCode','01','orgId','businessDate','currency',"'收款核销-' + sourceEventCode + '-' + orderId",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0',"(collectionEventCode == 'CR007' || collectionEventCode == 'CR008') && applicationAmount > 0",'零售回租收款-未确认款项核销模板','AUTO_NO_APPROVAL');

-- 两类模板均引用同一套场景金额字段；下列分录只在对应金额大于0时生成。
INSERT INTO eg_scene_voucher_entry (id,scene_voucher_id,fund_type,relate_bank_flag,bank_account,voucher_summary,create_by,create_time,update_by,update_time,del_flag,cash_attribute_flag,assist_flags)
VALUES
 (202609075400001,202609075300001,'bank_deposit','1','bankNo',"contractCode + '-' + sourceEventCode + '-银行到账'",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0','2'),
 (202609075400002,202609075300001,'lease_asset_movable_leaseback','0',NULL,"contractCode + '-收到现金折扣'",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0','0,1'),
 (202609075400003,202609075300001,'customer_deposit_payable','0',NULL,"contractCode + '-收取保证金'",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0','0,1'),
 (202609075400004,202609075300001,'unidentified_receipts','0',NULL,"contractCode + '-款项无法确认'",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0','0,1'),
 (202609075400005,202609075300001,'lease_principal_receivable','0',NULL,"contractCode + '-核销租赁本金'",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0','0,1'),
 (202609075400006,202609075300001,'lease_interest_receivable','0',NULL,"contractCode + '-核销租赁利息'",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0','0,1'),
 (202609075400007,202609075300001,'lease_interest_vat_receivable','0',NULL,"contractCode + '-核销利息增值税'",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0','0,1'),
 (202609075400008,202609075300001,'residual_value_receivable','0',NULL,"contractCode + '-核销留购价'",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0','0,1'),
 (202609075400009,202609075300001,'residual_value_vat_receivable','0',NULL,"contractCode + '-核销留购价增值税'",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0','0,1'),
 (202609075400010,202609075300001,'overdue_lease_principal','0',NULL,"contractCode + '-核销逾期本金'",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0','0,1'),
 (202609075400011,202609075300001,'overdue_lease_interest','0',NULL,"contractCode + '-核销逾期利息'",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0','0,1'),
 (202609075400012,202609075300001,'overdue_lease_interest_vat','0',NULL,"contractCode + '-核销逾期利息增值税'",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0','0,1'),
 (202609075400013,202609075300001,'overdue_residual_value','0',NULL,"contractCode + '-核销逾期留购价'",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0','0,1'),
 (202609075400014,202609075300001,'overdue_residual_value_vat','0',NULL,"contractCode + '-核销逾期留购价增值税'",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0','0,1'),
 (202609075400015,202609075300001,'vehicle_profit_sharing_receivable','0',NULL,"contractCode + '-收到退回分润费'",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0','0,1'),
 (202609075400101,202609075300002,'unidentified_receipts','0',NULL,"contractCode + '-' + sourceEventCode + '-核销前期未确认收款'",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0','0,1'),
 (202609075400103,202609075300002,'customer_deposit_payable','0',NULL,"contractCode + '-确认保证金用途'",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0','0,1'),
 (202609075400105,202609075300002,'lease_principal_receivable','0',NULL,"contractCode + '-确认租赁本金用途'",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0','0,1'),
 (202609075400106,202609075300002,'lease_interest_receivable','0',NULL,"contractCode + '-确认租赁利息用途'",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0','0,1'),
 (202609075400107,202609075300002,'lease_interest_vat_receivable','0',NULL,"contractCode + '-确认利息增值税用途'",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0','0,1'),
 (202609075400108,202609075300002,'residual_value_receivable','0',NULL,"contractCode + '-确认留购价用途'",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0','0,1'),
 (202609075400109,202609075300002,'residual_value_vat_receivable','0',NULL,"contractCode + '-确认留购价增值税用途'",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0','0,1'),
 (202609075400110,202609075300002,'overdue_lease_principal','0',NULL,"contractCode + '-确认逾期本金用途'",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0','0,1'),
 (202609075400111,202609075300002,'overdue_lease_interest','0',NULL,"contractCode + '-确认逾期利息用途'",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0','0,1'),
 (202609075400112,202609075300002,'overdue_lease_interest_vat','0',NULL,"contractCode + '-确认逾期利息增值税用途'",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0','0,1'),
 (202609075400113,202609075300002,'overdue_residual_value','0',NULL,"contractCode + '-确认逾期留购价用途'",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0','0,1'),
 (202609075400114,202609075300002,'overdue_residual_value_vat','0',NULL,"contractCode + '-确认逾期留购价增值税用途'",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0','0,1'),
 (202609075400115,202609075300002,'vehicle_profit_sharing_receivable','0',NULL,"contractCode + '-确认退回分润费用途'",'retail-collection-config',NOW(),'retail-collection-config',NOW(),'0','0','0,1');

INSERT INTO eg_scene_voucher_condition (id,scene_voucher_entry_id,serial,script_condition,dondition_description,script_amount,amount_description,debit_credit_type,create_by,create_time,update_by,update_time,del_flag)
VALUES
 (202609075500001,202609075400001,1,'receivedAmount > 0','银行实际到账','receivedAmount','实际收款金额','DR','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0'),
 (202609075500002,202609075400002,2,'cashDiscountAmount > 0','收到现金折扣','cashDiscountAmount','现金折扣金额','CR','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0'),
 (202609075500003,202609075400003,3,'depositAmount > 0','收取保证金','depositAmount','保证金金额','CR','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0'),
 (202609075500004,202609075400004,4,'unidentifiedAmount > 0','款项无法确认','unidentifiedAmount','未确认收款金额','CR','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0'),
 (202609075500005,202609075400005,5,'principalAmount > 0','核销租赁本金','principalAmount','本金金额','CR','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0'),
 (202609075500006,202609075400006,6,'interestAmount > 0','核销租赁利息','interestAmount','不含税利息','CR','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0'),
 (202609075500007,202609075400007,7,'interestTaxAmount > 0','核销利息增值税','interestTaxAmount','利息增值税','CR','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0'),
 (202609075500008,202609075400008,8,'residualValueAmount > 0','核销留购价','residualValueAmount','不含税留购价','CR','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0'),
 (202609075500009,202609075400009,9,'residualValueTaxAmount > 0','核销留购价增值税','residualValueTaxAmount','留购价增值税','CR','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0'),
 (202609075500010,202609075400010,10,'overduePrincipalAmount > 0','核销逾期本金','overduePrincipalAmount','逾期本金','CR','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0'),
 (202609075500011,202609075400011,11,'overdueInterestAmount > 0','核销逾期利息','overdueInterestAmount','逾期不含税利息','CR','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0'),
 (202609075500012,202609075400012,12,'overdueInterestTaxAmount > 0','核销逾期利息增值税','overdueInterestTaxAmount','逾期利息增值税','CR','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0'),
 (202609075500013,202609075400013,13,'overdueResidualValueAmount > 0','核销逾期留购价','overdueResidualValueAmount','逾期不含税留购价','CR','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0'),
 (202609075500014,202609075400014,14,'overdueResidualValueTaxAmount > 0','核销逾期留购价增值税','overdueResidualValueTaxAmount','逾期留购价增值税','CR','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0'),
 (202609075500015,202609075400015,15,'profitSharingRefundAmount > 0','收到退回分润费','profitSharingRefundAmount','退回分润费','CR','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0'),
 (202609075500101,202609075400101,1,'applicationAmount > 0','核销前期未确认收款','applicationAmount','核销金额','DR','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0'),
 (202609075500103,202609075400103,3,'depositAmount > 0','确认保证金用途','depositAmount','保证金金额','CR','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0'),
 (202609075500105,202609075400105,5,'principalAmount > 0','确认租赁本金用途','principalAmount','本金金额','CR','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0'),
 (202609075500106,202609075400106,6,'interestAmount > 0','确认租赁利息用途','interestAmount','不含税利息','CR','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0'),
 (202609075500107,202609075400107,7,'interestTaxAmount > 0','确认利息增值税用途','interestTaxAmount','利息增值税','CR','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0'),
 (202609075500108,202609075400108,8,'residualValueAmount > 0','确认留购价用途','residualValueAmount','不含税留购价','CR','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0'),
 (202609075500109,202609075400109,9,'residualValueTaxAmount > 0','确认留购价增值税用途','residualValueTaxAmount','留购价增值税','CR','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0'),
 (202609075500110,202609075400110,10,'overduePrincipalAmount > 0','确认逾期本金用途','overduePrincipalAmount','逾期本金','CR','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0'),
 (202609075500111,202609075400111,11,'overdueInterestAmount > 0','确认逾期利息用途','overdueInterestAmount','逾期不含税利息','CR','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0'),
 (202609075500112,202609075400112,12,'overdueInterestTaxAmount > 0','确认逾期利息增值税用途','overdueInterestTaxAmount','逾期利息增值税','CR','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0'),
 (202609075500113,202609075400113,13,'overdueResidualValueAmount > 0','确认逾期留购价用途','overdueResidualValueAmount','逾期不含税留购价','CR','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0'),
 (202609075500114,202609075400114,14,'overdueResidualValueTaxAmount > 0','确认逾期留购价增值税用途','overdueResidualValueTaxAmount','逾期留购价增值税','CR','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0'),
 (202609075500115,202609075400115,15,'profitSharingRefundAmount > 0','确认退回分润费用途','profitSharingRefundAmount','退回分润费','CR','retail-collection-config',NOW(),'retail-collection-config',NOW(),'0');

COMMIT;
