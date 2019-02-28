package moar.sugar.demo.example;

import static moar.sugar.Sugar.swallow;
import java.io.PrintStream;

public class SwallowExample
    extends
    BaseExample {

  public SwallowExample(PrintStream out) {
    super(out);
  }

  @Override
  public void demo() {
    out.println("Example: Swallow Exception");
    String x = swallow(() -> methodWithException("test"));
    out.println("  x:" + x);
  }

}
