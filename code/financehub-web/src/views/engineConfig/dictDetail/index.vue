<template>
  <div>
    <UTable ref="utableRef"  :listBefore="listBefore" :addBefore="listBefore" :options="options" :autoLoad="false" @search-form-data="searchFormData">
      <template #status="{ row }">
        <el-switch v-permission="permission.enable" active-value="0" inactive-value="1" v-model="row.status" size="small"
          @change="changeEnableFlag(row)" />
      </template>
    </UTable>
  </div>
</template>
<script setup>
import { ref, onActivated, computed } from 'vue'
import { optionsConfig } from './config'
import { useRouter } from '@toystory/lotso'
import { changeSwitchRequest } from '@/utils'
import { useModifyRouteTitle } from '@/hooks'
import { useStore } from 'vuex'
const { setCurrentTabbarTitle } = useModifyRouteTitle()
const utableRef = ref()
const { router } = useRouter()
const options = ref(optionsConfig(router))
const store = useStore()
// 切换switch 状态
const changeEnableFlag = async (row) => {
  changeSwitchRequest(options.value.request.edit, row, options.value.rowKey)
}

const searchFormData = (value) => {
  refreshList(value)
}

const listBefore = (val) => {
  const currentRoute = router.currentRoute.value
  const { query } = currentRoute
  return { dictType: query?.dictType ?? '', ...val }
}
// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.dictDetail
})
const refreshList = (val = {}) => {
  const currentRoute = router.currentRoute.value
  const { query } = currentRoute
  utableRef.value.requestBefore({ dictType: query.dictType, ...val })
}

onActivated(() => {
  setCurrentTabbarTitle('title')
  refreshList()
})
</script>
