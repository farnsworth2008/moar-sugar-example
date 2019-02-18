package moar.sugar.example;

import static java.lang.String.format;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static moar.sugar.Sugar.safely;
import static moar.sugar.Sugar.swallow;
import static moar.sugar.thread.MoarThreadSugar.$;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import moar.sugar.SafeResult;
import moar.sugar.thread.MoarThreadSugar.AsyncService;

public class AsyncExample
    extends
    BaseExample {

  public AsyncExample(PrintStream out) {
    super(out);
  }

  void exampleAsyncStandard() {
    out.println("  ASYNC METHODS WITH STANDARD JAVA");
    ExecutorService service = newFixedThreadPool(4);
    try {
      Vector<Future<String>> futures = new Vector<>();

      for (int i = 0; i < 3; i++) {
        String message = "async " + i;
        futures.add(service.submit(() -> methodOne(message)));
      }

      for (int i = 0; i < 3; i++) {
        String message = "async " + i;
        futures.add(service.submit(() -> methodOne(message)));
      }

      for (int i = 0; i < 3; i++) {
        String message = "async " + i;
        futures.add(service.submit(() -> methodWithRetryableException(message)));
      }

      out.println("  async work started");
      List<Exception> exceptions = new ArrayList<>();
      for (Future<String> future : futures) {
        try {
          future.get();
        } catch (Exception e) {
          exceptions.add(e);
        }
      }
      out.println("  async work complete");

      int i = 0;
      for (Future<String> future : futures) {
        String displayValue;
        try {
          displayValue = future.get();
        } catch (Exception e) {
          displayValue = e.getMessage();
        }
        out.println(format("  futures[%d]: %s", ++i, displayValue));
      }

    } finally {
      service.shutdown();
    }
    out.println();
  }

  void exampleAsyncSugar() {
    out.println("  ASYNC METHODS WITH MOAR SUGAR");
    AsyncService service = $(4);
    try {
      Vector<Future<String>> futures = $(String.class);

      for (int i = 0; i < 3; i++) {
        String message = "async " + i;
        $(service, futures, () -> methodOne(message));
      }

      for (int i = 0; i < 3; i++) {
        String message = "async " + i;
        $(service, futures, () -> methodTwo(message));
      }

      for (int i = 0; i < 3; i++) {
        String message = "async " + i;
        $(service, futures, () -> methodWithRetryableException(message));
      }

      out.println("  async work started");
      swallow(() -> $(futures));
      out.println("  async work complete");

      int i = 0;
      for (Future<String> future : futures) {
        SafeResult<String> result = safely(() -> future.get());
        String displayValue = result.thrown() == null ? result.get() : result.thrown().getMessage();
        out.println(format("  futures[%d]: %s", ++i, displayValue));
      }

    } finally {
      service.shutdown();
    }
    out.println();
  }

  @Override
  public void run() {
    out.println("Example: Async Execution");
    exampleAsyncStandard();
    exampleAsyncSugar();
  }

}
