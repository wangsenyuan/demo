package main;

import main.common.Spec;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JobRunner {

    public void runJob(Spec spec) throws InterruptedException {
        JobManager jobManager = new JobManager();
        List<Job<Void, Void>> jobs = jobManager.createJobs(spec);
        System.out.println("total " + jobs.size() + " jobs need to process");
        CountDownLatch latch = new CountDownLatch(jobs.size());

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

        submitJobs(executor, latch, jobs);
        latch.await();
        executor.shutdownNow();
        System.out.println("all job done");
    }

    private void submitJobs(ExecutorService executor, CountDownLatch latch, List<Job<Void, Void>> jobs) {
        for (Job<Void, Void> job : jobs) {
            executor.submit(() -> {
                job.apply(null, (v) -> {
                    System.out.println("processing....");
                });
                System.out.println("one more job done");
                latch.countDown();
                System.out.println("latch count  " + latch.getCount());
            });
        }
    }
}
