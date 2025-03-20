# Get the current project directory
$projectDir = Get-Location
$outputDir = Join-Path $projectDir 'output_files'

# Usunięcie końcowego ukośnika z $projectDir, jeśli istnieje
$projectDir = $projectDir.Path.TrimEnd('\','/')

# Tworzenie katalogu output_files, jeśli nie istnieje
If (-Not (Test-Path $outputDir)) {
    New-Item -ItemType Directory -Path $outputDir | Out-Null
}

# Katalogi do wykluczenia
$excludeDirs = @(
    '.git',
    '.idea',
    '.settings',
    'WEB-INF\classes',
    'WEB-INF\lib',
    'node_modules',
    'dist',
    'build',
    'target'
)

# Rozszerzenia plików do uwzględnienia
$includeExtensions = @(
    '.java',
    '.xml',
    '.properties',
    '.yml',
    '.yaml',
    '.sql',
    '.html',
    '.css',
    '.js'
)

# Funkcja sprawdzająca, czy plik znajduje się w wykluczonym katalogu
function IsInExcludedDir($file) {
    foreach ($dir in $excludeDirs) {
        if ($file.DirectoryName -like "*\$dir\*") {
            return $true
        }
    }
    return $false
}

# Funkcja do obliczania ścieżki względnej
function Get-RelativePath {
    param (
        [string]$BasePath,
        [string]$TargetPath
    )

    # Upewnienie się, że ścieżki mają końcowy ukośnik
    $baseUri = New-Object System.Uri($BasePath + [IO.Path]::DirectorySeparatorChar)
    $targetUri = New-Object System.Uri($TargetPath)

    if ($baseUri.Scheme -ne $targetUri.Scheme) {
        return $TargetPath
    }

    $relativeUri = $baseUri.MakeRelativeUri($targetUri)
    $relativePath = [System.Uri]::UnescapeDataString($relativeUri.ToString())

    if ($targetUri.Scheme -eq 'file') {
        return $relativePath.Replace('/', '\')
    } else {
        return $relativePath
    }
}

# Pobranie plików do uwzględnienia
$files = Get-ChildItem -Path $projectDir -Recurse -File | Where-Object {
    ($includeExtensions -contains $_.Extension.ToLower()) -and
    -not (IsInExcludedDir $_)
}

# Grupowanie plików według ostatniego katalogu (liścia)
$filesGrouped = $files | Group-Object -Property { $_.Directory.FullName }

foreach ($group in $filesGrouped) {
    $leafDirectory = $group.Group[0].Directory
    # Uzyskanie ścieżki względnej ostatniego katalogu względem katalogu projektu
    $relativePath = Get-RelativePath $projectDir $leafDirectory.FullName
    # Generowanie nazwy pliku wynikowego
    $outputFileName = $relativePath.Replace('\', '_') + '.txt'
    $outputFilePath = Join-Path $outputDir $outputFileName

    # Upewnienie się, że katalog wyjściowy istnieje
    $outputFileDir = Split-Path $outputFilePath
    If (-Not (Test-Path $outputFileDir)) {
        New-Item -ItemType Directory -Path $outputFileDir -Force | Out-Null
    }

    # Przetwarzanie każdego pliku w grupie
    foreach ($file in $group.Group) {
        # Nagłówek z pełną ścieżką pliku względną do katalogu projektu
        $fileRelativePath = Get-RelativePath $projectDir $file.FullName
        "Plik: $($fileRelativePath)" | Out-File -FilePath $outputFilePath -Append -Encoding UTF8

        # Zawartość pliku
        Get-Content -Path $file.FullName | Out-File -FilePath $outputFilePath -Append -Encoding UTF8

        # Separator między plikami
        "======================" | Out-File -FilePath $outputFilePath -Append -Encoding UTF8
    }
}

# Generowanie uproszczonej struktury drzewa plików
$outputTreeFile = Join-Path $outputDir 'project_structure.txt'

If (Test-Path $outputTreeFile) {
    Remove-Item $outputTreeFile
}

"--- Struktura projektu ---" | Out-File -FilePath $outputTreeFile -Encoding UTF8
"$projectDir\" | Out-File -FilePath $outputTreeFile -Append -Encoding UTF8

Function Get-Tree {
    param(
        [string]$Path,
        [int]$Indent = 0
    )

    $items = Get-ChildItem -Path $Path | Where-Object {
        -not ($_.PSIsContainer -and ($excludeDirs -contains $_.Name))
    } | Sort-Object -Property PSIsContainer, Name

    foreach ($item in $items) {
        $indentStr = '|' + ('   |' * $Indent) + '-- '

        if ($item.PSIsContainer) {
            "$indentStr$($item.Name)\" | Out-File -FilePath $outputTreeFile -Append -Encoding UTF8
            Get-Tree -Path $item.FullName -Indent ($Indent + 1)
        } else {
            "$indentStr$($item.Name)" | Out-File -FilePath $outputTreeFile -Append -Encoding UTF8
        }
    }
}

Get-Tree -Path $projectDir -Indent 0