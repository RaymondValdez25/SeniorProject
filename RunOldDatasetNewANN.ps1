#dataset type
$datasetType = "old"
$pythonFile = "Scikit-Learn-ANN-old.py"

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