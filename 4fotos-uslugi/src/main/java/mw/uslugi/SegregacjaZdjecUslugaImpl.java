package mw.uslugi;



import mw.uslugi.bazowe.UslugaBazowa;
import mw.uslugi.io.ZarzadcaLogowania;
import mw.wspolne.model.*;
import mw.wspolne.wlasnosci.KonfiguratorAplikacji;

import mw.wspolne.zdarzenia.ZdarzenieInicjalizacjiPaskaPostepu;
import mw.wspolne.zdarzenia.ZdarzenieInkrementacjiPaskaPostepu;
import org.dom4j.Document;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created with IntelliJ IDEA.
 * User: mw
 * Date: 18.03.14
 * Time: 20:24
 * To change this template use File | Settings | File Templates.
 */
@Component
public class SegregacjaZdjecUslugaImpl extends UslugaBazowa implements SegregacjaZdjecUsluga {
    //private String SEP = KonfiguratorAplikacji.separator();

    private Path KATALOG_GLOWNY_CACHE = null;

    private int WIELKOSC_OBRAZKA = 300;

    @PostConstruct
    private void init(){
        KATALOG_GLOWNY_CACHE = Paths.get(konfiguratorAplikacji.getKatalogAplikacji()).resolve( konfiguratorAplikacji.getObszarRoboczy().getCache());
    }

    class Licznik {
        public int licznik = 1;
    }



    public boolean czyMiniaturyPrzygotowane(String aRootGaleriiWejsciowej){
        Path cel = KATALOG_GLOWNY_CACHE.resolve(aRootGaleriiWejsciowej);
        return Files.exists(cel);
    }

    public void wygenerujMiniatury(Galeria aRootGaleriiWejsciowej) {

        try {
            if (!KATALOG_GLOWNY_CACHE.toFile().exists()) {
                Files.createDirectory(KATALOG_GLOWNY_CACHE);
            }
            Path pGaleriaWejsciowa = aRootGaleriiWejsciowej.getSciezka().resolve(konfiguratorAplikacji.getPodkatalog().getJpg());

            List<Obrazek> pListaObrazkowOryginalnych = podajObrazkiZkatalogu(pGaleriaWejsciowa);


            Path cel = KATALOG_GLOWNY_CACHE.resolve(aRootGaleriiWejsciowej.podajNazwe());
            Map<String, Double> pObrazkiXml = podajOceny(aRootGaleriiWejsciowej, cel);

            if (!cel.toFile().exists()) {
                Files.createDirectory(cel);

               // MonitorPaskaPostepu.podajInstancje().inicjalizujPasekPostepu("Przygotowywanie podglądu", pListaObrazkowOryginalnych.size());
                getPublikujacy().publikujZdarzenie(new ZdarzenieInicjalizacjiPaskaPostepu(this,pListaObrazkowOryginalnych.size(),0,"Przygotowanie podglądu",""));
                Licznik pLicznik = new Licznik();
                pListaObrazkowOryginalnych.stream().forEach(o -> {
                    //  MonitorPaskaPostepu.podajInstancje().aktualizujStan(pLicznik.licznik++);
                    getPublikujacy().publikujZdarzenie(new ZdarzenieInkrementacjiPaskaPostepu(this,pListaObrazkowOryginalnych.size(),pLicznik.licznik++,"Przygotowanie podglądu",o.getSciezka().getFileName().toString()));

                    o.setMiniatura(new Obrazek(ObrazkiHelper.przeskalujObrazek(o.getSciezka(), cel, WIELKOSC_OBRAZKA,null)));
                    if (pObrazkiXml != null && pObrazkiXml.containsKey(o.podajNazwe())) {
                        o.setOcena(Double.valueOf(pObrazkiXml.get(o.podajNazwe())));
                        o.getMiniatura().setOcena(Double.valueOf(pObrazkiXml.get(o.podajNazwe())));
                    }
                });

            } else {
                return;
            }

        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

    }

    public void podajMiniatury(Galeria aRootGaleriiWejsciowej) {

        try {
            if (!KATALOG_GLOWNY_CACHE.toFile().exists()) {
                Files.createDirectory(KATALOG_GLOWNY_CACHE);
            }
            Path pGaleriaWejsciowa = aRootGaleriiWejsciowej.getSciezka().resolve(konfiguratorAplikacji.getPodkatalog().getJpg());
            List<Obrazek> pListaObrazkowOryginalnych = podajObrazkiZkatalogu(pGaleriaWejsciowa);


            Path cel =KATALOG_GLOWNY_CACHE.resolve(aRootGaleriiWejsciowej.podajNazwe());
            Map<String, Double> pObrazkiXml = podajOceny(aRootGaleriiWejsciowej, cel);

            if (!cel.toFile().exists()) {
                throw new IllegalArgumentException("Katalog z miniaturami =>"+cel.toString()+" nie istnieje!");

            } else {
                Map<String, Obrazek> pMapaMiniatur = podajObrazkiZkatalogu(cel).stream().collect(Collectors.toMap(Obrazek::podajNazwe, Function.identity()));
                pListaObrazkowOryginalnych.stream().forEach(o -> {
                    o.setMiniatura(pMapaMiniatur.get(o.podajNazwe()));
                    if (pObrazkiXml != null && pObrazkiXml.containsKey(o.podajNazwe())) {
                        o.getMiniatura().setOcena(Double.valueOf(pObrazkiXml.get(o.podajNazwe())));
                        o.setOcena(Double.valueOf(pObrazkiXml.get(o.podajNazwe())));
                    }
                });
            }
            aRootGaleriiWejsciowej.getObrazki().addAll(pListaObrazkowOryginalnych);


        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

    }


    private Map<String, Double> podajOceny(Galeria aGaleria, Path aKatalogPlikuOcen) {
        Path pPlikOcen = aKatalogPlikuOcen.resolve(
                Galeria.PLIK_OCEN);
        if (Files.exists(pPlikOcen)) {
            try {
                Document pOceny = DokumentXMLHelper.instancja().podajDokument(pPlikOcen.toFile().toURL());
                List<Obrazek> pObrazki = DokumentXMLHelper.instancja().podajListeObrazowZXML(pOceny.getRootElement(), aGaleria);
                return pObrazki.stream().collect(Collectors.toMap((Obrazek o)->o.podajNazwe(), Obrazek::getOcena));
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return null;
    }

    @Override
    public void zapiszOceny(Galeria aGaleria) {
        Path cel = KATALOG_GLOWNY_CACHE.resolve(aGaleria.podajNazwe());
        Document pDok = DokumentXMLHelper.instancja().podajDokumentOcen(aGaleria);
        DokumentXMLHelper.instancja().zapiszDoPliku(cel.resolve(Galeria.PLIK_OCEN), pDok);
    }

    @Override
    public void przenosWybraneDoUsuniecia(Galeria aGaleria) {
        String katUsNazwa = konfiguratorAplikacji.getPodkatalog().getDousuniecia();
        Path podkatalogUs = aGaleria.getSciezka().resolve(katUsNazwa);

        try {
            if (!Files.exists(podkatalogUs)) {
                ZarzadcaLogowania.podajInstancje().logujKomunikat("Założono katalog" + podkatalogUs);
                Files.createDirectory(podkatalogUs);
            }

        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
         List<Obrazek> obrazkiDoUsuniecia = aGaleria.getObrazki().stream().filter(o -> o.podajOcene() == 0).collect(Collectors.toList());

        if(obrazkiDoUsuniecia!=null) {
            System.out.println("Do przeniesienia wybrano=>" + obrazkiDoUsuniecia.size());

        }

        if(obrazkiDoUsuniecia==null){
            return;
        }

        List<String> maskiPlikowDoUsuniecia=obrazkiDoUsuniecia.stream().map
                (o->o.getSciezka().getFileName().toString()).map(s->s.substring(0,s.indexOf("."))).collect(Collectors.toList());


        Licznik pLicznik = new Licznik();
        getPublikujacy().publikujZdarzenie(new ZdarzenieInicjalizacjiPaskaPostepu(this,maskiPlikowDoUsuniecia.size(),pLicznik.licznik++,"Przenoszenie do usuniętych",""));

        maskiPlikowDoUsuniecia.forEach(maska -> {
            try {
                getPublikujacy().publikujZdarzenie(new ZdarzenieInkrementacjiPaskaPostepu(this,maskiPlikowDoUsuniecia.size(),pLicznik.licznik++,"Przenoszenie do usuniętych",""));
                Stream<Path> pZnalezione = Files.find(aGaleria.getSciezka(), 10, (p, a) -> p.getFileName().toString().contains(maska));

                pZnalezione.forEach(p -> {
                    Path cel = podkatalogUs.resolve(p.getFileName());

                    int iter=1;

                    while(Files.exists(cel)){
                        cel= podkatalogUs.resolve(iter+p.getFileName().toString());
                        iter++;
                    }

                    ZarzadcaLogowania.podajInstancje().logujKomunikat("Przenosze zrodlo=>" + p.toString() + " cel=>" + cel.toString());
                    try {
                        Files.move(p,cel);
                    } catch (IOException e) {
                        throw new IllegalArgumentException(e);
                    }
                });

            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        });

        obrazkiDoUsuniecia.stream().forEach(o->{


                Path cel = podkatalogUs.resolve(o.getMiniatura().getSciezka().getFileName());
                ZarzadcaLogowania.podajInstancje().logujKomunikat("Przenosze zrodlo=>" + o.getMiniatura().getSciezka().toString() + " cel=>" + cel.toString());
                try {
                Files.move(o.getMiniatura().getSciezka(),cel);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        });



           /* List<Path> pPliki = new ArrayList<>();
            obrazkiDoUsuniecia.stream().forEach(o -> {
                try {
                    Files.walkFileTree(aGaleria.getSciezka(), new WizytatorPlikuDoUsuniecia(o.podajNazweBezRozszerzenia(), pPliki));
                    aGaleria.getObrazki().remove(o);
                    Files.delete(o.getMiniatura().getSciezka());
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }

            });

            pPliki.stream().forEach(f->{
                ZarzadcaLogowania.podajInstancje().logujKomunikat("USUNIETO=>"+f);
                Path cel= Paths.get(podkatalogUs+SEP+f.toFile().getName());
                try {
                    Files.move(f,cel);
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            });

        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }*/
    }

   /* class WizytatorPlikuDoUsuniecia extends SimpleFileVisitor<Path> {

        private String maskaNazwy;
        List<Path> listaDoUsuniecia;

        public WizytatorPlikuDoUsuniecia(String aMaskaNazwy, List<Path> aKolekcja) {
            maskaNazwy = aMaskaNazwy;
            listaDoUsuniecia = aKolekcja;

        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                throws IOException {

            if(file.toString().contains(maskaNazwy)){
                listaDoUsuniecia.add(file);
            }

            return super.visitFile(file, attrs);
        }
    }*/

    private List<Obrazek> podajObrazkiZkatalogu(Path aKatalog) {
        if (aKatalog == null || !aKatalog.toFile().exists()) {
            return null;
        }
        try {
            List<Obrazek> pLista = Files.walk(aKatalog)
                    .filter(Files::isRegularFile)
                    .filter(f -> ObrazkiHelper.czyJestObrazem(f) && !ObrazkiHelper.czyJestWykluczony(f))
                    .sorted((p1, p2) -> {
                                return p1.toFile().getName().compareTo(p2.toFile().getName());
                            }
                    )
                    .map(p -> new Obrazek(p))
                    .collect(Collectors.toList());
            return pLista;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }


}
