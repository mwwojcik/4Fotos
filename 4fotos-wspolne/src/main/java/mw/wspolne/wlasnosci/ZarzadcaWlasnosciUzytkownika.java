package mw.wspolne.wlasnosci;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.*;

/**
 * Wymaga ustawienia GUCIO_HOME i GUCIO_DOKUMENTY
 */
/*
@Component
@Deprecated
public class ZarzadcaWlasnosciUzytkownika {


    //singleton
    private static ZarzadcaWlasnosciUzytkownika zarzadcaWlasnosci;
    //singleton

    public static synchronized ZarzadcaWlasnosciUzytkownika podajInstancje() {

        if (zarzadcaWlasnosci == null) {
            zarzadcaWlasnosci = new ZarzadcaWlasnosciUzytkownika();
            zarzadcaWlasnosci.init();
        }

        return zarzadcaWlasnosci;
    }

    private Properties properties = new Properties();

    private String hasloJira = null;
@PostConstruct
    private void init() {
        try {

            String metrykiHomeStr = System.getenv("FOTOS_HOME");
            String pPlik = metrykiHomeStr + System.getProperty("file.separator") + "4fotos.properties";
            System.out.println(pPlik);
            InputStream in = new FileInputStream(pPlik);

            properties.load(in);




        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String podajKatalogGlownyGalerii(){
        return podajWartoscWlasciwosci(NazwaWlasnosciEnum.KATALOG_GALERII);
    }

    public String podajWartoscWlasciwosci(NazwaWlasnosciEnum aKluczEnum) {

            return properties.getProperty(aKluczEnum.toString());

    }




    public void wczytaj() {
        properties.clear();
        init();
    }

    public String podajKatalogHome() {
        return System.getenv("FOTOS_HOME");
    }





}
*/