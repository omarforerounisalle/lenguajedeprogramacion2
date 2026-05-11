#Requires -RunAsAdministrator
<#
.SYNOPSIS
  Prioriza JDK Temurin 25 sobre OpenJDK 8 en el PATH del sistema y define JAVA_HOME (Machine).

.NOTAS
  Click derecho -> Ejecutar con PowerShell como administrador
  O desde Admin PS:  powershell -ExecutionPolicy Bypass -File .\fix-java-path-machine.ps1
#>

$ErrorActionPreference = 'Stop'

$jdkHome = 'C:\Program Files\Eclipse Adoptium\jdk-25.0.3.9-hotspot'
$jdkBin  = Join-Path $jdkHome 'bin'

if (-not (Test-Path (Join-Path $jdkBin 'java.exe'))) {
    Write-Error "No existe java.exe en $jdkBin. Ajusta `$jdkHome en este script."
}

[Environment]::SetEnvironmentVariable('JAVA_HOME', $jdkHome, 'Machine')

$machinePath = [Environment]::GetEnvironmentVariable('Path', 'Machine')
if ([string]::IsNullOrEmpty($machinePath)) { $machinePath = '' }

$parts = $machinePath -split ';' |
    Where-Object { $_ -and $_.Trim().Length -gt 0 }

$filtered = $parts | Where-Object {
    $_ -notmatch '\\java-1\.8\.0-openjdk' -and
    $_ -ne $jdkBin -and
    $_ -notmatch '\\jdk-25\.0\.3\.9-hotspot\\bin'
}

$newMachinePath = ($jdkBin + ';' + ($filtered -join ';')).Trim(';')
[Environment]::SetEnvironmentVariable('Path', $newMachinePath, 'Machine')

Write-Host "OK: JAVA_HOME (Machine) = $jdkHome"
Write-Host "OK: PATH sistema reordenado (JDK 25 primero; entradas Java 8 eliminadas del PATH de sistema)."
Write-Host ""
Write-Host "Cierra y vuelve a abrir Cursor/terminal y ejecuta: java -version"
