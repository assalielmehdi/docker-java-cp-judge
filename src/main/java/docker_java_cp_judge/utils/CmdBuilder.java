package docker_java_cp_judge.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

public class CmdBuilder {
  private CmdBuilder() {
  }

  public static String[] buildCmd(
          String language,
          MultipartFile codeFile,
          String input,
          String expectedOutput,
          int timeLimit
  ) throws IOException {
    return new String[]{
            "judge",
            "run",
            language,
            Base64.getEncoder().encodeToString(codeFile.getBytes()),
            input,
            expectedOutput,
            String.valueOf(timeLimit)
    };
  }

  public static String[] buildTestCmd(String load) {
    return new String[]{"judge", "test", load};
  }
}
