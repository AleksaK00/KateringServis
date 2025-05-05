package projekat.kateringservis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projekat.kateringservis.models.Proslava;

@Repository
public interface ProslavaRepository extends JpaRepository<Proslava, Integer> {

}
