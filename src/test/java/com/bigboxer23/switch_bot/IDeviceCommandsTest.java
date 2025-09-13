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
		assertNotNull(IDeviceCommands.ROLLER_SHADE_CLOSE);
		assertNotNull(IDeviceCommands.ROLLER_SHADE_OPEN);
	}

	@Test
	public void testRollerShadeCloseCommand() {
		DeviceCommand rollerShadeClose = IDeviceCommands.ROLLER_SHADE_CLOSE;
		assertNotNull(rollerShadeClose);
		assertEquals("setPosition", rollerShadeClose.getCommand());
		assertEquals(100, rollerShadeClose.getParameter());
	}

	@Test
	public void testRollerShadeOpenCommand() {
		DeviceCommand rollerShadeOpen = IDeviceCommands.ROLLER_SHADE_OPEN;
		assertNotNull(rollerShadeOpen);
		assertEquals("setPosition", rollerShadeOpen.getCommand());
		assertEquals(0, rollerShadeOpen.getParameter());
	}

	@Test
	public void testRollerShadePositionWithIntegerParameter() {
		DeviceCommand command = IDeviceCommands.rollerShadePosition(50);

		assertNotNull(command);
		assertEquals("setPosition", command.getCommand());
		assertEquals(50, command.getParameter());
		assertEquals("command", command.getCommandType());
	}

	@Test
	public void testRollerShadePositionWithZeroParameter() {
		DeviceCommand command = IDeviceCommands.rollerShadePosition(0);

		assertNotNull(command);
		assertEquals("setPosition", command.getCommand());
		assertEquals(0, command.getParameter());
		assertEquals("command", command.getCommandType());
	}

	@Test
	public void testRollerShadePositionWithMaximumParameter() {
		DeviceCommand command = IDeviceCommands.rollerShadePosition(100);

		assertNotNull(command);
		assertEquals("setPosition", command.getCommand());
		assertEquals(100, command.getParameter());
		assertEquals("command", command.getCommandType());
	}

	@Test
	public void testRollerShadePositionWithNegativeParameter() {
		DeviceCommand command = IDeviceCommands.rollerShadePosition(-10);

		assertNotNull(command);
		assertEquals("setPosition", command.getCommand());
		assertEquals(-10, command.getParameter());
		assertEquals("command", command.getCommandType());
	}

	@Test
	public void testRollerShadePositionCreatesNewInstances() {
		DeviceCommand command1 = IDeviceCommands.rollerShadePosition(25);
		DeviceCommand command2 = IDeviceCommands.rollerShadePosition(25);
		DeviceCommand command3 = IDeviceCommands.rollerShadePosition(75);

		assertNotSame(command1, command2);
		assertEquals(command1.getCommand(), command2.getCommand());
		assertEquals(command1.getParameter(), command2.getParameter());
		assertEquals(command1.getCommandType(), command2.getCommandType());

		assertNotEquals(command1.getParameter(), command3.getParameter());
	}

	@Test
	public void testSetPositionConstant() {
		assertEquals("setPosition", IDeviceCommands.SET_POSITION);
	}
}
