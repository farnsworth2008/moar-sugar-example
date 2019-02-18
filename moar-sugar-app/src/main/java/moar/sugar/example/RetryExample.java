package moar.sugar.example;

import static moar.sugar.Sugar.retryable;
import java.io.PrintStream;

public class RetryExample
    extends
    BaseExample {

  public RetryExample(PrintStream out) {
    super(out);
  }

  void exampleRetryStandard() {
    out.println("  RETRY EXCEPTION WITH STANDARD JAVA");
    try {
      int tries = 3;
      Exception lastException = null;
      while (tries-- > 0) {
        try {
          methodWithRuntimeException("three");
        } catch (Exception e) {
          lastException = e;
          Thread.sleep(10);
        }
      }
      throw lastException;
    } catch (Exception e) {
      out.println("  We tried three times.");
      out.println("  e: " + e.getMessage());
    }
    out.println();
  }

  void exampleRetrySugar() {
    out.println("  RETRY EXCEPTION WITH MOAR SUGAR");
    try {
      retryable(3, 10, () -> methodWithRetryableException("three"));
    } catch (Exception e) {
      out.println("  We tried three times.");
      out.println("  e: " + e.getMessage());
    }
    out.println();
  }

  @Override
  public void run() {
    out.println("Example: Retry methods");
    exampleRetryStandard();
    exampleRetrySugar();
  }

}
