package com.glue.feed;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.feed.toulouse.open.data.biblio.LibraryAgendaJob;
import com.glue.feed.toulouse.open.data.so.SoToulouseAgendaJob;
import com.glue.feed.youtube.YoutubeMediaFeeder;

public class GlueFeed {

	static final Logger LOG = LoggerFactory.getLogger(GlueFeed.class);

	private static final String OPTION_PASSWORD = "password";
	private static final String OPTION_USER = "user";
	private static final String OPTION_DBNAME = "dbname";
	private static final String OPTION_PORT_NUMBER = "port";
	private static final String OPTION_DBSERVER = "dbserver";

	public void run() {
		Scheduler scheduler = null;
		try {
			SchedulerFactory sf = new StdSchedulerFactory();
			// sf.initialize(); // TODO: if we need to configure our scheduler
			// within the quartz.properties file

			scheduler = sf.getScheduler();
			scheduler.start();

			// Cron expression that fires every five minutes to test a new
			// trigger
			// "0 0/5 * * * ?"

			// AGENDA DES MANIFESTATIONS CULTURELLES SO TOULOUSE
			// Twice a week, every Tuesday and Friday at midnight
			JobDetail job = newJob(SoToulouseAgendaJob.class).withIdentity(
					"SoToulouseAgenda", "Toulouse").build();
			CronTrigger trigger = newTrigger()
					.withIdentity("SoToulouseAgendaTrigger", "Toulouse")
					.withSchedule(cronSchedule("0 0 0 ? * TUE,FRI")).build();
			scheduler.scheduleJob(job, trigger);

			// YouTube feed
			// Every day at 01:00 am
			job = newJob(YoutubeMediaFeeder.class).withIdentity("YouTube")
					.build();
			trigger = newTrigger().withIdentity("YouTubeTrigger")
					.withSchedule(cronSchedule("0 0 1 * * ?")).build();
			scheduler.scheduleJob(job, trigger);

			// AGENDA DES MANIFESTATIONS DE LA BIBLIOTHÈQUE DE TOULOUSE
			// Every day at 02:00 am
			job = newJob(LibraryAgendaJob.class).withIdentity("LibraryAgenda",
					"Toulouse").build();
			trigger = newTrigger()
					.withIdentity("LibraryAgendaTrigger", "Toulouse")
					.withSchedule(cronSchedule("0 0 2 * * ?")).build();
			scheduler.scheduleJob(job, trigger);

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

			DataSourceManager manager = DataSourceManager.getInstance();

			if (line.hasOption(OPTION_DBSERVER)) {
				manager.setServerName(line.getOptionValue(OPTION_DBSERVER));
			}
			if (line.hasOption(OPTION_PORT_NUMBER)) {
				manager.setPortNumber(Integer.parseInt(line
						.getOptionValue(OPTION_PORT_NUMBER)));
			}
			if (line.hasOption(OPTION_DBNAME)) {
				manager.setDatabaseName(line.getOptionValue(OPTION_DBNAME));
			}
			if (line.hasOption(OPTION_USER)) {
				manager.setUser(line.getOptionValue(OPTION_USER));
			}
			if (line.hasOption(OPTION_PASSWORD)) {
				manager.setPassword(line.getOptionValue(OPTION_PASSWORD));
			}

			// Attempts to establish a connection with the data source
			DataSource dataSource = manager.getDataSource();
			Connection conn = dataSource.getConnection();

			LOG.info("Successfully established a connection to "
					+ manager.getDatabaseName());

			feed.run();

		} catch (SQLException e) {
			System.err
					.println("Cannot establish a connection with the given parameters");
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("gluefeed", options);
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

		options.addOption("h", "help", false, "print this message");

		options.addOption(OptionBuilder.withArgName("host").hasArg()
				.withDescription("database server IP address or hostname")
				.create(OPTION_DBSERVER));

		options.addOption(OptionBuilder.withArgName("number").hasArg()
				.withDescription("database port number")
				.create(OPTION_PORT_NUMBER));

		options.addOption(OptionBuilder.withArgName("name").hasArg()
				.withDescription("database name").create(OPTION_DBNAME));

		options.addOption(OptionBuilder.withArgName("username").hasArg()
				.withDescription("database username").create(OPTION_USER));

		options.addOption(OptionBuilder.withArgName("passphrase").hasArg()
				.withDescription("database user password")
				.create(OPTION_PASSWORD));

		return options;
	}

}
