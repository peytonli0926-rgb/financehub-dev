<template>
  <div>
    <UTable ref="utableRef" @search-form-data="refreshList" :defaultParams="defaultParams" :autoLoad="false"
      :options="options">
      <template #operateHeaderRight>
        <el-button @click="handleAsyncData" :icon="Switch" v-permission="permission.dataAsync" type="success">同步</el-button>
      </template>
    </UTable>

  </div>
</template>

<script setup>
import { ref, onBeforeMount, computed } from 'vue'
import { optionsConfig } from './config'
import { useRouter } from '@toystory/lotso'
import { useStore } from 'vuex'
import moment from 'moment'
import { syncCheckExisted, syncSubjectMiddle } from '@/api/reconciliationReports'
import { ElMessage } from 'element-plus'
import { Switch } from '@element-plus/icons-vue'
import { confirmEl } from '@/utils'

const defaultParams = ref({})
const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)

// 检查数据同步数据同步
const handleAsyncData = async () => {
  const { periodCode } = await utableRef.value.getSearchParams()
  if (!periodCode) {
    ElMessage.error('请选择会计期间')
    return
  }
  const { data } = await syncCheckExisted({
    periodCode,
    businessType: 'Middle'
  })
  if (data) {
    confirmEl(`会计期间[${periodCode}]已经存在，是否继续数据同步操作？`).then(async () => {
      await asyncData(periodCode)
    })
  } else {
    await asyncData(periodCode)
  }
}
// 数据同步接口
const asyncData = async (periodCode) => {
  const { code, msg } = await syncSubjectMiddle(periodCode)
  if (code === 200) {
    ElMessage.success(msg || '数据同步成功')
    return
  }
  ElMessage.error(msg || '数据同步失败')
}
// 刷新列表
const refreshList = (val = {}) => {
  utableRef.value.requestBefore(val)
}
// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.subjectKingDeeMiddle
})
onBeforeMount(() => {
  const periodCode = parseInt(moment().format('YYYYMM'))
  defaultParams.value = { periodCode }
})

</script>

<style lang="scss" scoped></style>
