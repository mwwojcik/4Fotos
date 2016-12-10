package mw.uslugi;

import mw.uslugi.io.ZarzadcaLogowania;
import mw.wspolne.kontekst.ZarzadcaObiektow;
import mw.wspolne.model.TypyPlikowEnum;
import mw.wspolne.wlasnosci.NazwaWlasnosciEnum;
import mw.wspolne.wlasnosci.ZarzadcaWlasnosciUzytkownika;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

/**
 * Created by mw on 01.02.16.
 */
@Component
public class KopiaZapasowaUslugaImpl implements KopiaZapasowaUsluga {

    private String SEP = ZarzadcaWlasnosciUzytkownika.podajInstancje().separator();

    private boolean czyJestTypu(String aNazwa, String[] aRozszerzenia) {
        for (String pRoz : aRozszerzenia) {
            if (aNazwa.contains(pRoz)) {
                return true;
            }
        }
        return false;
    }

    private void odwiedzPlik(Path file, Set<TypyPlikowEnum> Typy, Element parent) {
/*

        if (czyJestTypu(file.toString(), TypyPlikowEnum.WSZYSTKIE.getRozszerzenia())) {

            if (Typy.contains(TypyPlikowEnum.JPG) && czyJestTypu(file.toString(), TypyPlikowEnum.JPG.getRozszerzenia())) {
                DokumentXMLHelper.instancja().podajElementDlaPliku(parent, file);
            } else if (Typy.contains(TypyPlikowEnum.RAW) && czyJestTypu(file.toString(), TypyPlikowEnum.RAW.getRozszerzenia())) {
                DokumentXMLHelper.instancja().podajElementDlaPliku(parent, file);
            } else if (Typy.contains(TypyPlikowEnum.XCF) && czyJestTypu(file.toString(), TypyPlikowEnum.XCF.getRozszerzenia())) {
                DokumentXMLHelper.instancja().podajElementDlaPliku(parent, file);
            }

        }
*/


    }

    private void odwiedzKatalog(Path aKatalog, Element parent, Set<TypyPlikowEnum> aTypy) {
        try {
            Files.list(aKatalog).forEach(p -> {
                if (Files.isDirectory(p)) {
                    odwiedzKatalog(p, DokumentXMLHelper.instancja().podajElementDlaKatalogu(parent, p), aTypy);
                } else {
                    odwiedzPlik(p, aTypy, parent);
                }
            });
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }


    @Override
    public Path wykonajKopieZapasowa(Set<Path> aKatalogiWejsciowe, Set<TypyPlikowEnum> pWybraneTypyPlikow) {

        ZarzadcaObiektow.podajInstancje().podajKontekst().zerujMiernikiZajetosci();

        ZarzadcaLogowania.podajInstancje().wyczyscPole();

        Document pDok = DokumentXMLHelper.instancja().podajDokumentDlaKopiiZapasowej();

        aKatalogiWejsciowe.stream().forEach(p -> {
            Element pElem = DokumentXMLHelper.instancja().podajElementDlaKatalogu(pDok.getRootElement(), p);
            odwiedzKatalog(p, pElem, pWybraneTypyPlikow);
        });

        String pStr = DokumentXMLHelper.instancja().zapiszDoStr(pDok).replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");

        //odczytaj plik i zamien element ELEMENT_FILES_TEMPLATE

        System.out.println(pStr);

        Path pCel = zalozKatalog();

        zapiszDoPliku(pCel, pStr, "ELEMENT_FILES_TEMPLATE");

        return null;
    }


    private Path zalozKatalog() {
        Path pKatalog = Paths.get(ZarzadcaWlasnosciUzytkownika.podajInstancje().podajWartoscWlasciwosci(NazwaWlasnosciEnum.KATALOG_GLOWY_BACKUPU));
        try {
            if (!Files.exists(pKatalog)) {
                pKatalog = Files.createDirectory(pKatalog);
            }

            int iter = 1;
            String pNazwaKatDocelowegoNiezmienna = "projekt-k3b";

            String pNazwa=pKatalog+SEP+pNazwaKatDocelowegoNiezmienna;

            while (Files.exists(Paths.get(pNazwa))) {
                pNazwa=pKatalog+SEP+pNazwaKatDocelowegoNiezmienna+"-"+iter;
                iter++;
            }

            Path pCel=Files.createDirectory(Paths.get(pNazwa));

            pKatalog = pCel;

        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return pKatalog;
    }


    private void zapiszDoPliku(Path aKatalog, String aTekst, String aWzorzec) {
        try {

            Path pKatalogSzablonu=Paths.get(ZarzadcaWlasnosciUzytkownika.podajInstancje().podajKatalogHome() + SEP + "szablony" + SEP + "projekt-k3b");

            Path pPlikSzablonu = Paths.get(pKatalogSzablonu + SEP + "maindata.xml");

            Path pPlikMime=Paths.get(pKatalogSzablonu+SEP+"mimetype");

            String pSzablon = null;

            pSzablon = String.join("\n", Files.readAllLines(pPlikSzablonu));


            Path pPlik = Paths.get(aKatalog + SEP + "maindata.xml");

            try (BufferedWriter writer = Files.newBufferedWriter(pPlik)) {
                String pStr = pSzablon.replace(aWzorzec, aTekst);
                writer.write(pStr);
                writer.flush();

            }

            Files.copy(pPlikMime,Paths.get(aKatalog+SEP+"mimetype"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
