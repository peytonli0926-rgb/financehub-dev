[CmdletBinding()]
param([string]$BaseUrl = 'http://127.0.0.1:8202')

$ErrorActionPreference = 'Stop'
$stamp = Get-Date -Format 'MMddHHmmss'
$common = @{
    businessDate = '2026-09-09 09:00:00'
    contractCode = 'HXZL-PC-202609-0001'
    currency = 'CNY'
    counterpartyName = '张华'
}
$positive = @()

function Add-Case([string]$name,[string]$path,[string]$scene,[string]$event,[hashtable]$amounts) {
    $body = $common.Clone()
    $body.orderId = "HXZL-PC-202609-0001-ADD-$stamp-$($positive.Count + 1)"
    $body.eventCode = $event
    foreach ($key in $amounts.Keys) { $body[$key] = $amounts[$key] }
    $script:positive += [pscustomobject]@{ Name=$name; Path=$path; Scene=$scene; Body=$body }
}

Add-Case '普通退款' '/retail-leaseback/refund/execute' 'CYC_REFUND' '退款' @{refundAmount=100;advanceReceiptAmount=100;bankAccountNo='1000000000000000005'}
Add-Case '保证金退款' '/retail-leaseback/refund/execute' 'CYC_REFUND' '保证金退款' @{refundAmount=200;depositAmount=200;bankAccountNo='1000000000000000005'}
Add-Case '未确认款退款' '/retail-leaseback/refund/execute' 'CYC_REFUND' '多收款退款' @{refundAmount=300;unidentifiedAmount=300;bankAccountNo='1000000000000000005'}
Add-Case '厂商贴息确认' '/retail-leaseback/subsidy-confirm/execute' 'CYC_SUBSIDY_CONFIRM' '厂商贴息确认' @{subsidyAmount=100;subsidyTaxAmount=6}
Add-Case '平台贴息确认' '/retail-leaseback/subsidy-confirm/execute' 'CYC_SUBSIDY_CONFIRM' '平台贴息确认' @{subsidyAmount=200;subsidyTaxAmount=12}
Add-Case '提前结清贴息冲回' '/retail-leaseback/subsidy-confirm/execute' 'CYC_SUBSIDY_CONFIRM' '提前结清贴息冲回' @{subsidyAmount=50;subsidyTaxAmount=3}
Add-Case '本金转逾期' '/retail-leaseback/overdue/execute' 'CYC_OVERDUE' '本金转逾期' @{overduePrincipalAmount=1000}
Add-Case '利息转逾期' '/retail-leaseback/overdue/execute' 'CYC_OVERDUE' '利息转逾期' @{overdueInterestAmount=100;overdueInterestTaxAmount=6}
Add-Case '留购价转逾期' '/retail-leaseback/overdue/execute' 'CYC_OVERDUE' '留购价转逾期' @{overdueResidualValueAmount=50;overdueResidualValueTaxAmount=3}
Add-Case '逾期罚息确认' '/retail-leaseback/overdue/execute' 'CYC_OVERDUE' '逾期罚息确认' @{penaltyInterestAmount=80;penaltyInterestTaxAmount=4.8}

$structureEvents = @('租金计划调整','租金信息变更','结清','起租后GPS加装','留购价反向','费用减免租金','费用减免留购价','天津车辆处置结清','提前留购','尾款调整租金','车辆处置','车辆买断')
for ($i=0; $i -lt $structureEvents.Count; $i++) {
    $value = if ($i % 2 -eq 0) { 100 + $i } else { -100 - $i }
    $field = if ($structureEvents[$i] -eq '起租后GPS加装') { 'gpsAdjustmentAmount' } elseif ($structureEvents[$i] -match '留购价|买断') { 'residualValueAdjustmentAmount' } else { 'principalAdjustmentAmount' }
    Add-Case $structureEvents[$i] '/retail-leaseback/transaction-structure-change/execute' 'JYJGBG' $structureEvents[$i] @{$field=$value}
}

$results = foreach ($case in $positive) {
    try {
        $json = $case.Body | ConvertTo-Json -Depth 8
        $response = Invoke-RestMethod -Method Post -Uri ($BaseUrl + $case.Path) -ContentType 'application/json; charset=utf-8' -Body ([Text.Encoding]::UTF8.GetBytes($json))
        $vouchers = @($response.data)
        $entries = @($vouchers | ForEach-Object { @($_.entryList) })
        $balanced = $true
        foreach ($voucher in $vouchers) {
            $dr = [decimal](($voucher.entryList | Measure-Object debitAmount -Sum).Sum)
            $cr = [decimal](($voucher.entryList | Measure-Object creditAmount -Sum).Sum)
            if ($dr -ne $cr) { $balanced = $false }
        }
        [pscustomobject]@{Case=$case.Name;Passed=($response.code -eq 200 -and $vouchers.Count -gt 0 -and $entries.Count -ge 2 -and $balanced -and @($vouchers | Where-Object {$_.sceneCode -ne $case.Scene -or $_.validFlag -ne '1'}).Count -eq 0);Message="scene=$($case.Scene), vouchers=$($vouchers.Count), entries=$($entries.Count), balanced=$balanced"}
    } catch {
        [pscustomobject]@{Case=$case.Name;Passed=$false;Message=$_.ErrorDetails.Message}
    }
}

$negative = @(
    @{Name='退款金额不平';Path='/retail-leaseback/refund/execute';Error='退款总金额必须等于';Body=@{eventCode='退款';refundAmount=100;advanceReceiptAmount=90;bankAccountNo='1000000000000000005'}},
    @{Name='未知贴息事件';Path='/retail-leaseback/subsidy-confirm/execute';Error='事件未配置值映射';Body=@{eventCode='未知贴息事件';subsidyAmount=100}},
    @{Name='逾期金额为空';Path='/retail-leaseback/overdue/execute';Error='必须大于0';Body=@{eventCode='本金转逾期'}},
    @{Name='结构变更金额为空';Path='/retail-leaseback/transaction-structure-change/execute';Error='至少填写一个';Body=@{eventCode='租金计划调整'}}
)
foreach ($case in $negative) {
    $body = $common.Clone(); foreach ($key in $case.Body.Keys) {$body[$key]=$case.Body[$key]}; $body.orderId="HXZL-PC-NEG-$stamp-$($results.Count+1)"
    try {
        $response=Invoke-RestMethod -Method Post -Uri ($BaseUrl+$case.Path) -ContentType 'application/json; charset=utf-8' -Body ([Text.Encoding]::UTF8.GetBytes(($body|ConvertTo-Json -Depth 8)))
        $text=$response|ConvertTo-Json -Depth 8 -Compress
        $results += [pscustomobject]@{Case=$case.Name;Passed=($response.code -ne 200);Message=$response.msg}
    } catch {
        $results += [pscustomobject]@{Case=$case.Name;Passed=$true;Message=$_.ErrorDetails.Message}
    }
}

$results | Format-Table -AutoSize -Wrap
$passed=@($results|Where-Object Passed).Count
Write-Host "RESULT: $passed/$($results.Count) passed"
if ($passed -ne $results.Count) { exit 1 }
