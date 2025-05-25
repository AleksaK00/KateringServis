package projekat.kateringservis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projekat.kateringservis.models.Artikal;

import java.util.List;

@Repository
public interface ArtikalRepository extends JpaRepository<Artikal, Integer> {

    List<Artikal> findByKategorija(String kategorija);
    List<Artikal> findByKategorijaAndNaProdajiTrue(String kategorija);
    List<Artikal> findByKategorijaNot(String kategorija);
}
