package com.bigboxer23.switch_bot;

import com.bigboxer23.switch_bot.data.ApiResponse;
import com.bigboxer23.switch_bot.data.ApiResponseBody;
import com.bigboxer23.switch_bot.data.Device;
import com.bigboxer23.switch_bot.data.DeviceApiResponse;
import com.bigboxer23.utils.http.OkHttpUtil;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
			if (!response.isSuccessful()) {
				throw new IOException(response.message());
			}
			ApiResponse apiResponse = provider.getMoshi()
					.adapter(ApiResponse.class)
					.fromJson(response.body().string());
			return Optional.ofNullable(apiResponse)
					.map(ApiResponse::getBody)
					.map(ApiResponseBody::getDeviceList)
					.orElse(Collections.emptyList());
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
			if (!response.isSuccessful()) {
				throw new IOException(response.message());
			}
			DeviceApiResponse apiResponse = provider.getMoshi()
					.adapter(DeviceApiResponse.class)
					.fromJson(response.body().string());
			if (Optional.ofNullable(apiResponse)
							.map(DeviceApiResponse::getStatusCode)
							.orElse(-1)
					!= 100) {
				logger.error("error code " + apiResponse.getStatusCode());
				return null;
			}
			return Optional.of(apiResponse).map(DeviceApiResponse::getBody).orElse(null);
		}
	}

	/**
	 * @see <a
	 *     href="https://github.com/OpenWonderLabs/SwitchBotAPI#send-device-control-commands">send
	 *     device control commands</a>
	 */
	public void sendDeviceControlCommands() {
		// TODO:
	}
}
