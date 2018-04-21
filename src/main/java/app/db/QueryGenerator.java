package app.db;

public class QueryGenerator {

  /**
   * Generates a parametric SQL insert statement based on the provided object.
   * <p>
   * The table name must be the unqualified (without package) class name of
   * the provided object or the value provided by the {@link Table} annotation
   * if it's present (preferred).
   * <p>
   * The column names must match the field names of the entity class or
   * the names as specified by the {@link Column} annotations on the
   * fields (preferred).
   * <p>
   * The VALUES part must contain a matching number of placeholders for the
   * query parameters. The returned {@link Query} object must also contain the actual
   * values to be inserted, based on the object's fields' values.
   *
   * @return a {@link Query} object containing a valid parametric SQL insert
   * statement and its parameter values
   */
  public Query generateInsertStatement(Object entity) throws Exception {
    // TODO generate the SQL statement and find the parameter values
    // 1) use the entity's unqualified class name for table name
    // 2) find all fields in the entity object
    // 2.1) add a column name for each field
    // 2.2) add a value placeholder for each field
    // 2.3) store the field's value (query parameter value)
    // run the tests to see what's missing
    return null;
  }
}
