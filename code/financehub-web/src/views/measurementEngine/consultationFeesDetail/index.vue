<template>
    <div>
        <UTable ref="utableRef"  :defaultParams="defaultParams" :options="options"
          >

            <template #operationBtn="{ row }">
              <el-button type="primary" v-permission="permission.viewVoucher" link size="small" @click="viewVoucher(row)" :disabled="!row.voucherId">查看凭证</el-button>
                <el-button type="primary" v-permission="permission.view" link size="small" @click="fromToRulePage(row)">查看详情</el-button>

            </template>

            <!-- <template #operateHeaderRight>
                <el-button @click="exportExcel" type="primary">导出</el-button>
            </template> -->

        </UTable>

    </div>
</template>
<script setup>
import { ref, onBeforeMount, computed } from 'vue'
import { useStore } from 'vuex'
import { optionsConfig } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { useVoucherPage } from '@/hooks'
const { setVoucherPage } = useVoucherPage()
const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)
const route = useRoute()
const { query } = route.value

const defaultParams = ref({})

const viewVoucher = ({ voucherId }) => {
  setVoucherPage({ voucherIdList: [voucherId] })
}

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.consultationFeesDetail
})

const fromToRulePage = ({ contractCode }) => {
  router.push(`/rentBusiness/consultationFeesPlan?contractCode=${contractCode}`)
}

onBeforeMount(() => {
  const { serviceFeeId } = query
  defaultParams.value = { serviceFeeId }
})

</script>
