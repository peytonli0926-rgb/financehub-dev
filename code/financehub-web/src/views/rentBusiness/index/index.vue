<template>
  <div class="lease-workbench">
    <header class="page-header">
      <div class="header-copy">
        <div class="kicker"><span></span> 租赁业务</div>
        <h1>业务处理工作台</h1>
        <p>围绕收款和月末核算集中办理华夏金租核心业务</p>
      </div>
      <div class="header-summary">
        <strong>6</strong>
        <span>项核心功能</span>
      </div>
    </header>

    <main class="process-list">
      <section v-for="stream in workstreams" :key="stream.title" class="process-section">
        <div class="section-intro">
          <div class="section-icon">{{ stream.icon }}</div>
          <div>
            <span class="section-label">{{ stream.label }}</span>
            <h2>{{ stream.title }}</h2>
            <p>{{ stream.description }}</p>
          </div>
        </div>

        <div class="process-flow">
          <template v-for="(item, index) in stream.items" :key="item.path">
            <button type="button" class="process-card" @click="openFunction(item.path)">
              <div class="card-heading">
                <span class="step-number">{{ item.step }}</span>
                <span class="enter-link">进入办理 <b>→</b></span>
              </div>
              <div class="card-icon">{{ item.icon }}</div>
              <h3>{{ item.title }}</h3>
              <p>{{ item.description }}</p>
            </button>
            <div v-if="index < stream.items.length - 1" class="flow-arrow" aria-hidden="true">→</div>
          </template>
        </div>
      </section>
    </main>

    <div class="workbench-note">
      <span>业务办理结果统一进入凭证处理流程</span>
      <span class="note-divider"></span>
      <span>数据口径以财务中台为准</span>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from '@toystory/lotso'

const { router } = useRouter()

const workstreams = [
  {
    label: '日常业务',
    title: '收款处理',
    description: '从来款认领到业务核销，完整跟踪收款处理进度',
    icon: '收',
    items: [
      { step: '01', title: '未确认收款', description: '核对银行来款并完成业务认领', path: '/rentBusiness/nonConfirmCollectionSum', icon: '款' },
      { step: '02', title: '核销', description: '办理租赁业务核销及凭证处理', path: '/rentBusiness/verification', icon: '核' },
      { step: '03', title: '核销回款', description: '查询与跟踪核销回款明细', path: '/rentBusiness/verificationIncome', icon: '回' }
    ]
  },
  {
    label: '月末业务',
    title: '核算处理',
    description: '完成收益、税费和科目余额调整，保障月末核算准确',
    icon: '算',
    items: [
      { step: '01', title: '收益计提', description: '生成并管理租赁收益计提结果', path: '/measurementEngine/incomeProvision', icon: '益' },
      { step: '02', title: '税费', description: '办理增值税对账、转出及退回', path: '/rentBusiness/valueAddedTax', icon: '税' },
      { step: '03', title: '尾差调整', description: '处理合同科目余额尾差并生成凭证', path: '/rentBusiness/endAdjust', icon: '差' }
    ]
  }
]

const openFunction = (path) => router.push(path)
</script>

<style scoped lang="scss">
$red: #d70d18;
$dark: #252b35;
$muted: #77808e;
$line: #e7eaf0;

.lease-workbench {
  min-height: 100%;
  padding: 20px 24px 26px;
  color: $dark;
  background: #f4f6f8;
}

.page-header {
  position: relative;
  display: flex;
  min-height: 112px;
  padding: 24px 30px;
  overflow: hidden;
  background: #fff;
  border: 1px solid $line;
  border-radius: 8px;
  box-shadow: 0 3px 12px rgba(31, 38, 50, 0.04);
  align-items: center;
  justify-content: space-between;
}

.page-header::after {
  position: absolute;
  top: 0;
  right: 0;
  width: 210px;
  height: 100%;
  background: linear-gradient(135deg, transparent 38%, rgba(215, 13, 24, 0.045));
  content: '';
}

.header-copy { position: relative; z-index: 1; }
.kicker { display: flex; color: $red; font-size: 12px; font-weight: 600; letter-spacing: 1px; align-items: center; }
.kicker span { width: 18px; height: 3px; margin-right: 8px; background: $red; }
.page-header h1 { margin: 8px 0 5px; font-size: 25px; font-weight: 600; letter-spacing: 1px; }
.page-header p { margin: 0; color: $muted; font-size: 13px; }

.header-summary {
  position: relative;
  z-index: 1;
  display: flex;
  min-width: 138px;
  padding: 10px 18px;
  background: #fff7f7;
  border-left: 3px solid $red;
  border-radius: 4px;
  align-items: baseline;
}

.header-summary strong { margin-right: 8px; color: $red; font-size: 30px; line-height: 1; }
.header-summary span { color: #656d79; font-size: 12px; }
.process-list { display: grid; margin-top: 14px; gap: 14px; }

.process-section {
  display: grid;
  min-height: 236px;
  padding: 22px;
  background: #fff;
  border: 1px solid $line;
  border-radius: 8px;
  grid-template-columns: 235px minmax(0, 1fr);
  gap: 24px;
  align-items: stretch;
}

.section-intro {
  display: flex;
  padding: 18px 20px;
  background: #fafbfc;
  border-radius: 7px;
  flex-direction: column;
  justify-content: center;
}

.section-icon {
  display: flex;
  width: 42px;
  height: 42px;
  margin-bottom: 20px;
  color: #fff;
  font-size: 18px;
  font-weight: 600;
  background: $red;
  border-radius: 6px;
  align-items: center;
  justify-content: center;
}

.section-label { color: $red; font-size: 11px; font-weight: 600; letter-spacing: 1px; }
.section-intro h2 { margin: 6px 0 9px; font-size: 20px; font-weight: 600; }
.section-intro p { margin: 0; color: $muted; font-size: 12px; line-height: 1.7; }
.process-flow { display: flex; min-width: 0; align-items: stretch; }

.process-card {
  display: flex;
  min-width: 0;
  padding: 18px 19px;
  color: inherit;
  text-align: left;
  background: #fff;
  border: 1px solid $line;
  border-radius: 7px;
  cursor: pointer;
  transition: border-color 0.18s ease, box-shadow 0.18s ease, transform 0.18s ease;
  flex: 1;
  flex-direction: column;
}

.process-card:hover { border-color: #e7959a; box-shadow: 0 7px 18px rgba(37, 43, 53, 0.08); transform: translateY(-2px); }
.card-heading { display: flex; margin-bottom: 18px; align-items: center; justify-content: space-between; }
.step-number { color: #a5abb5; font-size: 11px; font-weight: 600; letter-spacing: 1px; }
.enter-link { color: #9299a4; font-size: 11px; opacity: 0; transition: opacity 0.18s ease; }
.enter-link b { color: $red; font-size: 15px; font-weight: 400; }
.process-card:hover .enter-link { opacity: 1; }

.card-icon {
  display: flex;
  width: 38px;
  height: 38px;
  margin-bottom: 14px;
  color: $red;
  font-size: 16px;
  font-weight: 600;
  background: #fff1f2;
  border-radius: 8px;
  align-items: center;
  justify-content: center;
}

.process-card h3 { margin: 0 0 8px; font-size: 15px; font-weight: 600; }
.process-card p { margin: 0; color: $muted; font-size: 12px; line-height: 1.55; }
.flow-arrow { display: flex; width: 34px; color: #c7cbd2; font-size: 19px; align-items: center; justify-content: center; }

.workbench-note {
  display: flex;
  margin-top: 14px;
  padding: 2px 4px;
  color: #9399a3;
  font-size: 11px;
  align-items: center;
  justify-content: flex-end;
}

.note-divider { width: 1px; height: 11px; margin: 0 12px; background: #d8dbe0; }

@media (max-width: 1200px) {
  .process-section { grid-template-columns: 190px minmax(0, 1fr); gap: 16px; }
  .process-card { padding: 16px; }
  .flow-arrow { width: 24px; }
}

@media (max-width: 900px) {
  .lease-workbench { padding: 14px; }
  .process-section { grid-template-columns: 1fr; }
  .section-intro { flex-direction: row; align-items: center; justify-content: flex-start; }
  .section-icon { margin: 0 14px 0 0; }
  .process-flow { display: grid; gap: 10px; }
  .flow-arrow { display: none; }
}
</style>
