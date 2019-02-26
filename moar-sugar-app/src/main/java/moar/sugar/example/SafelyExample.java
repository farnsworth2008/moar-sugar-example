package moar.sugar.example;

import static moar.sugar.Sugar.has;
import static moar.sugar.Sugar.safely;
import java.io.PrintStream;

class SafelyExample
    extends
    BaseExample {

  SafelyExample(PrintStream out) {
    super(out);
  }

  @Override
  public void run() {
    out.println("Example: Safely invoke a method that may throw");
    var result = safely(() -> methodWithException("two"));
    if (has(result.thrown())) {
      out.println("  we got: " + result.thrown().getMessage());
    } else {
      out.println("  we got: " + result.get());
    }
    out.println();
  }

}
