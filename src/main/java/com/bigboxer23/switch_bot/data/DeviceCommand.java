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

	private String commandType;
	private String command;
	private String parameter;
}
