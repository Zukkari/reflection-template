package app.db;

import app.db.samples.ColumnEntity;
import app.db.samples.Empty;
import app.db.samples.MixedFieldTypes;
import app.db.samples.PrivateFields;
import app.db.samples.PrivateInheritedFields;
import app.db.samples.PublicFields;
import app.db.samples.PublicInheritedFields;
import app.db.samples.PublicStaticMixed;
import app.db.samples.SuperTableEntity;
import app.db.samples.TableEntity;
import app.db.util.ParsedQuery;
import app.db.util.QueryParser;
import org.junit.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class QueryGeneratorTest {

  @Test
  public void testClassWithNoFieldsIsValid() throws Exception {
    ParsedQuery q = generateAndParse(new Empty());
    assertEquals("table name", Empty.class.getSimpleName(), q.getTable());
    assertEquals("column count", 0, q.getColumns().size());
  }

  @Test
  public void testPublicFieldsIncluded() throws Exception {
    ParsedQuery q = generateAndParse(new PublicFields());
    assertEquals("table name", PublicFields.class.getSimpleName(), q.getTable());
    assertEquals("column count", 2, q.getColumns().size());
    assertContains(q, "field1", "field1value");
    assertContains(q, "field2", "field2value");
  }

  @Test
  public void testPrivateFieldsIncluded() throws Exception {
    ParsedQuery q = generateAndParse(new PrivateFields());
    assertEquals("table name", PrivateFields.class.getSimpleName(), q.getTable());
    assertEquals("column count", 2, q.getColumns().size());
    assertContains(q, "pfield1", "pfield1value");
    assertContains(q, "pfield2", "pfield2value");
  }

  @Test
  public void testMixedTypesAccepted() throws Exception {
    ParsedQuery q = generateAndParse(new MixedFieldTypes());
    assertEquals("table name", MixedFieldTypes.class.getSimpleName(), q.getTable());
    assertEquals("column count", 3, q.getColumns().size());
    assertContains(q, "field1", "field1value");
    assertContains(q, "field2", 2);
    assertContains(q, "field3", Instant.EPOCH);
  }

  @Test
  public void testPublicStaticFieldsIgnored() throws Exception {
    ParsedQuery q = generateAndParse(new PublicStaticMixed());
    assertEquals("table name", PublicStaticMixed.class.getSimpleName(), q.getTable());
    assertEquals("column count", 2, q.getColumns().size());
    assertContains(q, "field1", "field1value");
    assertContains(q, "field3", "field3value");
  }

  @Test
  public void testPublicFieldsInherited() throws Exception {
    ParsedQuery q = generateAndParse(new PublicInheritedFields());
    assertEquals("table name", PublicInheritedFields.class.getSimpleName(), q.getTable());
    assertEquals("column count", 3, q.getColumns().size());
    assertContains(q, "field1", "field1value");
    assertContains(q, "field2", "field2value");
    assertContains(q, "field3", "field3value");
  }

  @Test
  public void testPrivateFieldsInherited() throws Exception {
    ParsedQuery q = generateAndParse(new PrivateInheritedFields());
    assertEquals("table name", PrivateInheritedFields.class.getSimpleName(), q.getTable());
    assertEquals("column count", 3, q.getColumns().size());
    assertContains(q, "pfield1", "pfield1value");
    assertContains(q, "pfield2", "pfield2value");
    assertContains(q, "pfield3", "pfield3value");
  }

  @Test
  public void testTableAnnotationReplacesSimpleName() throws Exception {
    ParsedQuery q = generateAndParse(new TableEntity());
    assertEquals("table name", "AnnotationName", q.getTable());
    assertEquals("column count", 1, q.getColumns().size());
    assertContains(q, "field1", "field1value");
  }

  @Test
  public void testTableAnnotationIgnoredOnSuperclass() throws Exception {
    ParsedQuery q = generateAndParse(new SuperTableEntity());
    assertEquals("table name", SuperTableEntity.class.getSimpleName(), q.getTable());
    assertEquals("column count", 2, q.getColumns().size());
    assertContains(q, "field1", "field1value");
    assertContains(q, "field2", "field2value");
  }

  @Test
  public void testColumnAnnotationReplacesFieldName() throws Exception {
    ParsedQuery q = generateAndParse(new ColumnEntity());
    assertEquals("table name", ColumnEntity.class.getSimpleName(), q.getTable());
    assertEquals("column count", 2, q.getColumns().size());
    assertContains(q, "annotatedField1", "field1value");
    assertContains(q, "field2", "field2value");
  }

  private static ParsedQuery generateAndParse(Object entity) throws Exception {
    return QueryParser.parse(new QueryGenerator().generateInsertStatement(entity));
  }

  private static void assertContains(ParsedQuery q, String columnName, Object value) {
    boolean found = false;
    List<String> columns = q.getColumns();
    for (int i = 0; i < columns.size(); i++) {
      if (columnName.equals(columns.get(i))) {
        if (!value.equals(q.getParams().get(i))) {
          throw new AssertionError(String.format(
              "parameter for column %s doesn't match at position %d. expected %s, found %s",
              columnName, i, value, q.getParams().get(i)));
        }
        found = true;
      }
    }
    if (!found) {
      throw new AssertionError("didn't find column " + columnName);
    }
  }
}
