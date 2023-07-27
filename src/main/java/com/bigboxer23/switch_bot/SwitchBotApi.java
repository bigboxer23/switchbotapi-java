package com.bigboxer23.switch_bot;

import com.bigboxer23.utils.http.OkHttpUtil;
import com.bigboxer23.utils.http.RequestBuilderCallback;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import okhttp3.Response;

/** */
public class SwitchBotApi {
	public static final String baseUrl = "https://api.switch-bot.com/";

	private static SwitchBotApi instance;

	private String token;
	private String secret;

	private SwitchBotApi(String token, String secret) {
		this.secret = secret;
		this.token = token;
	}

	public static SwitchBotApi getInstance(String token, String secret) {
		return Optional.ofNullable(instance).orElseGet(() -> new SwitchBotApi(token, secret));
	}

	public void getDevices() throws IOException {
		try (Response response = OkHttpUtil.getSynchronous(baseUrl + "v1.1/devices", addAuth())) {
			System.out.println(response.body().string());
		}
	}

	private RequestBuilderCallback addAuth() {
		return builder -> {
			String nonce = UUID.randomUUID().toString();
			String time = "" + Instant.now().toEpochMilli();
			String data = token + time + nonce;
			try {
				SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256");
				Mac mac = Mac.getInstance("HmacSHA256");
				mac.init(secretKeySpec);
				String signature = new String(Base64.getEncoder().encode(mac.doFinal(data.getBytes("UTF-8"))));
				builder.addHeader("Authorization", token)
						.addHeader("sign", signature)
						.addHeader("nonce", nonce)
						.addHeader("t", time);
			} catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException theE) {
				theE.printStackTrace();
			}
			return builder;
		};
	}
}
