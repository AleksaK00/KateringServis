package projekat.kateringservis.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Stavka {

    //Polja tabele
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int kolicina;

    //Relacije tabele
    @ManyToOne
    private Narudzbina narudzbina;
    @ManyToOne
    private Artikal artikal;
}
