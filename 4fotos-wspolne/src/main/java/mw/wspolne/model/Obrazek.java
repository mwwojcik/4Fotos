package mw.wspolne.model;

import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;

/**
 * Created by mw on 24.01.16.
 */

@Setter
@Getter
public class Obrazek extends Zasob {
    private double ocena=2;
    private Obrazek miniatura;

    public Obrazek(String etykieta, Path aSciezka) {
        super(etykieta, aSciezka);
    }


    public Obrazek(Path sciezka) {
        super(sciezka);
    }

    public double getOcena() {
       //todo do uspojnienia
        return podajOcene();
    }

    public double podajOcene(){
        return ocena;
    }


}
