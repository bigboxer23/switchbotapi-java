package com.bigboxer23.switch_bot.data;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ApiResponseBodyTest {

	@Test
	public void testDefaultConstructor() {
		ApiResponseBody body = new ApiResponseBody();
		assertNotNull(body);
		assertNull(body.getDeviceList());
	}

	@Test
	public void testSetAndGetDeviceList() {
		ApiResponseBody body = new ApiResponseBody();

		Device device1 = new Device();
		device1.setDeviceId("device1");
		device1.setDeviceName("Test Device 1");

		Device device2 = new Device();
		device2.setDeviceId("device2");
		device2.setDeviceName("Test Device 2");

		List<Device> devices = Arrays.asList(device1, device2);
		body.setDeviceList(devices);

		assertEquals(devices, body.getDeviceList());
		assertEquals(2, body.getDeviceList().size());
		assertEquals("device1", body.getDeviceList().get(0).getDeviceId());
		assertEquals("Test Device 1", body.getDeviceList().get(0).getDeviceName());
	}

	@Test
	public void testSetDeviceListWithEmptyList() {
		ApiResponseBody body = new ApiResponseBody();
		List<Device> emptyList = Collections.emptyList();

		body.setDeviceList(emptyList);

		assertEquals(emptyList, body.getDeviceList());
		assertTrue(body.getDeviceList().isEmpty());
	}

	@Test
	public void testSetDeviceListWithNull() {
		ApiResponseBody body = new ApiResponseBody();
		body.setDeviceList(null);

		assertNull(body.getDeviceList());
	}

	@Test
	public void testEqualsAndHashCode() {
		ApiResponseBody body1 = new ApiResponseBody();
		ApiResponseBody body2 = new ApiResponseBody();

		assertEquals(body1, body2);
		assertEquals(body1.hashCode(), body2.hashCode());

		Device device = new Device();
		device.setDeviceId("test");
		List<Device> devices = List.of(device);

		body1.setDeviceList(devices);
		body2.setDeviceList(devices);

		assertEquals(body1, body2);
		assertEquals(body1.hashCode(), body2.hashCode());
	}

	@Test
	public void testToString() {
		ApiResponseBody body = new ApiResponseBody();
		String toString = body.toString();

		assertNotNull(toString);
		assertTrue(toString.contains("ApiResponseBody"));
		assertTrue(toString.contains("deviceList"));
	}

	@Test
	public void testSetDeviceListModification() {
		ApiResponseBody body = new ApiResponseBody();

		Device device = new Device();
		device.setDeviceId("original");
		List<Device> devices = List.of(device);

		body.setDeviceList(devices);
		assertEquals(1, body.getDeviceList().size());
		assertEquals("original", body.getDeviceList().get(0).getDeviceId());

		device.setDeviceId("modified");
		assertEquals("modified", body.getDeviceList().get(0).getDeviceId());
	}
}
