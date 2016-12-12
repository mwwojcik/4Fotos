package mw.uslugi.stan;

import mw.wspolne.model.io.Katalog;
import mw.wspolne.model.io.Plik;
import mw.wspolne.wlasnosci.KonfiguratorAplikacji;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Mariusz.Wojcik on 2016-08-17.
 */
@Component
public class ZarzadcaStanu{

    private Map<String,Katalog> mapaKatalogowGalerii;

    @Autowired
    private KonfiguratorAplikacji konfiguratorAplikacji;

    @PostConstruct
    private void inicjalizujStan(){
        try {

            mapaKatalogowGalerii =Files.walk(Paths.get(konfiguratorAplikacji.getKatalogZestawowZdjec()),1).filter
                    (p->Files.isDirectory(p)).map(p->new Katalog(p,podajPliki(p)))
                    .collect(Collectors.toMap(k -> k.getNazwa(), k -> k));

        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

    private List<Plik>podajPliki(Path aKat){
        List<Plik> pPliki=null;
        try {
            pPliki=Files.find(aKat,10,(p,attr)->
                    Files.isRegularFile(p)
            ).map(p->new Plik(p)).collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return pPliki;
    }

    public List<Katalog> getMapaKatalogowGalerii() {
        return mapaKatalogowGalerii.values().stream().collect(Collectors.toList());
    }

    public Katalog podajKatalogPoKluczu(String aKlucz){
        return mapaKatalogowGalerii.get(aKlucz);
    }

    public List<Plik> podajPlikiDlaKatalogu(String aKatalog){
        return mapaKatalogowGalerii.get(aKatalog).getPliki();
    }
}
