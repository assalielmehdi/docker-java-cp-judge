import java.util.*;
import java.io.*;

public class AVERAGE {
  static class Solver {
    void solve(InputReader in, PrintWriter out) throws Exception {

    }
  }

  public static void main(String... args) throws Exception {
    InputReader in = new InputReader("LIGHT.in");
    PrintWriter out = new PrintWriter(System.out);
    new Solver().solve(in, out);
    out.close();
    in.close();
  }

  static class InputReader {
    BufferedReader reader;
    StringTokenizer tokenizer;

    InputReader(InputStream is) throws Exception {
      reader = new BufferedReader(new InputStreamReader(is));
    }

    InputReader(String inFilePath) throws Exception {
      this(new FileInputStream(inFilePath));
    }

    InputReader() throws Exception {
      this(System.in);
    }

    String next() throws Exception {
      while (tokenizer == null || !tokenizer.hasMoreTokens()) {
        tokenizer = new StringTokenizer(reader.readLine());
      }
      return tokenizer.nextToken();
    }

    String nextLine() throws Exception {
      return reader.readLine();
    }

    int nextInt() throws Exception {
      return Integer.parseInt(next());
    }

    long nextLong() throws Exception {
      return Long.parseLong(next());
    }

    double nextDouble() throws Exception {
      return Double.parseDouble(next());
    }

    void close() throws Exception {
      reader.close();
    }
  }
}