package com.bigboxer23.switch_bot;

import lombok.Data;

/** */
@Data
public class ApiResponse {
	private int statusCode;

	private ApiResponseBody body;

	private String message;
}
