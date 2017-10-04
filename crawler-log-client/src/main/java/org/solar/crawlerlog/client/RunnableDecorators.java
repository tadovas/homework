package org.solar.crawlerlog.client;

public class RunnableDecorators {

  public static Runnable executeAndSleep(int seconds, Runnable task) {
    return () -> {
      try {
        task.run();
        System.out.println("Sleeping");
        Thread.sleep(seconds * 1000);
      } catch (InterruptedException e) {
        System.out.println("Bailing out!");
      }
    };
  }

  public static Runnable continueOn(Class<? extends RuntimeException> exClass, Runnable task) {
    return () -> {
      try {
        task.run();
      } catch (Exception e) {
        if (exClass.isAssignableFrom(e.getClass())) {
          System.out.println("Recoverable - swallowing. Error was: " + e.getMessage());
        } else {
          throw e;
        }
      }
    };
  }

  public static Runnable repeatIndefinetely(Runnable task) {
    return () -> {
      for (; ; ) {
        System.out.println("Task started");
        task.run();
        System.out.println("Task ended");
      }
    };
  }

  public static Runnable catchAndLogException(Runnable task) {
    return () -> {
      try {
        task.run();
      } catch (Exception e) {
        e.printStackTrace();
      }
    };
  }
}
