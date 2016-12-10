package mw.wspolne.model;

/**
 * Created by mw on 04.02.16.
 */
public enum TypPublikacjiEnum {

    WSZYSTKO("Pełna publikacja"),
    TYLKO_PLIKI_INDEKSOW ("Tylko pliki indeksów"),
    TYLKO_ZMIENIONE("Tylko zmienione"),
    TYLKO_KASOWANIE("Kasowanie zdalnej galerii");
    private String etykieta;

    TypPublikacjiEnum(String etykieta) {
        this.etykieta = etykieta;
    }

    public String getEtykieta() {
        return etykieta;
    }

    public static TypPublikacjiEnum getWartosc(String aWartosc){
        for(TypPublikacjiEnum pTyp:values()){
            if(pTyp.toString().compareToIgnoreCase(aWartosc)==0){
                return pTyp;
            }
        }
        return null;
    }

}
