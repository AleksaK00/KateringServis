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

    @Autowired
    public ArtikalService(ArtikalRepository artikalRepository) {
        this.artikalRepository = artikalRepository;
    }

    //Metoda za hvatanje svih artikala prema kategoriji artikla
    public List<Artikal> getByCategory(String kategorija) {
        return artikalRepository.findByKategorija(kategorija);
    }

    //Metoda za hvatanje artikla po id-u
    public Optional<Artikal> getById(int id) {
        return artikalRepository.findById(id);
    }
}
