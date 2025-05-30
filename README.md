# agloraemulator

[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/magdel/agloraemulator/blob/main/LICENSE.txt)
[![Hits-of-Code](https://hitsofcode.com/github/magdel/agloraemulator?branch=main&label=Hits-of-Code)](https://hitsofcode.com/github/magdel/agloraemulator/view?branch=main&label=Hits-of-Code)

Emulator sends serial data as would AGLoRa device do to BLE.
Data sending is every second as recorded in source track [raw NMEA 0183](src/main/resources/data/round1.txt)

See Full project (C++, PlatformIO): https://github.com/Udj13/AGLoRa-full/

Mobile client (Dart, Flutter): https://github.com/Udj13/AGLoRa-client-flutter

## Usage

To start application correctly you need IntelliJ IDEA CE.
**Running build from gradle will not work because
of limited support for GUI Designer by Jetbrains and they not gonna improve it (https://youtrack.jetbrains.com/issue/IDEA-223518).**

**Run application in IDE** and specify device id and COM port write data to.

![Sample running view](docs/images/img.png)

## BLE adapter assembly

To send data use ready made modules like JDY-09 (AT-09) and USB-To-TTL (CH340) coupled together. See datasheet for your modules to connect properly. Connect to BLE module from phone
as you do to connect to device.

Front
![Assembly view 1](docs/images/assembly1.jpg)
Back
![Assembly view 2](docs/images/assembly2.jpg)


## Contributing

For simple bug reports and fixes, and feature requests, please simply use projects
[Issue Tracker](../../issues)

