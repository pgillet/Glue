package com.glue.feed.html.demo;

import java.util.Calendar;
import java.util.Date;

import de.unihd.dbs.heideltime.standalone.DocumentType;
import de.unihd.dbs.heideltime.standalone.HeidelTimeStandalone;
import de.unihd.dbs.heideltime.standalone.OutputType;
import de.unihd.dbs.heideltime.standalone.POSTagger;
import de.unihd.dbs.heideltime.standalone.exceptions.DocumentCreationTimeMissingException;
import de.unihd.dbs.uima.annotator.heideltime.resources.Language;

public class HeidelDemo {

    public static void main(String[] args)
	    throws DocumentCreationTimeMissingException {
	HeidelTimeStandalone heidel = new HeidelTimeStandalone(Language.FRENCH,
		DocumentType.NEWS, OutputType.TIMEML, "/tmp/config.props",
		POSTagger.TREETAGGER, true);

	Calendar cal = Calendar.getInstance();
	Date documentCreationTime = cal.getTime();

	String document = "Le jeudi 24 décembre";
	// "Du lundi 21 décembre au vendredi 25 décembre";

	String result = heidel.process(document, documentCreationTime);

	System.out.println(result);
    }

}
