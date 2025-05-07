package projekat.kateringservis.helperClasses;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import projekat.kateringservis.models.Korisnik;
import projekat.kateringservis.services.KorisnikService;

//Abstraktna klasa koja sadrzi metode koje su potrebne svim kontrolerima za upravljanje nalozima(admin, menadzer, korisnik)
public abstract class PrijavljeniKorisnikController {

    protected final KorisnikService korisnikService;

    protected PrijavljeniKorisnikController(KorisnikService korisnikService) {
        this.korisnikService = korisnikService;
    }

    //Metoda vraca prijavljenog korisnika
    protected Korisnik trenutnoPrijavljen(@AuthenticationPrincipal UserDetails userDetails) {

        return korisnikService.getByKorisnickoime(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Nema prijavljenog korisnika!"));
    }
}
