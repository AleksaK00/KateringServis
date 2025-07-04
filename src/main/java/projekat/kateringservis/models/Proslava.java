package projekat.kateringservis.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Proslava {

    //Polja tabele
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double cena;
    private String adresa;
    private int brGostiju;
    private String napomena;
    private LocalDateTime datum;
    private boolean otkazana;
    private boolean neprocitanaPorukaKorisnik;
    private boolean neprocitanaPorukaMenadzer;

    //Relacije tabele
    @ManyToOne
    private Korisnik korisnik;
    @OneToMany(mappedBy = "proslava")
    private Set<Poruka> poruke;

    public Proslava(Korisnik korisnik, String adresa, int brGostiju, double cena, LocalDateTime datum, String napomena) {
        this.korisnik = korisnik;
        this.adresa = adresa;
        this.brGostiju = brGostiju;
        this.cena = cena;
        this.datum = datum;
        this.napomena = napomena;
    }
}
