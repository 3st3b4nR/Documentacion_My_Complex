param(
    [Parameter(Mandatory=$true)]
    [string]$BaseUrl
)

$BaseUrl = $BaseUrl.TrimEnd("/")

Write-Host "Probando health..."
$health = Invoke-RestMethod "$BaseUrl/health"
$health | ConvertTo-Json

Write-Host ""
Write-Host "Probando info..."
$info = Invoke-RestMethod "$BaseUrl/api/info"
$info | ConvertTo-Json

Write-Host ""
Write-Host "Prueba finalizada correctamente."
