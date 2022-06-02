package pl.sda.arp4.model;

public enum TypNadwozia {
    SUV (1000),
    SEDAN (800),
    CABRIO (900);

    private final double cenaBazowa;

    public double getCenaBazowa() {
        return cenaBazowa;
    }

    TypNadwozia(double cenaBazowa) {
        this.cenaBazowa = cenaBazowa;


    }
}
