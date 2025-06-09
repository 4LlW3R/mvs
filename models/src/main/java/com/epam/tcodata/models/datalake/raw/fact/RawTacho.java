package com.epam.tcodata.models.datalake.raw.fact;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.raw.RawEntity;

import java.sql.Timestamp;

@SuppressWarnings("CPD-START")
public class RawTacho extends RawEntity {

    private static final long serialVersionUID = 3579732217819218053L;

    public static class Fields {
        public static final String ASSET_ID = "asset_id";
        public static final String TACHO_PARAMETER_DEFINITIONS = "tacho_parameter_definitions";
        public static final String TACHO_INTERVALS = "tacho_intervals";
        public static final String START_DATE_TIME = "start_date_time";
        public static final String END_DATE_TIME = "end_date_time";
        public static final String YEAR = "year";
        public static final String WEEK_NUMBER = "week_number";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.ASSET_ID)
    private Long assetId;
    @ColumnName(Fields.TACHO_PARAMETER_DEFINITIONS)
    private String tachoParameterDefinitions;
    @ColumnName(Fields.TACHO_INTERVALS)
    private String tachoIntervals;
    @ColumnName(Fields.START_DATE_TIME)
    private Timestamp startDateTime;
    @ColumnName(Fields.END_DATE_TIME)
    private Timestamp endDateTime;
    @ColumnName(RawTrip.Fields.YEAR)
    private Integer year;
    @ColumnName(RawTrip.Fields.WEEK_NUMBER)
    private Integer weekNumber;

    public RawTacho() {
        /***  Default implementation ***/
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public String getTachoParameterDefinitions() {
        return tachoParameterDefinitions;
    }

    public void setTachoParameterDefinitions(String tachoParameterDefinitions) {
        this.tachoParameterDefinitions = tachoParameterDefinitions;
    }

    public String getTachoIntervals() {
        return tachoIntervals;
    }

    public void setTachoIntervals(String tachoIntervals) {
        this.tachoIntervals = tachoIntervals;
    }

    public Timestamp getStartDateTime() {
        return startDateTime == null ? null : new Timestamp(startDateTime.getTime());
    }

    public void setStartDateTime(Timestamp startDateTime) {
        this.startDateTime = startDateTime == null ? null : new Timestamp(startDateTime.getTime());
    }

    public Timestamp getEndDateTime() {
        return endDateTime == null ? null : new Timestamp(endDateTime.getTime());
    }

    public void setEndDateTime(Timestamp endDateTime) {
        this.endDateTime = endDateTime == null ? null : new Timestamp(endDateTime.getTime());
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }
}
