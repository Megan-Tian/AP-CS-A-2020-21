import static org.junit.Assert.*;
import java.io.PrintStream;
import org.junit.*;
import java.io.ByteArrayOutputStream;
import java.io.*;
import java.nio.charset.StandardCharsets;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

abstract class FracCalcTests {
  static boolean testing = false;

  static boolean testMain(String input, String expected)
  {
    if (testing)
      return false;

    testing = true;
    
    String result = ConsoleInterceptor.copyOut(input, () ->{
      Main.main(new String[] {});
    });

    assertEquals(expected, result);

    testing = false;
    return true;
  }

  public static void Checkpoint1() {
    try {
      if (!testMain("1_1/3 + 2_1/2\n", "2_1/2\n"))
        return;

      // produceAnswer returns the second operand. For example, produceAnswer(“1_2/3 +
      // 4_5/6”) returns “4_5/6”.
      assertEquals("4_5/6", Main.produceAnswer("1_2/3 + 4_5/6"));
      assertEquals("-3_3/16", Main.produceAnswer("1_2/3 + -3_3/16"));
      assertEquals("1", Main.produceAnswer("1_2/3 + 1"));
      assertEquals("1/2", Main.produceAnswer("1_2/3 + 1/2"));
      assertEquals("3", Main.produceAnswer("1 * 3"));
      assertEquals("-3", Main.produceAnswer("-1 - -3"));
      System.out.println("Checkpoint 1 PASSED.");

    } catch (ComparisonFailure c) {
      System.out.println("Checkpoint 1 FAILED: " + c.getMessage());
    }
  }

  public static void Checkpoint2() {
    try {
      if (!testMain("1_1/3 - 2_1/3\n-3/7 - 20\nquit", "whole:2 numerator:1 denominator:3\nwhole:20 numerator:0 denominator:1\n"))
        return;

      // produceAnswer("5_3/4 - 6_5/8") returns "whole:6 numerator:5 denominator:8"
      // produceAnswer("-3/7 - 20") returns "whole:20 numerator:0 denominator:1"
      // produceAnswer("-32 - 27/21") returns "whole:0 numerator:27 denominator:21"
      assertEquals("whole:6 numerator:5 denominator:8", Main.produceAnswer("5_3/4 - 6_5/8"));
      assertEquals("whole:20 numerator:0 denominator:1", Main.produceAnswer("-3/7 - 20"));
      assertEquals("whole:0 numerator:27 denominator:21", Main.produceAnswer("-32 - 27/21"));
      assertEquals("whole:-1 numerator:5 denominator:4", Main.produceAnswer("-32 - -1_5/4"));
      System.out.println("Checkpoint 2 PASSED.");
    
    } catch (ComparisonFailure c) {
      System.out.println("Checkpoint 2 FAILED: " + c.getMessage());
    }
  }
  public static void Checkpoint3() {
    ScriptEngineManager mgr = new ScriptEngineManager();
    ScriptEngine engine = mgr.getEngineByName("JavaScript");
    
    String[][] tests = {
      { "2 + 1",       "3" },
      { "1_1/2 + 3/2", "3" },
      { "4 - 2/1",     "2" },
      { "1/2 * 6",     "3" },
      { "1 - 1_1/2", "-0.5" },
      { "2_1/2 / 1/2", "5" }
    };
    try {
      for (int i = 0; i < tests.length; ++i) {
        String result = Main.produceAnswer(tests[i][0]);
        Object answer = engine.eval(result.replace("_", result.startsWith("-") ? "-" : "+")).toString();
        assertEquals(tests[i][1], answer.toString());
      }
      System.out.println("Checkpoint 3 PASSED.");
    } catch (ComparisonFailure c) {
      System.out.println("Checkpoint 3 FAILED: " + c.getMessage());
    } catch (ScriptException e) {
      System.out.println("Checkpoint 3 TEST ERRROR: Your answer doesn't look like a fraction (" + e.getMessage() + ")");
    }
  }

  public static void FinalCheckpoint() {
    try {
      if (!testMain("1_1/3 + 2_1/3\n-3/7 - 20\nquit", "3_2/3\n-20_3/7\n"))
        return;

      Basic();
      Intermediate();
      Advanced();
      System.out.println("Final Checkpoint PASSED.");
    } catch (ComparisonFailure c) {
      System.out.println("Final Checkpoint FAILED: " + c.getMessage());
    }
  }

  public static void Basic() {
    testAdditionBasic();
    testSubtractionBasic();
    testMultiplicationBasic();
    testDivisionBasic();
    System.out.println("All Basic Tests PASSED.");
  }

  public static void Intermediate() {
    testAdditionIntermediate();
    testSubtractionIntermediate();
    testMultiplicationIntermediate();
    testDivisionIntermediate();
    System.out.println("All Intermediate Tests PASSED.");
  }

  public static void Advanced() {
    testAdditionAdvanced();
    testSubtractionAdvanced();
    testMultiplicationAdvanced();
    testDivisionAdvanced();
    System.out.println("All Advanced Tests PASSED.");
  }

  @Test
  public static void testAdditionBasic() {
    assertEquals("2/5", Main.produceAnswer("1/5 + 1/5"));
    assertEquals("1_1/5", Main.produceAnswer("3/5 + 3/5"));
    assertEquals("1_1/5", Main.produceAnswer("4/5 + 2/5"));
    assertEquals("1/4", Main.produceAnswer("1/8 + 1/8"));
  }

  @Test
  public static void testAdditionIntermediate() {
    assertEquals("2/5", Main.produceAnswer("3/5 + -1/5"));
    assertEquals("5_5/6", Main.produceAnswer("20/8 + 3_1/3"));
    assertEquals("1", Main.produceAnswer("3/5 + 2/5"));
    assertEquals("1_1/20", Main.produceAnswer("4/5 + 2/8"));
    assertEquals("452", Main.produceAnswer("452 + 0"));
    assertEquals("2", Main.produceAnswer("1 + 1"));
    assertEquals("4", Main.produceAnswer("1 + 3"));
    assertEquals("254", Main.produceAnswer("0 + 254"));
    assertEquals("1021778", Main.produceAnswer("124543 + 897235"));
    assertEquals("900", Main.produceAnswer("978 + -78"));
  }

  @Test
  public static void testAdditionAdvanced() {
    assertEquals("-9035", Main.produceAnswer("-9035 + 0"));
    assertEquals("-64", Main.produceAnswer("64 + -128"));
    assertEquals("-133", Main.produceAnswer("-98 + -35"));
    assertEquals("62_11/19", Main.produceAnswer("0 + 34_543/19"));
    assertEquals("-44_229/888", Main.produceAnswer("-38_3/72 + -4_82/37"));
    assertEquals("-7/8", Main.produceAnswer("5_3/4 - 6_5/8"));
  }

  @Test
  public static void testSubtractionBasic() {
    assertEquals("1/5", Main.produceAnswer("3/5 - 2/5"));
    assertEquals("4/5", Main.produceAnswer("9/10 - 1/10"));
    assertEquals("0", Main.produceAnswer("1/5 - 1/5"));
    assertEquals("0", Main.produceAnswer("4_1/2 - 4_1/2"));
  }

  @Test
  public static void testSubtractionIntermediate() {
    assertEquals("0", Main.produceAnswer("68591 - 68591"));
    assertEquals("-36891", Main.produceAnswer("48623 - 85514"));
    assertEquals("-9284", Main.produceAnswer("0 - 9284"));
    assertEquals("-2/5", Main.produceAnswer("2/5 - 4/5"));
    assertEquals("-1_5/8", Main.produceAnswer("4_1/2 - 5_9/8"));
    assertEquals("-1_1/8", Main.produceAnswer("3_3/4 - 4_7/8"));
  }

  @Test
  public static void testSubtractionAdvanced() {
    assertEquals("12_3/8", Main.produceAnswer("5_3/4 - -6_5/8"));
    assertEquals("8_5/21", Main.produceAnswer("-12_3/7 - -20_2/3"));
    assertEquals("-1_5/12", Main.produceAnswer("-2/3 - 3/4"));
  }

  @Test
  public static void testMultiplicationBasic() {
    assertEquals("3", Main.produceAnswer("1_1/2 * 2"));
    assertEquals("6/25", Main.produceAnswer("3/5 * 2/5"));
    assertEquals("0", Main.produceAnswer("0 * 0"));
    assertEquals("0", Main.produceAnswer("0 * 9321"));
    assertEquals("0", Main.produceAnswer("0 * -5902"));
    assertEquals("164268", Main.produceAnswer("234 * 702"));
    assertEquals("216", Main.produceAnswer("12 * 18"));
    assertEquals("8", Main.produceAnswer("12/3 * 2/1"));
  }

  @Test
  public static void testMultiplicationIntermediate() {
    assertEquals("2", Main.produceAnswer("16 * 1/8"));
    assertEquals("0", Main.produceAnswer("0 * 4/5"));
    assertEquals("2", Main.produceAnswer("3 * 2/3"));
    assertEquals("1_1/2", Main.produceAnswer("6 * 1/4"));
    assertEquals("8994872", Main.produceAnswer("1 * 8994872"));
  }

  @Test
  public static void testMultiplicationAdvanced() {
    assertEquals("-842346", Main.produceAnswer("1 * -842346"));
    assertEquals("-75421", Main.produceAnswer("-1 * 75421"));
    assertEquals("37953", Main.produceAnswer("-1 * -37953"));
    assertEquals("8", Main.produceAnswer("-12/3 * -2/1"));
  }

  @Test
  public static void testDivisionBasic() {
    assertEquals("9/16", Main.produceAnswer("3/4 / 4/3"));
    assertEquals("2_1/4", Main.produceAnswer("3/2 / 2/3"));
    assertEquals("9457", Main.produceAnswer("9457 / 1"));
    assertEquals("0", Main.produceAnswer("0 / 37569"));
    assertEquals("6/11", Main.produceAnswer("6 / 11"));
    assertEquals("4/9", Main.produceAnswer("4 / 9"));
    assertEquals("1", Main.produceAnswer("23 / 23"));
    assertEquals("2_6/7", Main.produceAnswer("20 / 7"));
    assertEquals("13/24", Main.produceAnswer("1_1/12 / 2"));
  }

  @Test
  public static void testDivisionIntermediate() {
    assertEquals("2_2/3", Main.produceAnswer("16/4 / 3/2"));
    assertEquals("0", Main.produceAnswer("0 / -98701"));
    assertEquals("1", Main.produceAnswer("3/4 / 3/4"));
    assertEquals("-2_2/3", Main.produceAnswer("16/4 / -3/2"));
  }

  @Test
  public static void testDivisionAdvanced() {
    assertEquals("1_5/8", Main.produceAnswer("-13 / -8"));
    assertEquals("-5/21", Main.produceAnswer("1_2/3 / -5_6/3"));
    assertEquals("6_661/5520", Main.produceAnswer("-38_3/72 / -4_82/37"));
    assertEquals("-2803", Main.produceAnswer("-2803 / 1"));
    assertEquals("-12457", Main.produceAnswer("12457 / -1"));
    assertEquals("45236", Main.produceAnswer("-45236 / -1"));
    assertEquals("-2_6/7", Main.produceAnswer("-20 / 7"));
  }
}
