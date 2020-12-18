package docker_java_cp_judge.runners;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.model.Frame;
import docker_java_cp_judge.docker_clients.CustomDockerHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

@Component
public class RunnerImpl implements Runner {
  private final CustomDockerHttpClient client;

  @Autowired
  public RunnerImpl(CustomDockerHttpClient client) {
    this.client = client;
  }

  public DeferredResult<ResponseEntity<String>> run(String containerId, String... cmd) {
    ExecCreateCmdResponse execResp = client.getClient().execCreateCmd(containerId)
      .withAttachStdout(true)
      .withCmd(cmd)
      .exec();

    var response = new DeferredResult<ResponseEntity<String>>();

    client.getClient()
      .execStartCmd(execResp.getId())
      .exec(new ResultCallback.Adapter<>() {
        @Override
        public void onNext(Frame frame) {
          response.setResult(ResponseEntity.ok(new String(frame.getPayload())));
        }
      });

    return response;
  }
}
