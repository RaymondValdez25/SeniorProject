# Run a Python script to convert csv files to numpy arrays 
python -c @"

import numpy as np

# Load the CSV file
X = np.genfromtxt('Output_NN_In_V7.csv', delimiter=',')
y = np.genfromtxt('Output_OneHot_V7.csv', delimiter=',')

np.save('x_V7.npy', X)
np.save('y_V7.npy', y)

# Print the resulting NumPy array
print(X)
"@

python NN_backProp_opt3_V7.py