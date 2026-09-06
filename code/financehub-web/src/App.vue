<template>
  <el-config-provider :locale="localLanguage">
    <el-scrollbar height="100vh" ref="scroll" view-class="container-scroll">
      <router-view></router-view>
    </el-scrollbar>
    <UFileUpload />
  </el-config-provider>
</template>

<script setup>
import { ref, watch, onBeforeMount } from 'vue'
import UFileUpload from '@/components/UFileUpload/index.vue'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import { getToken } from '@/utils/auth'
import { useRouter } from '@toystory/lotso'
import { useStore } from 'vuex'

const btnPermissions = ref({})
const store = useStore()
const localLanguage = ref(zhCn)

const getAllDictObj = async () => {
  if (getToken()) {
    store.dispatch('useDictMapping/getDictMapping')
    const userInfo = await store.getters['user/userInfo']
    if (userInfo) {
      const params = {
        staffName: userInfo.staffName,
        email: userInfo.email,
        remark: userInfo.remark,
        phoneNumber: userInfo.phoneNumber,
        stat: userInfo.stat,
        workplace: userInfo.workplace,
        deptmentId: userInfo?.deptmentId ?? '',
        deptmentName: userInfo.deptmentName,
        gender: userInfo.gender,
        staffType: userInfo.staffType,
        deptmentSecId: userInfo?.deptmentSecId ?? '',
        deptmentSecName: userInfo.deptmentSecName,
        deptmentThrId: userInfo?.deptmentThrId ?? '',
        deptmentThrName: userInfo.deptmentThrName,
        positionId: (userInfo?.positionId ?? '').toString(),
        positionName: userInfo.positionName
      }
      window.eagleEye && window.eagleEye.trackUser(params)
    }
    btnPermissions.value = await getRentBusinessMenu()
    // store.dispatch('useBtnPermission/setPermission',btnPermissions.value)
  }
}

const getRentBusinessMenu = async () => {
  const bpmData = await store.getters['user/bpmData']
  return await new Promise((resolve, reject) => {
    const btnPermissions = {}
    const _rentBusinessMenu = (arr) => {
      try {
        arr.map((item) => {
          if (item.children && item.children.length > 0) {
            _rentBusinessMenu(item.children)
          } else {
            if (item.menuType === 3) {
              const elements = item.elements
              const elementObj = {}
              elements.map((item) => {
                const name = item.name
                const nameArr = name.split('_')
                const _index = nameArr.findIndex((ii) => ii === 'btn')
                const _newNameArr = nameArr.slice(_index)
                if (_newNameArr.length === 3) {
                  elementObj[_newNameArr[1]] = {
                    ...elementObj[_newNameArr[1]],
                    [_newNameArr[2]]: name
                  }
                } else {
                  elementObj[nameArr[nameArr.length - 1]] = name
                }
              })

              btnPermissions[item.menu] = elementObj
            }
          }
        })
      } catch (e) {
        // getRentBusinessMenu()
      }
    }
    _rentBusinessMenu(bpmData)
    btnPermissions.value = btnPermissions
    store.dispatch('useBtnPermission/setPermission', btnPermissions)
    resolve(btnPermissions)
  })
}

const scroll = ref(null)

const { router } = useRouter()

watch(
  () => router.currentRoute.value,
  () => {
    getAllDictObj()
    scroll.value.setScrollTop(0)
  }
)

const posiTop = ref('')
watch(
  () => store.getters.collapse,
  (val) => {
    if (!val) {
      posiTop.value = '239px'
    } else {
      posiTop.value = '63px'
    }
  },
  {
    immediate: true
  }
)

onBeforeMount(async () => {
  getAllDictObj()
})

watch(
  () => btnPermissions.value,
  (n) => {
    store.dispatch('useBtnPermission/setPermission', n)
  },
  { deep: true, immediate: true }
)
</script>

<style lang="scss">
#app {
  font-family: 'Microsoft Yahei', Avenir, Helvetica, Arial, sans-serif;
  font-size: $base-font-size-default;
  color: #2c3e50;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;

  .el-scrollbar {
    --el-scrollbar-bg-color: #d70d18;
    --el-scrollbar-hover-bg-color: #d70d18;
    --el-scrollbar-opacity: 0.8;
  }

  .admin-container {
    .main {
      top: 102px !important;
    }
    .header {
      position: fixed;
      top: 0;
      left: v-bind(posiTop) !important;
      z-index: 100;
      width: calc(100% - v-bind(posiTop));
      height: auto;
      background: #f5f7f9;
    }
  }
}

.el-sub-menu .el-menu--inline {
  background: #74070e !important;
}

.sidebar-logo-container {
  background: #fff !important;
  border-right: 1px solid #ebeef2;
  text-align: left !important;
}

.sidebar-logo-container:not(.collapse) .sidebar-logo {
  width: 188px !important;
  height: auto !important;
  margin: 0 0 0 14px !important;
}

.sidebar-logo-container:not(.collapse) .sidebar-title {
  display: none !important;
}

.sidebar-logo-container.collapse .sidebar-logo {
  width: 32px !important;
  height: 32px !important;
  content: url('/static/image/side-logo-huaxia.png');
}

.el-menu .is-black .el-menu-item.is-active {
  z-index: 99;
  background: #d70d18 !important;
}

.tabs-bar-container {
  height: auto !important;
  padding-left: 0 !important;
}

.el-tabs__header {
  margin-bottom: 12px !important;
}

.tabs-bar-container .tabs-content .el-tabs__header .el-tabs__nav .el-tabs__item.is-active {
  background: #d70d18 !important;
}

.tabs-bar-container .tabs-content .el-tabs__header .el-tabs__nav .el-tabs__item:hover {
  background: #d70d18 !important;
}

.el-dialog__body {
  border-top: 1px solid #e2e2e2 !important;
  border-bottom: 1px solid #e2e2e2 !important;
}

.w-100 {
  width: 100% !important;
}

.h-100 {
  height: 100% !important;
}

ul[role='menubar'] {
  position: relative;
  z-index: 1010;
}

.custom-edit-show {
  max-width: 400px !important;
  overflow: auto;
}

.custom-dialog .el-dialog__body {
  padding: 0 !important;
}

.el-row {
  display: flex;

  flex-wrap: wrap;
}

.el-popover__title {
  padding: 10px 10px 0 10px;
  margin: 0 !important;
}

.custom-tooltip {
  max-width: 600px !important; //宽度可根据自己需要进行设置
}

.custom-class {
  .el-dialog__header {
    padding: 10px !important;
    text-align: left;

    .el-dialog__headerbtn {
      width: 30px;
      height: 30px;
    }
  }
  .el-dialog__body {
    padding: 10px 20px 10px 20px !important;
    margin: 0;
  }
  .el-dialog__footer {
    padding: 10px !important;
  }
}
</style>
