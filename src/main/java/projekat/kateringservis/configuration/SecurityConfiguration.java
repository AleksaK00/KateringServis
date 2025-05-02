package projekat.kateringservis.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import projekat.kateringservis.services.KorisnikService;

//Klasa potrebna za konfiguraciju Spring security-a
@Configuration
public class SecurityConfiguration {

    //Povezivanje sa KorisnikService preko konstruktora
    protected KorisnikService korisnikService;
    @Autowired
    public SecurityConfiguration(KorisnikService korisnikService)
    {
        this.korisnikService = korisnikService;
    }

    //Security chain definise autorizaciju i odredjuje rute za login i logout. Spring security ce po ovoj konfiguraciji sam vrsiti autorizaciju na osnovu ruta
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(
                        authorize -> {
                            //Resursi i stranice koje su dozvoljene svima, ukljucujuci ne ulogovane korisnike
                            authorize.requestMatchers("/css/**", "/js/**", "/images/**").permitAll();
                            authorize.requestMatchers("/login", "/error/**", "/logout", "/", "/meni/**", "/korpa/**", "/register").permitAll();
                            //Stranice koje zahtevaju specificnu ulogu za autorizaciju
                            authorize.requestMatchers("/admin/**").hasRole("ADMIN");
                            authorize.requestMatchers("/menadzer/**").hasAnyRole("MENADZER", "ADMIN");
                            authorize.requestMatchers("/korisnik/**").hasRole("KORISNIK");
                            //Sve ostale stranice zahtevaju ulogovanog korisnika, bez obzira na ulogu
                            authorize.anyRequest().authenticated();
                        }
                ).formLogin(formLogin -> formLogin
                        .loginPage("/login")  //definise stranicu za login
                        .defaultSuccessUrl("/", true) //Redirekcija u slucaju uspesnog logina
                        .failureUrl("/login?greska=true") //Neuspesni login url
                        .permitAll())
                .logout(logout -> logout.logoutUrl("/logout") //Definise logout url
                        .logoutSuccessUrl("/")  //Redirekcija posle logouta
                        .permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)  //Za svrhu projekta, iskljucena je csrf protekcija
                .build();
    }

    //Konfigurise UserDetailsService
    @Bean
    public UserDetailsService userDetailService() {
        return korisnikService;
    }


    //Konfigurise enkoder koji ce vrsiti hashovanje sifre
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Konfigurise sitem za koji ce se koristiti za autentikaciju.
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(korisnikService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }
}
