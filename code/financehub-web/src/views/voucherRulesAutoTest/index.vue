<template>
  <!-- 凭证规则自动测试 -->
  <div>
    <UTable
      ref="utableRef"
      :defaultParams="defaultParams"
      :options="options"
      @selection-change="handleSelectionChange"
    >
      <template #operateHeaderRight>
        <el-button type="warning" v-permission="permission.refresh" @click="refreshFn">
          刷新
        </el-button>
        <el-button v-permission="permission.run" type="success" @click="bindRunFn">
          运行
        </el-button>
        <el-button v-permission="permission.allRun" type="primary" @click="bindAllRunFn">
          全部运行
        </el-button>
        <el-button
          v-permission="permission.updateVoucher"
          type="warning"
          @click="bindUpdateVoucher"
        >
          更新凭证比对样本
        </el-button>
      </template>

      <template #operationBtn="{ row }">
        <!-- 导出报告 -->
        <el-button
          v-if="row.state === '30'"
          type="primary"
          link
          size="small"
          v-permission="permission.export"
          @click="exportReportFn(row)"
        >
          导出报告
        </el-button>
      </template>
    </UTable>
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { toRefs, reactive, computed, ref } from 'vue'
import { useRouter } from '@toystory/lotso'
import { useStore } from 'vuex'
import { optionsConfig } from './config'
import { confirmEl } from '@/utils'
import { useExport } from '@/hooks'
import VoucherRulesAutoTestAPI from '@/api/voucherRulesAutoTest/index'

const { router } = useRouter()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const { handleExport } = useExport()

const utableRef = ref()
const state = reactive({
  defaultParams: {},
  options: optionsConfig(router, dictData),
  selectArr: []
})
const { defaultParams, options } = toRefs(state)

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']

  return btnPermissions.voucherRulesAutoTestMain
})

const runRulesSubmit = async (flag) => {
  try {
    const params = {
      flag,
      ...state.defaultParams
    }

    const { code, msg } = await VoucherRulesAutoTestAPI.runRulesSubmit(params)
    if (code === 200) {
      ElMessage.success('生成成功')
      utableRef.value.requestBefore() // 刷新
    } else {
      ElMessage.error(msg)
    }
  } catch (error) {
    console.log(error)
  }
}

// 运行
const bindRunFn = () => {
  if (state.defaultParams.eventFrom && state.defaultParams.eventTo) {
    confirmEl('是否确定开始执行?', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(async () => {
      runRulesSubmit('N')
    })
  } else {
    ElMessage.error('请输入事件编号')
  }
}

// 全部运行
const bindAllRunFn = () => {
  confirmEl('是否确定开始执行?', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    runRulesSubmit('Y')
  })
}
// 更新凭证比对样本
const updateCompareVoucher = async () => {
  try {
    const { taskId } = state.selectArr[0]
    const { code, msg } = await VoucherRulesAutoTestAPI.updateCompareVoucher({
      taskId
    })
    if (+code === 200) {
      ElMessage.success('操作成功')
      utableRef.value.requestBefore()
    } else {
      ElMessage.error(msg)
    }
  } catch (error) {
    console.log(error)
  }
}

// 更新凭证比对样本
const bindUpdateVoucher = () => {
  if (state.selectArr.length > 1) {
    ElMessage.error('只能选择一条数据')
  } else if (state.selectArr.length === 0) {
    ElMessage.error('请选择数据')
  } else {
    confirmEl('是否确定更新凭证比对样本?', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      updateCompareVoucher()
    })
  }
}

// 导出
const exportReportFn = ({ taskId }) => {
  handleExport(
    {
      url: '/engine/finance/voucher-rule-check/downloadVoucherRuleRpt',
      params: {
        taskId
      }
    },
    '凭证规则自动测试报表.xlsx'
  )
}

// 多选
const handleSelectionChange = (val) => {
  state.selectArr = val
}

// 刷新
const refreshFn = () => {
  utableRef.value.requestBefore()
}
</script>

<style lang="scss" scoped></style>
