package com.bigboxer23.switch_bot.data;

import com.squareup.moshi.Json;
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

	private boolean moving;

	private int slidePosition;

	private boolean master;

	private String power;

	private float voltage;

	@Json(name = "weight")
	private float watts;

	private int electricityOfDay;

	private float electricCurrent; // amps / 10

	@Json(name = "status")
	private int waterDetectorStatus;

	public boolean isDry() {
		return waterDetectorStatus == 0;
	}

	public boolean isWet() {
		return waterDetectorStatus == 1;
	}

	public boolean isPowerOn() {
		return "on".equalsIgnoreCase(getPower());
	}
}
