Write-Host "Verificando Azure CLI..."
az version

if ($LASTEXITCODE -ne 0) {
    Write-Error "Azure CLI no está instalado o no está en PATH."
    exit 1
}

Write-Host ""
Write-Host "Verificando Docker..."
docker --version

if ($LASTEXITCODE -ne 0) {
    Write-Error "Docker no está instalado o no está en PATH."
    exit 1
}

Write-Host ""
Write-Host "Verificando sesión de Azure..."
az account show --output table

if ($LASTEXITCODE -ne 0) {
    Write-Error "Debes iniciar sesión con: az login"
    exit 1
}

Write-Host ""
Write-Host "Prerequisitos listos."
