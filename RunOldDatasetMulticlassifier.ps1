#dataset type
$datasetType = "old"
$pythonFile = "Scikit-Learn-ANN-Multiclassifier.py"
$numOutputs = 3

#Enter Java Code Folder
cd 'Java Code'
./PreprocessJava $datasetType

#Exit folder
cd ..

#Enter Python Code Folder
cd 'Code_py\V7'

./PreprocessPython $pythonFile $numOutputs

#Exit folder
cd ../..