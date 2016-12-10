package mw.uslugi;

import mw.uslugi.bazowe.UslugaBazowa;
import mw.wspolne.model.MiernikZajetosci;
import mw.wspolne.model.TypyPlikowEnum;
import mw.wspolne.model.io.Plik;
import mw.wspolne.model.io.ZbiorDyskowy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by mw on 17.08.16.
 */
@Component
public class GalerieUslugaImpl extends UslugaBazowa implements GalerieUsluga {


    public List<String> podajListeKatalogowGalerii() {
        return getZarzadcaStanu().getMapaKatalogowGalerii().stream().map(k -> k.getNazwa()).sorted()
                .collect(Collectors.toList());
    }

    @Override
    public Map<TypyPlikowEnum, List<ZbiorDyskowy>> podajListePlikow(List<TypyPlikowEnum> typyPlikow, List<String> aListaKatalogow) {
        if (typyPlikow == null || aListaKatalogow == null) {
            return null;
        }

        Map<TypyPlikowEnum, List<ZbiorDyskowy>> mapaZbiorow = new HashMap<>();

        typyPlikow.forEach(t ->
                        mapaZbiorow.put(t, new ArrayList<>())
        );


        aListaKatalogow.forEach(kat -> {
                    List<Plik> pListaPlikow = getZarzadcaStanu().podajPlikiDlaKatalogu(kat);
                    if (pListaPlikow != null) {
                        Map<TypyPlikowEnum, List<Plik>> mapaPlikowDlaKataloguWgTypow = pListaPlikow.stream().collect(Collectors.groupingBy(Plik::getTyp));
                        typyPlikow.forEach(typ -> {
                            dodajDoMapyZbiorczej(mapaPlikowDlaKataloguWgTypow.get(typ), typ,
                                    kat,
                                    mapaZbiorow
                            );
                        });
                    }
                }
        );
        return mapaZbiorow;
    }

    private void dodajDoMapyZbiorczej(List<Plik> aListaPlikowOTypieWKatalogu, TypyPlikowEnum aTypPliku, String katalog, Map<TypyPlikowEnum, List<ZbiorDyskowy>> mapaZbiorcza) {
        if (aListaPlikowOTypieWKatalogu == null) {
            return;
        }



            List<ZbiorDyskowy> pZbiory = aListaPlikowOTypieWKatalogu.stream().map(plik -> {
                        try {
                            return new ZbiorDyskowy(plik.getNazwa(), Files.size(plik.getSciezka()), katalog);
                        } catch (IOException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
            ).collect(Collectors.toList());

            if (pZbiory != null) {
                mapaZbiorcza.get(aTypPliku).addAll(pZbiory);
            }


    }

    @Override
    public List<MiernikZajetosci> obliczRozmiarPlikow(Map<TypyPlikowEnum, List<ZbiorDyskowy>> mapaPlikow) {

        if(mapaPlikow==null){
            return null;
        }
        List<MiernikZajetosci> pMierniki=new ArrayList<>();
        MiernikZajetosci pMiernikRazem=new MiernikZajetosci();
        pMiernikRazem.setRodzaj("Razem");
        mapaPlikow.forEach((k,v)->{
         MiernikZajetosci pMiernik=new MiernikZajetosci();
            pMiernik.setLiczbaPlikow((int)v.stream().count());
            pMiernik.setRozmiar(v.stream().mapToLong(z->z.getRozmiar()).sum());
            pMiernik.setRodzaj(k.toString());

            pMiernikRazem.setRozmiar(pMiernikRazem.getRozmiar()+pMiernik.getRozmiar());
            pMiernikRazem.setLiczbaPlikow(pMiernikRazem.getLiczbaPlikow()+pMiernik.getLiczbaPlikow());
            pMierniki.add(pMiernik);
        });

        pMierniki.add(pMiernikRazem);

        return pMierniki;
    }
}
