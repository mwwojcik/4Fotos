package mw.wspolne.wlasnosci;

import lombok.Getter;
import lombok.Setter;
import mw.wspolne.model.GalerieWWW;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by mw on 11.12.16.
 */
@Configuration
@ConfigurationProperties("fotos")
@PropertySource(value = {"application.properties"})
@EnableConfigurationProperties
@Setter
@Getter
public class KonfiguratorAplikacji {

    private String katalogAplikacji;
    private String katalogZestawowZdjec;
    private String katalogAparatu;
    private String katalogUsunietych;



    private PodkatalogZestawuZdjec podkatalog;
    private ObszarRoboczy obszarRoboczy;
    private Galeria galeria;

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "podkatalog")
    public static class PodkatalogZestawuZdjec{
       private String raw;
       private String jpg;
       private String dousuniecia;
    }

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "obszarRoboczy")
    public static class ObszarRoboczy{
        private String cache;
    }

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "galeria")
    public static class Galeria{
        private String zrodlo;
        private String cel;
    }


}
