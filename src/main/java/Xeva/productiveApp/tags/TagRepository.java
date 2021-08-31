package Xeva.productiveApp.tags;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByNameAndOwnerEmail(String name, String ownerEmail);

    Optional<Tag> findByOwnerEmailAndId(String ownerEmail, Long id);


    Optional<List<Tag>> findAllByTaskId(Long id);

    Optional<Set<Tag>> findAllByOwnerEmail(String ownerEmail);

    @Transactional
    @Modifying
    @Query("UPDATE Tag tag " +
            "SET tag.taskId = null where tag.id = ?1")
    void deleteById(Long id);

    @Transactional
    void deleteByNameAndOwnerEmail(String name, String ownerEmail);

    @Transactional
    void deleteAllByOwnerEmail(String ownerEmail);

}
