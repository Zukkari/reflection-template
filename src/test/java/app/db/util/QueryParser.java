package app.db.util;

import app.db.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QueryParser {

  public static ParsedQuery parse(Query query) {
    if (query == null)
      throw new AssertionError("Query must not be null");

    validateSyntax(query);

    StringTokenizer tok = new StringTokenizer(query.getQuery(), " ,();");
    expect(tok, "INSERT");
    expect(tok, "INTO");
    String table = findToken(tok, "table name");
    List<String> columns = new ArrayList<>();
    while (true) {
      String column = findToken(tok, "column name or VALUES");
      if (column.equalsIgnoreCase("VALUES"))
        break;
      columns.add(column);
    }
    for (int i = 0; i < columns.size(); i++) {
      expect(tok, "?");
    }

    return new ParsedQuery(table, columns, query.getParameters());
  }

  private static void validateSyntax(Query query) {
    Pattern insert = Pattern.compile(
        " *INSERT +INTO +([^ ]+) *\\(([^)]*)\\) *VALUES *\\(([^)]*)\\) *; *",
        Pattern.CASE_INSENSITIVE);

    Matcher matcher = insert.matcher(query.getQuery());
    if (!matcher.matches()) {
      throw new AssertionError("query doesn't match pattern 'INSERT INTO ... (...) VALUES (...);'");
    }

    List<String> columns = getItems(matcher.group(2));
    List<String> placeholders = getItems(matcher.group(3));
    if (columns.size() != placeholders.size()) {
      throw new AssertionError(String.format(
          "column count should equals placeholder count; found columns=%s, placeholders=%s",
          columns, placeholders));
    }
  }

  private static List<String> getItems(String columns) {
    return Stream.of(columns.split(",")).map(String::trim).collect(Collectors.toList());
  }

  private static String findToken(StringTokenizer tok, String description) {
    if (!tok.hasMoreTokens()) {
      throw new AssertionError(String.format("expected '%s' but found nothing", description));
    }
    return tok.nextToken();
  }

  private static void expect(StringTokenizer tok, String expected) {
    String next = findToken(tok, expected);
    if (!expected.equalsIgnoreCase(next)) {
      throw new AssertionError(String.format("expected '%s' but found '%s'", expected, next));
    }
  }
}
