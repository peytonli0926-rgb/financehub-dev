<template>
  <svg :class="svgClass" aria-hidden="true" v-bind="$attrs" :style="{ 'font-size': size, color }">
    <use :xlink:href="symbolId" :fill="color || ''" />
  </svg>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  name: {
    type: String,
    required: true
  },
  color: {
    type: String,
    default: '#333'
  },
  size: {
    type: String,
    default: '14px'
  }
})

const symbolId = computed(() => `#icon-${props.name}`)
const svgClass = computed(() => {
  if (props.name) {
    return `svg-icon icon-${props.name}`
  }
  return 'svg-icon'
})
</script>

<style lang="scss">
  .svg-icon {
    width: 1em;
    height: 1em;
    vertical-align: middle;
    fill: currentColor;
  }
</style>
