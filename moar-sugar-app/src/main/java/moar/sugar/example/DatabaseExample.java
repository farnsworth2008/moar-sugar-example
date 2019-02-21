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
    var ds = getDataSource();

    out.println("Example: DB");

    // simple way to executeSql against a DataSource
    wake(ds).executeSql("delete from pet");

    // style 1: Upsert using a fully fluent style
    var pet1 = wake(PetRow.class).of(ds).upsert(row -> {
      row.setName("Donut");
      row.setOwner("Mark");
      row.setSex("F");
      row.setSpecies("Dog");
      row.setBirth(toUtilDate(2015, 3, 15));
    });
    out.println("  upsert pet #1: " + pet1.getId() + ", " + pet1.getName());

    // style 2: Upsert using style where we hold the repository reference.
    var repo = wake(PetRow.class).of(ds);
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
    var foundPet = repo.id(pet2Id).find();
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
    var petList = repo.list("where species=?", "Cat");
    for (PetRow petItem : petList) {
      out.println("  found: " + petItem.getName() + ", " + petItem.getOwner());
    }
    out.println();
  }

}
