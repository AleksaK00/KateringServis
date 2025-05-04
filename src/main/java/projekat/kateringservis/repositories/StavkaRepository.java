package projekat.kateringservis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projekat.kateringservis.models.Stavka;

@Repository
public interface StavkaRepository extends JpaRepository<Stavka, Integer> {
}
