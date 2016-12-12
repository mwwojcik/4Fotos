package mw.wspolne.model.io;

import lombok.Getter;
import mw.wspolne.model.Zasob;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by Mariusz.Wojcik on 2016-08-17.
 */
@Getter
public class Katalog extends Zasob {
    private List<Plik> pliki;

    public Katalog(Path sciezka) {
        super(sciezka);
    }

    public Katalog(Path sciezka, List<Plik> pliki) {
        super(sciezka);
        this.pliki = pliki;
    }


}
