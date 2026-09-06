<template>
    <UTable ref="utableRef" :defaultParams="defaultParams" :options="options">
        <template #operationBtn="{ row }">
            <el-button type="primary" link size="small" v-permission="permission.viewVoucher" @click="fromToRulePage(row)"
                :disabled="!row.voucherId">查看凭证</el-button>
        </template>
    </UTable>
</template>

<script setup>
import { ref, onBeforeMount } from 'vue'
import { useStore } from 'vuex'
import { optionsStructureConfig } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { useVoucherPage } from '@/hooks'

defineProps({
  permission: {
    type: Object,
    default: () => ({})
  }
})

const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const defaultParams = ref({})
const { router } = useRouter()
const options = optionsStructureConfig(router, dictData)

const { setVoucherPage } = useVoucherPage()

const route = useRoute()
const { query } = route.value

const fromToRulePage = ({ voucherId }) => {
  setVoucherPage({ voucherIdList: [voucherId] })
}

onBeforeMount(() => {
  const { id } = query
  defaultParams.value = {
    idList: [id]
  }
})
</script>

<style lang="scss" scoped></style>
