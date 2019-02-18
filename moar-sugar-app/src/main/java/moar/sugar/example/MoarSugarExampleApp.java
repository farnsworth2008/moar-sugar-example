package moar.sugar.example;

import static java.lang.String.format;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static moar.awake.Waker.wake;
import static moar.sugar.Sugar.require;
import static moar.sugar.Sugar.retryable;
import static moar.sugar.Sugar.safely;
import static moar.sugar.Sugar.swallow;
import static moar.sugar.Sugar.toUtilDate;
import static moar.sugar.thread.MoarThreadSugar.$;
import static moar.sugar.thread.MoarThreadSugar.$$;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import moar.awake.WokenWithSession;
import moar.driver.Driver;
import moar.sugar.MoarJson;
import moar.sugar.RetryableException;
import moar.sugar.SafeResult;
import moar.sugar.thread.MoarThreadReport;
import moar.sugar.thread.MoarThreadSugar.AsyncService;
import moar.sugar.thread.MoarThreadTracker;

public class MoarSugarExampleApp {
  public static void main(String[] args) {
    Driver.init();
    MoarSugarExampleApp app = new MoarSugarExampleApp();
    app.doMain(args);
  }

  PrintStream out = System.out;

  private final MoarJson moarJson = MoarJson.getMoarJson();

  private final Map<String, String> config;

  public MoarSugarExampleApp() {
    String configFilename = "moar_example_app_config.json";
    if (new File(configFilename).exists() == false) {
      configFilename = "../" + configFilename;
    }

    config = moarJson.fromJsonFile(configFilename, HashMap.class);
  }

  public void doMain(String[] args) {
    DataSource ds = getDataSource();
    exampleSwallow();
    exampleConvertToRuntime();
    exampleRetry();
    exampleTimeMethods();
    exampleAync();
    exampleDb(ds);
  }

  void exampleAsyncStandard() {
    out.println("  ASYNC METHODS WITH STANDARD JAVA");
    ExecutorService service = newFixedThreadPool(4);
    try {
      Vector<Future<String>> futures = new Vector<>();

      for (int i = 0; i < 3; i++) {
        String message = "async " + i;
        futures.add(service.submit(() -> methodOne(message)));
      }

      for (int i = 0; i < 3; i++) {
        String message = "async " + i;
        futures.add(service.submit(() -> methodOne(message)));
      }

      for (int i = 0; i < 3; i++) {
        String message = "async " + i;
        futures.add(service.submit(() -> methodWithRetryableException(message)));
      }

      out.println("  async work started");
      List<Exception> exceptions = new ArrayList<>();
      for (Future<String> future : futures) {
        try {
          future.get();
        } catch (Exception e) {
          exceptions.add(e);
        }
      }
      out.println("  async work complete");

      int i = 0;
      for (Future<String> future : futures) {
        String displayValue;
        try {
          displayValue = future.get();
        } catch (Exception e) {
          displayValue = e.getMessage();
        }
        out.println(format("  futures[%d]: %s", ++i, displayValue));
      }

    } finally {
      service.shutdown();
    }
    out.println();
  }

  void exampleAsyncSugar() {
    out.println("  ASYNC METHODS WITH MOAR SUGAR");
    AsyncService service = $(4);
    try {
      Vector<Future<String>> futures = $(String.class);

      for (int i = 0; i < 3; i++) {
        String message = "async " + i;
        $(service, futures, () -> methodOne(message));
      }

      for (int i = 0; i < 3; i++) {
        String message = "async " + i;
        $(service, futures, () -> methodTwo(message));
      }

      for (int i = 0; i < 3; i++) {
        String message = "async " + i;
        $(service, futures, () -> methodWithRetryableException(message));
      }

      out.println("  async work started");
      swallow(() -> $(futures));
      out.println("  async work complete");

      int i = 0;
      for (Future<String> future : futures) {
        SafeResult<String> result = safely(() -> future.get());
        String displayValue = result.thrown() == null ? result.get() : result.thrown().getMessage();
        out.println(format("  futures[%d]: %s", ++i, displayValue));
      }

    } finally {
      service.shutdown();
    }
    out.println();
  }

  void exampleAync() {
    out.println("Example: Async Execution");
    exampleAsyncStandard();
    exampleAsyncSugar();
  }

  void exampleConvertToRuntime() {
    out.println("Example: Convert to RuntimeException");
    exampleConvertToRuntimeStandard();
    exampleConvertToRuntimeSugar();
  }

  void exampleConvertToRuntimeStandard() {
    out.println("  RUNTIME EXCEPTION WITH STANDARD JAVA");
    try {

      try {
        methodWithRetryableException("two");
      } catch (Exception e) {
        throw new RuntimeException(e);
      }

    } catch (RuntimeException e) {
      out.println("  e: " + e.getCause().getMessage());
    }
    out.println();
  }

  void exampleConvertToRuntimeSugar() {
    out.println("  RUNTIME EXCEPTION WITH MOAR SUGAR");
    try {
      require(() -> methodWithRetryableException("two"));
    } catch (RuntimeException e) {
      out.println("  e: " + e.getCause().getMessage());
    }
    out.println();
  }

  void exampleDb(DataSource ds) {
    out.println("Example: DB");

    // simple way to executeSql against a DataSource
    wake(ds).executeSql("delete from pet");

    // style 1: Upsert using a fully fluent style
    PetRow pet1 = wake(PetRow.class).of(ds).upsert(row -> {
      row.setName("Donut");
      row.setOwner("Mark");
      row.setSex("F");
      row.setSpecies("Dog");
      row.setBirth(toUtilDate(2015, 3, 15));
    });
    out.println("  upsert pet #1: " + pet1.getId() + ", " + pet1.getName());

    // style 2: Upsert using style where we hold the repository reference.
    WokenWithSession<PetRow> repo = wake(PetRow.class).of(ds);
    PetRow pet2 = repo.define();
    pet2.setName("Tig");
    pet2.setOwner("None");
    pet2.setSex("M");
    pet2.setSpecies("Dog");
    pet2.setBirth(toUtilDate(2018, 4, 22));
    repo.upsert(pet2);
    out.println("  upsert pet #2: " + pet2.getId() + ", " + pet2.getName());
    Long pet2Id = pet2.getId();

    // Find with ID and update
    PetRow foundPet = repo.id(pet2Id).find();
    out.println("  found: " + foundPet.getName());
    foundPet.setOwner("Mark");
    repo.update(foundPet);

    // Find with a key
    foundPet = repo.key(r -> {
      r.setName("Tig");
      r.setSpecies("Dog");
    }).find();
    out.println("  found: " + foundPet.getName() + ", " + foundPet.getOwner());

    // Delete
    repo.delete(foundPet);

    // Upsert to add row for query
    repo.upsert(row -> {
      row.setName("Twyla");
      row.setOwner("Kendra");
      row.setSex("F");
      row.setSpecies("Cat");
      row.setBirth(toUtilDate(2012, 6, 5));
    });

    // Upsert to add row for query
    repo.upsert(row -> {
      row.setName("Jasper");
      row.setOwner("Kendra");
      row.setSex("M");
      row.setSpecies("Cat");
      row.setBirth(toUtilDate(2012, 9, 1));
    });

    // Find with a where clause
    List<PetRow> petList = repo.list("where species=?", "Cat");
    for (PetRow petItem : petList) {
      out.println("  found: " + petItem.getName() + ", " + petItem.getOwner());
    }
  }

  void exampleRetry() {
    out.println("Example: Retry methods");
    exampleRetryStandard();
    exampleRetrySugar();
  }

  void exampleRetryStandard() {
    out.println("  RETRY EXCEPTION WITH STANDARD JAVA");
    try {
      int tries = 3;
      Exception lastException = null;
      while (tries-- > 0) {
        try {
          methodWithRuntimeException("three");
        } catch (Exception e) {
          lastException = e;
          Thread.sleep(10);
        }
      }
      throw lastException;
    } catch (Exception e) {
      out.println("  We tried three times.");
      out.println("  e: " + e.getMessage());
    }
    out.println();
  }

  void exampleRetrySugar() {
    out.println("  RETRY EXCEPTION WITH MOAR SUGAR");
    try {
      retryable(3, 10, () -> methodWithRetryableException("three"));
    } catch (Exception e) {
      out.println("  We tried three times.");
      out.println("  e: " + e.getMessage());
    }
    out.println();
  }

  void exampleSwallow() {
    out.println("Example: Swallow Exception");
    exampleSwallowStandard();
    exampleSwallowSugar();
  }

  void exampleSwallowStandard() {
    out.println("  SWALLOW WITH STANDARD JAVA");
    String x;
    try {
      x = methodWithRetryableException("one");
    } catch (Exception e) {
      x = null;
    }
    out.println("  x:" + x);
    out.println();
  }

  void exampleSwallowSugar() {
    out.println("  SWALLOW WITH MOAR SUGAR");
    String x = swallow(() -> methodWithRetryableException("one"));
    out.println("  x:" + x);
    out.println();
  }

  void exampleTimeMethods() {
    out.println("Example 4, Time methods");
    exampleTimeMethodsStandard();
    exampleTimeMethodsSugar();
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

  private BasicDataSource getDataSource() {
    StringBuilder jdbcBuilder = new StringBuilder();
    jdbcBuilder.append("moar:moar.sugar.example:jdbc:mysql://");
    jdbcBuilder.append(config.get("host"));
    jdbcBuilder.append("/");
    jdbcBuilder.append(config.get("db"));
    jdbcBuilder.append("?useSSL=false&allowPublicKeyRetrieval=true");
    String jdbcUrl = jdbcBuilder.toString();

    BasicDataSource bds = new BasicDataSource();
    bds.setUrl(jdbcUrl);
    bds.setUsername(config.get("user"));
    bds.setPassword(config.get("password"));
    return bds;
  }

  private String methodOne(String message) {
    randomSleep();
    String threadName = Thread.currentThread().getName();
    out.println("  methodOne: " + message + " " + threadName);
    randomSleep();
    return "Hello from method One with " + message;
  }

  private String methodTwo(String message) {
    randomSleep();
    out.println("  methodTwo: " + message + " " + Thread.currentThread().getName());
    randomSleep();
    return "Hello from method Two with " + message;
  }

  private String methodWithRetryableException(String message) throws Exception {
    try {
      methodWithRuntimeException(message);
      return null;
    } catch (RuntimeException e) {
      throw new RetryableException(e);
    }
  }

  private void methodWithRuntimeException(String message) {
    randomSleep();
    out.println("  methodThatThrows for " + message);
    throw new RuntimeException("Demo Ex for " + message);
  }

  private void randomSleep() {
    long mills = (long) (1000 * Math.random() + 10);
    require(() -> Thread.sleep(mills));
  }
}
