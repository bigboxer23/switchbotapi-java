package com.bigboxer23.switch_bot;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Need to define environment variables for SwitchBotToken/SwitchBotSecret to run tests
 */
public class SwitchBotApiTest
{
	private String token = System.getenv("SwitchBotToken");

	private String secret = System.getenv("SwitchBotSecret");

	@Test
	public void getDevices() throws IOException
	{
		SwitchBotApi instance = SwitchBotApi.getInstance(
				token,
				secret);
		List<Device> devices = instance.getDevices();
		assertFalse(devices.isEmpty());
		assertNotNull(devices.get(0).getDeviceId());
	}
}
