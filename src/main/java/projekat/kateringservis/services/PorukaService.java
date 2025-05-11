package projekat.kateringservis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projekat.kateringservis.models.Korisnik;
import projekat.kateringservis.models.Poruka;
import projekat.kateringservis.models.Proslava;
import projekat.kateringservis.repositories.PorukaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class PorukaService {

    private final PorukaRepository porukaRepository;

    @Autowired
    public PorukaService(PorukaRepository porukaRepository) {
        this.porukaRepository = porukaRepository;
    }

    //Metoda za dodavanje nove poruke u bazu
    public void kreirajPoruku(Proslava proslava, Korisnik korisnik, String tekstPoruke) {

        Poruka novaPoruka = new Poruka(proslava, korisnik.getKorisnickoIme(), tekstPoruke, LocalDateTime.now());

        if (Objects.equals(korisnik.getUloga(), "KORISNIK")) {
            novaPoruka.setProcitanaKorisnik(true);
        }
        else {
            novaPoruka.setProcitanaMenadzer(true);
        }

        try {
            porukaRepository.save(novaPoruka);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Metoda za hvatanje svih poruka u chatu jedne proslave
    public List<Poruka> getByProslava(Proslava proslava) { return porukaRepository.findByProslava(proslava); }
}
