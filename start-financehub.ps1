[CmdletBinding()]
param(
    [int]$StartupTimeoutSeconds = 90
)

$ErrorActionPreference = 'Stop'

$ProjectRoot = $PSScriptRoot
$RuntimeRoot = 'D:\local'
$LogRoot = Join-Path $ProjectRoot 'runtime-logs'
$LocalConfigRoot = Join-Path $ProjectRoot '.local'
$Timestamp = Get-Date -Format 'yyyyMMdd-HHmmss'

New-Item -ItemType Directory -Path $LogRoot -Force | Out-Null

function Assert-Path {
    param(
        [Parameter(Mandatory = $true)][string]$Path,
        [Parameter(Mandatory = $true)][string]$Description
    )

    if (-not (Test-Path -LiteralPath $Path)) {
        throw "$Description not found: $Path"
    }
}

function Test-TcpPort {
    param([Parameter(Mandatory = $true)][int]$Port)

    $client = [System.Net.Sockets.TcpClient]::new()
    try {
        $connectTask = $client.ConnectAsync('127.0.0.1', $Port)
        return $connectTask.Wait(500) -and $client.Connected
    }
    catch {
        return $false
    }
    finally {
        $client.Dispose()
    }
}

function Wait-TcpPort {
    param(
        [Parameter(Mandatory = $true)][string]$Name,
        [Parameter(Mandatory = $true)][int]$Port,
        [Parameter(Mandatory = $true)][System.Diagnostics.Process]$Process,
        [Parameter(Mandatory = $true)][string]$StdOutPath,
        [Parameter(Mandatory = $true)][string]$StdErrPath
    )

    $deadline = (Get-Date).AddSeconds($StartupTimeoutSeconds)
    while ((Get-Date) -lt $deadline) {
        if (Test-TcpPort -Port $Port) {
            Write-Host "[OK] $Name is ready on port $Port (PID $($Process.Id))." -ForegroundColor Green
            return
        }

        if ($Process.HasExited) {
            Write-Host "[ERROR] $Name exited before port $Port was ready." -ForegroundColor Red
            if (Test-Path -LiteralPath $StdOutPath) {
                Get-Content -LiteralPath $StdOutPath -Tail 50
            }
            if (Test-Path -LiteralPath $StdErrPath) {
                Get-Content -LiteralPath $StdErrPath -Tail 50
            }
            throw "$Name failed to start. See logs under $LogRoot."
        }

        Start-Sleep -Seconds 2
        $Process.Refresh()
    }

    throw "$Name did not open port $Port within $StartupTimeoutSeconds seconds. See logs under $LogRoot."
}

function Start-ManagedProcess {
    param(
        [Parameter(Mandatory = $true)][string]$Name,
        [Parameter(Mandatory = $true)][int]$Port,
        [Parameter(Mandatory = $true)][string]$FilePath,
        [Parameter(Mandatory = $true)][string[]]$ArgumentList,
        [Parameter(Mandatory = $true)][string]$WorkingDirectory,
        [Parameter(Mandatory = $true)][string]$LogPrefix
    )

    if (Test-TcpPort -Port $Port) {
        $listener = Get-NetTCPConnection -State Listen -LocalPort $Port -ErrorAction SilentlyContinue | Select-Object -First 1
        $pidText = if ($listener) { $listener.OwningProcess } else { 'unknown' }
        Write-Host "[SKIP] $Name is already listening on port $Port (PID $pidText)." -ForegroundColor Yellow
        return
    }

    Assert-Path -Path $FilePath -Description "$Name executable"
    Assert-Path -Path $WorkingDirectory -Description "$Name working directory"

    $stdout = Join-Path $LogRoot "$LogPrefix-$Timestamp.out.log"
    $stderr = Join-Path $LogRoot "$LogPrefix-$Timestamp.err.log"
    Write-Host "[START] $Name..."
    $process = Start-Process `
        -FilePath $FilePath `
        -ArgumentList $ArgumentList `
        -WorkingDirectory $WorkingDirectory `
        -RedirectStandardOutput $stdout `
        -RedirectStandardError $stderr `
        -WindowStyle Hidden `
        -PassThru

    Wait-TcpPort -Name $Name -Port $Port -Process $process -StdOutPath $stdout -StdErrPath $stderr
}

$Java8 = Join-Path $RuntimeRoot 'jdk8\bin\java.exe'
$JavaModern = 'C:\Program Files\Eclipse Adoptium\jdk-25.0.3.9-hotspot\bin\java.exe'
if (-not (Test-Path -LiteralPath $JavaModern)) {
    $JavaModern = $Java8
}

$MySqlExe = Join-Path $RuntimeRoot 'mysql\bin\mysqld.exe'
$MySqlConfig = Join-Path $RuntimeRoot 'mysql-my.ini'
$RedisExe = Join-Path $RuntimeRoot 'redis\Redis-8.8.0-Windows-x64-msys2\redis-server.exe'
$RedisConfig = '/cygdrive/d/local/redis.conf'
$NacosHome = Join-Path $RuntimeRoot 'nacos'
$NacosJar = Join-Path $NacosHome 'target\nacos-server.jar'

$GatewayJar = Join-Path $ProjectRoot 'code\financehub-gateway\target\financehub-gateway.jar'
$AuthJar = Join-Path $ProjectRoot 'code\financehub-auth\target\financehub-auth.jar'
$AdminJar = Join-Path $ProjectRoot 'code\financehub-admin\financehub-admin-service\target\financehub-admin-service.jar'
$EngineJar = Join-Path $ProjectRoot 'code\financehub-engine\financehub-engine-service\target\financehub-engine-service.jar'
$EngineOverrides = Join-Path $LocalConfigRoot 'engine-local-overrides.properties'
$WebRoot = Join-Path $ProjectRoot 'code\financehub-web'
$NodeExe = Join-Path $RuntimeRoot 'node16\node.exe'
$ViteScript = Join-Path $WebRoot 'node_modules\vite\bin\vite.js'

$NacosArguments = @(
    '--spring.profiles.active=dev',
    '--spring.cloud.nacos.config.server-addr=127.0.0.1:8848',
    '--spring.cloud.nacos.config.namespace=financehub-local',
    '--spring.cloud.nacos.config.group=financehub-local',
    '--spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848',
    '--spring.cloud.nacos.discovery.namespace=financehub-local',
    '--spring.cloud.nacos.discovery.group=financehub-local',
    '--spring.cloud.nacos.discovery.ip=127.0.0.1'
)

try {
    Start-ManagedProcess -Name 'MySQL' -Port 3306 -FilePath $MySqlExe `
        -ArgumentList @("--defaults-file=$MySqlConfig", '--console') `
        -WorkingDirectory (Split-Path $MySqlExe) -LogPrefix 'mysql'

    Start-ManagedProcess -Name 'Redis' -Port 6379 -FilePath $RedisExe `
        -ArgumentList @($RedisConfig) `
        -WorkingDirectory (Split-Path $RedisExe) -LogPrefix 'redis'

    $nacosPluginPath = "$(Join-Path $NacosHome 'plugins'),$(Join-Path $NacosHome 'plugins\health'),$(Join-Path $NacosHome 'plugins\cmdb'),$(Join-Path $NacosHome 'plugins\selector')"
    Start-ManagedProcess -Name 'Nacos' -Port 8848 -FilePath $Java8 `
        -ArgumentList @(
            '-Xms512m', '-Xmx512m', '-Xmn256m',
            '-Dnacos.standalone=true',
            "-Dloader.path=$nacosPluginPath",
            "-Dnacos.home=$NacosHome",
            '-jar', $NacosJar,
            "--spring.config.additional-location=file:$NacosHome/conf/",
            "--logging.config=$NacosHome/conf/nacos-logback.xml",
            'nacos.nacos', '-m', 'standalone'
        ) `
        -WorkingDirectory $NacosHome -LogPrefix 'nacos'

    Start-ManagedProcess -Name 'FinanceHub Gateway' -Port 8888 -FilePath $Java8 `
        -ArgumentList (@('-jar', $GatewayJar) + $NacosArguments) `
        -WorkingDirectory (Split-Path $GatewayJar) -LogPrefix 'gateway'

    Start-ManagedProcess -Name 'FinanceHub Auth' -Port 8200 -FilePath $JavaModern `
        -ArgumentList (@('-jar', $AuthJar) + $NacosArguments) `
        -WorkingDirectory (Split-Path $AuthJar) -LogPrefix 'auth'

    $adminDatabaseUrl = 'jdbc:mysql://127.0.0.1:3306/financialdb4?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true'
    Start-ManagedProcess -Name 'FinanceHub Admin' -Port 8201 -FilePath $JavaModern `
        -ArgumentList (@(
            '-jar', $AdminJar,
            "--spring.datasource.dynamic.datasource.master.url=$adminDatabaseUrl",
            '--spring.datasource.dynamic.datasource.master.username=db01',
            '--spring.datasource.dynamic.datasource.master.password=1qaz@wsx'
        ) + $NacosArguments) `
        -WorkingDirectory (Split-Path $AdminJar) -LogPrefix 'admin'

    Start-ManagedProcess -Name 'FinanceHub Accounting Engine' -Port 8202 -FilePath $JavaModern `
        -ArgumentList (@(
            '-jar', $EngineJar,
            "--spring.config.additional-location=file:$EngineOverrides"
        ) + $NacosArguments) `
        -WorkingDirectory (Split-Path $EngineJar) -LogPrefix 'engine'

    Start-ManagedProcess -Name 'FinanceHub Web' -Port 8899 -FilePath $NodeExe `
        -ArgumentList @($ViteScript, '--mode', 'development') `
        -WorkingDirectory $WebRoot -LogPrefix 'web'

    Write-Host ''
    Write-Host 'FinanceHub is ready.' -ForegroundColor Green
    Write-Host 'URL:      http://localhost:8899/'
    Write-Host 'Username: admin'
    Write-Host 'Password: 123456'
    Write-Host "Logs:     $LogRoot"
}
catch {
    Write-Host ''
    Write-Host "Startup failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

