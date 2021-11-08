package com.fota.fotamargin;

import com.fota.fotamargin.job.JobRunnable;
import com.fota.fotamargin.job.common.JobRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ImportResource;

import javax.annotation.PostConstruct;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootApplication
@RefreshScope
@ImportResource(locations={"classpath:application-context.xml"})
public class FotaMarginApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(FotaMarginApplication.class);

	@PostConstruct
	public void runForcedLiquidation() {
		ThreadFactory nameFactory = new MarginThreadFactory();
		ThreadPoolExecutor singleThreadPool = new ThreadPoolExecutor(1, 1,
				0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(16), nameFactory);
		singleThreadPool.execute(new JobRunnable());
	}

	@PostConstruct
	public void runJob() {
		ThreadPoolExecutor jobThreadPool = new ThreadPoolExecutor(1, 1,
				0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(16), new JobThreadFactory());
		jobThreadPool.execute(new JobRunner());
	}

	static class MarginThreadFactory implements ThreadFactory {
		private static AtomicLong id = new AtomicLong(0);
		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, "margin-job-thread-pool-" + id.addAndGet(1));
		}
	}

	static class JobThreadFactory implements ThreadFactory {
		private static AtomicLong id = new AtomicLong(0);
		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, "elastic-job-thread-pool-" + id.addAndGet(1));
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(FotaMarginApplication.class, args);
	}
}
