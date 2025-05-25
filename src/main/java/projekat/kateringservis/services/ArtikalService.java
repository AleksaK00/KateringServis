package projekat.kateringservis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projekat.kateringservis.models.Artikal;
import projekat.kateringservis.repositories.ArtikalRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ArtikalService {

    private final ArtikalRepository artikalRepository;
    private static final double POPUST_AKCIJA = 0.10;

    @Autowired
    public ArtikalService(ArtikalRepository artikalRepository) {
        this.artikalRepository = artikalRepository;
    }

    //Metoda koja vraca cenu sa popustom
    private double primeniPopust(Artikal artikal) {
        if (artikal.isNaAkciji()) {
            return artikal.getCena() * (1 - POPUST_AKCIJA);
        }
        else {
            return artikal.getCena();
        }
    }

    //Metoda za hvatanje svih artikala prema kategoriji artikla
    public List<Artikal> getByCategory(String kategorija) {

        List<Artikal> artikli = artikalRepository.findByKategorijaAndNaProdajiTrue(kategorija);
        artikli.forEach(artikal -> artikal.setCena(primeniPopust(artikal)));
        return artikli;
    }

    //Metoda za hvatanje artikla po id-u
    public Optional<Artikal> getById(int id) {
        return artikalRepository.findById(id)
                .map(artikal -> {
                    artikal.setCena(primeniPopust(artikal));
                    return artikal;
                });
    }

    //Metoda za hvatanje cene po osobi proslave
    public double getPriceByPerson() {
        Artikal artikal = artikalRepository.findByKategorija("PROSLAVA").getFirst();
        return artikal.getCena();
    }

    //Metoda za hvatanje svih artikala
    public List<Artikal> getAll() { return artikalRepository.findByKategorijaNot("PROSLAVA");}

    //Metoda menja da li je artikal na akciji ili nije.
    public void toggleDiscount(int id) {
        artikalRepository.findById(id).ifPresent(artikal -> {
            artikal.setNaAkciji(!artikal.isNaAkciji());
            artikalRepository.save(artikal);
        });
    }

    //Metoda menja da li je artikal na meniju ili nije.
    public void toggleNaMeniju(int id) {
        artikalRepository.findById(id).ifPresent(artikal -> {
            artikal.setNaProdaji(!artikal.isNaProdaji());
            artikalRepository.save(artikal);
        });
    }

}
