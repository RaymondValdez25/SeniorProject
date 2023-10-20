# Run a Python script to convert JSON to NumPy array
python -c @"

import numpy as np

# Load the CSV file
X = np.genfromtxt('Output_NN_In_V7.csv', delimiter=',')
Y = np.genfromtxt('Output_OneHot_V7.csv', delimiter=',')

np.save('x_V7.npy', X)
np.save('y_V7.npy', Y)

# Print the resulting NumPy array
print(X)
"@

python NN_backProp_opt3_V7.py