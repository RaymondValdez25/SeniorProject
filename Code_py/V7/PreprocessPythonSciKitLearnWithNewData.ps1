# Run a Python script to convert csv files to numpy arrays 
python -c @"

import numpy as np

# Load the CSV file
X = np.genfromtxt('Output_NN_In_V8.csv', delimiter=',')
y = np.genfromtxt('Output_OneHot_V8.csv', delimiter=',')

np.save('x_V8.npy', X)
np.save('y_V8.npy', y)

# Print the resulting NumPy array
print(X)
"@

python Scikit-Learn-ANN-NewDataset.py