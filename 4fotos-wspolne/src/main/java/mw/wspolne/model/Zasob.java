package mw.wspolne.model;

import mw.wspolne.wlasnosci.ZarzadcaWlasnosciUzytkownika;

import java.nio.file.Path;

/**
 * Created by mw on 24.01.16.
 */
//todo do zastanowienia polaczenie Zbior i Zasob - bo byc moze nadmiarowe
@Deprecated
public class Zasob {
    private String etykieta;
    private Path sciezka;
    private String SEP = ZarzadcaWlasnosciUzytkownika.podajInstancje().separator();

    public Zasob(String etykieta, Path aSciezka) {
        this.etykieta = etykieta;
        this.sciezka = aSciezka;
    }

    public Zasob(Path sciezka) {
        this.sciezka = sciezka;
    }

    public Path getSciezka() {
        return sciezka;
    }

    public void setSciezka(Path sciezka) {
        this.sciezka = sciezka;
    }

    public String podajSciezkeRoot(){
        return sciezka.toFile().getAbsolutePath().replace(SEP+podajNazwe(),"");
    }

    public String podajNazwe(){
        String[] pTab=dekomponujSciezke();
        return pTab[pTab.length-1];
    }

    public String podajNazweBezRozszerzenia(){
        String pNazwa=podajNazwe();
        if(pNazwa.contains(".")){
            String[] pTab=pNazwa.split("\\.");
            return pTab[0];
        }
        return pNazwa;
    }

    private String[] dekomponujSciezke(){
        return sciezka.toFile().getAbsolutePath().split(SEP);
    }

    public boolean czyIstnieje(){
        return getSciezka().toFile().exists();
    }

    public String getEtykieta() {
        return etykieta;
    }

    public void setEtykieta(String etykieta) {
        this.etykieta = etykieta;
    }

    public boolean equals(Object obj) {
        Zasob pObr=(Zasob)obj;
        return pObr.getSciezka().toString().compareTo(getSciezka().toString())==0;
    }

    @Override
    public String toString() {
        return etykieta;
    }
}
