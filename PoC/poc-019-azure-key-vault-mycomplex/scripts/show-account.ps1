Write-Host "Cuenta Azure actual:"
az account show --output table

Write-Host ""
Write-Host "Usuario actual:"
az ad signed-in-user show `
  --query "{displayName:displayName,id:id,userPrincipalName:userPrincipalName}" `
  --output table
