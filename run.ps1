param()

$ProjectRoot = "C:\SYSTEM\SYSTEM2\Projects\Examination result processing system\result-management-system\ResultManagementSystem"
$JavaFXPath = "C:\SYSTEM\SYSTEM2\Projects\Examination result processing system\result-management-system\javafx-sdk-26\lib"

# Also check if JavaFX is inside the lib folder
if (Test-Path "$ProjectRoot\lib\javafx-sdk-26\lib") {
    $JavaFXPath = "$ProjectRoot\lib\javafx-sdk-26\lib"
}

cd $ProjectRoot

# Create bin directory
if (-not (Test-Path "bin")) {
    New-Item -ItemType Directory -Force -Path "bin" | Out-Null
}

echo "Compiling Java sources..."
$javaFiles = Get-ChildItem -Path "src\main\java" -Filter "*.java" -Recurse | Select-Object -ExpandProperty FullName

# We use an array for arguments to avoid weird quoting issues
$javacArgs = @(
    "-d", "bin",
    "-cp", "lib\*",
    "--module-path", $JavaFXPath,
    "--add-modules", "javafx.controls,javafx.fxml"
) + $javaFiles

& javac @javacArgs

if ($LASTEXITCODE -ne 0) {
    echo "Compilation failed!"
    exit $LASTEXITCODE
}

echo "Copying resources (FXML, CSS)..."
Copy-Item -Path "src\main\resources\*" -Destination "bin\" -Recurse -Force

echo "Running Application..."
$javaArgs = @(
    "-cp", "bin;lib\*",
    "--module-path", $JavaFXPath,
    "--add-modules", "javafx.controls,javafx.fxml",
    "app.Main"
)

& java @javaArgs
