package moar.sugar.example;

import static moar.sugar.Sugar.require;
import java.io.PrintStream;

class RuntimeExceptionExample
    extends
    BaseExample {

  RuntimeExceptionExample(PrintStream out) {
    super(out);
  }

  @Override
  public void run() {
    out.println("Example: Convert to RuntimeException");
    try {
      require(() -> methodWithException("two"));
    } catch (RuntimeException e) {
      out.println("  e: " + e.getCause().getMessage());
    }
    out.println();
  }

}
