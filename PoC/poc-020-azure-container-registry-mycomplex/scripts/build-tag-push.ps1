param(
    [string]$AcrName = $env:AZURE_ACR_NAME,
    [string]$Repository = $env:ACR_REPOSITORY,
    [string]$Tag = $env:IMAGE_TAG
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

if (-not $loginServer) {
    Write-Error "No se pudo obtener loginServer del ACR."
    exit 1
}

Write-Host "Iniciando sesión en ACR..."
az acr login --name $AcrName

if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

$localImage = "mycomplex/poc-020-acr:$Tag"
$remoteImage = "$loginServer/$Repository`:$Tag"

Write-Host ""
Write-Host "Construyendo imagen local:"
Write-Host $localImage

docker build `
  --build-arg APP_VERSION=$Tag `
  -t $localImage `
  .

if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

Write-Host ""
Write-Host "Etiquetando imagen:"
docker tag $localImage $remoteImage

if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

Write-Host ""
Write-Host "Publicando imagen en ACR:"
Write-Host $remoteImage

docker push $remoteImage

if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

Write-Host ""
Write-Host "Imagen publicada correctamente."
