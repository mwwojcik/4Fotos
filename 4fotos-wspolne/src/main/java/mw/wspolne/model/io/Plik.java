package mw.wspolne.model.io;

import mw.wspolne.model.TypyPlikowEnum;

import java.nio.file.Path;

/**
 * Created by Mariusz.Wojcik on 2016-08-17.
 */
public class Plik extends Zbior{

    private TypyPlikowEnum typ;

    public Plik(Path sciezka) {
        super(sciezka);
        typ=TypyPlikowEnum.podajTypDlaNazwy(this.getNazwa());
    }

    public TypyPlikowEnum getTyp() {
        return typ;
    }
}
