import authData from './data/authData'

// const VITE_APP_SERVER_SER = '/api/portalBackend'
const VITE_APP_BPM_API = '/api'
const VITE_APP_GATEWAY = '/api'
export default [
  {
    url: `${VITE_APP_GATEWAY}/login`,
    method: 'post',
    response: (config) => {
      // const { username } = config.body
      return {
        msg: '操作成功',
        code: 200,
        data: {
          targetSystemName: 'FLS-BIZ-COMM-CCM',
          userId: 'XUEXIANGQIAN585'
        },
        success: true
      }
    }
  },
  {
    url: `${VITE_APP_BPM_API}/bpmAuthTokenRegister/register`,
    type: 'post',
    response (config) {
      const { systemCode } = config.body
      if (!systemCode) {
        return {
          code: 500,
          msg: '参数错误',
          success: false,
          data: null,
          time: '2021-11-03 18:37:24'
        }
      } else {
        return {
          code: 200,
          msg: '操作成功',
          success: true,
          data: {
            token: '0852421302BDB2C8C4264DF99EDE465F'
          },
          time: '2021-11-03 18:37:24'
        }
      }
    }
  },
  {
    url: `${VITE_APP_BPM_API}/authDataOutput/frontData`,
    type: 'post',
    response (config) {
      return {
        code: 200,
        msg: '操作成功',
        success: true,
        data: {
          role: [{ roleNo: '100009990001', roleName: '系统管理员' }, { roleNo: '100009990026', roleName: '福利管理员' }],
          data: authData
        },
        time: '2021-11-03 18:37:24'
      }
    }
  },
  {
    url: `${VITE_APP_GATEWAY}/logout`,
    type: 'post',
    response () {
      return {
        msg: '操作成功',
        code: 200,
        success: true
      }
    }
  },
  {
    url: `${VITE_APP_GATEWAY}/getUserInfo`,
    type: 'get',
    response () {
      return {
        code: 200,
        msg: '操作成功',
        success: true,
        data: {
          stat: '在职',
          staffCode: '102931',
          gender: '',
          deptmentThrName: '',
          staffType: null,
          positionName: '前端开发岗',
          deptmentSecName: '系统研发部',
          deptmentThrId: '',
          password: null,
          phoneNumber: '19921070517',
          positionId: '2304',
          deptmentId: '545',
          staffName: '高翔宇',
          pwdUpdateTime: null,
          deptmentName: '金融科技部',
          workplace: '恒信',
          deptmentSecId: '546',
          email: 'xiangyu.gao@utflc.com'
        },
        time: '2022-10-24 18:57:48'
      }
    }
  },
  {
    url: '/api/pacas/responseCode',
    type: 'get',
    response () {
      return {
        msg: '操作成功',
        code: 200,
        data: ['101301500', '101402900', '101403000', '101403100', '101302800'],
        success: true
      }
    }
  }
]
