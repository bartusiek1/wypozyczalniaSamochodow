package rental;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import pl.sda.arp4.exceptions.SamochodNieIstniejeException;
import pl.sda.arp4.model.*;
import pl.sda.arp4.serwis.Wypozyczalnia;

import java.util.List;
import java.util.Optional;


public class WypozyczalniaTest {

    public void test_mozliweJestDodawanieSamochodu() {
        Samochod testowanySamochod = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.DOSTEPNY);

        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();
        wypozyczalnia.dodajSamochod(
                testowanySamochod.getNumerRejestracyjny(),
                testowanySamochod.getSkrzynia(),
                testowanySamochod.getTyp(),
                testowanySamochod.getStatus());

        List<Samochod> wynikZwroconaLista = wypozyczalnia.zwrocListe();
        // contains również porównuje metodą equals
        Assert.assertEquals("Lista powinna zawierać dokładnie jeden pojazd, bo tylko tyle ich dodaliśmy", 1, wynikZwroconaLista.size());

        Assert.assertTrue("Lista zwrócona przez obiekt wypożyczalnia nie zawiera pojazdu dodanego, a powinna go zawierać",
                wynikZwroconaLista.contains(testowanySamochod));
    }

    // nie jest możliwe 'nadpisanie' samochodu
    @Test
    public void test_mozliweJestDodawanieSamochoduAleNieJegoNadpisanie() {
        Samochod testowanySamochod = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.DOSTEPNY);
        Samochod testowanySamochodDrugi = new Samochod("test1", SkrzyniaBiegow.MANUAL, TypNadwozia.SUV, StatusSamochodu.NIEDOSTEPNY);

        // Testujemy na jednej wypożyczalni
        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();

        // dodajemy pierwszy (zadziała poprawnie)
        wypozyczalnia.dodajSamochod(
                testowanySamochod.getNumerRejestracyjny(),
                testowanySamochod.getSkrzynia(),
                testowanySamochod.getTyp(),
                testowanySamochod.getStatus());

        // to musi nie zadziałać (nie może dodać, nie może nadpisać)
        wypozyczalnia.dodajSamochod(
                testowanySamochodDrugi.getNumerRejestracyjny(),
                testowanySamochodDrugi.getSkrzynia(),
                testowanySamochodDrugi.getTyp(),
                testowanySamochodDrugi.getStatus());

        List<Samochod> wynikZwroconaLista = wypozyczalnia.zwrocListe();
        // contains również porównuje metodą equals
        Assert.assertEquals("Lista powinna zawierać dokładnie jeden pojazd, bo drugi nie powinien być dodany", 1, wynikZwroconaLista.size());
        Assert.assertTrue("Lista zwrócona przez obiekt wypożyczalnia nie zawiera pojazdu dodanego, a powinna go zawierać", wynikZwroconaLista.contains(testowanySamochod));
    }

    @Test
    public void test_mozemyPobracListeSamochodowDostepnych() {
        // pobieramy liste ktora zawiera TYLKO DOSTEPNE samochody
        Samochod testowanySamochod = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.DOSTEPNY);
        Samochod testowanySamochodDrugi = new Samochod("test2", SkrzyniaBiegow.MANUAL, TypNadwozia.SUV, StatusSamochodu.NIEDOSTEPNY);

        // Testujemy na jednej wypożyczalni
        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();

        // dodajemy pierwszy (zadziała poprawnie)
        wypozyczalnia.dodajSamochod(
                testowanySamochod.getNumerRejestracyjny(),
                testowanySamochod.getSkrzynia(),
                testowanySamochod.getTyp(),
                testowanySamochod.getStatus());

        // to musi nie zadziałać (nie może dodać, nie może nadpisać)
        wypozyczalnia.dodajSamochod(
                testowanySamochodDrugi.getNumerRejestracyjny(),
                testowanySamochodDrugi.getSkrzynia(),
                testowanySamochodDrugi.getTyp(),
                testowanySamochodDrugi.getStatus());

        List<Samochod> wynikZwroconaListaWszystkichSamochodow = wypozyczalnia.zwrocListe();
        Assert.assertEquals("Lista powinna zawierać oba pojazdy", 2, wynikZwroconaListaWszystkichSamochodow.size());

        List<Samochod> dostepne = wypozyczalnia.zwrocListeDostepnych();
        Assert.assertEquals("Lista powinna zawierać jeden dostepny pojazd", 1, dostepne.size());
        Assert.assertTrue("Lista powinna zawierać tylko samochod dostepny", dostepne.contains(testowanySamochod));
    }

    @Test
    public void test_mozemyZmienicStatusSamochoduNaNiedostepny() {
        Samochod testowanySamochod = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.DOSTEPNY);

        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();
        wypozyczalnia.dodajSamochod(
                testowanySamochod.getNumerRejestracyjny(),
                testowanySamochod.getSkrzynia(),
                testowanySamochod.getTyp(),
                testowanySamochod.getStatus());

        List<Samochod> wynikZwroconaLista = wypozyczalnia.zwrocListe();
        // contains również porównuje metodą equals
        Assert.assertEquals("Lista powinna zawierać dokładnie jeden pojazd, bo tylko tyle ich dodaliśmy", 1, wynikZwroconaLista.size());
        Assert.assertTrue("Lista zwrócona przez obiekt wypożyczalnia nie zawiera pojazdu dodanego, a powinna go zawierać", wynikZwroconaLista.contains(testowanySamochod));

        wypozyczalnia.usunSamochod("test1");
        wynikZwroconaLista = wypozyczalnia.zwrocListe();
        // contains również porównuje metodą equals
        Assert.assertEquals("Lista powinna zawierać dokładnie jeden pojazd, bo tylko tyle ich dodaliśmy", 1, wynikZwroconaLista.size());
        Assert.assertTrue("Samochod powinien miec status niedostepny", wynikZwroconaLista.get(0).getStatus() == StatusSamochodu.NIEDOSTEPNY);

        Samochod samochodDoPorownania = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.NIEDOSTEPNY);
        Assert.assertTrue("Samochod powinien miec status niedostepny", wynikZwroconaLista.contains(samochodDoPorownania));

        // TODO: lista dostępnych powinna być rozmiaru 0
    }

    // 1. Chcę wiedzieć że mogę pobrać listę wynajętych samochodów
    //  - Stwórz nowy obiekt Wypozyczalnia
    //  - Nie posiada samochodów więc zwróć (wynik metody ma zwrócić) listę wynajętych - lista musi być pusta
    //  - Nie posiada samochodów więc zwróć (wynik metody ma zwrócić) listę wszystkich - lista musi być pusta
    //  - Dodajemy dwa samochody
    //  - zwróć (wynik metody ma zwrócić) listę wszystkich - lista musi zawierać dwa samochody podane wyżej
    //  - zwróć (wynik metody ma zwrócić) listę wynajętych - lista musi być pusta
    //  - wynajmuję samochód 1
    //  - zwróć (wynik metody ma zwrócić) listę wszystkich - lista musi zawierać dwa samochody podane wyżej
    //  - zwróć (wynik metody ma zwrócić) listę wynajętych - lista musi zwrócić samochód 1
    //  - wynajmuję samochód 2
    //  - zwróć (wynik metody ma zwrócić) listę wszystkich - lista musi zawierać dwa samochody podane wyżej
    //  - zwróć (wynik metody ma zwrócić) listę wynajętych - lista musi zwrócić samochód 1 i 2


    @Test
    public void test_zwrocListeWynajetych() {
        Samochod sam1 = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.DOSTEPNY);
        Samochod sam2 = new Samochod("test2", SkrzyniaBiegow.MANUAL, TypNadwozia.SUV, StatusSamochodu.DOSTEPNY);

        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();

        //  - krok 1:
        List<Samochod> wynajete1 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkie1 = wypozyczalnia.zwrocListe(); // to jest zwrocListe (wszystkich)
        Assert.assertEquals("Lista powinna być pusta", 0, wynajete1.size());
        Assert.assertEquals("Lista powinna być pusta", 0, wszystkie1.size());

        //  - krok 2:
        wypozyczalnia.dodajSamochod(
                sam1.getNumerRejestracyjny(),
                sam1.getSkrzynia(),
                sam1.getTyp(),
                sam1.getStatus());
        wypozyczalnia.dodajSamochod(
                sam2.getNumerRejestracyjny(),
                sam2.getSkrzynia(),
                sam2.getTyp(),
                sam2.getStatus());
        List<Samochod> wynajete2 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkie2 = wypozyczalnia.zwrocListe(); // to jest zwrocListe (wszystkich)
        Assert.assertEquals("Lista powinna być pusta", 0, wynajete2.size());
        Assert.assertEquals("Lista powinna mieć 2 samochody", 2, wszystkie2.size());

        // Opcjonalne
        Assert.assertTrue("Powinien zawierać samochód 1", wszystkie2.contains(sam1));
        Assert.assertTrue("Powinien zawierać samochód 1", wszystkie2.contains(sam2));

        //  - krok 3:
        wypozyczalnia.wynajmij("test1", "Pawel Test Gawel", 10);
        List<Samochod> wynajete3 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkie3 = wypozyczalnia.zwrocListe(); // to jest zwrocListe (wszystkich)
        Assert.assertEquals("Lista powinna mieć jeden samochód", 1, wynajete3.size());
        Assert.assertEquals("Lista powinna mieć 2 samochody", 2, wszystkie3.size());

        // Opcjonalne
        Samochod sam1Wynajety = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.WYNAJETY);
        Assert.assertTrue("Wynajete 3 po wynajeciu powinien zawierać samochód 1 ze zmienionym statusem", wynajete3.contains(sam1Wynajety));
        Assert.assertTrue("Wynajete 3 po wynajeciu powinien zawierać samochód 1", wynajete3.contains(sam1));

        //  - krok 4:
        wypozyczalnia.wynajmij("test2", "Pawel Test Gawel", 20);
        List<Samochod> wynajete4 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkie4 = wypozyczalnia.zwrocListe(); // to jest zwrocListe (wszystkich)
        Assert.assertEquals("Lista powinna mieć 2 samochody", 2, wynajete4.size());
        Assert.assertEquals("Lista powinna mieć 2 samochody", 2, wszystkie4.size());

        // Opcjonalne
        Assert.assertTrue("Wynajete 4 po wynajeciu powinien zawierać samochód 1", wynajete4.contains(sam1));
        Assert.assertTrue("Wynajete 4 po wynajeciu powinien zawierać samochód 2", wynajete4.contains(sam2));
    }

    // 2. Działą metoda sprawdzCeneSamochodu
    // - stwóz nowy obiekt Wypozyczalnia
    // - sprawdzCeneSamochodu dla samochodu 'test1' nie działą, bo samochod nie istnieje
    // - dodaj samochod 'test1'
    // - cena dziala dla samochodu 'test1'
    // przeliczania ceny dziala dla ilosci dni 1, 5, 10
    // sprawdzCeneSamochodu dla samochodu 'test1' nie dziala bo samochod nie istnieje

    @Test
    public void test_sprawdzCeneSamochodu() {

        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();

        // sprawdzCeneSamochodu dla samochodu 'test1' nie dziala bo samochod nie istnieje
        Optional<Double> cenaOptional1 = wypozyczalnia.sprawdzCeneSamochodu("test1", 10);
        Assert.assertFalse("Cena nie powinna istnieć, bo nie powinno być samochodów", cenaOptional1.isPresent());

        // dodaj samochod 'test1'
        Samochod sam1 = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.DOSTEPNY);
        wypozyczalnia.dodajSamochod(
                sam1.getNumerRejestracyjny(),
                sam1.getSkrzynia(),
                sam1.getTyp(),
                sam1.getStatus());

        // cena dziala dla smaochodu 'test1'
        Optional<Double> cenaOptional2 = wypozyczalnia.sprawdzCeneSamochodu("test1", 1);
        Assert.assertTrue("Cena powinna istnieć, dodaliśmy samochód", cenaOptional2.isPresent());
        double cenaZa1Dzien = cenaOptional2.get();

        //  - przeliczenie ceny dziala dla ilosci dni 1, 5
        Optional<Double> cenaOptional3_dla5 = wypozyczalnia.sprawdzCeneSamochodu("test1", 5);
        Assert.assertTrue("Cena powinna istnieć, dodaliśmy samochód", cenaOptional3_dla5.isPresent());
        double cenaZa5Dni = cenaOptional3_dla5.get();
        Assert.assertTrue("Cena za 1 dzien * 5 == cena za 5 dni", cenaZa5Dni == (cenaZa1Dzien * 5));

        //  - przeliczenie ceny dziala dla ilosci dni 1, 10
        Optional<Double> cenaOptional4_dla10 = wypozyczalnia.sprawdzCeneSamochodu("test1", 10);
        Assert.assertTrue("Cena powinna istnieć, dodaliśmy samochód", cenaOptional4_dla10.isPresent());
        double cenaZa10Dni = cenaOptional4_dla10.get();
        Assert.assertTrue("Cena za 1 dzien * 10 == cena za 10 dni", cenaZa10Dni == (cenaZa1Dzien * 10));

        //  - sprawdzCeneSamochodu dla samochodu 'test2' nie dziala bo samochod nie istnieje
        Optional<Double> cenaOptional5 = wypozyczalnia.sprawdzCeneSamochodu("test2", 10);
        Assert.assertFalse("Cena nie powinna istnieć, bo nie powinno być samochodów", cenaOptional5.isPresent());
    }

    // 3. Dziala metoda Wynajmij i ListaAktywnychWynajmów
    ////  - Stwórz nowy obiekt Wypozyczalnia
    ////  - (Na początku wypozyczalnia nie posiada samochodów) - zwróć (wynik metody ma zwrócić) listę wynajętych - lista musi być pusta
    ////  - (Na początku wypozyczalnia nie posiada samochodów) -  zwróć (wynik metody ma zwrócić) listę wszystkich - lista musi być pusta
    ////  - (Na początku wypozyczalnia nie posiada wynajmów) -  zwróć (wynik metody ma zwrócić) listę aktywnych wynajmów - lista musi być pusta
    ////  - dodaj samochod 'test1'
    ////  - dodaj samochod 'test2'
    ////  - listę wynajętych - lista musi być pusta
    ////  - listę wszystkich - 2 samochody
    ////  - listę aktywnych wynajmów - lista musi być pusta
    ////  - wynajmij samochod 'test1'
    ////  - listę wszystkich - 2 samochody
    ////  - listę wynajętych - 1 samochód
    ////  - listę aktywnych wynajmów - lista musi zawierać 1 wynajem
    ////  - obiekt na pozycji 0 (wynajęty samochód) musi zawierać samochód z listy wynajętych

    @Test
    public void test_wynajmowanieSamnochodu() {

        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();

        // Krok 1
        List<Samochod> wynajete1 = wypozyczalnia.zwrocListeWynajetych();
        Assert.assertEquals("Lista powinna być pusta", 0, wynajete1.size());

        List<Samochod> wszystkie1 = wypozyczalnia.zwrocListe();
        Assert.assertEquals("Lista powinna być pusta", 0, wszystkie1.size());

        List<WynajemSamochodu> wynajmy1 = wypozyczalnia.listaAktywnychWynajmów();
        Assert.assertEquals("Lista powinna być pusta", 0, wynajmy1.size());

        // Krok 2

        Samochod sam1 = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.DOSTEPNY);
        Samochod sam2 = new Samochod("test2", SkrzyniaBiegow.MANUAL, TypNadwozia.SUV, StatusSamochodu.DOSTEPNY);

        wypozyczalnia.dodajSamochod(
                sam1.getNumerRejestracyjny(),
                sam1.getSkrzynia(),
                sam1.getTyp(),
                sam1.getStatus());

        wypozyczalnia.dodajSamochod(
                sam2.getNumerRejestracyjny(),
                sam2.getSkrzynia(),
                sam2.getTyp(),
                sam2.getStatus());

        // Krok 3

        List<Samochod> wynajete2 = wypozyczalnia.zwrocListeWynajetych();
        Assert.assertEquals("Lista powinna być pusta", 0, wynajete2.size());

        List<Samochod> wszystkie2 = wypozyczalnia.zwrocListe();
        Assert.assertEquals("Lista powinna mieć 2 elementy", 2, wszystkie2.size());

        List<WynajemSamochodu> wynajmy2 = wypozyczalnia.listaAktywnychWynajmów();
        Assert.assertEquals("Lista powinna być pusta", 0, wynajmy2.size());

        // Krok 4

        wypozyczalnia.wynajmij("test1", "Paweł test Gaeweł", 10);

        List<Samochod> wynajete3 = wypozyczalnia.zwrocListeWynajetych();
        Assert.assertEquals("Lista powinna miećjeden samochód", 1, wynajete3.size());

        List<Samochod> wszystkie3 = wypozyczalnia.zwrocListe();
        Assert.assertEquals("Lista powinna mieć 2 elementy", 2, wszystkie3.size());

        List<WynajemSamochodu> wynajmy3 = wypozyczalnia.listaAktywnychWynajmów();
        Assert.assertEquals("Lista powinna mieć 1 wynajem", 1, wynajmy3.size());

        // obiekt na pozycji 0(wynajety samochod) musi zawierać samochod z listy wynajętych

        WynajemSamochodu sprawdzanyWynajem = wynajmy3.get(0);
        Samochod sprawdzamySamochod = wynajete3.get(0);
        Assert.assertEquals("Wynajem posiada samochód i tym samochodem jest jedyny wynajęty samochód", sprawdzanyWynajem.getWynajetySamochod(), sprawdzamySamochod);

    }
    // 4. Wynajmij rzuca exception jeśli nie ma szukanego samochodu
    //  - Stwórz nowy obiekt Wypozyczalnia
    //  - (Na początku wypozyczalnia nie posiada samochodów) - zwróć (wynik metody ma zwrócić) listę wynajętych - lista musi być pusta
    //  - (Na początku wypozyczalnia nie posiada samochodów) -  zwróć (wynik metody ma zwrócić) listę wszystkich - lista musi być pusta
    //  - (Na początku wypozyczalnia nie posiada wynajmów) -  zwróć (wynik metody ma zwrócić) listę aktywnych wynajmów - lista musi być pusta
    //  - wywołuję metodę wynajmij samochodu 'test1' i oczekuję że otrzymam exception
    // Jeśli ktokolwiek skorzysta
    @Test(expected = SamochodNieIstniejeException.class)
    public void test_wynajmijNieDzialaJesliSamochodNieIstnieje() {
        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();

        //  - krok 1:
        //  - (Na początku wypozyczalnia nie posiada samochodów wynajetych) - zwróć (wynik metody ma zwrócić) listę wynajętych - lista musi być pusta
        //  - (Na początku wypozyczalnia nie posiada samochodów [wszystkich]) -  zwróć (wynik metody ma zwrócić) listę wszystkich - lista musi być pusta
        //  - (Na początku wypozyczalnia nie posiada wynajmów) -  zwróć (wynik metody ma zwrócić) listę aktywnych wynajmów - lista musi być pusta
        List<Samochod> wynajete1 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkie1 = wypozyczalnia.zwrocListe(); // to jest zwrocListe (wszystkich)
        List<WynajemSamochodu> wynajmy1 = wypozyczalnia.listaAktywnychWynajmów(); // to jest zwrocListe (wszystkich)
        Assert.assertEquals("Lista powinna być pusta", 0, wynajete1.size());
        Assert.assertEquals("Lista powinna być pusta", 0, wszystkie1.size());
        Assert.assertEquals("Lista powinna być pusta", 0, wynajmy1.size());

        //  - wywołuję metodę wynajmij samochodu 'test1' i oczekuję że otrzymam exception
        wypozyczalnia.wynajmij("test1", "Pawel Test Gawel", 10);
    }

    // 5. Wynajmij i zwróć samochod działa
    // Krok 1:
    //  - Stwórz nowy obiekt Wypozyczalnia
    //  - (Na początku wypozyczalnia nie posiada samochodów) - zwróć (wynik metody ma zwrócić) listę wynajętych - lista musi być pusta
    //  - (Na początku wypozyczalnia nie posiada samochodów) -  zwróć (wynik metody ma zwrócić) listę wszystkich - lista musi być pusta
    //  - (Na początku wypozyczalnia nie posiada wynajmów) -  zwróć (wynik metody ma zwrócić) listę aktywnych wynajmów - lista musi być pusta
    //
    // Krok 2:
    //  - dodaj samochod 1 i 2
    //  - wywołuję metodę wynajmij samochodu 'test1' powinno dzialac
    //  - listę wszystkich - 2 samochody
    //  - listę wynajętych - 1 samochód
    //  - listę aktywnych wynajmów - lista musi zawierać 1 wynajem
    //  - listę zakonczonych wynajmów - lista musi zawierać 0 wynajmów
    //  - obiekt na pozycji 0 (wynajęty samochód) musi zawierać samochód z listy wynajętych
    //
    // Krok 3:
    //  - wywołuję metodę zwrocSamochod 'test1` - powinno dzialac i nie rzucić exception (wszystko ok)
    //  - listę wszystkich - 2 samochody
    //  - listę wynajętych - 0 samochód
    //  - listę aktywnych wynajmów - lista musi zawierać 0 wynajem
    //  - listę zakonczonych wynajmów - lista musi zawierać 1 wynajem
    //
    // Krok 4:
    //  - wywołuję metodę zwrocSamochod (drugi raz) 'test1` - powinno rzucić exception


    // 6. Zwróć samochód zwraca exception dla nieistniejącego samochodu
    @Test(expected = SamochodNieIstniejeException.class)
    public void test_zwrocSamochodRzucaExceptionBoSamochodNieIstnieje() {
        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();

        List<Samochod> wynajete1 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkie1 = wypozyczalnia.zwrocListe(); // to jest zwrocListe (wszystkich)
        List<WynajemSamochodu> wynajmy1 = wypozyczalnia.listaAktywnychWynajmów(); // to jest zwrocListe (wszystkich)
        Assert.assertEquals("Lista powinna być pusta", 0, wynajete1.size());
        Assert.assertEquals("Lista powinna być pusta", 0, wszystkie1.size());
        Assert.assertEquals("Lista powinna być pusta", 0, wynajmy1.size());

        //  - wywołuję metodę wynajmij samochodu 'test1' i oczekuję że otrzymam exception
        wypozyczalnia.zwrocSamochod("test1", "WYNAJEM-1");
    }

    // 7. Zwróć samochód zwraca exception dla niedostępnego samochodu
    @Test(expected = SamochodNieIstniejeException.class)
    public void test_zwrocSamochodRzucaExceptionBoSamochodJestNiedostepny() {
        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();

        List<Samochod> wynajete1 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkie1 = wypozyczalnia.zwrocListe(); // to jest zwrocListe (wszystkich)
        List<WynajemSamochodu> wynajmy1 = wypozyczalnia.listaAktywnychWynajmów(); // to jest zwrocListe (wszystkich)
        Assert.assertEquals("Lista powinna być pusta", 0, wynajete1.size());
        Assert.assertEquals("Lista powinna być pusta", 0, wszystkie1.size());
        Assert.assertEquals("Lista powinna być pusta", 0, wynajmy1.size());

        Samochod sam1 = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.DOSTEPNY);
        Samochod sam2 = new Samochod("test2", SkrzyniaBiegow.MANUAL, TypNadwozia.SUV, StatusSamochodu.DOSTEPNY);
        wypozyczalnia.dodajSamochod(
                sam1.getNumerRejestracyjny(),
                sam1.getSkrzynia(),
                sam1.getTyp(),
                sam1.getStatus());
        wypozyczalnia.dodajSamochod(
                sam2.getNumerRejestracyjny(),
                sam2.getSkrzynia(),
                sam2.getTyp(),
                sam2.getStatus());
        List<Samochod> wynajete2 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkie2 = wypozyczalnia.zwrocListe(); // to jest zwrocListe (wszystkich)
        List<WynajemSamochodu> wynajmy2 = wypozyczalnia.listaAktywnychWynajmów(); // to jest zwrocListe (wszystkich)
        Assert.assertEquals("Lista powinna być pusta", 0, wynajete2.size());
        Assert.assertEquals("Lista powinna mieć 2 elementy", 2, wszystkie2.size());
        Assert.assertEquals("Lista powinna być pusta", 0, wynajmy2.size());

        //  - wywołuję metodę wynajmij samochodu 'test1' i oczekuję że otrzymam exception
        wypozyczalnia.usunSamochod("test1");
        wypozyczalnia.wynajmij("test1", "Pawel Test Gawel", 10);
//        //  - wywołuję metodę wynajmij samochodu 'test1' i oczekuję że otrzymam exception
//        wypozyczalnia.zwrocSamochod("test1", "WYNAJEM-1");
    }


    // 8. Usuń samochód usuwa go z listy dostępnych samochodów (tylko samochody dostępne i te które nie są wynajęte) [trzeba poprawić kod]
    @Test()
    public void test_usunSamochodUsuwaZListyDostepnych() {
        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();

        List<Samochod> wynajete1 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkie1 = wypozyczalnia.zwrocListe(); // to jest zwrocListe (wszystkich)
        List<WynajemSamochodu> wynajmy1 = wypozyczalnia.listaAktywnychWynajmów(); // to jest zwrocListe (wszystkich)
        Assert.assertEquals("Lista powinna być pusta", 0, wynajete1.size());
        Assert.assertEquals("Lista powinna być pusta", 0, wszystkie1.size());
        Assert.assertEquals("Lista powinna być pusta", 0, wynajmy1.size());

        Samochod sam1 = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.DOSTEPNY);
        Samochod sam2 = new Samochod("test2", SkrzyniaBiegow.MANUAL, TypNadwozia.SUV, StatusSamochodu.DOSTEPNY);
        wypozyczalnia.dodajSamochod(
                sam1.getNumerRejestracyjny(),
                sam1.getSkrzynia(),
                sam1.getTyp(),
                sam1.getStatus());
        wypozyczalnia.dodajSamochod(
                sam2.getNumerRejestracyjny(),
                sam2.getSkrzynia(),
                sam2.getTyp(),
                sam2.getStatus());
        List<Samochod> dostepne2 = wypozyczalnia.zwrocListeDostepnych();
        List<Samochod> wynajete2 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkie2 = wypozyczalnia.zwrocListe(); // to jest zwrocListe (wszystkich)
        List<WynajemSamochodu> wynajmy2 = wypozyczalnia.listaAktywnychWynajmów(); // to jest zwrocListe (wszystkich)
        Assert.assertEquals("Lista powinna być pusta", 0, wynajete2.size());
        Assert.assertEquals("Lista powinna mieć 2 elementy", 2, wszystkie2.size());
        Assert.assertEquals("Lista powinna być pusta", 0, wynajmy2.size());
        Assert.assertEquals("Lista powinna miec 2 elementy", 2, dostepne2.size());

        wypozyczalnia.usunSamochod("test1");
        List<Samochod> dostepne3 = wypozyczalnia.zwrocListeDostepnych();
        List<Samochod> wynajete3 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkie3 = wypozyczalnia.zwrocListe(); // to jest zwrocListe (wszystkich)
        List<WynajemSamochodu> wynajmy3 = wypozyczalnia.listaAktywnychWynajmów(); // to jest zwrocListe (wszystkich)
        Assert.assertEquals("Lista powinna być pusta", 0, wynajete3.size());
        Assert.assertEquals("Lista powinna mieć 2 elementy", 2, wszystkie3.size());
        Assert.assertEquals("Lista powinna być pusta", 0, wynajmy3.size());
        Assert.assertEquals("Lista powinna miec 1 element", 1, dostepne3.size());
        wypozyczalnia.usunSamochod("test2");

        List<Samochod> dostepne4 = wypozyczalnia.zwrocListeDostepnych();
        List<Samochod> wynajete4 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkie4 = wypozyczalnia.zwrocListe(); // to jest zwrocListe (wszystkich)
        List<WynajemSamochodu> wynajmy4 = wypozyczalnia.listaAktywnychWynajmów(); // to jest zwrocListe (wszystkich)
        Assert.assertEquals("Lista powinna być pusta", 0, wynajete4.size());
        Assert.assertEquals("Lista powinna mieć 2 elementy", 2, wszystkie4.size());
        Assert.assertEquals("Lista powinna być pusta", 0, wynajmy4.size());
        Assert.assertEquals("Lista powinna byc pusta", 0, dostepne4.size());
    }

    // 9. Nie można sprawdzić ceny samochodu który nie jest dostępny [trzeba poprawić kod]
    @Test(expected = SamochodNieIstniejeException.class)
    public void test_nieMoznaSprawdzicCenyNiedostepnychSamochodow(){

        Wypozyczalnia wypozyczalnia = new Wypozyczalnia();

        List<Samochod> wynajete1 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkie1 = wypozyczalnia.zwrocListe(); // to jest zwrocListe (wszystkich)
        List<WynajemSamochodu> wynajmy1 = wypozyczalnia.listaAktywnychWynajmów(); // to jest zwrocListe (wszystkich)
        Assert.assertEquals("Lista powinna być pusta", 0, wynajete1.size());
        Assert.assertEquals("Lista powinna być pusta", 0, wszystkie1.size());
        Assert.assertEquals("Lista powinna być pusta", 0, wynajmy1.size());

        Samochod sam1 = new Samochod("test1", SkrzyniaBiegow.AUTOMATYCZNA, TypNadwozia.CABRIO, StatusSamochodu.DOSTEPNY);
        Samochod sam2 = new Samochod("test2", SkrzyniaBiegow.MANUAL, TypNadwozia.SUV, StatusSamochodu.DOSTEPNY);
        wypozyczalnia.dodajSamochod(
                sam1.getNumerRejestracyjny(),
                sam1.getSkrzynia(),
                sam1.getTyp(),
                sam1.getStatus());
        wypozyczalnia.dodajSamochod(
                sam2.getNumerRejestracyjny(),
                sam2.getSkrzynia(),
                sam2.getTyp(),
                sam2.getStatus());
        List<Samochod> dostepne2 = wypozyczalnia.zwrocListeDostepnych();
        List<Samochod> wynajete2 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkie2 = wypozyczalnia.zwrocListe(); // to jest zwrocListe (wszystkich)
        List<WynajemSamochodu> wynajmy2 = wypozyczalnia.listaAktywnychWynajmów(); // to jest zwrocListe (wszystkich)
        Assert.assertEquals("Lista powinna być pusta", 0, wynajete2.size());
        Assert.assertEquals("Lista powinna mieć 2 elementy", 2, wszystkie2.size());
        Assert.assertEquals("Lista powinna być pusta", 0, wynajmy2.size());
        Assert.assertEquals("Lista powinna miec 2 elementy", 2, dostepne2.size());

        wypozyczalnia.usunSamochod("test1");
        List<Samochod> dostepne3 = wypozyczalnia.zwrocListeDostepnych();
        List<Samochod> wynajete3 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkie3 = wypozyczalnia.zwrocListe(); // to jest zwrocListe (wszystkich)
        List<WynajemSamochodu> wynajmy3 = wypozyczalnia.listaAktywnychWynajmów(); // to jest zwrocListe (wszystkich)
        Assert.assertEquals("Lista powinna być pusta", 0, wynajete3.size());
        Assert.assertEquals("Lista powinna mieć 2 elementy", 2, wszystkie3.size());
        Assert.assertEquals("Lista powinna być pusta", 0, wynajmy3.size());
        Assert.assertEquals("Lista powinna miec 1 element", 1, dostepne3.size());
        wypozyczalnia.usunSamochod("test2");

        List<Samochod> dostepne4 = wypozyczalnia.zwrocListeDostepnych();
        List<Samochod> wynajete4 = wypozyczalnia.zwrocListeWynajetych();
        List<Samochod> wszystkie4 = wypozyczalnia.zwrocListe(); // to jest zwrocListe (wszystkich)
        List<WynajemSamochodu> wynajmy4 = wypozyczalnia.listaAktywnychWynajmów(); // to jest zwrocListe (wszystkich)
        Assert.assertEquals("Lista powinna być pusta", 0, wynajete4.size());
        Assert.assertEquals("Lista powinna mieć 2 elementy", 2, wszystkie4.size());
        Assert.assertEquals("Lista powinna być pusta", 0, wynajmy4.size());
        Assert.assertEquals("Lista powinna byc pusta", 0, dostepne4.size());

        wypozyczalnia.sprawdzCeneSamochodu("test1", 10);
    }

    // 10. Znajdź samochód zwraca KAŻDY SAMOCHÓD (musisz potwierdzić, że niezależnie od tego czy samochód jest:
    //      dostępny,
    //      wynajęty czy
    //      niedostępny
    //  - możliwe jest znalezienie samochodu metodą znajdzSamochod
    // 12. Możliwe jest wynajęcie dwóch samochodów (tutaj mamy bug'a, trzeba poprawić kod)
    // stwórz samochód sam1, sam2
    // dodaj oba samochody
    // wynajmij samochod sam1 (sprawdz dlugosci poszczegolnych list)
    // wynajmij samochod sam2 (sprawdz dlugosci poszczegolnych list)
}