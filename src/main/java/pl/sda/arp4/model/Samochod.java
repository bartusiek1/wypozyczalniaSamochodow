package pl.sda.arp4.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Samochod {


    // dla identyfikacji pojazdu
    private String numerRejestracyjny;

    // dla filtru
    private SkrzyniaBiegow skrzynia;
    private TypNadwozia typ;

    // dla sprawdzenia dostępności
    private StatusSamochodu status;

}
