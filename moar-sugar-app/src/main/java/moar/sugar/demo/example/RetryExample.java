package moar.sugar.demo.example;

import static moar.sugar.Sugar.retry;
import java.io.PrintStream;

public class RetryExample
    extends
    BaseExample {

  public RetryExample(PrintStream out) {
    super(out);
  }

  @Override
  public void demo() {
    try {
      retry(3, 10, () -> methodWithRetryableException("three"));
    } catch (Exception e) {
      out.println("  We tried three times.");
      out.println("  e: " + e.getMessage());
    }
  }

}
