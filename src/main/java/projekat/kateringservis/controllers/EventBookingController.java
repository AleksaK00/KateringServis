package projekat.kateringservis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projekat.kateringservis.helperClasses.MessageSender;
import projekat.kateringservis.models.Korisnik;
import projekat.kateringservis.services.ArtikalService;
import projekat.kateringservis.services.KorisnikService;
import projekat.kateringservis.services.ProslavaService;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/proslave")
public class EventBookingController {

    private final ArtikalService artikalService;
    private final KorisnikService korisnikService;
    private final ProslavaService proslavaService;

    @Autowired
    public EventBookingController(ArtikalService artikalService, KorisnikService korisnikService, ProslavaService proslavaService) {
        this.artikalService = artikalService;
        this.korisnikService = korisnikService;
        this.proslavaService = proslavaService;
    }

    //Vraca pogled za zakazivanje proslava, i hvata trenutnu cenu po osobi
    @GetMapping
    public String proslave(Model model) {

        //Hvatanje cene i postaljanje lokala
        model.addAttribute("cena", artikalService.getPriceByPerson());
        Locale serbianLatinLocale = new Locale.Builder().setLanguage("sr").setRegion("RS").setScript("Latn").build();
        LocaleContextHolder.setLocale(serbianLatinLocale);

        return "proslave";
    }

    @PostMapping("/zakazi")
    public String zakaziProslavu(@RequestParam String adresa, @RequestParam LocalDateTime datum, @RequestParam int brOsoba, @RequestParam String napomena,
         RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails userDetails) {

        //Validacija
        if (adresa.isEmpty()) {
            redirectAttributes.addFlashAttribute("greska", "adresa je obavezna!");
            return "redirect:/proslave";
        }
        if (datum.isBefore(LocalDateTime.now().plusDays(1))) {
            redirectAttributes.addFlashAttribute("greska", "datum je obavezan i mora biti u budućnosti!");
            return "redirect:/proslave";
        }
        if (brOsoba < 20 || brOsoba > 200) {
            redirectAttributes.addFlashAttribute("greska", "minimalno 20, maksimalno 200 osoba!");
        }

        //Racunanje cene
        double cena = artikalService.getPriceByPerson();
        double ukupnaCena = cena * brOsoba;
        if (brOsoba >= 100) {
            ukupnaCena *= 0.9;
        }

        //Hvatanje ulugovanog korisnika i dodavanje proslave u bazu
        Optional<Korisnik> korisnik = korisnikService.getByKorisnickoime(userDetails.getUsername());
        if (korisnik.isEmpty()) {
            return "redirect:/login";
        }
        proslavaService.novaProslava(korisnik.get(), adresa, napomena, brOsoba, ukupnaCena, datum);

        //Ispis poruke o uspehu
        MessageSender.redirektPoruka(redirectAttributes, "Uspešno ste rezervisali proslavu! Razgovarajte sa menadžerom za detalje o proslavi.",
                "pogledajte vaše zakazane proslave", "/korisnik/proslave");
        return "redirect:/obavestenje";
    }
}
