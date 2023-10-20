# Define the file name 
$javaFile = "ParseConnData_FollowHopsV3.java"

# Compile the Java file
javac $javaFile

# Run the Java program with the "windows" parameter
java ParseConnData_FollowHopsV3 windows

#Copy FinalGraph_V4.csv from Output into Data
cp ../Output/FinalGraph_V4.csv ../Data/

$javaFile = "ParseConnData_Create_ANN_DataV3.java"

#Compile the Java file
javac $javaFile

#Run java program with windows parameter
java ParseConnData_Create_ANN_DataV3 windows

#Copy the Output_NN_In_V7 and Output_OneHot_V7 into python V7 folder
cp ../Output/Output_NN_In_V7.csv ../Code_py/V7
cp ../Output/Output_OneHot_V7.csv ../Code_py/V7