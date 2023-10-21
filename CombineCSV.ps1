# Define the path to the folder containing the CSV files
$sourceFolder = "C:\SeniorProject\Data\SeparatedCSV"

# Define the output file path and name
$outputFilePath = "C:\SeniorProject\Data\CombinedNewData.csv"

# Get a list of all CSV files in the source folder
$csvFiles = Get-ChildItem -Path $sourceFolder -Filter *.csv

# Initialize an empty array to store the combined data
$combinedData = @()

# Loop through each CSV file
foreach ($csvFile in $csvFiles) {
    # Import the data from the current CSV file
    $data = Import-Csv $csvFile.FullName

    # Add the data to the combined array
    $combinedData += $data
}

# Export the combined data to a new CSV file
$combinedData | Export-Csv -Path $outputFilePath -NoTypeInformation

Write-Host "CSV files combined and saved to $outputFilePath"
