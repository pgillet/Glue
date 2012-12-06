package com.glue.webapp.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

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

	public static <T> T getGsonObjectFromMultiPartRequest(HttpServletRequest request, Type aClass) throws IOException {

		Gson gson = new Gson();

		Part jsonPart;
		try {
			jsonPart = request.getPart("json");
			String jsonObject = getValue(jsonPart);
			if (jsonObject != null) {
				return (gson.fromJson(jsonObject, aClass));
			}
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static Part getStreamPartFromMultiPartRequest(HttpServletRequest request) throws IOException {

		try {
			return request.getPart("file");

		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private static String getValue(Part part) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream(), "UTF-8"));
		StringBuilder value = new StringBuilder();
		char[] buffer = new char[1024];
		for (int length = 0; (length = reader.read(buffer)) > 0;) {
			value.append(buffer, 0, length);
		}
		return value.toString();
	}
}
