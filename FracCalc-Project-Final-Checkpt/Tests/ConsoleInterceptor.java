import java.io.*;
import java.nio.charset.StandardCharsets;

public class ConsoleInterceptor {

    public interface Block {
      void call();
    }

    public static String copyOut(String initialString, Block block) {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      PrintStream printStream = new PrintStream(bos, true);
      PrintStream oldStream = System.out;
      System.setOut(printStream);

      InputStream oldInput = System.in;
      InputStream inputStream = new ByteArrayInputStream(initialString.getBytes());
      System.setIn(inputStream);

      try {
        block.call();
      }
      catch (Exception e) 
      {
        System.err.println("FAIL: " + e.toString());
      }
      finally {
        System.setOut(oldStream);
        System.setIn(oldInput);
      }
      return bos.toString();
    }
}