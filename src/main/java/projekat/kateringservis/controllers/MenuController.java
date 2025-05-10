package projekat.kateringservis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import projekat.kateringservis.models.Artikal;
import projekat.kateringservis.services.ArtikalService;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/meni")
public class MenuController {

    ArtikalService artikalService;

    @Autowired
    public MenuController(ArtikalService artikalService) {
        this.artikalService = artikalService;
    }

    //Metoda koja vraca pogled sa meniom za narucivanje artikala.
    @GetMapping("/proizvodi")
    public String prikaziMeni(Model model) {

        //Hvatanja slanih i slatkih artikala i stavljanje u model
        List<Artikal> slaniMeni = artikalService.getByCategory("SLANO");
        List<Artikal> slatkiMeni = artikalService.getByCategory("SLATKO");
        model.addAttribute("slaniMeni", slaniMeni);
        model.addAttribute("slatkiMeni", slatkiMeni);

        //Postavljanje lokala na srpski za formatiranje valute
        Locale serbianLatinLocale = new Locale.Builder().setLanguage("sr").setRegion("RS").setScript("Latn").build();
        LocaleContextHolder.setLocale(serbianLatinLocale);

        return "menu";
    }

    //Metoda koja vraca pogled sa meniom za narucivanje setova
    @GetMapping("setovi")
    public String prikaziSetove(Model model)
    {
        //Hvatanja setova i stavljanje u model
        List<Artikal> setoviMeni = artikalService.getByCategory("SET");
        model.addAttribute("setoviMeni", setoviMeni);

        //Postavljanje lokala na srpski za formatiranje valute
        Locale serbianLatinLocale = new Locale.Builder().setLanguage("sr").setRegion("RS").setScript("Latn").build();
        LocaleContextHolder.setLocale(serbianLatinLocale);

        return "menuSetovi";
    }
}
