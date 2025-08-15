package com.bigboxer23.switch_bot.data;

import static org.junit.jupiter.api.Assertions.*;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DeviceTest {

	private Device device;
	private Moshi moshi;
	private JsonAdapter<Device> adapter;

	@BeforeEach
	void setUp() {
		device = new Device();
		moshi = new Moshi.Builder().build();
		adapter = moshi.adapter(Device.class);
	}

	@Test
	public void testIsDryWithDryStatus() {
		device.setWaterDetectorStatus(0);
		assertTrue(device.isDry());
		assertFalse(device.isWet());
	}

	@Test
	public void testIsWetWithWetStatus() {
		device.setWaterDetectorStatus(1);
		assertTrue(device.isWet());
		assertFalse(device.isDry());
	}

	@Test
	public void testWaterDetectorStatusEdgeCases() {
		device.setWaterDetectorStatus(-1);
		assertFalse(device.isDry());
		assertFalse(device.isWet());

		device.setWaterDetectorStatus(2);
		assertFalse(device.isDry());
		assertFalse(device.isWet());
	}

	@Test
	public void testIsPowerOnWithOnStatus() {
		device.setPower("on");
		assertTrue(device.isPowerOn());

		device.setPower("ON");
		assertTrue(device.isPowerOn());

		device.setPower("On");
		assertTrue(device.isPowerOn());
	}

	@Test
	public void testIsPowerOnWithOffStatus() {
		device.setPower("off");
		assertFalse(device.isPowerOn());

		device.setPower("OFF");
		assertFalse(device.isPowerOn());

		device.setPower("Off");
		assertFalse(device.isPowerOn());
	}

	@Test
	public void testIsPowerOnWithNullPower() {
		device.setPower(null);
		assertFalse(device.isPowerOn());
	}

	@Test
	public void testIsPowerOnWithInvalidPower() {
		device.setPower("invalid");
		assertFalse(device.isPowerOn());

		device.setPower("");
		assertFalse(device.isPowerOn());

		device.setPower("  ");
		assertFalse(device.isPowerOn());
	}

	@Test
	public void testDeviceBasicProperties() {
		device.setDeviceId("test-device-123");
		device.setDeviceName("Test Device");
		device.setDeviceType("Plug");
		device.setHumidity(45);
		device.setTemperature(23.5f);
		device.setLightLevel(300);
		device.setVersion("1.2.3");
		device.setBattery(85);

		assertEquals("test-device-123", device.getDeviceId());
		assertEquals("Test Device", device.getDeviceName());
		assertEquals("Plug", device.getDeviceType());
		assertEquals(45, device.getHumidity());
		assertEquals(23.5f, device.getTemperature(), 0.01);
		assertEquals(300, device.getLightLevel());
		assertEquals("1.2.3", device.getVersion());
		assertEquals(85, device.getBattery());
	}

	@Test
	public void testDeviceGroupProperties() {
		device.setGroup(true);
		device.setMaster(false);
		device.setGroupName("Living Room Group");
		device.setGroupingDevicesIds(Arrays.asList("device1", "device2", "device3"));
		device.setCurtainDevicesIds(Arrays.asList("curtain1", "curtain2"));

		assertTrue(device.isGroup());
		assertFalse(device.isMaster());
		assertEquals("Living Room Group", device.getGroupName());
		assertEquals(3, device.getGroupingDevicesIds().size());
		assertEquals("device1", device.getGroupingDevicesIds().get(0));
		assertEquals(2, device.getCurtainDevicesIds().size());
		assertEquals("curtain1", device.getCurtainDevicesIds().get(0));
	}

	@Test
	public void testDeviceCurtainProperties() {
		device.setOpenDirection("left");
		device.setMoving(true);
		device.setSlidePosition(75);

		assertEquals("left", device.getOpenDirection());
		assertTrue(device.isMoving());
		assertEquals(75, device.getSlidePosition());
	}

	@Test
	public void testDeviceElectricalProperties() {
		device.setVoltage(120.5f);
		device.setWatts(15.3f);
		device.setElectricityOfDay(250);
		device.setElectricCurrent(1.25f);

		assertEquals(120.5f, device.getVoltage(), 0.01);
		assertEquals(15.3f, device.getWatts(), 0.01);
		assertEquals(250, device.getElectricityOfDay());
		assertEquals(1.25f, device.getElectricCurrent(), 0.01);
	}

	@Test
	public void testDeviceEnvironmentalProperties() {
		device.setCo2(400);
		device.setCalibrate(true);
		device.setHubDeviceId("hub-123");
		device.setEnableCloudService(false);

		assertEquals(400, device.getCo2());
		assertTrue(device.isCalibrate());
		assertEquals("hub-123", device.getHubDeviceId());
		assertFalse(device.isEnableCloudService());
	}

	@Test
	public void testDeviceJsonSerialization() throws IOException {
		device.setDeviceId("json-test-device");
		device.setDeviceName("JSON Test Device");
		device.setWatts(25.8f);
		device.setCo2(350);

		String json = adapter.toJson(device);
		assertNotNull(json);
		assertTrue(json.contains("json-test-device"));
		assertTrue(json.contains("JSON Test Device"));
		assertTrue(json.contains("\"weight\":25.8"));
		assertTrue(json.contains("\"CO2\":350"));
	}

	@Test
	public void testDeviceJsonDeserialization() throws IOException {
		String json = "{"
				+ "\"deviceId\":\"deserialize-test\","
				+ "\"deviceName\":\"Deserialize Test\","
				+ "\"power\":\"on\","
				+ "\"waterDetectorStatus\":1,"
				+ "\"weight\":30.5,"
				+ "\"CO2\":420"
				+ "}";

		Device deserializedDevice = adapter.fromJson(json);

		assertNotNull(deserializedDevice);
		assertEquals("deserialize-test", deserializedDevice.getDeviceId());
		assertEquals("Deserialize Test", deserializedDevice.getDeviceName());
		assertEquals("on", deserializedDevice.getPower());
		assertTrue(deserializedDevice.isPowerOn());
		assertEquals(1, deserializedDevice.getWaterDetectorStatus());
		assertTrue(deserializedDevice.isWet());
		assertEquals(30.5f, deserializedDevice.getWatts(), 0.01);
		assertEquals(420, deserializedDevice.getCo2());
	}

	@Test
	public void testDeviceJsonDeserializationWithNullValues() throws IOException {
		String json = "{" + "\"deviceId\":\"null-test\"," + "\"power\":null," + "\"groupName\":null" + "}";

		Device deserializedDevice = adapter.fromJson(json);

		assertNotNull(deserializedDevice);
		assertEquals("null-test", deserializedDevice.getDeviceId());
		assertNull(deserializedDevice.getPower());
		assertFalse(deserializedDevice.isPowerOn());
		assertNull(deserializedDevice.getGroupName());
	}

	@Test
	public void testDeviceDefaultValues() {
		Device newDevice = new Device();

		assertEquals(0, newDevice.getHumidity());
		assertEquals(0.0f, newDevice.getTemperature(), 0.01);
		assertEquals(0, newDevice.getLightLevel());
		assertEquals(0, newDevice.getBattery());
		assertFalse(newDevice.isGroup());
		assertFalse(newDevice.isMaster());
		assertFalse(newDevice.isMoving());
		assertEquals(0, newDevice.getSlidePosition());
		assertEquals(0.0f, newDevice.getVoltage(), 0.01);
		assertEquals(0.0f, newDevice.getWatts(), 0.01);
		assertEquals(0, newDevice.getElectricityOfDay());
		assertEquals(0.0f, newDevice.getElectricCurrent(), 0.01);
		assertEquals(0, newDevice.getWaterDetectorStatus());
		assertEquals(0, newDevice.getCo2());
		assertFalse(newDevice.isCalibrate());
		assertFalse(newDevice.isEnableCloudService());
	}
}
