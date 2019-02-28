package moar.sugar.demo.example;

import static moar.sugar.MoarJson.getMoarJson;
import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.dbcp.BasicDataSource;
import moar.sugar.MoarJson;

abstract class DatabaseBaseExample
    extends
    BaseExample {

  protected final Map<String, String> config;
  protected final MoarJson moarJson = getMoarJson();

  @SuppressWarnings("unchecked")
  DatabaseBaseExample(PrintStream out) {
    super(out);
    String configFilename = "moar_example_app_config.json";
    if (new File(configFilename).exists() == false) {
      configFilename = "../" + configFilename;
    }
    config = moarJson.fromJsonFile(configFilename, HashMap.class);
  }

  protected BasicDataSource getDataSource() {
    var builder = new StringBuilder();
    builder.append("moar:moar.sugar.example:jdbc:mysql://");
    builder.append(config.get("host"));
    builder.append("/");
    builder.append(config.get("db"));
    builder.append("?useSSL=false&allowPublicKeyRetrieval=true");
    var jdbcUrl = builder.toString();

    var bds = new BasicDataSource();
    bds.setUrl(jdbcUrl);
    bds.setUsername(config.get("user"));
    bds.setPassword(config.get("password"));
    return bds;
  }

}