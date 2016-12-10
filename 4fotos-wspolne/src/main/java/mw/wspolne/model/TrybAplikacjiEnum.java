package mw.wspolne.model;

/**
 * Created by mw on 20.01.16.
 */
public enum TrybAplikacjiEnum {
    OBSLUGA_ZDJEC("Obsługa galerii"),
    OBSLUGA_WWW("Obsługa galerii WWW");
    private String etykieta;

    TrybAplikacjiEnum(String etykieta) {
        this.etykieta = etykieta;
    }

    public String toString(){
        return etykieta;
    }
}

