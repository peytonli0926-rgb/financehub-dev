<template>

    <div ref="widthRef" :style="{ height: (height + 10) + 'px' }">

      <div v-if="getDivWidth > 0" :id="canvasId" class="canvas-container"
        :style="{ height: (height - 49) + 'px', width: ((getDivWidth - 10) + 'px') }"></div>
    </div>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue'
import * as echarts from 'echarts'

const widthRef = ref()
const getDivWidth = ref(0)
const pieChart = ref()

const props = defineProps({
  height: {
    type: Number,
    default: () => 0
  },
  width: {
    type: Number,
    default: () => 0
  },
  canvasId: {
    type: String,
    default: () => 'main'
  },
  title: {
    type: String,
    default: () => ''
  },
  option: {
    type: Object,
    required: true,
    default: () => { }
  }
})

watch(getDivWidth, (n) => {
  if (n > 0) {
    setTimeout(() => {
      pieChart.value = echarts.init(document.getElementById(props.canvasId))
      pieChart.value.setOption(props.option, true)
    }, 300)
  }
}, {
  deep: true,
  immediate: true
})

onMounted(() => {
  setTimeout(() => {
    getDivWidth.value = widthRef.value.offsetWidth
  }, 300)
})

defineExpose({
  chart: pieChart
})
</script>

<style lang="scss" scoped>
.canvas-container {
  // position: absolute;
  position: relative;
  overflow: hidden;
  left: 0;
  width: 100%;
}

::v-deep(.el-card__body) {
  height: 100%;
  padding: 0;
}</style>
