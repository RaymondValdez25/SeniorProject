import numpy as np
#import pandas as pd
import tensorflow as tf
from sklearn.model_selection import train_test_split
from tensorflow.keras.layers import Dense
from tensorflow.keras.optimizers import Adam
from sklearn.metrics import precision_score, recall_score, f1_score, roc_auc_score
import time

X = np.load('x_V8.npy') # Data
y = np.load('y_V8.npy') # One-hot

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size = 0.20, random_state = 0)

ann = tf.keras.models.Sequential()
ann.add(tf.keras.layers.Dense(units=5, activation='relu'))
ann.add(tf.keras.layers.Dense(units=5, activation='relu'))
ann.add(tf.keras.layers.Dense(units=3, activation='sigmoid'))

custom_optimizer = Adam(learning_rate=0.05) 

# Compile the model
ann.compile(optimizer=custom_optimizer, loss='categorical_crossentropy', metrics=['accuracy'])

start_time_train = time.time()
ann.fit(X_train, y_train, batch_size = 32, epochs = 100, verbose=0)
end_time_train = time.time()

total_training_time = f"{end_time_train - start_time_train:.2f}"

#y_pred = ann.predict(X_test)
#y_pred = np.argmax(y_pred, axis=1)

#Make predictions based on X_test
print('========...predicting========')
start_time_test = time.time()
y_pred = ann.predict(X_test)
y_pred_rounded = np.round(y_pred)
end_time_test = time.time()

total_testing_time = f"{end_time_test - start_time_test:.2f}"

print('====y_test====\n', y_test)
print('====y_pred====\n',y_pred)
print('====y_pred_rounded====\n', y_pred_rounded)

#Accuracy = (True Positives + True Negatives)/(True Positives + True Negatives + False Positives + False Negatives) 
test_loss, test_accuracy = ann.evaluate(X_test, y_test)
print('======Accuracy=====\n',test_accuracy * 100,'%')

#Recall = (true positive)/(TruePositive + FalseNegatives)
#may have to use a different average
recall = recall_score(y_test, y_pred_rounded, average='weighted', zero_division=1.0)
print('======= recall ========\n', recall)

#Precision score = (true positive)/(TruePositive + FalsePositive)
precision = precision_score(y_test, y_pred_rounded, average='weighted', zero_division=1.0)
print('=======precision=======\n', precision)

#F-measure False Positive Rate
f_measure = f1_score(y_test, y_pred_rounded, average='weighted')
print('==========F measure======\n',f_measure)

#AUC
AUC = roc_auc_score(y_test, y_pred)
print('==========AUC======\n',AUC)

#Training Time
print('======total training time====')
print(total_training_time, ' seconds')

#Testing Time
print('======total testing time====')
print(total_testing_time, ' seconds')