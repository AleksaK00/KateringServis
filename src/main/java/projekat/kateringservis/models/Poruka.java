package projekat.kateringservis.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
public class Poruka {

    //Polja tabele
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String imePosaljioca;
    @Lob
    private String sadrzaj;
    private LocalDateTime vreme;
    private boolean procitanaKorisnik;
    private boolean procitanaMenadzer;

    //Relacije tabele
    @ManyToOne
    private Proslava proslava;

    public Poruka(Proslava proslava, String imePosaljioca, String sadrzaj, LocalDateTime vreme) {
        this.proslava = proslava;
        this.imePosaljioca = imePosaljioca;
        this.sadrzaj = sadrzaj;
        this.vreme = vreme;
    }
}
