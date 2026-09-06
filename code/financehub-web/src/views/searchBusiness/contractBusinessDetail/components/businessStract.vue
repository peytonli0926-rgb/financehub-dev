<template>
  <div class="baseInfo" v-loading="loading">
    <UDescriptions  :list="businessStractList" :detail="details" />
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from '@toystory/lotso'
import { businessStractList } from './data.js'
import UDescriptions from '@/components/UDescriptions/index.vue'
import { getContractDetail } from '@/api/searchBusiness/contractBusiness.js'

const route = useRoute()
const details = ref({})
const loading = ref(false)
const queryContractDetail = async () => {
  loading.value = true
  const { query } = route.value
  const { data } = await getContractDetail(query.id)
  details.value = data
  loading.value = false
}

onMounted(() => {
  queryContractDetail()
})

</script>

<style lang="scss" scoped></style>
