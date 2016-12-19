package mw.uslugi;


import mw.uslugi.io.ZarzadcaLogowania;
import mw.uslugi.stan.ZarzadcaStanuGaleriiWWW;
import mw.wspolne.model.*;
import mw.wspolne.model.io.ZbiorDyskowy;
import mw.wspolne.wlasnosci.KonfiguratorAplikacji;
import mw.wspolne.zdarzenia.ZdarzenieInicjalizacjiPaskaPostepu;
import mw.wspolne.zdarzenia.ZdarzenieInkrementacjiPaskaPostepu;
import mw.wspolne.zdarzenia.publikacja.PublikujacyZdarzeniaBean;
import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.CRC32;

/**
 * Created by mw on 14.01.16.
 */
@Component
public class ZarzadzanieGaleriaWWWUslugaImpl implements ZarzadzanieGaleriaWWWUsluga {

    @Autowired
    protected KonfiguratorAplikacji konfiguratorAplikacji;

    @Autowired
    protected PublikujacyZdarzeniaBean publikujacy;

    private Map<String, String> mapaCRC = new HashMap<String, String>();

    private String KATALOG_ZRODLOWY = null;

    private String KATALOG_DOCELOWY = null;


    @PostConstruct
    private void init(){
        KATALOG_ZRODLOWY = konfiguratorAplikacji.getGaleria().getZrodlo();
        KATALOG_DOCELOWY = konfiguratorAplikacji.getGaleria().getCel();

        Path pPlikSzablonuGaleria = Paths.get(konfiguratorAplikacji.getKatalogAplikacji()).resolve("szablony").resolve("galeria.html");

        Path pPlikSzablonuKategoria = Paths.get(konfiguratorAplikacji.getKatalogAplikacji()).resolve("szablony" ).resolve("kategoria.html");

        try {
            SZABLON_KATEGORIA = new String(Files.readAllBytes(pPlikSzablonuKategoria));
            SZABLON_GALERIA = new String(Files.readAllBytes(pPlikSzablonuGaleria));

        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Autowired
    private ZarzadcaStanuGaleriiWWW stanGaleriiWWW;


    private int WYMIAR_ZDJECIA = 720;
    private int WYMIAR_MINIATURA = 110;

    private String SZABLON_KATEGORIA = null;
    private String SZABLON_GALERIA = null;

    private String TEMPLATE_GALERIA = "ELEMENTY_GALERII_TEMPLATE";
    private String TEMPLATE_KATEGORIA = "ELEMENTY_KATEGORII_TEMPLATE";
    private String TEMPLATE_TYTUL = "TYTUL";

    private String SZABLON_ELEMENTU_GALERII = "" +
            "<li class=\"col-xs-6 col-sm-4 col-md-3\" data-src=\"SCIEZKA_OBRAZEK\" >\n" +
            "                    <a href=\"\">\n" +
            "                        <img class=\"img-responsive\" src=\"SCIEZKA_MINIATURA\">\n" +
            "                    </a>\n" +
            "<span class=\"podpis_galerii\">PODPIS_GALERII</span>" +
            "                </li>";

    private String SZABLON_ELEMENTU_KATEGORII = "" +
            "<li class=\"col-xs-6 col-sm-4 col-md-3\">\n" +
            "                    <a href=\"SCIEZKA_ROOT_KATEGORII\">\n" +
            "                        <img class=\"img-responsive\" src=\"SCIEZKA_MINIATURA\">\n" +
            "                    </a>\n" +
            "<span class=\"podpis_galerii\">PODPIS_GALERII</span>" +
            "                </li>";


    /*class Zdjecie {
        private Path miniatura;
        private Path zdjecie;
    }*/



    @Override
    public GalerieWWW podajGalerieWWW() {
        return stanGaleriiWWW.podajGalerieWWW();
    }


    @Override
    public List<ZbiorDyskowy> przekonwertujDoZbiorowDyskowych(Zasob aZbior) {
        if (aZbior == null) {
            return null;
        }

            if (aZbior instanceof KategoriaWWW) {
                KategoriaWWW pKat = (KategoriaWWW) aZbior;

                List<ZbiorDyskowy> pWynik = new ArrayList<>();

                pKat.getListaGalerii().forEach(gal ->
                        {
                            gal.getObrazki().forEach(o -> pWynik.add(przekonwertujZasobDoZbioru(o, gal.getEtykieta())));
                        }
                );
                return pWynik;

            } else if (aZbior instanceof Galeria) {
                Galeria pGal = (Galeria) aZbior;

                List<ZbiorDyskowy> pWynik = new ArrayList<>();
                pGal.getObrazki().forEach(o -> pWynik.add(przekonwertujZasobDoZbioru(o, pGal.getEtykieta())));

                return pWynik;
            }else{
                throw new IllegalArgumentException("Niedozwolony typ elementu!");
            }

        }

    private ZbiorDyskowy przekonwertujZasobDoZbioru(Zasob aZasob, String aKatalog) {
        try {
            ZbiorDyskowy pZbior = new ZbiorDyskowy(aZasob.getEtykieta(), Files.size(aZasob.getSciezka()), aKatalog);
            return pZbior;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }


    private Long obliczSumeCRC(String aSciezkaDoPliku) {

        InputStream in = null;
        try {
            in = new FileInputStream(aSciezkaDoPliku);

            CRC32 crc = new CRC32();
            int c;
            while ((c = in.read()) != -1) {
                crc.update(c);
            }

            return crc.getValue();
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }


    private String przygotujMetryczkeGalerii(Galeria aGaleria) {
        String pGalStr = (SZABLON_ELEMENTU_KATEGORII.replace("SCIEZKA_ROOT_KATEGORII", aGaleria.getSciezka().toFile().getName() + "/index.html"));
        pGalStr = (pGalStr.replace("SCIEZKA_MINIATURA", aGaleria.getSciezka().toFile().getName() + "/" + ObrazkiHelper.podajNazwePostfixowana(aGaleria.getObrazki().get(0).getSciezka(), "-miniatura")));
        pGalStr = (pGalStr.replace("PODPIS_GALERII", aGaleria.getEtykieta()));
        return pGalStr;
    }

    private String przygotujMetryczkeZdjecia(Path aZdjecie) {
        if (aZdjecie == null || !aZdjecie.toFile().exists()) {
            return null;
        }
        String pStr = SZABLON_ELEMENTU_GALERII.replace("SCIEZKA_OBRAZEK", aZdjecie.toFile().getName());
        pStr = pStr.replace("SCIEZKA_MINIATURA", ObrazkiHelper.podajNazwePostfixowana(aZdjecie, "-miniatura"));
        pStr = pStr.replace("PODPIS_GALERII", aZdjecie.toFile().getName().replace(".jpg", ""));
        return pStr;

    }

    private boolean czyGenerowacPlik(Path aSciezka) {
        String pNazwa = aSciezka.toFile().getName();
        Long pSuma = obliczSumeCRC(aSciezka.toFile().getAbsolutePath());
        if (!mapaCRC.containsKey(pNazwa)) {
            mapaCRC.put(pNazwa, String.valueOf(pSuma));
            return true;
        } else {
            return (!mapaCRC.get(pNazwa).equals(String.valueOf(pSuma)));
        }

    }


    @Override
    public void aktualizujPlikStruktury() {
        long czasStart = System.currentTimeMillis();
        ZarzadcaLogowania.podajInstancje().logujKomunikat("Inicjalizacja rozpoczęta.");
        try {

            Path root = Paths.get(KATALOG_ZRODLOWY);

            Path pGaleriaPlik = Paths.get(konfiguratorAplikacji.getKatalogAplikacji()).resolve("galerie.xml");

            URL pURL = pGaleriaPlik.toUri().toURL();

            GalerieWWW pGalerie = stanGaleriiWWW.podajGalerieWWW(DokumentXMLHelper.instancja().podajDokument(pURL));

            Document pDok = DokumentXMLHelper.instancja().podajDokument(pGalerie);

            DokumentXMLHelper.instancja().zapiszDoPliku(pGaleriaPlik, pDok);


        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        long czasStop = System.currentTimeMillis();

        ZarzadcaLogowania.podajInstancje().logujKomunikat("Czas przetwarzania=>" + (czasStop - czasStart) / 1000 + " sekund");
    }

    @Override
    public void generujGalerieWWW() {
        long czasStart = System.currentTimeMillis();

        ZarzadcaLogowania.podajInstancje().logujKomunikat("Galerie WWW usluga");

        GalerieWWW pGalerie = podajGalerieWWW();

        eksportujGalerie(pGalerie);

        long czasStop = System.currentTimeMillis();

        ZarzadcaLogowania.podajInstancje().logujKomunikat("Czas przetwarzania=>" + (czasStop - czasStart) / 1000 + " sekund");
    }

    class Inkrement {
        public int wartosc;
    }

    private void eksportujGalerie(GalerieWWW aGalerieWWW) {

        if (aGalerieWWW == null) {
            return;
        }

        final Inkrement inkrement = new Inkrement();
        inkrement.wartosc = 1;

        long liczbaZdjec =aGalerieWWW.getListaKategorii().stream().flatMap
                (kat -> kat.getListaGalerii().stream().flatMap(g -> g.getObrazki().stream())).count();

        publikujacy.publikujZdarzenie(new ZdarzenieInicjalizacjiPaskaPostepu(this,liczbaZdjec,0,"Przygotowanie galerii WWW",""));

        aGalerieWWW.getListaKategorii().forEach(kat -> {
            wygenerujPlikKategorii(kat);
            kat.getListaGalerii().forEach(gal -> {
                Path cel = zalozKatalog(gal.getSciezka());
                wygenerujPlikGalerii(gal,cel);
                gal.getObrazki().stream().forEach(zd -> {
                    wygenerujObrazki(zd,cel);
                    publikujacy.publikujZdarzenie(new ZdarzenieInkrementacjiPaskaPostepu(this,liczbaZdjec,inkrement.wartosc++,"Przygotowanie galerii WWW",
                            "Zapisano obrazek=>"+zd.podajNazwe()));
                });
            });
        });

    }

    private void wygenerujObrazki(Obrazek zd,Path aCel){
        ObrazkiHelper.przeskalujObrazek(zd.getSciezka(), aCel, WYMIAR_ZDJECIA, null);
        ObrazkiHelper.przeskalujObrazek(zd.getSciezka(), aCel, WYMIAR_MINIATURA, "-miniatura");
    }

    /**
     * Zakłada plik index.html zawierający miniatury galerii.
     * @param aGaleria
     * @param aKatalogCel
     */
    private void wygenerujPlikGalerii(Galeria aGaleria,Path aKatalogCel){
        try {
            List<String> pListaElementowZdjec = aGaleria.getObrazki().stream().map(z -> przygotujMetryczkeZdjecia(z.getSciezka())).collect(Collectors.toList());
            String SZABLON_GALERIA_LOKALNY = SZABLON_GALERIA.replace(TEMPLATE_TYTUL, aGaleria.getEtykieta());
            zapiszDoPliku(aKatalogCel.resolve("index.html"),
                    pListaElementowZdjec, TEMPLATE_GALERIA, SZABLON_GALERIA_LOKALNY);
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * Metoda zakłada katalog, oraz generuje plik index.html, stanowiący punkt wejścia
     * do galerii z danej kategorii.
     * @param aKategoria
     */
    private void wygenerujPlikKategorii(KategoriaWWW aKategoria){
        Path katCel = zalozKatalog(aKategoria.getSciezka());
        //todo zapisz do pliku
        try {
            List<String> pListaElementow = aKategoria.getListaGalerii().stream().map(k -> przygotujMetryczkeGalerii(k)).collect(Collectors.toList());

            String SZABLON_KATEGORIA_LOKALNY = SZABLON_KATEGORIA.replace(TEMPLATE_TYTUL, aKategoria.getEtykieta());
            zapiszDoPliku(katCel.resolve("index.html"),
                    pListaElementow, TEMPLATE_KATEGORIA, SZABLON_KATEGORIA_LOKALNY);

        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private void zapiszDoPliku(Path aPlik, List<String> aTekst, String aWzorzec, String aSzablon) throws IOException {


        try (BufferedWriter writer = Files.newBufferedWriter(aPlik)) {

            String pStr = String.join("\n", aTekst);
            pStr = aSzablon.replace(aWzorzec, pStr);
            writer.write(pStr);
            writer.flush();

        }


    }

    private Path zalozKatalog(Path aKatalogZrodlowy) {
        Path pKat = podajKatalogDocelowy(aKatalogZrodlowy);
        try {
            if (Files.notExists(pKat)) {
                Files.createDirectories(pKat);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return pKat;
    }


    private Path podajKatalogDocelowy(Path aKatalogZrodlowy){

        Path pKATALOG_ZRODLOWY=Paths.get(KATALOG_ZRODLOWY);
        Path pKatalogWyjsciowy=Paths.get(KATALOG_DOCELOWY);


        for(int i=0;i<aKatalogZrodlowy.getNameCount();i++){
           //wybieram poziomy zaglebienia ponizej katalogu zrodlowego
            if((i<pKATALOG_ZRODLOWY.getNameCount())&&(aKatalogZrodlowy.getName(i).equals(pKATALOG_ZRODLOWY.getName(i)))){
               continue;
           }
           pKatalogWyjsciowy=pKatalogWyjsciowy.resolve(aKatalogZrodlowy.getName(i));
        }
        return pKatalogWyjsciowy;
    }


    /*private Future<Path> przeskalujObrazekAsynchronicznie(Path aPlik, Path aKatalogDocelowy, int aRozmiarDocelowy, String aPostfixNazwy) {
        CompletableFuture<Path> futurePrice = new CompletableFuture<Path>();
        new Thread(() -> {
            //Path pObrazek = ObrazkiHelper.przeskalujObrazek(aPlik, aKatalogDocelowy, aRozmiarDocelowy,KATALOG_ZRODLOWY);
            //futurePrice.complete(pObrazek);
        }).start();
        return futurePrice;
    }*/


}
