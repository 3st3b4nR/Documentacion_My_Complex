param(
    [string]$ImageName = "mycomplex/poc-016-docker",
    [string]$Tag = "1.0.0",
    [string]$ContainerName = "mycomplex-poc-016"
)

docker rm -f $ContainerName 2>$null | Out-Null

Write-Host "Iniciando contenedor..."

docker run --name $ContainerName `
    -p 8087:8080 `
    -e APP_ENVIRONMENT="docker" `
    "${ImageName}:${Tag}"

