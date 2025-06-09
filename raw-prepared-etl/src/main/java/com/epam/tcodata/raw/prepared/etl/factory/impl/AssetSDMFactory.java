package com.epam.tcodata.raw.prepared.etl.factory.impl;

import com.epam.tcodata.models.datalake.prepared.dimension.PreparedVehicle;
import com.epam.tcodata.models.datalake.prepared.statics.FuelType;
import com.epam.tcodata.models.datalake.prepared.statics.VehicleState;
import com.epam.tcodata.models.datalake.prepared.statics.VehicleType;
import com.epam.tcodata.models.datalake.raw.dimension.RawAsset;
import com.epam.tcodata.raw.prepared.etl.ReferenceSupplier;
import com.epam.tcodata.raw.prepared.etl.converter.ISingleDomainModelConverter;
import com.epam.tcodata.raw.prepared.etl.factory.AbstractSDMFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssetSDMFactory extends AbstractSDMFactory<RawAsset, PreparedVehicle> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetSDMFactory.class);

    private static final long serialVersionUID = -9082928992709214209L;

    public AssetSDMFactory() {
        super(RawAsset.class, PreparedVehicle.class);
    }

    @Override
    public ISingleDomainModelConverter<RawAsset, PreparedVehicle> createConverter(ReferenceSupplier referenceSupplier) {
        return raw -> {
            String orgGroupDurableId = referenceSupplier.getSubGroupDurableId(raw.getSiteId());
            String organizationDurableKey = referenceSupplier.getGroupDurableId(raw.getSubscriptionId());
            VehicleType.Values vehicleType = VehicleType.Values.valueByIntCode(raw.getAssetTypeId());
            String vehicleTypeDurableId = vehicleType == null ? null : vehicleType.getUuid().toString();
            String vehicleTypeName = vehicleType == null ? null : vehicleType.name();
            FuelType.Values fuelType = FuelType.Values.valueByStringCode(raw.getFuelType());
            String fuelTypeDurableId = fuelType == null ? null : fuelType.getUuid().toString();
            VehicleState.Values vehicleState = VehicleState.Values.valueByStringCode(raw.getUserState());
            String vehicleStateDurableId = vehicleState == null ? null : vehicleState.getUuid().toString();

            PreparedVehicle res = new PreparedVehicle();
            res.setDurableId(raw.getDurableId());
            res.setOrganizationDurableKey(organizationDurableKey);
            res.setExternalId(raw.getAssetId());
            res.setPersistedDateUtc(raw.getPersistedDateUtc());
            res.setVehicleTypeDurableKey(vehicleTypeDurableId);
            res.setVehicleTypeCode(vehicleTypeName);
            res.setDescription(raw.getDescription());
            res.setConnectedTrailer(raw.getConnectedTrailer());
            res.setRegistrationNumber(raw.getRegistrationNumber());
            res.setOrgGroupDurableId(orgGroupDurableId);
            res.setFuelTypeDurableKey(fuelTypeDurableId);
            res.setFuelTypeCode(raw.getFuelType());
            res.setTargetFuelConsumption(raw.getTargetFuelConsumption());
            res.setTargetFuelConsumptionUnits(raw.getTargetFuelConsumptionUnits());
            res.setTargetHourlyFuelConsumption(raw.getTargetHourlyFuelConsumption());
            res.setTargetHourlyFuelConsumptionUnits(raw.getTargetHourlyFuelConsumptionUnits());
            res.setFleetNumber(raw.getFleetNumber());
            res.setMake(raw.getMake());
            res.setModel(raw.getModel());
            res.setYear(raw.getYear());
            res.setVinNumber(raw.getVinNumber());
            res.setEngineNumber(raw.getEngineNumber());
            res.setFmVehicleId(raw.getFmVehicleId());
            res.setAdditionalMobileDevice(raw.getAdditionalMobileDevice());
            res.setNotes(raw.getNotes());
            res.setVehicleStateDurableKey(vehicleStateDurableId);
            res.setVehicleStateCode(raw.getUserState());
            res.setCreatedBy(raw.getCreatedBy());
            res.setCreatedDate(raw.getCreatedDate());
            res.setOdometer(raw.getOdometer());
            res.setEngineSeconds(engineHoursToSeconds(raw.getEngineHours()));

            return res;
        };
    }

    Long engineHoursToSeconds(String engineHours) {
        Long result = 0L;
        long hoursInDays = 24L;
        long minutesInHours = 60L;
        long secondsInMinutes = 60L;
        boolean brokenFormat = true;

        if (engineHours == null) {
            result = null;
        } else {
            Matcher matcher = Pattern.compile("P(\\d+D)?T((\\d+H)?(\\d+M)?(\\d+S)?)").matcher(engineHours);
            if (matcher.find()) {
                result = checkEachCase(engineHours, result, hoursInDays, minutesInHours, secondsInMinutes, brokenFormat);
            } else {
                LOGGER.warn("Broken engine hours format: {}", engineHours);
                result = -1L;
            }
        }
        return result;
    }

    private static Long checkEachCase(String engineHours, Long result, long hoursInDays, long minutesInHours, long secondsInMinutes, boolean brokenFormat) {
        Matcher matcher;
        String dayRegex = "(\\d+)D";
        matcher = Pattern.compile(dayRegex).matcher(engineHours);
        if (matcher.matches()) {
            result += Integer.parseInt(matcher.group(1)) * hoursInDays * minutesInHours * secondsInMinutes;
            brokenFormat = false;
        }

        String hourRegex = "(\\d+)H";
        matcher = Pattern.compile(hourRegex).matcher(engineHours);
        if (matcher.matches()) {
            result += Integer.parseInt(matcher.group(1)) * minutesInHours * secondsInMinutes;
            brokenFormat = false;
        }

        String minRegex = "(\\d+)M";
        matcher = Pattern.compile(minRegex).matcher(engineHours);
        if (matcher.matches()) {
            result += Integer.parseInt(matcher.group(1)) * secondsInMinutes;
            brokenFormat = false;
        }

        String secRegex = "(\\d+)S";
        matcher = Pattern.compile(secRegex).matcher(engineHours);
        if (matcher.matches()) {
            result += Integer.parseInt(matcher.group(1));
            brokenFormat = false;
        }

        if (brokenFormat) {
            LOGGER.warn("Broken engine hours format (PT no day/hour/minute/second info): {}", engineHours);
            result = -2L;
        }
        return result;
    }
}
