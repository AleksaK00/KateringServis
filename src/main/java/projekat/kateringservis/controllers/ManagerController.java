package projekat.kateringservis.controllers;

import jakarta.validation.Valid;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projekat.kateringservis.helperClasses.*;
import projekat.kateringservis.models.Artikal;
import projekat.kateringservis.models.Korisnik;
import projekat.kateringservis.models.Narudzbina;
import projekat.kateringservis.models.Proslava;
import projekat.kateringservis.services.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@RequestMapping("/menadzer")
@Controller
public class ManagerController extends PrijavljeniKorisnikController {

    private final NarudzbinaService narudzbinaService;
    private final PorukaService porukaService;
    private final ArtikalService artikalService;

    protected ManagerController(KorisnikService korisnikService, ProslavaService proslavaService, NarudzbinaService narudzbinaService,
                                PorukaService porukaService, ArtikalService artikalService) {
        super(korisnikService, proslavaService);
        this.narudzbinaService = narudzbinaService;
        this.porukaService = porukaService;
        this.artikalService = artikalService;
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

        //Hvatanje svih proslava i stavljanje u model, postavljanje lokala
        List<Proslava> proslave = proslavaService.getAll();
        model.addAttribute("proslave", proslave);
        model.addAttribute("cenaPoOsobi", artikalService.getPriceByPerson());
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

    //Metoda za otkazivanje proslave od strane menadzera
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

    //Metoda za prikaz detalja proslave
    @GetMapping("/proslava/{id}")
    public String prikaziProslavu(@PathVariable int id, Model model, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {

        Korisnik menadzer = trenutnoPrijavljen(userDetails);

        //Hvatanje proslave i dodavanje u model
        Optional<Proslava> proslava = proslavaService.getById(id);
        if (proslava.isPresent()) {
            MessageSender.redirektPoruka(redirectAttributes, "Ne postojeća proslava!", "Nazad", "korisnik/proslave");
        }
        model.addAttribute("proslava", proslava.get());
        model.addAttribute("korisnik", proslava.get().getKorisnik());
        model.addAttribute("poruke", porukaService.getByProslava(proslava.get()));

        //Postavljanje polja neprocitanaPorukaKorisnik na false
        proslavaService.skiniNeprocitanuPoruku(proslava.get(), menadzer);

        return "menadzerDetaljiProslave";
    }

    //Metoda za slanje poruke od strane menadzera
    @PostMapping("/proslava/{id}/posaljiPoruku")
    public String posaljiPoruku(@PathVariable int id, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes, @RequestParam String porukaTekst) {

        Korisnik menadzer = trenutnoPrijavljen(userDetails);

        //Validacija
        if (porukaTekst.isEmpty()) {
            MessageSender.redirektPoruka(redirectAttributes, "Ne možete slati praznu poruku", "Nazad", "menadzer/proslave/" + id);
        }
        Optional<Proslava> proslava = proslavaService.getById(id);
        if (proslava.isPresent()) {
            MessageSender.redirektPoruka(redirectAttributes, "Ne postojeća proslava!", "Nazad", "menadzer/proslave");
        }

        //Dodavanje u bazu
        porukaService.kreirajPoruku(proslava.get(), menadzer, porukaTekst.trim());
        return "redirect:/menadzer/proslava/" + id;
    }

    //Metoda za prikazivanje forme za izmenu proslave
    @GetMapping("/proslava/{id}/izmena")
    public String izmenaProslave(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {

        //provera da li proslava postoji
        Optional<Proslava> proslava = proslavaService.getById(id);
        if (proslava.isPresent()) {
            MessageSender.redirektPoruka(redirectAttributes, "Ne postojeća proslava!", "Nazad", "menadzer/proslave");
        }

        //Inicijalizacija DTO objekta sa trenutnim vrednostima proslave
        IzmenaProslaveDTO izmenaProslaveDTO = new IzmenaProslaveDTO();
        izmenaProslaveDTO.setAdresa(proslava.get().getAdresa());
        izmenaProslaveDTO.setDatum(proslava.get().getDatum().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        izmenaProslaveDTO.setNapomena(proslava.get().getNapomena());
        izmenaProslaveDTO.setBrOsoba(proslava.get().getBrGostiju());
        izmenaProslaveDTO.setCenaUnos(proslava.get().getCena());

        //Hvatanje cene po osobi, postaljanje lokala i dodavanje u model
        Locale serbianLatinLocale = new Locale.Builder().setLanguage("sr").setRegion("RS").setScript("Latn").build();
        LocaleContextHolder.setLocale(serbianLatinLocale);
        model.addAttribute("cena", artikalService.getPriceByPerson());
        model.addAttribute("izmenaProslaveDTO", izmenaProslaveDTO);
        model.addAttribute("proslava", proslava.get());

        return "menadzerIzmenaProslave";
    }

    //Obrada post requesta za izmenu proslave
    @PostMapping("/proslava/{id}/izmeni")
    public String izmeniProslavu(@PathVariable int id, Model model, @Valid @ModelAttribute IzmenaProslaveDTO izmenjenaProslava,
                                 BindingResult bindingResult) {

        //Validacija
        if (bindingResult.hasErrors()) {
            Optional<Proslava> proslava = proslavaService.getById(id);
            model.addAttribute("proslava", proslava.get());
            model.addAttribute("cena", artikalService.getPriceByPerson());
            return "menadzerIzmenaProslave";
        }

        proslavaService.updateProslava(izmenjenaProslava, id);
        return "redirect:/menadzer/proslava/" + id;
    }

    //Obrada post requesta za izmenu cene po osobi proslave
    @PostMapping("/proslava/izmenaCenePoOsobi")
    public String izmenaCenePoOsobi(@RequestParam double cenaPoOsobi, RedirectAttributes redirectAttributes) {

        //validacija
        if (cenaPoOsobi <= 0) {
            MessageSender.redirektPoruka(redirectAttributes, "Neispravna vrednost cene po osobi!", "Nazad", "menadzer/proslave");
            return "redirect:/obavestenje";
        }

        //Promena u bazi
        boolean uspeh = artikalService.updatePricePerPerson(cenaPoOsobi);
        if (!uspeh) {
            MessageSender.redirektPoruka(redirectAttributes, "Ne uspešno menjanje cene po osobi!", "Nazad", "menadzer/proslave");
            return "redirect:/obavestenje";
        }
        MessageSender.redirektPoruka(redirectAttributes, "Uspešno menjanje cene po osobi!", "Nazad", "menadzer/proslave");
        return "redirect:/obavestenje";
    }

    //Obrada get requesta za prikazivanje liste proizvoda za menadzera
    @GetMapping("/proizvodi")
    public String prikaziProizvode(Model model) {

        model.addAttribute("artikli", artikalService.getAll());
        Locale serbianLatinLocale = new Locale.Builder().setLanguage("sr").setRegion("RS").setScript("Latn").build();
        LocaleContextHolder.setLocale(serbianLatinLocale);
        return "menadzerProizvodi";
    }

    //Obrada get requesta za promenu stanja akcije proizvoda
    @GetMapping("/proizvodi/toggleAkcija/{id}")
    public String promeniStanjeAkcije(@PathVariable int id) {

        artikalService.toggleDiscount(id);
        return "redirect:/menadzer/proizvodi";
    }

    @GetMapping("/proizvodi/toggleProdaja/{id}")
    public String promeniStanjeProdaje(@PathVariable int id) {

        artikalService.toggleNaMeniju(id);
        return "redirect:/menadzer/proizvodi";
    }

    //Ispisivanje stranice za dodavanje proizvoda
    @GetMapping("/proizvodi/dodaj")
    public String prikaziFormuZaDodavanjeProizvoda(Model model) {

        model.addAttribute("artikalDTO", new ArtikalDTO());
        return "menadzerDodavanjeProizvoda";
    }

    //Izvrsavanje post requesta za dodavanje proizvoda
    @PostMapping("/proizvodi/dodaj")
    public String dodajProizvod(@Valid @ModelAttribute("artikalDTO") ArtikalDTO artikalDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                Model model) {

        //validacija
        if (bindingResult.hasErrors()) {
            return "menadzerDodavanjeProizvoda";
        }

        boolean uspeh = artikalService.addArtikal(artikalDTO);
        if (!uspeh) {
            MessageSender.redirektPoruka(redirectAttributes, "Neuspešno dodavanje proiyvoda", "Nazad", "menadzer/proizvodi/dodaj");
            return "redirect:/obavestenje";
        }

        MessageSender.redirektPoruka(redirectAttributes, "Proizvod je uspešno dodat!", "Nazad", "menadzer/proizvodi");
        return "redirect:/obavestenje";
    }

    //Ispisivanje stranice za promenu proizvoda
    @GetMapping("/proizvodi/{id}/izmeni")
    public String prikaziFormuZaIzmenuProizvoda(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {

        Optional<Artikal> artikal = artikalService.getByIdNoDiscount(id);
        if (artikal.isEmpty()) {
            MessageSender.redirektPoruka(redirectAttributes, "Ne postojeći proizvod!", "Nazad", "menadzer/proizvodi");
            return "redirect:/obavestenje";
        }

        model.addAttribute("artikal", artikal.get());
        return "menadzerIzmenaProizvoda";
    }

    //Izvrsavanje izmene proizvoda
    @PostMapping("/proizvodi/{id}/izmeni")
    public String izvrsiIzmenuProizvoda(@RequestParam String opis, @RequestParam double cena, @PathVariable int id, RedirectAttributes redirectAttributes) {

        //Validacija
        if (opis.isEmpty() || cena <= 0) {
            redirectAttributes.addFlashAttribute("greska", "Neispravne vrednosti!");
            return "redirect:/menadzer/proizvodi/" + id + "/izmeni";
        }

        //Izmena artikla u bazi, ispis poruke uspeha/neuspeha
        String porukaUspeha = artikalService.editArtikal(id, opis, cena);
        if (!Objects.equals(porukaUspeha, "Uspeh")) {
            redirectAttributes.addFlashAttribute("greska", porukaUspeha);
            return "redirect:/menadzer/proizvodi/" + id + "/izmeni";
        }
        MessageSender.redirektPoruka(redirectAttributes, "Uspešno izmenjen proizvod!", "Nazad", "menadzer/proizvodi");
        return "redirect:/obavestenje";
    }
}
