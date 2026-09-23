param(
    [string]$AcrName = $env:AZURE_ACR_NAME,
    [string]$Repository = $env:ACR_REPOSITORY,
    [string]$Tag = $env:IMAGE_TAG,
    [string]$ContainerName = "mycomplex-poc-020"
)

if (-not $AcrName) {
    Write-Error "Falta AZURE_ACR_NAME."
    exit 1
}

if (-not $Repository) {
    $Repository = "mycomplex-backend"
}

if (-not $Tag) {
    $Tag = "1.0.0"
}

$loginServer = az acr show `
  --name $AcrName `
  --query loginServer `
  --output tsv

$image = "$loginServer/$Repository`:$Tag"

Write-Host "Login..."
az acr login --name $AcrName

Write-Host ""
Write-Host "Descargando:"
docker pull $image

if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

docker rm -f $ContainerName 2>$null | Out-Null

Write-Host ""
Write-Host "Ejecutando imagen descargada desde ACR..."

docker run `
  --name $ContainerName `
  -p 8089:8080 `
  -e APP_VERSION=$Tag `
  $image
