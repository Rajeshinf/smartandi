package com.briotribes.smartapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SmartClientUtils {

	private static final String URL_SEPARATOR = "/";

	private static final Logger LOGGER = Logger
			.getLogger(SmartClientUtils.class.getName());

	public static SmartResponse submitToSmart(SmartConfig config,
			String smarttenant, String smartflow, String eventName, Map data)
			throws IOException, URISyntaxException, JSONException {
		HttpResponse response = connectToSmart(config,
				buildUri(config, smarttenant, smartflow, eventName), data);
		return createResponse(response);
	}

	private static HttpResponse connectToSmart(SmartConfig config, URI uri, Map data)
			throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost();
		post.setURI(uri);
		client.getParams().setParameter("http.protocol.expect-continue", true);
		client.getParams().setParameter("http.socket.timeout", new Integer(0));
		if (notNullNotBlank(config.origin))
			post.setHeader("origin", config.origin);
        JSONObject json = new JSONObject(data);

		StringEntity entity = new StringEntity(json.toString(), HTTP.UTF_8);
		post.setEntity(entity);
		LOGGER.log(Level.INFO, "\nSending 'POST' request to URL : " + uri);
		for (Header j : post.getAllHeaders()) {
			System.out.println("Posting with " + j.getName() + " as "
					+ j.getValue());
		}
		org.apache.http.HttpResponse response = client.execute(post);
		return response;
	}

	private static SmartResponse createResponse(HttpResponse response)
			throws IOException, JSONException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(response
                .getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        JSONObject jsonData = new JSONObject(result.toString());

		// Check if responses is present
		JSONArray responses = jsonData.getJSONArray("responses");
		SmartResponse smartResponse = new SmartResponse();
		if (responses != null && responses.length() > 0) {
			// Extract the first item which will be Map of data
            JSONObject responseData = responses.optJSONObject(0);

			// Extract the unique response id for logging
			smartResponse.setResponseid((String) responseData
					.get("___smart_responseid___"));

		}

		LOGGER.log(Level.INFO, "The response has some content : " + jsonData);
		LOGGER.log(Level.INFO, "The smart response object is  : " + smartResponse);
		return smartResponse;
	}

	private static URI buildUri(SmartConfig config, String smarttenant,
			String smartflow, String eventName) throws URISyntaxException,
			MalformedURLException {
		int port = config.smartport;
		String path = URL_SEPARATOR + smarttenant + URL_SEPARATOR + smartflow
				+ URL_SEPARATOR + eventName;

		if ((eventName.equals("UploadEvent"))
				|| (eventName.equals("DownloadEvent"))) {
			port = config.uploadport;
			if (notNullNotBlank(config.portApiUploadMap)) {
				path = URL_SEPARATOR + config.portApiUploadMap + path;
			}
		} else if (notNullNotBlank(config.portApiMap)) {
			path = URL_SEPARATOR + config.portApiMap + path;
		}
		URI uri = new URI(config.protocol, null, config.server, port, path,
				null, null);

		return uri;
	}
	
	private static boolean notNullNotBlank(String checkString) {
		return (checkString != null && !checkString.trim().equals(""));

	}
}
