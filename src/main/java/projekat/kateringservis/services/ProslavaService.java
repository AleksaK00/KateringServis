package projekat.kateringservis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projekat.kateringservis.models.Korisnik;
import projekat.kateringservis.models.Proslava;
import projekat.kateringservis.repositories.ProslavaRepository;

import java.time.LocalDateTime;

@Service
public class ProslavaService {

    private final ProslavaRepository proslavaRepository;

    @Autowired
    public ProslavaService(ProslavaRepository proslavaRepository) {
        this.proslavaRepository = proslavaRepository;
    }

    public void novaProslava(Korisnik korisnik, String adresa, String napomena, int brOsoba, double cena, LocalDateTime datum) {

        //Instanciranje objekta Proslava
        Proslava proslava = new Proslava(korisnik, adresa, brOsoba, cena, datum, napomena);
        proslavaRepository.save(proslava);
    }
}
