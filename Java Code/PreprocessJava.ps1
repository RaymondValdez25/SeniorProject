# Define the file name
$javaFile = "ParseConnData_FollowHopsV3.java"

# Compile the Java file
javac $javaFile

# Run the Java program with the "windows" parameter
java ParseConnData_FollowHopsV3 windows

#copy FinalGraph_V4.csv from Output into Data
cp ../Output/FinalGraph_V4.csv ../Data/

$javaFile = "ParseConnData_Create_ANN_DataV2.java"

#compile the Java file
javac $javaFile

#run java program with windows parameter
java ParseConnData_Create_ANN_DataV2 windows