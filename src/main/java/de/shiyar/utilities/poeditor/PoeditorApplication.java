package de.shiyar.utilities.poeditor;

import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PoeditorApplication {

    private static ConfigurableApplicationContext context;
    private static MariaDB4jSpringService mariaDB4j;

    public PoeditorApplication(MariaDB4jSpringService mariaDB4j) {
        PoeditorApplication.mariaDB4j = mariaDB4j;
    }

    public static void main(String[] args) {
        context = SpringApplication.run(PoeditorApplication.class, args);
    }

    public static void restart() {

        ApplicationArguments args = context.getBean(ApplicationArguments.class);

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mariaDB4j.stop();
            context.close();
            context = SpringApplication.run(PoeditorApplication.class, args.getSourceArgs());
        });

        thread.setDaemon(false);
        thread.start();
    }
}
