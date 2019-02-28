package moar.sugar.demo.example;

import static java.lang.Math.random;
import static java.lang.Thread.currentThread;
import static moar.sugar.Sugar.require;
import static moar.sugar.Sugar.retryable;
import java.io.PrintStream;
import moar.sugar.Ansi;

public abstract class BaseExample
    implements
    Runnable {
  final PrintStream out;

  BaseExample(PrintStream out) {
    this.out = out;
  }

  abstract void demo();

  String methodOne(String message) {
    randomSleep();
    var threadName = currentThread().getName();
    out.println("  methodOne: " + message + " " + threadName);
    randomSleep();
    return "Hello from method One with " + message;
  }

  String methodTwo(String message) {
    randomSleep();
    out.println("  methodTwo: " + message + " " + currentThread().getName());
    randomSleep();
    return "Hello from method Two with " + message;
  }

  String methodWithException(String message) throws Exception {
    randomSleep();
    out.println("  methodThatThrows for " + message);
    throw new Exception("Demo Ex for " + message);
  }

  String methodWithRetryableException(String message) throws Exception {
    return retryable(() -> {
      methodWithRuntimeException(message);
      return null;
    });
  }

  String methodWithRuntimeException(String message) {
    randomSleep();
    out.println("  methodThatThrows for " + message);
    throw new RuntimeException("Demo Ex for " + message);
  }

  void randomSleep() {
    var mills = (long) (1000 * random() + 10);
    require(() -> Thread.sleep(mills));
  }

  @Override
  public final void run() {
    out.println(Ansi.blue(this.getClass().getSimpleName()));
    demo();
    out.println("\n ");
  }

}