package projekat.kateringservis.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    //Relacije tabele
    @ManyToOne
    private Proslava proslava;
}
