package projekat.kateringservis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projekat.kateringservis.models.Poruka;
import projekat.kateringservis.models.Proslava;

import java.util.List;

@Repository
public interface PorukaRepository extends JpaRepository<Poruka, Integer> {

    List<Poruka> findByProslava(Proslava proslava);
}
