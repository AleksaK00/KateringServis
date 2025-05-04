package projekat.kateringservis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projekat.kateringservis.models.Narudzbina;

@Repository
public interface NarudzbinaRepository extends JpaRepository<Narudzbina, Integer> {
}
