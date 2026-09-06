<template>
  <el-dialog
    :z-index="zIndex"
    class="custom-class"
    :modelValue="visible"
    :width="dialogWidth"
    :before-close="handleClose"
  >
    <template #header>
      {{ title }}
    </template>
    <slot></slot>

    <template #footer>
      <div v-if="isFooter">
        <span class="dialog-footer">
          <el-button @click="handleClose">取消</el-button>
          <el-button type="primary" @click="handleSubmit"> 确定 </el-button>
        </span>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
const emit = defineEmits(['update:visible', 'handle-submit'])

defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  dialogWidth: {
    type: String,
    default: () => '30%'
  },
  title: {
    type: String,
    default: ''
  },
  isFooter: {
    type: Boolean,
    default: true
  },
  zIndex: {
    type: [String, Number]
  },
  permission: {
    type: Object,
    default: () => ({})
  }
})

const handleClose = () => {
  emit('update:visible', false)
}

const handleSubmit = () => {
  emit('handle-submit')
}
</script>

<style lang="scss" scoped></style>
