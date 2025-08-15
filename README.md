[![CodeQL](https://github.com/bigboxer23/switchbotapi-java/actions/workflows/codeql.yml/badge.svg)](https://github.com/bigboxer23/switchbotapi-java/actions/workflows/codeql.yml)
[![Build Status](https://github.com/bigboxer23/switchbotapi-java/actions/workflows/unittests.yml/badge.svg)](https://github.com/bigboxer23/switchbotapi-java/actions/workflows/unittests.yml)
[![codecov](https://codecov.io/gh/bigboxer23/switchbotapi-java/branch/main/graph/badge.svg)](https://codecov.io/gh/bigboxer23/switchbotapi-java)

# SwitchBotAPI-java

This project provides a java wrapper over the SwitchBot API v1.1 (https://github.com/OpenWonderLabs/SwitchBotAPI)

It presently implements the device apis, but may be updated in the future to support scenes and webhooks as well.

### Example Usages

1. Listing devices

```
SwitchBotApi instance = SwitchBotApi.getInstance(token, secret);
List<Device> devices = instance.getDeviceApi().getDevices();
```

2. Getting battery status of a device:

```
SwitchBotApi instance = SwitchBotApi.getInstance(token, secret);
List<Device> devices = instance.getDeviceApi().getDevices();
Device status = instance.getDeviceApi().getDeviceStatus(devices.get(0).getDeviceId());
System.out.println("" + status.getBattery());
```

3. Send command to opening a curtain

```
SwitchBotApi instance = SwitchBotApi.getInstance(token, secret);
instance.getDeviceApi().getDevices().stream()
        .filter(device -> IDeviceTypes.CURTAIN.equals(device.getDeviceType()))
        .filter(Device::isMaster)
        .findAny()
        .ifPresent(curtain -> {
            try {
                instance.getDeviceApi()
                        .sendDeviceControlCommands(
                                curtain.getDeviceId(), IDeviceCommands.CURTAIN_CLOSE);
            } catch (IOException theE) {
                theE.printStackTrace();
            }
            });
```

4. Send command to turn on a plug mini

```
SwitchBotApi instance = SwitchBotApi.getInstance(token, secret);
instance.getDeviceApi().getDevices().stream()
        .filter(device -> IDeviceTypes.PLUG_MINI.equals(device.getDeviceType()))
        .findAny()
        .ifPresent(plug -> {
            try {
                instance.getDeviceApi()
                        .sendDeviceControlCommands(
                                plug.getDeviceId(), IDeviceCommands.PLUG_MINI_ON);
            } catch (IOException theE) {
                theE.printStackTrace();
            }
            });
```

