package com.glue.feed.opennlp.dates;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.feed.opennlp.Constants;

public class DateAnalyzer {

    static final Logger LOG = LoggerFactory.getLogger(DateAnalyzer.class);

    private static Tokenizer tokenizer;
    private static NameFinderME nameFinder;

    private DateAnalyzer() {
	initNameModel();
	initTokenModel();
    }

    // DateModel loader
    private void initTokenModel() {

	InputStream is = null;
	TokenizerModel tmodel = null;
	try {

	    is = new FileInputStream(new File(getClass().getResource(
		    Constants.TOKEN_FILE).toURI()));
	    tmodel = new TokenizerModel(is);
	    is.close();
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (InvalidFormatException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (URISyntaxException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    try {
		is.close();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	tokenizer = new TokenizerME(tmodel);
    }

    // TokenModel loader
    private void initNameModel() {
	InputStream is = null;
	TokenNameFinderModel model = null;
	try {
	    is = new FileInputStream(new File(getClass().getResource(
		    Constants.DATE_MODEL_FILE).toURI()));
	    model = new TokenNameFinderModel(is);
	    is.close();
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (InvalidFormatException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (URISyntaxException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    try {
		is.close();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	nameFinder = new NameFinderME(model);
    }

    // See
    // http://thecodersbreakfast.net/index.php?post/2008/02/25/26-de-la-bonne-implementation-du-singleton-en-java
    private static class EventDateAnalyzerHolder {
	private final static DateAnalyzer instance = new DateAnalyzer();
    }

    // Get singleton instance
    public static DateAnalyzer getInstance() {
	return EventDateAnalyzerHolder.instance;
    }

    public List<EventDate> analyze(String date) {
	List<EventDate> result = new ArrayList<EventDate>();

	LOG.debug("Before filtering = " + date);
	String filter = filter(date);

	LOG.debug("After filtering = " + filter);
	String[] tokens = tokenizer.tokenize(filter);

	Span[] spans = nameFinder.find(tokens);

	traceSpans(spans);
	if (spans.length != 0) {
	    EventDateManager manager = new EventDateManager(
		    Arrays.asList(tokens), Arrays.asList(spans));
	    result = manager.process();

	}
	return result;
    }

    // Apply some basic filtering rules
    private static String filter(String date) {

	String result = date.trim();
	if (result.startsWith("-")) {
	    result = result.replaceFirst("-", "");
	}
	return result.replace("à", "a").replace("&", " et ").replace(",", "")
		.toLowerCase();
    }

    private static void traceSpans(Span[] spans) {
	for (Span s : spans) {
	    LOG.debug(s.toString());
	}
	LOG.debug("\n");

    }

}
