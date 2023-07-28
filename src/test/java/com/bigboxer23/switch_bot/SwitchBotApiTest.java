package com.bigboxer23.switch_bot;

import static org.junit.jupiter.api.Assertions.*;

import com.bigboxer23.switch_bot.data.Device;
import com.bigboxer23.switch_bot.data.DeviceCommand;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Need to define environment variables for SwitchBotToken/SwitchBotSecret to run tests */
public class SwitchBotApiTest {
	private final String token = System.getenv("SwitchBotToken");

	private final String secret = System.getenv("SwitchBotSecret");

	@Test
	public void testGetDevices() throws IOException {
		SwitchBotApi instance = SwitchBotApi.getInstance(token, secret);
		List<Device> devices = instance.getDeviceApi().getDevices();
		assertFalse(devices.isEmpty());
		assertNotNull(devices.get(0).getDeviceId());
	}

	@Test
	public void testDeviceStatus() throws IOException {
		SwitchBotApi instance = SwitchBotApi.getInstance(token, secret);
		assertNull(instance.getDeviceApi().getDeviceStatus("123"));
		for (Device device : instance.getDeviceApi().getDevices()) {
			assertNotNull(device.getDeviceId());
			Device status = instance.getDeviceApi().getDeviceStatus(device.getDeviceId());
			switch (status.getDeviceType()) {
				case "Hub 2", "Meter" -> {
					assertTrue(status.getTemperature() > 0);
					assertTrue(status.getHumidity() > 0);
					if ("Hub 2".equals(status.getDeviceType())) {
						assertTrue(status.getLightLevel() >= 0);
					}
				}
				case "Curtain" -> {
					assertTrue(status.getSlidePosition() >= 0);
					assertTrue(status.getMoving() >= 0);
					assertTrue(status.getBattery() >= 0);
				}
			}
		}
	}

	@Test
	public void testDeviceCommands() throws IOException {
		SwitchBotApi instance = SwitchBotApi.getInstance(token, secret);
		Device curtain = instance.getDeviceApi().getDevices().stream()
				.filter(device -> "Curtain".equals(device.getDeviceType()))
				.filter(Device::isMaster)
				.findAny()
				.orElse(null);
		assertNotNull(curtain);
		assertNotNull(curtain.getDeviceId());
		instance.getDeviceApi()
				.sendDeviceControlCommands(curtain.getDeviceId(), new DeviceCommand("turnOff", "default"));
	}
}
