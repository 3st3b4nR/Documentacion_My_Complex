param(
    [string]$AcrName = $env:AZURE_ACR_NAME,
    [string]$Repository = $env:ACR_REPOSITORY
)

if (-not $AcrName) {
    Write-Error "Falta AZURE_ACR_NAME."
    exit 1
}

if (-not $Repository) {
    $Repository = "mycomplex-backend"
}

Write-Host "Repositorios:"
az acr repository list `
  --name $AcrName `
  --output table

Write-Host ""
Write-Host "Tags de $Repository:"
az acr repository show-tags `
  --name $AcrName `
  --repository $Repository `
  --orderby time_desc `
  --detail `
  --output table
