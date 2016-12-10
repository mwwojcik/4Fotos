package mw.wspolne.model;

/**
 * Created with IntelliJ IDEA.
 * User: Mariusz.Wojcik
 * Date: 08.03.14
 * Time: 16:54
 * To change this template use File | Settings | File Templates.
 */
public enum TypyPlikowEnum {
    RAW("Raw", "arw"),
    JPG("Jpg", "jpg"),
    XCF("Xcf", "xcf"),
    NIEZNANY("Nieznany", "Nieznany");

    String rozszerzenie;
    String etykieta;

    private TypyPlikowEnum(String aEtykieta, String rozszerzenie) {
        this.rozszerzenie = rozszerzenie;
        this.etykieta = aEtykieta;
    }

    public String getRozszerzenie() {
        return rozszerzenie;
    }

    public String getEtykieta() {
        return etykieta;
    }

    public static TypyPlikowEnum podajTypDlaNazwy(String aNazwa) {

        for (TypyPlikowEnum pTyp : TypyPlikowEnum.values()) {
            if (aNazwa.toUpperCase().endsWith(pTyp.getRozszerzenie().toUpperCase())) {
                return pTyp;
            }

        }
        return NIEZNANY;
    }
}
