package projekat.kateringservis.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projekat.kateringservis.helperClasses.MessageSender;
import projekat.kateringservis.helperClasses.PrijavljeniKorisnikController;
import projekat.kateringservis.models.Korisnik;
import projekat.kateringservis.services.KorisnikService;
import projekat.kateringservis.services.ProslavaService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/admin")
public class AdminController extends PrijavljeniKorisnikController {

    public AdminController(KorisnikService korisnikService, ProslavaService proslavaService) {
        super(korisnikService, proslavaService);
    }

    //Vraca tabelu za upravljanje korisnicima
    @GetMapping("/panel")
    public String adminPanel(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        Korisnik korisnik = trenutnoPrijavljen(userDetails);
        model.addAttribute("adminovID", korisnik.getId());
        model.addAttribute("korisnici", korisnikService.getAll());
        return "adminKorisnici";
    }

    //Metoda gasi ili pali korisnicki nalog
    @GetMapping("/promeniObrisanStatus/{id}")
    public String promeniObrisanStatus(@PathVariable int id) {
        try {
            korisnikService.toggleAccountState(id);
        } catch (Exception e) {
            return "redirect:/admin/panel";
        }
        return "redirect:/admin/panel";
    }

    //Metoda za pretragu korisnika po korisnickom imenu ili ID-u
    @PostMapping("/pretragaKorisnika")
    public String pretragaNarudzbina(Model model, @RequestParam String pretraga, @RequestParam String tipPretrage, @AuthenticationPrincipal UserDetails userDetails) {

        //Ako je string prazan vraca sve korisnike(Reset pretrage)
        if (pretraga.isEmpty()) {
            return "redirect:/admin/panel";
        }

        //Pretraga po izabranom tipu i stavljanje u model
        List<Korisnik> korisnici = null;
        if (tipPretrage.equals("korisnickoIme")) {
            korisnici = korisnikService.getAllByKorisnickoIme(pretraga);
        }
        else {
            korisnici = korisnikService.getByID(pretraga).stream().toList();
        }
        model.addAttribute("korisnici", korisnici);
        Korisnik korisnik = trenutnoPrijavljen(userDetails);
        model.addAttribute("adminovID", korisnik.getId());
        return "adminKorisnici";
    }

    //Metoda prikazuje formu za unos novog korisnika
    @GetMapping("/noviKorisnik")
    public String prikaziFormuUnosKorisnika(Model model) {
        model.addAttribute("korisnik", new Korisnik());
        return "adminUnosKorisnika";
    }

    @PostMapping("/noviKorisnik")
    public String dodajNovogKorisnika(@ModelAttribute Korisnik korisnik, @RequestParam String passwordConfirm, RedirectAttributes redirectAttributes) {

        //Validacija praznih polja
        if (korisnik.getKorisnickoIme().isEmpty() || korisnik.getSifra().isEmpty() || korisnik.getEmail().isEmpty() || korisnik.getIme().isEmpty() || korisnik.getPrezime().isEmpty()
                || korisnik.getAdresa().isEmpty() || passwordConfirm.isEmpty()) {
            redirectAttributes.addFlashAttribute("greska", "Sva polja moraju biti popunjena!");
            return "redirect:/admin/noviKorisnik";
        }

        //Validacija potvrde lozinke
        if (!korisnik.getSifra().equals(passwordConfirm))
        {
            redirectAttributes.addFlashAttribute("greska", "Lozinke se ne poklapaju!");
            return "redirect:/admin/noviKorisnik";
        }

        //Validacija email-a
        Pattern pattern = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,6}$");
        Matcher matcher = pattern.matcher(korisnik.getEmail());
        if (!matcher.matches()) {
            redirectAttributes.addFlashAttribute("greska", "Email nije validan!");
            return "redirect:/admin/noviKorisnik";
        }

        //Validacija jacine lozinke
        pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$");
        matcher = pattern.matcher(korisnik.getSifra());
        if (!matcher.matches()) {
            redirectAttributes.addFlashAttribute("greska", "Lozinka mora da ima minimum osam karaktera, bar jedno veliko, jedno malo slovo i broj!");
            return "redirect:/admin/noviKorisnik";
        }

        //Dodavanje korisnika u bazu podataka ukoliko je prosla validacija, izbacivanje greske ako ime ili email vec postoji u bazi
        String uspeh = korisnikService.registrujKorisnika(korisnik);
        if (!uspeh.equals("uspeh")) {
            if (uspeh.equals("korisnickoIme")) {
                redirectAttributes.addFlashAttribute("greska", "Korisničko ime vec postoji!");
                return "redirect:/admin/noviKorisnik";
            }
            if (uspeh.equals("email")) {
                redirectAttributes.addFlashAttribute("greska", "Email ime vec postoji!");
                return "redirect:/admin/noviKorisnik";
            }
        }
        MessageSender.redirektPoruka(redirectAttributes, "Uspešno dodat novi korisnik", "Nazad", "/admin/panel");
        return "redirect:/obavestenje";
    }
}
