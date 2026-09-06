export default {
  hidden: false,
  title: '我的首页',
  icon: '',
  name: 'homePage'
}

export const option = {
  // title:{
  //   text:'异常报表'
  // },
  tooltip: {
    trigger: 'item'
  },
  legend: {
    // type: 'scroll',
    orient: 'vertical',
    right: 10,
    top: '25%'
    // bottom: 20,
  },
  series: [
    {
      name: 'Access From',
      type: 'pie',
      radius: ['60%', '80%'],
      center: ['35%', '50%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 0,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        position: 'center',
        show: true,
        formatter: () => {
          const str = '总风险数' + '\n\n' + `${0}`
          return str
        },
        color: '#D70D18',
        lineHeight: 16,
        fontSize: 18
      },

      emphasis: {
        label: {
          show: true,
          // fontSize: 40,
          fontWeight: 'bold'
        }
      },
      labelLine: {
        show: false
      },
      data: [
        { value: 1048, name: 'Search Engine', itemStyle: { color: '#D70D18' } },
        { value: 735, name: 'Direct', itemStyle: { color: '#F05A63' } },
        { value: 580, name: 'Email', itemStyle: { color: '#C7952D' } },
        { value: 484, name: 'Union Ads', itemStyle: { color: '#8F0911' } },
        { value: 300, name: 'Video Ads', itemStyle: { color: '#F3C969' } }
      ]
    }
  ]
}
const colors = ['#D70D18', '#C7952D', '#F05A63']
export const lineOption = {
  color: colors,
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'cross'
    }
  },
  grid: {
    right: '5%'
  },

  legend: {
    data: ['Evaporation', 'Precipitation']
  },
  xAxis: [
    {
      type: 'category',
      axisTick: {
        alignWithLabel: true
      },
      // prettier-ignore
      data: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
    }
  ],
  yAxis: {
    type: 'value',
    name: '温度',
    position: 'left',
    alignTicks: true,
    axisLine: {
      show: true,
      lineStyle: {
        color: colors[2]
      }
    },
    axisLabel: {
      formatter: '{value} °C'
    }
  },
  series: [
    {
      name: 'Evaporation',
      type: 'bar',
      data: [
        2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3
      ]
    },
    {
      name: 'Precipitation',
      type: 'bar',
      data: [
        2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3
      ]
    }

  ]
}
