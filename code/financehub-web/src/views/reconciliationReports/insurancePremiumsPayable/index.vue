<template>
  <!-- 应付保险费 -->
  <div>
    <UTable ref="utableRef" :defaultParams="defaultParams" :options="options">
      <template #operateHeaderRight>
        <el-button
          :icon="Switch"
          v-permission="permission.dataAsync"
          type="success"
          @click="handleAsyncData"
        >
          同步
        </el-button>
      </template>
    </UTable>
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { confirmEl } from '@/utils'
import { ref, reactive, toRefs, computed } from 'vue'
import { useRouter } from '@toystory/lotso'
import { useStore } from 'vuex'
import { Switch } from '@element-plus/icons-vue'
import { optionsConfig } from './config'
import { syncCheckExisted, syncSubjectCommon } from '@/api/reconciliationReports'

const { router } = useRouter()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']

const utableRef = ref()
const state = reactive({
  defaultParams: {},
  options: optionsConfig(router, dictData)
})

const { defaultParams, options } = toRefs(state)

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']

  return btnPermissions.insurancePremiumsPayable
})

// 数据同步
const asyncData = async (periodCode) => {
  try {
    const { code, msg } = await syncSubjectCommon({
      periodCode,
      businessType: 'yfbxf'
    })
    if (+code === 200) {
      ElMessage.success(msg || '数据同步成功')
      return
    }
    ElMessage.error(msg || '数据同步失败')
  } catch (error) {
    console.log(error)
  }
}

// 检查数据同步
const handleAsyncData = async () => {
  try {
    const { periodCode } = await utableRef.value.getSearchParams()
    if (!periodCode) {
      ElMessage.error('请选择会计期间')
      return
    }

    const { data, code, msg } = await syncCheckExisted({
      periodCode,
      businessType: 'Detail'
    })

    if (+code === 200) {
      if (data) {
        confirmEl(`会计期间[${periodCode}]已经存在，是否继续数据同步操作？`).then(() => {
          asyncData(periodCode)
        })
      } else {
        asyncData(periodCode)
      }
    } else {
      ElMessage.error(msg)
    }
  } catch (error) {
    console.log(error)
  }
}
</script>
