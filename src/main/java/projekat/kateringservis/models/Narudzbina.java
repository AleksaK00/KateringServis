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
public class Narudzbina {

    //Polja tabele
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private float cena;
    private String adresa;
    private LocalDateTime datum;

    //Relacije tabele
    @ManyToOne
    private Korisnik korisnik;
    @OneToMany(mappedBy = "narudzbina")
    private Set<Stavka> stavke;
}
