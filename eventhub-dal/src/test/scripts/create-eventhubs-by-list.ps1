<#
look for examples at 
https://docs.microsoft.com/en-us/azure/event-hubs/event-hubs-manage-with-ps
https://blogs.msdn.microsoft.com/paolos/2014/12/01/how-to-create-a-service-bus-namespace-and-an-event-hub-using-a-powershell-script/
#>

Function Create-EventHub-IfNotExist(
    $vResouceGroup,
    [String]$vNamespace,
    [String]$vLocation = "UK South",
    [String]$vEventHubName,
    [Int]$vPartitionCount = 2,
    [Int]$vMessageRetentionInDays = 1
    )
{
    # Query to see if the namespace currently exists
    $CurrentNamespace = Get-AzureRMEventHubNamespace -ResourceGroupName $vResouceGroup -NamespaceName $vNamespace

    # Check if event hub already exists
    $CurrentEH = Get-AzureRMEventHub -ResourceGroupName $vResouceGroup -NamespaceName $vNamespace -EventHubName $vEventHubName

    if($CurrentEH)
    {
        Write-Host "The event hub $vEventHubName already exists in the $vLocation region:"
    }
    else
    {
        Write-Host "The $vEventHubName event hub does not exist."
        Write-Host "Creating the $vEventHubName event hub in the $vLocation region..."
        New-AzureRmEventHub -ResourceGroupName $vResouceGroup `
            -NamespaceName $vNamespace `
            -EventHubName $vEventHubName `
            -Location $vLocation `
            -PartitionCount $vPartitionCount `
            -MessageRetentionInDays $vMessageRetentionInDays

        $CurrentEH = Get-AzureRmEventHub -ResourceGroupName $vResouceGroup -NamespaceName $vNamespace -EventHubName $vEventHubName
        Write-Host "The $vEventHubName event hub in Resource Group $vResouceGroup in the $vLocation region has been successfully created."
    }
}

# sign in
Write-Host "Logging in...";
Login-AzureRmAccount;

# select subscription
# subscriptionId for {Chevron, 6bbf3f76-6d30-4bba-956b-c595a2c9bafd}
$subscriptionId = '6bbf3f76-6d30-4bba-956b-c595a2c9bafd'
Write-Host "Selecting subscription '$subscriptionId'";
Select-AzureRmSubscription -SubscriptionID $subscriptionId;


$ResouceGroup = "FleetMonitoring"
$Namespace = "mvs-eventhub-telemetry-pump"
$Location = "UK South"

$eventHubNames = @(
    "fact-position",
    "fact-event",
    "fact-trip",
    "fact-subtrip",
    "dim-driver",
    "dim-vehicle",
    "dim-event-description",
    "dim-location"
)

Foreach($name in $eventHubNames)
{
    Create-EventHub-IfNotExist -vResouceGroup $ResouceGroup -vNamespace $Namespace -vEventHubName $name
}
