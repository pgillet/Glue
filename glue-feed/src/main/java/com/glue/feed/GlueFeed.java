package com.glue.feed;

import java.io.InputStream;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.persistence.GluePersistenceService;

public class GlueFeed {

    static final Logger LOG = LoggerFactory.getLogger(GlueFeed.class);
    
    private static String PROPERTIES_FILENAME = "/com/glue/feed/quartz.properties";

    private static final String OPTION_DUMMY = "dummy";

    public void run() {
	Scheduler scheduler = null;
	try {
	    StdSchedulerFactory sf = new StdSchedulerFactory();
	    InputStream in = GlueFeed.class
		    .getResourceAsStream(PROPERTIES_FILENAME);
	    sf.initialize(in);
	    IOUtils.closeQuietly(in);

	    LOG.info("Initialization Complete");

	    LOG.info("Not Scheduling any Jobs - relying on XML definitions");

	    LOG.info("Starting Scheduler");

	    scheduler = sf.getScheduler();
	    scheduler.start();

	    LOG.info("------- Started Scheduler -----------------");

	} catch (SchedulerException se) {
	    LOG.error(se.getMessage(), se);
	    if (scheduler != null) {
		try {
		    scheduler.shutdown(true);
		} catch (SchedulerException e) {
		    LOG.error("An error occured while shutting down the scheduler");
		    LOG.error(se.getMessage(), se);
		}
	    }
	}

    }

    public static void main(String[] args) {

	GlueFeed feed = new GlueFeed();
	Options options = feed.createOptions();

	// create the parser
	CommandLineParser parser = new BasicParser();
	try {
	    // parse the command line arguments
	    CommandLine line = parser.parse(options, args);

	    if (line.hasOption("h")) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("gluefeed", options);
		System.exit(0);
	    }

	    if (line.hasOption(OPTION_DUMMY)) {
		String val = line.getOptionValue(OPTION_DUMMY);
	    }

	    // Attempts to establish a connection with the data source
	    if (GluePersistenceService.getService().ping()) {
		LOG.info("Successfully established a connection to the database");
		feed.run();
	    } else {
		System.err
			.println("Cannot establish a connection to the database");
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("gluefeed", options);
		// System.exit(-1);
	    }

	} catch (NumberFormatException | ParseException exp) {
	    // oops, something went wrong
	    System.err.println("Parsing failed.  Reason: " + exp.getMessage());
	    HelpFormatter formatter = new HelpFormatter();
	    formatter.printHelp("gluefeed", options);
	}
    }

    private Options createOptions() {
	// create Options object
	Options options = new Options();

	options.addOption("h", "help", false, "Print this message");

	options.addOption(OptionBuilder.withArgName("dummy").hasArg()
		.withDescription("Some text here").create(OPTION_DUMMY));

	return options;
    }

}
