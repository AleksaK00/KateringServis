package projekat.kateringservis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projekat.kateringservis.helperClasses.KorisnikDTO;
import projekat.kateringservis.helperClasses.MessageSender;
import projekat.kateringservis.helperClasses.PrijavljeniKorisnikController;
import projekat.kateringservis.models.Korisnik;
import projekat.kateringservis.models.Narudzbina;
import projekat.kateringservis.models.Proslava;
import projekat.kateringservis.services.KorisnikService;
import projekat.kateringservis.services.NarudzbinaService;
import projekat.kateringservis.services.ProslavaService;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/korisnik")
public class AccountController extends PrijavljeniKorisnikController {

    NarudzbinaService narudzbinaService;
    ProslavaService proslavaService;

    @Autowired
    public AccountController(KorisnikService korisnikService, NarudzbinaService narudzbinaService, ProslavaService proslavaService) {
        super(korisnikService);
        this.narudzbinaService = narudzbinaService;
        this.proslavaService = proslavaService;
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
        List<Proslava> proveraProslave = proslavaService.getByKorisnik(korisnik);
        if (proveraProslave.stream().noneMatch(p -> p.getKorisnik().getId() == korisnik.getId())) {
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
}
