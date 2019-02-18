package moar.sugar.example;

import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.dbcp.BasicDataSource;
import moar.sugar.MoarJson;

public abstract class DatabaseBaseExample
    extends
    BaseExample {

  protected final Map<String, String> config;
  protected final MoarJson moarJson = MoarJson.getMoarJson();

  public DatabaseBaseExample(PrintStream out) {
    super(out);
    String configFilename = "moar_example_app_config.json";
    if (new File(configFilename).exists() == false) {
      configFilename = "../" + configFilename;
    }
    config = moarJson.fromJsonFile(configFilename, HashMap.class);
  }

  protected BasicDataSource getDataSource() {
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

}