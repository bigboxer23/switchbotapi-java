package com.bigboxer23.switch_bot;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class IDeviceTypesTest {

	@Test
	public void testDeviceTypeConstants() {
		assertEquals("Curtain", IDeviceTypes.CURTAIN);
		assertEquals("Hub 2", IDeviceTypes.HUB2);
		assertEquals("Meter", IDeviceTypes.METER);
		assertEquals("WoIOSensor", IDeviceTypes.WOIOSENSOR);
		assertEquals("Plug Mini (US)", IDeviceTypes.PLUG_MINI);
		assertEquals("Water Detector", IDeviceTypes.WATER_DETECTOR);
		assertEquals("MeterPro(CO2)", IDeviceTypes.METER_PRO_CO2);
		assertEquals("Roller Shade", IDeviceTypes.ROLLER_SHADE);
	}

	@Test
	public void testDeviceTypeConstantsAreNotNull() {
		assertNotNull(IDeviceTypes.CURTAIN);
		assertNotNull(IDeviceTypes.HUB2);
		assertNotNull(IDeviceTypes.METER);
		assertNotNull(IDeviceTypes.WOIOSENSOR);
		assertNotNull(IDeviceTypes.PLUG_MINI);
		assertNotNull(IDeviceTypes.WATER_DETECTOR);
		assertNotNull(IDeviceTypes.METER_PRO_CO2);
		assertNotNull(IDeviceTypes.ROLLER_SHADE);
	}

	@Test
	public void testDeviceTypeUniqueness() {
		String[] deviceTypes = {
			IDeviceTypes.CURTAIN,
			IDeviceTypes.HUB2,
			IDeviceTypes.METER,
			IDeviceTypes.WOIOSENSOR,
			IDeviceTypes.PLUG_MINI,
			IDeviceTypes.WATER_DETECTOR,
			IDeviceTypes.METER_PRO_CO2,
			IDeviceTypes.ROLLER_SHADE
		};

		for (int i = 0; i < deviceTypes.length; i++) {
			for (int j = i + 1; j < deviceTypes.length; j++) {
				assertNotEquals(
						deviceTypes[i],
						deviceTypes[j],
						"Device types should be unique: " + deviceTypes[i] + " vs " + deviceTypes[j]);
			}
		}
	}
}
