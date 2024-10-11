package ru.magdel.agloraemulator.emulator;

import ru.magdel.agloraemulator.util.MapUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DeviceTrack {

    private final List<DeviceTrackPoint> track = new ArrayList<DeviceTrackPoint>();
    private final Calendar calendar;
    private final long startTime;
    private final long endTime;
    private final long duration;
    private final Runnable daemon;
    private Thread thrd;
    private List<TrackPointListener> listeners = new ArrayList<TrackPointListener>();

    public DeviceTrack(InputStream nmeaInputStream) {
        calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.setTimeInMillis(System.currentTimeMillis());

        Reader r = new InputStreamReader(nmeaInputStream);
        StringBuffer line = new StringBuffer(200);
        char c;
        int bR;
        try {
            while ((bR = r.read()) >= 0) {
                c = (char) bR;
                if (c == (char) 13) {
                    try {
                        addLine(line);
                    } catch (Exception e) {
                    }
                    line.setLength(0);
                } else {
                    line.append(c);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(DeviceTrack.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalArgumentException(ex);
        }
        try {
            r.close();
        } catch (IOException ex) {
            Logger.getLogger(DeviceTrack.class.getName()).log(Level.SEVERE, null, ex);
        }
        startTime = track.get(0).getTime();
        endTime = track.get(track.size() - 1).getTime();
        duration = startTime - endTime;

        daemon = new Runnable() {

            public void run() {
                try {
                    for (; ; ) {
                        long timeDiff = System.currentTimeMillis() - startTime;
                        timeDiff = (timeDiff / 1000) * 1000;
                        Thread.sleep(100);
                        for (int i = 0; i < track.size(); i++) {
                            DeviceTrackPoint point = track.get(i);
                            while (point.getTime() > System.currentTimeMillis() - timeDiff) {
                                Thread.sleep(100);
                            }
                            DeviceTrackPoint sendPoint = point.toTime(point.getTime() + timeDiff);
                            for (TrackPointListener listener : listeners) {
                                listener.positionUpdate(sendPoint);
                            }
                            Thread.sleep(50);
                        }
                    }
                } catch (InterruptedException ex) {
                    //   Logger.getLogger(DeviceTrack.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        };
    }

    private void addLine(StringBuffer line) {
        String[] elems;
        try {
            //       0          1 2         3  4         5  6      7     8
            //$GPRMC,213931.000,A,5955.6719,N,03023.3435,E,29.35,348.43,271109,,*37
            elems = MapUtil.parseString(line.toString(), ',');
        } catch (Exception ex) {
            Logger.getLogger(DeviceTrack.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalArgumentException(ex);
        }
        String s = elems[0].substring(0, 2);
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(s));
        s = elems[0].substring(2, 4);
        calendar.set(Calendar.MINUTE, Integer.parseInt(s));
        s = elems[0].substring(4, 6);
        calendar.set(Calendar.SECOND, Integer.parseInt(s));

        s = elems[8].substring(0, 2);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(s));
        s = elems[8].substring(2, 4);
        calendar.set(Calendar.MONTH, Integer.parseInt(s) - 1);
        s = elems[8].substring(4, 6);
        calendar.set(Calendar.YEAR, 2000 + Integer.parseInt(s));
        long time = calendar.getTimeInMillis();

        s = elems[2].substring(0, 2);
        String s1 = elems[2].substring(2, 9);
        double lat = Double.parseDouble(s) + Double.parseDouble(s1) / 60.;
        s = elems[4].substring(0, 3);
        s1 = elems[4].substring(3, 10);
        double lon = Double.parseDouble(s) + Double.parseDouble(s1) / 60.;
        s = elems[6];
        double spd = Double.parseDouble(s);
        s = elems[7];
        double crs = Double.parseDouble(s);
        track.add(new DeviceTrackPoint(lat, lon, spd, crs, 0, time));
    }

    public void registerListener(TrackPointListener listener) {
        listeners.add(listener);
    }

    public void unregisterListener(TrackPointListener listener) {
        listeners.remove(listener);
    }

    public void start() {
        thrd = new Thread(daemon);
        thrd.setName("Position feeder");
        thrd.setDaemon(true);
        thrd.start();
    }

    public void stop() {
        thrd.interrupt();
    }
}
