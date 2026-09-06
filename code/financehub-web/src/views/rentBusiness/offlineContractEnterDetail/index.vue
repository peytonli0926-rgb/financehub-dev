<template>
  <el-card shadow="never" :body-style="{ padding: '0 12px 12px' }">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="交易结构" name="first">
        <div class="export-btn" v-permission="permission.F.export">
          <el-button @click="exportExcel" type="primary">导出</el-button>
        </div>
        <el-card shadow="never" class="u-m-t-12">
          <el-table :data="dataSource" border class="u-m-t-10">
            <el-table-column label="序号" width="60" fixed="left" type="index"></el-table-column>
            <el-table-column
              label="主合同编码"
              width="200"
              fixed="left"
              prop="contractCodeM"
            ></el-table-column>

            <el-table-column
              label="合同编码"
              width="200"
              fixed="left"
              prop="contractCode"
            ></el-table-column>
            <el-table-column label="设备款" width="100" prop="payableDevice"></el-table-column>
            <el-table-column
              label="首付款"
              width="100"
              prop="receivableDownpayment"
            ></el-table-column>
            <el-table-column
              label="出租人保险费"
              width="200"
              prop="lessorInsurance"
            ></el-table-column>
            <el-table-column
              label="承租人履行保证金"
              width="200"
              prop="lesseeMargin"
            ></el-table-column>
            <el-table-column label="渠道费用" width="100" prop="channelFee"></el-table-column>
            <el-table-column
              label="手续费收入(含增值税)"
              width="200"
              prop="receivableCommission"
            ></el-table-column>
            <el-table-column
              label="出租人其他成本"
              width="200"
              prop="lessorOtherincome"
            ></el-table-column>
            <el-table-column label="厂商返利" width="100" prop="receivableRebate"></el-table-column>
            <el-table-column
              label="承租人保险费"
              width="200"
              prop="receivableInsurance"
            ></el-table-column>
            <el-table-column
              label="期末残值"
              width="100"
              prop="receivableResidualValue"
            ></el-table-column>
            <el-table-column
              label="其他收入(含增值税)"
              width="200"
              prop="receivableOtherincome"
            ></el-table-column>
            <el-table-column
              label="咨询服务收入(含增值税)"
              width="200"
              prop="receivableService"
            ></el-table-column>
            <el-table-column
              label="供应商履约保证金"
              width="200"
              prop="supplierMargin"
            ></el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>
      <el-tab-pane label="租金计划" name="second">
        <div class="export-btn" v-permission="permission.S.export">
          <el-button @click="exportExcel" type="primary">导出</el-button>
        </div>
        <UTable
          :autoLoad="false"
          ref="utableRef"
          :defaultParams="defaultParams"
          :options="options"
        ></UTable>
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useStore } from 'vuex'
import { optionsConfig } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { offlineContractDetail } from '@/api/rentBusiness/offlineContractEnterDetail'
import { useExport } from '@/hooks'

const { handleExport } = useExport()
const dataSource = ref([])
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const activeName = ref('first')
const defaultParams = ref({})
const { router } = useRouter()
const options = optionsConfig(router, dictData)

const route = useRoute()
const { query } = route.value
const handleClick = ({ index }) => {
  if (index === '1') {
    refreshList()
  }
}

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.offlineContractEnterDetail
})

const getOfflineContractDetail = async () => {
  const { contractCode, id } = query
  const { data } = await offlineContractDetail({ contractCode, id })
  dataSource.value = [data]
}

// 刷新列表
const refreshList = () => {
  utableRef.value.requestBefore()
}

// 导出
const exportExcel = () => {
  const { contractCode, id } = query
  let label = '交易结构'
  let url = '/engine/finance/offline-contract/detail/structure/export'
  if (activeName.value === 'second') {
    label = '租金计划'
    url = '/engine/finance/offline-contract/detail/detail/export'
  }
  handleExport(
    {
      url,
      params: {
        contractCode,
        id
      }
    },
    `线下合同录入-${label}.xlsx`
  )
}
onMounted(() => {
  const { contractCode, id } = query
  getOfflineContractDetail()
  defaultParams.value = { contractCode, id }
})
</script>

<style lang="scss" scoped>
.export-btn {
  text-align: right;
}
</style>
