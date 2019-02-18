package moar.sugar.example;

import static moar.sugar.Sugar.swallow;
import java.io.PrintStream;

public class SwallowExample
    extends
    BaseExample {

  public SwallowExample(PrintStream out) {
    super(out);
  }

  void exampleSwallowStandard() {
    out.println("  SWALLOW WITH STANDARD JAVA");
    String x;
    try {
      x = methodWithRetryableException("one");
    } catch (Exception e) {
      x = null;
    }
    out.println("  x:" + x);
    out.println();
  }

  void exampleSwallowSugar() {
    out.println("  SWALLOW WITH MOAR SUGAR");
    String x = swallow(() -> methodWithRetryableException("one"));
    out.println("  x:" + x);
    out.println();
  }

  @Override
  public void run() {
    out.println("Example: Swallow Exception");
    exampleSwallowStandard();
    exampleSwallowSugar();
  }

}
