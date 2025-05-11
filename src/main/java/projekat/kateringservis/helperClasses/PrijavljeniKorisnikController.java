package projekat.kateringservis.helperClasses;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import projekat.kateringservis.models.Korisnik;
import projekat.kateringservis.models.Proslava;
import projekat.kateringservis.services.KorisnikService;
import projekat.kateringservis.services.ProslavaService;

import java.util.List;

//Abstraktna klasa koja sadrzi metode koje su potrebne svim kontrolerima za upravljanje nalozima(admin, menadzer, korisnik)
public abstract class PrijavljeniKorisnikController {

    protected final KorisnikService korisnikService;
    protected final ProslavaService proslavaService;

    protected PrijavljeniKorisnikController(KorisnikService korisnikService, ProslavaService proslavaService) {
        this.proslavaService = proslavaService;
        this.korisnikService = korisnikService;
    }

    //Metoda vraca prijavljenog korisnika
    protected Korisnik trenutnoPrijavljen(@AuthenticationPrincipal UserDetails userDetails) {

        return korisnikService.getByKorisnickoime(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Nema prijavljenog korisnika!"));
    }

    //Vraca informaciju da li je korisnik vlasnik proslave
    protected boolean jeVlasnikProslave(Korisnik korisnik, int id) {

        List<Proslava> proslave = proslavaService.getByKorisnik(korisnik);
        return proslave.stream().anyMatch(p -> p.getId() == id);
    }
}
