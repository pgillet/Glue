package com.glue.api;

import com.glue.IStream;
import com.glue.api.application.Glue;
import com.glue.api.application.GlueFactory;
import com.glue.api.model.GlueException;

public class CreateStreamTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Glue glue = new GlueFactory().getInstance();

		// Connect ...

		// Create a stream
		try {
			IStream myFirstStream = glue.createStream("Mon premier test!!!");
			System.out.println(myFirstStream.getTitle());
		} catch (GlueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
