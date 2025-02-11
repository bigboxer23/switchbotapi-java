package com.bigboxer23.switch_bot;

import com.bigboxer23.switch_bot.data.BadApiResponse;
import com.bigboxer23.switch_bot.data.IApiResponse;
import com.bigboxer23.utils.http.RequestBuilderCallback;
import com.squareup.moshi.Moshi;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

/** */
@Slf4j
@Getter
public class SwitchBotApi {
	protected static final String baseUrl = "https://api.switch-bot.com/";

	private static SwitchBotApi instance;

	private final SwitchBotDeviceApi deviceApi;

	private final String token;
	private final String secret;

	private final Moshi moshi = new Moshi.Builder().build();

	private SwitchBotApi(String token, String secret) {
		this.secret = secret;
		this.token = token;
		deviceApi = new SwitchBotDeviceApi(this);
	}

	public static SwitchBotApi getInstance(String token, String secret) {
		if (token == null || secret == null) {
			log.error("need to define token and secret values.");
			throw new RuntimeException("need to define token and secret values.");
		}
		return Optional.ofNullable(instance).orElseGet(() -> {
			instance = new SwitchBotApi(token, secret);
			return instance;
		});
	}

	protected Moshi getMoshi() {
		return moshi;
	}

	protected RequestBuilderCallback addAuth() {
		return builder -> {
			String nonce = UUID.randomUUID().toString();
			String time = "" + Instant.now().toEpochMilli();
			String data = token + time + nonce;
			try {
				SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
				Mac mac = Mac.getInstance("HmacSHA256");
				mac.init(secretKeySpec);
				String signature =
						new String(Base64.getEncoder().encode(mac.doFinal(data.getBytes(StandardCharsets.UTF_8))));
				builder.addHeader("Authorization", token)
						.addHeader("sign", signature)
						.addHeader("nonce", nonce)
						.addHeader("t", time);
			} catch (NoSuchAlgorithmException | InvalidKeyException e) {
				log.warn("exception with auth: ", e);
			}
			return builder;
		};
	}

	/**
	 * add standardized logging and error checking on request
	 *
	 * @param apiResponse the API response
	 * @return true if error occurs
	 */
	protected IApiResponse checkForError(Response response, Optional<IApiResponse> apiResponse) {
		return apiResponse
				.map(api -> {
					if (api.getStatusCode() != 100) {
						log.error("error code: " + api.getStatusCode() + " : " + api.getMessage());
						api.setSuccess(false);
						return api;
					}
					return api;
				})
				.orElseGet(() -> {
					log.error("Error calling switchbot api: " + response.code() + " " + response.message());
					return new BadApiResponse(response.code(), response.message());
				});
	}
}
