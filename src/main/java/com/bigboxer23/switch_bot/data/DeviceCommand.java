package com.bigboxer23.switch_bot.data;

import lombok.Data;

/** */
@Data
public class DeviceCommand {
	public DeviceCommand(String command, String parameter) {
		this.command = command;
		this.commandType = "command";
		this.parameter = parameter;
	}

	public DeviceCommand(String command, int parameter) {
		this.command = command;
		this.commandType = "command";
		this.parameter = parameter;
	}

	private String commandType;
	private String command;
	private Object parameter;
}
