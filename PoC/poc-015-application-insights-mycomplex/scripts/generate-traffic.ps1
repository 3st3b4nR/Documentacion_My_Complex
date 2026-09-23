param(
    [string]$BaseUrl = "http://localhost:8086",
    [int]$Iterations = 10
)

Write-Host "Generando trafico para Application Insights..."

for ($i = 1; $i -le $Iterations; $i++) {
    try {
        Invoke-RestMethod "$BaseUrl/api/random" | Out-Null
        Write-Host "Solicitud $i OK"
    }
    catch {
        Write-Host "Solicitud $i fallo: $($_.Exception.Message)"
    }
}

Write-Host "Generando un error controlado..."

try {
    Invoke-WebRequest "$BaseUrl/api/error" -UseBasicParsing | Out-Null
}
catch {
    Write-Host "Error 500 esperado generado correctamente."
}

Write-Host "Finalizado. Espera unos minutos y revisa Application Insights."
