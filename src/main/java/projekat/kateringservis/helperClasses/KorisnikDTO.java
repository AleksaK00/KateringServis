package projekat.kateringservis.helperClasses;

import lombok.Data;
import projekat.kateringservis.models.Korisnik;

//Klasa za predavanje podataka o korisniku pogledu, bez informacija kao ID i sifra.
@Data
public class KorisnikDTO {

    private String korisnickoIme;
    private String ime;
    private String prezime;
    private String email;
    private String adresa;

    public KorisnikDTO(Korisnik korisnik) {
        this.korisnickoIme = korisnik.getKorisnickoIme();
        this.ime = korisnik.getIme();
        this.prezime = korisnik.getPrezime();
        this.email = korisnik.getEmail();
        this.adresa = korisnik.getAdresa();
    }
}
