package mw.uslugi;

import mw.wspolne.model.*;
import mw.wspolne.model.io.ZbiorDyskowy;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Mariusz.Wojcik
 * Date: 19.02.14
 * Time: 23:08
 * To change this template use File | Settings | File Templates.
 */
public interface GaleriaFasadaUslug {

    //import
    Path importujPliki(String prefix);

    //segregacja
    public boolean czyMiniaturyPrzygotowane(String aKatalogWejsciowy);
    public void wygenerujMiniatury(String aKatalogWejsciowy);
    public Galeria podajMiniatury(String aKatalogWejsciowy);

    public void zapiszOceny(Galeria aGaleria);
    public void przenosWybraneDoUsuniecia(Galeria aGaleria);



    void wyczyscKosze(Set<Path> aWybraneKatalogi);
    Path wykonajKopieZapasowa(Set<Path> aKatalogiWejsciowe, Set<TypyPlikowEnum> pWybraneTypyPlikow);

    /*Nowe zweryfikowane*/

    List<String>podajListeKatalogowGalerii();

    Map<TypyPlikowEnum,List<ZbiorDyskowy>> podajListePlikow(List<TypyPlikowEnum>typyPlikow,List<String>aListaKatalogow);

    List<MiernikZajetosci> obliczRozmiarPlikow(Map<TypyPlikowEnum,List<ZbiorDyskowy>> mapaPlikow);

    void generujZdarzeniaTestowe();
}
