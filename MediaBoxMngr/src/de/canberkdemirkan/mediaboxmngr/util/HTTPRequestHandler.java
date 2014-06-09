package de.canberkdemirkan.mediaboxmngr.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * 
 * This class handles HTTP post and get methods receiving the target URI.
 * 
 * @param uri
 *            The target-URI for the HTTP-Request.
 * @author Canberk Demirkan
 * 
 */
public class HTTPRequestHandler {

	public static final String ERROR = "Unexpected Error! No HTTP-Response!";

	private String uri;

	public HTTPRequestHandler(String uri) {
		setUri(uri);
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * 
	 * @param params
	 *            The parameters added to the end of the URI.
	 * @return The response of the HTTP request converted into a String.
	 * @author Canberk Demirkan
	 * 
	 **/
	public String readHTTPGetResponse(String params) {
		HttpClient httpclient = new DefaultHttpClient();
		String uriWithParams = getUri() + params;

		try {
			HttpGet httpGet = new HttpGet(uriWithParams);
			HttpResponse response = httpclient.execute(httpGet);
			if (response != null) {
				InputStream inputstream = response.getEntity().getContent();
				String result = convertStreamToString(inputstream);
				return result;
			} else {
				return ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param params
	 *            A list of name value pairs which will be send as parameters.
	 * @return The response of the HTTP request converted into a String.
	 * @author Canberk Demirkan
	 * 
	 **/
	public String readHTTPPostResponse(List<NameValuePair> params) {
		HttpClient httpclient = new DefaultHttpClient();
		String uri = getUri();

		try {
			HttpPost httpPost = new HttpPost(uri);
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse response = httpclient.execute(httpPost);
			if (response != null) {
				InputStream inputstream = response.getEntity().getContent();
				String result = convertStreamToString(inputstream);
				return result;
			} else {
				return ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param is
	 *            An InputStream which will be converted to String.
	 * @return The InputStream converted into a String.
	 * 
	 **/
	private String convertStreamToString(InputStream is) {
		String line = "";
		StringBuilder sb = new StringBuilder();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		try {
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}