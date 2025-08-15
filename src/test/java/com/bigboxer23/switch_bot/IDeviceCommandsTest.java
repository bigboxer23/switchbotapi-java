package com.bigboxer23.switch_bot;

import static org.junit.jupiter.api.Assertions.*;

import com.bigboxer23.switch_bot.data.DeviceCommand;
import org.junit.jupiter.api.Test;

public class IDeviceCommandsTest {

	@Test
	public void testCommandConstants() {
		assertEquals("turnOff", IDeviceCommands.TURN_OFF);
		assertEquals("turnOn", IDeviceCommands.TURN_ON);
	}

	@Test
	public void testCurtainCloseCommand() {
		DeviceCommand curtainClose = IDeviceCommands.CURTAIN_CLOSE;
		assertNotNull(curtainClose);
		assertEquals("turnOff", curtainClose.getCommand());
		assertEquals("default", curtainClose.getParameter());
	}

	@Test
	public void testCurtainOpenCommand() {
		DeviceCommand curtainOpen = IDeviceCommands.CURTAIN_OPEN;
		assertNotNull(curtainOpen);
		assertEquals("turnOn", curtainOpen.getCommand());
		assertEquals("default", curtainOpen.getParameter());
	}

	@Test
	public void testPlugMiniOffCommand() {
		DeviceCommand plugMiniOff = IDeviceCommands.PLUG_MINI_OFF;
		assertNotNull(plugMiniOff);
		assertEquals("turnOff", plugMiniOff.getCommand());
		assertEquals("default", plugMiniOff.getParameter());
	}

	@Test
	public void testPlugMiniOnCommand() {
		DeviceCommand plugMiniOn = IDeviceCommands.PLUG_MINI_ON;
		assertNotNull(plugMiniOn);
		assertEquals("turnOn", plugMiniOn.getCommand());
		assertEquals("default", plugMiniOn.getParameter());
	}

	@Test
	public void testCommandConsistency() {
		assertEquals(IDeviceCommands.CURTAIN_CLOSE.getCommand(), IDeviceCommands.PLUG_MINI_OFF.getCommand());
		assertEquals(IDeviceCommands.CURTAIN_OPEN.getCommand(), IDeviceCommands.PLUG_MINI_ON.getCommand());

		assertEquals(IDeviceCommands.CURTAIN_CLOSE.getParameter(), IDeviceCommands.CURTAIN_OPEN.getParameter());
		assertEquals(IDeviceCommands.PLUG_MINI_OFF.getParameter(), IDeviceCommands.PLUG_MINI_ON.getParameter());
	}

	@Test
	public void testCommandObjectsAreNotNull() {
		assertNotNull(IDeviceCommands.CURTAIN_CLOSE);
		assertNotNull(IDeviceCommands.CURTAIN_OPEN);
		assertNotNull(IDeviceCommands.PLUG_MINI_OFF);
		assertNotNull(IDeviceCommands.PLUG_MINI_ON);
	}
}
