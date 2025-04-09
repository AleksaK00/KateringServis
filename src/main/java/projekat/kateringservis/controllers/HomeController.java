package projekat.kateringservis.controllers;

import org.springframework.web.bind.annotation.GetMapping;

public class HomeController {

    @GetMapping("/")
    public String pocetna() {
        return "index";
    }
}
