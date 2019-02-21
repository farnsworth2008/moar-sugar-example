package moar.sugar.example;

import static java.lang.String.format;
import static moar.sugar.Sugar.require;
import static moar.sugar.thread.MoarThreadSugar.$;
import java.io.PrintStream;
import java.util.List;
import moar.sugar.SafeResult;

class AsyncExample
    extends
    BaseExample {

  AsyncExample(PrintStream out) {
    super(out);
  }

  @Override
  public void run() {

    out.println("Example: Async Execution");

    /* [require] shorthand to require everything in the block to complete
     * without exception. */
    require(() -> {

      /* $ shorthand for a service using 4 threads. */
      try (var service = $(4)) {

        /* $ shorthand for a future where we get a string. */
        var futures = $(String.class);

        /* $ shorthand to run lambda(s) async */
        for (var i = 0; i < 3; i++) {
          var message1 = format("async One [%d]", i);
          var message2 = format("async Two [%d]", i);
          var message3 = format("async Three [%d]", i);
          $(service, futures, () -> methodOne(message1));
          $(service, futures, () -> methodTwo(message2), () -> methodTwo(message3));
        }

        /* $ shorthand to wait for all futures to complete */
        out.println("  async work started");
        List<SafeResult<String>> results = $(futures);
        out.println("  async work complete");

        /* $ shorthand to get a safe result from a future */
        var i = 0;
        for (var result : results) {
          var futureThrew = result.thrown() == null;
          var displayValue = futureThrew ? result.get() : result.thrown().getMessage();
          out.println(format("  futures[%d]: %s", ++i, displayValue));
        }
      }
    });
    out.println();
  }

}
