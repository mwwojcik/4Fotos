package mw.wspolne.wlasnosci;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by mw on 11.12.16.
 */
@Configuration
@ConfigurationProperties("fotos")
@PropertySource(value = {"application.properties"})
public class KonfiguratorAplikacji {
    private String testowa;


    public String getTestowa() {
        return testowa;
    }

    public void setTestowa(String testowa) {
        this.testowa = testowa;
    }
}
