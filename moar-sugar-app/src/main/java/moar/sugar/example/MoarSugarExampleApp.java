package moar.sugar.example;

import java.util.ArrayList;
import moar.driver.Driver;

/**
 * Example Application for Moar Sugar
 *
 * @author Mark Farnsworth
 */
public class MoarSugarExampleApp {
  /**
   * Entry Point for launch.
   *
   * @param args
   *   Arguments for launch.
   */
  public static void main(String[] args) {
    Driver.init();
    var out = System.out;
    var examples = new ArrayList<BaseExample>();
    examples.add(new SwallowExample(out));
    examples.add(new AsyncExample(out));
    examples.add(new RetryExample(out));
    examples.add(new DatabaseExample(out));
    examples.add(new RuntimeExceptionExample(out));
    examples.add(new TimeMethodsExample(out));
    for (var example : examples) {
      example.run();
    }
  }
}
