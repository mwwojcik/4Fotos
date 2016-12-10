package mw.uslugi;

import mw.wspolne.model.MiernikZajetosci;
import mw.wspolne.model.TypyPlikowEnum;
import mw.wspolne.model.io.Plik;
import mw.wspolne.model.io.ZbiorDyskowy;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by mw on 17.08.16.
 */
public interface GalerieUsluga {
    List<String> podajListeKatalogowGalerii() ;
    Map<TypyPlikowEnum,List<ZbiorDyskowy>> podajListePlikow(List<TypyPlikowEnum>typyPlikow,List<String>aListaKatalogow);
    List<MiernikZajetosci> obliczRozmiarPlikow(Map<TypyPlikowEnum, List<ZbiorDyskowy>> mapaPlikow);
}
