package projekat.kateringservis.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;
import projekat.kateringservis.models.Artikal;
import projekat.kateringservis.services.ArtikalService;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/meni")
public class MenuController {

    ArtikalService artikalService;
    LocaleResolver localeResolver;

    @Autowired
    public MenuController(ArtikalService artikalService, LocaleResolver localeResolver) {
        this.artikalService = artikalService;
        this.localeResolver = localeResolver;
    }

    //Metoda koja vraca pogled sa meniom za narucivanje artikala.
    @GetMapping("/proizvodi")
    public String prikaziMeni(Model model, HttpServletRequest request, HttpServletResponse response) {

        //Hvatanja slanih i slatkih artikala i stavljanje u model
        List<Artikal> slaniMeni = artikalService.getByCategory("SLANO");
        List<Artikal> slatkiMeni = artikalService.getByCategory("SLATKO");
        model.addAttribute("slaniMeni", slaniMeni);
        model.addAttribute("slatkiMeni", slatkiMeni);

        //Postavljanje lokala na srpski zarad formatiranja valute
        localeResolver.setLocale(request, response, new Locale("sr", "RS", "Latn"));

        return "menu";
    }

    //Metoda koja vraca pogled sa meniom za narucivanje setova
    @GetMapping("setovi")
    public String prikaziSetove(Model model, HttpServletRequest request, HttpServletResponse response)
    {
        //Hvatanja setova i stavljanje u model
        List<Artikal> setoviMeni = artikalService.getByCategory("SET");
        model.addAttribute("setoviMeni", setoviMeni);

        //Postavljanje lokala na srpski zarad formatiranja valute
        localeResolver.setLocale(request, response, new Locale("sr", "RS", "Latn"));

        return "menuSetovi";
    }
}
