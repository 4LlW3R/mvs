-- Clear tables
DELETE FROM dbo.key_rule_relations;
DELETE FROM dbo.key_rule_steps;
DELETE FROM dbo.key_rule;

-- Fill the rules for dimensions
DECLARE @API_2_0_CONST VARCHAR(10) = 'API_2_0';
DECLARE @VERSION_1_0 VARCHAR(15) = 'VERSION_1_0';

INSERT INTO dbo.key_rule (active, api_version, version, subscription, entity, natural_key_name)
VALUES
(1, @API_2_0_CONST, @VERSION_1_0, '*', 'DRIVER',                'driverId'),
(1, @API_2_0_CONST, @VERSION_1_0, '*', 'ASSET',                 'assetId'),
(1, @API_2_0_CONST, @VERSION_1_0, '*', 'LIBRARY_EVENT',         'eventTypeId'),
(1, @API_2_0_CONST, @VERSION_1_0, '*', 'LOCATION',              'locationId'),
(1, @API_2_0_CONST, @VERSION_1_0, '*', 'ORGANISATION_GROUP',    'groupId'),
(1, @API_2_0_CONST, @VERSION_1_0, '*', 'ORGANISATION_SUBGROUP', 'groupId');

-- Driver
DECLARE @driver_id1 AS bigint = (SELECT id FROM key_rule WHERE entity = 'DRIVER')

INSERT INTO dbo.key_rule_steps (parent_id, rule_type, ord, body, par0, par1)
VALUES
(@driver_id1, 'CUSTOM', 1, 'com.epam.tcodata.mdm.rules.DriverDimensionRule', 'driverId', 'employeeNumber'),
(@driver_id1, 'GENERATE', 2, '', NULL, NULL);

-- Asset
DECLARE @driver_id2 AS bigint = (SELECT id FROM key_rule WHERE entity = 'ASSET')

INSERT INTO dbo.key_rule_steps (parent_id, rule_type, ord, body)
VALUES
(@driver_id2, 'CUSTOM', 1, 'com.epam.tcodata.mdm.rules.VehicleDimensionRule'),
(@driver_id2, 'GENERATE', 2, '');

-- LibraryEvent
DECLARE @driver_id3 AS bigint = (SELECT id FROM key_rule WHERE entity = 'LIBRARY_EVENT')

INSERT INTO dbo.key_rule_steps (parent_id, rule_type, ord, body)
VALUES
(@driver_id3, 'CUSTOM', 1, 'com.epam.tcodata.mdm.rules.LibraryEventDimensionRule'),
(@driver_id3, 'GENERATE', 2, '');

-- Location
DECLARE @driver_id4 AS bigint = (SELECT id FROM key_rule WHERE entity = 'LOCATION')

INSERT INTO dbo.key_rule_steps (parent_id, rule_type, ord, body)
VALUES
(@driver_id4, 'CUSTOM', 1, 'com.epam.tcodata.mdm.rules.LocationDimensionRule'),
(@driver_id4, 'GENERATE', 2, '');

-- OrganisationGroup
DECLARE @driver_id5 AS bigint = (SELECT id FROM key_rule WHERE entity = 'ORGANISATION_GROUP')

INSERT INTO dbo.key_rule_steps (parent_id, rule_type, ord, body)
VALUES
(@driver_id5, 'CUSTOM', 1, 'com.epam.tcodata.mdm.rules.OrganisationGroupDimensionRule'),
(@driver_id5, 'GENERATE', 2, '');

-- OrganisationSubGroup
DECLARE @driver_id6 AS bigint = (SELECT id FROM key_rule WHERE entity = 'ORGANISATION_SUBGROUP')

INSERT INTO dbo.key_rule_steps (parent_id, rule_type, ord, body)
VALUES
(@driver_id6, 'CUSTOM', 1, 'com.epam.tcodata.mdm.rules.OrganisationSubGroupDimensionRule'),
(@driver_id6, 'GENERATE', 2, '');

-- Fill the rules for facts

INSERT INTO dbo.key_rule (active, api_version, version, subscription, entity, natural_key_name)
VALUES
(1, @API_2_0_CONST, @VERSION_1_0, '*', 'POSITION', 'positionId'),
(1, @API_2_0_CONST, @VERSION_1_0, '*', 'EVENT',    'eventId'),
(1, @API_2_0_CONST, @VERSION_1_0, '*', 'TRIP',     'tripId'),
(1, @API_2_0_CONST, @VERSION_1_0, '*', 'SUBTRIP',  'subTripId');

-- POSITION
DECLARE @position_id1 AS bigint = (SELECT id FROM key_rule WHERE entity = 'POSITION')

INSERT INTO dbo.key_rule_relations (parent_id, entity, natural_key_name, surrogate_key_name)
VALUES
(@position_id1, 'ASSET', 'assetId', 'vehicleDurableKey'),
(@position_id1, 'DRIVER', 'driverId', 'driverDurableKey');

-- EVENT
DECLARE @position_id2 AS bigint = (SELECT id FROM key_rule WHERE entity = 'EVENT')

INSERT INTO dbo.key_rule_relations (parent_id, entity, natural_key_name, surrogate_key_name)
VALUES
(@position_id2, 'ASSET', 'assetId', 'vehicleDurableKey'),
(@position_id2, 'DRIVER', 'driverId', 'driverDurableKey');

-- TRIP
DECLARE @position_id3 AS bigint = (SELECT id FROM key_rule WHERE entity = 'TRIP')

INSERT INTO dbo.key_rule_relations (parent_id, entity, natural_key_name, surrogate_key_name)
VALUES
(@position_id3, 'ASSET', 'assetId', 'vehicleDurableKey'),
(@position_id3, 'DRIVER', 'driverId', 'driverDurableKey');

-- SUBTRIP
DECLARE @position_id4 AS bigint = (SELECT id FROM key_rule WHERE entity = 'SUBTRIP')
-- Nothing to add to relations
