package projekat.kateringservis.helperClasses;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import projekat.kateringservis.Validators.ImageValidator;

@Data
public class ArtikalDTO {

    @NotBlank(message = "Naziv proizvoda je obavezan")
    @Size(min = 2, max = 100, message = "Naziv mora biti između 2 i 100 karaktera")
    private String naziv;

    @NotBlank(message = "Opis proizvoda je obavezan")
    @Size(min = 10, max = 1000, message = "Opis mora biti između 10 i 1000 karaktera")
    private String opis;

    @NotNull(message = "Cena je obavezna")
    @Positive(message = "Cena mora biti pozitivna vrednost")
    @DecimalMax(value = "1000000.00", message = "Cena ne može biti veća od 1,000,000.00")
    private Double cena;

    @NotNull(message = "Tip proizvoda je obavezan")
    private TipProizvoda tip;

    @NotNull(message = "Slika proizvoda je obavezna")
    private MultipartFile slika;

    public enum TipProizvoda {
        SLANO, SLATKO, SET
    }

    @AssertTrue(message = "Slika mora biti jpg i barem 1280x720 rezolucije")
    public boolean isValidImage() {
        if (slika == null || slika.isEmpty()) {
            return false;
        }
        try {
            return ImageValidator.validateResolutionAndType(slika);
        } catch (Exception e) {
            return false;
        }
    }

}
