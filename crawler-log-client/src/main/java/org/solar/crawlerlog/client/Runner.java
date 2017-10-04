package org.solar.crawlerlog.client;

public class Runner {

    private Runnable task;

    private Runner(Runnable task) {
        this.task = task;
    }

    public static Runner newTask(Runnable task) {
        return new Runner(task);
    }

    public Runner executeAndSleepSeconds(int seconds) {
        task = RunnableDecorators.executeAndSleep(seconds , task);
        return this;
    }

    public Runner continueOn(Class<? extends RuntimeException> exClass) {
        task = RunnableDecorators.continueOn(exClass , task);
        return this;
    }

    public Runner repeatIndefinetely() {
        task = RunnableDecorators.repeatIndefinetely(task);
        return this;
    }

    public Runner catchAndLogException() {
        task = RunnableDecorators.catchAndLogException(task);
        return this;
    }

    public void go() {
        task.run();
        System.out.println("Runner finished");
    }
}
