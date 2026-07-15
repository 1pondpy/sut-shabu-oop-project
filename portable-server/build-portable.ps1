param(
  # Default to the parent folder of this script (project root: ...\sut-shabu)
  [string]$ProjectRoot = (Resolve-Path (Join-Path $PSScriptRoot "..")).Path,
  # Default output folder is the script folder itself (...\sut-shabu\portable-server)
  [string]$OutDir = $PSScriptRoot
)

$ErrorActionPreference = 'Stop'

Write-Host "ProjectRoot: $ProjectRoot"
Write-Host "OutDir:      $OutDir"

if (-not (Test-Path (Join-Path $ProjectRoot 'pom.xml'))) {
  throw "pom.xml not found under ProjectRoot."
}

Push-Location $ProjectRoot
try {
  Write-Host "Building WAR (skip tests)..."
  mvn -q -DskipTests package

  $war = Join-Path $ProjectRoot 'target\sut-shabu.war'
  if (-not (Test-Path $war)) {
    throw "WAR not found at $war"
  }

  Write-Host "Copying WAR + data to portable-server..."
  Copy-Item $war (Join-Path $OutDir 'sut-shabu.war') -Force

  $dataSrc = Join-Path $ProjectRoot 'data'
  $dataDst = Join-Path $OutDir 'data'
  if (Test-Path $dataDst) { Remove-Item $dataDst -Recurse -Force }
  Copy-Item $dataSrc $dataDst -Recurse -Force

  Write-Host "Done. Now place jetty-runner.jar into $OutDir and run start.bat"
} finally {
  Pop-Location
}
