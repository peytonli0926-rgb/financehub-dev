import business from '@/assets/engineConfig/business.png'
// import event from '@/assets/engineConfig/event.png'
import preferences from '@/assets/engineConfig/preferences.png'
import regulation from '@/assets/engineConfig/regulation.png'

export default {
  hidden: true,
  title: '配置引导',
  icon: '',
  name: 'guideConfig'
}

export const flowList = [{
  label: '参数配置',
  url: '/engineConfig/dictConfig',
  imagePath: business
}, {
  label: '场景配置',
  url: '/engineConfig/sceneConfig',
  imagePath: preferences
}, {
  label: '业务配置',
  url: '/engineConfig/businessConfig',
  imagePath: regulation
}]

export const flowDescList = [{
  label: '参数配置流程',
  url: '',
  lavel: 1,
  children: [
    {
      label: '参数配置',
      url: '/engineConfig/dictConfig',
      desc: '参数的新增、删除操作及参数内容的维护操作。'
    }
  ]
}, {
  label: '场景配置流程',
  url: '',
  lavel: 1,
  children: [
    {
      label: '参数配置',
      url: '/engineConfig/dictConfig',
      desc: '对事件类型(Event_Type)参数下的参数项进行维护，完成事件的新增、编码和名称编辑、删除操作。'
    },
    {
      label: '场景配置',
      url: '/engineConfig/sceneConfig',
      desc: '事件发生周期的维护操作，事件对应规则的查看操作(注明：对于事件和规则的匹配操作在规则配置中完成)。'
    }
  ]
}, {
  label: '业务配置流程',
  url: '',
  lavel: 1,
  children: [
    {
      label: '参数配置',
      url: '/engineConfig/dictConfig',
      desc: '对产品类型(Business_Type)参数下的参数项进行维护，完成业务的新增、编码和名称编辑、增值税率编辑、删除操作。'
    },
    {
      label: '业务配置',
      url: '/engineConfig/businessConfig',
      desc: '业务所包含的事件及事件的执行顺序的配置操作。'
    }
  ]
}]
