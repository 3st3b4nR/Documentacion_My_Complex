Write-Host "Verificando Azure CLI..."
az version

if ($LASTEXITCODE -ne 0) {
    Write-Error "Azure CLI no está instalado o no está disponible en PATH."
    exit 1
}

Write-Host ""
Write-Host "Verificando extensión containerapp..."

az extension add `
  --name containerapp `
  --upgrade `
  --only-show-errors

if ($LASTEXITCODE -ne 0) {
    Write-Error "No fue posible preparar la extensión containerapp."
    exit 1
}

Write-Host ""
Write-Host "Verificando sesión Azure..."

az account show --output table

if ($LASTEXITCODE -ne 0) {
    Write-Error "Debes iniciar sesión con: az login"
    exit 1
}

Write-Host ""
Write-Host "Prerequisitos básicos listos."
