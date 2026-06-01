package backend.repository;

import backend.models.SavedTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedTransactionRepository extends JpaRepository<SavedTransaction, Long> {

    List<SavedTransaction> findByUserIdOrderByUpcomingDateAsc(long userId);
}