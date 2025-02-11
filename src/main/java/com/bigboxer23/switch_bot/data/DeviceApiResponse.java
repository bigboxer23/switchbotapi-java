package com.bigboxer23.switch_bot.data;

import lombok.Data;

/** */
@Data
public class DeviceApiResponse implements IApiResponse {
	private int statusCode;

	private Device body;

	private String message;

	private boolean success = true;
}
