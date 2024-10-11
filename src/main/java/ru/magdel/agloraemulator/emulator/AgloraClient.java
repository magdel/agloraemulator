package ru.magdel.agloraemulator.emulator;

import com.fazecast.jSerialComm.SerialPort;
import ru.magdel.agloraemulator.util.MapUtil;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class AgloraClient implements TrackPointListener {

    private volatile DeviceTrackPoint lastTrackPoint;
    private final Runnable daemon;
    private Thread thrd;
    private volatile boolean stopped;

    public AgloraClient(final String comPort, final String deviceId,
                        final JLabel jStatus) {
        daemon = new Runnable() {

            public void run() {
                try {
                    SwingUtilities.invokeAndWait(() -> jStatus.setText("Preparing..."));
                    try {

                        var portArray = SerialPort.getCommPorts();
                        var portList = List.of(portArray);
                        System.out.println("COM PORTS: " + portList.stream().map(SerialPort::getSystemPortName).toList());

                        Optional<SerialPort> portOpt = portList.stream().filter(port -> port.getSystemPortName().equals(comPort)).findFirst();
                        if (portOpt.isEmpty()) {
                            SwingUtilities.invokeAndWait(() -> jStatus.setText("Port not found, ports are:" + portList));
                            return;
                        }
                        var port = portOpt.get();

                        SwingUtilities.invokeLater(() -> jStatus.setText("Wait for trackpoint..."));
                        while (lastTrackPoint == null) {
                            Thread.sleep(300);
                        }

                        while (!stopped) {
                            Thread.sleep(1000);
                            SwingUtilities.invokeLater(() -> jStatus.setText("Connecting..."));
                            Thread.sleep(100);
                            try {
                                if (!port.openPort()) {
                                    throw new IOException("Not opened port");
                                }
                                OutputStream os = port.getOutputStream();
                                SwingUtilities.invokeLater(() -> jStatus.setText("Connected"));
                                boolean firstSent = false;
                                DeviceTrackPoint lastSentPoint = lastTrackPoint;
                                for (; ; ) {
                                    DeviceTrackPoint pointToSend = lastSentPoint;
                                    while (pointToSend == lastTrackPoint) {
                                        Thread.sleep(100);
                                    }
                                    pointToSend = lastTrackPoint;
                                    String data = createAgloraString(pointToSend, deviceId);
                                    System.out.println(data);
                                    os.write(data.getBytes(StandardCharsets.US_ASCII));
                                    os.flush();
                                    lastSentPoint = pointToSend;
                                    Thread.sleep(100);
                                    if (!firstSent) {
                                        SwingUtilities.invokeLater(() -> jStatus.setText("Sending..."));
                                        firstSent = true;
                                    }
                                }

                            } catch (IOException ee) {
                                final Exception e = ee;
                                SwingUtilities.invokeLater(() -> jStatus.setText(e.getMessage()));
                            }
                        }
                    } finally {
                        SwingUtilities.invokeLater(() -> jStatus.setText("Finished"));
                    }
                } catch (InvocationTargetException ex) {
                    System.out.println(ex.getMessage());
                } catch (InterruptedException ex) {
                    SwingUtilities.invokeLater(() -> jStatus.setText("Interrupted"));
                }
            }
        };
    }

    static String createAgloraString(DeviceTrackPoint pointToSend, String deviceId) {
        String data = "AGLoRa-newpoint&ver=1.0";
        data += "&name=" + deviceId;
        data += "&lat=" + MapUtil.coordRound5(pointToSend.getLat());
        data += "&lon=" + MapUtil.coordRound5(pointToSend.getLon());
        data += "&spd=" + MapUtil.speedRound1(pointToSend.getSpd());
        data += "&alt=" + pointToSend.getAlt();
        data += "&crs=" + ((int) pointToSend.getCrs());
        data += "&bat=60";
        data += "&tbat=70";
        data += "&timestamp=" + DateTimeFormatter.ISO_DATE_TIME.format(Instant.ofEpochMilli(pointToSend.getTime()).atOffset(ZoneOffset.UTC));//+"Z" ;
        data += "&sat=12\n";
        return data;
    }

    public void positionUpdate(DeviceTrackPoint trackPoint) {
        lastTrackPoint = trackPoint;
    }

    public void start() {
        thrd = new Thread(daemon);
        thrd.setName("Aglora Sender");
        thrd.setDaemon(true);
        thrd.start();
    }

    public void stop() {
        stopped = true;
        thrd.interrupt();
    }
}
