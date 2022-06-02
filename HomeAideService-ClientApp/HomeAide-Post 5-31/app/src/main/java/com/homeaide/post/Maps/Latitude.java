package com.homeaide.post.Maps;



public class Latitude {
    static Double latitude;
    static Double longitude;
    String technician;

    public boolean hasLatlong(boolean b) {
        return hasLatlong;
    }

    public void sethasLatlong(boolean hasLatlong) {
        this.hasLatlong = hasLatlong;
    }

    boolean hasLatlong;

    public Double getuserLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getuserLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }



    public String getTechnician() {
        return technician;
    }

    public void setTechnician(String technician) {
        this.technician = technician;
    }
}
