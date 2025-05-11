package projekat.kateringservis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projekat.kateringservis.helperClasses.KorisnikDTO;
import projekat.kateringservis.helperClasses.MessageSender;
import projekat.kateringservis.helperClasses.PrijavljeniKorisnikController;
import projekat.kateringservis.models.Korisnik;
import projekat.kateringservis.models.Narudzbina;
import projekat.kateringservis.models.Proslava;
import projekat.kateringservis.services.KorisnikService;
import projekat.kateringservis.services.NarudzbinaService;
import projekat.kateringservis.services.PorukaService;
import projekat.kateringservis.services.ProslavaService;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/korisnik")
public class AccountController extends PrijavljeniKorisnikController {

    NarudzbinaService narudzbinaService;
    PorukaService porukaService;

    @Autowired
    public AccountController(KorisnikService korisnikService, NarudzbinaService narudzbinaService, PorukaService porukaService, ProslavaService proslavaService) {
        super(korisnikService, proslavaService);
        this.narudzbinaService = narudzbinaService;
        this.porukaService = porukaService;
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
    public String narudzbine(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        //Preuzimanje ulogovanog korisnika, stavljanje u data transfer objekat, i hvatanje narudzbina korisnika.
        Korisnik korisnik = trenutnoPrijavljen(userDetails);
        KorisnikDTO korisnikDTO = new KorisnikDTO(korisnik);
        List<Narudzbina> narudzbine = narudzbinaService.getByKorisnik(korisnik);

        //Postavljanje lokala na srpski za formatiranje valute
        Locale serbianLatinLocale = new Locale.Builder().setLanguage("sr").setRegion("RS").setScript("Latn").build();
        LocaleContextHolder.setLocale(serbianLatinLocale);

        //Model i pogled
        model.addAttribute("korisnik", korisnikDTO);
        model.addAttribute("narudzbine", narudzbine);
        return "pregledNarudzbina";
    }

    //Metoda za otkazivanje narudzbine
    @PostMapping("/otkaziNarudzbinu/{id}")
    public String otkaziNarudzbinu(@PathVariable int id, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {

        //Provera da li je narudzbina od strane ulogovanog korisnika
        Korisnik korisnik = trenutnoPrijavljen(userDetails);
        List<Narudzbina> proveraNarudzbine = narudzbinaService.getByKorisnik(korisnik);
        if (proveraNarudzbine.stream().noneMatch(n -> n.getKorisnik().getId() == korisnik.getId())) {
            MessageSender.redirektPoruka(redirectAttributes, "Neautorizovan pristup!", "Nazad", "/korisnik/narudzbine");
            return "redirect:/obavestenje";
        }

        //Otkazivanje narudzbine i provera uspeha
        boolean uspeh = narudzbinaService.cancelOrder(id);
        if (!uspeh) {
            MessageSender.redirektPoruka(redirectAttributes, "Greška pri otkazivanju narudbine!", "Nazad", "/korisnik/narudzbine");
            return "redirect:/obavestenje";
        }

        MessageSender.redirektPoruka(redirectAttributes, "Narudžbina uspešno otkazana!", "Nazad", "/korisnik/narudzbine");
        return "redirect:/obavestenje";
    }

    //Metoda koja vraca pogled za pregled proslava
    @GetMapping("/proslave")
    public String proslave(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        //Preuzimanje ulogovanog korisnika, stavljanje u data transfer objekat, i hvatanje narudzbina korisnika.
        Korisnik korisnik = trenutnoPrijavljen(userDetails);
        KorisnikDTO korisnikDTO = new KorisnikDTO(korisnik);
        List<Proslava> proslave = proslavaService.getByKorisnik(korisnik);

        //Postavljanje lokala na srpski za formatiranje valute
        Locale serbianLatinLocale = new Locale.Builder().setLanguage("sr").setRegion("RS").setScript("Latn").build();
        LocaleContextHolder.setLocale(serbianLatinLocale);

        //Model i pogled
        model.addAttribute("korisnik", korisnikDTO);
        model.addAttribute("proslave", proslave);
        return "pregledProslava";
    }

    @PostMapping("/otkaziProslavu/{id}")
    public String otkaziProslavu(@PathVariable int id, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {

        //Provera da li proslava pripada ulogovanom korisnika
        Korisnik korisnik = trenutnoPrijavljen(userDetails);
        if (!jeVlasnikProslave(korisnik, id)) {
            MessageSender.redirektPoruka(redirectAttributes, "Neautorizovan pristup!", "Nazad", "/korisnik/proslave");
            return "redirect:/obavestenje";
        }

        //Otkazivanje proslave i provera uspeha
        boolean uspeh = proslavaService.cancelOrder(id);
        if (!uspeh) {
            MessageSender.redirektPoruka(redirectAttributes, "Greška pri otkazivanju proslave!", "Nazad", "/korisnik/proslave");
            return "redirect:/obavestenje";
        }

        MessageSender.redirektPoruka(redirectAttributes, "Proslava uspešno otkazana!", "Nazad", "/korisnik/proslave");
        return "redirect:/obavestenje";
    }

    @GetMapping("/proslava/{id}")
    public String detaljiProslave(@PathVariable int id, @AuthenticationPrincipal UserDetails userDetails, Model model, RedirectAttributes redirectAttributes) {

        //Provera da li proslava pripada ulogovanom korisnika
        Korisnik korisnik = trenutnoPrijavljen(userDetails);
        if (!jeVlasnikProslave(korisnik, id)) {
            MessageSender.redirektPoruka(redirectAttributes, "Neautorizovan pristup!", "Nazad", "/");
            return "redirect:/obavestenje";
        }

        //Hvatanje proslave i dodavanje u model
        Optional<Proslava> proslava = proslavaService.getById(id);
        if (proslava.isPresent()) {
            MessageSender.redirektPoruka(redirectAttributes, "Ne postojeća proslava!", "Nazad", "korisnik/proslave");
        }
        model.addAttribute("proslava", proslava.get());
        model.addAttribute("korisnik", new KorisnikDTO(korisnik));
        model.addAttribute("poruke", porukaService.getByProslava(proslava.get()));

        return "detaljiProslave";
    }

    @PostMapping("/proslava/{id}/posaljiPoruku")
    public String posaljiPoruku(@PathVariable int id, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes, @RequestParam String porukaTekst) {

        //Provera da li proslava pripada ulogovanom korisnika
        Korisnik korisnik = trenutnoPrijavljen(userDetails);
        if (!jeVlasnikProslave(korisnik, id)) {
            MessageSender.redirektPoruka(redirectAttributes, "Neautorizovan pristup!", "Nazad", "/");
            return "redirect:/obavestenje";
        }

        //Validacija
        if (porukaTekst.isEmpty()) {
            MessageSender.redirektPoruka(redirectAttributes, "Ne možete slati praznu poruku", "Nazad", "korisnik/proslave/" + id);
        }
        Optional<Proslava> proslava = proslavaService.getById(id);
        if (proslava.isPresent()) {
            MessageSender.redirektPoruka(redirectAttributes, "Ne postojeća proslava!", "Nazad", "korisnik/proslave");
        }

        //Dodavanje u bazu
        porukaService.kreirajPoruku(proslava.get(), korisnik, porukaTekst.trim());
        return "redirect:/korisnik/proslava/" + id;
    }
}
