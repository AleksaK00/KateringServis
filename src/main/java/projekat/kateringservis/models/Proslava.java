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
    private float cena;
    private String adresa;
    private int brGostiju;
    private String napomena;
    private LocalDateTime datum;

    //Relacije tabele
    @ManyToOne
    private Korisnik korisnik;
    @OneToMany(mappedBy = "proslava")
    private Set<Poruka> poruke;
}
