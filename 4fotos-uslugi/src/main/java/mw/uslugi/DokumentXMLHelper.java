package mw.uslugi;

import mw.wspolne.model.*;
import mw.wspolne.wlasnosci.ZarzadcaWlasnosciUzytkownika;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Mariusz.Wojcik on 2016-01-21.
 */
public class DokumentXMLHelper {

    private String XML_ELEMENT_GALERIE = "galerie";
    private String XML_ELEMENT_KATEGORIA = "kategoria";
    private String XML_ELEMENT_GALERIA = "galeria";
    private String XML_ELEMENT_OBRAZ = "obraz";

    private String XML_ATRYBUT_NAZWA = "nazwa";
    private String XML_ATRYBUT_KATALOG = "katalog";
    private String XML_ATRYBUT_PLIK = "plik";
    private String XML_ATRYBUT_OCENA = "ocena";
    private String SEP = ZarzadcaWlasnosciUzytkownika.podajInstancje().separator();


    public Document podajDokument(URL url) {
        try {
            SAXReader reader = new SAXReader();
            reader.setEncoding("UTF-8");
            Document document = null;
            return reader.read(url);
        } catch (DocumentException e) {
            throw new IllegalArgumentException(e);
        }

    }

    public static DokumentXMLHelper instancja() {
        DokumentXMLHelper pHelper = new DokumentXMLHelper();
        return pHelper;
    }

    public Document podajDokument(GalerieWWW aRoot) {
        Document pDokument = DocumentHelper.createDocument();
        pDokument.setXMLEncoding("UTF-8");
        Element root = pDokument.addElement(XML_ELEMENT_GALERIE);

        aRoot.getListaKategorii().stream().forEach(kat -> {
            dodajElementDlaKategorii(kat, root);
        });

        return pDokument;
    }

    private Element dodajElementDlaKategorii(KategoriaWWW aKategoria, Element aRoot) {
        Element pKategoriaElem = aRoot.addElement(XML_ELEMENT_KATEGORIA);
        pKategoriaElem.addAttribute(XML_ATRYBUT_NAZWA, aKategoria.getEtykieta());
        pKategoriaElem.addAttribute(XML_ATRYBUT_KATALOG, podajOstatniElementSciezki(aKategoria.getSciezka()));

        aKategoria.getListaGalerii().stream().forEach(gal -> {
            dodajElementDlaGalerii(gal, pKategoriaElem);
        });

        return pKategoriaElem;
    }


    private Element dodajElementDlaGalerii(Galeria aGaleria, Element aRoot) {
        Element pGaleriaElem = aRoot.addElement(XML_ELEMENT_GALERIA);
        pGaleriaElem.addAttribute(XML_ATRYBUT_NAZWA, aGaleria.getEtykieta());
        pGaleriaElem.addAttribute(XML_ATRYBUT_KATALOG, podajOstatniElementSciezki(aGaleria.getSciezka()));

        aGaleria.getObrazki().stream().forEach(zd -> {
            dodajElementDlaObrazu(zd, pGaleriaElem);
        });

        return pGaleriaElem;
    }


    private Element dodajElementDlaObrazu(Obrazek aObraz, Element aRoot) {
        Element pObrazElem = aRoot.addElement(XML_ELEMENT_OBRAZ);
        pObrazElem.addAttribute(XML_ATRYBUT_PLIK, podajOstatniElementSciezki(aObraz.getSciezka()));
        return pObrazElem;
    }

    private String podajOstatniElementSciezki(Path aSciezka) {
        String pElementySciezki = (aSciezka.toFile().getName());
        return pElementySciezki;
    }

    public List<Obrazek> podajListeObrazowZXML(Element aObiektGaleriiXml, Galeria aGaleria) {
        if (aGaleria.getSciezka() == null || !aGaleria.getSciezka().toFile().exists()) {
            return null;
        }
        List<Element> pElemList = aObiektGaleriiXml.elements(XML_ELEMENT_OBRAZ);
        List<Obrazek> pListaObrazkow = null;
        if (pElemList != null) {
            pListaObrazkow = pElemList.stream().map(e -> {
                String pNazwa = e.attribute(XML_ATRYBUT_PLIK).getValue();
                String pOcena ="";
                Attribute pOcAttr=e.attribute(XML_ATRYBUT_OCENA);
                if(pOcAttr!=null){
                    pOcena=pOcAttr.getValue();
                }
                Path p = Paths.get(aGaleria.getSciezka().toFile().getAbsolutePath() + SEP + pNazwa);
                Obrazek pObrazek = new Obrazek(pNazwa, p);
                if (pOcena != null&&!pOcena.isEmpty()) {
                    pObrazek.setOcena(Double.valueOf(pOcena));
                }
                return pObrazek;
            }).collect(Collectors.toList());

        }



        return pListaObrazkow;
    }

    public Document podajDokumentOcen(Galeria aRoot) {
        Document pDokument = DocumentHelper.createDocument();
        pDokument.setXMLEncoding("UTF-8");
        Element root = pDokument.addElement(XML_ELEMENT_GALERIA);

        aRoot.getObrazki().stream().forEach(o -> {
            dodajElementOcenyDlaObrazka(o, root);
        });

        return pDokument;
    }

    private Element dodajElementOcenyDlaObrazka(Obrazek aObraz, Element aRoot) {
        Element pObrazElem = aRoot.addElement(XML_ELEMENT_OBRAZ);
        pObrazElem.addAttribute(XML_ATRYBUT_PLIK, aObraz.podajNazwe());
        pObrazElem.addAttribute(XML_ATRYBUT_OCENA, String.valueOf(aObraz.podajOcene()));
        return pObrazElem;
    }


    public void zapiszDoPliku(Path aPlik, Document aDokument) {
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            // lets write to a file
            XMLWriter writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(aPlik.toFile()), "utf-8"), format);

            writer.write(aDokument);
            writer.close();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public String zapiszDoStr(Document aDokument) {
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            // lets write to a file

            StringWriter pStr=new StringWriter();
            XMLWriter writer = new XMLWriter(pStr, format);
            writer.write(aDokument);
            writer.close();
            return pStr.toString();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }




    private String XML_ELEMENT_FILES="files";
    private String XML_ELEMENT_DIRECTORY="directory";
    private String XML_ELEMENT_FILE="file";
    private String XML_ELEMENT_URL="url";

    public Document podajDokumentDlaKopiiZapasowej(){
        Document pDokument = DocumentHelper.createDocument();
        pDokument.setXMLEncoding("UTF-8");
        Element root = pDokument.addElement(XML_ELEMENT_FILES);
        return pDokument;
    }

    public Element podajElementDlaKatalogu(Element parent,Path aKatalog){
        return parent.addElement(XML_ELEMENT_DIRECTORY).addAttribute("name",aKatalog.toFile().getName());
    }

    public Element podajElementDlaPliku(Element parent,Path aPlik){
        Element pPlikElem = parent.addElement(XML_ELEMENT_FILE).addAttribute("name",aPlik.toFile().getName());
        pPlikElem.addElement(XML_ELEMENT_URL).setText(aPlik.toString());
        return pPlikElem;
    }
}
