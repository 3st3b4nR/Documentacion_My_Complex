param(
    [string]$ResourceGroup = $env:AZURE_RESOURCE_GROUP,
    [string]$AcrName = $env:AZURE_ACR_NAME
)

if (-not $ResourceGroup) {
    $ResourceGroup = "rg-mycomplex-poc"
}

if (-not $AcrName) {
    Write-Error "Falta AZURE_ACR_NAME."
    exit 1
}

Write-Host "Se eliminará el ACR:"
Write-Host $AcrName

$answer = Read-Host "Escribe ELIMINAR para continuar"

if ($answer -ne "ELIMINAR") {
    Write-Host "Cancelado."
    exit 0
}

az acr delete `
  --name $AcrName `
  --resource-group $ResourceGroup `
  --yes

Write-Host "ACR eliminado."
