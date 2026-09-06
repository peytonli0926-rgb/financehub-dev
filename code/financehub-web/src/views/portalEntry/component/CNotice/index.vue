<template>
  <div>
    <div class="notice-subtitle">
      <span class="u-font-24">金融安全与发展</span>
      <span class="u-m-t-10 color-999999">德勤2023年度数据发布</span>
    </div>
    <ul class="u-m-0 u-p-0 u-m-t-10" style="list-style: none;">
      <li class="u-p-8 u-flex u-row-between">
        <span>系统异常维护，超过字数鼠标悬浮展示…</span>
        <span class="color-999999">2023-01-01</span>
      </li>
      <li class="u-p-8 u-flex u-row-between">
        <span>系统异常维护，超过字数鼠标悬浮展示…</span>
        <span class="color-999999">2023-01-01</span>
      </li>
      <li class="u-p-8 u-flex u-row-between">
        <span>系统异常维护，超过字数鼠标悬浮展示…</span>
        <span class="color-999999">2023-01-01</span>
      </li>
      <li class="u-p-8 u-flex u-row-between">
        <span>系统异常维护，超过字数鼠标悬浮展示…</span>
        <span class="color-999999">2023-01-01</span>
      </li>
      <li class="u-p-8 u-flex u-row-between">
        <span>系统异常维护，超过字数鼠标悬浮展示…</span>
        <span class="color-999999">2023-01-01</span>
      </li>
    </ul>
  </div>
</template>

<script setup>
defineProps({
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
    default: () => { }
  }
})

</script>

<style lang="scss" scoped>
.notice-subtitle {
  margin-top: 20px;
  background: url('@/assets/notice_bg.png') center/cover no-repeat;
  height: 110px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 0 20px;
}
</style>
