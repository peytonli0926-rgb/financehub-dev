import moment from 'moment'
import { ElTag } from 'element-plus'
import { h } from 'vue'
import { toThousands, dictMappingLabel } from './index'

// 格式化：单元格返回值
export const changeCellValue = (row, item, dictData = []) => {
  const val = row[item.prop]

  switch (item.format) {
    case 'date':
      return (val && moment(val).format('YYYY-MM-DD')) || ''
    case 'time':
      return (val && moment(val).format('YYYY-MM-DD HH:mm:ss')) || ''
    case 'money':
      return toThousands(val)
    case 'dict':
      return (val && dictMappingLabel(dictData, item.dictKey, val)) || ''
    case 'dictTag':
      return (val && h(ElTag, () => dictMappingLabel(dictData, item.dictKey, val))) || ''
    default:
      return val
  }
}
