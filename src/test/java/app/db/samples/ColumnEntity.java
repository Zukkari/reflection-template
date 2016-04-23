package app.db.samples;

import app.db.Column;

public class ColumnEntity {

  @Column("annotatedField1")
  public String field1 = "field1value";

  public String field2 = "field2value";
}
