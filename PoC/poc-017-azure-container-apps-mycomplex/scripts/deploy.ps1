param(
    [string]$ResourceGroup = $env:AZURE_RESOURCE_GROUP,
    [string]$Location = $env:AZURE_LOCATION,
    [string]$EnvironmentName = $env:AZURE_CONTAINERAPPS_ENV,
    [string]$AppName = $env:AZURE_CONTAINERAPP_NAME,
    [string]$Image = $env:CONTAINER_IMAGE
)

if (-not $ResourceGroup) {
    $ResourceGroup = "rg-mycomplex-poc"
}

if (-not $Location) {
    $Location = "eastus"
}

if (-not $EnvironmentName) {
    $EnvironmentName = "mycomplex-poc-env"
}

if (-not $AppName) {
    $AppName = "mycomplex-poc-017"
}

if (-not $Image) {
    Write-Error "Falta CONTAINER_IMAGE. Publica primero la imagen Docker en un registry accesible."
    exit 1
}

Write-Host "1. Registrando proveedores requeridos..."

az provider register `
  --namespace Microsoft.App `
  --wait `
  --only-show-errors

az provider register `
  --namespace Microsoft.OperationalInsights `
  --wait `
  --only-show-errors

Write-Host "2. Creando Resource Group..."

az group create `
  --name $ResourceGroup `
  --location $Location `
  --output table

if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

Write-Host "3. Creando Container Apps Environment si no existe..."

$environmentExists = az containerapp env show `
  --name $EnvironmentName `
  --resource-group $ResourceGroup `
  --query "name" `
  --output tsv `
  2>$null

if (-not $environmentExists) {
    az containerapp env create `
      --name $EnvironmentName `
      --resource-group $ResourceGroup `
      --location $Location `
      --output table

    if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
}
else {
    Write-Host "Environment ya existe: $EnvironmentName"
}

Write-Host "4. Creando Container App..."

$appExists = az containerapp show `
  --name $AppName `
  --resource-group $ResourceGroup `
  --query "name" `
  --output tsv `
  2>$null

if (-not $appExists) {
    az containerapp create `
      --name $AppName `
      --resource-group $ResourceGroup `
      --environment $EnvironmentName `
      --image $Image `
      --target-port 8080 `
      --ingress external `
      --min-replicas 0 `
      --max-replicas 2 `
      --env-vars APP_ENVIRONMENT=azure-container-apps SERVER_PORT=8080 `
      --output table

    if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
}
else {
    Write-Host "Container App ya existe. Actualizando imagen..."

    az containerapp update `
      --name $AppName `
      --resource-group $ResourceGroup `
      --image $Image `
      --set-env-vars APP_ENVIRONMENT=azure-container-apps SERVER_PORT=8080 `
      --output table

    if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
}

Write-Host ""
Write-Host "5. Obteniendo URL..."

$fqdn = az containerapp show `
  --name $AppName `
  --resource-group $ResourceGroup `
  --query "properties.configuration.ingress.fqdn" `
  --output tsv

Write-Host ""
Write-Host "PoC desplegada:"
Write-Host "https://$fqdn"
Write-Host ""
Write-Host "Health:"
Write-Host "https://$fqdn/health"
