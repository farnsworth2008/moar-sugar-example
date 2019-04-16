package moar.sugar.demo.example;

import static java.lang.String.format;
import static moar.ansi.Ansi.green;
import static moar.awake.InterfaceUtil.use;
import static moar.sugar.Sugar.require;
import static moar.sugar.Sugar.toUtilDate;
import java.io.PrintStream;
import moar.sugar.example.schema.PetRow;

public class DatabaseExample
    extends
    DatabaseBaseExample {

  public DatabaseExample(PrintStream out) {
    super(out);
  }

  @Override
  public void demo() {

    /* Works with standard javax.sql.DataSource */
    var ds = getDataSource();

    /* Simple way to executeSql with connection open and close handled
     * automatically */
    require(() -> use(ds).executeSql("delete from pet"));

    /* Fluent syntax style without the need for a repository of each type. */
    var pet1 = use(PetRow.class).of(ds).upsert(row -> {
      row.setName("Donut");
      row.setOwner("Mark");
      row.setSex("F");
      row.setSpecies("Dog");
      row.setBirth(toUtilDate(2015, 3, 15));
    });
    out.println(format("  %s: %s, %s", green("Upsert #1"), pet1.getId(), pet1.getName()));

    /* Repository of each type can also be passed around. */
    var repo = use(PetRow.class).of(ds);
    PetRow pet2 = repo.define();
    pet2.setName("Tig");
    pet2.setOwner("None");
    pet2.setSex("M");
    pet2.setSpecies("Dog");
    pet2.setBirth(toUtilDate(2018, 4, 22));
    repo.upsert(pet2);
    out.println(format("  %s: %s, %s", green("Upsert #2"), pet2.getId(), pet2.getName()));
    var pet2Id = pet2.getId();

    /* Find based on ID is very simple */
    var foundPet = repo.id(pet2Id).find();
    out.println(format("  %s: %s, %s", green("Found"), foundPet.getName(), foundPet.getOwner()));

    /* Update is simply provided by the repo */
    foundPet.setOwner("Mark");
    repo.update(foundPet);

    // Find rows using an example row to search
    foundPet = repo.where(row -> {
      row.setName("Donut");
      row.setSpecies("Dog");
    }).find();
    out.println(format("  %s: %s, %s", green("Found"), foundPet.getName(), foundPet.getOwner()));

    // Delete
    repo.delete(foundPet);

    // Upsert multiple rows.
    use(ds).upsert(PetRow.class, row -> {
      row.setName("Twyla");
      row.setOwner("Kendra");
      row.setSex("F");
      row.setSpecies("Cat");
      row.setBirth(toUtilDate(2012, 6, 5));
    }, row -> {
      row.setName("Jasper");
      row.setOwner("Kendra");
      row.setSex("M");
      row.setSpecies("Cat");
      row.setBirth(toUtilDate(2012, 9, 1));
    }, row -> {
      row.setName("Woody");
      row.setOwner("Kendra");
      row.setSex("M");
      row.setSpecies("Dog");
      row.setBirth(toUtilDate(2016, 3, 8));
    });

    // Find where species is cat
    var petList = repo.where(row -> row.setSpecies("cat")).list();
    for (PetRow petItem : petList) {
      out.println(format("  %s: %s, %s", green("Cat"), petItem.getName(), petItem.getOwner()));
    }

    // Find where direct sql
    petList = repo.list("where species !=?", "cat");
    for (PetRow petItem : petList) {
      out.println(format("  %s: %s, %s", green(petItem.getSpecies()), petItem.getName(), petItem.getOwner()));
    }

  }

}
