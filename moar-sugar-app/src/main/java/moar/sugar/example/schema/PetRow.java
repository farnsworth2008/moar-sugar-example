package moar.sugar.example.schema;

import moar.awake.WakeableRow.IdColumnAsAutoLong;
import moar.sugar.example.schema.MoarSugarExampleSchema.BirthColumn;
import moar.sugar.example.schema.MoarSugarExampleSchema.DeathColumn;
import moar.sugar.example.schema.MoarSugarExampleSchema.NameColumn;
import moar.sugar.example.schema.MoarSugarExampleSchema.OwnerColumn;
import moar.sugar.example.schema.MoarSugarExampleSchema.SexColumn;
import moar.sugar.example.schema.MoarSugarExampleSchema.SpeciesColumn;

/**
 * Example definition for a ROW.
 */
public interface PetRow
    extends
    IdColumnAsAutoLong,
    NameColumn,
    OwnerColumn,
    SpeciesColumn,
    SexColumn,
    BirthColumn,
    DeathColumn {}