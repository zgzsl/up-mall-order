package com.zsl.upmall.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.zsl.upmall.util.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class SynQueryDemo {

	private String synUrl = "http://poll.kuaidi100.com/poll/query.do";
	private String key = "klQKEpZj7273";
	private String customer = "0230E4F8F6A00AB57F4A90862D5B9EEF";
	private Integer resultv = 2;
	private String autoUrl = "http://www.kuaidi100.com/autonumber/auto";


	public String getAutoCompany(String trackingNum){
		String result = HttpClientUtil.doGet(autoUrl+"?num="+trackingNum+"&key="+key);
		String companyCode = "";
		try {
			if(StringUtils.isNotBlank(result)){
				JSONArray json = JSON.parseArray(result);
				JSONObject jsonObject = json.getJSONObject(0);
				companyCode = jsonObject.getString("comCode");
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return companyCode;
	}

	public static void main(String[] args) {
		new SynQueryDemo().getAutoCompany("773040907408631");
	}


	public String synQueryData(String com, String num, String phone, String from, String to) {

		StringBuilder param = new StringBuilder("{");
		param.append("\"com\":\"").append(com).append("\"");
		param.append(",\"num\":\"").append(num).append("\"");
		param.append(",\"phone\":\"").append(phone).append("\"");
		param.append(",\"from\":\"").append(from).append("\"");
		param.append(",\"to\":\"").append(to).append("\"");
		if (1 == this.resultv) {
			param.append(",\"resultv2\":1");
		} else {
			param.append(",\"resultv2\":0");
		}
		param.append("}");

		Map<String, String> params = new HashMap<String, String>();
		params.put("customer", this.customer);
		String sign = MD5Utils.encode(param + this.key + this.customer);
		params.put("sign", sign);
		params.put("param", param.toString());

		return this.post(params);
	}

	public String post(Map<String, String> params) {
		StringBuffer response = new StringBuffer("");

		BufferedReader reader = null;
		try {
			StringBuilder builder = new StringBuilder();
			for (Map.Entry<String, String> param : params.entrySet()) {
				if (builder.length() > 0) {
					builder.append('&');
				}
				builder.append(URLEncoder.encode(param.getKey(), "UTF-8"));
				builder.append('=');
				builder.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
			}
			byte[] bytes = builder.toString().getBytes("UTF-8");

			URL url = new URL(synUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", String.valueOf(bytes.length));
			conn.setDoOutput(true);
			conn.getOutputStream().write(bytes);

			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

			String line = "";
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != reader) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return response.toString();
	}
}

class MD5Utils {
	private static MessageDigest mdigest = null;
	private static char digits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private static MessageDigest getMdInst() {
		if (null == mdigest) {
			try {
				mdigest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return mdigest;
	}

	public static String encode(String s) {
		if (null == s) {
			return "";
		}

		try {
			byte[] bytes = s.getBytes();
			getMdInst().update(bytes);
			byte[] md = getMdInst().digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = digits[byte0 >>> 4 & 0xf];
				str[k++] = digits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
