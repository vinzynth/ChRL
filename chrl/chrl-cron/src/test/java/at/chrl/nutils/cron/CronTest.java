/**
 * @author ChRL
 * Feb 24, 2015 - 3:24:11 PM
 * chrl-cron
 * at.chrl.nutils.cron
 */
package at.chrl.nutils.cron;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

/**
 * @author ChRL
 *
 */
public class CronTest {

	@Test
	public void testName() throws Exception {
		final CountDownLatch cdl = new CountDownLatch(1);
		
		CronService.initSingleton(RunnableRunnerImpl.class);
		Date schedule = CronService.getInstance().schedule(() -> {
			System.out.println("run!");
			CronService.getInstance().shutdown();
			cdl.countDown();
		}, "0 * * * * ?");
		System.out.println(new Date());
		System.out.println(schedule);
		cdl.await();
	}
}
