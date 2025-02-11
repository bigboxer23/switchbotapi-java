package com.bigboxer23.switch_bot.data;

/** */
public interface IApiResponse {
	int getStatusCode();

	void setStatusCode(int statusCode);

	String getMessage();

	void setMessage(String message);

	boolean isSuccess();

	void setSuccess(boolean success);
}
