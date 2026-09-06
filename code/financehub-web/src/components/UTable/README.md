# financehub-web 系统

基于 Vue3.0，使用 Element Plus 框架,对 table 进行了封装操作，对查询区域和列表展示合二为一，也可单独使用

## 使用

### 在全局中注册 UTable 组件

config 配置

```javascript
{
  isIndex: true, // 是否需要序号
  isIndexFixed: true,//序号是否浮动
  isIndexOrder:true,//序号是否需要计数，如果没有展示10行，那么在第二行序号从11开始
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,//是否要边框
  isOperateHeader: true, // 是否需要table头部操作区域，
  isSearch:true,//是否需要搜索
  rowKey: 'id', // 表格唯一id
  leftCardName: '',//表格名称展示
  dialogSpan:12,//新增和编辑弹框表单展示的宽度
  span:12,//搜索表单展示的宽度
  addBtn: {//统一点击按钮
    isShow: false,
    label:'新增'
  },
  editBtn: { //表格行内编辑按钮
    isShow: false,
    label:'编辑'
  },
  delBtn: {////表格行内删除按钮
    isShow: false,
     label:'删除'
  },
  request: { // 请求参数
    list: {
      url: '', //列表接口地址
      method: 'post'
    },
      add: {
     url: '', //新增接口地址
      method: 'post'
    },
    edit: {
     url: '', //编辑接口地址
      method: 'post'
    },
    del: {
     url: '', //删除接口地址
      method: 'post'
    }
  },
  //配置功能按钮 ，具体参考 UButtonOperate文件下的MD
  btnConfig:{

  },
  columns: [ { 
    width: 250, //列表宽度
    alias:''，//别名，用户搜索展示
    prop: '', //列表字段
    label: '' //列表名称
    type:''//组件类型默认input|search｜date｜inputNumber｜editor｜drawer
    search:true,//搜索展示要查询的字段
    hide:false,//列表中需要展示或者隐藏  默认false 展示 ｜true 隐藏
    display:true,//弹框中是否要展示改字段 默认false 不展示 ｜true 展示
    attrs:{},//element 组件属性
    }
    ]
}
```
