#dataset type
$datasetType = "new"
$pythonFile = "Scikit-Learn-ANN-Multiclassifier.py"
$numOutputs = 4

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