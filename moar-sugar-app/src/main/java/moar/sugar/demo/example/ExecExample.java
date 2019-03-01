package moar.sugar.demo.example;

import static moar.sugar.Sugar.require;
import java.io.IOException;
import java.io.PrintStream;
import moar.sugar.Sugar;

@SuppressWarnings("javadoc")
public class ExecExample
    extends
    BaseExample {

  public ExecExample(PrintStream out) {
    super(out);
  }

  @Override
  public void demo() {
    require(() -> doDemo());
  }

  private void doDemo() throws InterruptedException, IOException {
    var result = Sugar.exec("sed 's/quick/slow/g'", "The quick brown fox");
    out.println(result.getOutput());
    out.println("EXIT: " + result.getExitCode());
  }

}
