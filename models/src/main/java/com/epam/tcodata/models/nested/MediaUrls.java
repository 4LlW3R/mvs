package com.epam.tcodata.models.nested;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.io.Serializable;

/**
 * Nested in mix events.
 */
public class MediaUrls implements Serializable {

    private static final long serialVersionUID = 388280489623197043L;

    private String road;
    private String cab;
    private String camera3;
    private String camera4;

    public MediaUrls() {
    }

    /**
     * Main constructor.
     * @param road road
     * @param cab cab
     * @param camera3 camera3
     * @param camera4 camera4
     */
    public MediaUrls(String road, String cab, String camera3, String camera4) {
        this.road = road;
        this.cab = cab;
        this.camera3 = camera3;
        this.camera4 = camera4;
    }

    public String getCab() {
        return cab;
    }

    public void setCab(String cab) {
        this.cab = cab;
    }

    public String getCamera3() {
        return camera3;
    }

    @JsonSetter("Camera_3")
    public void setCamera3(String camera3) {
        this.camera3 = camera3;
    }


    public String getCamera4() {
        return camera4;
    }

    @JsonSetter("Camera_4")
    public void setCamera4(String camera4) {
        this.camera4 = camera4;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MediaUrls mediaUrls = (MediaUrls) o;

        if (road != null ? !road.equals(mediaUrls.road) : mediaUrls.road != null) return false;
        if (cab != null ? !cab.equals(mediaUrls.cab) : mediaUrls.cab != null) return false;
        if (camera3 != null ? !camera3.equals(mediaUrls.camera3) : mediaUrls.camera3 != null) return false;
        return camera4 != null ? camera4.equals(mediaUrls.camera4) : mediaUrls.camera4 == null;
    }

    @Override
    public int hashCode() {
        int result = road != null ? road.hashCode() : 0;
        result = 31 * result + (cab != null ? cab.hashCode() : 0);
        result = 31 * result + (camera3 != null ? camera3.hashCode() : 0);
        result = 31 * result + (camera4 != null ? camera4.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MediaUrls{"
                + "road='" + road + '\''
                + ", cab='" + cab + '\''
                + ", camera3='" + camera3 + '\''
                + ", camera4='" + camera4 + '\''
                + '}';
    }
}
