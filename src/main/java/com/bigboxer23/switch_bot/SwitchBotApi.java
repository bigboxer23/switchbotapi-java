package com.bigboxer23.switch_bot;

import java.util.Optional;

/** */
public class SwitchBotApi {
	private static SwitchBotApi instance;

	private SwitchBotApi() {}

	public SwitchBotApi getInstance() {
		return Optional.ofNullable(instance).orElseGet(SwitchBotApi::new);
	}

	public void getDevices() {}
}
