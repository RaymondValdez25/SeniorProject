import numpy as np
#import pandas as pd
import tensorflow as tf
from sklearn.model_selection import train_test_split
from tensorflow.keras.layers import Dense
from tensorflow.keras.optimizers import Adam

X = np.load('x_V8.npy') # Data
y = np.load('y_V8.npy') # One-hot

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size = 0.90, random_state = 0)

ann = tf.keras.models.Sequential()
ann.add(tf.keras.layers.Dense(units=5, activation='relu'))
ann.add(tf.keras.layers.Dense(units=5, activation='relu'))
ann.add(tf.keras.layers.Dense(units=4, activation='sigmoid'))

custom_optimizer = Adam(learning_rate=0.05) 

# Compile the model
ann.compile(optimizer=custom_optimizer, loss='categorical_crossentropy', metrics=['accuracy'])

ann.fit(X_train, y_train, batch_size = 32, epochs = 100, verbose=0)

y_pred = ann.predict(X_test)
y_pred = np.argmax(y_pred, axis=1)

print(y_pred)

validation = []

for i in y_test:
    if i[0] == 1:
        validation.append(0) 
    elif i[1] == 1:
        validation.append(1) 
    elif i[2] == 2:
        validation.append(2)
    else:
        validation.append(3)
        
for i in range(len(validation)):
    if(validation[i] == y_pred[i]):
        print(True)
    else:
        print(False)