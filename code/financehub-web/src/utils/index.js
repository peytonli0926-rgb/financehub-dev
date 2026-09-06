import setting from '@/config/setting'
import { ElMessageBox, ElMessage } from 'element-plus'
import { getCommonTableList } from '@/api/common'
import { customRef, ref, h } from 'vue'

const { title } = setting

export const getPageTitle = (pageTitle) => {
  if (pageTitle) {
    return `${pageTitle}-${title}`
  }
  return `${title}`
}

export function parseTime(time, cFormat) {
  if (arguments.length === 0) {
    return null
  }
  const format = cFormat || '{y}-{m}-{d} {h}:{i}:{s}'
  let date
  if (typeof time === 'object') {
    date = time
  } else {
    if (typeof time === 'string' && /^[0-9]+$/.test(time)) {
      time = parseInt(time)
    }
    if (typeof time === 'number' && time.toString().length === 10) {
      time = time * 1000
    }
    date = new Date(time)
  }
  const formatObj = {
    y: date && date.getFullYear(),
    m: date && date.getMonth() + 1,
    d: date && date.getDate(),
    h: date && date.getHours(),
    i: date && date.getMinutes(),
    s: date && date.getSeconds(),
    a: date && date.getDay()
  }
  const timeStr = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
    let value = formatObj[key]
    // Note: getDay() returns 0 on Sunday
    if (key === 'a') {
      return ['日', '一', '二', '三', '四', '五', '六'][value]
    }
    if (result.length > 0 && value < 10) {
      value = '0' + value
    }
    return value || 0
  })
  return timeStr
}

export function formatTime(time, option) {
  if (('' + time).length === 10) {
    time = parseInt(time) * 1000
  } else {
    time = +time
  }
  const d = new Date(time)
  const now = Date.now()

  const diff = (now - d) / 1000

  if (diff < 30) {
    return '刚刚'
  } else if (diff < 3600) {
    // less 1 hour
    return Math.ceil(diff / 60) + '分钟前'
  } else if (diff < 3600 * 24) {
    return Math.ceil(diff / 3600) + '小时前'
  } else if (diff < 3600 * 24 * 2) {
    return '1天前'
  }
  if (option) {
    return parseTime(time, option)
  } else {
    return (
      d.getMonth() + 1 + '月' + d.getDate() + '日' + d.getHours() + '时' + d.getMinutes() + '分'
    )
  }
}

export function getQueryObject(url) {
  url = url == null ? window.location.href : url
  const search = url.substring(url.lastIndexOf('?') + 1)
  const obj = {}
  const reg = /([^?&=]+)=([^?&=]*)/g
  search.replace(reg, (rs, $1, $2) => {
    const name = decodeURIComponent($1)
    let val = decodeURIComponent($2)
    val = String(val)
    obj[name] = val
    return rs
  })
  return obj
}

/**
 * @param {Sting} input value
 * @returns {number} output value
 */
export function byteLength(str) {
  // returns the byte length of an utf8 string
  let s = str.length
  for (var i = str.length - 1; i >= 0; i--) {
    const code = str.charCodeAt(i)
    if (code > 0x7f && code <= 0x7ff) s++
    else if (code > 0x7ff && code <= 0xffff) s += 2
    if (code >= 0xdc00 && code <= 0xdfff) i--
  }
  return s
}

export function cleanArray(actual) {
  const newArray = []
  for (let i = 0; i < actual.length; i++) {
    if (actual[i]) {
      newArray.push(actual[i])
    }
  }
  return newArray
}

export function param(json) {
  if (!json) return ''
  return cleanArray(
    Object.keys(json).map((key) => {
      if (json[key] === undefined) return ''
      return encodeURIComponent(key) + '=' + encodeURIComponent(json[key])
    })
  ).join('&')
}

export function param2Obj(url) {
  const search = url.split('?')[1]
  if (!search) {
    return {}
  }
  return JSON.parse(
    '{"' +
      decodeURIComponent(search)
        .replace(/"/g, '\\"')
        .replace(/&/g, '","')
        .replace(/=/g, '":"')
        .replace(/\+/g, ' ') +
      '"}'
  )
}

export function html2Text(val) {
  const div = document.createElement('div')
  div.innerHTML = val
  return div.textContent || div.innerText
}

export function objectMerge(target, source) {
  /* Merges two  objects,
     giving the last one precedence */

  if (typeof target !== 'object') {
    target = {}
  }
  if (Array.isArray(source)) {
    return source.slice()
  }
  Object.keys(source).forEach((property) => {
    const sourceProperty = source[property]
    if (typeof sourceProperty === 'object') {
      target[property] = objectMerge(target[property], sourceProperty)
    } else {
      target[property] = sourceProperty
    }
  })
  return target
}

export function toggleClass(element, className) {
  if (!element || !className) {
    return
  }
  let classString = element.className
  const nameIndex = classString.indexOf(className)
  if (nameIndex === -1) {
    classString += '' + className
  } else {
    classString =
      classString.substr(0, nameIndex) + classString.substr(nameIndex + className.length)
  }
  element.className = classString
}

export const pickerOptions = [
  {
    text: '今天',
    onClick(picker) {
      const end = new Date()
      const start = new Date(new Date().toDateString())
      end.setTime(start.getTime())
      picker.$emit('pick', [start, end])
    }
  },
  {
    text: '最近一周',
    onClick(picker) {
      const end = new Date(new Date().toDateString())
      const start = new Date()
      start.setTime(end.getTime() - 3600 * 1000 * 24 * 7)
      picker.$emit('pick', [start, end])
    }
  },
  {
    text: '最近一个月',
    onClick(picker) {
      const end = new Date(new Date().toDateString())
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
      picker.$emit('pick', [start, end])
    }
  },
  {
    text: '最近三个月',
    onClick(picker) {
      const end = new Date(new Date().toDateString())
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
      picker.$emit('pick', [start, end])
    }
  }
]

export function getTime(type) {
  if (type === 'start') {
    return new Date().getTime() - 3600 * 1000 * 24 * 90
  } else {
    return new Date(new Date().toDateString())
  }
}

export function debounce(func, wait, immediate) {
  let timeout, args, context, timestamp, result

  const later = function () {
    // 据上一次触发时间间隔
    const last = +new Date() - timestamp

    // 上次被包装函数被调用时间间隔 last 小于设定时间间隔 wait
    if (last < wait && last > 0) {
      timeout = setTimeout(later, wait - last)
    } else {
      timeout = null
      // 如果设定为immediate===true，因为开始边界已经调用过了此处无需调用
      if (!immediate) {
        result = func.apply(context, args)
        if (!timeout) context = args = null
      }
    }
  }

  return function (...args) {
    context = this
    timestamp = +new Date()
    const callNow = immediate && !timeout
    // 如果延时不存在，重新设定延时
    if (!timeout) timeout = setTimeout(later, wait)
    if (callNow) {
      result = func.apply(context, args)
      context = args = null
    }

    return result
  }
}

/**
 * This is just a simple version of deep copy
 * Has a lot of edge cases bug
 * If you want to use a perfect deep copy, use lodash's _.cloneDeep
 */
export function deepClone(source) {
  if (!source && typeof source !== 'object') {
    return new Error('error arguments', 'deepClone')
  }
  const targetObj = source.constructor === Array ? [] : {}
  Object.keys(source).forEach((keys) => {
    if (source[keys] && typeof source[keys] === 'object') {
      targetObj[keys] = deepClone(source[keys])
    } else {
      targetObj[keys] = source[keys]
    }
  })
  return targetObj
}

export function uniqueArr(arr) {
  return Array.from(new Set(arr))
}

export function createUniqueString() {
  const timestamp = +new Date() + ''
  const randomNum = parseInt((1 + Math.random()) * 65536) + ''
  return (+(randomNum + timestamp)).toString(32)
}

export function hasClass(ele, cls) {
  return !!ele.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'))
}
export function addClass(ele, cls) {
  if (!hasClass(ele, cls)) ele.className += ' ' + cls
}
export function removeClass(ele, cls) {
  if (hasClass(ele, cls)) {
    const reg = new RegExp('(\\s|^)' + cls + '(\\s|$)')
    ele.className = ele.className.replace(reg, ' ')
  }
}

export function isEmpty(obj) {
  return [Object, Array].includes((obj || {}).constructor) && !Object.entries(obj || {}).length
}

export function blob2file(blob, name) {
  return new File([blob], name, { type: blob.type })
}

export function cutstr(str, len) {
  let strlength = 0
  let strLen = 0
  let strcut = ''
  strLen = str.length
  for (var i = 0; i < strLen; i++) {
    const a = str.charAt(i)
    strlength++
    if (escape(a).length > 4) {
      // 中文字符的长度经编码之后大于4
      strlength++
    }
    strcut = strcut.concat(a)
    if (strlength >= len) {
      strcut = strcut.concat('...')
      return strcut
    }
  }
  // 如果给定字符串小于指定长度，则返回源字符串；
  if (strlength < len) {
    return str
  }
}

export function openLink(url, target) {
  // debugger
  const link = document.createElement('A')
  link.target = target || ''
  link.href = url
  link.download = url
  link.style.display = 'none'
  document.body.appendChild(link)
  link.click()
  link.parentNode.removeChild(link)
}

export function formatDuring(mss) {
  const days = parseInt(mss / (1000 * 60 * 60 * 24))
  const hours = parseInt((mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60))
  const minutes = parseInt((mss % (1000 * 60 * 60)) / (1000 * 60))
  const seconds = Math.round((mss % (1000 * 60)) / 1000)
  if (days > 0) {
    return days + '天' + hours + ' 小时 ' + minutes + ' 分钟 ' + seconds + ' 秒 '
  } else {
    if (hours > 0) {
      return hours + ' 小时 ' + minutes + ' 分钟 ' + seconds + ' 秒 '
    } else {
      if (minutes > 0) {
        return minutes + ' 分钟 ' + seconds + ' 秒 '
      } else {
        return seconds + ' 秒 '
      }
    }
  }
}

export function addElementColor(str = '', el = 'a', color) {
  return str.replace(new RegExp('<' + el + '.*?>', 'ig'), function (searchVal) {
    const styleAttr = searchVal.match(/(?<=style=['|"]).*?(?=['|"])/g)
    if (!styleAttr) {
      return searchVal.replace(new RegExp('<' + el), '<' + el + ' style="color: ' + color + ';"')
    } else {
      return searchVal.replace(/(?<=style=['|"]).*?(?=['|"])/g, function (val) {
        if (!/color/.test(val)) {
          return 'color: ' + color + ';' + val
        } else {
          const _wcolor = val.match(/\w*-color/g)
          const _color = val.match(/color/g)
          if (_wcolor && _wcolor.length === _color.length) {
            return 'color: ' + color + ';' + val
          } else {
            return val
          }
        }
      })
    }
  })
}

// 全局公共二次确认框
export const confirmEl = (content, title = '系统提示') => {
  return new Promise((resolve) => {
    ElMessageBox.confirm(content, title, {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
      .then(() => {
        resolve()
      })
      .catch(() => {})
  })
}
// 全局公共二次确认框
export const alertEl = (content, title = '系统提示') => {
  return new Promise((resolve) => {
    ElMessageBox.alert(content, title, {
      confirmButtonText: '确认',
      callback: (action) => {
        resolve()
      }
    })
  })
}
// 解析url地址参数是否有占位符
export const formatUrlParams = (url, row) => {
  const match = url.match(/\$\[(.*?)]/)
  if (match) {
    const placeholder = match[0]
    const key = match[1]
    const newUrl = url.replace(placeholder, row[key])
    return newUrl
  } else {
    return url
  }
}
// 千分位格式化
export const toThousands = (number, customStyle = '') => {
  if (number === 0 || number === null || number === undefined) {
    return h('div', { style: 'text-align:right' }, '0.00')
  }
  try {
    if (isNaN(number)) {
      return h('div', { style: `text-align:right;${customStyle}` }, '0.00')
    }
    const formattedNumber = Number(number).toFixed(2)
    const ddd = formattedNumber.replace(/\B(?=(\d{3})+(?!\d))/g, ',')
    return h('div', { style: `text-align:right;${customStyle}` }, `${ddd}`)
  } catch (error) {
    console.log('数据错误')
  }
}

// 根据字典mapping对象转换数据字典数组
export const dictMappingToArray = (dictMapping, dictType = '') => {
  const mapping = dictMapping[dictType] || {}
  let list = []
  for (const key in mapping) {
    const obj = {}
    obj.value = key
    obj.label = mapping[key]
    list = [...list, obj]
  }
  return list // .sort((a,b)=>a.sort-b.sort)
}

export const dictMappingLabel = (dictMapping, dictType = '', key) => {
  if (Reflect.has(dictMapping, dictType)) {
    if (Array.isArray(key)) {
      const obj = dictMapping[dictType]
      return key.map((item) => {
        return obj[item]
      })
    }

    return dictMapping[dictType][key] || key
  }
  return key
}

// 切换switch 状态
/**
 *
 * @param {*} request 请求参数
 * @param {*} row 当前一条数据
 * @param {*} key 关键key
 * @returns
 */
export const changeSwitchRequest = async (request, row, key) => {
  if (!Reflect.has(row, key)) return
  const { url: _url, ...result } = request
  const url = formatUrlParams(_url, row)
  const { msg } = await getCommonTableList({
    ...result,
    url,
    params: row
  })
  ElMessage.success(msg || '操作成功')
}
/**
 * 对表格进行合并计算操作
 * @param {*} sumArr  需要对表格进行合计的字段
 * @param {*} param table原始数据
 * @returns
 */
export const getTableSummaries = (sumArr = [], param) => {
  if (!Array.isArray(sumArr)) {
    ElMessage.error('请传入需要计算合计的字段名称。')
    Promise.reject(new Error('请传入需要计算合计的字段名称。'))
    return []
  }
  const { columns, data } = param
  const sums = []
  columns.forEach((column, index) => {
    if (index === 1) {
      sums[index] = '合计'
      return
    }
    const values = data.map((item) => {
      if (sumArr.includes(column.property)) {
        return Number(item[column.property])
      }
      return '-'
    })
    if (!values.every((value) => Number.isNaN(value))) {
      const total = `${values.reduce((prev, curr) => {
        const value = Number(curr)
        if (!Number.isNaN(value)) {
          return prev + curr
        } else {
          return ''
        }
      }, 0)}`
      sums[index] = total && toThousands(total)
    } else {
      sums[index] = '-'
    }
  })

  return sums
}

// 从 vue 中引入 customRef 和 ref

// data 为创建时的数据
// delay 为防抖时间
export function debounceRef(data, delay = 300) {
  // 创建定时器
  let timer = null
  // 对 delay 进行判断，如果传递的是 null 则不需要使用 防抖方案，直接返回使用 ref 创建的。
  // 返回 ref 创建的
  // customRef 中会返回两个函数参数。一个是：track 在获取数据时收集依赖的；一个是：trigger 在修改数据时进行通知派发更新的。
  return delay == null
    ? ref(data)
    : customRef((track, trigger) => {
      return {
        get() {
          // 收集依赖
          track()
          // 返回当前数据的值
          return data
        },
        set(value) {
          // 清除定时器
          if (timer != null) {
            clearTimeout(timer)
            timer = null
          }
          // 创建定时器
          timer = setTimeout(() => {
            // 修改数据
            data = value
            // 派发更新
            trigger()
          }, delay)
        }
      }
    })
}
// data 为创建时的数据
// delay 为节流时间
export function throttleRef(data, delay = 300) {
  // 创建定时器
  let timer = null
  // 对 delay 进行判断，如果传递的是 null 则不需要使用 节流方案，直接返回使用 ref 创建的。
  // 返回 ref 创建的
  // customRef 中会返回两个函数参数。一个是：track 在获取数据时收集依赖的；一个是：trigger 在修改数据时进行通知派发更新的。
  return delay == null
    ? ref(data)
    : customRef((track, trigger) => {
      return {
        get() {
          // 收集依赖
          track()
          // 返回当前数据的值
          return data
        },
        set(value) {
          // 判断
          if (timer == null) {
            // 创建定时器
            timer = setTimeout(() => {
              // 修改数据
              data = value
              // 派发更新
              trigger()
              // 清除定时器
              clearTimeout(timer)
              timer = null
            }, delay)
          }
        }
      }
    })
}

// 验证是否有数据
export const validateForm = (formEl) => {
  return new Promise((resolve) => {
    if (formEl) {
      formEl.validate((valid) => {
        if (valid) {
          resolve(true)
        } else {
          // ElMessage.error({
          //   zIndex: 9999999,
          //   message: '您的信息还未填写完整，请检查！'
          // })
          resolve(false)
        }
      })
    } else {
      resolve(true)
    }
  })
}
