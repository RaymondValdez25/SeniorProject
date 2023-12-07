param(
    [string]$csv
)

$pythonFile = "Scikit-Learn-ANN-NewDataset-Binary.py"


Write-Output "preprocessing $csv"

#Enter Java Code Folder
cd 'Java Code'
./PreprocessJavaBinary $csv new

#Exit folder
cd ..

#Enter Python Code Folder
cd 'Code_py\V7'

./PreprocessPython $pythonFile

#Exit folder
cd ../..