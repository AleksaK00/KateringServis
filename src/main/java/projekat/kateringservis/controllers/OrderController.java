package projekat.kateringservis.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projekat.kateringservis.models.Korisnik;
import projekat.kateringservis.models.Narudzbina;
import projekat.kateringservis.models.Stavka;
import projekat.kateringservis.services.ArtikalService;
import projekat.kateringservis.services.KorisnikService;
import projekat.kateringservis.services.NarudzbinaService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Controller
@RequestMapping("/narudzbina")
public class OrderController {

    private final NarudzbinaService narudzbinaService;
    ArtikalService artikalService;
    LocaleResolver localeResolver;
    KorisnikService korisnikService;

    @Autowired
    public OrderController(ArtikalService artikalService, LocaleResolver localeResolver, KorisnikService korisnikService, NarudzbinaService narudzbinaService) {
        this.artikalService = artikalService;
        this.korisnikService = korisnikService;
        this.localeResolver = localeResolver;
        this.narudzbinaService = narudzbinaService;
    }

    //Metoda koja vraca pogled sa formom za unos adrese i datuma dostave, default ispis je adresa korisnickog naloga
    @GetMapping("/adresa")
    public String unesiAdresu(Model model, @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {

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
        localeResolver.setLocale(request, response, new Locale("sr", "RS", "Latn"));

        return "addressInput";
    }

    @PostMapping("/naruci")
    public String naruci(HttpSession sesija, @RequestParam String adresa, @RequestParam LocalDateTime datum, RedirectAttributes redirectAttributes, Model model,
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
            redirectAttributes.addFlashAttribute("poruka", "Vaša korpa je prazna!");
            redirectAttributes.addFlashAttribute("tekstDugme", "Pogledajte meni");
            redirectAttributes.addFlashAttribute("linkDugme", "/meni/proizvodi");
            return "redirect:/obavestenje";
        }

        //Dodavanje narudzbine u bazi i brisanje sesije
        Narudzbina narudzbina = narudzbinaService.kreirajNarudzbinu(korpa, adresa, datum, korisnik.get());
        if (narudzbina == null) {
            redirectAttributes.addFlashAttribute("poruka", "Greška u kreiranju narudžbine");
            redirectAttributes.addFlashAttribute("tekstDugme", "Nazad na početnu");
            redirectAttributes.addFlashAttribute("linkDugme", "/");
            return "redirect:/obavestenje";
        }
        sesija.removeAttribute("korpa");

        //Ispis poruke o uspehu
        redirectAttributes.addFlashAttribute("poruka", "Uspešno ste kreirali narudžbinu!");
        redirectAttributes.addFlashAttribute("tekstDugme", "pogledajte vaše narudžbine");
        redirectAttributes.addFlashAttribute("linkDugme", "/"); //Promeni kasnije da vodi ka stranici za pregled narudžbina
        return "redirect:/obavestenje";
    }
}
