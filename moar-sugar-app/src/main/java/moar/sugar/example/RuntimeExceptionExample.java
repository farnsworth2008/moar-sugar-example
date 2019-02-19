package moar.sugar.example;

import static moar.sugar.Sugar.require;
import java.io.PrintStream;

class RuntimeExceptionExample
    extends
    BaseExample {

  RuntimeExceptionExample(PrintStream out) {
    super(out);
  }

  void exampleConvertToRuntimeStandard() {
    out.println("  RUNTIME EXCEPTION WITH STANDARD JAVA");
    try {

      try {
        methodWithRetryableException("two");
      } catch (Exception e) {
        throw new RuntimeException(e);
      }

    } catch (RuntimeException e) {
      out.println("  e: " + e.getCause().getMessage());
    }
    out.println();
  }

  void exampleConvertToRuntimeSugar() {
    out.println("  RUNTIME EXCEPTION WITH MOAR SUGAR");
    try {
      require(() -> methodWithRetryableException("two"));
    } catch (RuntimeException e) {
      out.println("  e: " + e.getCause().getMessage());
    }
    out.println();
  }

  @Override
  public void run() {
    out.println("Example: Convert to RuntimeException");
    exampleConvertToRuntimeStandard();
    exampleConvertToRuntimeSugar();
  }

}
