package moar.sugar.example;

import static moar.sugar.Sugar.require;
import static moar.sugar.thread.MoarThreadSugar.$;
import static moar.sugar.thread.MoarThreadSugar.$$;
import java.io.PrintStream;

class TimeMethodsExample
    extends
    BaseExample {

  TimeMethodsExample(PrintStream out) {
    super(out);
  }

  @Override
  public void run() {
    out.println("Example: Time methods");
    var report = $$(() -> {
      require(() -> {
        $("TrackOne", () -> methodOne("hello 1.1"));
        $("TrackOne", () -> methodOne("hello 1.2"));
        $("TrackTwo", () -> methodTwo("hello 2.1"));
        $("TrackTwo", () -> methodTwo("hello 2.1"));
      });
    });

    var time1 = report.getTracker("TrackOne");
    var time2 = report.getTracker("TrackTwo");
    out.println("  methodOne min of " + time1.getMin() + " ms.");
    out.println("  methodTwo min of " + time2.getMin() + " ms.");
    out.println("  methodOne max of " + time1.getMax() + " ms.");
    out.println("  methodTwo max of " + time2.getMax() + " ms.");
    out.println("  methods took total of " + report.getTime() + " ms.");
    out.println();
  }

}
