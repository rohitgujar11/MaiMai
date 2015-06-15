package com.nanostuffs.maimai;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

public class CustomHttpClient {
	static InputStream is = null;
	/** The time it takes for our client to timeout */
	public static final int HTTP_TIMEOUT = 100 * 1000; // milliseconds
	/** Single instance of our HttpClient */
	private static HttpClient mHttpClient;
	private static String result;

	/**
	 * Get our single instance of our HttpClient object.
	 * 
	 * @return an HttpClient object with connection parameters set
	 */

	private static HttpClient getHttpClient() {

		if (mHttpClient == null) {
			mHttpClient = new DefaultHttpClient();

			final HttpParams params = mHttpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
			ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
		}

		return mHttpClient;
	}

	public static DefaultHttpClient getThreadSafeClient() {
		DefaultHttpClient client = new DefaultHttpClient();
		ClientConnectionManager mgr = client.getConnectionManager();
		HttpParams params = client.getParams();
		client = new DefaultHttpClient(new ThreadSafeClientConnManager(params,
				mgr.getSchemeRegistry()), params);
		return client;
	}

	/**
	 * Performs an HTTP Post request to the specified url with the specified
	 * parameters.
	 * 
	 * @param url
	 *            The web address to post the request to
	 * @param postParameters
	 *            The parameters to send via the request
	 * @return The result of the request
	 * @throws Exception
	 */

	public static String executeHttpPost(String url,
			ArrayList<NameValuePair> postParameters) throws Exception {

		BufferedReader in = null;

		try {

			HttpClient client = getHttpClient();

			HttpPost request = new HttpPost(url);

			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(

			postParameters);

			request.setEntity(formEntity);

			HttpResponse response = getThreadSafeClient().execute(request);

			in = new BufferedReader(new InputStreamReader(response.getEntity()

			.getContent()));

			StringBuffer sb = new StringBuffer("");

			String line = "";

			String NL = System.getProperty("line.separator");

			while ((line = in.readLine()) != null) {

				sb.append(line + NL);

			}

			in.close();

			String result = sb.toString();

			return result;

		} finally {

			if (in != null) {

				try {

					in.close();

				}

				catch (HttpResponseException e) {

					Log.e("log_tag", "httpresponse exception " + e.toString());
				}

				catch (IOException e) {

					Log.e("log_tag", "Error converting result " + e.toString());

					e.printStackTrace();

				}

			}

		}

	}

	public static String executeHttpPostForImg(String url,
			ArrayList<NameValuePair> postParameters, String name, byte[] data)
			throws Exception {

		BufferedReader in = null;

		try {

			HttpClient client = getHttpClient();
			MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);

			HttpPost request = new HttpPost(url);

			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(

			postParameters);

			ByteArrayBody bab = new ByteArrayBody(data, name + ".jpg");
			entity.addPart("userfile", bab);
			StringBody sname = new StringBody(name);
			entity.addPart("name", sname);
			for (int index = 0; index < postParameters.size(); index++) {
				if (postParameters.get(index).getName()
						.equalsIgnoreCase("image")) {
					// If the key equals to "image", we use FileBody to transfer
					// the data
					entity.addPart(postParameters.get(index).getName(),
							new FileBody(new File(postParameters.get(index)
									.getValue())));
				} else {
					// Normal string data
					Log.e("", "text : " + postParameters.get(index).getName());
					entity.addPart(
							postParameters.get(index).getName(),
							new StringBody(postParameters.get(index).getValue()));
				}
			}

			request.setEntity(entity);

			HttpResponse response = getThreadSafeClient().execute(request);

			in = new BufferedReader(new InputStreamReader(response.getEntity()

			.getContent()));

			StringBuffer sb = new StringBuffer("");

			String line = "";

			String NL = System.getProperty("line.separator");

			while ((line = in.readLine()) != null) {

				sb.append(line + NL);

			}

			in.close();

			String result = sb.toString();

			return result;

		} finally {

			if (in != null) {

				try {

					in.close();

				}

				catch (HttpResponseException e) {

					Log.e("log_tag", "httpresponse exception " + e.toString());
				}

				catch (IOException e) {

					Log.e("log_tag", "Error converting result " + e.toString());

					e.printStackTrace();

				}

			}

		}

	}

	public static String executeHttpPostForImgChat(String url,
			ArrayList<NameValuePair> postParameters, String name, byte[] data)
			throws Exception {

		BufferedReader in = null;

		try {

			HttpClient client = getHttpClient();
			MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);

			HttpPost request = new HttpPost(url);

			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(

			postParameters);

			ByteArrayBody bab = new ByteArrayBody(data, name + ".jpg");
			entity.addPart("chatimage", bab);
			StringBody sname = new StringBody(name);
			entity.addPart("name", sname);
			for (int index = 0; index < postParameters.size(); index++) {
				if (postParameters.get(index).getName()
						.equalsIgnoreCase("image")) {
					// If the key equals to "image", we use FileBody to transfer
					// the data
					entity.addPart(postParameters.get(index).getName(),
							new FileBody(new File(postParameters.get(index)
									.getValue())));
				} else {
					// Normal string data
					Log.e("", "text : " + postParameters.get(index).getName());
					entity.addPart(
							postParameters.get(index).getName(),
							new StringBody(postParameters.get(index).getValue()));
				}
			}

			request.setEntity(entity);

			HttpResponse response = getThreadSafeClient().execute(request);

			in = new BufferedReader(new InputStreamReader(response.getEntity()

			.getContent()));

			StringBuffer sb = new StringBuffer("");

			String line = "";

			String NL = System.getProperty("line.separator");

			while ((line = in.readLine()) != null) {

				sb.append(line + NL);

			}

			in.close();

			String result = sb.toString();

			return result;

		} finally {

			if (in != null) {

				try {

					in.close();

				}

				catch (HttpResponseException e) {

					Log.e("log_tag", "httpresponse exception " + e.toString());
				}

				catch (IOException e) {

					Log.e("log_tag", "Error converting result " + e.toString());

					e.printStackTrace();

				}

			}

		}

	}

	public static String executeHttpPostForAudio(String url,
			ArrayList<NameValuePair> postParameters, String name, byte[] data)
			throws Exception {

		BufferedReader in = null;

		try {

			HttpClient client = getHttpClient();
			MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);

			HttpPost request = new HttpPost(url);

			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(

			postParameters);

			ByteArrayBody bab = new ByteArrayBody(data, name + ".mp3");
			entity.addPart("audio", bab);
			StringBody sname = new StringBody(name);
			entity.addPart("name", sname);
			for (int index = 0; index < postParameters.size(); index++) {
				if (postParameters.get(index).getName()
						.equalsIgnoreCase("image")) {
					// If the key equals to "image", we use FileBody to transfer
					// the data
					entity.addPart(postParameters.get(index).getName(),
							new FileBody(new File(postParameters.get(index)
									.getValue())));
				} else {
					// Normal string data
					Log.e("", "text : " + postParameters.get(index).getName());
					entity.addPart(
							postParameters.get(index).getName(),
							new StringBody(postParameters.get(index).getValue()));
				}
			}

			request.setEntity(entity);

			HttpResponse response = getThreadSafeClient().execute(request);

			in = new BufferedReader(new InputStreamReader(response.getEntity()

			.getContent()));

			StringBuffer sb = new StringBuffer("");

			String line = "";

			String NL = System.getProperty("line.separator");

			while ((line = in.readLine()) != null) {

				sb.append(line + NL);

			}

			in.close();

			String result = sb.toString();

			return result;

		} finally {

			if (in != null) {

				try {

					in.close();

				}

				catch (HttpResponseException e) {

					Log.e("log_tag", "httpresponse exception " + e.toString());
				}

				catch (IOException e) {

					Log.e("log_tag", "Error converting result " + e.toString());

					e.printStackTrace();

				}

			}

		}

	}

	public static String executeHttpPostForImg2(String url,
			ArrayList<NameValuePair> postParameters, String name0,
			byte[] data0, String name1, byte[] data1, String name2,
			byte[] data2, String name3, byte[] data3) throws Exception {

		BufferedReader in = null;

		try {

			HttpClient client = getHttpClient();
			MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);

			HttpPost request = new HttpPost(url);

			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(

			postParameters);
			ByteArrayBody bab = new ByteArrayBody(data0, name0 + ".jpg");
			entity.addPart("itemimg0", bab);
			ByteArrayBody bab1 = new ByteArrayBody(data1, name1 + ".jpg");
			entity.addPart("itemimg1", bab1);
			ByteArrayBody bab2 = new ByteArrayBody(data2, name2 + ".jpg");
			entity.addPart("itemimg2", bab2);
			ByteArrayBody bab3 = new ByteArrayBody(data3, name3 + ".jpg");
			entity.addPart("itemimg3", bab3);

			StringBody sname0 = new StringBody(name0);
			entity.addPart("name0", sname0);
			StringBody sname = new StringBody(name1);
			entity.addPart("name1", sname);
			StringBody sname2 = new StringBody(name2);
			entity.addPart("name2", sname2);
			StringBody sname3 = new StringBody(name3);
			entity.addPart("name3", sname3);

			for (int index = 0; index < postParameters.size(); index++) {
				if (postParameters.get(index).getName()
						.equalsIgnoreCase("image")) {
					// If the key equals to "image", we use FileBody to transfer
					// the data
					entity.addPart(postParameters.get(index).getName(),
							new FileBody(new File(postParameters.get(index)
									.getValue())));
				} else {
					// Normal string data
					Log.e("", "text : " + postParameters.get(index).getName());
					entity.addPart(
							postParameters.get(index).getName(),
							new StringBody(postParameters.get(index).getValue()));
				}
			}

			request.setEntity(entity);

			HttpResponse response = getThreadSafeClient().execute(request);

			in = new BufferedReader(new InputStreamReader(response.getEntity()

			.getContent()));

			StringBuffer sb = new StringBuffer("");

			String line = "";

			String NL = System.getProperty("line.separator");

			while ((line = in.readLine()) != null) {

				sb.append(line + NL);

			}

			in.close();

			String result = sb.toString();

			return result;

		} finally {

			if (in != null) {

				try {

					in.close();

				}

				catch (HttpResponseException e) {

					Log.e("log_tag", "httpresponse exception " + e.toString());
				}

				catch (IOException e) {

					Log.e("log_tag", "Error converting result " + e.toString());

					e.printStackTrace();

				}

			}

		}

	}

	// public static String executeHttpPostForImg10(String url,
	// ArrayList<NameValuePair> postParameters, String name0,
	// byte[] data0, String name1, byte[] data1, String name2,
	// byte[] data2, String name3, byte[] data3, String name4,
	// byte[] data4, String name5, byte[] data5, String name6,
	// byte[] data6, String name7, byte[] data7, String name8,
	// byte[] data8, String name9, byte[] data9, String mExtension0,
	// String mExtension1, String mExtension2, String mExtension3,
	// String mExtension4, String mExtension5, String mExtension6,
	// String mExtension7, String mExtension8, String mExtension9)
	// throws Exception {
	//
	// Log.e("", "name0 : " + name0 + "   name1: " + name1 + "name2 : "
	// + name2 + "   name3: " + name3 + "name4 : " + name4
	// + "   name5: " + name5 + "name6 : " + name6 + "   name7: "
	// + name7 + "name8 : " + name8 + "   name9: " + name9);
	//
	// BufferedReader in = null;
	//
	// try {
	//
	// HttpClient client = getHttpClient();
	// MultipartEntity entity = new MultipartEntity(
	// HttpMultipartMode.BROWSER_COMPATIBLE);
	//
	// HttpPost request = new HttpPost(url);
	//
	// UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
	//
	// postParameters);
	// ByteArrayBody bab = new ByteArrayBody(data0, name0 + mExtension0);
	// entity.addPart(name0, bab);
	// ByteArrayBody bab1 = new ByteArrayBody(data1, name1 + mExtension1);
	// entity.addPart(name1, bab1);
	// ByteArrayBody bab2 = new ByteArrayBody(data2, name2 + mExtension2);
	// entity.addPart(name2, bab2);
	// ByteArrayBody bab3 = new ByteArrayBody(data3, name3 + mExtension3);
	// entity.addPart(name3, bab3);
	// ByteArrayBody bab4 = new ByteArrayBody(data4, name4 + mExtension4);
	// entity.addPart(name4, bab4);
	// ByteArrayBody bab5 = new ByteArrayBody(data5, name5 + mExtension5);
	// entity.addPart(name5, bab5);
	// ByteArrayBody bab6 = new ByteArrayBody(data6, name6 + mExtension6);
	// entity.addPart(name6, bab6);
	// ByteArrayBody bab7 = new ByteArrayBody(data7, name7 + mExtension7);
	// entity.addPart(name7, bab7);
	// ByteArrayBody bab8 = new ByteArrayBody(data8, name8 + mExtension8);
	// entity.addPart(name8, bab8);
	// ByteArrayBody bab9 = new ByteArrayBody(data9, name9 + mExtension9);
	// entity.addPart(name9, bab9);
	//
	// StringBody sname0 = new StringBody(name0);
	// entity.addPart("name0", sname0);
	// StringBody sname = new StringBody(name1);
	// entity.addPart("name1", sname);
	// StringBody sname2 = new StringBody(name2);
	// entity.addPart("name2", sname2);
	// StringBody sname3 = new StringBody(name3);
	// entity.addPart("name3", sname3);
	// StringBody sname4 = new StringBody(name4);
	// entity.addPart("name4", sname4);
	// StringBody sname5 = new StringBody(name5);
	// entity.addPart("name5", sname5);
	// StringBody sname6 = new StringBody(name6);
	// entity.addPart("name6", sname6);
	// StringBody sname7 = new StringBody(name7);
	// entity.addPart("name7", sname7);
	// StringBody sname8 = new StringBody(name8);
	// entity.addPart("name8", sname8);
	// StringBody sname9 = new StringBody(name9);
	// entity.addPart("name9", sname9);
	//
	// for (int index = 0; index < postParameters.size(); index++) {
	// if (postParameters.get(index).getName()
	// .equalsIgnoreCase("image")) {
	// // If the key equals to "image", we use FileBody to transfer
	// // the data
	// entity.addPart(postParameters.get(index).getName(),
	// new FileBody(new File(postParameters.get(index)
	// .getValue())));
	// } else {
	// // Normal string data
	// Log.e("", "text : " + postParameters.get(index).getName());
	// entity.addPart(
	// postParameters.get(index).getName(),
	// new StringBody(postParameters.get(index).getValue()));
	// }
	// }
	//
	// request.setEntity(entity);
	//
	// HttpResponse response = getThreadSafeClient().execute(request);
	//
	// in = new BufferedReader(new InputStreamReader(response.getEntity()
	//
	// .getContent()));
	//
	// StringBuffer sb = new StringBuffer("");
	//
	// String line = "";
	//
	// String NL = System.getProperty("line.separator");
	//
	// while ((line = in.readLine()) != null) {
	//
	// sb.append(line + NL);
	//
	// }
	//
	// in.close();
	//
	// String result = sb.toString();
	//
	// return result;
	//
	// } finally {
	//
	// if (in != null) {
	//
	// try {
	//
	// in.close();
	//
	// }
	//
	// catch (HttpResponseException e) {
	//
	// Log.e("log_tag", "httpresponse exception " + e.toString());
	// }
	//
	// catch (IOException e) {
	//
	// Log.e("log_tag", "Error converting result " + e.toString());
	//
	// e.printStackTrace();
	//
	// }
	//
	// }
	//
	// }
	//
	// }

	// public static String executeHttpPostForImg10(String url,
	// ArrayList<NameValuePair> postParameters, String name0,
	// byte[] data0, String name1, byte[] data1, String name2,
	// byte[] data2, String name3, byte[] data3, String name4,
	// byte[] data4, String name5, byte[] data5, String name6,
	// byte[] data6, String name7, byte[] data7, String name8,
	// byte[] data8, String name9, byte[] data9, String name10,
	// byte[] data10, String name11, byte[] data11, String name12,
	// byte[] data12, String name13, byte[] data13, String name14,
	// byte[] data14, String name15, byte[] data15, String name16,
	// byte[] data16, String name17, String name18, String mExtension0,
	// String mExtension1, String mExtension2, String mExtension3,
	// String mExtension4, String mExtension5, String mExtension6,
	// String mExtension7, String mExtension8, String mExtension9,
	// String mExtension10, String mExtension11, String mExtension12,
	// String mExtension13, String mExtension14, String mExtension15,
	// String mExtension16, String mExtension17) throws Exception {
	//
	// Log.e("", "name0 : " + name0 + "   name1: " + name1 + "name2 : "
	// + name2 + "   name3: " + name3 + "name4 : " + name4
	// + "   name5: " + name5 + "name6 : " + name6 + "   name7: "
	// + name7 + "name8 : " + name8 + "   name9: " + name9
	// + "name10 : " + name10 + "   name11: " + name11 + "name12 : "
	// + name12 + "   name13: " + name13 + "name14 : " + name14
	// + "   name15: " + name15 + "name16 : " + name16 + "   name17: "
	// + name17 + "name18 : " + name18);
	//
	// BufferedReader in = null;
	//
	// try {
	//
	// HttpClient client = getHttpClient();
	// MultipartEntity entity = new MultipartEntity(
	// HttpMultipartMode.BROWSER_COMPATIBLE);
	//
	// HttpPost request = new HttpPost(url);
	//
	// UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
	//
	// postParameters);
	// ByteArrayBody bab = new ByteArrayBody(data0, name0 + mExtension0);
	// entity.addPart(name0, bab);
	// ByteArrayBody bab1 = new ByteArrayBody(data1, name1 + mExtension1);
	// entity.addPart(name1, bab1);
	// ByteArrayBody bab2 = new ByteArrayBody(data2, name2 + mExtension2);
	// entity.addPart(name2, bab2);
	// ByteArrayBody bab3 = new ByteArrayBody(data3, name3 + mExtension3);
	// entity.addPart(name3, bab3);
	// ByteArrayBody bab4 = new ByteArrayBody(data4, name4 + mExtension4);
	// entity.addPart(name4, bab4);
	// ByteArrayBody bab5 = new ByteArrayBody(data5, name5 + mExtension5);
	// entity.addPart(name5, bab5);
	// ByteArrayBody bab6 = new ByteArrayBody(data6, name6 + mExtension6);
	// entity.addPart(name6, bab6);
	// ByteArrayBody bab7 = new ByteArrayBody(data7, name7 + mExtension7);
	// entity.addPart(name7, bab7);
	// ByteArrayBody bab8 = new ByteArrayBody(data8, name8 + mExtension8);
	// entity.addPart(name8, bab8);
	// ByteArrayBody bab9 = new ByteArrayBody(data9, name9 + mExtension9);
	// entity.addPart(name9, bab9);
	//
	// ByteArrayBody bab10 = new ByteArrayBody(data10, name10
	// + mExtension10);
	// entity.addPart(name10, bab10);
	// ByteArrayBody bab11 = new ByteArrayBody(data11, name11
	// + mExtension11);
	// entity.addPart(name11, bab11);
	// ByteArrayBody bab12 = new ByteArrayBody(data12, name12
	// + mExtension12);
	// entity.addPart(name12, bab12);
	// ByteArrayBody bab13 = new ByteArrayBody(data13, name13
	// + mExtension13);
	// entity.addPart(name13, bab13);
	// ByteArrayBody bab14 = new ByteArrayBody(data14, name14
	// + mExtension14);
	// entity.addPart(name14, bab14);
	// ByteArrayBody bab15 = new ByteArrayBody(data15, name15
	// + mExtension15);
	// entity.addPart(name15, bab15);
	// ByteArrayBody bab16 = new ByteArrayBody(data16, name16
	// + mExtension16);
	// entity.addPart(name16, bab16);
	// // ByteArrayBody bab17 = new ByteArrayBody(data17, name17 +
	// // mExtension17);
	// // entity.addPart(name17, bab17);
	// // ByteArrayBody bab18 = new ByteArrayBody(data18, name18 +
	// // mExtension18);
	// // entity.addPart(name18, bab18);
	// //
	//
	// StringBody sname0 = new StringBody(name0);
	// entity.addPart("name0", sname0);
	// StringBody sname = new StringBody(name1);
	// entity.addPart("name1", sname);
	// StringBody sname2 = new StringBody(name2);
	// entity.addPart("name2", sname2);
	// StringBody sname3 = new StringBody(name3);
	// entity.addPart("name3", sname3);
	// StringBody sname4 = new StringBody(name4);
	// entity.addPart("name4", sname4);
	// StringBody sname5 = new StringBody(name5);
	// entity.addPart("name5", sname5);
	// StringBody sname6 = new StringBody(name6);
	// entity.addPart("name6", sname6);
	// StringBody sname7 = new StringBody(name7);
	// entity.addPart("name7", sname7);
	// StringBody sname8 = new StringBody(name8);
	// entity.addPart("name8", sname8);
	// StringBody sname9 = new StringBody(name9);
	// entity.addPart("name9", sname9);
	//
	// StringBody sname10 = new StringBody(name10);
	// entity.addPart("name10", sname10);
	// StringBody sname11 = new StringBody(name11);
	// entity.addPart("name11", sname11);
	// StringBody sname12 = new StringBody(name12);
	// entity.addPart("name12", sname12);
	// StringBody sname13 = new StringBody(name13);
	// entity.addPart("name13", sname13);
	// StringBody sname14 = new StringBody(name14);
	// entity.addPart("name14", sname14);
	// StringBody sname15 = new StringBody(name15);
	// entity.addPart("name15", sname15);
	// StringBody sname16 = new StringBody(name16);
	// entity.addPart("name16", sname16);
	// // StringBody sname17 = new StringBody(name17);
	// // entity.addPart("name17", sname17);
	// // StringBody sname18 = new StringBody(name18);
	// // entity.addPart("name18", sname18);
	//
	// for (int index = 0; index < postParameters.size(); index++) {
	// if (postParameters.get(index).getName()
	// .equalsIgnoreCase("image")) {
	// // If the key equals to "image", we use FileBody to transfer
	// // the data
	// entity.addPart(postParameters.get(index).getName(),
	// new FileBody(new File(postParameters.get(index)
	// .getValue())));
	// } else {
	// // Normal string data
	// Log.e("", "text : " + postParameters.get(index).getName());
	// entity.addPart(
	// postParameters.get(index).getName(),
	// new StringBody(postParameters.get(index).getValue()));
	// }
	// }
	//
	// request.setEntity(entity);
	//
	// HttpResponse response = getThreadSafeClient().execute(request);
	//
	// in = new BufferedReader(new InputStreamReader(response.getEntity()
	//
	// .getContent()));
	//
	// StringBuffer sb = new StringBuffer("");
	//
	// String line = "";
	//
	// String NL = System.getProperty("line.separator");
	//
	// while ((line = in.readLine()) != null) {
	//
	// sb.append(line + NL);
	//
	// }
	//
	// in.close();
	//
	// String result = sb.toString();
	//
	// return result;
	//
	// } finally {
	//
	// if (in != null) {
	//
	// try {
	//
	// in.close();
	//
	// }
	//
	// catch (HttpResponseException e) {
	//
	// Log.e("log_tag", "httpresponse exception " + e.toString());
	// }
	//
	// catch (IOException e) {
	//
	// Log.e("log_tag", "Error converting result " + e.toString());
	//
	// e.printStackTrace();
	//
	// }
	//
	// }
	//
	// }
	//
	// }

	public static String executeHttpPostForImg1(String url,
			ArrayList<NameValuePair> postParameters, String name1,
			byte[] data1, String name2, byte[] data2, String name3, byte[] data3)
			throws Exception {

		BufferedReader in = null;

		try {

			HttpClient client = getHttpClient();
			MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);

			HttpPost request = new HttpPost(url);

			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(

			postParameters);

			ByteArrayBody bab1 = new ByteArrayBody(data1, name1 + ".jpg");
			entity.addPart("player_pic1", bab1);
			ByteArrayBody bab2 = new ByteArrayBody(data2, name2 + ".jpg");
			entity.addPart("player_pic2", bab2);
			ByteArrayBody bab3 = new ByteArrayBody(data3, name3 + ".jpg");
			entity.addPart("player_pic3", bab3);

			StringBody sname = new StringBody(name1);
			entity.addPart("name1", sname);
			StringBody sname2 = new StringBody(name2);
			entity.addPart("name2", sname2);
			StringBody sname3 = new StringBody(name3);
			entity.addPart("name3", sname3);

			for (int index = 0; index < postParameters.size(); index++) {
				if (postParameters.get(index).getName()
						.equalsIgnoreCase("image")) {
					// If the key equals to "image", we use FileBody to transfer
					// the data
					entity.addPart(postParameters.get(index).getName(),
							new FileBody(new File(postParameters.get(index)
									.getValue())));
				} else {
					// Normal string data
					Log.e("", "text : " + postParameters.get(index).getName());
					entity.addPart(
							postParameters.get(index).getName(),
							new StringBody(postParameters.get(index).getValue()));
				}
			}

			request.setEntity(entity);

			HttpResponse response = getThreadSafeClient().execute(request);

			in = new BufferedReader(new InputStreamReader(response.getEntity()

			.getContent()));

			StringBuffer sb = new StringBuffer("");

			String line = "";

			String NL = System.getProperty("line.separator");

			while ((line = in.readLine()) != null) {

				sb.append(line + NL);

			}

			in.close();

			String result = sb.toString();

			return result;

		} finally {

			if (in != null) {

				try {

					in.close();

				}

				catch (HttpResponseException e) {

					Log.e("log_tag", "httpresponse exception " + e.toString());
				}

				catch (IOException e) {

					Log.e("log_tag", "Error converting result " + e.toString());

					e.printStackTrace();

				}

			}

		}

	}

	/**
	 * 
	 * Performs an HTTP GET request to the specified url.
	 * 
	 * 
	 * 
	 * @param url
	 * 
	 *            The web address to post the request to
	 * 
	 * @return The result of the request
	 * 
	 * @throws Exception
	 */

	public static String executeHttpGet(String url) throws Exception {

		BufferedReader in = null;

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();

			HttpGet httpGet = new HttpGet();

			httpGet.setURI(new URI(url));
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();

			in = new BufferedReader(new InputStreamReader(httpResponse
					.getEntity()

					.getContent()));

			StringBuffer sb = new StringBuffer("");

			String line = "";

			String NL = System.getProperty("line.separator");

			while ((line = in.readLine()) != null) {

				sb.append(line + NL);

			}

			in.close();

			String result = sb.toString();
			Log.d("heyy", result);
			return result;

		} finally {

			if (in != null) {

				try {

					in.close();

				} catch (IOException e) {

					Log.e("log_tag", "Error converting result " + e.toString());

					e.printStackTrace();

				}

			}

		}

	}

	public static String executeHttpPostForImg10(String url,
			ArrayList<NameValuePair> postParameters, String name0,
			byte[] data0, String name1, byte[] data1, String name2,
			byte[] data2, String name3, byte[] data3, String name4,
			byte[] data4, String name5, byte[] data5, String name6,
			byte[] data6, String name7, byte[] data7, String name8,
			byte[] data8, String name9, byte[] data9, String name10,
			byte[] data10, String name11, byte[] data11, String name12,
			byte[] data12, String name13, byte[] data13, String name14,
			byte[] data14, String name15, byte[] data15, String name16,
			byte[] data16, String name17, byte[] data17, String name18,
			byte[] data18, String mExtension0, String mExtension1,
			String mExtension2, String mExtension3, String mExtension4,
			String mExtension5, String mExtension6, String mExtension7,
			String mExtension8, String mExtension9, String mExtension10,
			String mExtension11, String mExtension12, String mExtension13,
			String mExtension14, String mExtension15, String mExtension16,
			String mExtension17, String mExtension18, String mExtension19)
			throws ClientProtocolException, IOException {
		BufferedReader in = null;

		Log.e("", "name0 : " + name0 + "name1" + name1 + "name2" + name2
				+ "name3" + name3 + "name4 : " + name4 + "name5" + name5
				+ "name6" + name6 + "name7" + name7 + "name8 : " + name8
				+ "name9" + name9 + "name10" + name10 + "name11" + name11
				+ "name12 : " + name12 + "name13" + name13 + "name14" + name14
				+ "name15" + name15 + "name16" + name16 + "name17" + name17
				+ "name18" + name18);
		Log.e("", "mExtension0 : " + mExtension0 + "mExtension1" + mExtension1
				+ "mExtension2" + mExtension2 + "mExtension3" + mExtension3
				+ "mExtension4 : " + mExtension4 + "mExtension5 : "
				+ mExtension5 + "mExtension6 :" + mExtension6
				+ "mExtension7  : " + mExtension7 + "mExtension8 : "
				+ mExtension8 + "mExtension9" + mExtension9 + "mExtension10"
				+ mExtension10 + "mExtension11" + mExtension11
				+ "mExtension12 : " + mExtension12 + "mExtension13"
				+ mExtension13 + "mExtension14" + mExtension14 + "mExtension15"
				+ mExtension15 + "mExtension16" + mExtension16 + "mExtension17"
				+ mExtension17 + "mExtension18" + mExtension18);

		try {

			HttpClient client = getHttpClient();
			MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);

			HttpPost request = new HttpPost(url);

			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(

			postParameters);
			ByteArrayBody bab = new ByteArrayBody(data0, name0 + mExtension0);
			entity.addPart(name0, bab);
			ByteArrayBody bab1 = new ByteArrayBody(data1, name1 + mExtension1);
			entity.addPart(name1, bab1);
			ByteArrayBody bab2 = new ByteArrayBody(data2, name2 + mExtension2);
			entity.addPart(name2, bab2);
			ByteArrayBody bab3 = new ByteArrayBody(data3, name3 + mExtension3);
			entity.addPart(name3, bab3);
			ByteArrayBody bab4 = new ByteArrayBody(data4, name4 + mExtension4);
			entity.addPart(name4, bab4);
			ByteArrayBody bab5 = new ByteArrayBody(data5, name5 + mExtension5);
			entity.addPart(name5, bab5);
			ByteArrayBody bab6 = new ByteArrayBody(data6, name6 + mExtension6);
			entity.addPart(name6, bab6);
			ByteArrayBody bab7 = new ByteArrayBody(data7, name7 + mExtension7);
			entity.addPart(name7, bab7);
			ByteArrayBody bab8 = new ByteArrayBody(data8, name8 + mExtension8);
			entity.addPart(name8, bab8);
			ByteArrayBody bab9 = new ByteArrayBody(data9, name9 + mExtension9);
			entity.addPart(name9, bab9);

			ByteArrayBody bab10 = new ByteArrayBody(data10, name10
					+ mExtension10);
			entity.addPart(name10, bab10);
			ByteArrayBody bab11 = new ByteArrayBody(data11, name11
					+ mExtension11);
			entity.addPart(name11, bab11);
			ByteArrayBody bab12 = new ByteArrayBody(data12, name12
					+ mExtension12);
			entity.addPart(name12, bab12);
			ByteArrayBody bab13 = new ByteArrayBody(data13, name13
					+ mExtension13);
			entity.addPart(name13, bab13);
			ByteArrayBody bab14 = new ByteArrayBody(data14, name14
					+ mExtension14);
			entity.addPart(name14, bab14);
			ByteArrayBody bab15 = new ByteArrayBody(data15, name15
					+ mExtension15);
			entity.addPart(name15, bab15);
			ByteArrayBody bab16 = new ByteArrayBody(data16, name16
					+ mExtension16);
			entity.addPart(name16, bab16);
			ByteArrayBody bab17 = new ByteArrayBody(data17, name17
					+ mExtension17);
			entity.addPart(name17, bab17);
			ByteArrayBody bab18 = new ByteArrayBody(data18, name18
					+ mExtension18);
			entity.addPart(name18, bab18);

			StringBody sname0 = new StringBody(name0);
			entity.addPart("name0", sname0);
			StringBody sname = new StringBody(name1);
			entity.addPart("name1", sname);
			StringBody sname2 = new StringBody(name2);
			entity.addPart("name2", sname2);
			StringBody sname3 = new StringBody(name3);
			entity.addPart("name3", sname3);
			StringBody sname4 = new StringBody(name4);
			entity.addPart("name4", sname4);
			StringBody sname5 = new StringBody(name5);
			entity.addPart("name5", sname5);
			StringBody sname6 = new StringBody(name6);
			entity.addPart("name6", sname6);
			StringBody sname7 = new StringBody(name7);
			entity.addPart("name7", sname7);
			StringBody sname8 = new StringBody(name8);
			entity.addPart("name8", sname8);
			StringBody sname9 = new StringBody(name9);
			entity.addPart("name9", sname9);

			StringBody sname10 = new StringBody(name10);
			entity.addPart("name10", sname10);
			StringBody sname11 = new StringBody(name11);
			entity.addPart("name11", sname11);
			StringBody sname12 = new StringBody(name12);
			entity.addPart("name12", sname12);
			StringBody sname13 = new StringBody(name13);
			entity.addPart("name13", sname13);
			StringBody sname14 = new StringBody(name14);
			entity.addPart("name14", sname14);
			StringBody sname15 = new StringBody(name15);
			entity.addPart("name15", sname15);
			StringBody sname16 = new StringBody(name16);
			entity.addPart("name16", sname16);
			StringBody sname17 = new StringBody(name17);
			entity.addPart("name17", sname17);
			StringBody sname18 = new StringBody(name18);
			entity.addPart("name18", sname18);

			for (int index = 0; index < postParameters.size(); index++) {
				if (postParameters.get(index).getName()
						.equalsIgnoreCase("image")) {
					// If the key equals to "image", we use FileBody to transfer
					// the data
					entity.addPart(postParameters.get(index).getName(),
							new FileBody(new File(postParameters.get(index)
									.getValue())));
				} else {
					// Normal string data
					Log.e("", "text : " + postParameters.get(index).getName());
					entity.addPart(
							postParameters.get(index).getName(),
							new StringBody(postParameters.get(index).getValue()));
				}
			}

			request.setEntity(entity);

			HttpResponse response = getThreadSafeClient().execute(request);

			in = new BufferedReader(new InputStreamReader(response.getEntity()

			.getContent()));

			StringBuffer sb = new StringBuffer("");

			String line = "";

			String NL = System.getProperty("line.separator");

			while ((line = in.readLine()) != null) {

				sb.append(line + NL);

			}

			in.close();

			String result = sb.toString();

			return result;

		} finally {

			if (in != null) {

				try {

					in.close();

				}

				catch (HttpResponseException e) {

					Log.e("log_tag", "httpresponse exception " + e.toString());
				}

				catch (IOException e) {

					Log.e("log_tag", "Error converting result " + e.toString());

					e.printStackTrace();

				}

			}

		}
	}

	public static String executeHttpPostForImgChat1(String url,
			ArrayList<NameValuePair> postParameters, String name0,
			byte[] data0, String name1, byte[] data1) {
		BufferedReader in = null;

		try {

			HttpClient client = getHttpClient();
			MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);

			HttpPost request = new HttpPost(url);
			try {

				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(

				postParameters);
				ByteArrayBody bab = new ByteArrayBody(data0, name0 + ".jpg");
				entity.addPart("chatimage", bab);
				ByteArrayBody bab1 = new ByteArrayBody(data1, name1 + ".jpg");
				entity.addPart("imagethumb", bab1);

				StringBody sname0 = new StringBody(name0);
				entity.addPart("name0", sname0);
				StringBody sname = new StringBody(name1);
				entity.addPart("name1", sname);

				for (int index = 0; index < postParameters.size(); index++) {
					if (postParameters.get(index).getName()
							.equalsIgnoreCase("image")) {
						// If the key equals to "image", we use FileBody to
						// transfer
						// the data
						entity.addPart(postParameters.get(index).getName(),
								new FileBody(new File(postParameters.get(index)
										.getValue())));
					} else {
						// Normal string data
						Log.e("", "text : "
								+ postParameters.get(index).getName());
						entity.addPart(postParameters.get(index).getName(),
								new StringBody(postParameters.get(index)
										.getValue()));
					}
				}

				request.setEntity(entity);

				HttpResponse response = getThreadSafeClient().execute(request);

				in = new BufferedReader(new InputStreamReader(response
						.getEntity()

						.getContent()));

				StringBuffer sb = new StringBuffer("");

				String line = "";

				String NL = System.getProperty("line.separator");

				while ((line = in.readLine()) != null) {

					sb.append(line + NL);

				}

				in.close();

				result = sb.toString();
			} catch (Exception e) {
			}

			return result;

		} finally {

			if (in != null) {

				try {

					in.close();

				}

				catch (HttpResponseException e) {

					Log.e("log_tag", "httpresponse exception " + e.toString());
				}

				catch (IOException e) {

					Log.e("log_tag", "Error converting result " + e.toString());

					e.printStackTrace();

				}

			}

		}
	}

}