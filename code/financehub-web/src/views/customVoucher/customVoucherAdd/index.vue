<template>
  <div class="custom-content" v-loading="loading">
    <el-card shadow="never" :body-style="{ padding: '12px' }">
      <el-form ref="ruleFormRef" :model="rulesForm" label-width="150px" label-position="top">
        <el-row :gutter="15">
          <el-col :span="8">
            <el-form-item
              label="签约主体"
              prop="orgId"
              :rules="{ required: true, message: '请选择签约主体', trigger: ['blur', 'change'] }"
            >
              <USelect
                :option="dictKeyLists.company"
                class="w-100 u-flex-1"
                :attrs="{ filterable: true }"
                v-model="rulesForm.orgId"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="记账日期"
              prop="voucherDate"
              :rules="{ required: true, message: '请选择记账日期', trigger: ['blur', 'change'] }"
            >
              <el-date-picker
                value-format="YYYY-MM-DD"
                format="YYYY-MM-DD"
                style="width: 100%"
                placeholder="请选择记账日期"
                v-model="rulesForm.voucherDate"
                class="w-100"
                type="date"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="业务日期"
              prop="businessDate"
              :rules="{ required: true, message: '请选择业务日期', trigger: ['blur', 'change'] }"
            >
              <el-date-picker
                value-format="YYYY-MM-DD"
                format="YYYY-MM-DD"
                style="width: 100%"
                placeholder="请选择业务日期"
                v-model="rulesForm.businessDate"
                class="w-100"
                type="date"
                clearable
              />
            </el-form-item>
          </el-col>
          <!-- <el-col :span="8">
            <el-form-item label="会计期间" prop="periodCode"
              :rules="{ required: true, message: '请选择会计期间', trigger: ['blur', 'change'] }">
              <USelect :attrs="{ filterable: true }" :option="dictKeyLists.periodList" class="w-100 u-flex-1"
                v-model="rulesForm.periodCode" />

            </el-form-item>
          </el-col> -->
          <el-col :span="8">
            <el-form-item
              label="币种"
              prop="currencyCode"
              :rules="{ required: true, message: '请选择币种', trigger: ['blur', 'change'] }"
            >
              <USelect
                :option="dictKeyLists.sys_currency_type"
                class="w-100 u-flex-1"
                :attrs="{ filterable: true }"
                v-model="rulesForm.currencyCode"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="凭证类型"
              prop="voucherType"
              :rules="{ required: true, message: '请选择凭证类型', trigger: ['blur', 'change'] }"
            >
              <USelect
                :option="dictKeyLists.voucherType"
                class="w-100 u-flex-1"
                :attrs="{ filterable: true }"
                v-model="rulesForm.voucherType"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="业务编码"
              prop="businessCode"
              :rules="{ required: true, message: '请选择记业务编码', trigger: ['blur', 'change'] }"
            >
              <USelect
                :option="dictKeyLists.sys_business_code"
                class="w-100 u-flex-1"
                :attrs="{ filterable: true }"
                v-model="rulesForm.businessCode"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="业务场景"
              prop="sceneCode"
              :rules="{ required: true, message: '请选择业务场景', trigger: ['blur', 'change'] }"
            >
              <USelect
                :attrs="{ filterable: true }"
                :option="dictKeyLists.sceneList"
                class="w-100 u-flex-1"
                v-model="rulesForm.sceneCode"
                @change="changeSubjectCode($event, rulesForm, 'sceneName')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="细分场景"
              prop="subSceneType"
              :rules="{ required: true, message: '请选择细分场景', trigger: ['blur', 'change'] }"
            >
              <USelect
                :attrs="{ filterable: true }"
                :option="dictKeyLists.sys_sub_scene_type"
                class="w-100 u-flex-1"
                v-model="rulesForm.subSceneType"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="汇率"
              prop="rate"
              :rules="{ required: true, message: '请输入汇率', trigger: ['blur', 'change'] }"
            >
              <el-input-number
                v-model="rulesForm.rate"
                placeholder="请输入汇率"
                controls-position="right"
                class="w-100 u-flex-1"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              label="摘要内容"
              prop="voucherSummary"
              :rules="{ required: true, message: '请输入摘要内容', trigger: ['blur', 'change'] }"
            >
              <el-input
                type="textarea"
                placeholder="请输入摘要内容"
                @change="changeSummaryToAccount"
                v-model="rulesForm.voucherSummary"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-card
              v-for="(item, index) in rulesForm.manualVoucherDTOList"
              :key="index"
              class="u-m-t-10"
            >
              <template #header>
                <div class="u-flex u-row-between">
                  <UHeader :title="`分录-${index + 1}`" />
                  <el-space>
                    <el-button
                      circle
                      size="small"
                      type="warning"
                      :icon="CopyDocument"
                      @click="copyFromDataItem(ruleFormRef, item)"
                    ></el-button>
                    <el-button
                      circle
                      size="small"
                      v-if="index !== 0"
                      type="danger"
                      :icon="Delete"
                      @click="deleteFormDataItem(index)"
                    ></el-button>
                    <el-button
                      circle
                      size="small"
                      :icon="ArrowUp"
                      @click="moveUp(index, rulesForm.list, $event)"
                    ></el-button>
                    <el-button
                      circle
                      size="small"
                      :icon="ArrowDown"
                      @click="moveDown(index, rulesForm.list, $event)"
                    ></el-button>
                  </el-space>
                </div>
              </template>
              <el-row :gutter="15">
                <el-col :span="8">
                  <el-form-item
                    label="辅助账摘要"
                    :prop="`manualVoucherDTOList.${index}.subsidiaryAccount`"
                  >
                    <el-input v-model="item.subsidiaryAccount" placeholder="请输入辅助账摘要" />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item
                    label="是否涉及其他客户及辅助账"
                    :prop="`manualVoucherDTOList.${index}.isRelatedOtherCustomer`"
                    :rules="{
                      required: true,
                      message: '请选择科目名称',
                      trigger: ['blur', 'change']
                    }"
                  >
                    <USelect
                      :option="dictKeyLists.is_sys_bool"
                      class="w-100 u-flex-1"
                      v-model="item.isRelatedOtherCustomer"
                      :keyValue="{ label: 'label', value: 'value' }"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item
                    label="科目"
                    :prop="`manualVoucherDTOList.${index}.accountCode`"
                    :rules="{
                      required: true,
                      message: '请选择科目名称',
                      trigger: ['blur', 'change']
                    }"
                  >
                    <USelect
                      :attrs="{ filterable: true }"
                      :option="dictKeyLists.subjectList"
                      class="w-100 u-flex-1"
                      v-model="item.accountCode"
                      :keyValue="{
                        label: 'label',
                        value: 'value',
                        formatLabel: ['value', 'label']
                      }"
                      @change="changeSubjectCode($event, item, 'accountName')"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="8" v-show="false">
                  <el-form-item
                    label="科目代码"
                    :prop="`manualVoucherDTOList.${index}.accountName`"
                    :rules="{
                      required: true,
                      message: '请选择科目代码',
                      trigger: ['blur', 'change']
                    }"
                  >
                    <el-input v-model="item.accountCode" placeholder="科目代码" disabled />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="借方金额" prop="debitAmount">
                    <el-input-number
                      v-model="item.debitAmount"
                      @change="changeAmount($event, item, 'creditAmount')"
                      class="w-100 u-flex-1"
                      controls-position="right"
                      placeholder="请输入"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="贷方金额" prop="creditAmount">
                    <el-input-number
                      v-model="item.creditAmount"
                      @change="changeAmount($event, item, 'debitAmount')"
                      class="w-100 u-flex-1"
                      controls-position="right"
                      placeholder="请输入"
                    />
                  </el-form-item>
                </el-col>

                <!-- <el-col :span="8">
                  <el-form-item label="是否有现金流量" :prop="`manualVoucherDTOList.${index}.isCashFlow`">
                    <USelect :option="dictKeyLists.is_sys_bool" class="w-100 u-flex-1" :attrs="{ filterable: true }"
                      v-model="item.isCashFlow" />
                  </el-form-item>
                </el-col> -->
                <el-col :span="8">
                  <el-form-item label="合同编号" prop="contractCode">
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
                      :keyValue="{ label: 'name', value: 'code', formatLabel: ['code', 'name'] }"
                      v-model="item.contractCode"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="客户编号" prop="clientCode">
                    <USelectPagination
                      :attrs="{ filterable: true }"
                      searchName="clientCodeOrName"
                      placeholder="请选择"
                      class="w-100 u-flex-1"
                      title="客户编号"
                      :request="{ url: '/engine/finance/client/page', method: 'post' }"
                      :keyValue="{
                        label: 'clientName',
                        value: 'clientCode',
                        formatLabel: ['clientCode', 'clientName']
                      }"
                      v-model="item.clientCode"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="借款合同编号" prop="loansContractCodeName">
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
                      v-model="item.loansContractCode"
                    />
                  </el-form-item>
                </el-col>
                <!-- <el-col :span="8">
                  <el-form-item label="成本中心" prop="costCentre">
                    <USelectPagination :attrs="{ filterable: true }" placeholder="请选择" class="w-100 u-flex-1"
                      title="成本中心" :request="{ url: '/engine/finance/kingdee/option/queryCostcenter', method: 'post' }"
                      :keyValue="{ label: 'name', value: 'code' }" v-model="item.costCentre" />

                  </el-form-item>
                </el-col> -->
                <!-- <el-col :span="8">
                  <el-form-item label="费用类型" prop="expenseTypeName">
                    <USelectPagination :attrs="{ filterable: true }" placeholder="请选择" class="w-100 u-flex-1"
                      title="费用类型"
                      :request="{ url: '/engine/finance/kingdee/option/queryGeneralAsst', method: 'post', params: { asstType: '费用类型' } }"
                      :keyValue="{ label: 'name', value: 'code' }" v-model="item.expenseType" />
                  </el-form-item>
                </el-col> -->
                <!-- <el-col :span="8">
                  <el-form-item label="金融机构" prop="financialInstitutionName">
                    <USelectPagination :attrs="{ filterable: true }" placeholder="最少需输入3个字符" class="w-100 u-flex-1"
                      title="金融机构" :request="{ url: '/engine/finance/kingdee/option/queryBank', method: 'post' }"
                      :keyValue="{ label: 'name', value: 'code' }" v-model="item.financialInstitution" />
                  </el-form-item>
                </el-col> -->
                <!-- <el-col :span="8">
                  <el-form-item label="批次号" prop="batchNum">
                    <USelectPagination :attrs="{ filterable: true }" placeholder="请选择" class="w-100 u-flex-1"
                      title="批次号"
                      :request="{ url: '/engine/finance/kingdee/option/queryGeneralAsst', method: 'post', params: { asstType: '批次号' } }"
                      :keyValue="{ label: 'name', value: 'code' }" v-model="item.batchNum" />

                  </el-form-item>
                </el-col> -->
                <el-col :span="8">
                  <el-form-item label="银行账号" prop="bankNo">
                    <USelectPagination
                      :attrs="{ filterable: true }"
                      placeholder="请选择"
                      class="w-100 u-flex-1"
                      title="银行账号"
                      :request="{ url: '/engine/finance/bank-account/page', method: 'post' }"
                      :keyValue="{ label: 'bankAccountNumber', value: 'bankAccountNumber' }"
                      v-model="item.bankNo"
                    />
                  </el-form-item>
                </el-col>
                <!-- <el-col :span="8">
                  <el-form-item label="合同号" prop="materialContractCode">
                    <USelectPagination :attrs="{ filterable: true }" placeholder="请选择" class="w-100 u-flex-1"
                      title="合同号"
                      :request="{ url: '/engine/finance/kingdee/option/queryGeneralAsst', method: 'post', params: { asstType: '合同编号' } }"
                      :keyValue="{ label: 'name', value: 'code' }" v-model="item.materialContractCode" />

                  </el-form-item>
                </el-col> -->
                <!-- <el-col :span="8">
                  <el-form-item label="职员" prop="employeeCode">
                    <USelectPagination :attrs="{ filterable: true }" placeholder="请选择" class="w-100 u-flex-1" title="职员"
                      :request="{ url: '/engine/finance/kingdee/option/queryPerson', method: 'post' }"
                      :keyValue="{ label: 'name', value: 'code' }" v-model="item.employeeCode" />
                  </el-form-item>
                </el-col> -->
                <!-- <el-col :span="8">
                  <el-form-item label="借据号" prop="receiptNum">

                    <USelectPagination :attrs="{ filterable: true }" placeholder="请选择" class="w-100 u-flex-1"
                      title="借据号"
                      :request="{ url: '/engine/finance/kingdee/option/queryGeneralAsst', method: 'post', params: { asstType: '借据号' } }"
                      :keyValue="{ label: 'name', value: 'code' }" v-model="item.receiptNum" />

                  </el-form-item>
                </el-col> -->
                <!-- <el-col :span="8">
                  <el-form-item label="衍生合约编号" prop="derivativeContractCode">
                    <USelectPagination :attrs="{ filterable: true }" placeholder="请选择" class="w-100 u-flex-1"
                      title="衍生合约编号"
                      :request="{ url: '/engine/finance/kingdee/option/queryGeneralAsst', method: 'post', params: { asstType: '衍生合约编号' } }"
                      :keyValue="{ label: 'name', value: 'code' }" v-model="item.derivativeContractCode" />
                  </el-form-item>
                </el-col> -->
                <!-- <el-col :span="8">
                  <el-form-item label="开发项目" prop="projectType">
                    <USelectPagination :attrs="{ filterable: true }" placeholder="请选择" class="w-100 u-flex-1"
                      title="开发项目"
                      :request="{ url: '/engine/finance/kingdee/option/queryGeneralAsst', method: 'post', params: { asstType: '项目类型' } }"
                      :keyValue="{ label: 'name', value: 'code' }" v-model="item.projectType" />

                  </el-form-item>
                </el-col> -->
              </el-row>
            </el-card>
            <div class="u-text-center u-m-t-10">
              <el-button type="primary" @click="addItems(ruleFormRef)">新增一条</el-button>
            </div>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
    <div class="footer-submit u-text-right">
      <el-button class="w-88" @click="cancelPage">取消</el-button>
      <el-button
        class="w-88"
        type="primary"
        v-permission="permission.save"
        v-loading="loading"
        @click="extenalDataCheck(ruleFormRef)"
        >保存</el-button
      >
    </div>
  </div>
</template>
<script setup>
import { ref, nextTick, onMounted, computed, onActivated } from 'vue'
import { useStore } from 'vuex'
import { defaultFormItem } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { dictMappingToArray, confirmEl, validateForm } from '@/utils'
import { CopyDocument, Delete, ArrowUp, ArrowDown } from '@element-plus/icons-vue'
import {
  manualSave,
  manualDetails,
  manualUpdate,
  manualExtenalDataCheck
} from '@/api/customVoucher'
import { getCommonTableList } from '@/api/common'
import { ElMessage } from 'element-plus'
import { useModifyRouteTitle } from '@/hooks'

const { setCurrentTabbarTitle } = useModifyRouteTitle()
const route = useRoute()
const { query } = route.value

const loading = ref(false)
const { router } = useRouter()
const ruleFormRef = ref()
const store = useStore()
const rulesForm = ref({
  manualVoucherDTOList: [Object.assign({}, defaultFormItem)]
})
const dictData = store.getters['useDictMapping/dictMapping']
const dictKeyLists = ref({
  company: dictMappingToArray(dictData, 'company'),
  voucherType: dictMappingToArray(dictData, 'sys_voucher_type'),
  sys_currency_type: dictMappingToArray(dictData, 'sys_currency_type'),
  is_sys_bool: dictMappingToArray(dictData, 'is_sys_bool'),
  charge_type: dictMappingToArray(dictData, 'charge_type'),
  sys_business_code: dictMappingToArray(dictData, 'sys_business_code'),
  sys_sub_scene_type: dictMappingToArray(dictData, 'sys_sub_scene_type') || [],
  sceneList: [],
  periodList: [],
  subjectList: []
})

// 获取所有的自定义下拉接口
const querySelectOptionLists = async () => {
  // 业务场景 会计期间 科目编码
  const apiURL = [
    { url: '/engine/scene/list', keyValue: { label: 'sceneName', value: 'sceneCode' } },
    // { url: '/engine/scene/account-period/queryAll', keyValue: { label: 'periodName', value: 'periodCode' } },
    {
      url: '/engine/scene/account/listAll',
      keyValue: { label: 'accountName', value: 'accountCode' }
    }
  ]
  const [sceneList, subjectList] = await Promise.all(
    apiURL.map(async ({ url, keyValue }) => {
      const { data } = await getCommonTableList({ url, method: 'post' })
      return data.map((item) => {
        return {
          label: item[keyValue.label],
          value: item[keyValue.value]
        }
      })
    })
  )
  dictKeyLists.value = {
    ...dictKeyLists.value,
    sceneList,
    // periodList,
    subjectList
  }
}

const changeSubjectCode = (event, item, key) => {
  item[key] = event.label
}

// 切换金额
const changeAmount = (e, item, key) => {
  item[key] = undefined
}

// 新增一行
const addItems = async (formEl) => {
  const valid = await validateForm(formEl)
  if (!valid) return
  rulesForm.value.manualVoucherDTOList.push(
    Object.assign(
      {},
      { ...defaultFormItem, subsidiaryAccount: rulesForm.value?.voucherSummary ?? '' }
    )
  )
  nextTick(() => {
    scrollToElementBottom()
  })
}

// 复制
const copyFromDataItem = async (formEl, item) => {
  const valid = await validateForm(formEl)
  if (!valid) return
  confirmEl('您确定要复制该条数据吗？').then(() => {
    rulesForm.value.manualVoucherDTOList.push(Object.assign({}, item))
    nextTick(() => {
      scrollToElementBottom()
    })
  })
}
// 删除选择
const deleteFormDataItem = (index) => {
  confirmEl('删除该条数据不能恢复，您确定要删除吗？').then(() => {
    rulesForm.value.manualVoucherDTOList.splice(index, 1)
  })
}
// 滚动到底部
const scrollToElementBottom = () => {
  const el = document.querySelector('.el-scrollbar__wrap')
  if (el) {
    el.scrollTo({
      // 滚动到元素位置
      top: 50000, // 推荐使用，getBoundingClientRect 相对于当前视口的位置
      behavior: 'smooth' // 平滑滚动
    })
  }
}
// 上移
const moveUp = (index, arr, e) => {
  e.stopPropagation()
  if (index === 0) {
    ElMessage.error('已是第一条')
    return
  }
  arr.splice(index - 1, 1, ...arr.splice(index, 1, arr[index - 1]))
}
// 下移
const moveDown = (index, arr, e) => {
  e.stopPropagation()
  if (index === arr.length - 1) {
    ElMessage.error('已是最后一条')
    return
  }
  arr.splice(index, 1, ...arr.splice(index + 1, 1, arr[index]))
}
// 数据提交
const saveSceneRuleSubmit = async () => {
  loading.value = true
  const { manualId } = query
  const methods = manualId ? manualUpdate : manualSave
  const { code, msg, data } = await methods(rulesForm.value).catch((res) => (loading.value = false))
  if (code === 200) {
    loading.value = false
    if (data) {
      ElMessage.success('保存成功')
      cancelPage()
    }
  } else {
    loading.value = false
    ElMessage.error(msg)
  }
}

// 取消
const cancelPage = () => {
  router.push('/customVoucher/index')
}
// 获取详情
const getManualDetail = async () => {
  const { manualId, businessDate, orgId, externalId, sourceFrom, clientCode, contractCode } = query

  if (!manualId) {
    rulesForm.value = {
      ...rulesForm.value,
      businessDate,
      orgId,
      externalId,
      sourceFrom,
      manualVoucherDTOList: [{ clientCode, contractCode }]
    }
    return
  }
  loading.value = true
  const { data } = await manualDetails(manualId).catch(() => {})
  rulesForm.value = Object.assign({}, rulesForm.value, data)
  loading.value = false
}
// 更改摘要同步到列表里面
const changeSummaryToAccount = (val) => {
  rulesForm.value.manualVoucherDTOList.forEach((item) => {
    item.subsidiaryAccount = val
  })
}

const extenalDataCheck = async (formEl) => {
  const valid = await validateForm(formEl)
  if (!valid) return
  const { data, code, msg } = await manualExtenalDataCheck(rulesForm.value)
  if (code !== 200) return ElMessage.error(msg)
  if (data !== '') {
    confirmEl(data).then(() => {
      saveSceneRuleSubmit()
    })
    return
  }

  saveSceneRuleSubmit()
}
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.customVoucherAdd
})
onMounted(() => {
  querySelectOptionLists()
  getManualDetail()
})

onActivated(() => {
  const { manualId } = query
  const title = manualId ? '凭证录入-编辑' : '凭证录入-新增'
  setCurrentTabbarTitle(title)
})
// onActivated(()=>{
//   console.log('======11')
//   getManualDetail()
// })
</script>

<style scoped lang="scss">
.custom-content {
  :deep(.el-card__header) {
    padding: 10px;
  }
  .footer-submit {
    position: fixed;
    bottom: 0;
    left: 0;
    z-index: 12;
    width: calc(100% - 24px);
    padding: 12px;
    background: #fff;
    box-shadow: 0 0 5px #666;
    .w-88 {
      width: 88px;
    }
  }
}
</style>
