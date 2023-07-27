package com.bigboxer23.switch_bot.data;

import lombok.Data;

/** */
@Data
public class DeviceApiResponse {
	private int statusCode;

	private Device body;

	private String message;
}
