Folders V4 to V7 hold each of the approaches code and output data.

All these approaches used the same input.  The x_V#.npy and the y_V4.npy files.

Steps:

1. Run the java code (see java code folder) to produce the following files:
     a. Output_NN_In_V#.csv
     b. Output_OneHot_V#.csv

2. Open NN_SaveArrays_V#.py and:
     a. Replace the "X = np.array(" with the contents of the array produced
        in the Output_NN_In_V#.csv file
     b. Replace the "y = np.array(" with the contents of the array produced
        in the Output_OneHot_V#.csv

3. Run the NN_SaveArrays_V#.py code and the x_V#.npy and the y_V4.npy files
   will be produced.

4. Once the .npy files are produced, you can run NN_backProp_opt3_V4.py to
   train the ANN with the data.