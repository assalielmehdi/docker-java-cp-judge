package docker_java_cp_judge.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

public class CmdBuilder {
  @Value("${service.cmd}")
  private static String cmd;

  @Value("${service.test_cmd}")
  private static String testCmd;

  private CmdBuilder() {
  }

  public static String[] buildCmd(
    MultipartFile codeFile,
    String input,
    String expectedOutput,
    String language
  ) throws IOException {
    return new String[]{
      cmd,
      language,
      Base64.getEncoder().encodeToString(codeFile.getBytes()),
      input,
      expectedOutput
    };
  }

  public static String[] buildTestCmd(String load) {
    return new String[]{cmd, testCmd, load};
  }
}
