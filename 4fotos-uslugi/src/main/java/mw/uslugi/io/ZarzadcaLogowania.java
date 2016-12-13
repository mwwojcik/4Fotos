package mw.uslugi.io;

/*import mw.gui.MonitorPaskaPostepu;
import mw.gui.MonitorPolaRaportu;*/
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Mariusz.Wojcik
 * Date: 2012-06-18
 * Time: 00:03:14
 * To change this template use File | Settings | File Templates.
 */
public class ZarzadcaLogowania {

    private ZarzadcaLogowania() {

        /*try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String pDataStr = format.format(new Date());

            String pStr="************* "+pDataStr+ "***************";

            //infoLogger.addAppender(new FileAppender(new PatternLayout("%d{HH:mm:ss,SSS} %r %-5p %c %x - %m%n"), "info.log",false));

            infoLogger.info(pStr);



        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }



    Logger infoLogger = Logger.getLogger("InfoLogger");


      //singleton
    private static ZarzadcaLogowania zarzadcaLogowania;
    //singleton

    public static synchronized ZarzadcaLogowania podajInstancje() {

        if (zarzadcaLogowania == null) {
            zarzadcaLogowania = new ZarzadcaLogowania();
        }

        return zarzadcaLogowania;
    }



    public synchronized   void logujNoweZadanie(){
        //noweZadanieLogger.info();
    }




    public synchronized   void logujKomunikat(String aKomunikat){
        infoLogger.info(aKomunikat);
        //System.out.println(aKomunikat);
        //MonitorPolaRaportu.podajInstancje().aktualizujStan(aKomunikat);
    }

    public synchronized   void wyczyscPole(){
        /*MonitorPolaRaportu.podajInstancje().wyczyscRaport();*/
    }
}
