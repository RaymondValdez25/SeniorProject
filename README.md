Before running the program you must download the data from these locations:
- First Dataset: https://drive.google.com/drive/u/0/folders/1ZvV2aThxfFhrJZzZhaLsqseQotHAH7Pr
- Second Dataset: https://drive.google.com/drive/u/0/folders/1ZvV2aThxfFhrJZzZhaLsqseQotHAH7Pr

These need to be inserted into your folder structure under the Data folder
  Data
    | -- FirstDataset
    | -- SecondDataset

Any csv file with (2UCT-UTB) as a suffix is a file that is oversampled

You can run the program by running any of the following Powershell scripts:
- RunAll
- RunNewDatasetBinary
- RunNewDatasetMulticlassifier
- RunOldDatasetBinary
- RunOldDatasetMulticlassifier
- RunOldDatasetOldANN

- RunNewDatasetBinary script requires a csv file name passed in as a parameter from the SecondDataset folder. Any file with binary in its title will work.
    Example: ./RunNewDatasetBinary Reconnaissance_Binary.csv
- RunOldDatasetBinary script also requires a a csv file name passed in as a parameter from the SecondDataset folder. Any file with binary in its title will work.
    Example: ./RunOldDatasetBinary Discovery_Binary_2UCT-UTB.csv
