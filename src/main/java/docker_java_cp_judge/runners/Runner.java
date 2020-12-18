package docker_java_cp_judge.runners;

import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

public interface Runner {
  DeferredResult<ResponseEntity<String>> run(String containerId, String...cmd);
}
