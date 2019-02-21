package moar.sugar.example;

import static moar.sugar.Sugar.retry;
import java.io.PrintStream;

class RetryExample
    extends
    BaseExample {

  RetryExample(PrintStream out) {
    super(out);
  }

  @Override
  public void run() {
    out.println("Example: Retry methods");
    try {
      retry(3, 10, () -> methodWithRetryableException("three"));
    } catch (Exception e) {
      out.println("  We tried three times.");
      out.println("  e: " + e.getMessage());
    }
    out.println();
  }

}
