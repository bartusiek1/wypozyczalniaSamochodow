package pl.sda.arp4.model;

import java.time.LocalDateTime;

public class WynajemSamochodu {

    private String identyfikator;
    private LocalDateTime dataWynajmu;  // w datach wystarczy LocalDate, ale żeby można się tym było "bawić" ustawiłem LocalDateTime
    private LocalDateTime dataZwrotu;   // domyślnie null, jeśli jest ustawione, oznacza że samochód jest zwrócony
    private String imieINazwiskoKlienta;
    private Samochod wynajetySamochod;

    public WynajemSamochodu(String identyfikator, String imieINazwiskoKlienta, Samochod wynajetySamochod) {
        this.identyfikator = identyfikator;
        this.imieINazwiskoKlienta = imieINazwiskoKlienta;
        this.wynajetySamochod = wynajetySamochod;
        this.dataWynajmu = LocalDateTime.now();
    }

    public String getIdentyfikator() {
        return identyfikator;
    }

    public void setIdentyfikator(String identyfikator) {
        this.identyfikator = identyfikator;
    }

    public LocalDateTime getDataWynajmu() {
        return dataWynajmu;
    }

    public void setDataWynajmu(LocalDateTime dataWynajmu) {
        this.dataWynajmu = dataWynajmu;
    }

    public LocalDateTime getDataZwrotu() {
        return dataZwrotu;
    }

    public void setDataZwrotu(LocalDateTime dataZwrotu) {
        this.dataZwrotu = dataZwrotu;
    }

    public String getImieINazwiskoKlienta() {
        return imieINazwiskoKlienta;
    }

    public void setImieINazwiskoKlienta(String imieINazwiskoKlienta) {
        this.imieINazwiskoKlienta = imieINazwiskoKlienta;
    }

    public Samochod getWynajetySamochod() {
        return wynajetySamochod;
    }

    public void setWynajetySamochod(Samochod wynajetySamochod) {
        this.wynajetySamochod = wynajetySamochod;
    }

    @Override
    public String toString() {
        return "WynajemSamochodu{" +
                "identyfikator='" + identyfikator + '\'' +
                ", dataWynajmu=" + dataWynajmu +
                ", dataZwrotu=" + dataZwrotu +
                ", imieINazwiskoKlienta='" + imieINazwiskoKlienta + '\'' +
                ", wynajetySamochod=" + wynajetySamochod +
                '}';
    }
}
