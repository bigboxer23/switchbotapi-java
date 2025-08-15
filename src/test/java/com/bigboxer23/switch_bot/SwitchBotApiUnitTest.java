package com.bigboxer23.switch_bot;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.bigboxer23.switch_bot.data.*;
import com.bigboxer23.utils.time.ITimeConstants;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SwitchBotApiUnitTest {

	@BeforeEach
	void resetSingleton() throws Exception {
		java.lang.reflect.Field instanceField = SwitchBotApi.class.getDeclaredField("instance");
		instanceField.setAccessible(true);
		instanceField.set(null, null);
	}

	@Test
	public void testSingletonBehavior() {
		SwitchBotApi mockApi1 = SwitchBotApi.getInstance("token", "secret");
		SwitchBotApi mockApi2 = SwitchBotApi.getInstance("token", "secret");
		assertSame(mockApi1, mockApi2, "Should return the same singleton instance");
	}

	@Test
	public void testGetInstanceWithNullToken() {
		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			SwitchBotApi.getInstance(null, "secret");
		});
		assertEquals("need to define token and secret values.", exception.getMessage());
	}

	@Test
	public void testGetInstanceWithNullSecret() {
		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			SwitchBotApi.getInstance("token", null);
		});
		assertEquals("need to define token and secret values.", exception.getMessage());
	}

	@Test
	public void testAddAuthReturnsCallback() {
		assertNotNull(SwitchBotApi.getInstance("testToken", "testSecret"));
	}

	@Test
	public void testCheckForErrorWithSuccessfulResponse() {
		SwitchBotApi api = SwitchBotApi.getInstance("token", "secret");
		Response mockResponse = mock(Response.class);
		ApiResponse successResponse = new ApiResponse();
		successResponse.setStatusCode(100);
		successResponse.setMessage("success");

		IApiResponse result = api.checkForError(mockResponse, Optional.of(successResponse));

		assertTrue(result.isSuccess());
		assertEquals(100, result.getStatusCode());
	}

	@Test
	public void testCheckForErrorWithFailedResponse() {
		SwitchBotApi api = SwitchBotApi.getInstance("token", "secret");
		Response mockResponse = mock(Response.class);
		ApiResponse failedResponse = new ApiResponse();
		failedResponse.setStatusCode(190);
		failedResponse.setMessage("Device not found");

		IApiResponse result = api.checkForError(mockResponse, Optional.of(failedResponse));

		assertFalse(result.isSuccess());
		assertEquals(190, result.getStatusCode());
		assertEquals("Device not found", result.getMessage());
	}

	@Test
	public void testCheckForErrorWithEmptyResponse() {
		SwitchBotApi api = SwitchBotApi.getInstance("token", "secret");
		Response mockResponse = mock(Response.class);
		when(mockResponse.code()).thenReturn(404);
		when(mockResponse.message()).thenReturn("Not Found");

		IApiResponse result = api.checkForError(mockResponse, Optional.empty());

		assertFalse(result.isSuccess());
		assertEquals(404, result.getStatusCode());
		assertEquals("Not Found", result.getMessage());
		assertInstanceOf(BadApiResponse.class, result);
	}

	@Test
	public void testDeviceCommandSerialization() throws IOException {
		SwitchBotApi api = SwitchBotApi.getInstance("token", "secret");

		DeviceCommand originalCommand = new DeviceCommand("turnOn", "default");

		String json = api.getMoshi().adapter(DeviceCommand.class).toJson(originalCommand);
		assertNotNull(json);
		assertTrue(json.contains("turnOn"));

		DeviceCommand deserializedCommand =
				api.getMoshi().adapter(DeviceCommand.class).fromJson(json);
		assertNotNull(deserializedCommand);
		assertEquals(originalCommand.getCommand(), deserializedCommand.getCommand());
		assertEquals(originalCommand.getParameter(), deserializedCommand.getParameter());
		assertEquals("command", deserializedCommand.getCommandType());
	}

	@Test
	public void testDeviceNameCacheRefreshBehavior() throws IOException {
		SwitchBotApi mockApi = mock(SwitchBotApi.class);
		SwitchBotDeviceApi deviceApi = spy(new SwitchBotDeviceApi(mockApi));

		deviceApi.deviceIdToNamesCacheTime = System.currentTimeMillis() - (ITimeConstants.HOUR * 2);

		Device dummyDevice = new Device();
		dummyDevice.setDeviceId("12345");
		dummyDevice.setDeviceName("Test Device");
		doReturn(List.of(dummyDevice)).when(deviceApi).getDevices();

		String deviceName = deviceApi.getDeviceNameFromId("12345");
		assertEquals("Test Device", deviceName);

		reset(deviceApi);
		deviceApi.getDeviceNameFromId("12345");
		verify(deviceApi, times(0)).getDevices();
	}
}
