package com.bigboxer23.switch_bot;

import com.bigboxer23.switch_bot.data.DeviceCommand;

/** */
public interface IDeviceCommands {
	String TURN_OFF = "turnOff";

	String TURN_ON = "turnOn";

	DeviceCommand CURTAIN_CLOSE = new DeviceCommand(TURN_OFF, "default");

	DeviceCommand CURTAIN_OPEN = new DeviceCommand(TURN_ON, "default");

	DeviceCommand PLUG_MINI_OFF = new DeviceCommand(TURN_OFF, "default");

	DeviceCommand PLUG_MINI_ON = new DeviceCommand(TURN_ON, "default");
}
