package app.db;

import java.util.List;

public class Query {

  private final String query;
  private final List<Object> parameters;

  public Query(String query, List<Object> parameters) {
    this.query = query;
    this.parameters = parameters;
  }

  public String getQuery() {
    return query;
  }

  public List<Object> getParameters() {
    return parameters;
  }

  @Override
  public String toString() {
    return String.format("Query{query='%s', parameters=%s}", query, parameters);
  }
}
