package projekat.kateringservis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projekat.kateringservis.models.Korisnik;
import projekat.kateringservis.models.Proslava;
import projekat.kateringservis.repositories.ProslavaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProslavaService {

    private final ProslavaRepository proslavaRepository;

    @Autowired
    public ProslavaService(ProslavaRepository proslavaRepository) {
        this.proslavaRepository = proslavaRepository;
    }

    //Metoda za dodavanje nove proslave u bazu
    public void novaProslava(Korisnik korisnik, String adresa, String napomena, int brOsoba, double cena, LocalDateTime datum) {

        //Instanciranje objekta Proslava
        Proslava proslava = new Proslava(korisnik, adresa, brOsoba, cena, datum, napomena);
        proslavaRepository.save(proslava);
    }

    //Metoda za hvatanje svih proslava jednog korisnika
    public List<Proslava> getByKorisnik(Korisnik korisnik) { return proslavaRepository.findByKorisnik(korisnik); }

    //Metoda za otkazivanje proslave
    public boolean cancelOrder(int id) {

        Optional<Proslava> proslava = proslavaRepository.findById(id);

        if (proslava.isEmpty()) {
            return false;
        }
        else {
            proslava.get().setOtkazana(true);
            proslavaRepository.save(proslava.get());
            return true;
        }
    }
}
