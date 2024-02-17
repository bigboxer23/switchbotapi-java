package com.bigboxer23.switch_bot;

import com.bigboxer23.switch_bot.data.*;
import com.bigboxer23.utils.http.OkHttpUtil;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** */
public class SwitchBotDeviceApi {
	private static final Logger logger = LoggerFactory.getLogger(SwitchBotDeviceApi.class);
	private final SwitchBotApi provider;

	protected SwitchBotDeviceApi(SwitchBotApi provider) {
		this.provider = provider;
	}

	/**
	 * @see <a href="https://github.com/OpenWonderLabs/SwitchBotAPI#get-device-list">Get device
	 *     list</a>
	 * @return list of devices associated with the switchBot acct
	 * @throws IOException error class thrown for various reasons
	 */
	public List<Device> getDevices() throws IOException {
		try (Response response = OkHttpUtil.getSynchronous(SwitchBotApi.baseUrl + "v1.1/devices", provider.addAuth())) {
			return parseResponse(response, ApiResponse.class).getBody().getDeviceList();
		}
	}

	/**
	 * @see <a href="https://github.com/OpenWonderLabs/SwitchBotAPI#get-device-status">Get device
	 *     status</a>
	 * @param deviceId id of the device to query
	 */
	public Device getDeviceStatus(String deviceId) throws IOException {
		if (deviceId == null) {
			logger.error("Need valid device id");
			return null;
		}
		try (Response response = OkHttpUtil.getSynchronous(
				SwitchBotApi.baseUrl + "v1.1/devices/" + deviceId + "/status", provider.addAuth())) {
			return parseResponse(response, DeviceApiResponse.class).getBody();
		}
	}

	/**
	 * @see <a
	 *     href="https://github.com/OpenWonderLabs/SwitchBotAPI#send-device-control-commands">send
	 *     device control commands</a>
	 */
	public void sendDeviceControlCommands(String deviceId, DeviceCommand command) throws IOException {

		String stringCommand = provider.getMoshi().adapter(DeviceCommand.class).toJson(command);
		try (Response response = OkHttpUtil.postSynchronous(
				SwitchBotApi.baseUrl + "v1.1/devices/" + deviceId + "/commands",
				RequestBody.create(URLDecoder.decode(stringCommand, StandardCharsets.UTF_8.displayName())
						.getBytes(StandardCharsets.UTF_8)),
				provider.addAuth())) {
			parseResponse(response, DeviceApiResponse.class);
		}
	}

	private <T extends IApiResponse> T parseResponse(Response response, Class<T> clazz) throws IOException {
		if (!response.isSuccessful()) {
			throw new IOException(response.code() + " " + response.message() + " "
					+ response.body().string());
		}
		String body = response.body().string();
		T apiResponse = provider.getMoshi().adapter(clazz).fromJson(body);
		if (!provider.checkForError(apiResponse)) {
			throw new IOException(response.code() + " " + response.message() + " " + body);
		}
		return apiResponse;
	}
}
