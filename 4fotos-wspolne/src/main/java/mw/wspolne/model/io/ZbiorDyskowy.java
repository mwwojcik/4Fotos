package mw.wspolne.model.io;

import java.text.DecimalFormat;

/**
 * Created by mw on 17.08.16.
 */
public class ZbiorDyskowy {
    private String nazwa;
    private long rozmiar;
    private String nazwaKatalogu;

    public ZbiorDyskowy(String nazwa, long rozmiar, String nazwaKatalogu) {
        this.nazwa = nazwa;
        this.rozmiar = rozmiar;
        this.nazwaKatalogu = nazwaKatalogu;
    }

    public String getNazwa() {
        return nazwa;
    }

    public long getRozmiar() {
        return rozmiar;
    }

    public String getNazwaKatalogu() {
        return nazwaKatalogu;
    }

    public String getRozmiarMB() {
        double pRozmiar = rozmiar;

        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(pRozmiar / (1024 * 1024));


    }
}
