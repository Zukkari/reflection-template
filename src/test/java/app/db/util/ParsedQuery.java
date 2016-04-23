package app.db.util;

import java.util.List;

public class ParsedQuery {

  private final String table;
  private final List<String> columns;
  private final List<Object> params;

  public ParsedQuery(String table, List<String> columns, List<Object> params) {
    this.table = table;
    this.columns = columns;
    this.params = params;
    if (columns.size() != params.size())
      throw new IllegalStateException(String.format(
          "columns.size() != params.size(); columns=%s, params=%s",
          columns, params));
  }

  public String getTable() {
    return table;
  }

  public List<String> getColumns() {
    return columns;
  }

  public List<Object> getParams() {
    return params;
  }
}
