package engine.repositories;

import engine.models.Completion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletionRepository extends PagingAndSortingRepository<Completion, Integer> {
    @Query("SELECT c FROM Completion c WHERE c.solver.id = ?1 ORDER BY c.completedAt DESC")
    Page<Completion> findAllCompletionsByUser(int id, PageRequest pageable);
}
