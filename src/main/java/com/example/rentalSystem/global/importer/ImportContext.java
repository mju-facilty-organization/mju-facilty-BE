package com.example.rentalSystem.global.importer;

import java.time.LocalDate;

public class ImportContext {

  private final LocalDate validStartDate;
  private final LocalDate validEndDate;

  private ImportContext(Builder b) {
    this.validStartDate = b.validStartDate;
    this.validEndDate = b.validEndDate;
  }

  public LocalDate getValidStartDate() {
    return validStartDate;
  }

  public LocalDate getValidEndDate() {
    return validEndDate;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {

    private LocalDate validStartDate;
    private LocalDate validEndDate;

    public Builder validStartDate(LocalDate d) {
      this.validStartDate = d;
      return this;
    }

    public Builder validEndDate(LocalDate d) {
      this.validEndDate = d;
      return this;
    }

    public ImportContext build() {
      return new ImportContext(this);
    }
  }
}