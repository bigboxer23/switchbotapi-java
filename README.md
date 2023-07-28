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
        .filter(device -> "Curtain".equals(device.getDeviceType()))
        .filter(Device::isMaster)
        .findAny()
        .ifPresent(curtain -> {
            try {
                instance.getDeviceApi()
                        .sendDeviceControlCommands(
                                curtain.getDeviceId(), new DeviceCommand("turnOff", "default"));
            } catch (IOException theE) {
                theE.printStackTrace();
            }
            });
```

