package com.glue.webapp.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;

public class GSonHelper {

	public static <T> T getGsonObjectFromRequest(HttpServletRequest request, Type aClass) throws IOException {

		Gson gson = new Gson();
		BufferedReader reader = request.getReader();
		StringBuilder sb = new StringBuilder();
		String line = reader.readLine();
		while (line != null) {
			sb.append(line + "\n");
			line = reader.readLine();
		}
		reader.close();
		if (sb.toString() != null) {
			return (gson.fromJson(sb.toString(), aClass));
		}
		return null;
	}

}
