package com.bigboxer23.switch_bot.data;

import lombok.Data;

/** */
@Data
public class ApiResponse implements IApiResponse {
	private int statusCode;

	private ApiResponseBody body;

	private String message;

	private boolean success = true;
}
