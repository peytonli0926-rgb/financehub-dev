<template>
  <div class="lease-workbench">
    <section class="workbench-hero">
      <div class="hero-copy">
        <h1>业务处理工作台</h1>
        <span class="title-mark"></span>
        <p>围绕收款、尾差、减值、收益、税务等关键环节，提供高效、可视、可追溯的业务处理能力。</p>
      </div>

      <div class="architecture" aria-hidden="true">
        <div class="building building-back"></div>
        <div class="building building-front"></div>
      </div>

      <div class="hero-slogan">
        <span>金融赋能产业</span>
        <span>服务创造价值</span>
        <i></i>
      </div>
    </section>

    <main class="function-grid">
      <button
        v-for="item in functions"
        :key="item.path"
        type="button"
        class="function-card"
        :class="`theme-${item.theme}`"
        @click="openFunction(item.path)"
      >
        <span class="function-icon">
          <el-icon><component :is="item.icon" /></el-icon>
        </span>
        <h2>{{ item.title }}</h2>
        <p>
          <span>{{ item.description[0] }}</span>
          <span>{{ item.description[1] }}</span>
        </p>
        <span class="enter-button" :aria-label="`进入${item.title}`">
          <el-icon><Right /></el-icon>
        </span>
        <span class="card-wave" aria-hidden="true"></span>
      </button>
    </main>

    <footer class="workbench-footer">
      <span>© 2025 华夏金融租赁有限公司</span>
      <i></i>
      <span>租赁业务管理系统&nbsp;&nbsp;V1.0</span>
      <div class="footer-values">
        <span>专业</span><span>高效</span><span>稳健</span><span>共赢</span>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { Coin, DataAnalysis, Operation, Right, Tickets, TrendCharts } from '@element-plus/icons-vue'
import { useRouter } from '@toystory/lotso'

const { router } = useRouter()

const functions = [
  { title: '未确认收款', description: ['核对银行来款', '完成收款认领'], path: '/rentBusiness/nonConfirmCollectionSum', icon: Coin, theme: 'rose' },
  { title: '尾差调整', description: ['处理合同尾差', '生成调整凭证'], path: '/rentBusiness/endAdjust', icon: Operation, theme: 'blue' },
  { title: '减值计提', description: ['执行减值计提', '生成计提凭证'], path: '/measurementEngine/impairmentBusiness', icon: DataAnalysis, theme: 'amber' },
  { title: '收益计提', description: ['计算租赁收益', '生成计提凭证'], path: '/incomeProvisionAccess/incomeProvision', icon: TrendCharts, theme: 'green' },
  { title: '税务报表', description: ['生成税务报表', '完成申报准备'], path: '/rentBusiness/valueAddedTax', icon: Tickets, theme: 'violet' }
]

const openFunction = path => {
  router.push(path)
}
</script>

<style scoped lang="scss">
$ink: #111c2f;
$muted: #748198;
$line: #e4eaf2;

.lease-workbench {
  display: flex;
  min-height: calc(100vh - 116px);
  padding: 0 34px;
  overflow: hidden;
  color: $ink;
  background: radial-gradient(circle at 78% 10%, rgba(225, 237, 249, 0.58), transparent 25%), linear-gradient(180deg, #fbfdff 0%, #fff 42%);
  flex-direction: column;
}

.workbench-hero { position: relative; min-height: 178px; padding: 44px 12px 12px; overflow: hidden; }
.hero-copy { position: relative; z-index: 2; }
.hero-copy h1 { margin: 0; font-size: 34px; font-weight: 700; letter-spacing: 2px; line-height: 1.25; }
.title-mark { display: block; width: 31px; height: 4px; margin: 12px 0; background: #e60012; border-radius: 2px; }
.hero-copy p { margin: 0; color: $muted; font-size: 14px; letter-spacing: 0.5px; }

.hero-slogan {
  position: absolute;
  z-index: 2;
  top: 54px;
  right: 12px;
  display: flex;
  color: #9ba8bc;
  font-size: 14px;
  letter-spacing: 4px;
  line-height: 2;
  flex-direction: column;
}

.hero-slogan i { width: 28px; height: 1px; margin-top: 6px; background: #cbd3df; }
.architecture { position: absolute; top: 16px; right: 116px; width: 410px; height: 160px; opacity: 0.36; transform: skewY(-3deg); }

.building {
  position: absolute;
  right: 0;
  bottom: -23px;
  border: 1px solid rgba(160, 186, 211, 0.48);
  background-color: rgba(237, 245, 252, 0.72);
  background-image: repeating-linear-gradient(90deg, rgba(143, 173, 201, 0.33) 0 1px, transparent 1px 13px), repeating-linear-gradient(0deg, rgba(173, 197, 218, 0.23) 0 1px, transparent 1px 16px);
  clip-path: polygon(14% 10%, 100% 0, 100% 100%, 0 100%, 0 26%);
  box-shadow: inset 0 0 42px rgba(255, 255, 255, 0.92);
}

.building-back { right: 154px; width: 188px; height: 143px; opacity: 0.75; transform: skewX(-10deg); }
.building-front { width: 260px; height: 118px; background-image: repeating-linear-gradient(90deg, rgba(105, 148, 186, 0.4) 0 3px, transparent 3px 22px), repeating-linear-gradient(0deg, rgba(171, 196, 219, 0.32) 0 1px, transparent 1px 18px); transform: skewX(-7deg); }

.function-grid {
  position: relative;
  z-index: 3;
  display: grid;
  margin: 8px 0 24px;
  grid-template-columns: repeat(5, minmax(170px, 1fr));
  gap: 16px;
}

.function-card {
  --theme: #e64b57;
  --theme-rgb: 230, 75, 87;
  position: relative;
  min-height: 326px;
  padding: 28px 24px 26px;
  overflow: hidden;
  color: $ink;
  text-align: left;
  background: linear-gradient(155deg, #fff 12%, rgba(var(--theme-rgb), 0.035) 100%);
  border: 1px solid $line;
  border-radius: 9px;
  box-shadow: 0 7px 24px rgba(27, 46, 72, 0.045);
  cursor: pointer;
  transition: transform 0.2s ease, border-color 0.2s ease, box-shadow 0.2s ease;
}

.function-card:hover,
.function-card:focus-visible { border-color: rgba(var(--theme-rgb), 0.35); box-shadow: 0 14px 30px rgba(28, 47, 75, 0.11); outline: none; transform: translateY(-4px); }
.theme-blue { --theme: #347ed5; --theme-rgb: 52, 126, 213; }
.theme-amber { --theme: #bd7b18; --theme-rgb: 189, 123, 24; }
.theme-green { --theme: #07966a; --theme-rgb: 7, 150, 106; }
.theme-violet { --theme: #7841cf; --theme-rgb: 120, 65, 207; }

.function-icon { position: relative; z-index: 2; display: flex; width: 72px; height: 72px; color: var(--theme); background: rgba(var(--theme-rgb), 0.11); border-radius: 12px; align-items: center; justify-content: center; }
.function-icon :deep(.el-icon) { font-size: 39px; }
.function-card h2 { position: relative; z-index: 2; margin: 23px 0 18px; font-size: 21px; font-weight: 700; letter-spacing: 0.5px; }
.function-card p { position: relative; z-index: 2; margin: 0; color: #718097; font-size: 14px; line-height: 1.75; }
.function-card p span { display: block; }

.enter-button { position: absolute; z-index: 3; bottom: 42px; left: 24px; display: flex; width: 43px; height: 43px; color: var(--theme); background: rgba(var(--theme-rgb), 0.11); border-radius: 50%; align-items: center; justify-content: center; transition: color 0.2s ease, background 0.2s ease, transform 0.2s ease; }
.enter-button :deep(.el-icon) { font-size: 22px; }
.function-card:hover .enter-button { color: #fff; background: var(--theme); transform: translateX(4px); }

.card-wave { position: absolute; right: -36px; bottom: -75px; width: 130%; height: 145px; background: rgba(var(--theme-rgb), 0.075); border-radius: 50% 50% 0 0 / 38% 38% 0 0; transform: rotate(-7deg); }
.card-wave::after { position: absolute; top: -16px; left: -15%; width: 120%; height: 100%; background: rgba(255, 255, 255, 0.52); border-radius: 50% 50% 0 0 / 38% 38% 0 0; content: ''; }

.workbench-footer { display: flex; min-height: 54px; margin: auto -34px 0; padding: 0 42px; color: #8b96a8; font-size: 11px; border-top: 1px solid #e8edf3; align-items: center; }
.workbench-footer > i { width: 1px; height: 12px; margin: 0 12px; background: #d8dee7; }
.footer-values { display: flex; margin-left: auto; gap: 18px; }

@media (max-width: 1280px) {
  .lease-workbench { padding-right: 24px; padding-left: 24px; }
  .function-grid { gap: 11px; }
  .function-card { min-height: 300px; padding-right: 18px; padding-left: 18px; }
  .enter-button { left: 18px; }
  .hero-slogan { display: none; }
  .workbench-footer { margin-right: -24px; margin-left: -24px; }
}

@media (max-width: 1050px) {
  .function-grid { grid-template-columns: repeat(3, 1fr); }
  .function-card { min-height: 286px; }
}

@media (max-width: 760px) {
  .lease-workbench { min-height: 100%; padding: 0 16px; overflow: visible; }
  .workbench-hero { min-height: 164px; padding-top: 34px; }
  .hero-copy h1 { font-size: 28px; }
  .hero-copy p { max-width: 88%; line-height: 1.7; }
  .architecture { right: -150px; opacity: 0.22; }
  .function-grid { grid-template-columns: 1fr; }
  .function-card { min-height: 260px; }
  .workbench-footer { margin-right: -16px; margin-left: -16px; padding: 0 20px; }
  .footer-values { display: none; }
}
</style>
