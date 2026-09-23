param(
    [string]$ImageName = "mycomplex/poc-016-docker",
    [string]$Tag = "1.0.0"
)

Write-Host "Construyendo imagen Docker..."
docker build -t "${ImageName}:${Tag}" .

if ($LASTEXITCODE -ne 0) {
    Write-Error "La construcción de la imagen falló."
    exit $LASTEXITCODE
}

Write-Host ""
Write-Host "Imagen creada:"
docker images "${ImageName}:${Tag}"
