# Define the file name 
$javaFile = "ParseConnData_FollowHops_NewData.java"

# Compile the Java file
javac $javaFile

# Run the Java program with the "windows" parameter
java ParseConnData_FollowHops_NewData windows

#Copy FinalGraph_V4.csv from Output into Data
cp ../Output/FinalGraph_V4.csv ../Data/

$javaFile = "ParseConnData_Create_ANN_NewData.java"

#Compile the Java file
javac $javaFile

#Run java program with windows parameter
java ParseConnData_Create_ANN_NewData windows

#Copy the Output_NN_In_V7 and Output_OneHot_V7 into python V7 folder
cp ../Output/Output_NN_In_V8.csv ../Code_py/V7
cp ../Output/Output_OneHot_V8.csv ../Code_py/V7