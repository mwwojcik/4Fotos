package mw.uslugi.stan;

import mw.uslugi.DokumentXMLHelper;
import mw.uslugi.ObrazkiHelper;
import mw.wspolne.model.Galeria;
import mw.wspolne.model.GalerieWWW;
import mw.wspolne.model.KategoriaWWW;
import mw.wspolne.model.Obrazek;
import mw.wspolne.wlasnosci.KonfiguratorAplikacji;
import mw.wspolne.wlasnosci.NazwaWlasnosciEnum;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by mw on 01.10.16.
 */
@Component
public class ZarzadcaStanuGaleriiWWW {

    @Autowired
    protected KonfiguratorAplikacji konfiguratorAplikacji;

    private String KATALOG_ZRODLOWY = null;
    private String SEP = KonfiguratorAplikacji.separator();

    private String XML_ATRYBUT_NAZWA = "nazwa";
    private String XML_ATRYBUT_KATALOG = "katalog";
    private String XML_ATRYBUT_PLIK = "plik";

    private String XML_ELEM_GALERIE = "galerie";
    private String XML_ELEM_KATEGORIA = "kategoria";
    private String XML_ELEM_GALERIA = "galeria";
    private String XML_ELEM_OBRAZ = "obraz";


    @PostConstruct
    private void init(){
        KATALOG_ZRODLOWY = konfiguratorAplikacji.getGaleria().getZrodlo();
    }


    public GalerieWWW podajGalerieWWW() {
        Path root = Paths.get(KATALOG_ZRODLOWY);

        URL pURL = null;
        try {
            pURL = (new File(konfiguratorAplikacji.getKatalogAplikacji()+ SEP + "galerie.xml")).toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }

        return podajGalerieWWW(DokumentXMLHelper.instancja().podajDokument(pURL));
    }

    public GalerieWWW podajGalerieWWW(Document aDokument) {

        final GalerieWWW pGalerie = new GalerieWWW();

        /*
         WAZNE!!!
        * */
        //init();
        // inicjalizujCRC();

        //Mapa elementów XML pogrupowanych wedlug kategorii
        Map<Element, List<Element>> pMapa = przetwarzajDokument(aDokument);

        pMapa.entrySet().stream().forEach(e -> {
                    Element pKatElem = e.getKey();
                    final KategoriaWWW pKat = stworzObiektKategoriiWWW(pKatElem);

                    if (pKat.czyIstnieje()) {
                        pGalerie.getListaKategorii().add(pKat);
                        //lista elementow xml reprezentujacych galerie
                        //znajdujace sie w kategorii
                        List<Element> pGalElemLista = e.getValue();
                        pGalElemLista.stream().forEach(
                                pGalElem -> {
                                    Galeria pGaleria = stworzObiektGaleriiWWW(pGalElem, pKat.getSciezka());

                                    if (pGaleria.czyIstnieje()) {
                                        //do kategorii dodajemy kolejne galerie
                                        pKat.getListaGalerii().add(pGaleria);

                                        // pListaPlikow.removeIf(p->!czyGenerowacPlik(p));

                                        pGaleria.getObrazki().addAll(podajListeObrazow(pGalElem, pGaleria));
                                        //jesli posiada jakiekolwiek zdjecia
                                      /*  if (!pGaleria.getListaZdjecWWW().isEmpty()) {
                                            przygotujMetryczkeGalerii(pGaleria);
                                        }*/
                                        //dla kazdego pliku uzupelniamy
                                        /*pGaleria.getListaZdjecWWW().stream().forEach(p -> {
                                            pGaleria.getZdjeciaStr().add(przygotujMetryczkeZdjecia(p.getSciezka()));
                                        });*/

                                    }
                                }
                        );
                    }
                }
        );

        // zapiszCRC();

        return pGalerie;
    }



    private KategoriaWWW stworzObiektKategoriiWWW(Element katElemXml) {
        KategoriaWWW pKat = new KategoriaWWW(katElemXml.attributeValue(XML_ATRYBUT_NAZWA),
                Paths.get(KATALOG_ZRODLOWY + SEP + katElemXml.attributeValue(XML_ATRYBUT_KATALOG)));
        return pKat;
    }

    private Galeria stworzObiektGaleriiWWW(Element galElemXml, Path aKatalogKategorii) {
        Galeria pGaleria = new Galeria(galElemXml.attributeValue(XML_ATRYBUT_NAZWA), Paths.get(aKatalogKategorii.toString() + SEP + galElemXml.attributeValue(XML_ATRYBUT_KATALOG)));
        return pGaleria;
    }


    private List<Obrazek> podajListeObrazow(Element aObiektGaleriiXml, Galeria aGaleria) {
        List<Obrazek> pObrazy = DokumentXMLHelper.instancja().podajListeObrazowZXML(aObiektGaleriiXml, aGaleria);
        pObrazy.removeIf(o -> !o.czyIstnieje());
        //todo usunac duplikaty

        List<Obrazek> listaZKatalogu = podajListeObazowZKatalogu(aGaleria.getSciezka());

        listaZKatalogu.removeIf(o -> {
            return pObrazy.contains(o);
        });

        pObrazy.addAll(listaZKatalogu);

        return pObrazy;
    }


   private Map<Element, List<Element>> przetwarzajDokument(Document document) {
        List<Element> pListaGalerii = document.selectNodes("//" + XML_ELEM_GALERIE + "/" + XML_ELEM_KATEGORIA + "/" + XML_ELEM_GALERIA);

        if (pListaGalerii == null || pListaGalerii.isEmpty()) {
            return null;
        }

        Map<Element, List<Element>> pMapa = pListaGalerii.stream().collect(Collectors.groupingBy(e -> e.getParent()));

        return pMapa;
    }

    private List<Obrazek> podajListeObazowZKatalogu(Path aKatalog) {
        if (aKatalog == null || !aKatalog.toFile().exists()) {
            return null;
        }

        try {
            List<Obrazek> pListaPlikow = Files.walk(aKatalog)
                    .filter(Files::isRegularFile)
                    .filter(f -> ObrazkiHelper.czyJestObrazem(f) && !ObrazkiHelper.czyJestWykluczony(f))
                    .sorted((p1, p2) -> {
                                return p1.toFile().getName().compareTo(p2.toFile().getName());
                            }
                    ).map(p -> new Obrazek(p))
                    .collect(Collectors.toList());
            return pListaPlikow;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
