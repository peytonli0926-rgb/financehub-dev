<template>
  <div>
    <!-- @selection-change="selectionChange" -->
    <UTable ref="utableRef" :options="options" @search-form-data="searchFormData">
      <template #operationBtn="{ row }">
        <!-- <el-button type="primary" link size="small" @click="fromToRulePage(row, 'view')">查看凭证</el-button> -->
        <el-button
          type="primary"
          link
          size="small"
          v-permission="permission.view"
          @click="fromToRulePage(row, 'detail')"
          >查看详情</el-button
        >
        <el-button
          type="primary"
          link
          size="small"
          v-permission="permission.claim"
          @click="fromToRulePage(row, 'claim', '认领')"
          >认领</el-button
        >
        <el-button
          type="primary"
          link
          size="small"
          v-permission="permission.destroy"
          @click="fromToRulePage(row, 'destroy', '冲销')"
          >冲销</el-button
        >
        <el-button
          type="primary"
          link
          size="small"
          v-permission="permission.custom"
          @click="fromToRulePage(row, 'changeNumber', '修改网银编号')"
          >修改网银编号</el-button
        >
        <el-button
          type="primary"
          @click="fromToRulePage(row, 'custom', '手工处理')"
          link
          size="small"
          >手工处理</el-button
        >
      </template>
    </UTable>

    <UDialog
      :visible="sureFormVisible"
      @handle-submit="sureSubmit"
      dialogWidth="95%"
      :title="dialogConfig.title"
      @update:visible="(val) => (sureFormVisible = val)"
    >
      <component
        ref="formRef"
        :is="componentId"
        :detail-data="detailData"
        :companyList="dictKeyLists.company"
        :subjectList="dictKeyLists.subjectList"
        :statusVerifiedList="dictKeyLists.statusVerifiedList"
        :assistClientList="dictKeyLists.sys_assist_client"
        :debitCreditList="dictKeyLists.sys_debit_credit"
      ></component>
    </UDialog>
  </div>
</template>
<script setup>
import { ref, onMounted, shallowRef } from 'vue'
import { useStore } from 'vuex'
import { optionsConfigSumTable } from './config'
import { useRouter } from '@toystory/lotso'
import {
  nonConfirmCollectionSumQuery,
  nonConfirmCollectionSumConfirm,
  nonConfirmCollectionSumModifyEbankNoQuery,
  nonConfirmCollectionSumModifyEbankNoConfirm,
  nonConfirmCollectionSumWriteOffConfirm
} from '@/api/rentBusiness/nonConfirmCollectionSum'
import { ElMessage } from 'element-plus'
import { dictMappingToArray, confirmEl } from '@/utils'
import { getCommonTableList } from '@/api/common'

import DestroyForm from './dialogForm/destroyForm.vue'
import ClaimForm from './dialogForm/claimForm.vue'
import ChangeBankNumber from './dialogForm/changeBankNumber.vue'

defineProps({
  permission: {
    type: Object,
    default: () => ({})
  }
})

const dialogConfig = ref({})
const formRef = ref()
const componentId = shallowRef()
const sureFormVisible = ref(false)
const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfigSumTable(router, dictData)
const formValue = ref({})

const detailData = ref()
const dictKeyLists = ref({
  company: dictMappingToArray(dictData, 'company'),
  sys_assist_client: dictMappingToArray(dictData, 'sys_assist_client'),
  sys_debit_credit: dictMappingToArray(dictData, 'sys_debit_credit'),
  statusVerifiedList: dictMappingToArray(dictData, 'status_verified_unconfirmed'),
  subjectList: []
})

// 获取所有的自定义下拉接口
const querySelectOptionLists = async () => {
  // 业务场景 会计期间 科目编码
  const apiURL = [
    {
      url: '/engine/scene/account/listAll',
      keyValue: { label: 'accountName', value: 'accountCode' }
    }
  ]
  const [subjectList] = await Promise.all(
    apiURL.map(async ({ url, keyValue }) => {
      const { data } = await getCommonTableList({ url, method: 'post' })
      return data.map((item) => {
        return {
          label: item[keyValue.label],
          value: item[keyValue.value],
          assistFlags: item.assistFlags
        }
      })
    })
  )
  dictKeyLists.value = {
    ...dictKeyLists.value,
    subjectList
  }
}

const searchFormData = (formData) => {
  formValue.value = formData
}
const fromToRulePage = async ({ id }, type, title = '') => {
  const url = `/rentBusiness/nonConfirmCollectionSumDetail?sumId=${id}`
  dialogConfig.value = {
    type,
    title
  }
  // detailData.value={}
  if (type === 'view') {
    // setVoucherPage({ batchType, batchId: id, periodCode })
  } else if (type === 'claim') {
    const { claimQueryResultDetailDTO, ...result } = await queryNonConfirmCollectionSum(id)
    componentId.value = ClaimForm
    detailData.value = {
      claimQueryResultDetailList: claimQueryResultDetailDTO,
      ...result
    }
  } else if (type === 'destroy') {
    const { claimQueryResultDetailDTO, ...result } = await queryNonConfirmCollectionSum(id)
    componentId.value = DestroyForm

    const writeOffDetailList = claimQueryResultDetailDTO.map((item) => {
      const { claimAmount, ...row } = item
      return {
        ...row,
        writeOffAmount: claimAmount
      }
    })
    detailData.value = {
      writeOffDetailList,
      ...result
    }
  } else if (type === 'changeNumber') {
    await queryNonConfirmCollectionSumModifyEbankNo(id)
    componentId.value = ChangeBankNumber
  } else if (type === 'custom') {
    router.push(`/rentBusiness/nonConfirmCustom?sumId=${id}`)
  } else {
    router.push(url)
  }
}
// 冲销 认领
const queryNonConfirmCollectionSum = async (id) => {
  const { data, code, msg } = await nonConfirmCollectionSumQuery({ id })
  if (code === 200) {
    sureFormVisible.value = true
    return data
  }
  ElMessage.error(msg)
  return {}
}

// 网银编号确认
const queryNonConfirmCollectionSumModifyEbankNo = async (sumId) => {
  const { data, code, msg } = await nonConfirmCollectionSumModifyEbankNoQuery({ sumId })
  if (code === 200) {
    const { claimRecordNewList } = data
    const _claimRecordNewList = claimRecordNewList.map((item) => ({ ...item, contractCode: '' }))
    data.claimRecordNewList = _claimRecordNewList
    detailData.value = data || {}
    sureFormVisible.value = true
    return
  }
  ElMessage.error(msg)
}

// // 提交
const sureSubmit = async () => {
  const data = await formRef.value.exposeFormData()
  if (!data) return
  confirmEl('您确定提交数据吗?').then(async () => {
    let result = {}
    switch (dialogConfig.value.type) {
      case 'claim':
        result = await nonConfirmCollectionSumConfirm(data)
        break
      case 'destroy':
        result = await nonConfirmCollectionSumWriteOffConfirm(data)
        break
      case 'changeNumber':
        result = await nonConfirmCollectionSumModifyEbankNoConfirm(data)
        break
      default:
        break
    }
    if (Object.keys(result).length > 0) {
      result.code === 200 ? ElMessage.success('操作成功') : ElMessage.error(result.msg)
      sureFormVisible.value = false
      refreshList()
    }
  })
}

// 刷新列表
const refreshList = () => {
  utableRef.value.requestBefore()
}
onMounted(() => {
  querySelectOptionLists()
})
</script>
