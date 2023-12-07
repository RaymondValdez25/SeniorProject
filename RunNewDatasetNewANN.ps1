#dataset type
$datasetType = "new"
$pythonFile = "Scikit-Learn-ANN-NewDataset.py"

#Enter Java Code Folder
cd 'Java Code'
./PreprocessJava $datasetType

#Exit folder
cd ..

#Enter Python Code Folder
cd 'Code_py\V7'

./PreprocessPython $pythonFile

#Exit folder
cd ../..