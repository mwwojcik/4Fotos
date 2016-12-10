package mw.wspolne.zdarzenia.bazowe;

import org.springframework.context.ApplicationEvent;

/**
 * Created by mw on 19.08.16.
 */
public class ZdarzenieBazowe extends ApplicationEvent {
    public ZdarzenieBazowe(Object source) {
        super(source);
    }
}
