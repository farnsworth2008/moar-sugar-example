package moar.sugar.demo.example;

import static java.lang.String.format;
import static java.lang.Thread.sleep;
import static moar.ansi.Ansi.blue;
import static moar.ansi.Ansi.clearLine;
import static moar.ansi.Ansi.green;
import static moar.ansi.Ansi.red;
import static moar.sugar.Sugar.swallow;
import java.io.PrintStream;
import moar.ansi.StatusLine;
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
    Sugar.require(() -> sleep(1000 * 3));
    clearLine(out);
    out.print("Rewritten line");

    StatusLine progress = new StatusLine(out, "Demo Progress");
    for (var i = 0; i < 100; i++) {
      swallow(() -> sleep(100));
      var completed = i;
      progress.set(() -> (float) completed / 100);
    }
    progress.clear();
  }

}
