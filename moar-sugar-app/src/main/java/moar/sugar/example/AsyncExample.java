package moar.sugar.example;

import static java.lang.String.format;
import static moar.sugar.Sugar.safely;
import static moar.sugar.Sugar.swallow;
import static moar.sugar.thread.MoarThreadSugar.$;
import java.io.PrintStream;

class AsyncExample
    extends
    BaseExample {

  AsyncExample(PrintStream out) {
    super(out);
  }

  @Override
  public void run() {
    out.println("Example: Async Execution");
    var service = $(4);
    try {
      var futures = $(String.class);

      for (var i = 0; i < 3; i++) {
        var message = "async " + i;
        $(service, futures, () -> methodOne(message));
      }

      for (var i = 0; i < 3; i++) {
        var message = "async " + i;
        $(service, futures, () -> methodTwo(message));
      }

      for (var i = 0; i < 3; i++) {
        var message = "async " + i;
        $(service, futures, () -> methodWithRetryableException(message));
      }

      out.println("  async work started");
      swallow(() -> $(futures));
      out.println("  async work complete");

      var i = 0;
      for (var future : futures) {
        var result = safely(() -> future.get());
        var displayValue = result.thrown() == null ? result.get() : result.thrown().getMessage();
        out.println(format("  futures[%d]: %s", ++i, displayValue));
      }

    } finally {
      service.shutdown();
    }
    out.println();
  }

}
