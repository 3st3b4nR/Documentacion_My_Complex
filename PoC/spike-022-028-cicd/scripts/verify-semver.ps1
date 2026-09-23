param([Parameter(Mandatory)][string]$Version)

$semver = '^v(0|[1-9]\d*)\.(0|[1-9]\d*)\.(0|[1-9]\d*)(?:-[0-9A-Za-z-]+(?:\.[0-9A-Za-z-]+)*)?(?:\+[0-9A-Za-z-]+(?:\.[0-9A-Za-z-]+)*)?$'
if ($Version -notmatch $semver) {
    throw "La etiqueta '$Version' no cumple SemVer con prefijo v."
}
Write-Output "Etiqueta SemVer válida: $Version"

