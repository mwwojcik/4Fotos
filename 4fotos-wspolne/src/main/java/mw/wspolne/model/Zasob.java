package mw.wspolne.model;

import lombok.Getter;
import lombok.Setter;
import mw.wspolne.wlasnosci.KonfiguratorAplikacji;


import java.nio.file.Path;

/**
 * Created by mw on 24.01.16.
 */

@Getter
@Setter
public class Zasob {
    private String etykieta;
    private Path sciezka;


    public Zasob(String etykieta, Path aSciezka) {
        this.etykieta = etykieta;
        this.sciezka = aSciezka;
    }

    public Zasob(Path sciezka) {
        this.sciezka = sciezka;
    }


    public String podajNazwe(){
        return sciezka.getFileName().toString();
    }


    public boolean czyIstnieje(){
        return getSciezka().toFile().exists();
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
