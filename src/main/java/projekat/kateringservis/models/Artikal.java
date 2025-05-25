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
public class Artikal {

    //Polja tabele
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String naziv;
    private String kategorija;
    private String opis;
    private double cena;
    private boolean naAkciji;
    private boolean naProdaji;

    //Relacije tabele
    @OneToMany(mappedBy = "artikal")
    private Set<Stavka> stavke;
}
