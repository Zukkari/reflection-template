package app.db;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
    String tableName = tableName(entity);
    List<String> columnNames = new ArrayList<>();
    List<String> placeholders = new ArrayList<>();
    List<Object> parameterValues = new ArrayList<>();
    for (Field field : entity.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      columnNames.add(columnName(field));
      placeholders.add("?");
      parameterValues.add(field.get(entity));
    }

    String sql = String.format(
        "INSERT INTO %s (%s) VALUES (%s);",
        tableName,
        String.join(", ", columnNames),
        String.join(", ", placeholders));
    return new Query(sql, parameterValues);
  }

  private String tableName(Object entity) {
    Table table = entity.getClass().getAnnotation(Table.class);
    if (table != null)
      return table.value();
    return entity.getClass().getSimpleName();
  }

  private String columnName(Field field) {
    Column column = field.getAnnotation(Column.class);
    if (column != null)
      return column.value();
    return field.getName();
  }
}
