package mw;

import mw.bazowe.DaoKonfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/**
 * Created by mw on 10.12.16.
 */
@Component
public class DaoTestBean {

    public void uruchom(){
        System.out.println("uruchomiono dao" );
    }

}
