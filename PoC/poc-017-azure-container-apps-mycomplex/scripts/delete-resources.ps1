param(
    [string]$ResourceGroup = $env:AZURE_RESOURCE_GROUP
)

if (-not $ResourceGroup) {
    $ResourceGroup = "rg-mycomplex-poc"
}

Write-Host "ATENCIÓN: esto elimina el Resource Group completo:"
Write-Host $ResourceGroup
Write-Host ""

$answer = Read-Host "Escribe ELIMINAR para continuar"

if ($answer -ne "ELIMINAR") {
    Write-Host "Cancelado."
    exit 0
}

az group delete `
  --name $ResourceGroup `
  --yes `
  --no-wait

Write-Host "Eliminación solicitada."
