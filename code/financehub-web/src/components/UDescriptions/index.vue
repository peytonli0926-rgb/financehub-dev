<template>
  <div class="baseInfo">
    <div class="baseInfo-item" v-for="(item, index) of descriptList" :key="index">
      <div class="baseInfo-title u-flex u-row-between">
        <span>{{ item.title }}</span>
        <div class="u-p-r-20 u-cursor-p" @click="handleClick(item)">
          <span>{{ !item.show ? '收起' : '展开' }}</span>
          <el-icon v-if="item.show">
            <ArrowUp />
          </el-icon>
          <el-icon v-else>
            <ArrowDown />
          </el-icon>
        </div>
      </div>
      <div v-show="!item.show" class="baseInfo-content u-flex" v-for="(contentItem, indx) in item.column" :key="indx">
        <div class="baseInfo-content-item u-flex u-flex-1" v-for="prop in contentItem.props" :key="prop.prop">
          <div class="baseInfo-content-item-title u-flex-1 u-p-10 left-label ">
            {{ prop.name }}
          </div>
          <div class="baseInfo-content-item-content u-flex-1 u-p-10 left-value">
            <span v-if="prop.type === 'currency'">
               <component :is="toThousands(detail[prop.prop])"/>
            </span>
            <span v-else>{{ formatShowData(prop) }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref, onMounted } from 'vue'
import { useStore } from 'vuex'
import { dictMappingLabel, toThousands, parseTime } from '@/utils'
import { ArrowUp, ArrowDown } from '@element-plus/icons-vue'

const descriptList = ref([])
const props = defineProps({
  list: {
    type: Array,
    default: () => []
  },
  detail: {
    type: Object,
    default: () => ({})
  },
  leftBgColor: {
    type: String,
    default: '#FAFAFA'
  },
  borderColor: {
    type: String,
    default: '#EFEFEF'
  }
})
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']

const formatShowData = ({ type, dictType, prop }) => {
  const value = props.detail[prop]
  if (prop === '') return ''
  if ([undefined, null].includes(value)) return ''

  const typeFun = {
    date: parseTime(value, '{y}-{m}-{d}'),
    dict: dictMappingLabel(dictData, dictType, value)
  }
  return (type && typeFun[type]) || value
}
const handleClick = (item) => {
  if (Reflect.has(item, 'show')) {
    item.show = !item.show
  } else {
    item.show = true
  }
}

const borderColor = computed(() => {
  return props.borderColor
})

const leftBgColor = computed(() => {
  return props.leftBgColor
})

// watch(()=>props.list,(val)=>{
//   descriptList.value = val
// },{
//   deep:true,
//   immediate:true
// })

onMounted(() => {
  descriptList.value = props.list
})

</script>

<style lang="scss" scoped>
// .baseInfo-item {

.baseInfo-title {
  position: relative;
  height: 40px;
  line-height: 40px;
  padding: 10px 0 10px 15px;
  // border-left: 1px solid;
  // border-right: 1px solid;
  // border-color: v-bind(borderColor);

  &::before {
    content: '';
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
    left: 0;
    width: 4px;
    height: 20px;
    border-radius: 5px;
    background-color: #D70D18
  }

}

// &:first-child .baseInfo-title {
//     border-top: 1px solid;
//     border-color: v-bind(borderColor);
// }
// }

.baseInfo-content {
  border-top: 1px solid;
  border-left: 1px solid;
  border-right: 1px solid;
  border-color: v-bind(borderColor);

  &:last-child {
    // border-right: 1px solid #000;
    border-bottom: 1px solid;
    border-color: v-bind(borderColor);
  }

  .baseInfo-content-item {
    box-sizing: border-box;

    .left-label {
      background: v-bind(leftBgColor);
      border-left: 1px solid;
      border-color: v-bind(borderColor);

    }

    .left-value {
      word-break: break-all;
      border-left: 1px solid;
      border-color: v-bind(borderColor);
    }

    &:first-child .left-label {
      border-left: 0px solid transparent;
    }

  }
}</style>
