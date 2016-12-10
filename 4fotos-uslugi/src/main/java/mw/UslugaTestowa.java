package mw;

import mw.bazowe.UslugiKonfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by mw on 10.12.16.
 */
@Component
public class UslugaTestowa {
    @Autowired
    private UslugiKonfigurator uslugiKonfigurator;

    @Autowired
    private DaoTestBean daoTestBean;

    public void uruchom(){
        System.out.println("uruchomiono");
    }
}
