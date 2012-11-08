package com.glue.api.json;

import com.glue.api.external.http.HTMLEntity;
import com.glue.api.external.org.json.JSONException;
import com.glue.api.external.org.json.JSONObject;

public final class JSONUtil {

	public static String getUnescapedString(String str, JSONObject json) {
		return HTMLEntity.unescape(getRawString(str, json));
	}

	public static String getRawString(String name, JSONObject json) {
		try {
			if (json.isNull(name)) {
				return null;
			} else {
				return json.getString(name);
			}
		} catch (JSONException jsone) {
			return null;
		}
	}

}
