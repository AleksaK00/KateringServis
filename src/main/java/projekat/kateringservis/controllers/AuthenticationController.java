package projekat.kateringservis.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekat.kateringservis.models.Korisnik;
import projekat.kateringservis.services.KorisnikService;

@Controller
public class AuthenticationController {

    private final KorisnikService korisnikService;

    @Autowired
    public AuthenticationController(KorisnikService korisnikService)
    {
        this.korisnikService = korisnikService;
    }

    //Metoda koja vraca pogled sa formom za prijavu
    @GetMapping("/login")
    public String login() {

        return "login";
    }

    //Metoda koja vraca pogled sa formom za registraciju
    @GetMapping("/register")
    public String register(Model model) {

        model.addAttribute("korisnik", new Korisnik());
        return "register";
    }

    //Metoda koja izvrsava registraciju
    @PostMapping("/register")
    public String izvrsiRegistraciju(@ModelAttribute Korisnik korisnik, @RequestParam String passwordConfirm, HttpServletRequest request) {

        //Dodaj validaciju ovde

        //Dodavanje korisnika u bazu podataka ukoliko je prosla validacija
        String sifra = korisnik.getSifra();
        korisnikService.registrujKorisnika(korisnik);
        try {
            request.login(korisnik.getKorisnickoIme(), sifra);
        } catch (ServletException e) {
            return "redirect:/register";
        }

        return "redirect:/";
    }
}
