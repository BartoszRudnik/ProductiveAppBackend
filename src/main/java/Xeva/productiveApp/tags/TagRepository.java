package Xeva.productiveApp.tags;

import Xeva.productiveApp.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<List<Tag>> findAllByTaskId(Long id);

}
