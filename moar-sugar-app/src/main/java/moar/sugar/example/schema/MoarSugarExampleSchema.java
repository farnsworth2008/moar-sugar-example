package moar.sugar.example.schema;

import java.util.Date;

/**
 * Example Schema for Moar Sugar
 *
 * @author Mark Farnsworth
 */
@SuppressWarnings("javadoc")
public interface MoarSugarExampleSchema {
  public interface BirthColumn {
    Date getBirth();
    void setBirth(Date value);
  }

  public interface DeathColumn {
    Date getDeath();
    void setDeath(Date value);
  }

  public interface NameColumn {
    String getName();
    void setName(String value);
  }

  public interface OwnerColumn {
    String getOwner();
    void setOwner(String value);
  }

  public interface SexColumn {
    String getSex();
    void setSex(String value);
  }

  public interface SpeciesColumn {
    String getSpecies();
    void setSpecies(String value);
  }

}
