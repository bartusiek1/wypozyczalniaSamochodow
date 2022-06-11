package pl.sda.arp4.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
    @EqualsAndHashCode.Exclude
    private StatusSamochodu status;




}
