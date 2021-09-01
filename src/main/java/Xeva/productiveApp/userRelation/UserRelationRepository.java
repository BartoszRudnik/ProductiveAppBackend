package Xeva.productiveApp.userRelation;

import Xeva.productiveApp.appUser.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRelationRepository extends JpaRepository<UserRelation, Long> {

    Optional<UserRelation> findByUser1AndUser2(ApplicationUser user1, ApplicationUser user2);
    Optional<Set<UserRelation>> findAllByUser1(ApplicationUser user);
    Optional<Set<UserRelation>> findAllByUser2(ApplicationUser user);
    Optional<Set<UserRelation>> findAllByUser1OrUser2(ApplicationUser user1, ApplicationUser user2);

    @Transactional
    void deleteByUser1OrUser2(ApplicationUser user1, ApplicationUser user2);

}
