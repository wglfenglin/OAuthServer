package com.example.oauthserver.utils;

import net.sf.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Map;

@Slf4j
public class ESBHttpUtil {


	private static String account="qywxapp";

	private static String pass="qywxapp@2019";

	public static String httpPost(Map<String, Object> params,String url) {
		CloseableHttpClient httpClient = getHttpClient(account,pass);
		HttpPost httpPost = new HttpPost(url);
		String json = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonstr = objectMapper.writeValueAsString(params);

			StringEntity se = new StringEntity(jsonstr, "UTF-8");
			se.setContentType("text/json");
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			httpPost.setEntity(se);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity=  response.getEntity();

			json = EntityUtils.toString( entity, "UTF-8");
		} catch (Exception e) {
			log.error("请求异常",e);
		}
		return json;
	}

	/**
	 *  获取带有权限的HttpClient连接
	 */
	private static CloseableHttpClient getHttpClient(String username, String password) {
		CredentialsProvider provider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
		provider.setCredentials(AuthScope.ANY, credentials);
		return HttpClients.custom().setDefaultCredentialsProvider(provider).build();
	}

	/**
	 * 发起https请求并获取结果
	 *
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr 提交的数据
	 * @return String
	 */
	/**
	 * 发起https请求并获取结果
	 *
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) throws Exception {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			//URL url = new URL(requestUrl);
			URL url= new URL(null, requestUrl, new sun.net.www.protocol.https.Handler());
			HttpsURLConnection httpUrlConn = null;
			log.info("**********************调用微信接口URL：" + requestUrl + "***************************");
			/*
			  if("test".equals(null)){ //设置代理
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new
			  InetSocketAddress(ParamesAPI.proxyIp, ParamesAPI.proxyPort)); httpUrlConn=
			  (HttpsURLConnection) url.openConnection(proxy); }else{ httpUrlConn =
			  (HttpsURLConnection) url.openConnection(); }*/
			//  10.1.27.102   10.1.166.166

			//log.info("::::::::::::::"+wxUnifiedProperties.getProxyPort());
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new
					InetSocketAddress("10.1.166.166", 8080));

			httpUrlConn= (HttpsURLConnection) url.openConnection(proxy);
			//httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);
			httpUrlConn.connect();
			OutputStream outputStream = httpUrlConn.getOutputStream();
			// 当有数据需要提交时
			if (null != outputStr) {
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));

			}
			// 将返回的输入流转换成字符串
			log.info("返回状态" + httpUrlConn.getResponseCode());
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			outputStream.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.", ce);
			throw ce;
		} catch (Exception e) {
			log.error("https request error:{}", e);
			throw e;
		}
		log.info("**********************请求返回内容：" + jsonObject.toString() + "***************************");
		return jsonObject;
	}

	public static String doGet(String url, Map params) {
		String apiUrl = url;
		StringBuffer param =new StringBuffer();
		int i =0;
		for (Object key : params.keySet()) {
			if (i == 0) {
				param.append("?");
			}else{
  				param.append("&");
			}
			param.append(key).append("=").append(params.get(key));
			i++;
		}
		apiUrl += param;
		String result =null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response =null;
		try {
			HttpGet httpGet =new HttpGet(apiUrl);
			response = httpclient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}
			HttpEntity entity = response.getEntity();
			if (entity !=null) {
				result = EntityUtils.toString(entity,"UTF-8");
			}
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (response !=null) {
				try {
					EntityUtils.consume(response.getEntity());
				}catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;

	}

}
