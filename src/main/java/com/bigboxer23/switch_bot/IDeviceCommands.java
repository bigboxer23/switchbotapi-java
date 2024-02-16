package com.bigboxer23.switch_bot;

import com.bigboxer23.switch_bot.data.DeviceCommand;

/**
 *
 */
public interface IDeviceCommands
{
	String TURN_OFF = "turnOff";

	String TURN_ON = "turnOn";

	DeviceCommand CLOSE_CURTAIN = new DeviceCommand(TURN_OFF, "default");

	DeviceCommand OPEN_CURTAIN = new DeviceCommand(TURN_ON, "default");
}
