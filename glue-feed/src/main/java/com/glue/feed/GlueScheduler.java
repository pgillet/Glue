package com.glue.feed;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

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

public class GlueScheduler {

	static final Logger LOG = LoggerFactory.getLogger(GlueScheduler.class);

	public static void main(String[] args) {
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

}
