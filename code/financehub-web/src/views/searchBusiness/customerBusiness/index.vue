<template>
    <div>
        <UTable ref="utableRef" :options="options" @selection-change="handlerChange"></UTable>

    </div>
</template>

<script setup>
import { ref } from 'vue'
import { optionsConfig } from './config'
import { useRouter } from '@toystory/lotso'

const { router } = useRouter()
const utableRef = ref()

const options = optionsConfig(router)

const handlerChange = (value) => {
  console.log(value)
}
</script>

<style lang="scss" scoped></style>
