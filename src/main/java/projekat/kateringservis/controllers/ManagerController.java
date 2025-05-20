package projekat.kateringservis.controllers;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projekat.kateringservis.helperClasses.MessageSender;
import projekat.kateringservis.helperClasses.PrijavljeniKorisnikController;
import projekat.kateringservis.models.Narudzbina;
import projekat.kateringservis.models.Proslava;
import projekat.kateringservis.services.KorisnikService;
import projekat.kateringservis.services.NarudzbinaService;
import projekat.kateringservis.services.ProslavaService;

import java.util.List;
import java.util.Locale;

@RequestMapping("/menadzer")
@Controller
public class ManagerController extends PrijavljeniKorisnikController {

    private final NarudzbinaService narudzbinaService;

    protected ManagerController(KorisnikService korisnikService, ProslavaService proslavaService, NarudzbinaService narudzbinaService) {
        super(korisnikService, proslavaService);
        this.narudzbinaService = narudzbinaService;
    }

    //Metoda koja prikazuje menadzer panel sa nekim osnovnim biznis informacijama
    @GetMapping("/panel")
    public String prikaziMenadzerPanel(Model model) {

        //Izracunavanje broja narudzbina ovog meseca
        List<Narudzbina> narudzbine = narudzbinaService.getAllThisMonth();
        int brojNarudzbina = narudzbine.size();
        model.addAttribute("brojNarudzbina", brojNarudzbina);

        //Izracunavanje broja proslava ovog meseca
        List<Proslava> proslava = proslavaService.getAllThisMonth();
        int brojProslava = proslava.size();
        model.addAttribute("brojProslava", brojProslava);

        //Izracunavanje prihoda ovog meseca i postavljanje lokala zarad ispisa valute
        double prihod = 0;
        for (Narudzbina narudzbina : narudzbine) {
            prihod += narudzbina.getCena();
        }
        for (Proslava proslava1 : proslava) {
            prihod += proslava1.getCena();
        }
        Locale serbianLatinLocale = new Locale.Builder().setLanguage("sr").setRegion("RS").setScript("Latn").build();
        LocaleContextHolder.setLocale(serbianLatinLocale);
        model.addAttribute("prihod", prihod);

        return "menadzerPanel";
    }

    //Metoda za prikaz pogleda sa svim narudzbinama
    @GetMapping("/narudzbine")
    public String prikaziNarudzbine(Model model) {

        //Hvatanje svih narudzbina i stavljanje u model, postavljanje lokala
        List<Narudzbina> narudzbine = narudzbinaService.getAll();
        model.addAttribute("narudzbine", narudzbine);
        Locale serbianLatinLocale = new Locale.Builder().setLanguage("sr").setRegion("RS").setScript("Latn").build();
        LocaleContextHolder.setLocale(serbianLatinLocale);

        return "menadzerNarudzbine";
    }

    //Metoda za otkazivanje narudzbine od strane menadzera
    @PostMapping("/otkaziNarudzbinu/{id}")
    public String otkaziNarudzbinu(@PathVariable int id, RedirectAttributes redirectAttributes) {

        //Otkazivanje narudzbine i provera uspeha
        boolean uspeh = narudzbinaService.cancelOrder(id);
        if (!uspeh) {
            MessageSender.redirektPoruka(redirectAttributes, "Greška pri otkazivanju narudbine!", "Nazad", "/menadzer/narudzbine");
            return "redirect:/obavestenje";
        }
        MessageSender.redirektPoruka(redirectAttributes, "Narudžbina uspešno otkazana!", "Nazad", "/menadzer/narudzbine");
        return "redirect:/obavestenje";
    }

    //Metoda za pretragu narudzbina po korisniku ili ID-u
    @PostMapping("/pretragaNarudzbina")
    public String pretragaNarudzbina(Model model, @RequestParam String pretraga, @RequestParam String tipPretrage) {

        //Ako je string prazan vraca sve narudzbina(Reset pretrage)
        if (pretraga.isEmpty()) {
            return "redirect:/menadzer/narudzbine";
        }

        //Pretraga po izabranom tipu i stavljanje u model
        List<Narudzbina> narudzbine = null;
        if (tipPretrage.equals("korisnickoIme")) {
            narudzbine = narudzbinaService.getAllByKorisnickoIme(pretraga);
        }
        else {
            narudzbine = narudzbinaService.getAllByKorisnikID(pretraga);
        }
        model.addAttribute("narudzbine", narudzbine);
        return "menadzerNarudzbine";
    }

    //Metoda za prikaz pogleda sa svim proslavama
    @GetMapping("/proslave")
    public String prikaziProslave(Model model) {

        //Hvatanje svih narudzbina i stavljanje u model, postavljanje lokala
        List<Proslava> proslave = proslavaService.getAll();
        model.addAttribute("proslave", proslave);
        Locale serbianLatinLocale = new Locale.Builder().setLanguage("sr").setRegion("RS").setScript("Latn").build();
        LocaleContextHolder.setLocale(serbianLatinLocale);

        return "menadzerProslave";
    }

    //Metoda za pretragu proslava po korisniku ili ID-u
    @PostMapping("/pretragaProslava")
    public String pretragaProslava(Model model, @RequestParam String pretraga, @RequestParam String tipPretrage) {

        //Ako je string prazan vraca sve proslave(Reset pretrage)
        if (pretraga.isEmpty()) {
            return "redirect:/menadzer/proslave";
        }

        //Pretraga po izabranom tipu i stavljanje u model
        List<Proslava> proslave = null;
        if (tipPretrage.equals("korisnickoIme")) {
            proslave = proslavaService.getAllByKorisnickoIme(pretraga);
        }
        else {
            proslave = proslavaService.getAllByKorisnikID(pretraga);
        }
        model.addAttribute("proslave", proslave);
        return "menadzerProslave";
    }

    @PostMapping("/otkaziProslavu/{id}")
    public String otkaziProslavu(@PathVariable int id, RedirectAttributes redirectAttributes) {

        boolean uspeh = proslavaService.cancelOrder(id);
        if (!uspeh) {
            MessageSender.redirektPoruka(redirectAttributes, "Greška pri otkazivanju proslave!", "Nazad", "/menadzer/proslave");
            return "redirect:/obavestenje";
        }
        MessageSender.redirektPoruka(redirectAttributes, "Proslava uspešno otkazana!", "Nazad", "/menadzer/proslave");
        return "redirect:/obavestenje";
    }
}
