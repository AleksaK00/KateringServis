package projekat.kateringservis.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import projekat.kateringservis.helperClasses.ArtikalDTO;
import projekat.kateringservis.models.Artikal;
import projekat.kateringservis.repositories.ArtikalRepository;

import java.io.File;
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

    //Metoda za hvatanje artikla po id-u bez promene cene u slucaju akcije
    public Optional<Artikal> getByIdNoDiscount(int id) {
        return artikalRepository.findById(id);
    }

    //Metoda za hvatanje cene po osobi proslave
    public double getPriceByPerson() {
        Artikal artikal = artikalRepository.findByKategorija("PROSLAVA").getFirst();
        return artikal.getCena();
    }

    //Metoda za promenu cene po osobi proslave
    public boolean updatePricePerPerson(double novaCena) {
        try {
            Artikal artikal = artikalRepository.findByKategorija("PROSLAVA").getFirst();
            artikal.setCena(novaCena);
            artikalRepository.save(artikal);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //Metoda za hvatanje svih artikala
    public List<Artikal> getAll() {
        List<Artikal> artikli = artikalRepository.findByKategorijaNot("PROSLAVA");
        artikli.forEach(artikal -> artikal.setCena(primeniPopust(artikal)));
        return artikli;
    }

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

    //Metoda za dodavanje novog artikla
    @Transactional
    public boolean addArtikal(ArtikalDTO artikalDTO) {
        try {

            //Dodavanje artikla
            Artikal noviArtikal = new Artikal();
            noviArtikal.setNaziv(artikalDTO.getNaziv());
            noviArtikal.setCena(artikalDTO.getCena());
            noviArtikal.setOpis(artikalDTO.getOpis());
            noviArtikal.setKategorija(String.valueOf(artikalDTO.getTip()));
            noviArtikal.setNaProdaji(true);
            noviArtikal.setNaAkciji(false);
            Artikal artikal = artikalRepository.save(noviArtikal);

            //Dodavanje slike
            String putanja = new ClassPathResource("static/images/products").getFile().getAbsolutePath();
            File destinacija = new File(putanja + "/" + artikal.getId() + ".jpg");
            artikalDTO.getSlika().transferTo(destinacija);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //Metoda za izmenu artikla
    @Transactional
    public String editArtikal(int id, String opis, double cena) {

        try {
            Optional<Artikal> artikal = artikalRepository.findById(id);
            if (artikal.isEmpty()) {
                return "Ne postoji artikal sa datim id-om";
            }
            artikal.get().setOpis(opis);
            artikal.get().setCena(cena);
            artikalRepository.save(artikal.get());
            return "Uspeh";
        } catch (Exception e) {
            return "Greška prilikom dodavanja artikla";
        }
    }

}
