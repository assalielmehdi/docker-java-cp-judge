package docker_java_cp_judge.schedulers;

import com.github.dockerjava.api.model.Container;
import docker_java_cp_judge.docker_clients.CustomDockerHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

@Component
public class SchedulerImpl implements Scheduler {
  private final CustomDockerHttpClient client;

  @Value("${service.image}")
  private String image;

  @Value("${config.max_running_containers}")
  private int maxRunningContainers;

  private final static Map<String, String> labels = Map.of("app", "docker-judge");

  @Autowired
  public SchedulerImpl(CustomDockerHttpClient client) {
    this.client = client;
  }

  @PostConstruct
  public void init() {
    IntStream.range(0, maxRunningContainers).forEach(this::createContainer);
  }

  @Override
  public synchronized String schedule() {
    Optional<Container> runner = null;

    do {
      runner = listContainers().stream()
        .filter(c -> "exited|created".contains(c.getState()))
        .findAny();
    } while (runner.isEmpty());

    startContainer(runner.get().getId());

    return runner.get().getId();
  }

  @Override
  public void update(String containerId) {
    stopContainer(containerId);
  }

  private List<Container> listContainers() {
    return client.getClient()
      .listContainersCmd()
      .withLabelFilter(labels)
      .withShowAll(true)
      .exec();
  }

  private void createContainer(int containerNum) {
    client.getClient()
      .createContainerCmd(image)
      .withName(String.format("judge-%02d", containerNum))
      .withLabels(labels)
      .exec();
  }

  private void startContainer(String containerId) {
    client.getClient()
      .startContainerCmd(containerId)
      .exec();
  }

  private void stopContainer(String containerId) {
    client.getClient()
      .stopContainerCmd(containerId)
      .exec();
  }
}
