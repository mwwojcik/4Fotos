package mw.wspolne.model.io;

import java.nio.file.Path;

/**
 * Created by Mariusz.Wojcik on 2016-08-17.
 */
//todo do zastanowienia polaczenie Zbior i Zasob - bo byc moze nadmiarowe
@Deprecated
public class Zbior {
    protected Path sciezka;

    public Zbior(Path sciezka) {
        this.sciezka = sciezka;
    }

    public String getNazwa(){
        return sciezka.getFileName().toString();
    }

    public Path getSciezka() {
        return sciezka;
    }
}
