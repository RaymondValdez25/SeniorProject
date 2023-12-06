#dataset type
$datasetType = "new"

#Enter Java Code Folder
cd 'Java Code'
./PreprocessJava $datasetType

#Exit folder
cd ..

#Enter Python Code Folder
cd 'Code_py\V7'

./PreprocessPythonSciKitLearnWithNewData

#Exit folder
cd ../..