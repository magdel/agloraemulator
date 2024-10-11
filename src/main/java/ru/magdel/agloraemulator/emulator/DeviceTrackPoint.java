package ru.magdel.agloraemulator.emulator;

import java.util.Date;

public class DeviceTrackPoint {

    private final double lat;
    private final double lon;
    private final double spd;
    private final double crs;
    private final int alt;
    private final long time;

    public DeviceTrackPoint(double lat, double lon, double spd, double crs, int alt, long time) {
        this.lat=lat;
        this.lon=lon;
        this.spd=spd;
        this.crs=crs;
        this.alt=alt;
        this.time=time;
    }

    public DeviceTrackPoint toTime(long newTime) {
        return new DeviceTrackPoint(lat, lon, spd, crs, alt, newTime);
    }

    public int getAlt() {
        return alt;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public double getSpd() {
        return spd;
    }

    public long getTime() {
        return time;
    }

    public double getCrs() {
        return crs;
    }

    @Override
    public String toString() {
        return "DTP{dt="+(new Date(time))+";spd="+spd+"}";

    }
}
