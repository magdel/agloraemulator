package ru.magdel.agloraemulator.emulator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AgloraClientTest {

    @Test
    void createAgloraString() {
        var data = AgloraClient.createAgloraString(new DeviceTrackPoint(60.2234d, 30.53434d, 21.3d, 123d, 12345, 965632345656L),
                "DEV-ID-877654.234-1", false);
        assertEquals("AGLoRa-newpoint&ver=1.0&name=DEV-ID-877654.234-1&lat=60.2234&lon=30.53434&spd=21.3&alt=12345&crs=123&bat=60&tbat=70&timestamp=2000-08-07T07:12:25Z&sat=12\r\n",
                data);
    }

    @Test
    void createCompactAgloraString() {
        var data = AgloraClient.createAgloraString(new DeviceTrackPoint(60.2234d, 30.53434d, 21.3d, 123d, 12345, 965632345656L),
                "DEV-ID-877654.234-1", true);
        assertEquals("AGLoRaN&ver=2.0&n=DEV-ID-877654.234-1&a=60.2234&o=30.53434&s=21.3&h=12345&c=123&dn=PCEmulator&db=77&c=123&b=33&t=2000-08-07T07:12:25Z&sat=12\r\n",
                data);
    }
}