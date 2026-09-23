param(
    [string]$ResourceGroup = $env:AZURE_RESOURCE_GROUP,
    [string]$AppName = $env:AZURE_CONTAINERAPP_NAME
)

if (-not $ResourceGroup) { $ResourceGroup = "rg-mycomplex-poc" }
if (-not $AppName) { $AppName = "mycomplex-poc-017" }

Write-Host "Container App:"
az containerapp show `
  --name $AppName `
  --resource-group $ResourceGroup `
  --query "{name:name,fqdn:properties.configuration.ingress.fqdn,provisioningState:properties.provisioningState,latestRevision:properties.latestRevisionName}" `
  --output table

Write-Host ""
Write-Host "Réplicas:"
az containerapp replica list `
  --name $AppName `
  --resource-group $ResourceGroup `
  --output table
