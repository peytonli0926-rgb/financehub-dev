
export const baseInfoList = [{
  title: '合同信息',
  id: '1',

  column: [{
    props: [{
      name: '合同编号',
      prop: 'contractCode'
    },
    {
      name: '合同名称',
      prop: 'contractName'
    }, {
      name: '签约主体',
      prop: 'orgId',
      type: 'dict',
      dictType: 'company'
    }]
  }, {
    props: [
      {
        name: '服务费合同编号',
        prop: 'contractCodeM'
      }, {
        name: '服务合同主体',
        prop: 'mainOrgIdName'
      }, {
        name: '签约日期',
        prop: 'dwContractSignDate',
        type: 'date'
      }]
  }, {
    props: [{
      name: '业务合同状态',
      prop: 'contractStatus',
      dictType: 'business_contract_status',
      type: 'dict'
    },
    {
      name: '财务合同状态',
      prop: 'financialContractStatus',
      dictType: 'financial_contract_status',
      type: 'dict'
    }, {
      name: '合同出单部门',
      prop: 'contractCreateDept',
      dictType: 'contract_create_dept',
      type: 'dict'

    }]
  }, {
    props: [{
      name: '货币类型',
      prop: 'currencyType',
      dictType: 'sys_currency_type',
      type: 'dict'
    },
    {
      name: '所属系统',
      prop: 'systemCode',
      dictType: 'sys_form_source',
      type: 'dict'
    }, {
      name: '项目编号',
      prop: 'projectNumber'
    }]
  }, {
    props: [{
      name: '客户编号',
      prop: 'clientCode'
    },
    {
      name: '客户名称',
      prop: 'clientName'
    }, {
      name: '客户类型',
      prop: 'clientType',
      dictType: 'sys_client_type',
      type: 'dict'
    }]
  }, {
    props: [{
      name: '组织机构代码/身份证号',
      prop: 'clientIdNumber'
    },
    {
      name: '起租日',
      prop: 'leaseDateStart',
      type: 'date'
    }, {
      name: '到期日',
      prop: 'leaseDateEnd',
      type: 'date'
    }]
  }]
}, {
  title: '业务信息',
  id: '2',

  column: [{
    props: [{
      name: '项目立项日期',
      prop: 'projectApproveDate'
    },
    {
      name: '租赁大类',
      prop: 'businessName'
    }, {
      name: '租赁类型',
      prop: 'leaseType',
      dictType: 'lease_type'
    }]
  }, {
    props: [{
      name: '租赁设备类型',
      prop: 'dwLeaseDeviceType'
    },
    {
      name: '业务种类',
      prop: 'dwBusinessType'
    }, {
      name: '车辆类型',
      prop: 'vehicleType'
    }]
  }, {
    props: [{
      name: '国标行业',
      prop: 'dwIndustryTag'
    },
    {
      name: '新行业分类',
      prop: 'newIndustry'
    }, {
      name: '海通一大一小标签',
      prop: 'dwLargeSmallTag'
    }]
  }, {
    props: [{
      name: '海通客户性质标签',
      prop: 'dwClientNature'
    },
    {
      name: '办事处',
      prop: 'office'
    }, {
      name: '厂商',
      prop: 'manufacturer'
    }]
  }, {
    props: [{
      name: '区域',
      prop: 'dwDistrict'
    },
    {
      name: '省',
      prop: 'province'
    }, {
      name: '市',
      prop: 'city'
    }]
  }, {
    props: [{
      name: '业绩归属部门',
      prop: 'performanceBelongDept'
    },
    {
      name: '业绩归属员工',
      prop: 'performanceBelongEmployee'
    }, {
      name: '业务板块',
      prop: 'businessPlate'
    }]
  }]
}, {
  title: '财务信息',
  id: '3',

  column: [{
    props: [{
      name: '租赁发票类型',
      prop: 'invoiceType',
      dictType: 'invoice_type',
      type: 'dict'
    },
    {
      name: '开票标识',
      prop: 'invoicingFlag',
      dictType: 'invoicing_flag',
      type: 'dict'
    }, {
      name: '利率浮动类型',
      prop: 'interestRateType',
      dictType: 'interest_rate_type',
      type: 'dict'
    }]
  }, {
    props: [{
      name: '收益计算',
      prop: 'incomeCalculate',
      dictType: 'income_calculate',
      type: 'dict'
    },
    {
      name: '收益计提方式',
      prop: 'incomeProvisionMethod',
      dictType: 'income_provision_method',
      type: 'dict'
    }, {
      name: '设备金额',
      prop: 'payableDeviceAmount'
    }]
  }, {
    props: [{
      name: '税率(%)',
      prop: 'taxRate'
    },
    {
      name: '',
      prop: ''
    }, {
      name: '',
      prop: ''
    }
    ]
  }]
},
{
  title: '出表信息',
  id: '4',

  column: [{
    props: [{
      name: '是否出表',
      prop: 'dwHasTable'
    },
    {
      name: '拟出表',
      prop: 'dwPlanTable'
    }, {
      name: '出表类型',
      prop: 'tableType'
    }]
  }, {
    props: [{
      name: 'ABS出表项目名称',
      prop: 'absTableProjectName'
    },
    {
      name: '封包日',
      prop: 'closePackageDate',
      type: 'date'
    }, {
      name: '发行日',
      prop: 'releaseDate',
      type: 'date'
    }]
  }, {
    props: [{
      name: '受让方',
      prop: 'receiver'
    },
    {
      name: '',
      prop: ''
    }, {
      name: '',
      prop: ''
    }]
  }]
},
{
  title: '转让信息',
  id: '5',

  column: [{
    props: [{
      name: '转让时间(转让基准日)',
      prop: 'transferDate'
    },
    {
      name: '是否内部转让合同',
      prop: 'transferFlag'
    }, {
      name: '转让前合同号',
      prop: 'beforeContractCode'
    }]
  }, {
    props: [{
      name: '转让前主体',
      prop: 'beforeSubject'
    },
    {
      name: '',
      prop: ''
    }, {
      name: '',
      prop: ''
    }]
  }]
}, {
  title: '抵债资产相关信息',
  id: '6',

  column: [{
    props: [{
      name: '评估主体',
      prop: 'assessmentSubject'
    },
    {
      name: '业务员',
      prop: 'businessMan'
    }, {
      name: '逾期阶段',
      prop: 'overdueStage'
    }]
  }, {
    props: [{
      name: '五级分类',
      prop: 'classificationFive'
    },
    {
      name: '风险阶段划分',
      prop: 'riskStage'
    }, {
      name: '',
      prop: ''
    }]
  }]
}
]

export const businessStractList = [{
  title: '交易结构信息',
  id: '1',
  column: [{

    props: [{
      name: '设备价格',
      prop: 'payableDeviceAmount',
      type: 'currency'
    },
    {
      name: '租金首付款',
      prop: 'receivableFirstAmount',
      type: 'currency'
    }]
  }, {
    props: [{
      name: '出租人保险费',
      prop: 'lessorInsuranceAmount',
      type: 'currency'
    },
    {
      name: '承租人履约保证金',
      prop: 'receivableMarginAmount',
      type: 'currency'
    }]
  }, {
    props: [{
      name: '渠道费用',
      prop: 'channelFees',
      type: 'currency'
    },
    {
      name: '手续费收入(含增值税)',
      prop: 'receivableProcedureAmount',
      type: 'currency'
    }]
  }, {
    props: [{
      name: '出租人其它成本',
      prop: 'lessorOtherCosts',
      type: 'currency'
    },
    {
      name: '名义留购价',
      prop: 'retainedPrice',
      type: 'currency'
    }]
  }, {
    props: [{
      name: '承租人保险费',
      prop: 'receivableInsuranceAmount',
      type: 'currency'
    },
    {
      name: '租赁销售额',
      prop: 'rentSalesAmount',
      type: 'currency'
    }]
  }, {
    props: [{
      name: '其他收入 (含增值税)',
      prop: 'receivableOther',
      type: 'currency'
    },
    {
      name: '供应商履约保证金',
      prop: 'vendorMarginAmount',
      type: 'currency'
    }]
  }, {
    props: [{
      name: '咨询服务收入(含增值税)',
      prop: 'receivableServiceAmount',
      type: 'currency'
    },
    {
      name: '租赁合同总计',
      prop: 'rentContractTotal',
      type: 'currency'
    }]
  }, {
    props: [{
      name: '租金概算本金',
      prop: 'rentPrincipal',
      type: 'currency'
    },
    {
      name: '租赁合同收入总计',
      prop: 'rentContractIncomesTotal',
      type: 'currency'
    }]
  }, {
    props: [{
      name: '净融资额',
      prop: 'financingAmount',
      type: 'currency'
    },
    {
      name: '厂商返利(含增值税)',
      prop: 'receivableFirmRebate',
      type: 'currency'
    }]
  }, {
    props: [{
      name: '还款标识',
      prop: 'payMethod'
      // type: 'currency'
    },
    {
      name: '还租方式',
      prop: 'returnType'
      // type: 'currency'
    }]
  }]
}
]
