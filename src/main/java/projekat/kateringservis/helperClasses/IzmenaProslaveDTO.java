package projekat.kateringservis.helperClasses;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IzmenaProslaveDTO {

    @NotBlank(message = "Adresa je obavezna!")
    private String adresa;

    @NotNull(message = "Datum je obavezan!")
    private String datum;

    @NotNull(message = "Broj gostiju je obavezan!")
    @Min(value = 20, message = "Minimalan broj gostiju je 20!")
    @Max(value = 200, message = "Maksimalan broj gostiju je 200!")
    private int brOsoba;

    @NotNull(message = "Cena je obavezna!")
    @Min(value = 20000, message = "Unesite cenu!")
    private double cenaUnos;

    private String napomena;
}
