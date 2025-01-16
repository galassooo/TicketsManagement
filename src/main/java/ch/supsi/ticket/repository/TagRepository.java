package ch.supsi.ticket.repository;

import ch.supsi.ticket.model.Attachment;
import ch.supsi.ticket.model.Milestone;
import ch.supsi.ticket.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}
