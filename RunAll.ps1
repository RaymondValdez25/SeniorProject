
Write-Output "running RunNewDatasetBinary"
./RunNewDatasetBinary ResourceDevelopment_Binary.csv

Write-Output "running RunNewDatasetMulticlassifier"
./RunNewDatasetMulticlassifier

Write-Output "running RunOldDatasetBinary"
./RunOldDatasetBinary Recon_Binary.csv

Write-Output "running RunOldDatasetMulticlassifier"
./RunOldDatasetMulticlassifier

Write-Output "running RunOldDatasetOldANN"
./RunOldDatasetOldANN