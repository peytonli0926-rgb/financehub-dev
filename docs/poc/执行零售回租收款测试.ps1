[CmdletBinding()]
# 统一收款场景：覆盖全部来源事件及负向校验。
param(
    [string]$BaseUrl = 'http://127.0.0.1:8202'
)

$ErrorActionPreference = 'Stop'
$caseFile = Get-ChildItem -LiteralPath $PSScriptRoot -Filter '*.json' |
    Where-Object { Select-String -LiteralPath $_.FullName -SimpleMatch '/retail-leaseback/collection/execute' -Quiet } |
    Select-Object -First 1 -ExpandProperty FullName
if (-not $caseFile) { throw 'Retail leaseback collection case file was not found.' }
$suite = Get-Content -LiteralPath $caseFile -Raw -Encoding UTF8 | ConvertFrom-Json
$results = @()

foreach ($case in $suite.cases) {
    try {
        $body = $case.request | ConvertTo-Json -Depth 10
        $response = Invoke-RestMethod -Method Post `
            -Uri "$BaseUrl/retail-leaseback/collection/execute" `
            -ContentType 'application/json; charset=utf-8' `
            -Body ([System.Text.Encoding]::UTF8.GetBytes($body))
        if (-not $case.positive) {
            $responseText = $response | ConvertTo-Json -Depth 10 -Compress
            $passed = $response.code -ne 200 -and $responseText -like "*$($case.expectedError)*"
            $results += [pscustomobject]@{ Case = $case.name; Passed = $passed; Message = $response.msg }
            continue
        }

        if ($response.code -ne 200) {
            $results += [pscustomobject]@{ Case = $case.name; Passed = $false; Message = $response.msg }
            continue
        }

        $vouchers = @($response.data)
        $actualEntries = @(
            foreach ($voucher in $vouchers) {
                foreach ($entry in @($voucher.entryList)) {
                    $isDebit = [decimal]$entry.debitAmount -gt 0
                    $direction = if ($isDebit) { [string][char]0x501F } else { [string][char]0x8D37 }
                    $amount = if ($isDebit) { [decimal]$entry.debitAmount } else { [decimal]$entry.creditAmount }
                    '{0}{1}={2}' -f $direction, $entry.accountCode, $amount.ToString('0.##', [System.Globalization.CultureInfo]::InvariantCulture)
                }
            }
        )
        $missingEntries = @($case.expectedEntries | Where-Object { $_ -notin $actualEntries })
        $rawQuery = @{ pageNum = 1; pageSize = 10; orderId = $case.request.orderId } | ConvertTo-Json
        $rawResponse = Invoke-RestMethod -Method Post `
            -Uri "$BaseUrl/rule/interface/pageRawData" `
            -ContentType 'application/json; charset=utf-8' `
            -Body ([System.Text.Encoding]::UTF8.GetBytes($rawQuery))
        $raw = @($rawResponse.data.records | Where-Object { $_.orderId -eq $case.request.orderId }) | Select-Object -First 1
        $voucherLink = if ($raw) { Invoke-RestMethod -Method Get -Uri "$BaseUrl/rule/interface/getVoucherInterfaceDataId/$($raw.id)" }
        $visible = $null -ne $raw -and $raw.sceneCode -eq 'CYC_COLLECTION' -and $raw.messageStatus -eq 'SUCCESS' -and $null -ne $voucherLink.data
        $summariesPresent = @($vouchers | Where-Object { [string]::IsNullOrWhiteSpace($_.voucherSummary) }).Count -eq 0 -and
            @($vouchers.entryList | Where-Object { [string]::IsNullOrWhiteSpace($_.voucherSummary) }).Count -eq 0
        $passed = $response.code -eq 200 -and $vouchers.Count -gt 0 -and
            @($vouchers | Where-Object { $_.validFlag -ne '1' -or $_.sceneCode -ne 'CYC_COLLECTION' }).Count -eq 0 -and
            $missingEntries.Count -eq 0 -and $summariesPresent -and $visible
        $results += [pscustomobject]@{
            Case = $case.name
            Passed = $passed
            Message = "event=$($case.request.eventCode); entries=$($actualEntries -join ','); summaries=$summariesPresent; visible=$visible; rawId=$($raw.id)"
        }
    }
    catch {
        $errorBody = $_.ErrorDetails.Message
        if ($errorBody) {
            $errorBody = [System.Text.Encoding]::UTF8.GetString([System.Text.Encoding]::GetEncoding(28591).GetBytes($errorBody))
        }
        $passed = -not $case.positive -and $errorBody -like "*$($case.expectedError)*"
        $results += [pscustomobject]@{ Case = $case.name; Passed = $passed; Message = $errorBody }
    }
}

$results | Format-Table -AutoSize -Wrap
if ($results.Where({ -not $_.Passed }).Count -gt 0) { exit 1 }
