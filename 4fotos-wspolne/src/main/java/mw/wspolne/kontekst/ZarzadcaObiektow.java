package mw.wspolne.kontekst;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: Mariusz.Wojcik
 * Date: 19.02.14
 * Time: 23:10
 * To change this template use File | Settings | File Templates.
 */
public class ZarzadcaObiektow {
    //singleton
    private static ZarzadcaObiektow zarzadcaObiektow;
    private ApplicationContext context;
    //singleton


    private ZarzadcaObiektow() {
       context =
                new ClassPathXmlApplicationContext("mw/db/spring-beans.xml");
    }

    public static ZarzadcaObiektow podajInstancje() {

        if (zarzadcaObiektow == null) {
            zarzadcaObiektow = new ZarzadcaObiektow();

        }

        return zarzadcaObiektow;
    }

    public Object podajObiekt(String aNazwa){
        return context.getBean(aNazwa);
    }

    public KontekstPrzetwarzania podajKontekst(){
        return (KontekstPrzetwarzania)context.getBean("kontekst");
    }
}
