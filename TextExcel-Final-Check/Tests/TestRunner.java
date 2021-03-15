import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import java.util.*;
import java.util.AbstractMap.SimpleImmutableEntry;

public class TestRunner {
  static List<Map.Entry<String, Class<?>[]>> tests = new ArrayList<Map.Entry<String, Class<?>[]>>();
  static {
    tests.add(new SimpleImmutableEntry("Part A", new Class<?>[] { TestsALL.A_Checkpoint1.class, TestsALL.A_Checkpoint2.class, TestsALL.A_Checkpoint3.class, TestsALL.A_Final.class }));
    tests.add(new SimpleImmutableEntry("Part B", new Class<?>[] { TestsALL.B_Checkpoint1.class, TestsALL.B_Final.class }));
    tests.add(new SimpleImmutableEntry("Part C", new Class<?>[] { TestsALL.C_Checkpoint1.class, TestsALL.C_Final.class }));
    tests.add(new SimpleImmutableEntry("Extra Credit", new Class<?>[] { TestsALL.ExtraCreditCommandErrors.class, TestsALL.ExtraCreditCommandHistory.class, TestsALL.ExtraCreditCircularReferenceErrors.class, TestsALL.ExtraCreditEvaluationErrors.class, TestsALL.ExtraCreditOperationOrder.class, TestsALL.C_ExtraCreditHeterogeneousSorting.class }));
  }

  public static void Run(String suite) {
    // clear the screen
    System.out.print("\033[H\033[2J");
    System.out.flush();
    
    for (Map.Entry<String, Class<?>[]> kv : tests) {
      if (suite == null || suite.compareToIgnoreCase(kv.getKey()) == 0) {
        for (Class<?> c : kv.getValue()) {
          Result result = JUnitCore.runClasses(c);
          if (result.wasSuccessful()) {
            continue;
          }
          
          System.out.println("" + kv.getKey() + " failed on " + c.getSimpleName() + ". Here are the errors:");
          for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
          }
          return; // only print the first failure
        }
        System.out.println("" + kv.getKey() + " passed.");
      }
    }  
  }
  
  public static void Run() {
    Run(null);
  }
}