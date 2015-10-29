package com.glue.feed.dvr;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.feed.error.StoreErrorListener;

@DisallowConcurrentExecution
public class EventReconciliationJob implements Job {

    static final Logger LOG = LoggerFactory
	    .getLogger(EventReconciliationJob.class);

    @Override
    public void execute(JobExecutionContext context)
	    throws JobExecutionException {

	try (EventServiceImpl service = new EventServiceImpl()) {

	    Date dateLimit = null;
	    if (context != null) {
		dateLimit = context.getPreviousFireTime(); // May be null
	    }
	    // Not a good idea   

	    service.addErrorListener(new StoreErrorListener());
	    service.execute(new DateTime().toDate());

	    service.flush();

	    // Update index
	    LOG.info("Deletes from the index the documents of withdrawn events");
	    EventIndexService svc = new EventIndexServiceImpl();
	    List<String> ids = svc.getWithdrawnEventIds();
	    svc.deleteById(ids);

	    LOG.info("Done");
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    throw new JobExecutionException(e);
	}
    }

    /**
     * @param args
     * @throws JobExecutionException
     */
    public static void main(String[] args) throws JobExecutionException {
	EventReconciliationJob job = new EventReconciliationJob();
	job.execute(null);
	System.exit(0);
    }

}
