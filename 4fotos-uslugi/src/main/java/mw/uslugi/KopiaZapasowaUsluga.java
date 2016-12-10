package mw.uslugi;

import mw.wspolne.model.TypyPlikowEnum;

import java.nio.file.Path;
import java.util.Set;

/**
 * Created by mw on 01.02.16.
 */
public interface KopiaZapasowaUsluga {
    Path wykonajKopieZapasowa(Set<Path> aKatalogiWejsciowe, Set<TypyPlikowEnum> pWybraneTypyPlikow);
}
