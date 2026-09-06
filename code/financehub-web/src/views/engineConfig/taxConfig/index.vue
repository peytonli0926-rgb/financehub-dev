<template>
  <div>
    <UTable ref="utableRef"  :options="options" >
      <template #enableFlag="{ row }">
        <el-switch v-permission="permission.enable" active-value="1" inactive-value="0" v-model="row.enableFlag" size="small"
          @change="changeEnableFlag(row)" />
      </template>

    </UTable>

  </div>
</template>
<script setup>
import { ref, computed } from 'vue'
import { optionsConfig } from './config'
import { useRouter } from '@toystory/lotso'
import { useStore } from 'vuex'
import { changeSwitchRequest } from '@/utils'

const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)
// 切换switch 状
const changeEnableFlag = async (row) => {
  changeSwitchRequest(options.request.edit, row, options.rowKey)
}

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.taxConfig
})

</script>

<style lang="scss" scoped>
:deep(.el-input__wrapper) {
    width: 95.5% !important;
}
</style>
