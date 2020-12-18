import java.util.*;
import java.io.*;

public class AVERAGE {
  static class Solver {
    int n, m;
    List<Integer>[] adj;
    int[] degree;
    boolean[] visited;
    List<Integer> cc = new ArrayList<>();

    void solve(InputReader in, PrintWriter out) throws Exception {
      n = in.nextInt();
      m = in.nextInt();
      adj = new List[n + 1];
      degree = new int[n + 1];
      visited = new boolean[n + 1];
      for (int i = 1; i <= n; i++) {
        adj[i] = new ArrayList<>();
      }
      for (int i = 1; i <= m; i++) {
        int u = in.nextInt(), v = in.nextInt();
        adj[u].add(v);
        adj[v].add(u);
        degree[u]++;
        degree[v]++;
      }
      for (int i = 1; i <= n; i++) {
        if (!visited[i]) {
          cc.clear();
          dfs(i);
          for (int node : cc) {
            if (degree[node] != cc.size() - 1) {
              out.println("NO");
              return;
            }
          }
        }
      }
      out.println("YES");
    }

    void dfs(int cur) {
      if (!visited[cur]) {
        visited[cur] = true;
        cc.add(cur);
        for (int next : adj[cur]) {
          if (!visited[next]) {
            dfs(next);
          }
        }
      }
    }
  }

  public static void main(String... args) throws Exception {
    long millis = System.currentTimeMillis();
    InputReader in = new InputReader("AVERAGE.in");
    PrintWriter out = new PrintWriter(System.out);
    new Solver().solve(in, out);
    out.printf("\nExecution Time: %dms\n", System.currentTimeMillis() - millis);
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