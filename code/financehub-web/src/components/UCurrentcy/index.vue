<template>
    <div class="text-right">
     {{ toThousandsValue }}
    </div>
</template>

<script setup>
import { computed } from 'vue'
import { toThousands } from '@/utils'

const props = defineProps({
  modelValue: {
    type: [String, Number],
    default: ''
  }

})
const toThousandsValue = computed(() => {
  return toThousands(props.modelValue)
})

</script>

<style lang="scss" scoped></style>
