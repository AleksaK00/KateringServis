package projekat.kateringservis.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.LocaleResolver;
import projekat.kateringservis.models.Artikal;
import projekat.kateringservis.models.Stavka;
import projekat.kateringservis.services.ArtikalService;

import java.util.*;

@Controller
public class OrderController {

    ArtikalService artikalService;
    LocaleResolver localeResolver;

    @Autowired
    public OrderController(ArtikalService artikalService, LocaleResolver localeResolver) {
        this.artikalService = artikalService;
        this.localeResolver = localeResolver;
    }

    //Metoda koja vraca pogled sa meniom za narucivanje artikala.
    @GetMapping("/meni/proizvodi")
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
    @GetMapping("/meni/setovi")
    public String prikaziSetove(Model model, HttpServletRequest request, HttpServletResponse response)
    {
        //Hvatanja setova i stavljanje u model
        List<Artikal> setoviMeni = artikalService.getByCategory("SET");
        model.addAttribute("setoviMeni", setoviMeni);

        //Postavljanje lokala na srpski zarad formatiranja valute
        localeResolver.setLocale(request, response, new Locale("sr", "RS", "Latn"));

        return "menuSetovi";
    }

    //Metoda koja dodaje artikal u korpu
    @PostMapping("/korpa/dodaj")
    ResponseEntity<Map<String, Object>> dodajuKorpu(HttpSession sesija, @RequestBody Stavka noviArtikal) {

        //Hvatanje korpe iz sesije i provera da li je prazna
        @SuppressWarnings("unchecked")
        List<Stavka> korpa = (List<Stavka>)sesija.getAttribute("korpa");
        if (korpa == null) {
            korpa = new ArrayList<>();
        }

        //Hvatanje artikla na osnovu id-a
        Optional<Artikal> artikal = artikalService.getById(noviArtikal.getArtikal().getId());
        if (artikal.isEmpty()) {
            throw new IllegalArgumentException("Artikal sa id-om " + noviArtikal.getArtikal().getId() + " ne postoji!");
        }

        //Provera da li stavka/artikal vec postoji u korpi. Povecanje kolicine ako postoji, dodavanje ako ne postoji
        Optional<Stavka> stavkaPostoji = korpa.stream().filter(s -> s.getArtikal().getId() == noviArtikal.getArtikal().getId()).findFirst();
        if (stavkaPostoji.isPresent()) {
            stavkaPostoji.get().setKolicina(stavkaPostoji.get().getKolicina() + noviArtikal.getKolicina());
        }
        else {
            noviArtikal.setArtikal(artikal.get());
            korpa.add(noviArtikal);
            sesija.setAttribute("korpa", korpa);
        }

        //Stavljanje broja artikala i provere uspesnosti u odgovor
        Map<String, Object> response = new HashMap<>();
        response.put("uspeh", true);
        response.put("brojArtikala", korpa.size());

        return ResponseEntity.ok(response);
    }

    //Metoda koja vraca pogled sa artiklima u korpi i opcijom za narucivanje
    @GetMapping("/korpa")
    public String prikaziKorpu(HttpSession sesija, Model model, HttpServletRequest request, HttpServletResponse response) {

        //Dodavanje korpe iz sesije u model
        @SuppressWarnings("unchecked")
        List<Stavka> korpa = (List<Stavka>)sesija.getAttribute("korpa");
        model.addAttribute("korpa", korpa);

        //Dodavanje ukupne cene u model
        double ukupnaCena = 0;
        if (korpa != null) {
            for (Stavka stavka : korpa) {
                ukupnaCena += stavka.getArtikal().getCena() * stavka.getKolicina();
            }
        }
        model.addAttribute("ukupnaCena", ukupnaCena);

        //Postavljanje lokala na srpski zarad formatiranja valute
        localeResolver.setLocale(request, response, new Locale("sr", "RS", "Latn"));

        return "checkout";
    }
}
