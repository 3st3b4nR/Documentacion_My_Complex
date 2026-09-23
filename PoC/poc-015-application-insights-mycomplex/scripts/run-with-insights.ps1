param(
    [string]$AgentPath = ".\applicationinsights-agent.jar"
)

if (-not $env:APPLICATIONINSIGHTS_CONNECTION_STRING) {
    Write-Error "Falta APPLICATIONINSIGHTS_CONNECTION_STRING"
    exit 1
}

if (-not (Test-Path $AgentPath)) {
    Write-Error "No se encontro el Java Agent en: $AgentPath"
    Write-Host "Descarga el Application Insights Java Agent y renombralo como applicationinsights-agent.jar"
    exit 1
}

Write-Host "Compilando proyecto..."
mvn clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Error "La compilacion fallo."
    exit $LASTEXITCODE
}

$jar = Get-ChildItem ".\target\*.jar" |
    Where-Object { $_.Name -notlike "*.original" } |
    Select-Object -First 1

if (-not $jar) {
    Write-Error "No se encontro el JAR generado."
    exit 1
}

Write-Host "Ejecutando MyComplex PoC con Application Insights..."
java "-javaagent:$AgentPath" -jar $jar.FullName
