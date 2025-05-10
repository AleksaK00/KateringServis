package projekat.kateringservis.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import projekat.kateringservis.models.Artikal;
import projekat.kateringservis.models.Stavka;
import projekat.kateringservis.services.ArtikalService;

import java.util.*;

@Controller
@RequestMapping("/korpa")
public class CartController {

    ArtikalService artikalService;

    @Autowired
    public CartController(ArtikalService artikalService) {
        this.artikalService = artikalService;
    }

    //Metoda koja dodaje artikal u korpu
    @PostMapping("/dodaj")
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
    @GetMapping
    public String prikaziKorpu(HttpSession sesija, Model model) {

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

        //Postavljanje lokala na srpski za formatiranje valute
        Locale serbianLatinLocale = new Locale.Builder().setLanguage("sr").setRegion("RS").setScript("Latn").build();
        LocaleContextHolder.setLocale(serbianLatinLocale);

        return "checkout";
    }

    //Metoda za smanjivanje kolicine artikala iz korpe
    @PostMapping("/smanji")
    public String smanjiKolicinu(HttpSession sesija, @RequestParam int artikalId) {

        //Hvatanje sesije
        @SuppressWarnings("unchecked")
        List<Stavka> korpa = (List<Stavka>)sesija.getAttribute("korpa");

        //Provera da li je korpa prazna i smanjivanje kolicine ako nije
        if (korpa != null) {

            Optional<Stavka> stavka = korpa.stream().filter(s -> s.getArtikal().getId() == artikalId).findFirst();
            if (stavka.isPresent()) {
                if (stavka.get().getKolicina() > 1) {
                    stavka.get().setKolicina(stavka.get().getKolicina() - 1);
                }
                else {
                    //Uklanjajne artikla iz korpe ako se smanjuje sa jedinice
                    korpa.remove(stavka.get());
                    if (korpa.isEmpty()) {
                        sesija.removeAttribute("korpa");
                    }
                }
            }
            //Moguce ako se pozove samo direktan link za smanjivanje dok je sesija prazna, vracanje na meni stranicu
            else {
                return "redirect:/meni/proizvodi";
            }
        }

        //Redirekt na prikaz korpe
        return "redirect:/korpa";
    }

    //Metoda za povecanje kolicine artikala iz korpe
    @PostMapping("/povecaj")
    public String povecajKolicinu(HttpSession sesija, @RequestParam int artikalId) {

        //Hvatanje sesije
        @SuppressWarnings("unchecked")
        List<Stavka> korpa = (List<Stavka>)sesija.getAttribute("korpa");

        //Provera da li je korpa prazna i povecanje kolicine ako nije
        if (korpa != null) {

            Optional<Stavka> stavka = korpa.stream().filter(s -> s.getArtikal().getId() == artikalId).findFirst();
            if (stavka.isPresent()) {
                stavka.get().setKolicina(stavka.get().getKolicina() + 1);
            }
            //Moguce ako se pozove samo direktan link za povecanje dok je sesija prazna, vracanje na meni stranicu
            else {
                return "redirect:/meni/proizvodi";
            }
        }

        //Redirekt na prikaz korpe
        return "redirect:/korpa";
    }

    @PostMapping("/ukloni")
    public String ukloniArtikal(HttpSession sesija, @RequestParam int artikalId) {

        //Hvatanje sesije
        @SuppressWarnings("unchecked")
        List<Stavka> korpa = (List<Stavka>)sesija.getAttribute("korpa");

        //Provera da li je korpa prazna i uklanjanje ako nije
        if (korpa != null) {

            Optional<Stavka> stavka = korpa.stream().filter(s -> s.getArtikal().getId() == artikalId).findFirst();

            if (stavka.isPresent()) {
                korpa.remove(stavka.get());

                //Brisanje sesije ako je korpa prazna
                if (korpa.isEmpty()) {
                    sesija.removeAttribute("korpa");
                }
            }
            else {
                return "redirect:/meni/prizvodi";
            }
        }

        return "redirect:/korpa";
    }
}
