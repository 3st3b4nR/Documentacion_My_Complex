param(
    [string]$ConfigPath = (Join-Path $PSScriptRoot '..\config.local.psd1')
)

$ErrorActionPreference = 'Stop'
$config = Import-PowerShellDataFile -LiteralPath $ConfigPath

$image = az acr repository show --name $config.RegistryName --image "$($config.ImageName):$($config.ImageTag)" --query name -o tsv
if (-not $image) { throw 'No se encontró la imagen versionada en ACR.' }

$revision = az containerapp revision list --resource-group $config.ResourceGroup --name $config.ContainerAppName --query "[?properties.active].name | [0]" -o tsv
if (-not $revision) { throw 'No existe una revisión activa en Container Apps.' }

$principal = az identity show --resource-group $config.ResourceGroup --name $config.IdentityName --query principalId -o tsv
$assignments = az role assignment list --assignee $principal --all --query "[].roleDefinitionName" -o tsv

Write-Output "Imagen ACR: $image"
Write-Output "Revisión activa: $revision"
Write-Output 'Roles de la identidad:'
Write-Output $assignments
Write-Output 'Complete las pruebas negativas de permisos, firewall, TLS y restauración en docs/evidence-checklist.md.'
