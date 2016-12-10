package mw.wspolne.zdarzenia.publikacja;

import mw.wspolne.zdarzenia.bazowe.ZdarzenieBazowe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

/**
 * Created by mw on 19.08.16.
 */
@Component
public class PublikujacyZdarzeniaBean implements ApplicationEventPublisherAware {

    @Autowired
    public PublikujacyZdarzeniaBean(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = publisher;
    }

    public void publikujZdarzenie(ZdarzenieBazowe aZdarzenie){
        publisher.publishEvent(aZdarzenie);
    }
}
