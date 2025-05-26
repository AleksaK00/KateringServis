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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projekat.kateringservis.models.Korisnik;
import projekat.kateringservis.services.KorisnikService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public String izvrsiRegistraciju(@ModelAttribute Korisnik korisnik, @RequestParam String passwordConfirm, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        //Validacija praznih polja
        if (korisnik.getKorisnickoIme().isEmpty() || korisnik.getSifra().isEmpty() || korisnik.getEmail().isEmpty() || korisnik.getIme().isEmpty() || korisnik.getPrezime().isEmpty()
                || korisnik.getAdresa().isEmpty() || passwordConfirm.isEmpty()) {
            redirectAttributes.addFlashAttribute("greska", "Sva polja moraju biti popunjena!");
            return "redirect:/register";
        }

        //Validacija potvrde lozinke
        if (!korisnik.getSifra().equals(passwordConfirm))
        {
            redirectAttributes.addFlashAttribute("greska", "Lozinke se ne poklapaju!");
            return "redirect:/register";
        }

        //Validacija email-a
        Pattern pattern = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,6}$");
        Matcher matcher = pattern.matcher(korisnik.getEmail());
        if (!matcher.matches()) {
            redirectAttributes.addFlashAttribute("greska", "Email nije validan!");
            return "redirect:/register";
        }

        //Validacija jacine lozinke
        pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$");
        matcher = pattern.matcher(korisnik.getSifra());
        if (!matcher.matches()) {
            redirectAttributes.addFlashAttribute("greska", "Lozinka mora da ima minimum osam karaktera, bar jedno veliko, jedno malo slovo i broj!");
            return "redirect:/register";
        }

        //Dodavanje korisnika u bazu podataka ukoliko je prosla validacija, izbacivanje greske ako ime ili email vec postoji u bazi
        String sifra = korisnik.getSifra();
        korisnik.setUloga("KORISNIK");
        String uspeh = korisnikService.registrujKorisnika(korisnik);
        if (uspeh.equals("uspeh")) {
            try {
                request.login(korisnik.getKorisnickoIme(), sifra);
            } catch (ServletException e) {
                return "redirect:/register";
            }
        }
        else {
            if (uspeh.equals("korisnickoIme")) {
                redirectAttributes.addFlashAttribute("greska", "Korisničko ime vec postoji!");
                return "redirect:/register";
            }
            if (uspeh.equals("email")) {
                redirectAttributes.addFlashAttribute("greska", "Email ime vec postoji!");
                return "redirect:/register";
            }
        }

        return "redirect:/";
    }
}
