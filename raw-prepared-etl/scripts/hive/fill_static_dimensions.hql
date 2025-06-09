USE prepared;

-- dim_fuel_type

INSERT OVERWRITE TABLE dim_fuel_type
SELECT q.durable_id, q.fuel_type_code, q.fuel_type_description
FROM (SELECT STACK(5
    , 'PETROL', '3179838d-2f99-49f2-baec-dd0a15226cfb', 'Petrol'
    , 'DIESEL', 'd00fcf78-91d8-419d-a11f-5357b60ea4b6', 'Diesel'
    , 'LPG', 'e843910c-8fc8-483f-a3b3-a5405913cdd2', 'LPG'
    , 'OTHER', '8cf8d0b3-80f5-4f10-a201-9783cd57cabb', 'Other'
    , 'NONE', 'cabdb120-fb7c-4346-a527-d7e5bc504cda', 'None'
) AS (fuel_type_code, durable_id, fuel_type_description)) AS q
WHERE q.durable_id NOT IN (SELECT durable_id FROM dim_fuel_type);

-- dim_group_type

INSERT OVERWRITE TABLE dim_group_type
SELECT q.durable_id, q.external_id, q.group_type_code, q.group_type_description
FROM (SELECT STACK(15
    , 'DATA_CENTRE', '3e6017ae-14f1-4fa8-9c93-f73bc1bf1b29', 0 , 'DataCentre'
    , 'RSO_GROUP', '50d9d701-35c9-41a5-96fd-ca2a5871052f', 2, 'RsoGroup'
    , 'DEALER_GROUP', '21ad712f-67f0-40d5-b78d-0a6d2341e3fc', 3, 'DealerGroup'
    , 'MULTI_LEVEL_ORG', 'acbd6a7e-a28f-4386-bcfb-35e8c13643d8', 12, 'MultiLevelOrg'
    , 'ORGANISATION_GROUP', 'bde0875c-8eda-4061-97b1-82f8994cf4ea', 1, 'OrganisationGroup'
    , 'ORGANISATION_SUB_GROUP', '59dfd194-0178-49be-be15-5d38a94bba57', 5, 'OrganisationSubGroup'
    , 'SITE_GROUP', 'd42a4802-ef80-44b8-b0e2-db7e9f147fa0', 4, 'SiteGroup'
    , 'DEFAULT_SITE', '05126c10-94c2-469d-84c9-221957529087', 6, 'DefaultSite'
    , 'SECURITY_GROUP', '4b18f406-d06c-46fc-8e6f-a4b3ca89d833', 7, 'SecurityGroup'
    , 'NOTIFICATION_GROUP', '4446085c-88cb-47c9-9e80-b8ead54496db', 8, 'NotificationGroup'
    , 'NOTIFICATION_ASSETS_GROUP', 'f324ee82-d06f-4b74-a25d-c96ecf5d9331', 9, 'NotificationAssetsGroup'
    , 'NOTIFICATION_DRIVERS_GROUP', 'a15f38e0-f1e4-4802-b137-3ceb85c78d1e', 10, 'NotificationDriversGroup'
    , 'NOTIFICATION_EVENTS_GROUP', 'cb199b56-d128-4ec1-b740-0eef921431e0', 11, 'NotificationEventsGroup'
    , 'MOBILE_DEVICE_ADMIN_COMMISSIONING_GROUP', '2ca95ddc-c135-42d0-b25a-ca5149b1ee74', 13, 'MobileDeviceAdminCommissioningGroup'
    , 'DRIVER_USER_GROUP', '6b9501bb-6f8e-49e8-a8bc-f673581c66d2', 14, 'DriverUserGroup'
) AS (group_type_code, durable_id, external_id, group_type_description)) AS q
WHERE q.durable_id NOT IN (SELECT durable_id FROM dim_group_type);

-- dim_location_type

INSERT OVERWRITE TABLE dim_location_type
SELECT q.durable_id, q.external_id, q.location_type_code, q.location_type_description
FROM (SELECT STACK(12
    , 'CUSTOMER', '0d470c0e-6938-48ee-89fd-72b37fe4657c', 1 , 'Customer'
    , 'NO_GO_ZONE', 'a3738ab4-af9b-429a-b340-763b54b90508', 2, 'NoGoZone'
    , 'SITE', 'cbf197a2-b92a-4192-9a96-0427989687de', 3, 'Site'
    , 'OTHER', '6f2b873b-62c4-4a44-8fcf-b53d2c50850b', 4, 'Other'
    , 'STREET_POLY_LINE', '59ab58dc-34ec-473d-a126-d35f923d711b', 5, 'StreetPolyLine'
    , 'ROUTE_POLY_LINE', '7b5678f5-48df-4d3d-a462-5da2317b90d6', 6, 'RoutePolyLine'
    , 'FUEL_STOP', '64f3c005-04a8-4ecb-b6f4-795791a68ce6', 7, 'FuelStop'
    , 'REST_STOP', 'b0bbb5ba-4ae9-4432-b9fd-f89d432bdf71', 8, 'RestStop'
    , 'SPEED_ZONE', '779c9b8b-b3b7-4fff-9f6d-21608e7cccc4', 9, 'SpeedZone'
    , 'BUSINESS', 'f767947d-615c-4567-9f39-b78f4c49bcbd', 10, 'Business'
    , 'PRIVATE', 'a0af6a49-f890-4999-8e2d-9c5bfc08d707', 12, 'Private'
    , 'UN_CLASSIFIED', 'e42c66b2-cd5e-4714-934a-46d9f3f46a4b', 14, 'UnClassified'
) AS (location_type_code, durable_id, external_id, location_type_description)) AS q
WHERE q.durable_id NOT IN (SELECT durable_id FROM dim_location_type);

-- dim_location_shape_type

INSERT OVERWRITE TABLE dim_location_shape_type
SELECT q.durable_id, q.external_id, q.shape_type_code, q.shape_type_description
FROM (SELECT STACK(4
    , 'CIRCLE', '4dcae378-17c2-483a-81c5-8faefcff95dc', 0, 'Circle'
    , 'POLYGON', '6b0ce8dc-0a0d-475f-a4cc-8328df638cff', 1, 'Polygon'
    , 'RECTANGLE', '86806b04-7f65-4978-9936-471983628cbe', 2, 'Rectangle'
    , 'POLYLINE', 'b79fb55a-81f1-444d-b9e8-4a46c657678a', 3, 'PolyLine'
) AS (shape_type_code, durable_id, external_id, shape_type_description)) AS q
WHERE q.durable_id NOT IN (SELECT durable_id FROM dim_location_shape_type);

-- dim_vehicle_state

INSERT OVERWRITE TABLE dim_vehicle_state
SELECT q.durable_id, q.state_code, q.state_description
FROM (SELECT STACK(16
    , 'AVAILABLE', '9866a2ce-eb38-4607-9014-617f0e77e44a', 'Available'
    , 'UNAVAILABLE', '76926fb2-9ebf-42da-b4b0-426cba2963bf', 'Unavailable'
    , 'ACCIDENT', 'ae0fd0c7-0f8e-4128-ad6c-639098bf6e89', 'Accident'
    , 'ACTIVE_MESSAGE_DEACTIVATED', '2765377d-bbf9-4b04-9392-0b58f961957a', 'Active message deactivated'
    , 'AWAITING_FEEDBACK', '8f6c6e64-9a29-4865-bfaf-8fba9a72be02', 'Awaiting feedback'
    , 'BUZZER_DEACTIVATED', 'b79b6bb9-0313-461b-b090-8410427598eb', 'Buzzer deactivated'
    , 'CONFIRMED_STANDING', '64598f72-456e-465b-b5e9-b7cb5f62cc79', 'Confirmed standing'
    , 'DE_INSTALLED', 'd6000ee3-2edc-4f1a-aae9-67e3e322d6a9', 'De-Installed'
    , 'IMMOBILIZER_BYPASSED', 'b44c74ba-a3f8-4434-8239-c0c5a714dbdc', 'Immobilizer bypassed'
    , 'NEW_INSTALLATION', '004d2909-81a3-48e4-95f9-df1a7f695f78', 'New installation'
    , 'OPERATIONAL_NOT_DOWNLOADING', '30172a62-0239-4c11-add5-344e5668abd7', 'Operational - Not downloading'
    , 'SOLD', 'd7878d17-9b46-4142-8d01-eda4914a0dcc', 'Sold'
    , 'VEHICLE_OFF_ROAD', '62f4c4c5-e5b2-4b32-ab4b-60f7484de90a', 'Vehicle off road'
    , 'WORKSHOP', '5de34510-0397-440f-b729-8b8834eab888', 'Workshop'
    , 'OTHER', '6049f532-3717-4072-8595-af0ac8874d73', 'Other'
    , 'DECOMMISSIONED', 'f4b1fe36-9904-45a1-b9cb-90e76949332e', 'Decommissioned'
) AS (state_code, durable_id, state_description)) AS q
WHERE q.durable_id NOT IN (SELECT durable_id FROM dim_vehicle_state);

-- dim_vehicle_type

INSERT OVERWRITE TABLE dim_vehicle_type
SELECT q.durable_id, q.external_id, q.vehicle_type_code, q.vehicle_type_description
FROM (SELECT STACK(23
    , 'MOTORCYCLE', 'cc808430-405c-4460-a741-599aae3dadd6', 1, 'Motorcycle'
    , 'TRAILER', '7427f1ed-091c-4623-9f8f-c92a056a38c3', 2, 'Trailer'
    , 'BOAT', '7a3d4d20-9913-41dc-8213-8dc7252601bd', 4, 'Boat'
    , 'MOBILE_PLANT_EQUIPMENT', '7a252948-9337-4cc6-a341-bfd21f0fa513', 5, 'Mobile Plant Equipment'
    , 'STATIONARY_PLANT_EQUIPMENT', 'f050e222-5276-4674-ab19-aa0b0202ae1b', 6, 'Stationary Plant Equipment'
    , 'EMERGENCY_SERVICE_VEHICLE', '84a9efe7-eb28-43d1-9bce-7fdac495237c', 7, 'Emergency Service Vehicle'
    , 'DANGEROUS_GOODS_VEHICLE', '8870f58d-e8cb-4e6e-a060-e9df7cc8352c', 8, 'Dangerous Goods Vehicle'
    , 'PASSENGER_VEHICLE', '8b54c61f-9b26-4b2c-a524-65f6600c9eb8', 9, 'Passenger Vehicle'
    , 'LIGHT_PASSENGER_VEHICLE_MINIBUS', '6bd056b0-bb58-4733-bff3-9f7b4b407337', 10, 'Light Passenger Vehicle - Minibus'
    , 'HEAVY_PASSENGER_VEHICLE_BUS_ARTICULATED', '0f58c517-f659-4d19-abc6-03177874f8be', 11, 'Heavy Passenger Vehicle - Bus - Articulated'
    , 'HEAVY_PASSENGER_VEHICLE_BUS_SINGLE_DECKER', '3a5557dc-3372-4738-97ee-4b6c95bd2a30', 12, 'Heavy Passenger Vehicle - Bus - Single Decker'
    , 'HEAVY_PASSENGER_VEHICLE_BUS_DOUBLE_DECKER', 'b915d0b4-bf81-4c19-b2ce-c8ec0ef6cadb', 13, 'Heavy Passenger Vehicle - Bus - Double Decker'
    , 'HEAVY_VEHICLE_ARTICULATED', '66670091-df55-4faf-9f3b-2ee26ee79b78', 14, 'Heavy Vehicle - Articulated'
    , 'HEAVY_VEHICLE_NON_ARTICULATED', 'c9650da2-7ef0-4919-8579-35973fabc6a9', 15, 'Heavy Vehicle - Non-Articulated'
    , 'HEAVY_VEHICLE_REFRIGERATED_TRANSPORT', '960f1b1a-4eec-4829-ba97-5b50e469c2e5', 16, 'Heavy Vehicle - Refrigerated Transport'
    , 'LIGHT_VEHICLE', '5ab6f6f4-36c7-4530-9474-84a271d1a2e1', 17, 'Light Vehicle'
    , 'FLUID_TRANSPORT_VEHICLE', 'f190e8f4-92fc-4262-8d63-b4f922211943', 18, 'Fluid Transport Vehicle'
    , 'OTHER', 'dbee5950-2f39-4b48-8119-3776279a5692', 20, 'Other'
    , 'TRAIN', '83915fc6-6ec4-4a5f-b98a-b6e636c47fbf', 21, 'Train'
    , 'LIGHT_DELIVERY_VEHICLE', 'aa6477d2-2496-4ad4-a6a2-374be55571d2', 22, 'Light Delivery Vehicle'
    , 'OFF_ROAD_VEHICLE', '51db66dc-efb1-464f-860a-75d3725e541e', 24, 'Off-Road Vehicle'
    , 'MEDIUM_COMMERCIAL_VEHICLE', 'da8245bd-b154-4929-a33e-2d8b05879889', 25, 'Medium Commercial Vehicle'
    , 'NON_POWERED_ASSET', '4a21fc7d-cb34-42f1-86af-a77302d3f995', 26, 'Non-Powered Asset'
) AS (vehicle_type_code, durable_id, external_id, vehicle_type_description)) AS q
WHERE q.durable_id NOT IN (SELECT durable_id FROM dim_vehicle_type);

-- dim_video_channel_type

INSERT OVERWRITE TABLE dim_vehicle_state
SELECT q.durable_id, q.channel_code, q.channel_description
FROM (SELECT STACK(4
    , 'ROAD', '02303edc-a453-4741-94b6-81fb5c35884e', 'Road – forward looks to road'
    , 'CAB', '7c904890-7fea-48ca-bf74-ed574b378365', 'Cab - internal looks into cabin'
    , 'CAMERA_3', '673a517d-3491-478e-9ee8-3d726f55091b', 'Camera_3'
    , 'CAMERA_4', '577492d4-2236-46e4-9b19-834a05e1ed91', 'Camera_4'
) AS (channel_code, durable_id, channel_description)) AS q
WHERE q.durable_id NOT IN (SELECT durable_id FROM dim_vehicle_state);

-- dim_event_validation_code

INSERT OVERWRITE TABLE dim_event_validation_code
SELECT q.durable_id, q.external_id, q.description
FROM (SELECT STACK(3
    , 'VALID', 'dc018d95-898d-4f64-bc5f-e2099e5aacb0', 'Valid'
    , 'SUSPECT', '4587cd2c-2776-4558-a60d-4e780a2e2305', 'Suspect'
    , 'FALSE_POSITIVE', '55d89649-9b4c-4ab7-a710-e346d6b86ca0', 'False positive'
) AS (external_id, durable_id, description)) AS q
WHERE q.durable_id NOT IN (SELECT durable_id FROM dim_event_validation_code);

-- dim_event_problem_vehicle_code

INSERT OVERWRITE TABLE dim_event_problem_vehicle_code
SELECT q.durable_id, q.external_id, q.shape_type_code, q.description
FROM (SELECT STACK(12
    , 'VALID', '31770080-0b43-4179-8acd-5e27bad38f56', 1, 'Valid'
    , 'GPS_PROBLEM', 'a6ec00eb-912b-418e-a250-b2d376cf5489', 2, 'Gps problem'
    , 'SPEED_SENDER_PROBLEM', '9f9144c1-3320-477b-a090-4bbd4eb0eb89', 3, 'Speed sender problem'
    , 'NO_GPS_DATA_AVAILABLE', 'e423055b-6e13-4de0-bc7b-341364f86e6c', 4, 'No gps data available'
    , 'DUPLICATE_EVENTS', '31f875d2-c68d-4727-9b82-14543a22545a', 5, 'Duplicate events'
    , 'BRACKING_RATE_VALUE_HIGH', '68786551-a3ef-49b1-82ed-9fb1011697ff', 6, 'Bracking rate value high'
    , 'BRACKING_RATE_VALUE_HIGH_COMPARE_WITH_GPS', '247413ae-dd2f-4bef-b113-065791d9f7ef', 7, 'Bracking rate value high compare with gps'
    , 'ACCELERATION_RATE_HIGH', 'cbd3a97d-6d8d-4158-abbe-ff68ecd2e104', 8, 'Acceleration rate high'
    , 'ACCELERATION_RATE_COMPARE_WITH_GPS', 'd823c4de-7a41-4622-9146-4cc5109bef9b', 9, 'Acceleration rate compare with gps'
    , 'SPEED_SENSOR_SPIKE', 'aec2d040-52d6-4918-8c95-4539c0953c72', 10, 'Speed sensor spike'
    , 'INVALID_SPEED_VALUE', 'eb00dc3c-a812-413f-a0c4-c807c1866938', 11, 'Invalid speed value'
    , 'VELOCITY_ISSUE', '482778ea-f689-49c3-a507-eeaa5202f433', 12, 'Velocity issue'
) AS (shape_type_code, durable_id, external_id, description)) AS q
WHERE q.durable_id NOT IN (SELECT durable_id FROM dim_event_problem_vehicle_code);

-- dim_overtaking_violation_code

INSERT OVERWRITE TABLE dim_overtaking_violation_code
SELECT q.durable_id, q.external_id, q.description
FROM (SELECT STACK(8
    , 'NO_VIOLATION', '492799ba-a72e-44cc-bce7-3eeda187a449', 1
    , 'NIGHT_TIME_OVERTAKING', '5130c642-452d-4caa-a88e-9e7df8c0b9b2', 2
    , 'COMMUTE_HOURS_OVERTAKING', '9048d99e-a49d-43ff-9930-2ddf9e17bdaa', 3
    , 'VEHICLE_IN_FRONT_IS_FAST_OVERTAKING', '7669813e-ce86-4306-9bf5-fe788f35ee80', 4
    , 'SPEED_LIMIT_EXCEEDED_OVERTAKING', '02e88ad2-2bae-40b4-9bc2-e29cb38b3ee4', 5
    , 'OVERTAKING_DURING_ROAD_CONDITION', 'ee45c23f-0071-4065-a771-74ccab3c2502', 6
    , 'NO_OVERTAKING_ZONE_OVERTAKING', '535e7b71-d216-4570-9e0a-9ecf11f9f9c6', 7
    , 'BUS_OVERTAKING', '6059bc9c-e6a9-4309-b84a-9a7482ee5b7a', 8
) AS (description, durable_id, external_id)) AS q
WHERE q.durable_id NOT IN (SELECT durable_id FROM dim_overtaking_violation_code);

