package moar.sugar.example;

import static moar.sugar.Sugar.require;
import static moar.sugar.thread.MoarThreadSugar.$;
import static moar.sugar.thread.MoarThreadSugar.$$;
import java.io.PrintStream;
import moar.sugar.thread.MoarThreadReport;
import moar.sugar.thread.MoarThreadTracker;

public class TimeMethodsExample
    extends
    BaseExample {

  public TimeMethodsExample(PrintStream out) {
    super(out);
  }

  void exampleTimeMethodsStandard() {
    out.println("  TIME METHODS WITH STANDARD JAVA");
    long startAll = System.currentTimeMillis();

    long start, time1, time1Min, time1Max, time2, time2Min, time2Max, timeAll;

    start = System.currentTimeMillis();
    methodOne("time 1.0");
    time1 = System.currentTimeMillis() - start;
    time1Min = time1Max = time1;

    start = System.currentTimeMillis();
    methodOne("time 1.1");
    time1 = System.currentTimeMillis() - start;
    time1Min = Math.min(time1, time1Min);
    time1Max = Math.max(time1, time1Max);

    start = System.currentTimeMillis();
    methodTwo("time 2.0");
    time2 = System.currentTimeMillis() - start;
    time2Min = time2Max = time2;

    start = System.currentTimeMillis();
    methodTwo("time 2.1");
    time2 = System.currentTimeMillis() - start;
    time2Min = Math.min(time2, time2Min);
    time2Max = Math.max(time2, time2Max);

    timeAll = System.currentTimeMillis() - startAll;

    out.println("  methodOne min of " + time1Min + " ms.");
    out.println("  methodTwo min of " + time2Min + " ms.");
    out.println("  methodOne max of " + time1Max + " ms.");
    out.println("  methodTwo max of " + time2Max + " ms.");
    out.println("  methods took total of " + timeAll + " ms.");
    out.println();
  }

  void exampleTimeMethodsSugar() {
    out.println("  TIME METHODS WITH MOAR SUGAR");
    MoarThreadReport report = $$(() -> {
      require(() -> $("One", () -> methodOne("hello 1.1")));
      require(() -> $("One", () -> methodOne("hello 1.2")));
      require(() -> $("Two", () -> methodTwo("hello 2.1")));
      require(() -> $("Two", () -> methodTwo("hello 2.1")));
    });

    MoarThreadTracker tracker1 = report.getTracker("One");
    out.println("  methodOne min of " + tracker1.getMin() + " ms.");
    MoarThreadTracker tracker2 = report.getTracker("Two");
    out.println("  methodTwo min of " + tracker2.getMin() + " ms.");
    out.println("  methodOne max of " + tracker1.getMax() + " ms.");
    out.println("  methodTwo max of " + tracker2.getMax() + " ms.");
    out.println("  methods took total of " + report.getTime() + " ms.");
    out.println();
  }

  @Override
  public void run() {
    out.println("Example: Time methods");
    exampleTimeMethodsStandard();
    exampleTimeMethodsSugar();
  }

}
