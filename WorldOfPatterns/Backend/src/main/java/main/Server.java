package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import utils.Configs;

@SpringBootApplication
@ComponentScan("handlers")
public class Server {

    private static String INSUFFICIENT_ARGUMENTS = "Please specify a path to a config.properties file";

    public static void main(String[] args) {

        if(args.length != 1)
        {
            System.out.println(INSUFFICIENT_ARGUMENTS);
            return;
        }

        Configs.readConfig(args[0]);
        SpringApplication.run(Server.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }
}