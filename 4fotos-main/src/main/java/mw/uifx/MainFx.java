package mw.uifx;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import mw.uifx.config.UIConfig;
import mw.uifx.kontekst.KontekstAplikacji;
import mw.uifx.wspolne.LokalizacjeFXMLEnum;
import mw.uifx.zasoby.FabrykaZasobow;
import mw.wspolne.wlasnosci.KonfiguratorAplikacji;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * Created by mw on 13.08.16.
 */
@SpringBootApplication
@ComponentScan("mw")
public class MainFx extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {

        /*AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(UIConfig.class);*/

        // Bootstrap Spring context here.
        ApplicationContext ctx = SpringApplication.run(MainFx.class);

        Font.loadFont(this.getClass().getResource("/mw/uifx/fonts/fontawesome-webfont.ttf").
                toExternalForm(), 12);


        ctx.getBean(KontekstAplikacji.class).setMainStage(primaryStage);
        FabrykaZasobow pFabrykaZasobow = ctx.getBean(FabrykaZasobow.class);
        pFabrykaZasobow.setKontekstSpringowy(ctx);

        KonfiguratorAplikacji pKonf=ctx.getBean(KonfiguratorAplikacji.class);
        System.out.println("katAplikacji=====>"+pKonf.getKatalogAplikacji());
        System.out.println("raw=====>"+pKonf.getPodkatalog().getRaw());
        System.out.println("raw=====>"+pKonf.getGaleria().getCel());


        Parent root = pFabrykaZasobow.podajObiektParentDlaZasobu(LokalizacjeFXMLEnum.ZASOBY_GLOWNE_WIDOK);
        Scene scene = new Scene(root, 1000, 900);
        pFabrykaZasobow.dodajStyleCSS(scene);
        primaryStage.setTitle("4Fotos");
        primaryStage.setScene(scene);

        // primaryStage.setAlwaysOnTop(true);
       // primaryStage.toFront();
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }


}
