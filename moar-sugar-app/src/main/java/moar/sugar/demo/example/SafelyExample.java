package moar.sugar.demo.example;

import static moar.sugar.Sugar.has;
import static moar.sugar.Sugar.safely;
import java.io.PrintStream;

public class SafelyExample
    extends
    BaseExample {

  public SafelyExample(PrintStream out) {
    super(out);
  }

  @Override
  public void demo() {
    out.println("Example: Safely invoke a method that may throw");
    var result = safely(() -> methodWithException("two"));
    if (has(result.thrown())) {
      out.println("  we got: " + result.thrown().getMessage());
    } else {
      out.println("  we got: " + result.get());
    }
  }

}
