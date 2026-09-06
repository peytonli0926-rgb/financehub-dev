<template>
  <el-form :model="sureFormData" ref="sureFormDataRef" label-width="100px">
    <el-row>
      <el-col :span="8">
        <el-form-item
          label="记账日期"
          prop="accountDate"
          :rules="{ required: true, message: '请选择记账日期', trigger: ['blur', 'change'] }"
        >
          <el-date-picker
            type="date"
            value-format="YYYY-MM-DD"
            format="YYYY-MM-DD"
            v-model="sureFormData.accountDate"
            placeholder="请选择记账日期"
          ></el-date-picker>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item
          label-width="180px"
          label="否涉及其他客户及辅助帐"
          prop="isRelateClientAuxiliaryAccount"
          :rules="{
            required: true,
            message: '请选择否涉及其他客户及辅助帐',
            trigger: ['blur', 'change']
          }"
        >
          <USelect
            :option="dictList.is_sys_bool"
            class="w-100 u-flex-1"
            v-model="sureFormData.isRelateClientAuxiliaryAccount"
          />
        </el-form-item>
      </el-col>
      <el-col>
        <!-- {{ sureFormData.writeOffDetailList }} -->
        <el-table
          :data="sureFormData.writeOffDetailList"
          border
          :summary-method="getSummaries"
          :show-summary="true"
        >
          <el-table-column label="到账主体" prop="collectionAccountsBank"> </el-table-column>
          <el-table-column label="业务系统批扣流水号" prop="ebankSerialNumber"> </el-table-column>
          <el-table-column label="到账金额" prop="bankAmount">
            <template v-slot="{ row }">
              <component :is="toThousands(row.bankAmount)"></component>
            </template>
          </el-table-column>
          <el-table-column label="冲销金额" prop="writeOffAmount" :width="250">
            <template v-slot="{ row, $index }">
              <el-form-item
                label-width="0px"
                :prop="`writeOffDetailList.${$index}.writeOffAmount`"
                :rules="{
                  required: true,
                  max: row.bankAmount,
                  validator: valideClaimAmount,
                  trigger: ['blur', 'change']
                }"
              >
                <el-input-number
                  controls-position="right"
                  class="w-100"
                  v-model="row.writeOffAmount"
                  placeholder="请输入冲销金额"
                />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="冲销备注">
            <template v-slot="{ row }">
              <el-input v-model="row.remark" placeholder="请输入冲销备注"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="认领主体">
            <template v-slot="{ row }">
              <USelect
                :option="companyList"
                class="w-100 u-flex-1"
                :attrs="{ filterable: true }"
                v-model="row.orgId"
              />
            </template>
          </el-table-column>
        </el-table>
      </el-col>
      <el-col>
        <el-divider />
        <el-table :data="sureFormData.writeOffConfirmVoucherList" border>
          <el-table-column label="做账主体">
            <template v-slot="{ row, $index }">
              <el-form-item
                label-width="0px"
                :prop="`writeOffConfirmVoucherList.${$index}.createConfirmOrgId`"
                :rules="{ required: true, message: '请选择做账主体', trigger: ['blur', 'change'] }"
              >
                <USelect
                  :option="companyList"
                  class="w-100 u-flex-1"
                  :attrs="{ filterable: true }"
                  v-model="row.createConfirmOrgId"
                />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="借贷方向" prop="crOrDt">
            <template v-slot="{ row, $index }">
              <el-form-item
                label-width="0px"
                :prop="`writeOffConfirmVoucherList.${$index}.crOrDt`"
                :rules="{
                  required: true,
                  message: '请选择科借贷方向',
                  trigger: ['blur', 'change']
                }"
              >
                <USelect :option="debitCreditList" class="w-100 u-flex-1" v-model="row.crOrDt" />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="科目名称">
            <template v-slot="{ row, $index }">
              <el-form-item
                label-width="0px"
                :prop="`writeOffConfirmVoucherList.${$index}.accountNumber`"
                :rules="{ required: true, message: '请选择科目名称', trigger: ['blur', 'change'] }"
              >
                <USelect
                  :attrs="{ filterable: true }"
                  :keyValue="{ label: 'label', value: 'value', formatLabel: ['label', 'value'] }"
                  :option="subjectList"
                  class="w-100 u-flex-1"
                  v-model="row.accountNumber"
                  @change="changeAccount($event, row)"
                />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="科目编码" prop="accountNumber"> </el-table-column>
          <el-table-column label="凭证摘要">
            <template v-slot="{ row }">
              <el-input v-model="row.voucherComments" placeholder="请输入凭证摘要"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="金额">
            <template v-slot="{ row, $index }">
              <el-form-item
                label-width="0px"
                :prop="`writeOffConfirmVoucherList.${$index}.amount`"
                :rules="{ required: true, message: '请输入金额', trigger: ['blur', 'change'] }"
              >
                <el-input-number
                  controls-position="right"
                  v-model="row.amount"
                  placeholder="请输入金额"
                />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="客户">
            <template v-slot="{ row, $index }">
              <el-form-item
                label-width="0px"
                :prop="`writeOffConfirmVoucherList.${$index}.clientCode`"
                :rules="{
                  required: row.assistFlagList.includes('0'),
                  message: '请选择客户',
                  trigger: ['blur', 'change']
                }"
              >
                <USelectPagination
                  :attrs="{ filterable: true }"
                  searchName="clientCodeOrName"
                  placeholder="请选择"
                  class="w-100 u-flex-1"
                  title="客户"
                  :request="{ url: '/engine/finance/client/page', method: 'post' }"
                  :keyValue="{ label: 'clientName', value: 'clientCode' }"
                  v-model="row.clientCode"
                />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="合同">
            <template v-slot="{ row, $index }">
              <el-form-item
                label-width="0px"
                :prop="`writeOffConfirmVoucherList.${$index}.contractCode`"
                :rules="{
                  required: row.assistFlagList.includes('1'),
                  message: '请输入合同编号',
                  trigger: ['blur', 'change']
                }"
              >
                <USelectPagination
                  :attrs="{ filterable: true }"
                  placeholder="请选择"
                  class="w-100 u-flex-1"
                  title="合同编号"
                  :request="{
                    url: '/engine/finance/kingdee/option/queryGeneralAsst',
                    method: 'post',
                    params: { asstType: '合同号' }
                  }"
                  :keyValue="{ label: 'code', value: 'code' }"
                  v-model="row.contractCode"
                />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="银行账号">
            <template v-slot="{ row, $index }">
              <el-form-item
                label=""
                label-width="0px"
                :prop="`writeOffConfirmVoucherList.${$index}.bankNo`"
              >
                <USelectPagination
                  :attrs="{ filterable: true }"
                  placeholder="请选择"
                  class="w-100 u-flex-1"
                  title="银行账号"
                  :request="{ url: '/engine/finance/bank-account/page', method: 'post' }"
                  :keyValue="{ label: 'bankAccountNumber', value: 'bankAccountNumber' }"
                  v-model="row.bankNo"
                />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="借款合同编号">
            <template v-slot="{ row, $index }">
              <el-form-item
                label=""
                label-width="0px"
                :prop="`writeOffConfirmVoucherList.${$index}.loansContractCode`"
              >
                <USelectPagination
                  :attrs="{ filterable: true }"
                  placeholder="请选择"
                  class="w-100 u-flex-1"
                  title="借款合同编号"
                  :request="{
                    url: '/engine/finance/kingdee/option/queryGeneralAsst',
                    method: 'post',
                    params: { asstType: '借款合同编号' }
                  }"
                  :keyValue="{ label: 'name', value: 'code', formatLabel: ['code', 'name'] }"
                  v-model="row.loansContractCode"
                />
              </el-form-item>
            </template>
          </el-table-column>

          <el-table-column label="操作" width="70px">
            <template v-slot="{ $index }">
              <el-button
                type="danger"
                :icon="Delete"
                circle
                size="small"
                @click="deleteItems($index)"
              ></el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="u-m-t-10">
          <el-button type="primary" :icon="Plus" size="small" @click="addItems(sureFormDataRef)"
            >新增一行</el-button
          >
        </div>
      </el-col>
    </el-row>
  </el-form>
</template>
<script setup>
// 认领
import { nextTick, ref, watch } from 'vue'
import { validateForm } from '@/utils/index'
import { ElMessage } from 'element-plus'
import { getTableSummaries, confirmEl, dictMappingToArray, toThousands } from '@/utils'
import { queryContractInfo } from '@/api/common'
import { useStore } from 'vuex'
import { Delete, Plus } from '@element-plus/icons-vue'
const props = defineProps({
  detailData: {
    type: Object,
    default: () => ({})
  },
  companyList: {
    type: Array,
    default: () => []
  },
  subjectList: {
    type: Array,
    default: () => []
  },
  assistClientList: {
    type: Array,
    default: () => []
  },
  debitCreditList: {
    type: Array,
    default: () => []
  },
  statusVerifiedList: {
    type: Array,
    default: () => []
  }
})
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const dictList = ref({
  is_sys_bool: dictMappingToArray(dictData, 'is_sys_bool')
})
const total = ref({
  totalMoney: 0,
  planMoney: 0
})
const sureFormDataRef = ref()
const globalContractInfo = ref({})
const sureFormData = ref({
  accountDate: '',
  writeOffConfirmVoucherList: [],
  writeOffDetailList: [],
  id: 0
})

const contractCodeFun = async (_accountNumbers) => {
  const data = await Promise.all(
    _accountNumbers.map(async ({ createConfirmOrgId, contractCode }) => {
      const { data } = await queryContractInfo({
        contractCode,
        orgId: createConfirmOrgId
      })
      return data
    })
  )
  const _info = data.find((item) => item !== null)
  globalContractInfo.value = _info || {}
}
const addItems = async () => {
  const valid = await validateForm(sureFormDataRef.value)
  if (!valid) return
  sureFormData.value.writeOffConfirmVoucherList.push(
    Object.assign(
      {},
      {
        accountName: '',
        accountNumber: '',
        amount: 0,
        clientCode: '',
        clientName: '',
        contractCode: '',
        crOrDt: '',
        createConfirmOrgId: '',
        createConfirmOrgName: '',
        voucherComments: '',
        assistFlagList: []
      }
    )
  )
}
// 删除行
const deleteItems = (index) => {
  sureFormData.value.writeOffConfirmVoucherList.splice(index, 1)
}

const changeAccount = (item, row) => {
  nextTick(() => {
    setTimeout(() => {
      row.accountName = item.label
      row.assistFlagList = item?.assistFlags ?? []
      row.clientList = props.assistClientList.filter((it) =>
        [...(item?.assistFlags ?? [])].includes(it.value)
      )
    }, 200)
  })
}
// 判断认领金额是否大于或者小于剩余未确认金额的绝对值
const valideClaimAmount = (rule, value, callback) => {
  const { max } = rule
  if (value > max) {
    callback(new Error('冲销金额不能大于到账金额'))
  } else if (value < -max) {
    callback(new Error('冲销金额不能小于到账金额'))
  } else {
    callback()
  }
}
const isOverFactMoney = () => {
  const { totalMoney, planMoney } = total.value
  return Number(planMoney) > Number(totalMoney)
}

watch(
  () => props.detailData,
  (n, o) => {
    if (JSON.stringify(n) !== JSON.stringify(o)) {
      sureFormData.value = {
        ...sureFormData.value,
        ...n,
        writeOffConfirmVoucherList: []
      }
    }
  },
  { deep: true, immediate: true }
)

const getSummaries = (param) => {
  const sumColumnArray = getTableSummaries(['bankAmount', 'writeOffAmount'], param)
  saveTotalMoney(sumColumnArray, param)
  return sumColumnArray
}

const saveTotalMoney = (totalColumn) => {
  const [totalMoney, planMoney] = totalColumn.slice(2, 4)
  total.value = {
    totalMoney: totalMoney.children.replace(/,/g, ''),
    planMoney: planMoney.children.replace(/,/g, '')
  }
}
const validateDREquCR = () => {
  const exclude = ['2241.02', '1221.01']
  const include = ['2241.17', '1221.15']
  return new Promise((resolve, reject) => {
    const { writeOffDetailList, writeOffConfirmVoucherList } = sureFormData.value
    const _accountNumber = writeOffConfirmVoucherList.filter((item) => item.accountNumber)
    contractCodeFun(_accountNumber)

    const cr = writeOffConfirmVoucherList
      .filter((ii) => ii.crOrDt === 'CR')
      .reduce((prev, current) => {
        return current.amount + prev
      }, 0)
    const dt = writeOffConfirmVoucherList
      .filter((ii) => ii.crOrDt === 'DR')
      .reduce((prev, current) => {
        return current.amount + prev
      }, 0)
    // 查询合同状态是否是6类资产
    const notSubjectArr = writeOffConfirmVoucherList.filter((ii) => ii.accountNumber === '1531.02')

    if (cr !== dt) {
      ElMessage.error('借方金额与贷方金额不相等')
      reject(new Error('借方金额与贷方金额不相等'))
      return
    }
    if (notSubjectArr.length === 0) {
      ElMessage.error('做账主体中不存在【未确认收款】科目，请添加【未确认收款】科目，然后继续提交')
      reject(
        new Error('做账主体中不存在【未确认收款】科目，请添加【未确认收款】科目，然后继续提交')
      )
      return
    }
    // 判断校验若到账主体和认领主体不一致，
    const isSameorgId = writeOffDetailList.every(
      (item) => item.orgId === item.collectionAccountsBankCode
    )
    // 是否包含科目编码2241.09
    const accountNumber224109Arr = writeOffConfirmVoucherList.filter(
      (item) => item.accountNumber === '2241.09'
    )
    // 六类资产数组
    const status = props.statusVerifiedList.map((item) => item.value)
    // 判断是否是六类资产
    const { financialContractStatus } = globalContractInfo.value
    const isSix = status.includes(financialContractStatus)
    if (!isSameorgId) {
      // 是否包含
      const includeArray = writeOffConfirmVoucherList
        .filter((item) => include.includes(item.accountNumber))
        .map((item) => item.accountNumber)
      // 是否排除
      const excludeArray = writeOffConfirmVoucherList
        .filter((item) => exclude.includes(item.accountNumber))
        .map((item) => item.accountNumber)
      // 去重
      const newIncludeArray = [...new Set(includeArray)].filter((item) => include.includes(item))
      const newExcludeArray = [...new Set(excludeArray)].filter((item) => exclude.includes(item))
      let isTrue = true
      // 5）校验若到账主体和认领主体不一致，当合同财务状态不为“资产处置结束（内部转让）”时，是否已录入关联往来科目信息（2241.02 其他应付款_关联公司往来+1221.01 其他应收款款_关联公司往来），若未录入，则校验不通过，无法提交
      if (financialContractStatus === '资产处置结束（内部转让）' && newIncludeArray.length < 2) {
        ElMessage.warning(
          '您没有录入科目编号【2241.17 其他应付款_代收转让款项】和【1221.15 其他应收款款_应收转让后收款】'
        )
        isTrue = false
      } else if (
        financialContractStatus !== '资产处置结束（内部转让）' &&
        newExcludeArray.length < 2
      ) {
        // 5）校验若到账主体和认领主体不一致，当合同财务状态不为“资产处置结束（内部转让）”时，是否已录入关联往来科目信息（2241.02 其他应付款_关联公司往来+1221.01 其他应收款款_关联公司往来），若未录入，则校验不通过，无法提交
        ElMessage.warning(
          '您没有录入科目编号【2241.02 其他应付款_关联公司往来】和【1221.01 其他应收款款_关联公司往来】'
        )
        isTrue = false
      }
      return isTrue
    }
    if (isSix && accountNumber224109Arr.length === 0) {
      confirmEl(
        '财务合同状态为6类资产转让状态时,未录入科目编码【2241.09】代收款项，是否继续提交'
      ).then(() => {
        if (isOverFactMoney()) {
          confirmEl('认领金额大于剩余未确认金额,网银余额不足，是否继续提交？').then(() => {
            resolve(true)
          })
        } else {
          resolve(true)
        }
      })
      return
    }
    if (isOverFactMoney()) {
      confirmEl('认领金额大于剩余未确认金额,网银余额不足，是否继续提交？').then(() => {
        resolve(true)
      })
    } else {
      resolve(true)
    }

    // resolve(true)
  })
}

const exposeFormData = async () => {
  const valid = await validateForm(sureFormDataRef.value)
  if (!valid) return
  const isEqu = await validateDREquCR()
  if (!isEqu) return
  return sureFormData.value
}

defineExpose({
  exposeFormData
})
</script>

<style lang="scss" scoped></style>
