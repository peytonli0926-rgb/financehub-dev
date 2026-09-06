<template>
  <div>
    <UTable ref="utableRef" :options="options">
      <template #enableFlag="{ row }">
        <el-switch v-permission="permission.enable" active-value="1" inactive-value="0" v-model="row.enableFlag" size="small"
          @change="changeEnableFlag(row)" />
      </template>
      <template #operationBtn="{ row }">
        <el-button v-if="permission.editrule" type="primary" link size="small" @click="fromToRulePage(row)">编辑凭证规则</el-button>
      </template>
    </UTable>
  </div>
</template>
<script setup>
import { ref, computed } from 'vue'
import { optionsConfig } from './config'
import { useRouter } from '@toystory/lotso'
import { changeSwitchRequest } from '@/utils'
import { useStore } from 'vuex'

const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)

// 切换switch 状态
const changeEnableFlag = async (row) => {
  changeSwitchRequest(options.request.edit, row, options.rowKey)
}

// 跳转到凭证规则页面
const fromToRulePage = (row) => {
  router.push(`/engineConfig/rulesConfig?sceneId=${row.id}&sceneName=${encodeURIComponent(row.sceneName)}`)
}
// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.sceneConfig
})

</script>
