import numpy as np
#import pandas as pd
import tensorflow as tf
from sklearn.model_selection import train_test_split
from tensorflow.keras.layers import Dense
from tensorflow.keras.optimizers import Adam

X = np.load('x_V8.npy') # Data
y = np.load('y_V8.npy') # One-hot

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size = 0.10, random_state = 0, stratify=y)

ann = tf.keras.models.Sequential()
ann.add(tf.keras.layers.Dense(units=5, activation='relu'))
ann.add(tf.keras.layers.Dense(units=5, activation='relu'))
ann.add(tf.keras.layers.Dense(units=1, activation='sigmoid'))

custom_optimizer = Adam(learning_rate=0.05) 

# Compile the model
ann.compile(optimizer=custom_optimizer, loss='binary_crossentropy', metrics=['categorical_accuracy'])

#set verbose = 2 to see accuracy line by line
ann.fit(X_train, y_train, batch_size = 32, epochs = 100, verbose=2, validation_data=(X_test,y_test))

#accuracy
test_loss, test_accuracy = ann.evaluate(X_test, y_test)
print('test_accuracy',test_accuracy)

#other predictions
#using test dataset
#y_pred = ann.predict(X_test)
#y_pred_bool = np.round(y_pred)
#print('y_predict_bool_Full_test',y_pred_bool)

#all Resource Development
#X_test = [[0.027777778,	0.998730941	,0.001484683	,0.002988301	,1],
#[0.027777778	,2.17553E-05	,0.007400172	,0.000726496	,0],
#[0,	5.07623E-05,	0.000336238,	0.000363248,	0],
#[0,	3.62588E-06	,5.95282E-05	,0.000242165	,0],
#[0,	0,	4.20179E-08,	0.000121083,	0],]
#y_pred = ann.predict(X_test)
#y_pred_bool = np.round(y_pred)
#print('y_predict_all_Zeroes',y_pred_bool)
