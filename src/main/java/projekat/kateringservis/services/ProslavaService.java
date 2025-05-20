package projekat.kateringservis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projekat.kateringservis.models.Korisnik;
import projekat.kateringservis.models.Proslava;
import projekat.kateringservis.repositories.KorisnikRepository;
import projekat.kateringservis.repositories.ProslavaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProslavaService {

    private final ProslavaRepository proslavaRepository;
    private final KorisnikRepository korisnikRepository;

    @Autowired
    public ProslavaService(ProslavaRepository proslavaRepository, KorisnikRepository korisnikRepository) {
        this.proslavaRepository = proslavaRepository;
        this.korisnikRepository = korisnikRepository;
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

    //Metoda koja azurira odgovarajuce polje neprocitanaPoruka
    public void skiniNeprocitanuPoruku(Proslava proslava, Korisnik korisnik) {

        if (Objects.equals(korisnik.getUloga(), "KORISNIK")) {
            proslava.setNeprocitanaPorukaKorisnik(false);
        }
        else {
            proslava.setNeprocitanaPorukaMenadzer(false);
        }

        try {
            proslavaRepository.save(proslava);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Metoda za hvatanje proslave po id-u
    public Optional<Proslava> getById(int id) { return proslavaRepository.findById(id); }

    //Vraca sve proslave zakazane za ovaj mesec
    public List<Proslava> getAllThisMonth() {

        LocalDateTime danasnjiDatum = LocalDateTime.now();
        LocalDateTime pocetakMeseca = LocalDateTime.of(danasnjiDatum.getYear(), danasnjiDatum.getMonth(), 1, 0, 0);
        LocalDateTime krajMeseca = LocalDateTime.of(danasnjiDatum.getYear(), danasnjiDatum.getMonth(), danasnjiDatum.getMonth().maxLength(), 23, 59);

        return proslavaRepository.findByDatumBetween(pocetakMeseca, krajMeseca).stream().filter(proslava -> !proslava.isOtkazana()).toList();
    }

    //Metoda vraca sve proslave iz baze
    public List<Proslava> getAll() { return proslavaRepository.findAll();}

    //Metoda hvata sve proslave ciji korisnik ima deo datog stringa u korisnickom imenu
    public List<Proslava> getAllByKorisnickoIme(String korisnickoIme) {

        List<Korisnik> korisnici = korisnikRepository.findByKorisnickoImeContainingIgnoreCase(korisnickoIme);

        List<Proslava> proslave = null;
        for(Korisnik korisnik : korisnici) {
            List<Proslava> noveProslave = proslavaRepository.findByKorisnik(korisnik);
            if (proslave == null) {
                proslave = noveProslave;
            }
            else {
                proslave.addAll(noveProslave);
            }
        }
        return proslave;
    }

    //Metoda hvata sve proslave korisnika sa datim id-om za pretragu
    public List<Proslava> getAllByKorisnikID(String korisnikID) {

        Optional<Korisnik> korisnik = korisnikRepository.findById(Integer.parseInt(korisnikID));
        //IntelliJ predlozio umesto if/elsa koji sam napisao, cool skracenica koda
        return korisnik.map(proslavaRepository::findByKorisnik).orElse(null);
    }
}
