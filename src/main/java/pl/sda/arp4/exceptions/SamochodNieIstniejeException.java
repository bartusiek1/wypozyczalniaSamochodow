package pl.sda.arp4.exceptions;

public class SamochodNieIstniejeException extends RuntimeException {
    public SamochodNieIstniejeException(String wiadomoscKtoraSieWyswietli) {
        super(wiadomoscKtoraSieWyswietli);
    }
}
