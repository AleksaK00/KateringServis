package projekat.kateringservis.helperClasses;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class MessageSender {

    //Metoda koja stavlja potrebne atribute za redirekciju ka stranici poruke
    public static void redirektPoruka(RedirectAttributes redirectAttributes,String poruka, String tekstDugme, String linkDugme) {

        redirectAttributes.addFlashAttribute("poruka", poruka);
        redirectAttributes.addFlashAttribute("tekstDugme", tekstDugme);
        redirectAttributes.addFlashAttribute("linkDugme", linkDugme);
    }
}
