package moar.sugar.example;

import static moar.sugar.Sugar.require;
import java.io.PrintStream;
import moar.sugar.RetryableException;

public abstract class BaseExample
    implements
    Runnable {
  final PrintStream out;

  public BaseExample(PrintStream out) {
    this.out = out;
  }

  String methodOne(String message) {
    randomSleep();
    String threadName = Thread.currentThread().getName();
    out.println("  methodOne: " + message + " " + threadName);
    randomSleep();
    return "Hello from method One with " + message;
  }

  String methodTwo(String message) {
    randomSleep();
    out.println("  methodTwo: " + message + " " + Thread.currentThread().getName());
    randomSleep();
    return "Hello from method Two with " + message;
  }

  String methodWithRetryableException(String message) throws Exception {
    try {
      methodWithRuntimeException(message);
      return null;
    } catch (RuntimeException e) {
      throw new RetryableException(e);
    }
  }

  void methodWithRuntimeException(String message) {
    randomSleep();
    out.println("  methodThatThrows for " + message);
    throw new RuntimeException("Demo Ex for " + message);
  }

  void randomSleep() {
    long mills = (long) (1000 * Math.random() + 10);
    require(() -> Thread.sleep(mills));
  }

}