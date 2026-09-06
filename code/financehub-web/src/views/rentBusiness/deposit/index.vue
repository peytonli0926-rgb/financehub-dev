<template>
  <div>
    <UTable
      ref="utableRef"
      :defaultParams="defaultParams"
      :options="options"
      @selection-change="selectionChange"
    >
      <template #operationBtn="{ row }">
        <el-button
          type="primary"
          v-permission="permission.viewVoucher"
          link
          size="small"
          @click="fromToRulePage(row, 'view')"
          :disabled="row.isGenerateVoucher === '0'"
          >查看凭证</el-button
        >
        <el-button
          type="primary"
          v-permission="permission.view"
          link
          size="small"
          @click="fromToRulePage(row, 'detail')"
          >查看详情</el-button
        >
      </template>
      <template #operateHeaderRight>
        <el-space>
          <el-button type="primary" v-permission="permission.input" @click="queryDialogList"
            >录入</el-button
          >
          <UButtonOperate
            :config="btnConfig"
            @update:loading="updateLoading"
            :permission="permission"
            :selected-data="selectedData"
            :form-search="formSearch"
            @refresh="refreshList"
          />
        </el-space>
      </template>
    </UTable>
    <UDialog
      :permission="permission"
      :visible="isBoolReactive.addTypeVisible"
      @update:visible="(vi) => (isBoolReactive.addTypeVisible = vi)"
      dialogWidth="98%"
      title="录入保证金信息"
      @handle-submit="handleSubmit(dialogFormRef)"
    >
      <el-form ref="dialogFormRef" :model="dialogFormData">
        <el-table height="400px" highlight-current-row border :data="dialogFormData.dataSource">
          <el-table-column label="序号" width="55" type="index" />
          <el-table-column label="签约主体">
            <template v-slot="{ row, $index }">
              <el-form-item
                :prop="`dataSource.${$index}.orgId`"
                :rules="{
                  required: true,
                  message: '请选择金额',
                  trigger: ['blur', 'change']
                }"
              >
                <USelect
                  v-model="row.orgId"
                  :option="dictKeyLists.company"
                  :attrs="{ filterable: true }"
                  placeholder="请选择"
                />
              </el-form-item>
            </template>
          </el-table-column>

          <el-table-column label="合同编号">
            <template v-slot="{ row, $index }">
              <el-form-item
                :prop="`dataSource.${$index}.contractCode`"
                :rules="{
                  required: true,
                  message: '请输入',
                  trigger: ['blur', 'change']
                }"
              >
                <el-input v-model="row.contractCode" placeholder="请输入"></el-input>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="合同名称">
            <template v-slot="{ row, $index }">
              <el-form-item
                :prop="`dataSource.${$index}.contractName`"
                :rules="{
                  required: true,
                  message: '请输入',
                  trigger: ['blur', 'change']
                }"
              >
                <el-input v-model="row.contractName" placeholder="请输入"></el-input>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="科目代码">
            <template v-slot="{ row, $index }">
              <el-form-item
                :prop="`dataSource.${$index}.accountCode`"
                :rules="{
                  required: true,
                  message: '请输入',
                  trigger: ['blur', 'change']
                }"
              >
                <el-input v-model="row.accountCode" placeholder="请输入"></el-input>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="科目名称">
            <template v-slot="{ row, $index }">
              <el-form-item
                :prop="`dataSource.${$index}.accountName`"
                :rules="{
                  required: true,
                  message: '请输入',
                  trigger: ['blur', 'change']
                }"
              >
                <el-input v-model="row.accountName" placeholder="请输入"></el-input>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="币种">
            <template v-slot="{ row, $index }">
              <el-form-item
                :prop="`dataSource.${$index}.currencyType`"
                :rules="{
                  required: true,
                  message: '请输入',
                  trigger: ['blur', 'change']
                }"
              >
                <USelect
                  v-model="row.currencyType"
                  :option="dictKeyLists.currencyType"
                  placeholder="请选择"
                />
              </el-form-item>
            </template>
          </el-table-column>

          <!-- <el-table-column label="日期">
            <template v-slot="{ row, $index }">
              <el-form-item :prop="`dataSource.${$index}.entryDate`" :rules="{
                required: true,
                message: '请选择日期',
                trigger: ['blur', 'change'],
              }">
                <el-date-picker v-model="row.entryDate" value-format="YYYY-MM-DD" format="YYYY-MM-DD"
                  placeholder="请选择日期"></el-date-picker>
              </el-form-item>
            </template>
          </el-table-column> -->

          <el-table-column label="保证金余额">
            <template v-slot="{ row, $index }">
              <el-form-item
                :prop="`dataSource.${$index}.contractBalance`"
                :rules="{
                  required: true,
                  message: '请输入',
                  trigger: ['blur', 'change']
                }"
              >
                <el-input-number
                  v-model="row.contractBalance"
                  controls-position="right"
                  placeholder="请输入"
                ></el-input-number>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="会计起租日">
            <template v-slot="{ row, $index }">
              <el-form-item
                :prop="`dataSource.${$index}.leaseDateStart`"
                :rules="{
                  required: true,
                  message: '请选择',
                  trigger: ['blur', 'change']
                }"
              >
                <el-date-picker
                  v-model="row.leaseDateStart"
                  format="YYYY-MM-DD"
                  value-format="YYYY-MM-DD"
                  placeholder="请选择"
                ></el-date-picker>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="约定到期日">
            <template v-slot="{ row, $index }">
              <el-form-item
                :prop="`dataSource.${$index}.leaseDateEnd`"
                :rules="{
                  required: true,
                  message: '请选择',
                  trigger: ['blur', 'change']
                }"
              >
                <el-date-picker
                  v-model="row.leaseDateEnd"
                  format="YYYY-MM-DD"
                  value-format="YYYY-MM-DD"
                  placeholder="请选择"
                ></el-date-picker>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120">
            <template v-slot="{ $index, row }">
              <el-button type="danger" link @click="deleteRowItem($index)">删除</el-button>
              <el-button type="success" link @click="addItem(dialogFormRef, row)">复制</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div class="u-m-t-10 u-text-center">
        <el-button type="primary" @click="addItem(dialogFormRef)">新增一行</el-button>
      </div>
    </UDialog>
  </div>
</template>
<script setup>
import { ref, reactive, onBeforeMount, computed } from 'vue'
import { optionsConfig, btnConfig } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { useStore } from 'vuex'
import moment from 'moment'
import { getMarginContractBalanceList, saveMarginContractBalance } from '@/api/rentBusiness/deposit'
import { ElMessage } from 'element-plus'
import { dictMappingToArray, validateForm } from '@/utils'
import { useVoucherPage } from '@/hooks'

const { setVoucherPage } = useVoucherPage()
const { router } = useRouter()
const route = useRoute()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)
const utableRef = ref()
const dialogFormRef = ref()
const isBoolReactive = reactive({
  withDrawDisabled: true,
  submitDisabled: true,
  addTypeVisible: false,
  isVoucher: true
})
const selectedData = ref([])

const dialogFormData = ref({
  dataSource: []
})
const defaultParams = ref({})
const dictKeyLists = ref({
  company: dictMappingToArray(dictData, 'company'),
  currencyType: dictMappingToArray(dictData, 'sys_currency_type')
})

const formSearch = () => {
  return utableRef.value.getSearchParams()
}

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.deposit
})
// 查询录入重分类/利息计提信息的数据
const queryDialogList = async () => {
  const { code, data, msg } = await getMarginContractBalanceList()
  if (code === 200) {
    dialogFormData.value.dataSource = data
    isBoolReactive.addTypeVisible = true
  } else {
    ElMessage.error(msg)
  }
}

// 新增一行
const addItem = async (formEl, item = {}) => {
  const isValid = await validateForm(formEl)
  if (!isValid) return
  dialogFormData.value.dataSource.unshift({ ...item, id: +new Date() })
}
// 删除一行
const deleteRowItem = (index) => {
  dialogFormData.value.dataSource.splice(index, 1)
}

// 保存录入数据
const handleSubmit = async (formEl) => {
  const isValid = await validateForm(formEl)
  if (!isValid) return
  const { dataSource } = dialogFormData.value
  const { code } = await saveMarginContractBalance(dataSource)
  if (code === 200) {
    isBoolReactive.addTypeVisible = false
  }
}
// 多选数据
const selectionChange = (data) => {
  selectedData.value = data
}
const updateLoading = (loading) => {
  utableRef.value.loading = loading
}
// 刷新列表
const refreshList = () => {
  utableRef.value.requestBefore()
}
// 跳转
const fromToRulePage = (row, type) => {
  if (type === 'view') {
    setVoucherPage({ batchType: row.batchType, batchId: row.batchId }, 'total')
    return
  }
  router.push(`/rentBusiness/depositDetail?batchId=${row.batchId}&marginType=${row.marginType}`)
}
onBeforeMount(() => {
  const { query } = route.value
  if (Object.keys(query).length) {
    defaultParams.value = { ...query }
  } else {
    defaultParams.value = { balanceDate: moment().format('YYYY-MM') }
  }
})
</script>
