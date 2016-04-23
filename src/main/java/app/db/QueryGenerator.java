package app.db;

public class QueryGenerator {

  /**
   * Generate a parametric SQL insert statement based on the provided object.
   * The general form of the statement is "<code>INSERT INTO tablename (field1, .., fieldn) VALUES (?, ?);</code>".
   * The table name must be the unqualified (without package) class name of the provided object or
   * the value provided by the {@link Table} annotation if it's present (preferred). The Table annotations on the
   * entity's superclasses are ignored. The inserted column names must match the field names of the entity class or
   * the column names as specified by the {@link Column} annotations on the fields (preferred). The resulting
   * insert statement contains all non-static fields in the entity's class and its superclasses. The VALUES part
   * must contain a matching number of placeholders for the query parameters. The returned query parameters must
   * be in the same order as they appear in the statement.
   * <p>
   * Example query: <code>INSERT INTO Student (name, grade) VALUES (?, ?);</code>
   *
   * @return a valid parametric SQL insert statement with its parameters
   */
  public Query generateInsertStatement(Object entity) throws Exception {
    return null; // TODO implement
  }
}
