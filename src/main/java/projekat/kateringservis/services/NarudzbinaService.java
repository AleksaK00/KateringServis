package projekat.kateringservis.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projekat.kateringservis.models.Korisnik;
import projekat.kateringservis.models.Narudzbina;
import projekat.kateringservis.models.Stavka;
import projekat.kateringservis.repositories.NarudzbinaRepository;
import projekat.kateringservis.repositories.StavkaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NarudzbinaService {

    private final NarudzbinaRepository narudzbinaRepository;
    private final StavkaRepository stavkaRepository;

    @Autowired
    public NarudzbinaService(NarudzbinaRepository narudzbinaRepository, StavkaRepository stavkaRepository) {
        this.narudzbinaRepository = narudzbinaRepository;
        this.stavkaRepository = stavkaRepository;
    }

    //Metoda koja kreira novu narudzbinu na osnovu korpe i unete adrese i datuma
    @Transactional
    public Narudzbina kreirajNarudzbinu(List<Stavka> korpa, String adresa, LocalDateTime datum, Korisnik korisnik) {

        Narudzbina narudzbina = new Narudzbina();

        try {
            //Kreiranje narudzbine i dodavanje u bazu podataka
            narudzbina.setAdresa(adresa);
            narudzbina.setDatum(datum);
            narudzbina.setKorisnik(korisnik);

            //Racunjanje ukupne cene narudzbine
            double ukupnaCena = 0;
            for (Stavka stavka : korpa) {
                ukupnaCena += stavka.getArtikal().getCena() * stavka.getKolicina();
            }
            narudzbina.setCena(ukupnaCena);

            narudzbinaRepository.save(narudzbina);

            //Dodavanje stavki u bazu podataka
            for (Stavka novaStavka : korpa) {
                novaStavka.setNarudzbina(narudzbina);
                stavkaRepository.save(novaStavka);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return narudzbina;
    }

    public List<Narudzbina> getByKorisnik(Korisnik korisnik) {
        return narudzbinaRepository.findByKorisnik(korisnik);
    }

    //Metoda za otkazivanje narudzbine, vraca true pri uspehu
    public boolean cancelOrder(int id) {

        Optional<Narudzbina> narudzbina = narudzbinaRepository.findById(id);

        if (narudzbina.isEmpty()) {
            return false;
        }
        else {
            narudzbina.get().setOtkazana(true);
            narudzbinaRepository.save(narudzbina.get());
            return true;
        }
    }

    //Vraca sve narudzbine
    public List<Narudzbina> getAll() { return narudzbinaRepository.findAll();}

    //Vraca sve narudzbine zakazane za ovaj mesec
    public List<Narudzbina> getAllThisMonth() {

        LocalDateTime danasnjiDatum = LocalDateTime.now();
        LocalDateTime pocetakMeseca = LocalDateTime.of(danasnjiDatum.getYear(), danasnjiDatum.getMonth(), 1, 0, 0);
        LocalDateTime krajMeseca = LocalDateTime.of(danasnjiDatum.getYear(), danasnjiDatum.getMonth(), danasnjiDatum.getMonth().maxLength(), 23, 59);

        return narudzbinaRepository.findByDatumBetween(pocetakMeseca, krajMeseca);
    }

}
