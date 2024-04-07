Before running the program you must download the data from these locations:
\nFirst Dataset: https://drive.google.com/drive/u/0/folders/1ZvV2aThxfFhrJZzZhaLsqseQotHAH7Pr
\nSecond Dataset: https://drive.google.com/drive/u/0/folders/1ZvV2aThxfFhrJZzZhaLsqseQotHAH7Pr

These need to be inserted into your folder structure under the Data folder
  Data
    | -- FirstDataset
    | -- SecondDataset

Any file with (2UCT-UTB) as a suffix is a file that is oversampled

You can run the program by running any of the following Powershell scripts:
1. RunAll
2. RunNewDatasetBinary
3. RunNewDatasetMulticlassifier
4. RunOldDatasetBinary
5. RunOldDatasetMulticlassifier
6. RunOldDatasetOldANN


  - RunNewDatasetBinary script requires a csv file name passed in as a parameter from the SecondDataset folder. Any file with binary in its title will work.
      Example: ./RunNewDatasetBinary Reconnaissance_Binary.csv
  - RunOldDatasetBinary script also requires a a csv file name passed in as a parameter from the SecondDataset folder. Any file with binary in its title will work.
     Example: ./RunOldDatasetBinary Discovery_Binary_2UCT-UTB.csv
