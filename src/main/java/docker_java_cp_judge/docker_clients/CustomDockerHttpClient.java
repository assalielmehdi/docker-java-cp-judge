package docker_java_cp_judge.docker_clients;

import com.github.dockerjava.api.DockerClient;

public interface CustomDockerHttpClient {
  DockerClient getClient();
}
