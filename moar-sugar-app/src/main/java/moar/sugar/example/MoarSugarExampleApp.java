package moar.sugar.example;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import moar.driver.Driver;

public class MoarSugarExampleApp {
  public static void main(String[] args) {
    Driver.init();
    PrintStream out = System.out;
    List<BaseExample> examples = new ArrayList<>();
    examples.add(new SwallowExample(out));
    examples.add(new AsyncExample(out));
    examples.add(new RetryExample(out));
    examples.add(new DatabaseExample(out));
    examples.add(new RuntimeExceptionExample(out));
    examples.add(new TimeMethodsExample(out));
    for (BaseExample example : examples) {
      example.run();
    }
  }
}
