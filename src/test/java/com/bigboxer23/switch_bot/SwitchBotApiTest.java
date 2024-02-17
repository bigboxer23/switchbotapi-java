package com.bigboxer23.switch_bot;

import static org.junit.jupiter.api.Assertions.*;

import com.bigboxer23.switch_bot.data.Device;
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

		try {
			instance.getDeviceApi().getDeviceStatus("123");
			fail();
		} catch (IOException e) {

		}
		for (Device device : instance.getDeviceApi().getDevices()) {
			assertNotNull(device.getDeviceId());
			Device status = instance.getDeviceApi().getDeviceStatus(device.getDeviceId());
			switch (status.getDeviceType()) {
				case IDeviceTypes.HUB2, IDeviceTypes.METER, IDeviceTypes.WOIOSENSOR -> {
					assertTrue(status.getTemperature() > -1);
					assertTrue(status.getHumidity() > -1);
					assertTrue(status.getBattery() >= 0);
					if (IDeviceTypes.HUB2.equals(status.getDeviceType())) {
						assertTrue(status.getLightLevel() >= 0);
					}
				}
				case IDeviceTypes.CURTAIN -> {
					assertTrue(status.getSlidePosition() >= 0);
					assertTrue(status.getMoving() >= 0);
					assertTrue(status.getBattery() >= 0);
				}
				case IDeviceTypes.PLUG_MINI -> {
					assertTrue("on".equals(status.getPower()) || "off".equals(status.getPower()));
					assertTrue(status.getVoltage() > 0);
					assertTrue(status.getWatts() > -1);
					assertTrue(status.getElectricityOfDay() > 0);
					assertTrue(status.getElectricCurrent() > -1);
				}
			}
		}
	}

	@Test
	public void testDeviceCommands() throws IOException {
		SwitchBotApi instance = SwitchBotApi.getInstance(token, secret);
		Device curtain = instance.getDeviceApi().getDevices().stream()
				.filter(device -> IDeviceTypes.CURTAIN.equals(device.getDeviceType()))
				.filter(Device::isMaster)
				.findAny()
				.orElse(null);
		assertNotNull(curtain);
		assertNotNull(curtain.getDeviceId());
		instance.getDeviceApi().sendDeviceControlCommands(curtain.getDeviceId(), IDeviceCommands.CLOSE_CURTAIN);
	}
}
