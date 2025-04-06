package tn.cloudnine.queute.repository.forum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.cloudnine.queute.model.forum.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
}
