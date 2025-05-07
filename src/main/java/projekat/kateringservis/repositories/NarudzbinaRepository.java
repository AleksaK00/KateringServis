package projekat.kateringservis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projekat.kateringservis.models.Korisnik;
import projekat.kateringservis.models.Narudzbina;

import java.util.List;

@Repository
public interface NarudzbinaRepository extends JpaRepository<Narudzbina, Integer> {

    List<Narudzbina> findByKorisnik(Korisnik korisnik);
}
