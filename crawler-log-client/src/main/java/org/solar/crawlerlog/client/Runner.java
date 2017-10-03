package org.solar.crawlerlog.client;

public class Runner {

    private Runnable task;

    private Runner(Runnable task) {
        this.task = task;
    }

    public static Runner newTask(Runnable task) {
        return new Runner(task);
    }

    public Runner sleepForSeconds(int seconds) {
        task = Runnables.sleepForSeconds(seconds , task);
        return this;
    }

    public Runner continueOn(Class<? extends RuntimeException> exClass) {
        task = Runnables.continueOn(exClass , task);
        return this;
    }

    public Runner repeatIndefinetely() {
        task = Runnables.repeatIndefinetely(task);
        return this;
    }

    public Runner logException() {
        task = Runnables.logException(task);
        return this;
    }

    public void go() {
        task.run();
        System.out.println("Runner finished");
    }
}
