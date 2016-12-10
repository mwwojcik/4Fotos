package mw.wspolne.model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mw on 17.01.16.
 */
public class KategoriaWWW extends Zasob{

    public KategoriaWWW(String aEtykieta, Path katalog) {
        super(aEtykieta, katalog);
    }

    List<Galeria>listaGalerii;

    public List<Galeria> getListaGalerii() {
        if(listaGalerii==null){
            listaGalerii=new ArrayList<Galeria>();
        }
        return listaGalerii;
    }


}
