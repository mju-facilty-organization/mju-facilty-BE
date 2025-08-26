package com.example.rentalSystem.global.importer;

public class ImportPolicy {

  private final boolean overwriteWithNewData;
  private final boolean fillNullsFromNewData;

  public ImportPolicy() {
    this(true, true);
  }

  public ImportPolicy(boolean overwriteWithNewData, boolean fillNullsFromNewData) {
    this.overwriteWithNewData = overwriteWithNewData;
    this.fillNullsFromNewData = fillNullsFromNewData;
  }

  public boolean isOverwriteWithNewData() {
    return overwriteWithNewData;
  }

  public boolean isFillNullsFromNewData() {
    return fillNullsFromNewData;
  }
}