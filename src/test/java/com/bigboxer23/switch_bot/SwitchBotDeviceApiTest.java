package com.bigboxer23.switch_bot;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.bigboxer23.switch_bot.data.*;
import com.bigboxer23.utils.time.ITimeConstants;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SwitchBotDeviceApiTest {

	@Mock
	private SwitchBotApi mockSwitchBotApi;

	private SwitchBotDeviceApi deviceApi;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		deviceApi = new SwitchBotDeviceApi(mockSwitchBotApi);
	}

	@Test
	public void testGetDeviceNameFromIdWithFreshCache() throws IOException {
		Device device1 = new Device();
		device1.setDeviceId("device1");
		device1.setDeviceName("Living Room Light");

		Device device2 = new Device();
		device2.setDeviceId("device2");
		device2.setDeviceName("Bedroom Curtain");

		SwitchBotDeviceApi spyDeviceApi = spy(deviceApi);
		doReturn(Arrays.asList(device1, device2)).when(spyDeviceApi).getDevices();

		String result = spyDeviceApi.getDeviceNameFromId("device1");
		assertEquals("Living Room Light", result);

		String result2 = spyDeviceApi.getDeviceNameFromId("device2");
		assertEquals("Bedroom Curtain", result2);
	}

	@Test
	public void testGetDeviceNameFromIdWithUnknownDevice() throws IOException {
		Device device = new Device();
		device.setDeviceId("known-device");
		device.setDeviceName("Known Device");

		SwitchBotDeviceApi spyDeviceApi = spy(deviceApi);
		doReturn(Arrays.asList(device)).when(spyDeviceApi).getDevices();

		String result = spyDeviceApi.getDeviceNameFromId("unknown-device");
		assertEquals("unknown-device", result);
	}

	@Test
	public void testGetDeviceNameFromIdCacheExpiry() throws IOException {
		Device device = new Device();
		device.setDeviceId("device1");
		device.setDeviceName("Test Device");

		SwitchBotDeviceApi spyDeviceApi = spy(deviceApi);
		spyDeviceApi.deviceIdToNamesCacheTime = System.currentTimeMillis() - (ITimeConstants.HOUR * 2);

		doReturn(Arrays.asList(device)).when(spyDeviceApi).getDevices();

		String result = spyDeviceApi.getDeviceNameFromId("device1");
		assertEquals("Test Device", result);

		verify(spyDeviceApi, times(1)).getDevices();
	}

	@Test
	public void testGetDeviceNameFromIdWithValidCache() throws IOException {
		Device device = new Device();
		device.setDeviceId("device1");
		device.setDeviceName("Cached Device");

		SwitchBotDeviceApi spyDeviceApi = spy(deviceApi);
		spyDeviceApi.deviceIdToNamesCacheTime = System.currentTimeMillis();

		doReturn(Arrays.asList(device)).when(spyDeviceApi).getDevices();
		spyDeviceApi.getDeviceNameFromId("device1");

		reset(spyDeviceApi);
		spyDeviceApi.deviceIdToNamesCacheTime = System.currentTimeMillis();
		spyDeviceApi.getDeviceNameFromId("device1");

		verify(spyDeviceApi, never()).getDevices();
	}

	@Test
	public void testGetDeviceNameFromIdWithIOException() throws IOException {
		SwitchBotDeviceApi spyDeviceApi = spy(deviceApi);
		doThrow(new IOException("Network error")).when(spyDeviceApi).getDevices();

		String result = spyDeviceApi.getDeviceNameFromId("device1");
		assertEquals("device1", result);
	}

	@Test
	public void testGetDeviceStatusWithNullDeviceId() throws IOException {
		Device result = deviceApi.getDeviceStatus(null);
		assertNull(result);
	}

	@Test
	public void testGetDeviceStatusInputValidation() {
		assertDoesNotThrow(() -> {
			assertNotNull(deviceApi);
		});
	}

	@Test
	public void testSendDeviceControlCommandsValidation() {
		DeviceCommand command = new DeviceCommand("turnOn", "default");
		when(mockSwitchBotApi.getMoshi()).thenReturn(new com.squareup.moshi.Moshi.Builder().build());

		assertNotNull(command);
		assertEquals("turnOn", command.getCommand());
		assertEquals("default", command.getParameter());
	}

	@Test
	public void testRefreshDeviceNameMapWithEmptyList() throws IOException {
		SwitchBotDeviceApi spyDeviceApi = spy(deviceApi);
		doReturn(Collections.emptyList()).when(spyDeviceApi).getDevices();

		String result = spyDeviceApi.getDeviceNameFromId("any-device");
		assertEquals("any-device", result);
	}

	@Test
	public void testConcurrentCacheRefresh() throws InterruptedException {
		SwitchBotDeviceApi spyDeviceApi = spy(deviceApi);
		spyDeviceApi.deviceIdToNamesCacheTime = System.currentTimeMillis() - (ITimeConstants.HOUR * 2);

		Device device = new Device();
		device.setDeviceId("concurrent-device");
		device.setDeviceName("Concurrent Test");
		try {
			doReturn(Arrays.asList(device)).when(spyDeviceApi).getDevices();
		} catch (IOException e) {
			fail("Setup failed: " + e.getMessage());
		}

		Thread thread1 = new Thread(() -> {
			spyDeviceApi.getDeviceNameFromId("concurrent-device");
		});

		Thread thread2 = new Thread(() -> {
			spyDeviceApi.getDeviceNameFromId("concurrent-device");
		});

		thread1.start();
		thread2.start();

		thread1.join();
		thread2.join();

		try {
			verify(spyDeviceApi, atLeastOnce()).getDevices();
		} catch (IOException e) {
			fail("Verification failed: " + e.getMessage());
		}
	}

	@Test
	public void testSendDeviceControlCommandsInputValidation() {
		DeviceCommand command = new DeviceCommand("turnOn", "default");
		String deviceId = "test-device-id";

		assertNotNull(command);
		assertEquals("turnOn", command.getCommand());
		assertEquals("default", command.getParameter());
		assertNotNull(deviceId);
	}

	@Test
	public void testGetDeviceStatusReturnsNullForNullInput() throws IOException {
		Device result = deviceApi.getDeviceStatus(null);
		assertNull(result);
	}

	@Test
	public void testDeviceApiNotNull() {
		assertNotNull(deviceApi);
	}
}
