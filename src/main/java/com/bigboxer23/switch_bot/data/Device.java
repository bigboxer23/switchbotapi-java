package com.bigboxer23.switch_bot.data;

import com.squareup.moshi.Json;
import lombok.Data;

import java.util.List;

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

	private boolean master;

	private String groupName;

	private List<String> groupingDevicesIds;

	private List<String> curtainDevicesIds;

	private String openDirection;

	private boolean moving;

	private int slidePosition;

	private String power;

	private float voltage;

	@Json(name = "weight")
	private float watts;

	private int electricityOfDay;

	private float electricCurrent; // amps / 10

	private int waterDetectorStatus;

	@Json(name = "CO2")
	private int co2;

	private boolean calibrate;

	private String hubDeviceId;

	private boolean enableCloudService;

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
