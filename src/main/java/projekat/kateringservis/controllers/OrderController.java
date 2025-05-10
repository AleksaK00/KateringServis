package projekat.kateringservis.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projekat.kateringservis.helperClasses.MessageSender;
import projekat.kateringservis.models.Korisnik;
import projekat.kateringservis.models.Narudzbina;
import projekat.kateringservis.models.Stavka;
import projekat.kateringservis.services.ArtikalService;
import projekat.kateringservis.services.KorisnikService;
import projekat.kateringservis.services.NarudzbinaService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Controller
@RequestMapping("/narudzbina")
public class OrderController {

    private final NarudzbinaService narudzbinaService;
    ArtikalService artikalService;
    KorisnikService korisnikService;

    @Autowired
    public OrderController(ArtikalService artikalService, KorisnikService korisnikService, NarudzbinaService narudzbinaService) {
        this.artikalService = artikalService;
        this.korisnikService = korisnikService;
        this.narudzbinaService = narudzbinaService;
    }

    //Metoda koja vraca pogled sa formom za unos adrese i datuma dostave, default ispis je adresa korisnickog naloga
    @GetMapping("/adresa")
    public String unesiAdresu(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        //Hvatanje ulogovanog korisnika i dodavanja u adrese u model
        Optional<Korisnik> korisnik = korisnikService.getByKorisnickoime(userDetails.getUsername());
        if (korisnik.isPresent()) {
            model.addAttribute("adresa", korisnik.get().getAdresa());
        }
        else {
            return "redirect:/login";
        }

        //dodavanje sutrasnjeg dana u model zarad minimalnog unosa datuma i lokala zarad ispisa datuma
        LocalDateTime sutra = LocalDateTime.now().plusDays(1);
        model.addAttribute("sutra", sutra);
        Locale serbianLatinLocale = new Locale.Builder().setLanguage("sr").setRegion("RS").setScript("Latn").build();
        LocaleContextHolder.setLocale(serbianLatinLocale);

        return "addressInput";
    }

    @PostMapping("/naruci")
    public String naruci(HttpSession sesija, @RequestParam String adresa, @RequestParam LocalDateTime datum, RedirectAttributes redirectAttributes,
                         @AuthenticationPrincipal UserDetails userDetails) {

        //Validacija
        if (adresa.isEmpty() || datum.isBefore(LocalDateTime.now().plusDays(1))) {
            redirectAttributes.addFlashAttribute("greska", "Sva polja moraju biti popunjena!");
            return "redirect:/narudzbina/adresa";
        }
        if (datum.isAfter(LocalDateTime.now().plusDays(60))) {
            redirectAttributes.addFlashAttribute("greska", "Ne primamo narudžbine proizvoda više od 60 dana unapred! Pogledajte proslave za zakazivanje velikih događaja.");
            return "redirect:/narudzbina/adresa";
        }
        LocalTime vreme = datum.toLocalTime();
        if (vreme.isBefore(LocalTime.of(8, 0)) || vreme.isAfter(LocalTime.of(22, 0)) ) {
            redirectAttributes.addFlashAttribute("greska", "Dostava je dostupna od 08:00 do 22:00!");
            return "redirect:/narudzbina/adresa";
        }

        //Provera da li je korisnik ulogovan
        Optional<Korisnik> korisnik = korisnikService.getByKorisnickoime(userDetails.getUsername());
        if (korisnik.isEmpty()) {
            return "redirect:/login";
        }

        //Hvatanje sesije i provera da li je korpa prazna
        @SuppressWarnings("unchecked")
        List<Stavka> korpa = (List<Stavka>)sesija.getAttribute("korpa");
        if (korpa == null) {
            MessageSender.redirektPoruka(redirectAttributes, "Vaša korpa je prazna!", "Pogledajte meni", "/meni/proizvodi");
            return "redirect:/obavestenje";
        }

        //Dodavanje narudzbine u bazi i brisanje sesije
        Narudzbina narudzbina = narudzbinaService.kreirajNarudzbinu(korpa, adresa, datum, korisnik.get());
        if (narudzbina == null) {
            MessageSender.redirektPoruka(redirectAttributes, "Greška u kreiranju narudžbine", "Nazad na početnu", "/");
            return "redirect:/obavestenje";
        }
        sesija.removeAttribute("korpa");

        //Ispis poruke o uspehu
        MessageSender.redirektPoruka(redirectAttributes, "Uspešno ste kreirali narudžbinu!", "pogledajte vaše narudžbine", "/korisnik/narudzbine");
        return "redirect:/obavestenje";
    }
}
