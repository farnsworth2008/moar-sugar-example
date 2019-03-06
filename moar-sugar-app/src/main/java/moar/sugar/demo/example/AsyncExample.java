package moar.sugar.demo.example;

import static java.lang.String.format;
import static moar.sugar.Sugar.require;
import static moar.sugar.thread.MoarThreadSugar.$;
import java.io.PrintStream;
import java.util.concurrent.Callable;

public class AsyncExample
    extends
    BaseExample {

  public AsyncExample(PrintStream out) {
    super(out);
  }

  @Override
  public void demo() {

    /* The 'require( () -> {} )' method makes sure that if anything in the block
     * fails an exception is thrown (with RuntimeException wrapping for checked
     * exceptions). It's a non magic version of the also very useful
     * Lombok's @SneakyThrows idea. */
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
          $(service, futures, () -> methodTwo(message2));
          $(service, futures, () -> methodWithException(message3));
        }

        /* $ shorthand to wait for all futures to *safely* complete */
        out.println("  async work started");
        var results = $(futures);
        out.println("  async work complete");

        /* easily to walk the result list without fear of exceptions */
        var i = 0;
        for (var result : results) {
          var futureThrew = result.thrown() == null;
          var displayValue = futureThrew ? result.get() : result.thrown().getMessage();
          out.println(format("  result %d: %s", ++i, displayValue));
        }

      }
    });
  }

}
