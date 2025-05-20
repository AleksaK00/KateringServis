package projekat.kateringservis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projekat.kateringservis.models.Korisnik;

import java.util.List;
import java.util.Optional;

@Repository
public interface KorisnikRepository extends JpaRepository<Korisnik, Integer> {

    Optional<Korisnik> findByKorisnickoIme(String korisnickoIme);
    List<Korisnik> findByKorisnickoImeContainingIgnoreCase(String korisnickoIme);
    Optional<Korisnik> findByEmail(String email);
}
