package mw.uslugi;

import mw.uslugi.bazowe.UslugaBazowa;
import mw.wspolne.model.*;
import mw.wspolne.model.io.Katalog;
import mw.wspolne.model.io.ZbiorDyskowy;
import mw.wspolne.wlasnosci.NazwaWlasnosciEnum;
import mw.wspolne.wlasnosci.ZarzadcaWlasnosciUzytkownika;
import mw.wspolne.zdarzenia.ProgressEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Mariusz.Wojcik
 * Date: 19.02.14
 * Time: 23:08
 * To change this template use File | Settings | File Templates.
 */
@Component
public class GaleriaFasadaUslugImpl extends UslugaBazowa implements GaleriaFasadaUslug {


    @Autowired
    private GalerieUsluga galerieUsluga;

    @Autowired
    private ImportZdjecUsluga importZdjecUsluga;
    @Autowired
    private CzyszczenieUsunietychUsluga czyszczenieUsunietychUsluga;
    @Autowired
    private SegregacjaZdjecUsluga segregacjaZdjecUsluga;
    @Autowired
    private KopiaZapasowaUsluga kopiaZapasowaUsluga;

    private String SEP = System.getProperty("file.separator");

    private String RAW_KATALOG = ZarzadcaWlasnosciUzytkownika.podajInstancje().podajWartoscWlasciwosci(NazwaWlasnosciEnum.PODKATALOG_RAW);



    @Override
    public List<String> podajListeKatalogowGalerii() {
       return galerieUsluga.podajListeKatalogowGalerii();
    }


    public Map<TypyPlikowEnum,List<ZbiorDyskowy>> podajListePlikow(List<TypyPlikowEnum>typyPlikow,List<String>aListaKatalogow){
        return galerieUsluga.podajListePlikow(typyPlikow,aListaKatalogow);
    }

    @Override
    public List<MiernikZajetosci> obliczRozmiarPlikow(Map<TypyPlikowEnum, List<ZbiorDyskowy>> mapaPlikow) {
        return galerieUsluga.obliczRozmiarPlikow(mapaPlikow);
    }


    @Override
    public boolean czyMiniaturyPrzygotowane(String aKatalogWejsciowy) {
        return segregacjaZdjecUsluga.czyMiniaturyPrzygotowane(aKatalogWejsciowy);
    }

    @Override
    public void wygenerujMiniatury(String aKatalogWejsciowy) {
        if (aKatalogWejsciowy == null) {
            return;
        }

        Katalog pKat=getZarzadcaStanu().podajKatalogPoKluczu(aKatalogWejsciowy);

        Galeria pGaleria = new Galeria(pKat.getSciezka());
        
        segregacjaZdjecUsluga.wygenerujMiniatury(pGaleria);

        return;
    }

    @Override
    public Galeria podajMiniatury(String aKatalogWejsciowy) {
        if (aKatalogWejsciowy == null) {
            return null;
        }

        Katalog pKat=getZarzadcaStanu().podajKatalogPoKluczu(aKatalogWejsciowy);

        Galeria pGaleria = new Galeria(pKat.getSciezka());

        segregacjaZdjecUsluga.podajMiniatury(pGaleria);

        return pGaleria;
    }

    @Override
    public void zapiszOceny(Galeria aGaleria) {
        segregacjaZdjecUsluga.zapiszOceny(aGaleria);
    }

    @Override
    public void przenosWybraneDoUsuniecia(Galeria aGaleria) {
        segregacjaZdjecUsluga.przenosWybraneDoUsuniecia(aGaleria);
    }

    @Override
    public Path importujPliki(String prefix) {
        return importZdjecUsluga.importujZdjecia(prefix);
    }



    @Override
    public void wyczyscKosze(Set<Path> aWybraneKatalogi) {
        czyszczenieUsunietychUsluga.wyczyscKosze(aWybraneKatalogi);
    }

    @Override
    public Path wykonajKopieZapasowa(Set<Path> aKatalogiWejsciowe, Set<TypyPlikowEnum> pWybraneTypyPlikow) {
        return kopiaZapasowaUsluga.wykonajKopieZapasowa(aKatalogiWejsciowe, pWybraneTypyPlikow);
    }


    public ImportZdjecUsluga getImportZdjecUsluga() {
        return importZdjecUsluga;
    }

    public void setImportZdjecUsluga(ImportZdjecUsluga importZdjecUsluga) {
        this.importZdjecUsluga = importZdjecUsluga;
    }

    public CzyszczenieUsunietychUsluga getCzyszczenieUsunietychUsluga() {
        return czyszczenieUsunietychUsluga;
    }

    public void setCzyszczenieUsunietychUsluga(CzyszczenieUsunietychUsluga czyszczenieUsunietychUsluga) {
        this.czyszczenieUsunietychUsluga = czyszczenieUsunietychUsluga;
    }

    public SegregacjaZdjecUsluga getSegregacjaZdjecUsluga() {
        return segregacjaZdjecUsluga;
    }

    public void setSegregacjaZdjecUsluga(SegregacjaZdjecUsluga segregacjaZdjecUsluga) {
        this.segregacjaZdjecUsluga = segregacjaZdjecUsluga;
    }

    public KopiaZapasowaUsluga getKopiaZapasowaUsluga() {
        return kopiaZapasowaUsluga;
    }

    public void setKopiaZapasowaUsluga(KopiaZapasowaUsluga kopiaZapasowaUsluga) {
        this.kopiaZapasowaUsluga = kopiaZapasowaUsluga;
    }

    @Override
    public void generujZdarzeniaTestowe() {
        int max=100;
        for(int i=0;i<max;i++) {
            getPublikujacy().publikujZdarzenie(new ProgressEvent(this, 100, i, "Przygotowanie podglądu", ""));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
}
