package com.bigboxer23.switch_bot;

import com.bigboxer23.switch_bot.data.DeviceCommand;

/** */
public interface IDeviceCommands {
	String TURN_OFF = "turnOff";

	String TURN_ON = "turnOn";

	String SET_POSITION = "setPosition";

	DeviceCommand CURTAIN_CLOSE = new DeviceCommand(TURN_OFF, "default");

	DeviceCommand CURTAIN_OPEN = new DeviceCommand(TURN_ON, "default");

	DeviceCommand PLUG_MINI_OFF = new DeviceCommand(TURN_OFF, "default");

	DeviceCommand PLUG_MINI_ON = new DeviceCommand(TURN_ON, "default");

	DeviceCommand ROLLER_SHADE_CLOSE = new DeviceCommand(SET_POSITION, 100);

	DeviceCommand ROLLER_SHADE_OPEN = new DeviceCommand(SET_POSITION, 0);

	static DeviceCommand rollerShadePosition(int position) {
		return new DeviceCommand(SET_POSITION, position);
	}
}
