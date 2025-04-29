package projekat.kateringservis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import projekat.kateringservis.models.Artikal;
import projekat.kateringservis.services.ArtikalService;

import java.util.List;

@Controller
public class OrderController {

    ArtikalService artikalService;

    @Autowired
    public OrderController(ArtikalService artikalService) {
        this.artikalService = artikalService;
    }

    //Metoda koja vraca pogled sa meniom za narucivanje artikala.
    @GetMapping("/meni/proizvodi")
    public String prikaziMeni(Model model) {

        //Hvatanja slanih i slatkih artikala i stavljanje u model
        List<Artikal> slaniMeni = artikalService.getByCategory("SLANO");
        List<Artikal> slatkiMeni = artikalService.getByCategory("SLATKO");
        model.addAttribute("slaniMeni", slaniMeni);
        model.addAttribute("slatkiMeni", slatkiMeni);

        return "menu";
    }

    //Metoda koja vraca pogled sa meniom za narucivanje setova
    @GetMapping("/meni/setovi")
    public String prikaziSetove(Model model)
    {
        //Hvatanja setova i stavljanje u model
        List<Artikal> setoviMeni = artikalService.getByCategory("SET");
        model.addAttribute("setoviMeni", setoviMeni);

        return "menuSetovi";
    }
}
