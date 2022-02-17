package org.authorizer;

import org.authorizer.core.config.ApplicationCoreProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationCoreProperties.class)
public class AuthorizerApplication {

    public static void main(String[] args) {

        SpringApplication.run(AuthorizerApplication.class, args);
    }
}
