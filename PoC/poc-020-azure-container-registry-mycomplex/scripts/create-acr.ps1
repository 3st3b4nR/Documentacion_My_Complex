param(
    [string]$ResourceGroup = $env:AZURE_RESOURCE_GROUP,
    [string]$Location = $env:AZURE_LOCATION,
    [string]$AcrName = $env:AZURE_ACR_NAME
)

if (-not $ResourceGroup) {
    $ResourceGroup = "rg-mycomplex-poc"
}

if (-not $Location) {
    $Location = "eastus"
}

if (-not $AcrName) {
    $suffix = Get-Random -Minimum 1000 -Maximum 9999
    $AcrName = "mycomplexacr$suffix"
}

Write-Host "Creando Resource Group..."
az group create `
  --name $ResourceGroup `
  --location $Location `
  --output table

if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

Write-Host "Creando Azure Container Registry..."
az acr create `
  --resource-group $ResourceGroup `
  --name $AcrName `
  --sku Basic `
  --admin-enabled false `
  --output table

if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

$loginServer = az acr show `
  --name $AcrName `
  --resource-group $ResourceGroup `
  --query loginServer `
  --output tsv

Write-Host ""
Write-Host "ACR creado:"
Write-Host $AcrName
Write-Host ""
Write-Host "Login Server:"
Write-Host $loginServer
Write-Host ""
Write-Host "Guarda este nombre como AZURE_ACR_NAME si deseas reutilizarlo."
