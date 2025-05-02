package projekat.kateringservis.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

//Klasa za konfiguraciju jezika lokalizacije, koriscen za svaku metodu koja vraca pogled sa ispisom valute RSD
@Configuration
public class LocaleConfiguration {

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(new Locale("sr", "RS", "Latn"));
        return resolver;
    }
}
