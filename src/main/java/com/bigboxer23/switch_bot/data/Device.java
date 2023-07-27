package com.bigboxer23.switch_bot.data;

import lombok.Data;

/** */
@Data
public class Device {
	private String deviceId;

	private String deviceName;

	private String deviceType;

	private int humidity;

	private float temperature;

	private int lightLevel;

	private String version;

	private int battery;

	private boolean group;

	private int moving;

	private int slidePosition;
}
