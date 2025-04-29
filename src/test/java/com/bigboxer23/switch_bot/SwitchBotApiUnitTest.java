package com.bigboxer23.switch_bot;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.bigboxer23.switch_bot.data.Device;
import com.bigboxer23.switch_bot.data.DeviceCommand;
import com.bigboxer23.utils.time.ITimeConstants;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

public class SwitchBotApiUnitTest {
	@Test
	public void testSingletonBehavior() {
		SwitchBotApi mockApi1 = SwitchBotApi.getInstance("token", "secret");
		SwitchBotApi mockApi2 = SwitchBotApi.getInstance("token", "secret");
		assertSame(mockApi1, mockApi2, "Should return the same singleton instance");
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
