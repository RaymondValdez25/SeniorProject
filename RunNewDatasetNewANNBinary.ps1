param(
    [string]$csv
)

Write-Output "preprocessing $csv"

#Enter Java Code Folder
cd 'Java Code'
./PreprocessJavaNewBinary $csv

#Exit folder
cd ..

#Enter Python Code Folder
cd 'Code_py\V7'

./PreprocessPythonSciKitLearnWithNewDataBinary

#Exit folder
cd ../..