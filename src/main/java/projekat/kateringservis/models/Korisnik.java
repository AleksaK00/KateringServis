package projekat.kateringservis.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Korisnik {

    //Polja tabele
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String korisnickoIme;
    private String email;
    private String sifra;
    private String uloga;
    private String ime;
    private String prezime;
    private String adresa;
    private boolean jeObrisan;

    //Relacije tabele
    @OneToMany(mappedBy = "korisnik")
    private Set<Narudzbina> narudzbine;
    @OneToMany(mappedBy = "korisnik")
    private Set<Proslava> proslave;
}
