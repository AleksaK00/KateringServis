package projekat.kateringservis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projekat.kateringservis.models.Korisnik;
import projekat.kateringservis.models.Proslava;

import java.util.List;

@Repository
public interface ProslavaRepository extends JpaRepository<Proslava, Integer> {

    List<Proslava> findByKorisnik(Korisnik korisnik);
}
