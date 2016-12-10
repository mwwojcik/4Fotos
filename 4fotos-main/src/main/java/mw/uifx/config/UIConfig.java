package mw.uifx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mw on 16.08.16.
 */
@Configuration
@ComponentScan(basePackages = "mw")
public class UIConfig {

    @Bean
    public ExecutorService exec() {
        return Executors.newCachedThreadPool(runnable -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });
    }

}
