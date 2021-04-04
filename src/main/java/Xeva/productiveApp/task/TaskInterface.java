package Xeva.productiveApp.task;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskInterface extends JpaRepository<Task, Long> {

}
