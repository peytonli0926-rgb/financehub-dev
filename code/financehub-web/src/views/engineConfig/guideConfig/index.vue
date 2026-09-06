<template>
  <el-card shadow="never">
    <div class="title">业务流程</div>
    <div class="content u-flex u-row-around u-text-center u-m-t-10">
      <dl v-for="item of flowList" :key="item.label">
        <dt><img :src="item.imagePath" style="width: 120px" alt="" srcset="" /></dt>
        <dd class="u-m-0 u-p-0">
          <el-button size="small" @click="routePath(item)">{{ item.label }}</el-button>
        </dd>
      </dl>
    </div>
  </el-card>

  <el-card shadow="never" class="u-m-t-12">
    <div class="title">业务说明</div>
    <el-row class="content u-row-top u-m-t-10">
      <el-col :span="8" v-for="item of flowDescList" :key="item.label" class="u-p-b-20 u-p-t-20">
        <div class="u-text-center u-text-bold">{{ item.label }}</div>
        <div class="u-m-t-20">
          <dl
            v-for="(child, index) of item.children"
            :class="[index === 0 ? '' : 'u-p-t-20']"
            class="u-m-0 line u-p-l-30 u-rela"
            :key="child.label"
          >
            <dt>
              <el-button @click="routePath(child)">{{ child.label }}</el-button>
            </dt>
            <dd class="u-m-0 u-m-t-20 u-p-0">{{ child.desc }}</dd>
          </dl>
        </div>
      </el-col>
    </el-row>
  </el-card>
</template>
<script setup>
import { flowList, flowDescList } from './config'
import { useRouter } from '@toystory/lotso'
const { router } = useRouter()

const routePath = (item) => {
  router.push(item.url)
}
</script>

<style lang="scss" scoped>
.title {
  font-family: PingFangSC-Medium, PingFang SC;
  font-size: 16px;
  font-weight: 400;
}

.content {
  .line {
    border-left: 1px dashed #ddd;
    &::after {
      position: absolute;
      top: 5px;
      left: -6px;
      width: 10px;
      height: 10px;
      content: '';
      background: var(--el-color-primary);
      border-radius: 10px;
    }
  }
}
</style>
