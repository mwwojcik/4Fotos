package mw.uifx.wspolne;

/**
 * Created by Mariusz.Wojcik on 2016-08-16.
 */
public enum LokalizacjeFXMLEnum {
    ZASOBY_GLOWNE_WIDOK("/mw/uifx/zasoby_glowne/zasoby-glowne.fxml","4Fotos"),
    IMPORT_ZDJEC_OKNO_MODALNE("/mw/uifx/zasoby_glowne/zasoby-glowne-import-zdjec.fxml","Import zdjęć"),
    SEGREGACJA_ZDJEC_OKNO_MODALNE("/mw/uifx/zasoby_glowne/zasoby-glowne-segregacja-zdjec.fxml","Segregacja zdjęć"),
    PODGLAD_DJECIA_OKNO_MODALNE("/mw/uifx/zasoby_glowne/zasoby-glowne-podglad-zdjecia.fxml","Podgląd zdjęcia"),

    PASEK_POSTEPU("/mw/uifx/wspolne/pasek-postepu.fxml","Postęp"),
    GALERIA_WWW_WIDOK("/mw/uifx/galeria_www/galeria-www.fxml","Galeria WWW");

    private String url;
    private String tytul;

    LokalizacjeFXMLEnum(String url,String aTytul) {
        this.url = url;tytul=aTytul;
    }

    public String getUrl() {
        return url;
    }

    public String getTytul() {
        return tytul;
    }
}
