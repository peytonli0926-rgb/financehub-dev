### 关于组件使用方法

### 按钮事件配置

该配置 放在和config.js 里面。在vue页面引入组件配置

目前默认对【voucher、
delete、
submit、
withdraw】
支持，可以不用写condition限制条件表达式， 可以自定义，如果有根据选择数据进行禁用启用，则需要配置condition


```vue
<template>
   <UButtonOperate :config="btnInboundConfig" :selected-data="selectedData" :ref-table="utableRef" @refresh="refreshList" />

</template>
```
```js
btnInboundConfig //按钮配置
selectedData //当前列表选中的数据
utableRef //当前列表的 ref
refreshList //操作成功后，回调刷新列表
```
---

```json

{
  common:{ //通用配置
    disabled:'processStatus', //勾选后根据哪一个字段禁用，自定义不受该字段限制  
    getKey:'id' //选择的数据，根据哪一个字段值返回给后端
  },
  import: { //导入按钮 固定写法
    title: '上传',
    type: 'primary',
    isDisabled:false,
    level:'S'//有第二层级
    query:[],//跳转参数
    redirectUrl:'',//重定向的地址
    params: {
      url: '/engine/finance/recycling-equipment/importTemplate',
      title: '回收设备-财务入库',
      data:{},//上传参数
      open: true,
      templateUrl: '/engine/finance/recycling-equipment/exportTemplate'
    },
  },
  export: { //导出按钮 固定写法
    title: '导出',
    url: '/engine/finance/recycling-equipment/exportSum',
    isParams: true,
    fetchKey:'',
    filename:'回收设备-财务入库.xlsx',
  },
  [key]: { //key,可以是voucher、delete、submit、withdraw，同时可以自定义
    title: '生成凭证', //按钮名称
    type: 'warning',//按钮类型
    isDisabled:false,// 是否需要禁用条件，如果false，则跳过该按钮的禁用限制
    condition:(item)=>{return true}, //数组item 是选择多选的数据
    url: '/engine/finance/recycling-equipment/generateVoucher',//接口地址
    isParams: true,//参数是否是json格式，如果是，则需要配置fetchKey，不是直接放在body中。
    fetchKey:'',//参数字段
  },
}


```
