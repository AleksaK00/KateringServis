package projekat.kateringservis.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projekat.kateringservis.models.Korisnik;
import projekat.kateringservis.models.Narudzbina;
import projekat.kateringservis.models.Stavka;
import projekat.kateringservis.repositories.KorisnikRepository;
import projekat.kateringservis.repositories.NarudzbinaRepository;
import projekat.kateringservis.repositories.StavkaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NarudzbinaService {

    private final NarudzbinaRepository narudzbinaRepository;
    private final StavkaRepository stavkaRepository;
    private final KorisnikRepository korisnikRepository;

    @Autowired
    public NarudzbinaService(NarudzbinaRepository narudzbinaRepository, StavkaRepository stavkaRepository, KorisnikRepository korisnikRepository) {
        this.narudzbinaRepository = narudzbinaRepository;
        this.stavkaRepository = stavkaRepository;
        this.korisnikRepository = korisnikRepository;
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

    //Vraca sve narudzbine zakazane za ovaj mesec koje nisu otkazane
    public List<Narudzbina> getAllThisMonth() {

        LocalDateTime danasnjiDatum = LocalDateTime.now();
        LocalDateTime pocetakMeseca = LocalDateTime.of(danasnjiDatum.getYear(), danasnjiDatum.getMonth(), 1, 0, 0);
        LocalDateTime krajMeseca = LocalDateTime.of(danasnjiDatum.getYear(), danasnjiDatum.getMonth(), danasnjiDatum.getMonth().maxLength(), 23, 59);

        return narudzbinaRepository.findByDatumBetween(pocetakMeseca, krajMeseca).stream().filter(narudzbina -> !narudzbina.isOtkazana()).toList();
    }

    //vraca sve narudzbine ciji korisnici sadrze deo stringa u korisnickom imenu
    public List<Narudzbina> getAllByKorisnickoIme(String korisnickoIme) {

        List<Korisnik> korisnici = korisnikRepository.findByKorisnickoImeContainingIgnoreCase(korisnickoIme);

        List<Narudzbina> narudzbine = null;
        for(Korisnik korisnik : korisnici) {
            List<Narudzbina> noveNarudzbine = narudzbinaRepository.findByKorisnik(korisnik);
            if (narudzbine == null) {
                narudzbine = noveNarudzbine;
            }
            else {
                narudzbine.addAll(noveNarudzbine);
            }
        }
        return narudzbine;
    }

    //vraca sve narudzbine ciji korisnik ima dati ID
    public List<Narudzbina> getAllByKorisnikID(String korisnikID) {

        Optional<Korisnik> korisnik = korisnikRepository.findById(Integer.parseInt(korisnikID));
        //IntelliJ predlozio umesto if/elsa koji sam napisao, cool skracenica koda
        return korisnik.map(narudzbinaRepository::findByKorisnik).orElse(null);
    }

}
