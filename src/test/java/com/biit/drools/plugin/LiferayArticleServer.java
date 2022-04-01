package com.biit.drools.plugin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@SpringBootApplication
@Service
public class LiferayArticleServer {

    public static void main(String[] args) {
        SpringApplication.run(LiferayArticleServer.class, args);
    }

}
