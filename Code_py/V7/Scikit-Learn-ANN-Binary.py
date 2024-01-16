import numpy as np
#import pandas as pd
import tensorflow as tf
from sklearn.model_selection import train_test_split
from tensorflow.keras.layers import Dense
from tensorflow.keras.optimizers import Adam
from sklearn.metrics import precision_score, recall_score, f1_score, roc_auc_score
import time
from sklearn.metrics import confusion_matrix

X = np.load('x_V8.npy') # Data
y = np.load('y_V8.npy') # One-hot

ann = tf.keras.models.Sequential()
ann.add(tf.keras.layers.Dense(units=5, activation='relu'))
ann.add(tf.keras.layers.Dense(units=5, activation='relu'))
ann.add(tf.keras.layers.Dense(units=1, activation='sigmoid'))

custom_optimizer = Adam(learning_rate=0.05) 

# Compile the model
ann.compile(optimizer=custom_optimizer, loss='binary_crossentropy', metrics=['categorical_accuracy'])


def runANN(testDataSizePercent):

    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size = testDataSizePercent, random_state = 0, stratify=y)

    #set verbose = 2 to see accuracy line by line
    start_time_train = time.time()
    ann.fit(X_train, y_train, batch_size = 32, epochs = 100, verbose=0, validation_data=(X_test,y_test))
    end_time_train = time.time()

    total_training_time = f"{end_time_train - start_time_train:.2f}"

    #Make predictions based on X_test
    print('========...predicting========')
    start_time_test = time.time()
    y_pred = ann.predict(X_test)
    y_pred_binary = np.round(y_pred)
    end_time_test = time.time()

    total_testing_time = f"{end_time_test - start_time_test:.2f}"

    #print('====y_test====\n', y_test)
    #print('====y_pred_binary====\n', y_pred_binary)

    print('=================================')
    print('\nresults with test datasize:', testDataSizePercent * 100, '%')
    print('\n=================================')
    #Accuracy = (True Positives + True Negatives)/(True Positives + True Negatives + False Positives + False Negatives) 
    test_loss, test_accuracy = ann.evaluate(X_test, y_test)
    print('======Accuracy=====\n',test_accuracy * 100,'%')

    #Recall = (true positive)/(TruePositive + FalseNegatives)
    recall = recall_score(y_test, y_pred_binary, average='binary')
    print('======= recall ========\n', recall)

    #Precision score = (true positive)/(TruePositive + FalsePositive)
    precision = precision_score(y_test, y_pred_binary, average='binary')
    print('=======precision=======\n', precision)

    #F-measure False Positive Rate
    f_measure = f1_score(y_test, y_pred_binary)
    print('==========F measure======\n',f_measure)

    #AUC
    AUC = roc_auc_score(y_test, y_pred_binary)
    print('==========AUC======\n',AUC)

    #Training Time
    print('======total training time====')
    print(total_training_time, ' seconds')

    #Testing Time
    print('======total testing time====')
    print(total_testing_time, ' seconds')

    #confusion matrix
    result = confusion_matrix(y_test, y_pred_binary).ravel()

    #matrix outputs
    trueNegative = result[0]
    falsePositive = result[1]
    falseNegative = result[2]
    truePositive = result[3]

    print('true negative:', trueNegative)
    print('false positive:', falsePositive)
    print('false negative:', falseNegative)
    print('true positive:', truePositive)
    

testDatasizePercentages = [0.10, 0.15, 0.25, 0.99]
for testDataSizePercent in testDatasizePercentages:
    runANN(testDataSizePercent)