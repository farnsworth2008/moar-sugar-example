package moar.sugar.example.schema;

import moar.awake.WakeableRow;
import moar.sugar.example.schema.MoarSugarExampleSchema.BirthColumn;
import moar.sugar.example.schema.MoarSugarExampleSchema.DeathColumn;
import moar.sugar.example.schema.MoarSugarExampleSchema.NameColumn;
import moar.sugar.example.schema.MoarSugarExampleSchema.OwnerColumn;
import moar.sugar.example.schema.MoarSugarExampleSchema.SexColumn;
import moar.sugar.example.schema.MoarSugarExampleSchema.SpeciesColumn;

public interface PetRow
    extends
    WakeableRow.IdColumnAsAutoLong,
    NameColumn,
    OwnerColumn,
    SpeciesColumn,
    SexColumn,
    BirthColumn,
    DeathColumn {}