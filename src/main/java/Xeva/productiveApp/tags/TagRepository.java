package Xeva.productiveApp.tags;

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
    @Modifying
    @Query("UPDATE Tag tag " +
            "SET tag.name = ?1 where (tag.name = ?2 AND tag.ownerEmail = ?3)")
    int updateTagName(String newName, String oldName, String ownerEmail);

    @Transactional
    void deleteAllByOwnerEmail(String ownerEmail);

}
