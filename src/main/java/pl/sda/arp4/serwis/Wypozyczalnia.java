package pl.sda.arp4.serwis;

import pl.sda.arp4.exceptions.SamochodNieIstniejeException;
import pl.sda.arp4.model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Wypozyczalnia {
    // TYP SAMOCHODU            MAP
    //  -> SKRZYNIA             MAP
    //      - > REJESTYRACJA    MAP

    private static Integer LICZBIK_WYNAJMOW = 1;
    private Map<String, Samochod> pojazdy = new HashMap<>();
    private Map<String, WynajemSamochodu> wynajmy = new HashMap<>();

    // Możemy dodać samochód
    // Co ta funkcja powinna robić:
    //  - dodawać samochód
    //      - jeśli dodam samochód ( X Y Z ) to ten samochód musi być w wypożyczalni
    //  - dodawać samochody
    //      - jeśli dodam 5 samochodów (różnych) to powinno być 5 różnych samochodów w wypożyczalni
    //
    // Czego funkcja nie powinna pozwalać:
    //  - dodawania pojazdu o istniejącym numerze rejestracyjnym
    //
    // Zasada z Test Driven Development - zasada dobrego testera:
    //
    // Treść testu powinna mówić jakie są warunki działania/zakres funkcji.
    // Test mówi jak funkcja powinna się zachowywać.
    public void dodajSamochod(String numerRejestracyjny,
                              SkrzyniaBiegow skrzyniaBiegow,
                              TypNadwozia typNadwozia,
                              StatusSamochodu statusSamochodu) {
        if (!pojazdy.containsKey(numerRejestracyjny)) {
            pojazdy.put(numerRejestracyjny,
                    new Samochod(numerRejestracyjny, skrzyniaBiegow, typNadwozia, statusSamochodu));
            // udało się dodać samochód
        }
    }

    public List<Samochod> zwrocListe() {
        return new ArrayList<>(pojazdy.values());
    }

    public List<Samochod> zwrocListeDostepnych() {
        List<Samochod> listaDostepnych = new ArrayList<>();
        for (Samochod pojazd : pojazdy.values()) {
            if (pojazd.getStatus() == StatusSamochodu.DOSTEPNY) {
                listaDostepnych.add(pojazd);
            }
        }

        return listaDostepnych;
    }

    public List<Samochod> zwrocListeWynajetych() {
        List<Samochod> listaWynajetych = new ArrayList<>();
        for (Samochod pojazd : pojazdy.values()) {
            if (pojazd.getStatus() == StatusSamochodu.WYNAJETY) {
                listaWynajetych.add(pojazd);
            }
        }

        return listaWynajetych;
    }

    public void usunSamochod(String numerRejestracyjny) {
        pojazdy.get(numerRejestracyjny).setStatus(StatusSamochodu.NIEDOSTEPNY);
    }

    private Optional<Samochod> znajdzSamochod(String rejestracja) {
        return Optional.ofNullable(pojazdy.get(rejestracja));
    }

    public Optional<Double> sprawdzCeneSamochodu(String rejestracja, int liczbaDni) {
        Optional<Samochod> optSamochod = znajdzSamochod(rejestracja);
        if (optSamochod.isPresent()) {
            Samochod samochod = optSamochod.get();
            if (samochod.getStatus() != StatusSamochodu.DOSTEPNY) {
                throw new SamochodNieIstniejeException("Nie można zwrócić samochodu który nie jest wynajety");
            }

            double cenaZaIloscDni = samochod.getTyp().getCenaBazowa() * liczbaDni;
            return Optional.of(cenaZaIloscDni);
        }

        return Optional.empty();
    }

    public void wynajmij(String rejestracja, String imieINazwiskoKlienta, int liczbaDni) {
        Optional<Samochod> optSamochod = znajdzSamochod(rejestracja);
        if (optSamochod.isPresent()) {
            Samochod samochod = optSamochod.get();
            if(samochod.getStatus() != StatusSamochodu.DOSTEPNY){
                throw new SamochodNieIstniejeException("Samochod o wpisanej rejestracji nie istnieje");
            }

            samochod.setStatus(StatusSamochodu.WYNAJETY);

            String generowanyIdentyfikator = "WYNAJEM-" + LICZBIK_WYNAJMOW;
            wynajmy.put(generowanyIdentyfikator,
                    new WynajemSamochodu(
                            generowanyIdentyfikator,
                            imieINazwiskoKlienta,
                            samochod));
            return;
        }

        throw new SamochodNieIstniejeException("Samochod o wpisanej rejestracji nie istnieje");
    }

    public void zwrocSamochod(String rejestracja, String identyfikatorWynajmu) {
        Optional<Samochod> optSamochod = znajdzSamochod(rejestracja);
        if (optSamochod.isPresent()) {
            Samochod samochod = optSamochod.get();

            if (samochod.getStatus() != StatusSamochodu.WYNAJETY) {
                throw new SamochodNieIstniejeException("Nie można zwrócić samochodu który nie jest wynajety");
            }
            samochod.setStatus(StatusSamochodu.DOSTEPNY);
            WynajemSamochodu wynajemSamochodu = wynajmy.get(identyfikatorWynajmu);
            wynajemSamochodu.setDataZwrotu(LocalDateTime.now());
            return;
        }

        throw new SamochodNieIstniejeException("Samochod o wpisanej rejestracji nie istnieje");
    }

    public List<WynajemSamochodu> listaAktywnychWynajmów() {
        List<WynajemSamochodu> wynajmyAktywne = new ArrayList<>();
        for (WynajemSamochodu wynajem : wynajmy.values()) {
            if (wynajem.getDataZwrotu() == null) {
                wynajmyAktywne.add(wynajem);
            }
        }
        return wynajmyAktywne;
    }

    public double łącznyZysk() {
        double zysk = 0.0;
        for (WynajemSamochodu wynajem : wynajmy.values()) {
            if (wynajem.getDataZwrotu() != null) {
            }

            Duration duration = Duration.between(wynajem.getDataWynajmu(), wynajem.getDataZwrotu());
            zysk += (duration.getSeconds() / 60) * wynajem.getWynajetySamochod().getTyp().getCenaBazowa();
        }
        return zysk;
    }
}