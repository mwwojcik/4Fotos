package mw.wspolne.model;

import java.text.DecimalFormat;

/**
 * Created with IntelliJ IDEA.
 * User: Mariusz.Wojcik
 * Date: 11.03.14
 * Time: 16:29
 * To change this template use File | Settings | File Templates.
 */
public class MiernikZajetosci {
    private String rodzaj;
    private long rozmiar;
    private int liczbaPlikow;

    public long getRozmiar() {
        return rozmiar;
    }

    public void setRozmiar(long rozmiar) {
        this.rozmiar = rozmiar;
    }

    public int getLiczbaPlikow() {
        return liczbaPlikow;
    }

    public void setLiczbaPlikow(int liczbaPlikow) {
        this.liczbaPlikow = liczbaPlikow;
    }

    public String getRozmiarMB() {
        double pRozmiar = rozmiar;

        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(pRozmiar / (1024 * 1024));


    }

    public String getRodzaj() {
        return rodzaj;
    }

    public void setRodzaj(String rodzaj) {
        this.rodzaj = rodzaj;
    }
}
