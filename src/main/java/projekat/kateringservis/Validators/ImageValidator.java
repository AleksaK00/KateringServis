package projekat.kateringservis.Validators;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class ImageValidator {

    //Metoda validatora koja proverava da li je slika veca od 720p i JPG
    public static boolean validateResolutionAndType(MultipartFile file) {
        try {

            //provera formata slike
            if (!Objects.equals(file.getContentType(), "image/jpeg")) {
                return false;
            }

            //provera rezolucije slike
            BufferedImage slika = ImageIO.read(file.getInputStream());
            if (slika == null) {
                return false;
            }
            return slika.getWidth() >= 1280 && slika.getHeight() >= 720;

        } catch (Exception e) {
            return false;
        }
    }
}
