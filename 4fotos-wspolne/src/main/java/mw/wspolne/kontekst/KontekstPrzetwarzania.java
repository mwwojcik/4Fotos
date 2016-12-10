package mw.wspolne.kontekst;

import mw.wspolne.model.TrybAplikacjiEnum;

import mw.wspolne.model.MiernikZajetosci;
import mw.wspolne.model.TypyPlikowEnum;
import mw.wspolne.wlasnosci.NazwaWlasnosciEnum;
import mw.wspolne.wlasnosci.ZarzadcaWlasnosciUzytkownika;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Mariusz.Wojcik
 * Date: 19.02.14
 * Time: 23:19
 * To change this template use File | Settings | File Templates.
 */
public class KontekstPrzetwarzania {
    Set<Path> wybraneKatalogi=new HashSet<Path>();
    Set<TypyPlikowEnum> wybraneTypyPlikow=new HashSet<TypyPlikowEnum>();
    Map<TypyPlikowEnum,MiernikZajetosci> miernikiZajetosci=new HashMap<TypyPlikowEnum, MiernikZajetosci>();
    private Path zaimportowanyKatalog;
    boolean czyModelWymagaAktualizacji=false;
    private TrybAplikacjiEnum trybPracyApliakacji=TrybAplikacjiEnum.OBSLUGA_ZDJEC;
    private Path katalogRoboczy;

    public void inicjalizuj(){
        miernikiZajetosci.put(TypyPlikowEnum.JPG,new MiernikZajetosci());
        miernikiZajetosci.put(TypyPlikowEnum.RAW,new MiernikZajetosci());
        miernikiZajetosci.put(TypyPlikowEnum.XCF,new MiernikZajetosci());
        //miernikiZajetosci.put(TypyPlikowEnum.WSZYSTKIE,new MiernikZajetosci());

        wybraneTypyPlikow.add(TypyPlikowEnum.JPG);
        wybraneTypyPlikow.add(TypyPlikowEnum.RAW);
        wybraneTypyPlikow.add(TypyPlikowEnum.XCF);

    }

    public void zerujMiernikiZajetosci(){
        miernikiZajetosci.put(TypyPlikowEnum.JPG,new MiernikZajetosci());
        miernikiZajetosci.put(TypyPlikowEnum.RAW,new MiernikZajetosci());
        miernikiZajetosci.put(TypyPlikowEnum.XCF,new MiernikZajetosci());
        //ernikiZajetosci.put(TypyPlikowEnum.WSZYSTKIE,new MiernikZajetosci());

    }

    public Set<Path> getWybraneKatalogi() {
        return wybraneKatalogi;
    }

    public void setWybraneKatalogi(Set<Path> wybraneKatalogi) {
        this.wybraneKatalogi = wybraneKatalogi;
    }

    public Set<TypyPlikowEnum> getWybraneTypyPlikow() {
        return wybraneTypyPlikow;
    }

    public Map<TypyPlikowEnum, MiernikZajetosci> getMiernikiZajetosci() {
        return miernikiZajetosci;
    }

    public void setMiernikiZajetosci(Map<TypyPlikowEnum, MiernikZajetosci> miernikiZajetosci) {
        this.miernikiZajetosci = miernikiZajetosci;
    }

    public Path getZaimportowanyKatalog() {
        return zaimportowanyKatalog;
    }

    public void setZaimportowanyKatalog(Path zaimportowanyKatalog) {
        this.zaimportowanyKatalog = zaimportowanyKatalog;
    }

    public boolean isCzyModelWymagaAktualizacji() {
        return czyModelWymagaAktualizacji;
    }

    public void setCzyModelWymagaAktualizacji(boolean czyModelWymagaAktualizacji) {
        this.czyModelWymagaAktualizacji = czyModelWymagaAktualizacji;
    }

    public TrybAplikacjiEnum getTrybPracyApliakacji() {
        return trybPracyApliakacji;
    }

    public void setTrybPracyApliakacji(TrybAplikacjiEnum trybPracyApliakacji) {
        this.trybPracyApliakacji = trybPracyApliakacji;
    }

    public Path getKatalogRoboczy() {
        if(trybPracyApliakacji==TrybAplikacjiEnum.OBSLUGA_WWW){
            return Paths.get(ZarzadcaWlasnosciUzytkownika.podajInstancje().podajWartoscWlasciwosci(NazwaWlasnosciEnum.GALERIA_WWW_ZRODLO));
        }
        else {
            return Paths.get(ZarzadcaWlasnosciUzytkownika.podajInstancje().podajKatalogGlownyGalerii());
        }
    }

}
