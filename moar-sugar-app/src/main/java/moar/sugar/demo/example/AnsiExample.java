package moar.sugar.demo.example;

import static java.lang.String.format;
import static moar.sugar.Ansi.blue;
import static moar.sugar.Ansi.clearLine;
import static moar.sugar.Ansi.green;
import static moar.sugar.Ansi.red;
import java.io.PrintStream;
import moar.sugar.Sugar;

@SuppressWarnings("javadoc")
public class AnsiExample
    extends
    BaseExample {

  public AnsiExample(PrintStream out) {
    super(out);
  }

  @Override
  public void demo() {
    out.println("If you run this with -Dmoar.ansi.enabled=true you will see colors");
    out.println();
    out.println();
    out.println(format("With %s, %s, %s, and more", red("red"), green("green"), blue("blue")));
    out.println("This line will clear and rewrite in 3 seconds.");
    Sugar.require(() -> Thread.sleep(1000 * 3));
    clearLine(out);
    out.print("Rewritten line");
  }

}
