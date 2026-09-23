param(
    [string]$BaseUrl = "http://localhost:8087"
)

Write-Host "Probando health..."
Invoke-RestMethod "$BaseUrl/health"

Write-Host ""
Write-Host "Probando información del contenedor..."
Invoke-RestMethod "$BaseUrl/api/info"
