package com.bigboxer23.switch_bot;

import com.bigboxer23.switch_bot.data.*;
import com.bigboxer23.utils.http.OkHttpUtil;
import com.bigboxer23.utils.time.ITimeConstants;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import okhttp3.RequestBody;
import okhttp3.Response;

/** */
@Slf4j
public class SwitchBotDeviceApi {
	private final SwitchBotApi provider;

	private Map<String, String> deviceIdToNames;

	private long deviceIdToNamesCacheTime = -1;

	protected SwitchBotDeviceApi(SwitchBotApi provider) {
		this.provider = provider;
		refreshDeviceNameMap();
	}

	public String getDeviceNameFromId(String deviceId) {
		refreshDeviceNameMap();
		return Optional.ofNullable(deviceIdToNames)
				.map(m -> m.getOrDefault(deviceId, deviceId))
				.orElse(deviceId);
	}

	private synchronized void refreshDeviceNameMap() {
		if (deviceIdToNames != null && (System.currentTimeMillis() - ITimeConstants.HOUR) < deviceIdToNamesCacheTime) {
			return;
		}
		try {
			log.info("Refreshing device id/name map...");
			deviceIdToNames = Collections.unmodifiableMap(
					getDevices().stream().collect(Collectors.toMap(Device::getDeviceId, Device::getDeviceName)));
			deviceIdToNamesCacheTime = System.currentTimeMillis();
		} catch (IOException e) {
			log.error("Failed to refresh device names.", e);
			deviceIdToNames = null;
			deviceIdToNamesCacheTime = -1;
		}
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
			log.error("Need valid device id");
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
	public IApiResponse sendDeviceControlCommands(String deviceId, DeviceCommand command) throws IOException {
		String stringCommand = provider.getMoshi().adapter(DeviceCommand.class).toJson(command);
		try (Response response = OkHttpUtil.postSynchronous(
				SwitchBotApi.baseUrl + "v1.1/devices/" + deviceId + "/commands",
				RequestBody.create(URLDecoder.decode(stringCommand, StandardCharsets.UTF_8.displayName())
						.getBytes(StandardCharsets.UTF_8)),
				provider.addAuth())) {
			return parseResponse(response, DeviceApiResponse.class);
		}
	}

	/**
	 * API is a bit weird, and need to check in returned body for status code as well
	 *
	 * @param response
	 * @param clazz
	 * @return
	 * @param <T>
	 * @throws IOException
	 */
	private <T extends IApiResponse> T parseResponse(Response response, Class<T> clazz) throws IOException {
		IApiResponse apiResponse =
				provider.checkForError(response, (Optional<IApiResponse>) OkHttpUtil.getBody(response, clazz));
		if (!apiResponse.isSuccess()) {
			throw new IOException(apiResponse.getStatusCode() + " " + apiResponse.getMessage());
		}
		return (T) apiResponse;
	}
}
