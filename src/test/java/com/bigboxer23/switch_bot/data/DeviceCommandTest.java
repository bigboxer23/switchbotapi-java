package com.bigboxer23.switch_bot.data;

import static org.junit.jupiter.api.Assertions.*;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DeviceCommandTest {

	private Moshi moshi;
	private JsonAdapter<DeviceCommand> adapter;

	@BeforeEach
	void setUp() {
		moshi = new Moshi.Builder().build();
		adapter = moshi.adapter(DeviceCommand.class);
	}

	@Test
	public void testDeviceCommandConstructor() {
		DeviceCommand command = new DeviceCommand("turnOn", "default");

		assertEquals("turnOn", command.getCommand());
		assertEquals("default", command.getParameter());
		assertEquals("command", command.getCommandType());
	}

	@Test
	public void testDeviceCommandConstructorWithNullValues() {
		DeviceCommand command = new DeviceCommand(null, null);

		assertNull(command.getCommand());
		assertNull(command.getParameter());
		assertEquals("command", command.getCommandType());
	}

	@Test
	public void testDeviceCommandConstructorWithEmptyValues() {
		DeviceCommand command = new DeviceCommand("", "");

		assertEquals("", command.getCommand());
		assertEquals("", command.getParameter());
		assertEquals("command", command.getCommandType());
	}

	@Test
	public void testDeviceCommandJsonSerialization() throws IOException {
		DeviceCommand command = new DeviceCommand("turnOff", "immediate");

		String json = adapter.toJson(command);

		assertNotNull(json);
		assertTrue(json.contains("\"command\":\"turnOff\""));
		assertTrue(json.contains("\"parameter\":\"immediate\""));
		assertTrue(json.contains("\"commandType\":\"command\""));
	}

	@Test
	public void testDeviceCommandJsonSerializationWithSpecialCharacters() throws IOException {
		DeviceCommand command = new DeviceCommand("setPosition", "50%");

		String json = adapter.toJson(command);

		assertNotNull(json);
		assertTrue(json.contains("\"command\":\"setPosition\""));
		assertTrue(json.contains("\"parameter\":\"50%\""));
	}

	@Test
	public void testDeviceCommandJsonDeserialization() throws IOException {
		String json =
				"{" + "\"command\":\"setBrightness\"," + "\"parameter\":\"75\"," + "\"commandType\":\"command\"" + "}";

		DeviceCommand command = adapter.fromJson(json);

		assertNotNull(command);
		assertEquals("setBrightness", command.getCommand());
		assertEquals("75", command.getParameter());
		assertEquals("command", command.getCommandType());
	}

	@Test
	public void testDeviceCommandJsonDeserializationWithMissingFields() throws IOException {
		String json = "{\"command\":\"toggle\"}";

		DeviceCommand command = adapter.fromJson(json);

		assertNotNull(command);
		assertEquals("toggle", command.getCommand());
		assertNull(command.getParameter());
		assertNull(command.getCommandType());
	}

	@Test
	public void testDeviceCommandJsonDeserializationWithNullValues() throws IOException {
		String json = "{" + "\"command\":null," + "\"parameter\":null," + "\"commandType\":null" + "}";

		DeviceCommand command = adapter.fromJson(json);

		assertNotNull(command);
		assertNull(command.getCommand());
		assertNull(command.getParameter());
		assertNull(command.getCommandType());
	}

	@Test
	public void testDeviceCommandSettersAndGetters() {
		DeviceCommand command = new DeviceCommand("initial", "param");

		command.setCommand("updated");
		command.setParameter("newParam");
		command.setCommandType("newType");

		assertEquals("updated", command.getCommand());
		assertEquals("newParam", command.getParameter());
		assertEquals("newType", command.getCommandType());
	}

	@Test
	public void testDeviceCommandEqualsAndHashCode() {
		DeviceCommand command1 = new DeviceCommand("turnOn", "default");
		DeviceCommand command2 = new DeviceCommand("turnOn", "default");
		DeviceCommand command3 = new DeviceCommand("turnOff", "default");

		assertEquals(command1, command2);
		assertEquals(command1.hashCode(), command2.hashCode());
		assertNotEquals(command1, command3);
		assertNotEquals(command1.hashCode(), command3.hashCode());
	}

	@Test
	public void testDeviceCommandToString() {
		DeviceCommand command = new DeviceCommand("setTimer", "30min");

		String toString = command.toString();

		assertNotNull(toString);
		assertTrue(toString.contains("setTimer"));
		assertTrue(toString.contains("30min"));
		assertTrue(toString.contains("command"));
	}

	@Test
	public void testDeviceCommandWithComplexParameters() throws IOException {
		DeviceCommand command = new DeviceCommand("setSchedule", "{\"time\":\"08:00\",\"days\":[\"mon\",\"tue\"]}");

		String json = adapter.toJson(command);
		DeviceCommand deserializedCommand = adapter.fromJson(json);

		assertEquals(command.getCommand(), deserializedCommand.getCommand());
		assertEquals(command.getParameter(), deserializedCommand.getParameter());
		assertEquals(command.getCommandType(), deserializedCommand.getCommandType());
	}

	@Test
	public void testDeviceCommandRoundTripSerialization() throws IOException {
		DeviceCommand originalCommand = new DeviceCommand("testCommand", "testParameter");

		String json = adapter.toJson(originalCommand);
		DeviceCommand deserializedCommand = adapter.fromJson(json);

		assertEquals(originalCommand.getCommand(), deserializedCommand.getCommand());
		assertEquals(originalCommand.getParameter(), deserializedCommand.getParameter());
		assertEquals(originalCommand.getCommandType(), deserializedCommand.getCommandType());
	}

	@Test
	public void testDeviceCommandConstructorWithIntegerParameter() {
		DeviceCommand command = new DeviceCommand("setPosition", 75);

		assertEquals("setPosition", command.getCommand());
		assertEquals(75, command.getParameter());
		assertEquals("command", command.getCommandType());
	}

	@Test
	public void testDeviceCommandConstructorWithZeroIntegerParameter() {
		DeviceCommand command = new DeviceCommand("setPosition", 0);

		assertEquals("setPosition", command.getCommand());
		assertEquals(0, command.getParameter());
		assertEquals("command", command.getCommandType());
	}

	@Test
	public void testDeviceCommandConstructorWithNegativeIntegerParameter() {
		DeviceCommand command = new DeviceCommand("setPosition", -10);

		assertEquals("setPosition", command.getCommand());
		assertEquals(-10, command.getParameter());
		assertEquals("command", command.getCommandType());
	}

	@Test
	public void testDeviceCommandJsonSerializationWithIntegerParameter() throws IOException {
		DeviceCommand command = new DeviceCommand("setPosition", 50);

		String json = adapter.toJson(command);

		assertNotNull(json);
		assertTrue(json.contains("\"command\":\"setPosition\""));
		assertTrue(json.contains("\"parameter\":50"));
		assertFalse(json.contains("\"parameter\":\"50\""));
		assertTrue(json.contains("\"commandType\":\"command\""));
	}

	@Test
	public void testDeviceCommandJsonSerializationIntegerVsString() throws IOException {
		DeviceCommand intCommand = new DeviceCommand("setPosition", 75);
		DeviceCommand stringCommand = new DeviceCommand("setPosition", "75");

		String intJson = adapter.toJson(intCommand);
		String stringJson = adapter.toJson(stringCommand);

		assertTrue(intJson.contains("\"parameter\":75"));
		assertTrue(stringJson.contains("\"parameter\":\"75\""));
		assertNotEquals(intJson, stringJson);
	}

	@Test
	public void testDeviceCommandEqualsWithIntegerParameter() {
		DeviceCommand command1 = new DeviceCommand("setPosition", 100);
		DeviceCommand command2 = new DeviceCommand("setPosition", 100);
		DeviceCommand command3 = new DeviceCommand("setPosition", 50);

		assertEquals(command1, command2);
		assertEquals(command1.hashCode(), command2.hashCode());
		assertNotEquals(command1, command3);
		assertNotEquals(command1.hashCode(), command3.hashCode());
	}

	@Test
	public void testDeviceCommandIntegerAndStringParametersNotEqual() {
		DeviceCommand intCommand = new DeviceCommand("setPosition", 100);
		DeviceCommand stringCommand = new DeviceCommand("setPosition", "100");

		assertNotEquals(intCommand, stringCommand);
		assertNotEquals(intCommand.hashCode(), stringCommand.hashCode());
	}
}
