package com.glue.api;

import java.util.Date;

import com.glue.api.application.Glue;
import com.glue.api.application.GlueFactory;
import com.glue.exceptions.GlueException;
import com.glue.struct.IStream;

public class CreateStreamTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Glue glue = new GlueFactory().getInstance();

		// Connect ...

		// Create a stream
		try {
			IStream myFirstStream = glue.createStream("Concert Nirvana", "Concert au bikini, lundi 9 décembre", true,
					true, null, null, null, false, new Date().getTime(), new Date().getTime(), 10.255, 15.378, null);
			System.out.println(myFirstStream.getTitle());
		} catch (GlueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
