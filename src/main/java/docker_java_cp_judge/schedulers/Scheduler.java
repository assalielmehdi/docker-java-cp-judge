package docker_java_cp_judge.schedulers;

public interface Scheduler {
  String schedule();

  void update(String containerId);
}
