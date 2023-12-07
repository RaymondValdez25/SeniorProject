param(
    [string]$csv,
	[string]$datasetNumber
)

Write-Output "passed in $datasetNumber"

# Define the file name 
if($datasetNumber -eq 'old' ){
	Write-Output "running old"
	$HopsFileJava = "ParseConnData_FollowHopsV3_Binary.java"
	$HopsFileClass = "ParseConnData_FollowHopsV3_Binary"
	$ConnFileJava = "ParseConnData_Create_ANN_NewData_Binary.java"
	$ConnFileClass = "ParseConnData_Create_ANN_NewData_Binary"
}

elseif($datasetNumber -eq 'new' ){
	Write-Output "running new"
	$HopsFileJava = "ParseConnData_FollowHops_NewData_Binary.java"
	$HopsFileClass = "ParseConnData_FollowHops_NewData_Binary"
	$ConnFileJava = "ParseConnData_Create_ANN_NewData_Binary.java"
	$ConnFileClass = "ParseConnData_Create_ANN_NewData_Binary"
}

else{
	Write-Output "Invalid dataset number"
}

# Compile the Java file
javac $HopsFileJava

# Run the Java program with the "windows" parameter
java $HopsFileClass windows $csv

#Copy FinalGraph_V4.csv from Output into Data
cp ../Output/FinalGraph_V4.csv ../Data/

#Compile the Java file
javac $ConnFileJava

#Run java program with windows parameter
java $ConnFileClass windows

#Copy the Output_NN_In_V7 and Output_OneHot_V7 into python V7 folder
cp ../Output/Output_NN_In_V8.csv ../Code_py/V7
cp ../Output/Output_OneHot_V8.csv ../Code_py/V7