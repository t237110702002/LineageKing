package com.tinatest.line_bot.service;

import com.google.gson.Gson;
import com.tinatest.line_bot.service.lineNotify.LineNotifyCallbackResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LineNotifyService {

	private static final String strEndpoint = "https://notify-api.line.me/api/notify";
	private static final String getTokenEndpoint = "https://notify-bot.line.me/oauth/token";
	private static final String authorizeEndpoint = "https://notify-bot.line.me/oauth/authorize";
	public static final String wrap = "%0D%0A";

	@Autowired
	private UserService userService;

	@Value("${line.notify.clientId}")
	private String clientId;

	@Value("${line.notify.clientSecret}")
	private String clientSecret;

	@Value("${line.notify.redirectUri}")
	private String redirectUri;


	public void sendMessages(List<String> userTokens, String message, boolean encoded) {
		userTokens.forEach(token-> sendMessage(token, message, encoded));
	}

	public boolean sendMessage(String token, String message, boolean encoded) {
		message = replaceProcess(message);
		if (!encoded) {
			try {
				message = URLEncoder.encode(message, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return callEvent(token, message);
	}

	private boolean callEvent(String token, String message) {
		boolean result = false;
		try {

			String strUrl = strEndpoint;
			URL url = new URL(strUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.addRequestProperty("Authorization", "Bearer " + token);
			connection.setRequestMethod("POST");
			connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setDoOutput(true);
			String parameterString = new String("message=" + message);
			PrintWriter printWriter = new PrintWriter(connection.getOutputStream());
			printWriter.print(parameterString);
			printWriter.close();
			connection.connect();

			int statusCode = connection.getResponseCode();
			if (statusCode == 200) {
				result = true;
			} else {
				throw new Exception("Error:(StatusCode)" + statusCode + ", " + connection.getResponseMessage());
			}
			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	private String replaceProcess(String txt) {
		txt = replaceAllRegex(txt, "\\\\", "￥"); // \
		return txt;
	}

	private String replaceAllRegex(String value, String regex, String replacement) {
		if (value == null || value.length() == 0 || regex == null || regex.length() == 0 || replacement == null)
			return "";
		return Pattern.compile(regex).matcher(value).replaceAll(replacement);
	}

	public String generateAuthLink(String userId) {
		try {
			String state = URLEncoder.encode(userId, "UTF-8");
			String clientIdValue = URLEncoder.encode(clientId, "UTF-8");
			String redirectUrlValue = URLEncoder.encode(redirectUri, "UTF-8");
			String data = "response_type=code" + "&scope=notify" + "&state="+state  + "&client_id=" + clientIdValue + "&redirect_uri=" + redirectUrlValue;
			return authorizeEndpoint + "?" + data;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return "";
	}

	private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (NameValuePair pair : params) {
			if (first)
				first = false;
			else
				result.append("&");
			result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
		}
		return result.toString();
	}

	public boolean callBack(String code, String userId) {
		log.warn(".....................userId:" + userId);
		try {
			String resp = getToken(code);
			LineNotifyCallbackResp lineNotifyCallbackResp = new Gson().fromJson(resp, LineNotifyCallbackResp.class);
			log.warn(".....................token:" + lineNotifyCallbackResp.getAccess_token());
			log.warn(".....................resp:" + resp);

			if (StringUtils.isBlank(lineNotifyCallbackResp.getAccess_token())) {
				return false;
			}
			userService.updateUserToken(userId, lineNotifyCallbackResp.getAccess_token());
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}
	}

	private String getToken(String code) {
		String result = null;

		try {
			URL url = new URL(getTokenEndpoint);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setDoOutput(true);
			List<NameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair("grant_type", "authorization_code"));
			params.add(new BasicNameValuePair("code", code));
			params.add(new BasicNameValuePair("client_id", clientId));
			params.add(new BasicNameValuePair("client_secret", clientSecret));
			params.add(new BasicNameValuePair("redirect_uri", redirectUri));

			OutputStream os = connection.getOutputStream();
			try( DataOutputStream wr = new DataOutputStream(os)) {
				wr.write(getQuery(params).getBytes(StandardCharsets.UTF_8));
				wr.flush();
			}
			os.close();
			connection.connect();

			int statusCode = connection.getResponseCode();

			BufferedReader br = null;
			if (connection.getResponseCode() ==200) {
				br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				result = br.lines().collect(Collectors.joining());
			} else {
				br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				result = br.lines().collect(Collectors.joining());
				throw new Exception("Error:(StatusCode)" + statusCode + ", " + result);
				}
			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result; //EX. {"status":200,"message":"access_token is issued","access_token":"CBMtxYQgXGFHGabgdTAu7hakElQKf3LCoCHAB85VYIl"}
	}


}