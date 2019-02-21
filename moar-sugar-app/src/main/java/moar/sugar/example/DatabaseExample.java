package moar.sugar.example;

import static moar.awake.Waker.wake;
import static moar.sugar.Sugar.toUtilDate;
import java.io.PrintStream;
import moar.sugar.example.schema.PetRow;

class DatabaseExample
    extends
    DatabaseBaseExample {

  DatabaseExample(PrintStream out) {
    super(out);
  }

  @Override
  public void run() {

    /* Works with standard javax.sql.DataSource */
    var ds = getDataSource();

    out.println("Example: DB");

    /* Simple way to executeSql with connection open and close handled
     * automatically */
    wake(ds).executeSql("delete from pet");

    /* Fluent syntax style without the need for a repository of each type. */
    var pet1 = wake(PetRow.class).of(ds).upsert(row -> {
      row.setName("Donut");
      row.setOwner("Mark");
      row.setSex("F");
      row.setSpecies("Dog");
      row.setBirth(toUtilDate(2015, 3, 15));
    });
    out.println("  upsert pet #1: " + pet1.getId() + ", " + pet1.getName());

    /* Repository of each type can also be passed around. */
    var repo = wake(PetRow.class).of(ds);
    PetRow pet2 = repo.define();
    pet2.setName("Tig");
    pet2.setOwner("None");
    pet2.setSex("M");
    pet2.setSpecies("Dog");
    pet2.setBirth(toUtilDate(2018, 4, 22));
    repo.upsert(pet2);
    out.println("  upsert pet #2: " + pet2.getId() + ", " + pet2.getName());
    var pet2Id = pet2.getId();

    /* Find based on ID is very simple and update */
    var foundPet = repo.id(pet2Id).find();
    out.println("  found: " + foundPet.getName());

    /* Update is simply provided by the repo */
    foundPet.setOwner("Mark");
    repo.update(foundPet);

    // Find rows using an example row to search
    foundPet = repo.key(where -> {
      where.setName("Tig");
      where.setSpecies("Dog");
    }).find();
    out.println("  found: " + foundPet.getName() + ", " + foundPet.getOwner());

    // Delete
    repo.delete(foundPet);

    // Upsert multiple rows.
    wake(ds).upsert(PetRow.class, row -> {
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
    });

    // Find with a where clause
    var petList = repo.list("where species=?", "Cat");
    for (PetRow petItem : petList) {
      out.println("  found: " + petItem.getName() + ", " + petItem.getOwner());
    }
    out.println();
  }

}
