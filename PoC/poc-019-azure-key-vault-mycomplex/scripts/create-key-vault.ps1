param(
    [string]$ResourceGroup = "rg-mycomplex-poc",
    [string]$Location = "eastus",
    [string]$VaultName = ""
)

if (-not $VaultName) {
    $suffix = Get-Random -Minimum 1000 -Maximum 9999
    $VaultName = "kv-mycomplex-poc-$suffix"
}

Write-Host "Creando Resource Group..."
az group create `
  --name $ResourceGroup `
  --location $Location `
  --output table

if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

Write-Host "Creando Key Vault..."
az keyvault create `
  --name $VaultName `
  --resource-group $ResourceGroup `
  --location $Location `
  --enable-rbac-authorization true `
  --output table

if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

Write-Host ""
Write-Host "Key Vault creado:"
Write-Host $VaultName
Write-Host ""
Write-Host "URL:"
Write-Host "https://$VaultName.vault.azure.net/"
Write-Host ""
Write-Host "Guarda esa URL en AZURE_KEY_VAULT_URL."
