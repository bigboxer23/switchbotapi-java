package com.bigboxer23.switch_bot.data;

import lombok.Data;

/** */
@Data
public class BadApiResponse implements IApiResponse {
	private int statusCode;

	private String message;

	private boolean success = false;

	public BadApiResponse(int statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
	}
}
