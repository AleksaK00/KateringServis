package projekat.kateringservis.controllers;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
