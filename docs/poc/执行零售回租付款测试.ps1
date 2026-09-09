[CmdletBinding()]
param(
    [string]$BaseUrl = 'http://127.0.0.1:8202',
    [switch]$IncludeNegative
)

$ErrorActionPreference = 'Stop'
$caseFile = Get-ChildItem -LiteralPath $PSScriptRoot -Filter '*.json' |
    Where-Object { Select-String -LiteralPath $_.FullName -SimpleMatch '/retail-leaseback/payment/execute' -Quiet } |
    Select-Object -First 1 -ExpandProperty FullName
if (-not $caseFile) {
    throw 'Retail leaseback payment case file was not found.'
}
$suite = Get-Content -LiteralPath $caseFile -Raw -Encoding UTF8 | ConvertFrom-Json
$results = @()

foreach ($case in $suite.cases) {
    if (-not $case.positive -and -not $IncludeNegative) {
        continue
    }
    try {
        $body = $case.request | ConvertTo-Json -Depth 10
        $response = Invoke-RestMethod -Method Post `
            -Uri "$BaseUrl/retail-leaseback/payment/execute" `
            -ContentType 'application/json; charset=utf-8' `
            -Body ([System.Text.Encoding]::UTF8.GetBytes($body))
        if ($case.positive) {
            $vouchers = @($response.data)
            $invalidVouchers = @($vouchers | Where-Object { $_.validFlag -ne '1' })
            $wrongSceneVouchers = @($vouchers | Where-Object { $_.sceneCode -ne 'CYC_PAYMENT' })
            $actualEntries = @(
                foreach ($voucher in $vouchers) {
                    foreach ($entry in @($voucher.entryList)) {
                        $amount = if ([decimal]$entry.debitAmount -gt 0) { [decimal]$entry.debitAmount } else { [decimal]$entry.creditAmount }
                        $direction = if ([decimal]$entry.debitAmount -gt 0) { [string][char]0x501F } else { [string][char]0x8D37 }
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
            $rawDocument = @($rawResponse.data.records | Where-Object { $_.orderId -eq $case.request.orderId }) | Select-Object -First 1
            $voucherLink = if ($rawDocument) {
                Invoke-RestMethod -Method Get -Uri "$BaseUrl/rule/interface/getVoucherInterfaceDataId/$($rawDocument.id)"
            }
            $shownInMyDocuments = $null -ne $rawDocument `
                -and $rawDocument.sceneCode -eq 'CYC_PAYMENT' `
                -and $rawDocument.messageStatus -eq 'SUCCESS' `
                -and $rawDocument.messageContent.source_system -eq 'CYCXT' `
                -and $rawDocument.messageContent.customer_no -eq $case.request.clientCode `
                -and $rawDocument.contractCode -eq $case.request.contractCode `
                -and $null -ne $voucherLink.data
            $passed = $response.code -eq 200 -and $vouchers.Count -gt 0 -and $invalidVouchers.Count -eq 0 -and $wrongSceneVouchers.Count -eq 0 -and $missingEntries.Count -eq 0 -and $shownInMyDocuments
            $message = if ($passed) { "shared template; valid voucher; entries matched; visible in My Documents" } else { "code=$($response.code), scenes=$($vouchers.sceneCode -join ','), validFlags=$($vouchers.validFlag -join ','), missing=$($missingEntries -join ','), myDocuments=$shownInMyDocuments" }
        }
        else {
            $responseText = $response | ConvertTo-Json -Depth 10 -Compress
            $stableExpected = if ($case.expectedError -match '[^\x00-\x7F]') { $case.request.bankAccountNo } else { $case.expectedError }
            $passed = $response.code -ne 200 -and $responseText -like "*$stableExpected*"
            $message = $response.msg
        }
        $results += [pscustomobject]@{ Case = $case.name; Passed = $passed; Message = $message }
    }
    catch {
        $errorBody = $_.ErrorDetails.Message
        if ($errorBody) {
            # Windows PowerShell 5 may decode an UTF-8 HTTP error body as ISO-8859-1.
            $errorBody = [System.Text.Encoding]::UTF8.GetString(
                [System.Text.Encoding]::GetEncoding(28591).GetBytes($errorBody)
            )
        }
        $stableExpected = if ($case.expectedError -match '[^\x00-\x7F]') { $case.request.bankAccountNo } else { $case.expectedError }
        $passed = -not $case.positive -and $errorBody -like "*$stableExpected*"
        $results += [pscustomobject]@{ Case = $case.name; Passed = $passed; Message = $errorBody }
    }
}

if ($suite.lifecycleContract) {
    $lifecycleQuery = @{
        pageNum = 1
        pageSize = 100
        contractCode = $suite.lifecycleContract.contractCode
    } | ConvertTo-Json
    $lifecycleResponse = Invoke-RestMethod -Method Post `
        -Uri "$BaseUrl/rule/interface/pageRawData" `
        -ContentType 'application/json; charset=utf-8' `
        -Body ([System.Text.Encoding]::UTF8.GetBytes($lifecycleQuery))
    $lifecycleRows = @($lifecycleResponse.data.records)
    $expectedPaymentIds = @($suite.cases | Where-Object positive | ForEach-Object { $_.request.orderId })
    $actualOrderIds = @($lifecycleRows | ForEach-Object orderId)
    $missingLifecycleIds = @($expectedPaymentIds | Where-Object { $_ -notin $actualOrderIds })
    $leaseStartExists = $suite.lifecycleContract.leaseStartOrderId -in $actualOrderIds
    $masterDataMatches = @($lifecycleRows | Where-Object {
        $_.orderId -in $expectedPaymentIds -and (
            $_.contractCode -ne $suite.lifecycleContract.contractCode -or
            $_.messageContent.customer_no -ne $suite.lifecycleContract.clientCode
        )
    }).Count -eq 0
    $lifecyclePassed = $leaseStartExists -and $missingLifecycleIds.Count -eq 0 -and $masterDataMatches
    $results += [pscustomobject]@{
        Case = '完整合同生命周期链路'
        Passed = $lifecyclePassed
        Message = "contract=$($suite.lifecycleContract.contractCode); leaseStart=$leaseStartExists; paymentEvents=$($expectedPaymentIds.Count); missing=$($missingLifecycleIds -join ',')"
    }
}

$results | Format-Table -AutoSize
if ($results.Where({ -not $_.Passed }).Count -gt 0) {
    exit 1
}
