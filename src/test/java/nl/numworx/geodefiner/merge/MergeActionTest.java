package nl.numworx.geodefiner.merge;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import nl.numworx.geodefiner.merge.MergeAction.QueryType;

public class MergeActionTest {

  MergeAction action;
  
  MergeAction.Query KEEP = new MergeAction.Query() {

    @Override
    public String name() {
      return null;
    }

    @Override
    public QueryType ask(String name) {
      return QueryType.KEEP;
    }
    
  };

  MergeAction.Query REPLACE = new MergeAction.Query() {

    @Override
    public String name() {
      return null;
    }

    @Override
    public QueryType ask(String name) {
      return QueryType.REPLACE;
    }
    
  };

  MergeAction.Query RENAME = new MergeAction.Query() {

    private String N;

    @Override
    public String name() {
      return N;
    }

    @Override
    public QueryType ask(String name) {
      N=name+1;
      return QueryType.RENAME;
    }
    
  };
  
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {}

  @AfterClass
  public static void tearDownAfterClass() throws Exception {}

  @Before
  public void setUp() throws Exception {
    action = new MergeAction();
    action.chooser = null;
    action.editor = null;
  }

  @After
  public void tearDown() throws Exception {}

  @SuppressWarnings("unchecked")
  private Map<String, ?> getData(String resource) throws IOException, ParseException {
    JSONParser parser = new JSONParser();
    return (Map<String,?>) parser.parse(new InputStreamReader(getClass().getResourceAsStream(resource)));
  }
   
  @Test
  public void testMergeKeep() throws IOException, ParseException {
    Map<String, ?> org = getData("launch_data");   
    Map<String, ?> result = action.merge(org, org, KEEP);
    assertNotNull(result);
    assertEquals("keep", org, result);
    List definitions = (List) result.get("definitions");
    assertEquals(4, definitions.size());
  }
  @Test
  public void testMergeReplace() throws IOException, ParseException {
    Map<String, ?> org = getData("launch_data");   
    Map<String, ?> result = action.merge(org, org, REPLACE);
    assertNotNull(result);
    assertEquals("keep", org, result);
    List definitions = (List) result.get("definitions");
    assertEquals(4, definitions.size());
  }
  @Test
  public void testMergeRename() throws IOException, ParseException {
    Map<String, ?> org = getData("launch_data");   
    Map<String, ?> result = action.merge(org, org, RENAME);
    assertNotNull(result);
    assertEquals("keep", org, result);
    List definitions = (List) result.get("definitions");
    assertEquals(8, definitions.size());
  }

}
