<template>
  <div class="cockpit-page">
    <section class="hero">
      <div><span>FINANCIAL MANAGEMENT PLATFORM</span><h1>财务中台首页</h1><p>日结日清 · 强大接口 · 灵活会计规则配置</p></div>
      <div class="hero-art" aria-hidden="true"><div class="orbit orbit-a"></div><div class="orbit orbit-b"></div><div class="stage"></div><img src="/static/image/side-logo-huaxia.png" alt="" /><div class="bars"><i></i><i></i><i></i><i></i></div></div>
    </section>

    <section class="metrics">
      <article v-for="item in metrics" :key="item.title" class="metric-card">
        <div class="metric-icon"><el-icon><component :is="item.icon" /></el-icon></div>
        <div class="metric-main"><span>{{ item.title }}</span><p><strong>{{ item.value }}</strong><b>{{ item.unit }}</b></p><small>较昨日&nbsp; {{ item.compare }} <em :class="item.direction">{{ item.direction === 'down' ? '↓' : '↑' }}</em></small></div>
      </article>
    </section>

    <section class="section-card route-section">
      <header><h2>处理链路运行状态</h2><span>实时监控业财处理全链路</span></header>
      <div class="route-flow">
        <template v-for="(system, index) in systems" :key="system.title">
          <article class="system-card" :class="{ active: index === 1 }">
            <div class="system-illustration"><div class="system-stack"><i></i><i></i><i></i></div><el-icon><component :is="system.icon" /></el-icon></div>
            <div class="system-info"><h3>{{ system.title }}</h3><p v-for="row in system.rows" :key="row.text"><i :class="row.tone"></i><span>{{ row.text }}</span><b v-if="row.value">{{ row.value }}</b></p></div>
          </article>
          <div v-if="index < systems.length - 1" class="flow-arrow"><span></span><el-icon><ArrowRightBold /></el-icon></div>
        </template>
      </div>
    </section>

    <section class="section-card capability-section">
      <header><h2>平台能力总览</h2><span>业财一体化核心能力</span></header>
      <div class="capability-grid">
        <article v-for="item in capabilities" :key="item.title"><div><el-icon><component :is="item.icon" /></el-icon></div><p><strong>{{ item.title }}</strong><span>{{ item.subtitle }}</span></p></article>
      </div>
    </section>

    <section class="target-strip">
      <div class="target-title"><el-icon><Aim /></el-icon><strong>建设目标</strong></div>
      <div v-for="item in targets" :key="item.title" class="target-item"><el-icon><component :is="item.icon" /></el-icon><strong>{{ item.title }}</strong></div>
      <p>统一纳入会计引擎、接口平台、业财数据模型、核对及特色功能，形成完整业财运营平台。</p>
    </section>

    <MyDocuments />
  </div>
</template>

<script setup>
import { Aim, ArrowRightBold, Calendar, CircleCheck, Coin, Connection, DataAnalysis, Document, Finished, Link, Notebook, Operation, PieChart, Setting, Tickets, TrendCharts, UploadFilled, WarningFilled } from '@element-plus/icons-vue'
import MyDocuments from './MyDocuments.vue'

const metrics = [
  ['今日接入业务数据', '128.6', '万笔', '+12.4%', 'up', UploadFilled],
  ['今日业务事件', '36.8', '万笔', '+8.7%', 'up', Document],
  ['自动核算率', '99.6', '%', '+0.3pct', 'up', TrendCharts],
  ['业财核对一致率', '99.8', '%', '+0.2pct', 'up', CircleCheck],
  ['凭证生成', '4,286', '张', '+356张', 'up', Tickets],
  ['异常待处理', '23', '笔', '-3笔', 'down', WarningFilled],
  ['月结进度', '82', '%', '+11pct', 'up', PieChart]
].map(([title, value, unit, compare, direction, icon]) => ({ title, value, unit, compare, direction, icon }))

const systems = [
  { title: '业务系统', icon: Notebook, rows: [{ text: '数据接入正常', tone: 'ok' }, { text: '接口成功率', value: '99.9%', tone: 'ok' }, { text: '待入湖', value: '2,341笔', tone: 'warn' }, { text: '最近同步', value: '09:35', tone: 'ok' }] },
  { title: '业财中台', icon: Setting, rows: [{ text: '事件识别正常', tone: 'ok' }, { text: '规则引擎运行中', tone: 'ok' }, { text: '自动核算处理中', tone: 'ok' }, { text: '核对任务待处理', value: '23笔', tone: 'warn' }] },
  { title: '金蝶总账', icon: DataAnalysis, rows: [{ text: '凭证已推送', value: '4,286张', tone: 'ok' }, { text: '入账成功率', value: '99.7%', tone: 'ok' }, { text: '待记账', value: '61张', tone: 'warn' }, { text: '总账返回正常', tone: 'ok' }] }
]

const capabilities = [
  ['接口平台', '多源系统统一接入与监控', Connection], ['会计引擎', '灵活规则配置与自动核算', Setting],
  ['业财数据模型', '沉淀事件、台账与核算数据', Coin], ['核对中心', '业财、账务、总账多维核对', CircleCheck],
  ['特色功能', '日清日结、异常预警、运营分析', Finished]
].map(([title, subtitle, icon]) => ({ title, subtitle, icon }))

const targets = [['日结日清', Calendar], ['强大接口', Link], ['灵活会计规则配置', Operation]].map(([title, icon]) => ({ title, icon }))
</script>

<style lang="scss" scoped>
$red:#d70d18;$deep:#8f0911;$muted:#7e8490;$line:#e9edf2;
.cockpit-page{min-height:calc(100vh - 158px);padding:10px 12px;overflow:auto;color:#25282e;background:#f5f6f8}
.hero{position:relative;display:flex;align-items:center;min-height:122px;padding:18px 32px;overflow:hidden;background:linear-gradient(105deg,#fff 0%,#fff 52%,#fff0f1 100%);border:1px solid #f4e6e7;border-radius:7px;box-shadow:0 3px 12px rgba(32,35,42,.03);>div:first-child{position:relative;z-index:2}>div>span{color:$red;font-size:10px;font-weight:700;letter-spacing:2px}h1{margin:6px 0 3px;font-size:30px;letter-spacing:2px}p{margin:0;color:$muted;font-size:13px;letter-spacing:1.5px}}
.hero-art{position:absolute;inset:0 0 0 auto;width:44%;background-image:linear-gradient(rgba(215,13,24,.055) 1px,transparent 1px),linear-gradient(90deg,rgba(215,13,24,.055) 1px,transparent 1px);background-size:24px 24px;mask-image:linear-gradient(90deg,transparent,#000 35%);.orbit{position:absolute;right:18%;bottom:16px;border:1px solid rgba(215,13,24,.27);border-radius:50%;transform:rotate(-8deg)}.orbit-a{width:295px;height:74px}.orbit-b{right:23%;bottom:27px;width:210px;height:50px}.stage{position:absolute;right:27%;bottom:22px;width:148px;height:42px;background:linear-gradient(#fff,#ffdfe1);border:1px solid #f5b6ba;border-radius:50%;box-shadow:0 17px 25px rgba(215,13,24,.16)}img{position:absolute;right:34%;bottom:39px;width:64px;height:64px;border:5px solid #fff;border-radius:18px;box-shadow:0 12px 22px rgba(174,6,17,.25)}}
.bars{position:absolute;right:10%;bottom:35px;display:flex;align-items:flex-end;gap:8px;i{width:14px;background:linear-gradient(#ff7d84,$red);border-radius:3px 3px 0 0}i:nth-child(1){height:20px;opacity:.55}i:nth-child(2){height:38px;opacity:.68}i:nth-child(3){height:55px;opacity:.82}i:nth-child(4){height:72px}}
.metrics{display:grid;grid-template-columns:repeat(7,minmax(0,1fr));gap:6px;margin-top:7px}.metric-card{display:flex;align-items:center;min-width:0;min-height:92px;padding:10px;background:#fff;border:1px solid $line;border-radius:7px;box-shadow:0 3px 10px rgba(30,34,40,.025)}
.metric-icon{display:flex;flex:0 0 auto;align-items:center;justify-content:center;width:42px;height:42px;margin-right:9px;color:$red;font-size:23px;background:#fff0f1;border-radius:50%}.metric-main{min-width:0;>span{display:block;overflow:hidden;font-size:11px;font-weight:600;text-overflow:ellipsis;white-space:nowrap}p{margin:3px 0 2px;color:$red;white-space:nowrap}strong{font-size:23px}b{margin-left:2px;font-size:10px}small{color:#999fa8;font-size:9px;white-space:nowrap}em{margin-left:2px;color:$red;font-style:normal;&.down{color:#43a46c}}}
.section-card{margin-top:7px;background:#fff;border:1px solid $line;border-radius:7px;box-shadow:0 3px 10px rgba(30,34,40,.025);header{display:flex;align-items:center;padding:10px 15px 7px}h2{margin:0;font-size:14px;&:before{display:inline-block;width:3px;height:14px;margin:0 7px -2px 0;content:'';background:$red;border-radius:2px}}header span{margin-left:10px;color:#a0a5ad;font-size:9px}}
.route-flow{display:flex;align-items:center;padding:3px 18px 12px}.system-card{display:flex;flex:1;align-items:center;min-height:126px;padding:12px 18px;background:linear-gradient(135deg,#fff,#fafbfc);border:1px solid #dfe4ea;border-radius:8px;&.active{background:linear-gradient(135deg,#fff,#fff7f7);border-color:#ef6a72;box-shadow:0 7px 20px rgba(215,13,24,.07)}}
.system-illustration{position:relative;display:flex;flex:0 0 auto;align-items:center;justify-content:center;width:112px;height:96px;margin-right:15px;color:$red;font-size:42px;background:radial-gradient(circle,#fff 20%,#fff0f1 65%,transparent 67%);>.el-icon{position:relative;z-index:2}.system-stack{position:absolute;display:flex;flex-direction:column;gap:5px;transform:rotate(-8deg);i{display:block;width:70px;height:13px;background:linear-gradient(90deg,#ff7b82,$red);border-radius:4px;box-shadow:0 5px 8px rgba(215,13,24,.14)}}}
.system-info{flex:1;h3{margin:0 0 9px;font-size:16px}.active & h3{color:$red}p{display:flex;align-items:center;margin:5px 0;font-size:10px}p>i{width:7px;height:7px;margin-right:7px;background:#52b976;border-radius:50%;&.warn{background:#f3a014}}p span{flex:1;color:#555b65}p b{margin-left:8px;font-weight:500;white-space:nowrap}}
.flow-arrow{display:flex;flex:0 0 52px;align-items:center;margin:0 8px;color:#c4c8ce;span{flex:1;height:8px;background:#d5d8dd;clip-path:polygon(0 25%,75% 25%,75% 0,100% 50%,75% 100%,75% 75%,0 75%)}.el-icon{display:none}}
.capability-grid{display:grid;grid-template-columns:repeat(5,1fr);gap:8px;padding:0 12px 12px}.capability-grid article{display:flex;align-items:center;min-height:62px;padding:8px 13px;border:1px solid $line;border-radius:7px;transition:.2s;&:hover{background:#fffafa;border-color:#efb3b7;transform:translateY(-1px)}article{}>div{display:flex;align-items:center;justify-content:center;width:40px;height:40px;margin-right:10px;color:$red;font-size:26px;background:#fff0f1;border-radius:9px}p{display:flex;min-width:0;margin:0;flex-direction:column}strong{font-size:13px}span{margin-top:4px;overflow:hidden;color:$muted;font-size:9px;text-overflow:ellipsis;white-space:nowrap}}
.target-strip{display:flex;align-items:center;min-height:52px;margin-top:7px;padding:0 15px;background:linear-gradient(90deg,#fff7f7,#fff);border:1px solid #f3d9db;border-radius:7px}.target-title,.target-item{display:flex;align-items:center;padding:0 18px;color:$red;border-right:1px solid #f0d7d9;.el-icon{margin-right:7px;font-size:22px}strong{font-size:12px;white-space:nowrap}}.target-title{padding-left:0}.target-item{height:34px}.target-strip>p{margin:0 0 0 18px;color:#6f7580;font-size:10px;line-height:1.6}
@media(max-width:1450px){.metrics{grid-template-columns:repeat(4,1fr)}.system-card{padding:10px}.system-illustration{width:78px}.capability-grid{grid-template-columns:repeat(3,1fr)}}
@media(max-width:950px){.metrics{grid-template-columns:repeat(2,1fr)}.route-flow{overflow-x:auto}.system-card{flex:0 0 330px}.capability-grid{grid-template-columns:repeat(2,1fr)}.hero-art{opacity:.55}.target-strip{align-items:flex-start;padding:12px;flex-wrap:wrap}.target-strip>p{width:100%;margin:8px 0 0}}
@media(max-width:600px){.hero{padding:18px}.hero-art{display:none}.metrics,.capability-grid{grid-template-columns:1fr}.target-item{padding:0 9px}.section-card header span{display:none}}
</style>
