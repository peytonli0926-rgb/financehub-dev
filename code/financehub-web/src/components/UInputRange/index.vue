<template>
    <el-space class="u-flex u-row-between w-100">
        <el-input v-model="start" :type="attrs['type']"  :placeholder="attrs['start-placeholder']" @change="inputChange($event,0)" />
        -
        <el-input v-model="end"  :type="attrs['type']" :placeholder="attrs['end-placeholder']" @change="inputChange($event,1)"/>
    </el-space>
</template>

<script setup>
import { ref, watch } from 'vue'
const start = ref('')
const end = ref('')
const values = ref(new Array(2))
const emits = defineEmits(['update:modelValue'])
const props = defineProps({
  attrs: {
    type: Object,
    default: () => ({})
  },
  modelValue: {
    type: Array,
    default: () => []
  }

})

watch(() => props.modelValue, (o, n) => {
  console.log(o, n)
  if (o.length === 0) {
    start.value = ''
    end.value = ''
    emits('update:modelValue', new Array(2))
  }
})

const inputChange = (val, index) => {
  values.value[index] = val
  emits('update:modelValue', values)
}

</script>

<style lang="scss" scoped></style>
