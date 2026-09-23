param(
    [string]$BaseUrl = "http://localhost:8088",
    [string]$SecretName = "resend-api-key-poc",
    [string]$SecretValue = "valor-falso-solo-para-la-poc"
)

Write-Host "1. Health..."
Invoke-RestMethod "$BaseUrl/health"

Write-Host ""
Write-Host "2. Guardando secreto..."

$body = @{
    name = $SecretName
    value = $SecretValue
} | ConvertTo-Json

$created = Invoke-RestMethod `
  -Method Post `
  -Uri "$BaseUrl/api/secrets" `
  -ContentType "application/json" `
  -Body $body

$created | ConvertTo-Json

Write-Host ""
Write-Host "3. Consultando metadata..."

Invoke-RestMethod `
  "$BaseUrl/api/secrets/$SecretName/metadata" |
  ConvertTo-Json

Write-Host ""
Write-Host "4. Verificando valor sin exponerlo en la respuesta..."

$verifyBody = @{
    expectedValue = $SecretValue
} | ConvertTo-Json

Invoke-RestMethod `
  -Method Post `
  -Uri "$BaseUrl/api/secrets/$SecretName/verify" `
  -ContentType "application/json" `
  -Body $verifyBody |
  ConvertTo-Json
