package com.briotribes.smartapi;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SmartClientUtils {

	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final String URL_SEPARATOR = "/";

	private static final Logger LOGGER = Logger
			.getLogger(SmartClientUtils.class.getName());

	public static SmartResponse submitToSmart(SmartConfig config,
			String smarttenant, String smartflow, String eventName, Object data)
			throws IOException, URISyntaxException {
		GenericUrl smartURL = new GenericUrl(buildUrl(config, smarttenant,
				smartflow, eventName));
		JsonHttpContent content = new JsonHttpContent(new GsonFactory(), data);
		HttpResponse response = postToSmart(config, smartURL, content);
		return createResponse(response);
	}

	private static URL buildUrl(SmartConfig config, String smarttenant,
			String smartflow, String eventName) throws URISyntaxException,
			MalformedURLException {
		int port = config.smartport;
		String path = URL_SEPARATOR + smarttenant + URL_SEPARATOR + smartflow
				+ URL_SEPARATOR + eventName;
		if ((eventName.equals("UploadEvent"))
				|| (eventName.equals("DownloadEvent"))) {
			port = config.uploadport;
		}
		URI uri = new URI(config.protocol, null, config.server, port, path,
				null, null);

		return uri.toURL();
	}

	private static SmartResponse createResponse(HttpResponse response)
			throws IOException {
		Map<String, Object> data = new HashMap<String, Object>();
		java.lang.reflect.Type mapType = new TypeToken<Map<String, Object>>() {
		}.getType();
		Gson gson = new Gson();
		Reader reader = new InputStreamReader(response.getContent());
		data = gson.fromJson(reader, mapType);

		// Check if responses is present
		List responses = (List) data.get("responses");
		SmartResponse smartResponse = new SmartResponse();
		if (responses != null && responses.size() > 0) {
			// Extract the first item which wil be Map of data
			Map responseData = (Map) responses.get(0);

			// Extract the unique response id for logging
			smartResponse.setResponseid((String) responseData
					.get("___smart_responseid___"));

		}

		LOGGER.log(Level.INFO, "The reponse has some content : " + data);

		return smartResponse;
	}

	private static HttpResponse postToSmart(SmartConfig config, GenericUrl url,
			HttpContent content) throws IOException {
		HttpRequestFactory requestFactory = HTTP_TRANSPORT
				.createRequestFactory();
		HttpResponse response = null;
		LOGGER.log(Level.INFO, "Posting data with content : " + content);
		HttpRequest request = requestFactory.buildPostRequest(url, content);
		HttpHeaders header = new HttpHeaders();
		header.set("Origin", config.origin);
		request.setHeaders(header);
		response = request.execute();
		LOGGER.log(Level.INFO, "The response received is  : " + response);
		return response;
	}
}
