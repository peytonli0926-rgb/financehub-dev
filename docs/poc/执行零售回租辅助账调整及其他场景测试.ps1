[CmdletBinding()]
param([string]$BaseUrl = 'http://127.0.0.1:8202')

$ErrorActionPreference = 'Stop'
$stamp = Get-Date -Format 'MMddHHmmss'
$common = @{ businessDate='2026-09-09 10:00:00'; contractCode='HXZL-PC-202609-0001'; currency='CNY' }
$cases = [Collections.ArrayList]::new()
function Add-Case([string]$name,[string]$path,[string]$scene,[string]$event,[hashtable]$fields) {
    $body=$common.Clone(); $body.orderId="HXZL-PC-202609-0001-AO-$stamp-$($cases.Count+1)"; $body.eventCode=$event
    foreach($key in $fields.Keys){$body[$key]=$fields[$key]}
    [void]$cases.Add([pscustomobject]@{Name=$name;Path=$path;Scene=$scene;Body=$body})
}

Add-Case 'Lessee change' '/retail-leaseback/auxiliary-account-adjustment/execute' 'CYC_AUXILIARY_ADJUSTMENT' 'C020' @{
    originalClientCode='CUST-OLD-0001';originalClientName='Original lessee';principalBalance=1000;interestBalance=100;residualValueBalance=50;
    interestTaxBalance=6;residualValueTaxBalance=3;accruedInterestBalance=80;accruedResidualValueBalance=40;accruedInterestTaxBalance=4.8;accruedResidualValueTaxBalance=2.4
}
Add-Case 'Auxiliary dimension correction' '/retail-leaseback/auxiliary-account-adjustment/execute' 'CYC_AUXILIARY_ADJUSTMENT' 'CR048' @{
    unidentifiedReceiptAssistAmount=100;managementFeePayableAssistAmount=80;inputVatReceivableAssistAmount=6;unearnedInterestAssistAmount=50
}
Add-Case 'Internal fund transfer' '/retail-leaseback/other/execute' 'CYC_OTHER' 'C016' @{sourceBankAccountNo='1000000000000000005';targetBankAccountNo='1000000000000000006';transferAmount=1000}
Add-Case 'Position transfer' '/retail-leaseback/other/execute' 'CYC_OTHER' 'C037' @{sourceBankAccountNo='1000000000000000006';targetBankAccountNo='1000000000000000005';transferAmount=800}
Add-Case 'Quarterly interest - interbank' '/retail-leaseback/other/execute' 'CYC_OTHER' 'C038' @{targetBankAccountNo='1000000000000000005';quarterlyInterestAmount=106}
Add-Case 'Quarterly interest - deposit' '/retail-leaseback/other/execute' 'CYC_OTHER' 'C039' @{targetBankAccountNo='1000000000000000006';quarterlyInterestAmount=212}
Add-Case 'Overpaid profit sharing' '/retail-leaseback/other/execute' 'CYC_OTHER' 'C045' @{overpaidProfitSharingAmount=300}
Add-Case 'Incorrect amount correction' '/retail-leaseback/other/execute' 'CYC_OTHER' 'C046' @{channelShareAmount=106}
Add-Case 'Penalty sharing correction' '/retail-leaseback/other/execute' 'CYC_OTHER' 'C047' @{penaltyShareAdjustmentAmount=200}
Add-Case 'Promotion profit sharing difference' '/retail-leaseback/other/execute' 'CYC_OTHER' 'CR057' @{promotionInterestAmount=100;promotionInputVatAmount=6;promotionIncomeAmount=80;promotionProfitSharingAmount=26}

$results = foreach($case in $cases){
    try {
        $json=$case.Body|ConvertTo-Json -Depth 8
        $response=Invoke-RestMethod -Method Post -Uri ($BaseUrl+$case.Path) -ContentType 'application/json; charset=utf-8' -Body ([Text.Encoding]::UTF8.GetBytes($json))
        $vouchers=@($response.data);$entries=@($vouchers|ForEach-Object{@($_.entryList)});$balanced=$true
        foreach($voucher in $vouchers){$dr=[decimal](($voucher.entryList|Measure-Object debitAmount -Sum).Sum);$cr=[decimal](($voucher.entryList|Measure-Object creditAmount -Sum).Sum);if($dr-ne$cr){$balanced=$false}}
        [pscustomobject]@{Case=$case.Name;Passed=($response.code-eq 200-and$vouchers.Count-gt 0-and$entries.Count-ge 2-and$balanced-and@($vouchers|Where-Object{$_.sceneCode-ne$case.Scene-or$_.validFlag-ne'1'}).Count-eq 0);Message="vouchers=$($vouchers.Count), entries=$($entries.Count), balanced=$balanced"}
    } catch {[pscustomobject]@{Case=$case.Name;Passed=$false;Message=$_.ErrorDetails.Message}}
}

$negative=@(
 @{Name='Same transfer account';Path='/retail-leaseback/other/execute';Body=@{eventCode='C016';sourceBankAccountNo='1000000000000000005';targetBankAccountNo='1000000000000000005';transferAmount=100}},
 @{Name='Empty auxiliary amount';Path='/retail-leaseback/auxiliary-account-adjustment/execute';Body=@{eventCode='C020'}},
 @{Name='Unbalanced promotion difference';Path='/retail-leaseback/other/execute';Body=@{eventCode='CR057';promotionInterestAmount=100;promotionIncomeAmount=90}}
)
foreach($case in $negative){$body=$common.Clone();foreach($key in $case.Body.Keys){$body[$key]=$case.Body[$key]};$body.orderId="HXZL-PC-AO-NEG-$stamp-$($results.Count+1)";try{$response=Invoke-RestMethod -Method Post -Uri ($BaseUrl+$case.Path) -ContentType 'application/json; charset=utf-8' -Body ([Text.Encoding]::UTF8.GetBytes(($body|ConvertTo-Json -Depth 8)));$results+=[pscustomobject]@{Case=$case.Name;Passed=($response.code-ne 200);Message=$response.msg}}catch{$results+=[pscustomobject]@{Case=$case.Name;Passed=$true;Message=$_.ErrorDetails.Message}}}

$results|Format-Table -AutoSize -Wrap
$passed=@($results|Where-Object Passed).Count
Write-Host "RESULT: $passed/$($results.Count) passed"
if($passed-ne$results.Count){exit 1}
