<template>
  <div class="u-flex u-col-center u-text-left">
    <slot name="icon">
      <el-icon>
        <Document />
      </el-icon>
    </slot>
    <slot name="title"
      ><span class="u-m-l-10 u-text-bold">{{ title }}</span></slot
    >
  </div>
</template>

<script setup>
import { Document } from '@element-plus/icons-vue'
defineProps({
  title: {
    type: String,
    default: () => ''
  }
})
</script>

<style lang="scss" scoped></style>
