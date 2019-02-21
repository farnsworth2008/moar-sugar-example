package moar.sugar.example;

import static moar.sugar.Sugar.swallow;
import java.io.PrintStream;

class SwallowExample
    extends
    BaseExample {

  SwallowExample(PrintStream out) {
    super(out);
  }

  @Override
  public void run() {
    out.println("Example: Swallow Exception");
    String x = swallow(() -> methodWithException("test"));
    out.println("  x:" + x);
    out.println();
  }

}
