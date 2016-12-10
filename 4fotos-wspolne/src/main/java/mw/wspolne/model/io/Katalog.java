package mw.wspolne.model.io;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by Mariusz.Wojcik on 2016-08-17.
 */
public class Katalog extends Zbior {
    private List<Plik> pliki;

    public Katalog(Path sciezka) {
        super(sciezka);
    }

    public Katalog(Path sciezka, List<Plik> pliki) {
        super(sciezka);
        this.pliki = pliki;
    }

    public List<Plik> getPliki() {
        return pliki;
    }
}
