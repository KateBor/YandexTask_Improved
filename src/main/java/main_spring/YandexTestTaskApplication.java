package main_spring;

import main_spring.web.ItemBasicController;
import main_spring.web.ItemExtraController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@ComponentScan(basePackages = {"main_spring.repository"})
@ComponentScan(basePackageClasses = ItemBasicController.class)
@ComponentScan(basePackageClasses = ItemExtraController.class)
public class YandexTestTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(YandexTestTaskApplication.class, args);
    }
}
