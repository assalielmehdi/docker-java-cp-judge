package docker_java_cp_judge.controllers;

import docker_java_cp_judge.runners.Runner;
import docker_java_cp_judge.schedulers.Scheduler;
import docker_java_cp_judge.utils.CmdBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class Controller {
  private final Runner runner;
  private final Scheduler scheduler;

  @Autowired
  public Controller(Runner runner, Scheduler scheduler) {
    this.runner = runner;
    this.scheduler = scheduler;
  }

  @PostMapping(path = "/submit")
  public DeferredResult<ResponseEntity<String>> postSubmission(
          @RequestParam String language,
          @RequestPart MultipartFile codeFile,
          @RequestParam String input,
          @RequestParam String expectedOutput,
          @RequestParam int timeLimit
  ) throws IOException {
    String containerId = scheduler.schedule();
    String[] cmd = CmdBuilder.buildCmd(language, codeFile, input, expectedOutput, timeLimit);
    return run(containerId, cmd);
  }

  @GetMapping(path = "/test")
  public DeferredResult<ResponseEntity<String>> testSystem(@RequestParam String load) {
    String containerId = scheduler.schedule();
    String[] cmd = CmdBuilder.buildTestCmd(load);
    return run(containerId, cmd);
  }

  private DeferredResult<ResponseEntity<String>> run(String containerId, String... cmd) {
    var result = runner.run(containerId, cmd);
    result.onCompletion(() -> scheduler.update(containerId));
    return result;
  }
}
