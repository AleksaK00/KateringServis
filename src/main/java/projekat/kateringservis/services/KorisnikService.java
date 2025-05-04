package projekat.kateringservis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import projekat.kateringservis.models.Korisnik;
import projekat.kateringservis.repositories.KorisnikRepository;

import java.util.Optional;

@Service
public class KorisnikService implements UserDetailsService {

    private final KorisnikRepository korisnikRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //Povezivanje sa beanovime preko konstruktora, oznaka @Lazy za password encoder da bi se izbeglo ciklicno mapiranje beanova
    @Autowired
    public KorisnikService(KorisnikRepository korisnikRepository, @Lazy BCryptPasswordEncoder bCryptPasswordEncoder)
    {
        this.korisnikRepository = korisnikRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    //Implementiranje metode naslednjene iz Spring Security-a, hvatanje naloga po korisnickom imenu i stavljanje u spring security UserDetails objekat
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Korisnik> korisnikOptional = korisnikRepository.findByKorisnickoIme(username);

        //Ukoliko korisnik sa datim imenom postoji, puni Spring Security User koriscenjem ugradjenog buildera, ukoliko ne vraca predefinisan izuzetak
        if (korisnikOptional.isPresent()) {
            Korisnik korisnik = korisnikOptional.get();

            return User.builder().username(korisnik.getKorisnickoIme()).password(korisnik.getSifra()).roles(korisnik.getUloga().split(",")).build();
        }
        else {
            throw new UsernameNotFoundException(username);
        }
    }

    //Metoda za registraciju korisnika, vraca poruku o tome da li je korisnicko ime ili email zauzet ukoliko jesu
    public String registrujKorisnika(Korisnik korisnik) {

        //Proverava da li je korisnicko ime vec zauzeto
        Optional<Korisnik> korisnikIme = korisnikRepository.findByKorisnickoIme(korisnik.getKorisnickoIme());
        if (korisnikIme.isPresent()) {
            return "korisnickoIme";
        }

        //Proverava da li je email vec zauzet
        Optional<Korisnik> korisnikEmail = korisnikRepository.findByEmail(korisnik.getEmail());
        if (korisnikEmail.isPresent()) {
            return "email";
        }

        //U sluvaju da su ime i email slobodni, enkriptuje sifru i cuva korisnika u bazu podataka
        korisnik.setSifra(bCryptPasswordEncoder.encode(korisnik.getSifra()));
        korisnik.setUloga("KORISNIK");
        korisnikRepository.save(korisnik);
        return "uspeh";

    }

    //Metoda vraca korisnika po njegovom korisnickom imenu
    public Optional<Korisnik> getByKorisnickoime(String korisnickoIme) {
        return korisnikRepository.findByKorisnickoIme(korisnickoIme);
    }
}
