package moar.sugar.example;

import moar.awake.WakeableRow;
import moar.sugar.example.MoarSugarExampleSchema.BirthColumn;
import moar.sugar.example.MoarSugarExampleSchema.DeathColumn;
import moar.sugar.example.MoarSugarExampleSchema.NameColumn;
import moar.sugar.example.MoarSugarExampleSchema.OwnerColumn;
import moar.sugar.example.MoarSugarExampleSchema.SexColumn;
import moar.sugar.example.MoarSugarExampleSchema.SpeciesColumn;

public interface PetRow
    extends
    WakeableRow.IdColumnAsAutoLong,
    NameColumn,
    OwnerColumn,
    SpeciesColumn,
    SexColumn,
    BirthColumn,
    DeathColumn {}