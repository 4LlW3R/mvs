use raw;

alter table dim_library_event rename to dim_library_event_bak;
alter table dim_asset rename to dim_asset_bak;
alter table dim_driver rename to dim_driver_bak;
alter table dim_location rename to dim_location_bak;
alter table dim_organisation_group rename to dim_organisation_group_bak;
alter table dim_organisation_subgroup rename to dim_organisation_subgroup_bak;

create table dim_library_event as select * from dim_library_event_bak where (ingested_date_utc >= (select max(ingested_date_utc) from dim_library_event_bak) - interval '1' month);
create table dim_asset as select * from dim_asset_bak where (ingested_date_utc >= (select max(ingested_date_utc) from dim_asset_bak) - interval '1' month);
create table dim_driver as select * from dim_driver_bak where (ingested_date_utc >= (select max(ingested_date_utc) from dim_driver_bak) - interval '1' month);
create table dim_location as select * from dim_location_bak where (ingested_date_utc >= (select max(ingested_date_utc) from dim_location_bak) - interval '1' month);
create table dim_organisation_group as select * from dim_organisation_group_bak where (ingested_date_utc >= (select max(ingested_date_utc) from dim_organisation_group_bak) - interval '1' month);
create table dim_organisation_subgroup as select * from dim_organisation_subgroup_bak where (ingested_date_utc >= (select max(ingested_date_utc) from dim_organisation_subgroup_bak) - interval '1' month);

drop table dim_library_event_bak;
drop table dim_asset_bak;
drop table dim_driver_bak;
drop table dim_location_bak;
drop table dim_organisation_group_bak;
drop table dim_organisation_subgroup_bak;