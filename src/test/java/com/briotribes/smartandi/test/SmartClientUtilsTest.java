package com.briotribes.smartandi.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.junit.Test;

import com.briotribes.smartapi.SmartClientUtils;
import com.briotribes.smartapi.SmartConfig;

public class SmartClientUtilsTest {
	@Test
	public void testSubmitToSmart() throws IOException, URISyntaxException, JSONException {
		SmartConfig config = new SmartConfig();
		config.server = "128.199.233.96";
		config.tenant = "gocharlie";
		config.origin = "128.199.233.96";
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("config", 1);
		data.put("group", "Package");
		data.put("size", 50);
		Map<String, Object> adminData = new HashMap<String, Object>();
		adminData.put("___smart_action___", "lookup");
		adminData.put("___smart_value___", "GCTripFlow");
		data.put("FlowAdmin", adminData);
		SmartClientUtils.submitToSmart(config, "gocharlie", "GCTripFlow",
				"ListAllEvent", data);
	}
}
