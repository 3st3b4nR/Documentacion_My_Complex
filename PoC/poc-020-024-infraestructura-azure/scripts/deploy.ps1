param(
    [string]$ConfigPath = (Join-Path $PSScriptRoot '..\config.local.psd1')
)

$ErrorActionPreference = 'Stop'
if (-not (Test-Path -LiteralPath $ConfigPath)) {
    throw 'Cree config.local.psd1 a partir de config.example.psd1.'
}

$config = Import-PowerShellDataFile -LiteralPath $ConfigPath
$required = 'SubscriptionId','Location','ResourceGroup','RegistryName','EnvironmentName','IdentityName','ContainerAppName','ImageName','ImageTag','KeyVaultName','StorageAccount','BlobContainer'
foreach ($name in $required) {
    if ([string]::IsNullOrWhiteSpace($config[$name]) -or $config[$name] -match '^<') {
        throw "Falta configurar $name."
    }
}

az account set --subscription $config.SubscriptionId
az group create --name $config.ResourceGroup --location $config.Location --output none
az acr create --resource-group $config.ResourceGroup --name $config.RegistryName --sku Basic --admin-enabled false --output none
az acr build --registry $config.RegistryName --image "$($config.ImageName):$($config.ImageTag)" .

$identity = az identity create `
    --resource-group $config.ResourceGroup `
    --name $config.IdentityName `
    --location $config.Location | ConvertFrom-Json
$identityId = $identity.id
$principalId = $identity.principalId

$acrId = az acr show --resource-group $config.ResourceGroup --name $config.RegistryName --query id -o tsv
az role assignment create --assignee-object-id $principalId --assignee-principal-type ServicePrincipal --role AcrPull --scope $acrId --output none

az containerapp env create --resource-group $config.ResourceGroup --name $config.EnvironmentName --location $config.Location --output none
az containerapp create `
    --resource-group $config.ResourceGroup `
    --name $config.ContainerAppName `
    --environment $config.EnvironmentName `
    --image "$($config.RegistryName).azurecr.io/$($config.ImageName):$($config.ImageTag)" `
    --registry-server "$($config.RegistryName).azurecr.io" `
    --registry-identity $identityId `
    --user-assigned $identityId `
    --ingress external `
    --target-port 8080 `
    --output none

az keyvault create --resource-group $config.ResourceGroup --name $config.KeyVaultName --location $config.Location --enable-rbac-authorization true --output none
$vaultId = az keyvault show --resource-group $config.ResourceGroup --name $config.KeyVaultName --query id -o tsv
az role assignment create --assignee-object-id $principalId --assignee-principal-type ServicePrincipal --role 'Key Vault Secrets User' --scope $vaultId --output none

az storage account create --resource-group $config.ResourceGroup --name $config.StorageAccount --location $config.Location --sku Standard_LRS --allow-blob-public-access false --output none
$storageId = az storage account show --resource-group $config.ResourceGroup --name $config.StorageAccount --query id -o tsv
az role assignment create --assignee-object-id $principalId --assignee-principal-type ServicePrincipal --role 'Storage Blob Data Contributor' --scope $storageId --output none
az storage container create --account-name $config.StorageAccount --name $config.BlobContainer --auth-mode login --output none

Write-Output 'Recursos base creados. Configure PostgreSQL y su firewall de acuerdo con el checklist antes de verificar.'
