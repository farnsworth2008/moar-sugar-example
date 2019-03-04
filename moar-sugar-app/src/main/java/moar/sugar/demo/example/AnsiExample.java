package moar.sugar.demo.example;

import static java.lang.String.format;
import static java.lang.Thread.sleep;
import static moar.ansi.Ansi.blue;
import static moar.ansi.Ansi.green;
import static moar.ansi.Ansi.red;
import static moar.sugar.Sugar.require;
import static moar.sugar.Sugar.swallow;
import java.io.PrintStream;
import moar.ansi.StatusLine;

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

    StatusLine status = new StatusLine(out);
    status.setCount(100, "Demo Progress");
    for (var i = 0; i < 100; i++) {
      require(() -> status.completeOne(() -> {
        swallow(() -> sleep(100));
      }));
    }
    status.clear();
  }

}
