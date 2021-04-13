package Xeva.productiveApp.tags;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<List<Tag>> findAllByTaskId(Long id);

    Optional<Set<Tag>> findAllByOwnerEmail(String ownerEmail);

    @Transactional
    void deleteByName(String name);

}
