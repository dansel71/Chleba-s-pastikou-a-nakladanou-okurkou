$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
Set-Location -LiteralPath $projectRoot

$sources = Get-ChildItem -LiteralPath . -Filter "*.java" -File | ForEach-Object {
    $_.Name
}

if ($sources.Count -eq 0) {
    Write-Error "No Java source files found in $projectRoot"
    exit 1
}

& javac -encoding UTF-8 @sources
if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}

& java -cp ".;lib/sqlite-jdbc-3.51.3.0.jar" ConsoleApp
exit $LASTEXITCODE
