package mw.wspolne.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mw on 17.01.16.
 */
public class GalerieWWW {

    private List<KategoriaWWW> listaKategorii;

    public List<KategoriaWWW> getListaKategorii() {
        if(listaKategorii ==null){
            listaKategorii =new ArrayList<KategoriaWWW>();
        }
        return listaKategorii;
    }
}
