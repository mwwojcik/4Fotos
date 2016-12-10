package mw.uslugi;

import mw.uslugi.io.ZarzadcaLogowania;
import mw.wspolne.wlasnosci.NazwaWlasnosciEnum;
import mw.wspolne.wlasnosci.ZarzadcaWlasnosciUzytkownika;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: mw
 * Date: 16.03.14
 * Time: 09:29
 * To change this template use File | Settings | File Templates.
 */
@Component
public class CzyszczenieUsunietychUslugaImpl implements CzyszczenieUsunietychUsluga {
    private String SEP = System.getProperty("file.separator");
    private String KATALOG_ROOT_USUNIETYCH_SCIEZKA = ZarzadcaWlasnosciUzytkownika.podajInstancje().podajWartoscWlasciwosci(NazwaWlasnosciEnum.KATALOG_GLOWNY_USUNIETYCH);
    @Override
    public void wyczyscKosze(Set<Path> aWybraneKatalogi) {
        if(aWybraneKatalogi==null){
            return;
        }

        String pNazwaKatUsunie=ZarzadcaWlasnosciUzytkownika.podajInstancje().podajWartoscWlasciwosci(NazwaWlasnosciEnum.PODKATALOG_DO_USUNIECIA);
        Path KATALOG_ROOT_USUNIETYCH=Paths.get(KATALOG_ROOT_USUNIETYCH_SCIEZKA);

        if(!Files.exists(KATALOG_ROOT_USUNIETYCH)){
            try {
                Files.createDirectory(KATALOG_ROOT_USUNIETYCH);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }

        aWybraneKatalogi.stream().forEach(kat->{
            Path pKosz= Paths.get(kat+SEP+pNazwaKatUsunie);
            if(!Files.exists(pKosz)){
                return;
            }

            try {
                List<Path> pListaPlikow=Files.list(pKosz).collect(Collectors.toList());

                Path cel=Paths.get(KATALOG_ROOT_USUNIETYCH+SEP+kat.toFile().getName());
                if(!Files.exists(cel)){
                    Files.createDirectory(cel);
                }

                pListaPlikow.stream().forEach(p->{
                    Path pDocelowy=Paths.get(cel+SEP+p.toFile().getName());
                    try {
                        ZarzadcaLogowania.podajInstancje().logujKomunikat("Usunięto=>"+p);
                        Files.move(p,pDocelowy);
                    } catch (IOException e) {
                        throw new IllegalArgumentException(e);
                    }
                });

            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }

        });
    }
}