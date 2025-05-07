package projekat.kateringservis.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;
import projekat.kateringservis.helperClasses.KorisnikDTO;
import projekat.kateringservis.helperClasses.PrijavljeniKorisnikController;
import projekat.kateringservis.models.Korisnik;
import projekat.kateringservis.models.Narudzbina;
import projekat.kateringservis.services.KorisnikService;
import projekat.kateringservis.services.NarudzbinaService;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/korisnik")
public class AccountController extends PrijavljeniKorisnikController {

    NarudzbinaService narudzbinaService;
    LocaleResolver localeResolver;

    @Autowired
    public AccountController(KorisnikService korisnikService, NarudzbinaService narudzbinaService, LocaleResolver localeResolver) {
        super(korisnikService);
        this.narudzbinaService = narudzbinaService;
        this.localeResolver = localeResolver;
    }

    //Metoda koja vraca pogled sa korisnickim profilom
    @GetMapping("/profil")
    public String profil(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        //Preuzimanje ulogovanog korisnika, stavljanje u data transfer objekat, i brojanje trenutnih narudzbina korisnika.
        Korisnik korisnik = trenutnoPrijavljen(userDetails);
        KorisnikDTO korisnikDTO = new KorisnikDTO(korisnik);
        List<Narudzbina> narudzbine = narudzbinaService.getByKorisnik(korisnik);

        model.addAttribute("korisnik", korisnikDTO);
        model.addAttribute("brojNarudzbina", narudzbine.size());
        return "korisnickiProfil";
    }

    //Metoda koja vraca pogled za pregled narudzbina
    @GetMapping("/narudzbine")
    public String narudzbine(@AuthenticationPrincipal UserDetails userDetails, Model model, HttpServletRequest request, HttpServletResponse response) {

        //Preuzimanje ulogovanog korisnika, stavljanje u data transfer objekat, i hvatanje narudzbina korisnika.
        Korisnik korisnik = trenutnoPrijavljen(userDetails);
        KorisnikDTO korisnikDTO = new KorisnikDTO(korisnik);
        List<Narudzbina> narudzbine = narudzbinaService.getByKorisnik(korisnik);

        //Postavljanje lokala na srpski za formatiranje valute
        localeResolver.setLocale(request, response, new Locale("sr", "RS", "Latn"));

        //Model i pogled
        model.addAttribute("korisnik", korisnikDTO);
        model.addAttribute("narudzbine", narudzbine);
        return "pregledNarudzbina";
    }
}
