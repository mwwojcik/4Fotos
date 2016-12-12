package mw.wspolne.model.io;

import lombok.Getter;
import mw.wspolne.model.TypyPlikowEnum;
import mw.wspolne.model.Zasob;

import java.nio.file.Path;

/**
 * Created by Mariusz.Wojcik on 2016-08-17.
 */
@Getter
public class Plik extends Zasob{

    private TypyPlikowEnum typ;

    public Plik(Path sciezka) {
        super(sciezka);
        typ=TypyPlikowEnum.podajTypDlaNazwy(this.podajNazwe());
    }


}
