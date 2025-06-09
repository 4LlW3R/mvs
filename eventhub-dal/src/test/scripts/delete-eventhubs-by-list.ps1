<#
https://docs.microsoft.com/en-us/powershell/module/azurerm.eventhub/remove-azurermeventhub?view=azurermps-4.4.1

Remove-AzureRmEventHub
      [-ResourceGroupName] <String>
      -Namespace <String>
      -Name <String>
      [-WhatIf]
      [-Confirm]
#>

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
#$EventHubName = "fact-position"

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
    Write-Host "removing eventhub $name in namespace $Namespace"

    Remove-AzureRmEventHub -ResourceGroupName $ResouceGroup `
     -Namespace $Namespace  `
     -Name $name
}