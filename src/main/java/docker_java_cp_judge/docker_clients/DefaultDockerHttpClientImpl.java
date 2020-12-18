package docker_java_cp_judge.docker_clients;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import org.springframework.stereotype.Component;

@Component
public class DefaultDockerHttpClientImpl implements CustomDockerHttpClient {
  private final DockerClient client;

  public DefaultDockerHttpClientImpl() {
    var config = DefaultDockerClientConfig.createDefaultConfigBuilder()
      .build();

    var httpClient = new ApacheDockerHttpClient.Builder()
      .dockerHost(config.getDockerHost())
      .build();

    client = DockerClientImpl.getInstance(config, httpClient);
  }

  @Override
  public DockerClient getClient() {
    return client;
  }
}
