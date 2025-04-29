package com.bigboxer23.switch_bot.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.bigboxer23.switch_bot.IDeviceCommands;
import com.bigboxer23.switch_bot.IDeviceTypes;
import com.bigboxer23.switch_bot.SwitchBotApi;
import com.bigboxer23.switch_bot.data.Device;
import com.bigboxer23.utils.command.Command;
import com.bigboxer23.utils.properties.PropertyUtils;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("integration")
public class SwitchBotApiIntegrationTest {
	private static final String token = PropertyUtils.getProperty("switchbot_token");
	private static final String secret = PropertyUtils.getProperty("switchbot_secret");
	private static final SwitchBotApi instance = SwitchBotApi.getInstance(token, secret);

	@Test
	public void testGetDevices() throws IOException {
		List<Device> devices = instance.getDeviceApi().getDevices();
		assertFalse(devices.isEmpty(), "Device list should not be empty");
		assertNotNull(devices.get(0).getDeviceId());
	}

	@Test
	public void getDeviceNameFromId() throws IOException {
		Device device = instance.getDeviceApi().getDevices().get(0);
		String deviceName = instance.getDeviceApi().getDeviceNameFromId(device.getDeviceId());
		assertEquals(device.getDeviceName(), deviceName);
		assertEquals("test", instance.getDeviceApi().getDeviceNameFromId("test"));
		assertNull(instance.getDeviceApi().getDeviceNameFromId(null));
	}

	@Test
	public void testDeviceStatus() throws IOException {
		try {
			instance.getDeviceApi().getDeviceStatus("123");
			fail("Expected IOException for invalid device ID");
		} catch (IOException ignored) {
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
					assertTrue(status.getBattery() >= 0);
				}
				case IDeviceTypes.PLUG_MINI -> {
					assertTrue("on".equals(status.getPower()) || "off".equals(status.getPower()));
					assertTrue(status.getVoltage() > -1);
					assertTrue(status.getWatts() > -1);
					assertTrue(status.getElectricityOfDay() >= 0);
					assertTrue(status.getElectricCurrent() > -1);
				}
				case IDeviceTypes.WATER_DETECTOR -> assertTrue(status.isWet() || status.isDry());
				case IDeviceTypes.METER_PRO_CO2 -> assertTrue(status.getCo2() > 0);
			}
		}
	}

	@Test
	public void testCurtainDeviceCommands() throws IOException, InterruptedException {
		Device curtain = instance.getDeviceApi().getDevices().stream()
				.filter(device -> IDeviceTypes.CURTAIN.equals(device.getDeviceType()))
				.filter(Device::isMaster)
				.findAny()
				.orElse(null);
		assertNotNull(curtain);
		assertNotNull(curtain.getDeviceId());

		instance.getDeviceApi().sendDeviceControlCommands(curtain.getDeviceId(), IDeviceCommands.CURTAIN_CLOSE);
		pollForStatus(() ->
				instance.getDeviceApi().getDeviceStatus(curtain.getDeviceId()).getSlidePosition() >= 90);

		instance.getDeviceApi().sendDeviceControlCommands(curtain.getDeviceId(), IDeviceCommands.CURTAIN_OPEN);
		pollForStatus(() ->
				instance.getDeviceApi().getDeviceStatus(curtain.getDeviceId()).getSlidePosition() == 0);
	}

	@Test
	public void testPlugDeviceCommands() throws IOException, InterruptedException {
		Device plug = instance.getDeviceApi().getDevices().stream()
				.filter(device -> IDeviceTypes.PLUG_MINI.equals(device.getDeviceType()))
				.findAny()
				.orElse(null);
		assertNotNull(plug);

		instance.getDeviceApi().sendDeviceControlCommands(plug.getDeviceId(), IDeviceCommands.PLUG_MINI_OFF);
		pollForStatus(() ->
				!instance.getDeviceApi().getDeviceStatus(plug.getDeviceId()).isPowerOn());

		instance.getDeviceApi().sendDeviceControlCommands(plug.getDeviceId(), IDeviceCommands.PLUG_MINI_ON);
		pollForStatus(() ->
				instance.getDeviceApi().getDeviceStatus(plug.getDeviceId()).isPowerOn());
	}

	private void pollForStatus(Command<Boolean> command) throws IOException, InterruptedException {
		boolean result = command.execute();
		for (int i = 0; i < 10 && !result; i++) {
			Thread.sleep(2000);
			result = command.execute();
		}
		assertTrue(result);
	}
}
