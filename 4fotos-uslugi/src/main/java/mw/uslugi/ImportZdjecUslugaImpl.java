package mw.uslugi;

import mw.uslugi.bazowe.UslugaBazowa;
import mw.uslugi.io.OperacjeIOUtil;
import mw.uslugi.io.ZarzadcaLogowania;

import mw.wspolne.model.TypyPlikowEnum;

import mw.wspolne.wlasnosci.KonfiguratorAplikacji;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Mariusz.Wojcik
 * Date: 06.03.14
 * Time: 18:35
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ImportZdjecUslugaImpl extends UslugaBazowa implements ImportZdjecUsluga{


    @Autowired
    private OperacjeIOUtil operacjeIOUtil;

    private Path katalogBazowy = null;
    private Path katalogAparatu = null;


    String SEP= KonfiguratorAplikacji.separator();

    class Katalog{
        Path root;
        Path jpg;
        Path raw;
    }

    @PostConstruct
    private  void init(){
        katalogBazowy = Paths.get(konfiguratorAplikacji.getKatalogZestawowZdjec());
        katalogAparatu = Paths.get(konfiguratorAplikacji.getKatalogAparatu());
    }

    private void sprawdzPoprawnoscPolaczenia(){

        if(!katalogAparatu.toFile().exists()){
            throw new IllegalStateException("Polaczenie niepoprawnie zainicjalizowane");
        }
    }

    private Katalog przygotujKatalogDoZgraniaZdjec(String postfix){

        ZarzadcaLogowania.podajInstancje().logujKomunikat("*********** Tworzenie katalogu docelowego *************");

        Date myDate=new Date();
        SimpleDateFormat pFormat=new SimpleDateFormat("yyyy-MM-dd");
        String pNazwaKataloguDocelowego=katalogBazowy.resolve(pFormat.format(myDate)).toString();//String.format('%tF', myDate);

        if(postfix!=null){
            pNazwaKataloguDocelowego+="-"+postfix;
        }

        int iter=1;
        String pNazwaKatDocelowegoNiezmienna=pNazwaKataloguDocelowego ;

        while((new File(pNazwaKataloguDocelowego)).exists()){
            pNazwaKataloguDocelowego=pNazwaKatDocelowegoNiezmienna;
            pNazwaKataloguDocelowego+="-"+iter;
            iter++;
        }

        Path pUtworzonyKatalog=Paths.get(pNazwaKataloguDocelowego);
        Katalog pWynik=new Katalog();
        try {

            pWynik.root=Files.createDirectory(pUtworzonyKatalog);
            pWynik.raw=utworzPodkatalog(pUtworzonyKatalog,konfiguratorAplikacji.getPodkatalog().getRaw());
            pWynik.jpg=utworzPodkatalog(pUtworzonyKatalog,konfiguratorAplikacji.getPodkatalog().getJpg());
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        ZarzadcaLogowania.podajInstancje().logujKomunikat(">>>>Utworzono katalog:"+pUtworzonyKatalog.toFile().getAbsolutePath()+"<<<<<");

        return pWynik;

    }

    private Path utworzPodkatalog(Path aBaza,String aNazwa)throws IOException{
        return Files.createDirectory(aBaza.resolve(aNazwa));
    }



    private boolean czySaPlikiDoPrzegrania(){

        if(FileUtils.sizeOfDirectory(katalogAparatu.toFile())==0) {
            return false;
        }
        else {
            return true;
        }
    }




    public Path importujZdjecia(String aPostfixDlaKatalogu){

        sprawdzPoprawnoscPolaczenia();

        if(czySaPlikiDoPrzegrania()){
            Katalog pKat=przygotujKatalogDoZgraniaZdjec(aPostfixDlaKatalogu);
            operacjeIOUtil.przeniesPliki(katalogAparatu,pKat.raw, TypyPlikowEnum.RAW);
            operacjeIOUtil.przeniesPliki(katalogAparatu,pKat.jpg, TypyPlikowEnum.JPG);
            return pKat.root;
        }else{
            ZarzadcaLogowania.podajInstancje().logujKomunikat("Wyglada na to ze nie ma zadnych zdjec do przegrania...");
            return null;
        }
    }
}
