package moar.sugar.demo;

import java.util.ArrayList;
import moar.driver.Driver;
import moar.sugar.demo.example.AnsiExample;
import moar.sugar.demo.example.AsyncExample;
import moar.sugar.demo.example.BaseExample;
import moar.sugar.demo.example.DatabaseExample;
import moar.sugar.demo.example.ExecExample;
import moar.sugar.demo.example.RetryExample;
import moar.sugar.demo.example.SafelyExample;
import moar.sugar.demo.example.SwallowExample;
import moar.sugar.demo.example.TimeMethodsExample;

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
    examples.add(new AnsiExample(out));
    examples.add(new SwallowExample(out));
    examples.add(new AsyncExample(out));
    examples.add(new RetryExample(out));
    examples.add(new DatabaseExample(out));
    examples.add(new SafelyExample(out));
    examples.add(new TimeMethodsExample(out));
    examples.add(new ExecExample(out));
    String filter = args.length == 0 ? ".*" : args[0];
    for (var example : examples) {
      String simpleName = example.getClass().getSimpleName();
      if (simpleName.matches(filter)) {
        example.run();
      }
    }
  }
}
